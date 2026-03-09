import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Tarjeta {
    private String numero;
    private String tipo; // DEBITO, CREDITO
    private Cliente titular;
    private Cuenta cuentaAsociada;
    private LocalDate fechaVencimiento;
    private boolean activa;
    
    public Tarjeta(String numero, String tipo, Cliente titular, Cuenta cuentaAsociada) {
        this.numero = numero;
        this.tipo = tipo;
        this.titular = titular;
        this.cuentaAsociada = cuentaAsociada;
        this.fechaVencimiento = LocalDate.now().plusYears(3);
        this.activa = true;
    }
    
    // Getters
    public String getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public Cliente getTitular() { return titular; }
    public Cuenta getCuentaAsociada() { return cuentaAsociada; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isActiva() { return activa; }
    
    public void setActiva(boolean activa) { this.activa = activa; }
    
    // Metodos abstractos que deben implementar las subclases
    public abstract boolean agregarConsumo(String descripcion, double monto, int cuotas);
    public abstract boolean pagarResumen();
    public abstract String generarResumen();
    public abstract double getSaldoDisponible();
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return String.format(" %s [%s] - %s - Vence: %s", 
            tipo, formatoNumeroTarjeta(), titular.getMail(), fechaVencimiento.format(formatter));
    }
    
    private String formatoNumeroTarjeta() {
        // Mostrar solo los ultimos 4 digitos por seguridad
        return "**** **** **** " + numero.substring(12);
    }
    
    public String getNumeroCompleto() {
        // Formato: XXXX XXXX XXXX XXXX
        StringBuilder formato = new StringBuilder();
        for (int i = 0; i < numero.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formato.append(" ");
            }
            formato.append(numero.charAt(i));
        }
        return formato.toString();
    }
}