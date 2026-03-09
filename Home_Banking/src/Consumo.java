import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Consumo {
    private String descripcion;
    private double monto;
    private LocalDate fecha;
    private int cuotas;
    private int cuotasPagadas;
    private boolean pagado;
    private Tarjeta tarjeta;
    
    public Consumo(String descripcion, double monto, int cuotas, Tarjeta tarjeta) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = LocalDate.now();
        this.cuotas = cuotas;
        this.cuotasPagadas = 0;
        this.pagado = false;
        this.tarjeta = tarjeta;
    }
    
    // Getters
    public String getDescripcion() { return descripcion; }
    public double getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public int getCuotas() { return cuotas; }
    public int getCuotasPagadas() { return cuotasPagadas; }
    public boolean isPagado() { return pagado; }
    public Tarjeta getTarjeta() { return tarjeta; }
    
    public double getMontoPorCuota() {
        return monto / cuotas;
    }
    
    public double getSaldoPendiente() {
        if (pagado) return 0.0;
        return monto - (getMontoPorCuota() * cuotasPagadas);
    }
    
    public void marcarComoPagado() {
        this.pagado = true;
        this.cuotasPagadas = this.cuotas;
    }
    
    public void pagarCuota() {
        if (cuotasPagadas < cuotas) {
            cuotasPagadas++;
            if (cuotasPagadas == cuotas) {
                pagado = true;
            }
        }
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String estado = pagado ? "PAGADO" : "PENDIENTE";
        
        if (cuotas > 1) {
            return String.format("%s | $%.2f | %d cuotas de $%.2f | %d/%d pagadas | %s", 
                descripcion, monto, cuotas, getMontoPorCuota(), cuotasPagadas, cuotas, estado);
        } else {
            return String.format("%s | $%.2f | %s", descripcion, monto, estado);
        }
    }
}