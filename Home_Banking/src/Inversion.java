import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Inversion {
    private String id;
    private Cuenta cuenta;
    private double montoInvertido;
    private double tasaInteresAnual; // En porcentaje, ej: 5.0 = 5%
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int diasTranscurridos;
    private boolean activa;
    
    public Inversion(Cuenta cuenta, double monto, double tasaInteresAnual, int diasDuracion) {
        this.id = generarId();
        this.cuenta = cuenta;
        this.montoInvertido = monto;
        this.tasaInteresAnual = tasaInteresAnual;
        this.fechaInicio = LocalDate.now();
        this.fechaFin = fechaInicio.plusDays(diasDuracion);
        this.diasTranscurridos = 0;
        this.activa = true;
    }
    
    private String generarId() {
        return "INV-" + System.currentTimeMillis();
    }
    
    // Getters
    public String getId() { return id; }
    public Cuenta getCuenta() { return cuenta; }
    public double getMontoInvertido() { return montoInvertido; }
    public double getTasaInteresAnual() { return tasaInteresAnual; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getDiasTranscurridos() { return diasTranscurridos; }
    public boolean isActiva() { return activa; }
    
    public double getInteresDiario() {
        return (tasaInteresAnual / 36500) * montoInvertido; // tasaAnual / 365 / 100
    }
    
    public double getInteresAcumulado() {
        return getInteresDiario() * diasTranscurridos;
    }
    
    public double getTotalActual() {
        return montoInvertido + getInteresAcumulado();
    }
    
    public int getDiasRestantes() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
    }
    
    // Simular un día
    public boolean simularUnDia() {
        if (!activa) return false;
        
        if (diasTranscurridos < ChronoUnit.DAYS.between(fechaInicio, fechaFin)) {
            diasTranscurridos++;
            return true;
        } else {
            activa = false;
            return false;
        }
    }
    
    // Simular varios días
    public boolean simularDias(int dias) {
        if (!activa) return false;
        
        int diasSimulados = 0;
        for (int i = 0; i < dias; i++) {
            if (simularUnDia()) {
                diasSimulados++;
            } else {
                break;
            }
        }
        return diasSimulados > 0;
    }
    
    // Retirar inversión (total o parcial)
    public double retirar(double monto) {
        if (!activa || monto <= 0) return 0;
        
        double montoDisponible = getTotalActual();
        double montoRetirado = Math.min(monto, montoDisponible);
        
        if (montoRetirado == montoDisponible) {
            // Retiro total
            activa = false;
            montoInvertido = 0;
        } else {
            // Retiro parcial
            double proporcion = montoRetirado / montoDisponible;
            montoInvertido -= montoInvertido * proporcion;
        }
        
        return montoRetirado;
    }
    
    // Retirar inversión completa
    public double retirarTotal() {
        return retirar(getTotalActual());
    }
    
    @Override
    public String toString() {
        return String.format("Inversion %s | Monto: $%.2f | Tasa: %.2f%% | Dias: %d/%d | Total: $%.2f",
            id.substring(0, 8), montoInvertido, tasaInteresAnual, 
            diasTranscurridos, ChronoUnit.DAYS.between(fechaInicio, fechaFin),
            getTotalActual());
    }
    
    public String getResumenDetallado() {
        StringBuilder sb = new StringBuilder();
        sb.append("RESUMEN DE INVERSION\n\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Cuenta: ").append(cuenta.getNumeroCuenta()).append("\n");
        sb.append("Fecha inicio: ").append(fechaInicio).append("\n");
        sb.append("Fecha fin: ").append(fechaFin).append("\n");
        sb.append("Dias transcurridos: ").append(diasTranscurridos).append("\n");
        sb.append("Dias restantes: ").append(getDiasRestantes()).append("\n");
        sb.append("Monto invertido: $").append(String.format("%.2f", montoInvertido)).append("\n");
        sb.append("Tasa anual: ").append(String.format("%.2f", tasaInteresAnual)).append("%\n");
        sb.append("Interes diario: $").append(String.format("%.2f", getInteresDiario())).append("\n");
        sb.append("Interes acumulado: $").append(String.format("%.2f", getInteresAcumulado())).append("\n");
        sb.append("Total actual: $").append(String.format("%.2f", getTotalActual())).append("\n");
        sb.append("Estado: ").append(activa ? "ACTIVA" : "FINALIZADA").append("\n");
        
        return sb.toString();
    }
}