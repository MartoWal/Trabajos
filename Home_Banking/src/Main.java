import java.time.LocalDate;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuInicio();
    }
    
    private static void inicializarSistema() {
        // Crear solo el administrador inicial
        // Los clientes se crearan dinamicamente a traves del administrador
        Usuario.getUsuarios().add(new Administrador("admin", "admin123", LocalDate.now()));
        
        JOptionPane.showMessageDialog(null, 
            "Sistema inicializado\n" +
            "Credenciales de administrador:\n" +
            "Email: admin\n" +
            "Contraseña: admin123");
    }
    
    private static void mostrarMenuInicio() {
        boolean salir = false;
        
        while (!salir) {
            String[] opciones = {"Login", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null,
                "HOME BANKING \n Bienvenido al sistema bancario",
                "Menú Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.DEFAULT_OPTION,
                null,
                opciones,
                opciones[0]);
            
            switch (opcion) {
                case 0: // Login
                    realizarLogin();
                    break;
                case 1: // Salir
                default:
                    salir = true;
                    JOptionPane.showMessageDialog(null, "Gracias por usar nuestro sistema!");
                    break;
            }
        }
    }
    
    private static void realizarLogin() {
        String mail = JOptionPane.showInputDialog("Ingrese su Usuario:");
        if (mail == null) return; // Si presiona cancelar
        
        String contr = JOptionPane.showInputDialog("Ingrese su Contraseña:");
        if (contr == null) return;
        
        Usuario usuarioLogueado = Usuario.Login(mail, contr);
        
        if (usuarioLogueado != null) {
            JOptionPane.showMessageDialog(null, "Login exitoso!\n Bienvenido: " + mail);
            usuarioLogueado.Menu();
        } else {
            JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos");
        }
    }
}