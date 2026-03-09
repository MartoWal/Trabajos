import java.util.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cuenta {
    private static LinkedList<Cuenta> cuentas = new LinkedList<Cuenta>();
    private static LinkedList<Transferencia> transferencias = new LinkedList<Transferencia>();
    
    private String numeroCuenta;
    private double saldo;
    private Cliente cliente;
    
    public Cuenta(String numeroCuenta, double saldoInicial, Cliente cliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.cliente = cliente;
        cuentas.add(this);
        
        // Registrar el deposito inicial como movimiento
        Movimientos.registrarMovimiento(new Movimiento(this, 
            Movimiento.TipoMovimiento.DEPOSITO, 
            saldoInicial, 
            "Apertura de cuenta"));
    }
    
    // Getters y Setters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public static LinkedList<Cuenta> getCuentas() {
        return cuentas;
    }
    
    public static LinkedList<Transferencia> getTransferencias() {
        return transferencias;
    }
    public boolean puedeEliminarse() {
        return this.saldo == 0.0;
    }
    
    // Metodos de operaciones
    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            // Registrar movimiento
            Movimientos.registrarMovimiento(new Movimiento(this, 
                Movimiento.TipoMovimiento.DEPOSITO, 
                monto, 
                "Deposito en cuenta"));
            return true;
        }
        return false;
    }
    
    public boolean retirar(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            // Registrar movimiento
            Movimientos.registrarMovimiento(new Movimiento(this, 
                Movimiento.TipoMovimiento.RETIRO, 
                monto, 
                "Retiro de cuenta"));
            return true;
        }
        return false;
    }
    
    public boolean transferir(Cuenta cuentaDestino, double monto) {
        if (monto > 0 && saldo >= monto && cuentaDestino != null && cuentaDestino != this) {
            this.retirar(monto);
            cuentaDestino.depositar(monto);
            
            // Registrar la transferencia
            Transferencia transferencia = new Transferencia(this, cuentaDestino, monto);
            transferencias.add(transferencia);
            
            // Registrar movimientos especificos de transferencia
            Movimientos.registrarMovimiento(new Movimiento(this, 
                Movimiento.TipoMovimiento.TRANSFERENCIA_ENVIADA, 
                monto, 
                "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta()));
            
            Movimientos.registrarMovimiento(new Movimiento(cuentaDestino, 
                Movimiento.TipoMovimiento.TRANSFERENCIA_RECIBIDA, 
                monto, 
                "Transferencia de cuenta " + this.getNumeroCuenta()));
            
            return true;
        }
        return false;
    }
    
    public static Cuenta buscarCuenta(String numeroCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Cuenta [numeroCuenta=" + numeroCuenta + ", saldo=" + saldo + ", cliente=" + cliente.getMail() + "]";
    }
}

// Clase para registrar transferencias
class Transferencia {
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private double monto;
    private LocalDateTime fechaHora;
    
    public Transferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
    }
    
    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }
    
    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }
    
    public double getMonto() {
        return monto;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("Transferencia: %s -> %s | Monto: $%.2f | Fecha: %s", 
            cuentaOrigen.getNumeroCuenta(), 
            cuentaDestino.getNumeroCuenta(), 
            monto, 
            fechaHora.format(formatter));
    }
}