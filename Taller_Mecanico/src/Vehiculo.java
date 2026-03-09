import java.time.LocalDate; // para obtener la fecha actual

public class Vehiculo {

	// Atributos 
	private String marca;
	private String modelo;
	private int anio;
	private int kilometraje;
	private String estado;
	private String fechaIngreso;

	// Constructor completo
	public Vehiculo(String marca, String modelo, int anio, int kilometraje, String estado, String fechaIngreso ) {
	setMarca(marca);
	setModelo(modelo);
	setAnio(anio);
	setKilometraje(kilometraje);
	setEstado(estado);
	setFechaIngreso(fechaIngreso);
	}

	// Constructor solo con marca y modelo
	public Vehiculo(String marca, String modelo) {
	setMarca(marca);
	setModelo(modelo);
	this.anio = 0;
	this.kilometraje = 0;
	this.estado = "pendiente";
	this.fechaIngreso = LocalDate.now().toString();	}
	
	// Metodos
	public void mostrarDatos() {
	System.out.println("\n La marca del vehiculo es :" + marca + "\n El modelo del vehiculo es : " 
	+ modelo + "\n El anio del vehiculo es : " + anio + "\n Los kilometros del vehcilo son : " 
	+ kilometraje + "\n El estado del mismo es :" + estado + "\n El vehiculo ingreso la fecha : " + fechaIngreso);
	}

	public void registrarRevision() {
	String fecha = LocalDate.now().toString();
	System.out.println("El vehículo fue revisado el día: " + fecha);
	}

	public void cambiarEstado(String nuevoEstado) {
	setEstado(nuevoEstado);
	System.out.println("Estado actualizado a: " + estado);
	}

	public void cambiarKilometraje(int km) {
	setKilometraje(km);
	System.out.println("Kilometraje actualizado a: " + kilometraje + " km");
	}

	public String getMarca() { return marca; }
	
	public void setMarca(String marca) {
	if (marca != null && !marca.isEmpty()) {
	this.marca = marca;
	} else {
	this.marca = "Desconocida";
	}
	}

	public String getModelo() { return modelo; }
	
	public void setModelo(String modelo) {
	if (modelo != null && !modelo.isEmpty()) {
	this.modelo = modelo;
	} else {
	this.modelo = "Desconocido";
	}
	}

	public int getAnio() { return anio; }
	
	public void setAnio(int anio) {
	int actual = LocalDate.now().getYear();
	if (anio > 1900 && anio <= actual) {
	this.anio = anio;
	} else {
	this.anio = actual;
	}
	}

	public int getKilometraje() { return kilometraje; }
	
	public void setKilometraje(int kilometraje) {
	if (kilometraje >= 0) {
	this.kilometraje = kilometraje;
	} else {
	this.kilometraje = 0;
	}
	}

	public String getEstado() { return estado; }
	public void setEstado(String estado) {
	if (estado.equalsIgnoreCase("revision") ||
	estado.equalsIgnoreCase("listo") ||
	estado.equalsIgnoreCase("pendiente")) {
	this.estado = estado;
	} else {
	this.estado = "pendiente";
	}
	}
	
	public String getFechaIngreso() { return fechaIngreso; }
	
	public void setFechaIngreso(String fechaIngreso) {
	if (fechaIngreso != null && !fechaIngreso.isEmpty()) {
	this.fechaIngreso = fechaIngreso;
	} else {
	// Si el usuario no ingresa nada, guardamos la fecha actual
	this.fechaIngreso = java.time.LocalDate.now().toString();
	}
	}

	
	}
