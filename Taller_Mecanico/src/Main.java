import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Registro de Vehiculos en el Taller Mecanico");
		System.out.println("Vamos a pedir algunos datos para registrar el vehiculo \n");
		System.out.print("Ingrese marca: ");
		String marca = sc.nextLine();
		System.out.print("Ingrese modelo: ");
		String modelo = sc.nextLine();
		System.out.print("Ingrese anio: ");
		int anio = sc.nextInt();
		System.out.print("Ingrese kilometraje: ");
		int km = sc.nextInt();
		sc.nextLine(); // limpiar buffer
		System.out.print("Ingrese estado (revision / listo / pendiente): ");
		String estado = sc.nextLine();
		System.out.println("Ingrese fecha de ingreso (AAAA-MM-DD) o fecha actual (vacio)");
		String fechaIngreso = sc.nextLine();
		//Creacion Vehiculo
		Vehiculo v = new Vehiculo(marca, modelo, anio, km, estado, fechaIngreso);

		int opcion;
		do {
		System.out.println("\n- Menu -");
		System.out.println("1. Mostrar datos");
		System.out.println("2. Registrar revision");
		System.out.println("3. Cambiar estado");
		System.out.println("4. Cambiar kilometraje");
		System.out.println("5. Cambiar fecha de ingreso");
		System.out.println("0. Salir");
		System.out.print("Elija una opcion: ");
		opcion = sc.nextInt();
		sc.nextLine();

		 switch (opcion) {
         case 1:
             v.mostrarDatos();
             break;
         case 2:
             v.registrarRevision();
             break;
         case 3:
             System.out.print("Nuevo estado: ");
             String nuevoEstado = sc.nextLine();
             v.cambiarEstado(nuevoEstado);
             break;
         case 4:
             System.out.print("Nuevo kilometraje: ");
             int nuevoKm = sc.nextInt();
             sc.nextLine();
             v.cambiarKilometraje(nuevoKm);
             break;
         case 5:
        	 System.out.println("Nueva fecha de ingreso (AAAA-MM-DD) :");
        	 String nuevaFecha = sc.nextLine();
        	 v.setFechaIngreso(nuevaFecha);
        	 v.setFechaIngreso(v.getFechaIngreso());
        	 break;
         case 0:
             System.out.println("Saliendo...");
             break;
         default:
             System.out.println("Opcion no valida.");
             break;
		 }
		} while (opcion != 0);
		sc.close();
	}
	
}


