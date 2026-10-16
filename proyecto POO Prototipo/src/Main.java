import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RegistrarEstudiante registrar = new RegistrarEstudiante();
        ArrayList<EstudianteData> estudiantesRegistrados = new ArrayList<>();

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerOpcion(scanner);

            switch (opcion) {
                case 1:
                    registrarCantidad(scanner, registrar, estudiantesRegistrados);
                    break;
                case 2:
                    consultarEstudiantes(registrar);
                    break;
                case 3:
                    eliminarEstudiante(scanner, registrar);
                    break;
                case 4:
                    agregarEstudiante(scanner, registrar, estudiantesRegistrados);
                    break;
                case 5:
                    buscarEstudiante(scanner, registrar);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente de nuevo.");
                    break;
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENU ESTUDIANTES =====");
        System.out.println("1. Registrar cantidad de estudiantes (Varios)");
        System.out.println("2. Consultar estudiantes registrados");
        System.out.println("3. Eliminar estudiante por numero de carnet");
        System.out.println("4. Agregar estudiante (Solo uno)");
        System.out.println("5. Buscar estudiante");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static int leerOpcion(Scanner scanner) {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static void registrarCantidad(Scanner scanner, RegistrarEstudiante registrar,
            ArrayList<EstudianteData> estudiantesRegistrados) {
        System.out.print("Cuantos estudiantes desea registrar? ");
        int cantidad;
        try {
            cantidad = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Debe ingresar un numero entero.");
            return;
        }

        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que 0.");
            return;
        }

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nRegistro estudiante " + (i + 1));
            agregarEstudiante(scanner, registrar, estudiantesRegistrados);
        }
    }

    private static void agregarEstudiante(Scanner scanner, RegistrarEstudiante registrar,
            ArrayList<EstudianteData> estudiantesRegistrados) {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Carrera: (Carreras disponibles: INGENIERIA, EDUCACION, CIENCIAS Y HUMANIDADES, BUSINESS, CIENCIAS SOCIALES, ART SCHOOL, COLEGIO, ARQUITECTURA\n)");
        String carreraTexto = scanner.nextLine();

        Carrera carrera = Carrera.fromString(carreraTexto);
        if (carrera == null) {
            System.out.println("Carrera invalida. Use una carrera de la lista.");
            return;
        }

        int carnet = 1000 + registrar.getEstudiantes().size() + 1;
        boolean ok = registrar.registrarEstudiante(nombre, apellido, carrera, carnet);
        if (ok) {
            EstudianteData nuevo = new EstudianteData(nombre.trim(), apellido.trim(), carrera, carnet);
            estudiantesRegistrados.add(nuevo);
            System.out.println("Estudiante registrado correctamente con carnet " + carnet);
        } else {
            System.out.println("No se pudo registrar el estudiante.");
        }
    }

    private static void consultarEstudiantes(RegistrarEstudiante registrar) {
        List<EstudianteData> estudiantes = registrar.getEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        System.out.println("\nLista de estudiantes registrados:");
        for (EstudianteData estudiante : estudiantes) {
            System.out.println(estudiante);
        }
    }

    private static void eliminarEstudiante(Scanner scanner, RegistrarEstudiante registrar) {
        System.out.print("Ingrese el numero de carnet a eliminar: ");
        int carnet;
        try {
            carnet = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Debe ingresar un numero de carnet valido.");
            return;
        }

        boolean eliminado = registrar.eliminarEstudiantePorCarnet(carnet);
        if (eliminado) {
            System.out.println("Estudiante con carnet " + carnet + " eliminado.");
        } else {
            System.out.println("No se encontro un estudiante con carnet " + carnet + ".");
        }
    }

    private static void buscarEstudiante(Scanner scanner, RegistrarEstudiante registrar) {
        System.out.print("Ingrese el numero de carnet a buscar: ");
        int carnet;
        try {
            carnet = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Debe ingresar un numero de carnet valido.");
            return;
        }

        EstudianteData encontrado = registrar.buscarEstudiantePorCarnet(carnet);
        if (encontrado == null) {
            System.out.println("No se encontro un estudiante con carnet " + carnet + ".");
        } else {
            System.out.println("Estudiante encontrado: " + encontrado);
        }
    }
}