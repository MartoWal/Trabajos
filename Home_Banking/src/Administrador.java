import java.time.LocalDate;
import javax.swing.JOptionPane;
import java.util.LinkedList;

public class Administrador extends Usuario {
    
    private LocalDate fechaInicio;

    public Administrador(String mail, String contr, LocalDate fechaInicio) {
        super(mail, contr, Rol.Administrador);
        this.fechaInicio = fechaInicio;
    }

    @Override
    public String toString() {
        return "Administrador [fechaInicio=" + fechaInicio + ", toString()=" + super.toString() + "]";
    }

    @Override
    public void Menu() {
        boolean salir = false;
        
        while (!salir) {
            int opcion = JOptionPane.showOptionDialog(null, 
                "Bienvenido Administrador " + this.getMail(), 
                "Menu Administrador", 
                0, 0, null, 
                this.getRol().getOpciones(), 
                this.getRol().getOpciones()[0]);
            
            switch (opcion) {
                case 0: // Crear cliente
                    crearCliente();
                    break;
                case 1: // Gestionar cuentas
                    gestionarCuentas();
                    break;
                case 2: // Ver cuentas
                    verCuentas();
                    break;
                case 3: // Ver transferencias
                    verTransferencias();
                    break;
                case 4: // Ver resumen
                    verResumen();
                    break;
                case 5: // Ver todos los movimientos
                    verTodosLosMovimientos();
                    break;
                case 6: // Salir
                    salir = true;
                    break;
                default:
                    salir = true;
                    break;
            }
        }
    }
    
    private void crearCliente() {
        // Solicitar datos del nuevo cliente
        String mail = JOptionPane.showInputDialog("Ingrese el email del nuevo cliente:");
        if (mail == null || mail.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email no valido. Operacion cancelada.");
            return;
        }
        
        // Verificar si el email ya existe
        if (emailExiste(mail)) {
            JOptionPane.showMessageDialog(null, "El email ya esta registrado en el sistema.");
            return;
        }
        
        String contr = JOptionPane.showInputDialog("Ingrese la contraseña del nuevo cliente:");
        if (contr == null || contr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Contraseña no valida. Operacion cancelada.");
            return;
        }
        
        // Crear el nuevo cliente
        Cliente nuevoCliente = new Cliente(mail, contr);
        Usuario.getUsuarios().add(nuevoCliente);
        
        // Crear cuenta inicial para el nuevo cliente
        crearCuentaParaCliente(nuevoCliente, true);
        
        JOptionPane.showMessageDialog(null, 
            "Cliente creado exitosamente!\n" +
            "Email: " + mail + "\n" +
            "Contraseña: " + contr);
    }
    
    private void crearCuentaParaCliente(Cliente cliente, boolean esCuentaInicial) {
        // Generar numero de cuenta automaticamente
        String numeroCuenta = generarNumeroCuenta();
        
        double saldoInicial = 0.0;
        
        if (esCuentaInicial) {
            // Solicitar saldo inicial solo para la primera cuenta
            String saldoStr = JOptionPane.showInputDialog(
                "Ingrese el saldo inicial para la cuenta del cliente " + cliente.getMail() + ":\n" +
                "Numero de cuenta asignado: " + numeroCuenta);
            
            try {
                saldoInicial = Double.parseDouble(saldoStr);
                if (saldoInicial < 0) {
                    JOptionPane.showMessageDialog(null, "El saldo inicial no puede ser negativo. Se asignara $0.0");
                    saldoInicial = 0.0;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Saldo no valido. Se creara la cuenta con saldo $0.0");
                saldoInicial = 0.0;
            }
        }
        
        // Crear la cuenta
        Cuenta nuevaCuenta = new Cuenta(numeroCuenta, saldoInicial, cliente);
        cliente.agregarCuenta(nuevaCuenta);
        
        JOptionPane.showMessageDialog(null, 
            "Cuenta creada exitosamente!\n" +
            "Numero de cuenta: " + numeroCuenta + "\n" +
            "Saldo inicial: $" + String.format("%.2f", saldoInicial) + "\n" +
            "Cliente: " + cliente.getMail());
    }
    
    private void gestionarCuentas() {
        String[] opcionesGestion = {"Crear cuenta para cliente", "Dar de baja cuenta", "Volver"};
        
        int opcion = JOptionPane.showOptionDialog(null,
            "Gestion de Cuentas",
            "Gestionar Cuentas",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opcionesGestion,
            opcionesGestion[0]);
        
        switch (opcion) {
            case 0: // Crear cuenta para cliente
                crearCuentaParaClienteExistente();
                break;
            case 1: // Dar de baja cuenta
                darDeBajaCuenta();
                break;
            // case 2: Volver - no hace nada
        }
    }
    
    private void crearCuentaParaClienteExistente() {
        // Obtener lista de clientes
        LinkedList<Cliente> clientes = obtenerClientes();
        
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clientes registrados en el sistema.");
            return;
        }
        
        
        String[] opciones = new String[clientes.size()];
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            opciones[i] = cliente.getMail() + " - Cuentas: " + cliente.getCuentas().size();
        }
        
        int seleccion = JOptionPane.showOptionDialog(null, 
            "Seleccione el cliente para crear una nueva cuenta:", 
            "Crear Cuenta Adicional",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion >= 0) {
            Cliente clienteSeleccionado = clientes.get(seleccion);
            crearCuentaParaCliente(clienteSeleccionado, false);
        }
    }
    
    private void darDeBajaCuenta() {
        // Obtener todas las cuentas del sistema
        LinkedList<Cuenta> cuentas = Cuenta.getCuentas();
        
        if (cuentas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay cuentas registradas en el sistema.");
            return;
        }
        
      
        String[] opciones = new String[cuentas.size()];
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta cuenta = cuentas.get(i);
            opciones[i] = "Cuenta: " + cuenta.getNumeroCuenta() + 
                         " | Cliente: " + cuenta.getCliente().getMail() + 
                         " | Saldo: $" + String.format("%.2f", cuenta.getSaldo());
        }
        
        int seleccion = JOptionPane.showOptionDialog(null, 
            "Seleccione la cuenta a dar de baja:", 
            "Dar de baja cuenta",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion >= 0) {
            Cuenta cuentaSeleccionada = cuentas.get(seleccion);
            
            // Verificar que la cuenta tenga saldo 0
            if (cuentaSeleccionada.getSaldo() > 0) {
                JOptionPane.showMessageDialog(null, 
                    "No se puede dar de baja una cuenta con saldo positivo.\n" +
                    "Saldo actual: $" + String.format("%.2f", cuentaSeleccionada.getSaldo()) + "\n" +
                    "Transfiera o retire el saldo antes de dar de baja la cuenta.");
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Esta seguro de dar de baja la cuenta?\n" +
                "Numero de cuenta: " + cuentaSeleccionada.getNumeroCuenta() + "\n" +
                "Cliente: " + cuentaSeleccionada.getCliente().getMail() + "\n" +
                "Saldo: $" + String.format("%.2f", cuentaSeleccionada.getSaldo()),
                "Confirmar baja de cuenta",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Eliminar la cuenta del cliente y del sistema
                Cliente cliente = cuentaSeleccionada.getCliente();
                cliente.getCuentas().remove(cuentaSeleccionada);
                cuentas.remove(cuentaSeleccionada);
                
                JOptionPane.showMessageDialog(null, 
                    "Cuenta dada de baja exitosamente.\n" +
                    "El cliente " + cliente.getMail() + " ahora tiene " + 
                    cliente.getCuentas().size() + " cuenta(s).");
            }
        }
    }
    
    		private LinkedList<Cliente> obtenerClientes() {
    			LinkedList<Cliente> clientes = new LinkedList<>();
    			for (Usuario usuario : Usuario.getUsuarios()) {
    				if (usuario instanceof Cliente) {
    					clientes.add((Cliente) usuario);
    				}
    			}
    			return clientes;
    }
    
    private String generarNumeroCuenta() {
        // Generar un numero de cuenta unico secuencial
        int numero = Cuenta.getCuentas().size() + 1;
        return String.format("%03d", numero);
    }
    
    private boolean emailExiste(String email) {
        for (Usuario usuario : Usuario.getUsuarios()) {
            if (usuario.getMail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
    
    private void verCuentas() {
        StringBuilder sb = new StringBuilder();
        sb.append(" TODAS LAS CUENTAS \n\n");
        
        if (Cuenta.getCuentas().isEmpty()) {
            sb.append("No hay cuentas registradas en el sistema.");
        } else {
            for (Cuenta cuenta : Cuenta.getCuentas()) {
                sb.append("Cuenta: ").append(cuenta.getNumeroCuenta())
                  .append("Cliente: ").append(cuenta.getCliente().getMail())
                  .append("Saldo: $").append(String.format("%.2f", cuenta.getSaldo()))
                  .append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private void verTransferencias() {
        StringBuilder sb = new StringBuilder();
        sb.append(" HISTORIAL DE TRANSFERENCIAS \n\n");
        
        if (Cuenta.getTransferencias().isEmpty()) {
            sb.append("No hay transferencias registradas.");
        } else {
            for (Transferencia transferencia : Cuenta.getTransferencias()) {
                sb.append(transferencia.toString()).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private void verResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append(" RESUMEN DEL SISTEMA \n\n");
        sb.append("Total de cuentas: ").append(Cuenta.getCuentas().size()).append("\n");
        sb.append("Total de transferencias: ").append(Cuenta.getTransferencias().size()).append("\n");
        
        int totalClientes = 0;
        int totalCuentasActivas = 0;
        for (Usuario usuario : Usuario.getUsuarios()) {
            if (usuario instanceof Cliente) {
                totalClientes++;
                Cliente cliente = (Cliente) usuario;
                totalCuentasActivas += cliente.getCuentas().size();
            }
        }
        
        sb.append("Total de clientes: ").append(totalClientes).append("\n");
        sb.append("Total de cuentas activas: ").append(totalCuentasActivas).append("\n");
        sb.append("Total de movimientos: ").append(Movimientos.getTodosLosMovimientos().size()).append("\n\n");
        
        double saldoTotal = 0;
        for (Cuenta cuenta : Cuenta.getCuentas()) {
            saldoTotal += cuenta.getSaldo();
        }
        sb.append("Saldo total en el sistema: $").append(String.format("%.2f", saldoTotal));
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private void verTodosLosMovimientos() {
        LinkedList<Movimiento> movimientos = Movimientos.getTodosLosMovimientos();
        
        if (movimientos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay movimientos registrados en el sistema");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" TODOS LOS MOVIMIENTOS DEL SISTEMA \n\n");
        
        for (Movimiento movimiento : movimientos) {
            sb.append(movimiento.toString()).append("\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}