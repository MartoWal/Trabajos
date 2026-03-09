import java.util.LinkedList;
import javax.swing.JOptionPane;

public class GestorInversiones {
    private static LinkedList<Inversion> inversiones = new LinkedList<>();
    
    public static void agregarInversion(Inversion inversion) {
        inversiones.add(inversion);
    }
    
    public static LinkedList<Inversion> getInversionesPorCliente(String mailCliente) {
        LinkedList<Inversion> inversionesCliente = new LinkedList<>();
        for (Inversion inversion : inversiones) {
            if (inversion.getCuenta().getCliente().getMail().equals(mailCliente)) {
                inversionesCliente.add(inversion);
            }
        }
        return inversionesCliente;
    }
    
    public static LinkedList<Inversion> getInversionesActivasPorCliente(String mailCliente) {
        LinkedList<Inversion> activas = new LinkedList<>();
        for (Inversion inversion : inversiones) {
            if (inversion.getCuenta().getCliente().getMail().equals(mailCliente) && 
                inversion.isActiva()) {
                activas.add(inversion);
            }
        }
        return activas;
    }
    
    public static Inversion buscarInversion(String id) {
        for (Inversion inversion : inversiones) {
            if (inversion.getId().equals(id)) {
                return inversion;
            }
        }
        return null;
    }
}