import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Crear Scanner.
        Scanner scanner = new Scanner(System.in);
        // Crear objeto RegistrarEstudiante.
        RegistrarEstudiante registrar = new RegistrarEstudiante();
        // Crear ArrayList<EstudianteData> para almacenar estudiantes.
        ArrayList<EstudianteData> estudiantesRegistrados = new ArrayList<>();
        // Mostrar menu y repetir hasta salir.
        int opcion;
        do {
            System.out.println("   Sistema de Registro de Estudiantes   ");
            System.out.println("1. Registrar cantidad de estudiantes");
            System.out.println("2. Registrar estudiante");
            System.out.println("3. Consultar estudiantes");
            System.out.println("4. Eliminar estudiante");
            System.out.println("5. Buscar estudiante");
            System.out.println("6. Salir");
            opcion = leerOpcion(scanner);
        } while (opcion != 6);
    }

    // Mostrar las opciones del menu.
    public static void mostrarMenu() {

    }

    // Leer opcion del usuario.
    public static int leerOpcion(Scanner scanner) {
        return scanner.nextInt();
    }

    // registrar una cantidad de estudiantes.
    public static void registrarCantidad(Scanner scanner, RegistrarEstudiante registrar,
            ArrayList<EstudianteData> estudiantesRegistrados) {
        System.out.print("Ingrese la cantidad de estudiantes a registrar: ");
        int cantidad = scanner.nextInt();
        for (int i = 0; i < cantidad; i++) {
            registrarEstudiante(scanner, registrar, estudiantesRegistrados);
        }
    }

    // agregar un estudiante usando el Scanner.
    public static void registrarEstudiante(Scanner scanner, RegistrarEstudiante registrar,
            ArrayList<EstudianteData> estudiantesRegistrados) {
        System.out.print("Ingrese el nombre del estudiante: ");
        String nombre = scanner.next();
        System.out.print("Ingrese el apellido del estudiante: ");
        String apellido = scanner.next();
        System.out.print("Ingrese la carrera del estudiante: ");
        Carrera carrera = Carrera.valueOf(scanner.next().toUpperCase());
        System.out.print("Ingrese el carnet del estudiante: ");
        int carnet = scanner.nextInt();
        EstudianteData estudiante = new EstudianteData(nombre, apellido, carrera, carnet);
        estudiantesRegistrados.add(estudiante);
    }

    // consultar la lista completa.
    public static void consultarEstudiantes(RegistrarEstudiante registrar) {
        for (EstudianteData e : registrar.getListaEstudiante()) {
            System.out.println(e.toString());
        }
    }

    // eliminar estudiante por carnet.
    public static void eliminarEstudiante(Scanner scanner, RegistrarEstudiante registrar) {
        System.out.print("Ingrese el carnet del estudiante a eliminar: ");
        int carnet = scanner.nextInt();
        boolean eliminado = registrar.eliminarEstudiantePorCarnet(carnet);
        if (eliminado) {
            System.out.println("Estudiante eliminado correctamente.");
        } else {
            System.out.println("No se encontró un estudiante con ese carnet.");
        }
    }

    // buscar por carnet.
    public static void buscarEstudiante(Scanner scanner, RegistrarEstudiante registrar) {
        System.out.print("Ingrese el carnet del estudiante a buscar: ");
        int carnet = scanner.nextInt();
        EstudianteData estudiante = registrar.buscarEstudiantePorCarnet(carnet);
        if (estudiante != null) {
            System.out.println(estudiante.toString());
        } else {
            System.out.println("No se encontró un estudiante con ese carnet.");
        }
    }
}
