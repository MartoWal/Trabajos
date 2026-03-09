import javax.swing.JOptionPane;
import java.util.LinkedList;

public class Cliente extends Usuario {
    private LinkedList<Cuenta> cuentas;
    private LinkedList<Tarjeta> tarjetas;
    
    public Cliente(String mail, String contr) {
        super(mail, contr, Rol.Cliente);
        this.cuentas = new LinkedList<Cuenta>();
        this.tarjetas = new LinkedList<Tarjeta>();
    }
    
    public LinkedList<Cuenta> getCuentas() {
        return cuentas;
    }
    
    public LinkedList<Tarjeta> getTarjetas() {
        return tarjetas;
    }
    
    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }
    
    public void agregarTarjeta(Tarjeta tarjeta) {
        tarjetas.add(tarjeta);
    }
    
    @Override
    public void Menu() {
        boolean salir = false;
        
        while (!salir) {
            int opcion = JOptionPane.showOptionDialog(null, 
                "Bienvenido cliente " + this.getMail() + 
                "\nCuentas activas: " + this.cuentas.size() +
                "\nTarjetas: " + this.tarjetas.size(), 
                "Menu Cliente", 
                0, 0, null, 
                this.getRol().getOpciones(), 
                this.getRol().getOpciones()[0]);
            
            switch (opcion) {
            case 0: // Transferir
                realizarTransferencia();
                break;
            case 1: // Consultar movimientos
                consultarMisMovimientos();
                break;
            case 2: // Consultar cuentas
                consultarMisCuentas();
                break;
            case 3: // Gestionar tarjetas
                gestionarTarjetas();
                break;
            case 4: // Pedir tarjetas
                pedirTarjetas();
                break;
            case 5: // Invertir
                gestionarInversiones();
                break;
            case 6: // Eliminar cuentas
                eliminarMisCuentas();
                break;
            case 7: // Salir
                salir = true;
                JOptionPane.showMessageDialog(null, "¡Hasta pronto!");
                break;
            default:
                salir = true;
                break;
        }
        }
    }
    
    private void realizarTransferencia() {
        if (cuentas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene cuentas asociadas para realizar transferencias.");
            return;
        }
        
        // Seleccionar cuenta de origen
        Cuenta cuentaOrigen = seleccionarCuenta("Seleccione cuenta de origen para la transferencia:");
        if (cuentaOrigen == null) return;
        
        // Ingresar cuenta destino
        String numeroCuentaDestino = JOptionPane.showInputDialog("Ingrese numero de cuenta destino:");
        if (numeroCuentaDestino == null || numeroCuentaDestino.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Numero de cuenta no valido.");
            return;
        }
        
        Cuenta cuentaDestino = Cuenta.buscarCuenta(numeroCuentaDestino.trim());
        
        if (cuentaDestino == null) {
            JOptionPane.showMessageDialog(null, "Cuenta destino no encontrada.");
            return;
        }
        
        if (cuentaOrigen == cuentaDestino) {
            JOptionPane.showMessageDialog(null, "No puede transferir a la misma cuenta.");
            return;
        }
        
        // Ingresar monto
        String montoStr = JOptionPane.showInputDialog(
            "Ingrese monto a transferir:\n" +
            "Saldo disponible: $" + String.format("%.2f", cuentaOrigen.getSaldo()));
        
        try {
            double monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(null, "El monto debe ser mayor a cero.");
                return;
            }
            
            if (cuentaOrigen.transferir(cuentaDestino, monto)) {
                JOptionPane.showMessageDialog(null, 
                    "Transferencia exitosa!\n" +
                    "Monto: $" + String.format("%.2f", monto) + "\n" +
                    "De: Cuenta " + cuentaOrigen.getNumeroCuenta() + "\n" +
                    "A: Cuenta " + cuentaDestino.getNumeroCuenta() + "\n" +
                    "Nuevo saldo: $" + String.format("%.2f", cuentaOrigen.getSaldo()));
            } else {
                JOptionPane.showMessageDialog(null, " Error en la transferencia. Verifique que el monto no exceda su saldo disponible.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, " Monto invalido. Ingrese un numero valido.");
        }
    }
    
    private void consultarMisMovimientos() {
        LinkedList<Movimiento> movimientos = Movimientos.getMovimientosPorCliente(this.getMail());
        
        if (movimientos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay movimientos registrados en sus cuentas.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("MIS MOVIMIENTOS \n\n");
        
        for (Movimiento movimiento : movimientos) {
            sb.append(" ").append(movimiento.toString()).append("\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Mis Movimientos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void consultarMisCuentas() {
        if (cuentas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene cuentas asociadas.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" MIS CUENTAS \n\n");
        
        double saldoTotal = 0;
        for (Cuenta cuenta : cuentas) {
            sb.append(" Cuenta: ").append(cuenta.getNumeroCuenta())
              .append(" Saldo: $").append(String.format("%.2f", cuenta.getSaldo()))
              .append("\n");
            saldoTotal += cuenta.getSaldo();
        }
        
        sb.append("\n Saldo total: $").append(String.format("%.2f", saldoTotal));
        sb.append("\n Total de cuentas: ").append(cuentas.size());
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Mis Cuentas", JOptionPane.INFORMATION_MESSAGE);
    }
    
   
    private void eliminarMisCuentas() {
        if (cuentas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tiene cuentas para eliminar.");
            return;
        }
        
     
        LinkedList<Cuenta> cuentasEliminables = new LinkedList<>();
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getSaldo() == 0.0) {
                cuentasEliminables.add(cuenta);
            }
        }
        
        if (cuentasEliminables.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No tiene cuentas eliminables.\n" +
                "Solo puede eliminar cuentas con saldo $0.00.\n" +
                "Transfiera o retire el saldo de sus cuentas primero.");
            return;
        }
      
        if (cuentasEliminables.size() == cuentas.size() && cuentas.size() > 1) {
            JOptionPane.showMessageDialog(null, 
                "Debe mantener al menos una cuenta activa.\n" +
                "No puede eliminar todas sus cuentas.");
            return;
        }
        
        String[] opciones = new String[cuentasEliminables.size()];
        for (int i = 0; i < cuentasEliminables.size(); i++) {
            Cuenta cuenta = cuentasEliminables.get(i);
            opciones[i] = "Cuenta: " + cuenta.getNumeroCuenta() + " | Saldo: $" + String.format("%.2f", cuenta.getSaldo());
        }
        
        int seleccion = JOptionPane.showOptionDialog(null, 
            "Seleccione la cuenta a eliminar (solo cuentas con saldo $0.00):", 
            "Eliminar Cuenta",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.DEFAULT_OPTION,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion >= 0) {
            Cuenta cuentaAEliminar = cuentasEliminables.get(seleccion);
            
            Object[] eliminacion = {"SI", "NO"};
            int confirmacion = JOptionPane.showOptionDialog(null,
                "¿Esta seguro de eliminar la cuenta?\n" +
                "Numero: " + cuentaAEliminar.getNumeroCuenta() + "\n" +
                "Saldo: $" + String.format("%.2f", cuentaAEliminar.getSaldo()) + "\n\n" +
                " Esta accion no se puede deshacer.",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.DEFAULT_OPTION,
                null,
                eliminacion,
                eliminacion[1]); // "NO" como opcion por defecto
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Eliminar la cuenta
                cuentas.remove(cuentaAEliminar);
                Cuenta.getCuentas().remove(cuentaAEliminar);
                
                JOptionPane.showMessageDialog(null, 
                    "Cuenta eliminada exitosamente\n" +
                    "Ahora tiene " + cuentas.size() + " cuenta(s) activa(s).");
            }
        }
    }
    
    private Cuenta seleccionarCuenta(String mensaje) {
        if (cuentas.isEmpty()) {
            return null;
        }
        
        String[] opciones = new String[cuentas.size()];
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta cuenta = cuentas.get(i);
            opciones[i] = "Cuenta " + cuenta.getNumeroCuenta() + " - Saldo: $" + String.format("%.2f", cuenta.getSaldo());
        }
        
        int seleccion = JOptionPane.showOptionDialog(null, 
            mensaje, "Seleccionar Cuenta", 
            0, 0, null, opciones, opciones[0]);
        
        return (seleccion >= 0) ? cuentas.get(seleccion) : null;
    }
    
    private String generarNumeroTarjeta() {
        // Generar un numero de tarjeta de 16 digitos
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                numero.append(" ");
            }
            numero.append((int)(Math.random() * 10));
        }
        return numero.toString();
    }
    
    @Override
    public String toString() {
        return "Cliente [mail=" + this.getMail() + ", cuentas=" + cuentas.size() + ", tarjetas=" + tarjetas.size() + "]";
    }

	private void verResumenTarjeta() {
	 Tarjeta tarjeta = seleccionarTarjeta("Seleccione la tarjeta para ver el resumen:");
	 if (tarjeta == null) return;
	 
	 String resumen = tarjeta.generarResumen();
	 JOptionPane.showMessageDialog(null, resumen, "Resumen de Tarjeta", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	private void pagarResumenTarjeta() {
	 Tarjeta tarjeta = seleccionarTarjeta("Seleccione la tarjeta para pagar resumen:");
	 if (tarjeta == null) return;
	 
	 tarjeta.pagarResumen();
	}
	
	private Tarjeta seleccionarTarjeta(String mensaje) {
	 if (tarjetas.isEmpty()) {
	     return null;
	 }
	 
	 String[] opciones = new String[tarjetas.size()];
	 for (int i = 0; i < tarjetas.size(); i++) {
	     Tarjeta tarjeta = tarjetas.get(i);
	     String infoExtra = "";
	     if (tarjeta instanceof TarjetaCredito) {
	         TarjetaCredito tc = (TarjetaCredito) tarjeta;
	         infoExtra = String.format(" | Limite: $%.2f | Utilizado: $%.2f", 
	             tc.getLimite(), tc.getSaldoUtilizado());
	     }
	     opciones[i] = tarjeta.getTipo() + " " + tarjeta.getNumeroCompleto() + infoExtra;
	 }
	 
	 int seleccion = JOptionPane.showOptionDialog(null, 
	     mensaje, "Seleccionar Tarjeta", 
	     0, 0, null, opciones, opciones[0]);
	 
	 return (seleccion >= 0) ? tarjetas.get(seleccion) : null;
	}
	
	//Y actualicemos el metodo pedirTarjetas:
	private void pedirTarjetas() {
	    if (cuentas.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "Necesita tener al menos una cuenta para solicitar una tarjeta.");
	        return;
	    }
	    
	    String[] tiposTarjeta = {"Debito", "Credito"};
	    
	    int tipoSeleccionado = JOptionPane.showOptionDialog(null,
	        "Seleccione el tipo de tarjeta que desea solicitar:",
	        "Solicitar Tarjeta",
	        JOptionPane.DEFAULT_OPTION,
	        JOptionPane.QUESTION_MESSAGE,
	        null,
	        tiposTarjeta,
	        tiposTarjeta[0]);
	    
	    if (tipoSeleccionado == -1) return;
	    
	    String tipoTarjeta = tiposTarjeta[tipoSeleccionado];
	    Cuenta cuentaAsociada = seleccionarCuenta("Seleccione la cuenta para asociar la tarjeta:");
	    if (cuentaAsociada == null) return;
	    
	    // Generar numero de tarjeta (16 digitos sin espacios)
	    String numeroTarjeta = generarNumeroTarjeta().replace(" ", "");
	    Tarjeta nuevaTarjeta;
	    
	    if (tipoTarjeta.equals("Credito")) {
	        double limite = 10000.0; // Limite por defecto
	        String limiteStr = JOptionPane.showInputDialog("Ingrese el limite de crédito (por defecto $10,000):");
	        try {
	            if (limiteStr != null && !limiteStr.trim().isEmpty()) {
	                limite = Double.parseDouble(limiteStr);
	                if (limite <= 0) {
	                    JOptionPane.showMessageDialog(null, "El limite debe ser mayor a cero. Se usara $10,000.");
	                    limite = 10000.0;
	                }
	            }
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "Limite no valido. Se usara $10,000.");
	        }
	        nuevaTarjeta = new TarjetaCredito(numeroTarjeta, this, cuentaAsociada, limite);
	    } else {
	        nuevaTarjeta = new TarjetaDebito(numeroTarjeta, this, cuentaAsociada);
	    }
	    
	    this.agregarTarjeta(nuevaTarjeta);
	    
	    StringBuilder mensaje = new StringBuilder();
	    mensaje.append("Tarjeta solicitada exitosamente!\n\n");
	    mensaje.append("Tipo: ").append(tipoTarjeta).append("\n");
	    mensaje.append("Numero: ").append(nuevaTarjeta.getNumeroCompleto()).append("\n");
	    mensaje.append("Titular: ").append(this.getMail()).append("\n");
	    mensaje.append("Cuenta asociada: ").append(cuentaAsociada.getNumeroCuenta()).append("\n");
	    
	    if (nuevaTarjeta instanceof TarjetaCredito) {
	        TarjetaCredito tc = (TarjetaCredito) nuevaTarjeta;
	        mensaje.append("Limite de credito: $").append(String.format("%.2f", tc.getLimite())).append("\n");
	        mensaje.append("Saldo disponible: $").append(String.format("%.2f", tc.getSaldoDisponible())).append("\n");
	    }
	    
	    mensaje.append("\n Su tarjeta sera enviada en un plazo de 5-7 dias habiles.");
	    
	    JOptionPane.showMessageDialog(null, mensaje.toString());
	}
		
		private void gestionarPagosTarjeta() {
		 Tarjeta tarjeta = seleccionarTarjeta("Seleccione la tarjeta para gestionar pagos:");
		 if (tarjeta == null) return;
		 
		 if (!(tarjeta instanceof TarjetaCredito)) {
		     JOptionPane.showMessageDialog(null, 
		         "Esta funcion solo esta disponible para tarjetas de credito.\n" +
		         "Las tarjetas de debito descuentan automaticamente al realizar el consumo.");
		     return;
		 }
		 
		 TarjetaCredito tarjetaCredito = (TarjetaCredito) tarjeta;
		 LinkedList<Consumo> consumosPendientes = tarjetaCredito.getConsumosPendientes();
		 
		 if (consumosPendientes.isEmpty()) {
		     JOptionPane.showMessageDialog(null, "No tiene consumos pendientes de pago.");
		     return;
		 }
		 
		 // Seleccionar consumo a pagar
		 Consumo consumo = seleccionarConsumo(consumosPendientes, "Seleccione el consumo a pagar:");
		 if (consumo == null) return;
		 
		 String[] opcionesPago = {"Pagar 1 cuota", "Pagar consumo completo", "Cancelar"};
		 
		 int opcion = JOptionPane.showOptionDialog(null,
		     "Seleccione tipo de pago:\n" +
		     "Consumo: " + consumo.getDescripcion() + "\n" +
		     "Monto total: $" + String.format("%.2f", consumo.getMonto()) + "\n" +
		     "Cuotas: " + consumo.getCuotasPagadas() + "/" + consumo.getCuotas() + "\n" +
		     "Saldo pendiente: $" + String.format("%.2f", consumo.getSaldoPendiente()) + "\n" +
		     "Valor cuota: $" + String.format("%.2f", consumo.getMontoPorCuota()),
		     "Tipo de Pago",
		     JOptionPane.DEFAULT_OPTION,
		     JOptionPane.DEFAULT_OPTION,
		     null,
		     opcionesPago,
		     opcionesPago[0]);
		 
		 switch (opcion) {
		     case 0: // Pagar 1 cuota
		         tarjetaCredito.pagarCuota(consumo);
		         break;
		     case 1: // Pagar consumo completo
		         tarjetaCredito.pagarConsumoCompleto(consumo);
		         break;
		     // case 2: Cancelar
		 }
		}
		
		private Consumo seleccionarConsumo(LinkedList<Consumo> consumos, String mensaje) {
		 if (consumos.isEmpty()) {
		     return null;
		 }
		 
		 String[] opciones = new String[consumos.size()];
		 for (int i = 0; i < consumos.size(); i++) {
		     Consumo consumo = consumos.get(i);
		     opciones[i] = consumo.getDescripcion() + " - $" + String.format("%.2f", consumo.getSaldoPendiente()) + 
		                   " - " + consumo.getCuotasPagadas() + "/" + consumo.getCuotas() + " cuotas";
		 }
		 
		 int seleccion = JOptionPane.showOptionDialog(null, 
		     mensaje, "Seleccionar Consumo", 
		     0, 0, null, opciones, opciones[0]);
		 
		 return (seleccion >= 0) ? consumos.get(seleccion) : null;
		}
		
		//Y actualicemos el metodo gestionarTarjetas para incluir la nueva opcion:
		private void gestionarTarjetas() {
		 if (tarjetas.isEmpty()) {
		     JOptionPane.showMessageDialog(null, "No tiene tarjetas asociadas.");
		     return;
		 }
		 
		 String[] opciones = {"Ver resumen tarjeta", "Agregar consumo", "Pagar resumen completo", "Gestionar pagos individuales", "Volver"};
		 
		 int opcion = JOptionPane.showOptionDialog(null,
		     "Gestion de Tarjetas",
		     "Tarjetas",
		     JOptionPane.DEFAULT_OPTION,
		     JOptionPane.DEFAULT_OPTION,
		     null,
		     opciones,
		     opciones[0]);
		 
		 switch (opcion) {
		     case 0: // Ver resumen
		         verResumenTarjeta();
		         break;
		     case 1: // Agregar consumo
		         agregarConsumoTarjeta();
		         break;
		     case 2: // Pagar resumen completo
		         pagarResumenTarjeta();
		         break;
		     case 3: // Gestionar pagos individuales
		         gestionarPagosTarjeta();
		         break;
		     // case 4: Volver
		 }
		}
		private void agregarConsumoTarjeta() {
		    Tarjeta tarjeta = seleccionarTarjeta("Seleccione la tarjeta para agregar consumo:");
		    if (tarjeta == null) return;
		    
		    String descripcion = JOptionPane.showInputDialog("Descripcion del consumo:");
		    if (descripcion == null || descripcion.trim().isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Descripcion no valida.");
		        return;
		    }
		    
		    String montoStr = JOptionPane.showInputDialog("Monto del consumo:");
		    try {
		        double monto = Double.parseDouble(montoStr);
		        if (monto <= 0) {
		            JOptionPane.showMessageDialog(null, "El monto debe ser mayor a cero.");
		            return;
		        }
		        
		        int cuotas = 1;
		        if (tarjeta instanceof TarjetaCredito) {
		            String cuotasStr = JOptionPane.showInputDialog(
		                "Numero de cuotas (1-12):\n" +
		                "Monto total: $" + String.format("%.2f", monto) + "\n" +
		                "Valor por cuota: $" + String.format("%.2f", monto) + " / 1 = $" + String.format("%.2f", monto));
		            
		            try {
		                cuotas = Integer.parseInt(cuotasStr);
		                if (cuotas < 1 || cuotas > 12) {
		                    JOptionPane.showMessageDialog(null, "El numero de cuotas debe estar entre 1 y 12. Se usara 1 cuota.");
		                    cuotas = 1;
		                } else {
		                    // Mostrar confirmacion con el valor de cada cuota
		                    double valorCuota = monto / cuotas;
		                    int confirmacion = JOptionPane.showConfirmDialog(null,
		                        "¿Confirmar consumo?\n\n" +
		                        "Descripcion: " + descripcion + "\n" +
		                        "Monto total: $" + String.format("%.2f", monto) + "\n" +
		                        "Cuotas: " + cuotas + "\n" +
		                        "Valor por cuota: $" + String.format("%.2f", valorCuota),
		                        "Confirmar Consumo",
		                        JOptionPane.YES_NO_OPTION);
		                    
		                    if (confirmacion != JOptionPane.YES_OPTION) {
		                        return;
		                    }
		                }
		            } catch (NumberFormatException e) {
		                JOptionPane.showMessageDialog(null, "Numero de cuotas no valido. Se usara 1 cuota.");
		                cuotas = 1;
		            }
		        } else {
		            // Para debito, mostrar confirmacion
		            int confirmacion = JOptionPane.showConfirmDialog(null,
		                "¿Confirmar consumo?\n\n" +
		                "Descripcion: " + descripcion + "\n" +
		                "Monto a debitar: $" + String.format("%.2f", monto) + "\n" +
		                "Nuevo saldo: $" + String.format("%.2f", (tarjeta.getCuentaAsociada().getSaldo() - monto)),
		                "Confirmar Consumo Debito",
		                JOptionPane.YES_NO_OPTION);
		            
		            if (confirmacion != JOptionPane.YES_OPTION) {
		                return;
		            }
		        }
		        
		        tarjeta.agregarConsumo(descripcion, monto, cuotas);
		        
		    } catch (NumberFormatException e) {
		        JOptionPane.showMessageDialog(null, "Monto no valido.");
		    }
		    
}
		// En la clase Cliente, agreguemos estos nuevos métodos:

		private void gestionarInversiones() {
		    String[] opciones = {"Crear inversion", "Ver mis inversiones", "Simular dias", "Retirar inversion", "Volver"};
		    
		    int opcion = JOptionPane.showOptionDialog(null,
		        "Gestion de Inversiones",
		        "Inversiones",
		        JOptionPane.DEFAULT_OPTION,
		        JOptionPane.INFORMATION_MESSAGE,
		        null,
		        opciones,
		        opciones[0]);
		    
		    switch (opcion) {
		        case 0: // Crear inversión
		            crearInversion();
		            break;
		        case 1: // Ver mis inversiones
		            verMisInversiones();
		            break;
		        case 2: // Simular días
		            simularDiasInversion();
		            break;
		        case 3: // Retirar inversión
		            retirarInversion();
		            break;
		        // case 4: Volver
		    }
		}

		private void crearInversion() {
		    if (cuentas.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Necesita tener al menos una cuenta para invertir.");
		        return;
		    }
		    
		    Cuenta cuenta = seleccionarCuenta("Seleccione la cuenta desde la cual invertira:");
		    if (cuenta == null) return;
		    
		    // Solicitar monto a invertir
		    String montoStr = JOptionPane.showInputDialog(
		        "Ingrese el monto a invertir:\n" +
		        "Saldo disponible: $" + String.format("%.2f", cuenta.getSaldo()));
		    
		    double monto;
		    try {
		        monto = Double.parseDouble(montoStr);
		        if (monto <= 0) {
		            JOptionPane.showMessageDialog(null, "El monto debe ser mayor a cero.");
		            return;
		        }
		        if (monto > cuenta.getSaldo()) {
		            JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
		            return;
		        }
		    } catch (NumberFormatException e) {
		        JOptionPane.showMessageDialog(null, "Monto no valido.");
		        return;
		    }
		    
		    // Solicitar tasa de interés
		    String[] tasas = {"3.5% (Conservador)", "5.0% (Moderado)", "7.5% (Arriesgado)", "10.0% (Alto riesgo)"};
		    double[] valoresTasa = {3.5, 5.0, 7.5, 10.0};
		    
		    int tasaSeleccionada = JOptionPane.showOptionDialog(null,
		        "Seleccione el tipo de inversion:",
		        "Tipo de Inversion",
		        JOptionPane.DEFAULT_OPTION,
		        JOptionPane.QUESTION_MESSAGE,
		        null,
		        tasas,
		        tasas[0]);
		    
		    if (tasaSeleccionada == -1) return;
		    
		    double tasaInteres = valoresTasa[tasaSeleccionada];
		    
		    // Solicitar duración
		    String duracionStr = JOptionPane.showInputDialog(
		        "Ingrese la duración en dias (minimo 30, maximo 365):");
		    
		    int duracion;
		    try {
		        duracion = Integer.parseInt(duracionStr);
		        if (duracion < 30 || duracion > 365) {
		            JOptionPane.showMessageDialog(null, "La duracion debe estar entre 30 y 365 dias.");
		            return;
		        }
		    } catch (NumberFormatException e) {
		        JOptionPane.showMessageDialog(null, "Duracion no valida.");
		        return;
		    }
		    
		    // Calcular proyección
		    double interesDiario = (tasaInteres / 36500) * monto;
		    double interesTotal = interesDiario * duracion;
		    double totalFinal = monto + interesTotal;
		    
		    // Mostrar resumen y confirmar
		    int confirmacion = JOptionPane.showConfirmDialog(null,
		        "RESUMEN DE INVERSION \n\n" +
		        "Cuenta: " + cuenta.getNumeroCuenta() + "\n" +
		        "Monto a invertir: $" + String.format("%.2f", monto) + "\n" +
		        "Tasa anual: " + String.format("%.2f", tasaInteres) + "%\n" +
		        "Duracion: " + duracion + " días\n" +
		        "Interes diario estimado: $" + String.format("%.2f", interesDiario) + "\n" +
		        "Interes total estimado: $" + String.format("%.2f", interesTotal) + "\n" +
		        "Total final estimado: $" + String.format("%.2f", totalFinal) + "\n\n" +
		        "¿Confirmar inversion?",
		        "Confirmar Inversion",
		        JOptionPane.YES_NO_OPTION);
		    
		    if (confirmacion != JOptionPane.YES_OPTION) {
		        return;
		    }
		    
		    // Retirar el monto de la cuenta
		    cuenta.retirar(monto);
		    
		    // Crear la inversión
		    Inversion nuevaInversion = new Inversion(cuenta, monto, tasaInteres, duracion);
		    GestorInversiones.agregarInversion(nuevaInversion);
		    
		    // Registrar movimiento
		    Movimientos.registrarMovimiento(new Movimiento(cuenta, 
		        Movimiento.TipoMovimiento.RETIRO, 
		        monto, 
		        "Inversion creada - ID: " + nuevaInversion.getId()));
		    
		    JOptionPane.showMessageDialog(null,
		        "Inversión creada exitosamente\n\n" +
		        "ID: " + nuevaInversion.getId() + "\n" +
		        "Monto invertido: $" + String.format("%.2f", monto) + "\n" +
		        "Tasa anual: " + String.format("%.2f", tasaInteres) + "%\n" +
		        "Duracion: " + duracion + " dias\n" +
		        "Fecha finalizacion: " + nuevaInversion.getFechaFin() + "\n\n" +
		        "Puede simular el paso de dias y retirar cuando lo desee.");
		}

		private void verMisInversiones() {
		    LinkedList<Inversion> inversiones = GestorInversiones.getInversionesPorCliente(this.getMail());
		    
		    if (inversiones.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "No tiene inversiones registradas.");
		        return;
		    }
		    
		    StringBuilder sb = new StringBuilder();
		    sb.append("MIS INVERSIONES \n\n");
		    
		    double totalInvertido = 0;
		    double totalActual = 0;
		    
		    for (Inversion inversion : inversiones) {
		        sb.append(inversion.toString()).append("\n\n");
		        totalInvertido += inversion.getMontoInvertido();
		        totalActual += inversion.getTotalActual();
		    }
		    
		    sb.append("RESUMEN TOTAL \n");
		    sb.append("Total invertido: $").append(String.format("%.2f", totalInvertido)).append("\n");
		    sb.append("Total actual: $").append(String.format("%.2f", totalActual)).append("\n");
		    sb.append("Ganancia total: $").append(String.format("%.2f", totalActual - totalInvertido)).append("\n");
		    sb.append("Numero de inversiones: ").append(inversiones.size()).append("\n");
		    
		    JOptionPane.showMessageDialog(null, sb.toString(), "Mis Inversiones", JOptionPane.INFORMATION_MESSAGE);
		}

		private void simularDiasInversion() {
		    LinkedList<Inversion> inversionesActivas = GestorInversiones.getInversionesActivasPorCliente(this.getMail());
		    
		    if (inversionesActivas.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "No tiene inversiones activas para simular.");
		        return;
		    }
		    
		    // Seleccionar inversión
		    Inversion inversion = seleccionarInversion(inversionesActivas, "Seleccione la inversion para simular dias:");
		    if (inversion == null) return;
		    
		    // Solicitar número de días
		    String diasStr = JOptionPane.showInputDialog(
		        "Ingrese numero de dias a simular (1-30):\n" +
		        "Dias transcurridos actuales: " + inversion.getDiasTranscurridos() + "\n" +
		        "Dias restantes: " + inversion.getDiasRestantes());
		    
		    int dias;
		    try {
		        dias = Integer.parseInt(diasStr);
		        if (dias < 1 || dias > 30) {
		            JOptionPane.showMessageDialog(null, "Debe ingresar entre 1 y 30 dias.");
		            return;
		        }
		    } catch (NumberFormatException e) {
		        JOptionPane.showMessageDialog(null, "Numero de dias no valido.");
		        return;
		    }
		    
		    // Guardar estado anterior
		    double totalAnterior = inversion.getTotalActual();
		    int diasAnteriores = inversion.getDiasTranscurridos();
		    
		    // Simular días
		    boolean simulado = inversion.simularDias(dias);
		    
		    if (simulado) {
		        double totalNuevo = inversion.getTotalActual();
		        double ganancia = totalNuevo - totalAnterior;
		        
		        StringBuilder mensaje = new StringBuilder();
		        mensaje.append("Simulacion completada\n\n");
		        mensaje.append("Dias simulados: ").append(dias).append("\n");
		        mensaje.append("Dias transcurridos: ").append(diasAnteriores).append(" → ").append(inversion.getDiasTranscurridos()).append("\n");
		        mensaje.append("Total anterior: $").append(String.format("%.2f", totalAnterior)).append("\n");
		        mensaje.append("Total actual: $").append(String.format("%.2f", totalNuevo)).append("\n");
		        mensaje.append("Ganancia en ").append(dias).append(" dias: $").append(String.format("%.2f", ganancia)).append("\n");
		        mensaje.append("Interes diario: $").append(String.format("%.2f", inversion.getInteresDiario())).append("\n");
		        
		        if (!inversion.isActiva()) {
		            mensaje.append("\n La inversion ha finalizado su periodo.\n");
		            mensaje.append("El dinero esta listo para retirar.");
		        }
		        
		        JOptionPane.showMessageDialog(null, mensaje.toString());
		    } else {
		        JOptionPane.showMessageDialog(null, "No se pudo simular. La inversion puede haber finalizado.");
		    }
		}

		private void retirarInversion() {
		    LinkedList<Inversion> inversionesActivas = GestorInversiones.getInversionesActivasPorCliente(this.getMail());
		    
		    if (inversionesActivas.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "No tiene inversiones activas para retirar.");
		        return;
		    }
		    
		    // Seleccionar inversión
		    Inversion inversion = seleccionarInversion(inversionesActivas, "Seleccione la inversion para retirar:");
		    if (inversion == null) return;
		    
		    String[] opcionesRetiro = {"Retirar total", "Retirar monto especifico", "Cancelar"};
		    
		    int opcion = JOptionPane.showOptionDialog(null,
		        "Seleccione tipo de retiro:\n" +
		        "Inversion: " + inversion.getId().substring(0, 8) + "\n" +
		        "Total disponible: $" + String.format("%.2f", inversion.getTotalActual()),
		        "Tipo de Retiro",
		        JOptionPane.DEFAULT_OPTION,
		        JOptionPane.QUESTION_MESSAGE,
		        null,
		        opcionesRetiro,
		        opcionesRetiro[0]);
		    
		    switch (opcion) {
		        case 0: // Retirar total
		            retirarInversionTotal(inversion);
		            break;
		        case 1: // Retirar monto específico
		            retirarInversionParcial(inversion);
		            break;
		        // case 2: Cancelar
		    }
		}

		private void retirarInversionTotal(Inversion inversion) {
		    double total = inversion.getTotalActual();
		    double montoRetirado = inversion.retirarTotal();
		    
		    if (montoRetirado > 0) {
		        // Depositar en la cuenta
		        inversion.getCuenta().depositar(montoRetirado);
		        
		        // Registrar movimiento
		        Movimientos.registrarMovimiento(new Movimiento(inversion.getCuenta(), 
		            Movimiento.TipoMovimiento.DEPOSITO, 
		            montoRetirado, 
		            "Retiro total inversion - ID: " + inversion.getId()));
		        
		        JOptionPane.showMessageDialog(null,
		            "Retiro total realizado\n\n" +
		            "Monto retirado: $" + String.format("%.2f", montoRetirado) + "\n" +
		            "Depositado en cuenta: " + inversion.getCuenta().getNumeroCuenta() + "\n" +
		            "Nuevo saldo: $" + String.format("%.2f", inversion.getCuenta().getSaldo()) + "\n" +
		            "Inversion finalizada.");
		    }
		}

		private void retirarInversionParcial(Inversion inversion) {
		    String montoStr = JOptionPane.showInputDialog(
		        "Ingrese monto a retirar:\n" +
		        "Total disponible: $" + String.format("%.2f", inversion.getTotalActual()));
		    
		    double monto;
		    try {
		        monto = Double.parseDouble(montoStr);
		        if (monto <= 0) {
		            JOptionPane.showMessageDialog(null, "El monto debe ser mayor a cero.");
		            return;
		        }
		        if (monto > inversion.getTotalActual()) {
		            JOptionPane.showMessageDialog(null, "Monto excede el total disponible.");
		            return;
		        }
		    } catch (NumberFormatException e) {
		        JOptionPane.showMessageDialog(null, "Monto no valido.");
		        return;
		    }
		    
		    double montoRetirado = inversion.retirar(monto);
		    
		    if (montoRetirado > 0) {
		        // Depositar en la cuenta
		        inversion.getCuenta().depositar(montoRetirado);
		        
		        // Registrar movimiento
		        Movimientos.registrarMovimiento(new Movimiento(inversion.getCuenta(), 
		            Movimiento.TipoMovimiento.DEPOSITO, 
		            montoRetirado, 
		            "Retiro parcial inversion - ID: " + inversion.getId()));
		        
		        JOptionPane.showMessageDialog(null,
		            "Retiro parcial realizado\n\n" +
		            "Monto retirado: $" + String.format("%.2f", montoRetirado) + "\n" +
		            "Depositado en cuenta: " + inversion.getCuenta().getNumeroCuenta() + "\n" +
		            "Nuevo saldo cuenta: $" + String.format("%.2f", inversion.getCuenta().getSaldo()) + "\n" +
		            "Saldo restante en inversion: $" + String.format("%.2f", inversion.getTotalActual()) + "\n" +
		            "Inversion " + (inversion.isActiva() ? "continua activa" : "finalizada"));
		    }
		}

		private Inversion seleccionarInversion(LinkedList<Inversion> inversiones, String mensaje) {
		    if (inversiones.isEmpty()) {
		        return null;
		    }
		    
		    String[] opciones = new String[inversiones.size()];
		    for (int i = 0; i < inversiones.size(); i++) {
		        Inversion inv = inversiones.get(i);
		        opciones[i] = "ID: " + inv.getId().substring(0, 8) + 
		                     " Monto: $" + String.format("%.2f", inv.getTotalActual()) +
		                     " Dias: " + inv.getDiasTranscurridos();
		    }
		    
		    int seleccion = JOptionPane.showOptionDialog(null, 
		        mensaje, "Seleccionar Inversion", 
		        0, 0, null, opciones, opciones[0]);
		    
		    return (seleccion >= 0) ? inversiones.get(seleccion) : null;
		}}