import java.util.LinkedList;
import javax.swing.JOptionPane;

public class TarjetaCredito extends Tarjeta {
    private double limite;
    private double saldoUtilizado;
    private LinkedList<Consumo> consumos;
    
    public TarjetaCredito(String numero, Cliente titular, Cuenta cuentaAsociada, double limite) {
        super(numero, "CREDITO", titular, cuentaAsociada);
        this.limite = limite;
        this.saldoUtilizado = 0.0;
        this.consumos = new LinkedList<>();
    }
    
    public double getLimite() { return limite; }
    public double getSaldoUtilizado() { return saldoUtilizado; }
    public LinkedList<Consumo> getConsumos() { return consumos; }
    
    @Override
    public double getSaldoDisponible() {
        return limite - saldoUtilizado;
    }
    
    @Override
    public boolean agregarConsumo(String descripcion, double monto, int cuotas) {
        if (!isActiva()) {
            JOptionPane.showMessageDialog(null, "La tarjeta no esta activa.");
            return false;
        }
        
        if (saldoUtilizado + monto > limite) {
            JOptionPane.showMessageDialog(null, 
                "Limite de credito excedido.\n" +
                "Monto del consumo: $" + String.format("%.2f", monto) + "\n" +
                "Saldo disponible: $" + String.format("%.2f", getSaldoDisponible()));
            return false;
        }
        
        saldoUtilizado += monto;
        Consumo nuevoConsumo = new Consumo(descripcion, monto, cuotas, this);
        consumos.add(nuevoConsumo);
        
        JOptionPane.showMessageDialog(null, 
            "Consumo agregado exitosamente!\n" +
            "Descripcion: " + descripcion + "\n" +
            "Monto total: $" + String.format("%.2f", monto) + "\n" +
            "Cuotas: " + cuotas + " de $" + String.format("%.2f", nuevoConsumo.getMontoPorCuota()) + "\n" +
            "Nuevo saldo utilizado: $" + String.format("%.2f", saldoUtilizado) + "\n" +
            "Saldo disponible: $" + String.format("%.2f", getSaldoDisponible()));
        
        return true;
    }
    
    // Nuevo metodo para pagar cuotas individuales
    public boolean pagarCuota(Consumo consumo) {
        if (consumo.isPagado()) {
            JOptionPane.showMessageDialog(null, "Este consumo ya esta completamente pagado.");
            return true;
        }
        
        double montoCuota = consumo.getMontoPorCuota();
        
        // Verificar que la cuenta asociada tenga saldo suficiente
        if (getCuentaAsociada().getSaldo() < montoCuota) {
            JOptionPane.showMessageDialog(null, 
                "Saldo insuficiente en la cuenta asociada.\n" +
                "Monto de la cuota: $" + String.format("%.2f", montoCuota) + "\n" +
                "Saldo disponible: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
            return false;
        }
        
        // Realizar el pago de la cuota
        getCuentaAsociada().retirar(montoCuota);
        consumo.pagarCuota();
        
        // Actualizar el saldo utilizado
        saldoUtilizado -= montoCuota;
        
        // Registrar el movimiento
        Movimientos.registrarMovimiento(new Movimiento(getCuentaAsociada(), 
            Movimiento.TipoMovimiento.RETIRO, 
            montoCuota, 
            "Pago cuota tarjeta credito: " + consumo.getDescripcion()));
        
        JOptionPane.showMessageDialog(null, 
            "Cuota pagada exitosamente!\n" +
            "Descripcion: " + consumo.getDescripcion() + "\n" +
            "Cuota: " + consumo.getCuotasPagadas() + "/" + consumo.getCuotas() + "\n" +
            "Monto: $" + String.format("%.2f", montoCuota) + "\n" +
            "Saldo pendiente: $" + String.format("%.2f", consumo.getSaldoPendiente()) + "\n" +
            "Nuevo saldo en cuenta: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
        
        return true;
    }
    
    // Metodo para pagar el consumo completo
    public boolean pagarConsumoCompleto(Consumo consumo) {
        if (consumo.isPagado()) {
            JOptionPane.showMessageDialog(null, "Este consumo ya esta completamente pagado.");
            return true;
        }
        
        double saldoPendiente = consumo.getSaldoPendiente();
        
        // Verificar que la cuenta asociada tenga saldo suficiente
        if (getCuentaAsociada().getSaldo() < saldoPendiente) {
            JOptionPane.showMessageDialog(null, 
                "Saldo insuficiente en la cuenta asociada.\n" +
                "Saldo pendiente: $" + String.format("%.2f", saldoPendiente) + "\n" +
                "Saldo disponible: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
            return false;
        }
        
        // Realizar el pago completo
        getCuentaAsociada().retirar(saldoPendiente);
        consumo.marcarComoPagado();
        
        // Actualizar el saldo utilizado
        saldoUtilizado -= saldoPendiente;
        
        // Registrar el movimiento
        Movimientos.registrarMovimiento(new Movimiento(getCuentaAsociada(), 
            Movimiento.TipoMovimiento.RETIRO, 
            saldoPendiente, 
            "Pago completo consumo: " + consumo.getDescripcion()));
        
        JOptionPane.showMessageDialog(null, 
            "Consumo pagado completamente!\n" +
            "Descripcion: " + consumo.getDescripcion() + "\n" +
            "Monto total: $" + String.format("%.2f", saldoPendiente) + "\n" +
            "Nuevo saldo en cuenta: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
        
        return true;
    }
    
    @Override
    public boolean pagarResumen() {
        // Buscar consumos pendientes
        LinkedList<Consumo> consumosPendientes = new LinkedList<>();
        for (Consumo consumo : consumos) {
            if (!consumo.isPagado()) {
                consumosPendientes.add(consumo);
            }
        }
        
        if (consumosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene consumos pendientes de pago.");
            return true;
        }
        
        // Calcular total pendiente
        double totalPendiente = 0;
        for (Consumo consumo : consumosPendientes) {
            totalPendiente += consumo.getSaldoPendiente();
        }
        
        // Verificar que la cuenta asociada tenga saldo suficiente
        if (getCuentaAsociada().getSaldo() < totalPendiente) {
            JOptionPane.showMessageDialog(null, 
                "Saldo insuficiente en la cuenta asociada para pagar todo el resumen.\n" +
                "Total pendiente: $" + String.format("%.2f", totalPendiente) + "\n" +
                "Saldo disponible: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
            return false;
        }
        
        // Realizar el pago de todos los consumos pendientes
        for (Consumo consumo : consumosPendientes) {
            double saldoPendiente = consumo.getSaldoPendiente();
            getCuentaAsociada().retirar(saldoPendiente);
            consumo.marcarComoPagado();
            saldoUtilizado -= saldoPendiente;
            
            Movimientos.registrarMovimiento(new Movimiento(getCuentaAsociada(), 
                Movimiento.TipoMovimiento.RETIRO, 
                saldoPendiente, 
                "Pago resumen consumo: " + consumo.getDescripcion()));
        }
        
        JOptionPane.showMessageDialog(null, 
            "Resumen pagado completamente!\n" +
            "Total pagado: $" + String.format("%.2f", totalPendiente) + "\n" +
            "Nuevo saldo en cuenta: $" + String.format("%.2f", getCuentaAsociada().getSaldo()) + "\n" +
            "Limite de credito disponible: $" + String.format("%.2f", getSaldoDisponible()));
        
        return true;
    }
    
    @Override
    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("RESUMEN TARJETA CREDITO \n\n");
        sb.append("Numero: ").append(getNumeroCompleto()).append("\n");
        sb.append("Limite: $").append(String.format("%.2f", limite)).append("\n");
        sb.append("Saldo utilizado: $").append(String.format("%.2f", saldoUtilizado)).append("\n");
        sb.append("Saldo disponible: $").append(String.format("%.2f", getSaldoDisponible())).append("\n\n");
        
        if (consumos.isEmpty()) {
            sb.append("No hay consumos registrados.\n");
        } else {
            sb.append("CONSUMOS PENDIENTES \n");
            boolean hayPendientes = false;
            for (Consumo consumo : consumos) {
                if (!consumo.isPagado()) {
                    sb.append(consumo.toString()).append("\n");
                    hayPendientes = true;
                }
            }
            if (!hayPendientes) {
                sb.append("No hay consumos pendientes.\n");
            }
            
            sb.append("\n CONSUMOS PAGADOS \n");
            boolean hayPagados = false;
            for (Consumo consumo : consumos) {
                if (consumo.isPagado()) {
                    sb.append(consumo.toString()).append("\n");
                    hayPagados = true;
                }
            }
            if (!hayPagados) {
                sb.append("No hay consumos pagados.\n");
            }
        }
        
        return sb.toString();
    }
    
    // Metodo para obtener consumos pendientes
    public LinkedList<Consumo> getConsumosPendientes() {
        LinkedList<Consumo> pendientes = new LinkedList<>();
        for (Consumo consumo : consumos) {
            if (!consumo.isPagado()) {
                pendientes.add(consumo);
            }
        }
        return pendientes;
    }
}