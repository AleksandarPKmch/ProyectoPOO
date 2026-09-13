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
        while (true) {
            mostrarMenu();
            int opcion = leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    registrarCantidad(scanner, registrar, estudiantesRegistrados);
                    break;
                case 2:
                    agregarEstudiante(scanner, registrar, estudiantesRegistrados);
                    break;
                case 3:
                    consultarEstudiantes(registrar);
                    break;
                case 4:
                    eliminarEstudiante(scanner, registrar);
                    break;
                case 5:
                    buscarEstudiante(scanner, registrar);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    // Mostrar las opciones del menu.
    public static void mostrarMenu() {
        System.out.println("1. Registrar estudiantes");
        System.out.println("2. Agregar estudiante");
        System.out.println("3. Consultar estudiantes");
        System.out.println("4. Eliminar estudiante");
        System.out.println("5. Buscar estudiante");
        System.out.println("6. Salir");
    }

    // Leer opcion del usuario.
    public static int leerOpcion(Scanner scanner) {
        return -1;
    }

    // registrar una cantidad de estudiantes.
    public static void registrarCantidad(Scanner scanner, RegistrarEstudiante registrar,
            ArrayList<EstudianteData> estudiantesRegistrados) {
    }

    // agregar un estudiante usando el Scanner.
    public static void agregarEstudiante(Scanner scanner, RegistrarEstudiante registrar,
            ArrayList<EstudianteData> estudiantesRegistrados) {
    }

    // consultar la lista completa.
    public static void consultarEstudiantes(RegistrarEstudiante registrar) {
    }

    // eliminar estudiante por carnet.
    public static void eliminarEstudiante(Scanner scanner, RegistrarEstudiante registrar) {
    }

    // buscar por carnet.
    public static void buscarEstudiante(Scanner scanner, RegistrarEstudiante registrar) {
    }

}
 