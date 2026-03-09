import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

	public class Movimientos {
	    private static LinkedList<Movimiento> movimientos = new LinkedList<Movimiento>();
	    
	    public static void registrarMovimiento(Movimiento movimiento) {
	        movimientos.add(movimiento);
	    }
	    
	    public static LinkedList<Movimiento> getMovimientosPorCliente(String mailCliente) {
	        LinkedList<Movimiento> movimientosCliente = new LinkedList<Movimiento>();
	        for (Movimiento movimiento : movimientos) {
	            if (movimiento.getCuenta().getCliente().getMail().equals(mailCliente)) {
	                movimientosCliente.add(movimiento);
	            }
	        }
	        return movimientosCliente;
	    }
	    
	    public static LinkedList<Movimiento> getTodosLosMovimientos() {
	        return new LinkedList<Movimiento>(movimientos);
	    }
	    
	    public static LinkedList<Movimiento> getMovimientosPorCuenta(String numeroCuenta) {
	        LinkedList<Movimiento> movimientosCuenta = new LinkedList<Movimiento>();
	        for (Movimiento movimiento : movimientos) {
	            if (movimiento.getCuenta().getNumeroCuenta().equals(numeroCuenta)) {
	                movimientosCuenta.add(movimiento);
	            }
	        }
	        return movimientosCuenta;
	    }
	}

	class Movimiento {
	    public enum TipoMovimiento {
	        DEPOSITO, RETIRO, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECIBIDA
	    }
	    
	    private Cuenta cuenta;
	    private TipoMovimiento tipo;
	    private double monto;
	    private LocalDateTime fechaHora;
	    private String descripcion;
	    
	    public Movimiento(Cuenta cuenta, TipoMovimiento tipo, double monto, String descripcion) {
	        this.cuenta = cuenta;
	        this.tipo = tipo;
	        this.monto = monto;
	        this.fechaHora = LocalDateTime.now();
	        this.descripcion = descripcion;
	    }
	    
	    // Getters
	    public Cuenta getCuenta() {
	        return cuenta;
	    }
	    
	    public TipoMovimiento getTipo() {
	        return tipo;
	    }
	    
	    public double getMonto() {
	        return monto;
	    }
	    
	    public LocalDateTime getFechaHora() {
	        return fechaHora;
	    }
	    
	    public String getDescripcion() {
	        return descripcion;
	    }
	    
	 
	    @Override
	    public String toString() {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	        String signo = "";
	        
	        switch (tipo) {
	            case DEPOSITO:
	                signo = "+";
	                break;
	            case RETIRO:	           
	                signo = "-";
	                break;
	            case TRANSFERENCIA_ENVIADA:	           
	                signo = "-";
	                break;
	            case TRANSFERENCIA_RECIBIDA:	             
	                signo = "+";
	                break;
	        }
	        
	        return String.format("%s %s$%.2f | %s | Cuenta: %s", 
	            fechaHora.format(formatter),
	            signo,
	            monto,
	            descripcion,
	            cuenta.getNumeroCuenta());
	    }
	}

