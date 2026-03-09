public enum Rol {
    
    Administrador(new String[] {"Crear cliente", "Gestionar cuentas", "Ver cuentas", "Ver transferencias", "Ver resumen", "Ver todos los movimientos", "Salir"}),
    Cliente(new String[] {"Transferir", "Consultar movimientos", "Consultar cuentas", "Gestionar tarjetas", "Pedir tarjetas", "Invertir", "Eliminar cuentas", "Salir"});
    
    private String[] opciones;

    private Rol(String[] opciones) {
        this.opciones = opciones;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public void setOpciones(String[] opciones) {
        this.opciones = opciones;
    }
}