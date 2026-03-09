import java.util.LinkedList;
import javax.swing.JOptionPane;

public class TarjetaDebito extends Tarjeta {
    private LinkedList<Consumo> consumos;
    
    public TarjetaDebito(String numero, Cliente titular, Cuenta cuentaAsociada) {
        super(numero, "DEBITO", titular, cuentaAsociada);
        this.consumos = new LinkedList<>();
    }
    
    public LinkedList<Consumo> getConsumos() { return consumos; }
    
    @Override
    public double getSaldoDisponible() {
        return getCuentaAsociada().getSaldo();
    }
    
    @Override
    public boolean agregarConsumo(String descripcion, double monto, int cuotas) {
        if (!isActiva()) {
            JOptionPane.showMessageDialog(null, "La tarjeta no esta activa.");
            return false;
        }
        
        // En debito, el consumo se descuenta inmediatamente
        if (getCuentaAsociada().getSaldo() < monto) {
            JOptionPane.showMessageDialog(null, 
                "Saldo insuficiente en la cuenta asociada.\n" +
                "Monto del consumo: $" + String.format("%.2f", monto) + "\n" +
                "Saldo disponible: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
            return false;
        }
        
        // Realizar el retiro inmediato
        boolean retiroExitoso = getCuentaAsociada().retirar(monto);
        
        if (!retiroExitoso) {
            JOptionPane.showMessageDialog(null, "Error al procesar el consumo.");
            return false;
        }
        
        // Registrar consumo (siempre a 1 cuota en debito)
        Consumo nuevoConsumo = new Consumo(descripcion, monto, 1, this);
        consumos.add(nuevoConsumo);
        nuevoConsumo.marcarComoPagado(); // En debito se paga inmediatamente
        
        // Registrar movimiento
        Movimientos.registrarMovimiento(new Movimiento(getCuentaAsociada(), 
            Movimiento.TipoMovimiento.RETIRO, 
            monto, 
            "Consumo tarjeta débito: " + descripcion));
        
        JOptionPane.showMessageDialog(null, 
            "Consumo realizado exitosamente!\n" +
            "Descripcion: " + descripcion + "\n" +
            "Monto debitado: $" + String.format("%.2f", monto) + "\n" +
            "Nuevo saldo: $" + String.format("%.2f", getCuentaAsociada().getSaldo()));
        
        return true;
    }
    
    @Override
    public boolean pagarResumen() {
        JOptionPane.showMessageDialog(null, 
            "Las tarjetas de debito no generan resumen.\n" +
            "Los consumos se descuentan inmediatamente de su cuenta.");
        return true;
    }
    
    @Override
    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("RESUMEN TARJETA DEBITO \n\n");
        sb.append("Numero: ").append(getNumeroCompleto()).append("\n");
        sb.append("Cuenta asociada: ").append(getCuentaAsociada().getNumeroCuenta()).append("\n");
        sb.append("Saldo disponible: $").append(String.format("%.2f", getSaldoDisponible())).append("\n\n");
        
        if (consumos.isEmpty()) {
            sb.append("No hay consumos registrados en el ultimo mes.\n");
        } else {
            sb.append("ULTIMOS CONSUMOS \n");
            int count = 0;
            for (Consumo consumo : consumos) {
                if (count < 15) { // Mostrar solo los ultimos 15 consumos
                    sb.append(consumo.getFecha().toString()).append(" - ");
                    sb.append(consumo.getDescripcion()).append(" - ");
                    sb.append("$").append(String.format("%.2f", consumo.getMonto())).append("\n");
                    count++;
                }
            }
        }
        
        return sb.toString();
    }
}