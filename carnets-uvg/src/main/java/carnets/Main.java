package carnets;

import carnets.modelo.Carrera;
import carnets.modelo.EstudianteData;
import carnets.repositorio.EstudianteRepository;
import carnets.repositorio.RepositorioMemoria;
import carnets.repositorio.RepositorioMongo;
import carnets.servicio.EstudianteService;

import com.mongodb.MongoException;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Pantalla del sistema: solo habla con la persona, las reglas estan en el servicio. */
public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final EstudianteService servicio;

    private Main(EstudianteService servicio) {
        this.servicio = servicio;
    }

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        try (EstudianteRepository repo = crearRepositorio()) {
            new Main(new EstudianteService(repo)).ejecutar();
        }
    }

    private static EstudianteRepository crearRepositorio() {
        String uri = Config.mongoUri();
        if (uri == null) {
            System.out.println("No encontre la conexion a MongoDB, falta MONGO_URI o el archivo .env.");
            System.out.println("Sigo en modo memoria, los datos se pierden al cerrar.");
            return new RepositorioMemoria();
        }
        System.out.println("Conectando con MongoDB...");
        try {
            RepositorioMongo repo = new RepositorioMongo(uri);
            System.out.println("Conectado.");
            return repo;
        } catch (IllegalArgumentException e) {
            System.out.println("La cadena de conexion no tiene un formato valido, revisa MONGO_URI.");
        } catch (MongoException e) {
            System.out.println("No pude conectar con MongoDB.");
            System.out.println(limpiar(e.getMessage()));
            System.out.println("Revisa en Atlas: usuario y contrasena, y que tu IP este en Network Access.");
        }
        System.out.println("Sigo en modo memoria, los datos se pierden al cerrar.");
        return new RepositorioMemoria();
    }

    /** Quita cualquier cadena de conexion que venga dentro de un mensaje de error. */
    private static String limpiar(String mensaje) {
        if (mensaje == null) {
            return "";
        }
        String sinUri = mensaje.replaceAll("mongodb(\\+srv)?://\\S+", "[conexion oculta]");
        String primeraLinea = sinUri.split("\n")[0];
        return primeraLinea.length() > 220 ? primeraLinea.substring(0, 220) + "..." : primeraLinea;
    }

    private void ejecutar() {
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            Integer leido = leerEntero("Que quieres hacer? ");
            opcion = leido == null ? -1 : leido;
            System.out.println();
            switch (opcion) {
                case 1:
                    registrarUno();
                    break;
                case 2:
                    registrarVarios();
                    break;
                case 3:
                    verEstudiantes();
                    break;
                case 4:
                    buscar();
                    break;
                case 5:
                    eliminar();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Esa opcion no existe, elige un numero del menu.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== CARNETS UVG =====");
        System.out.println("Guardando en: " + servicio.almacenamiento());
        System.out.println("1. Registrar un estudiante");
        System.out.println("2. Registrar varios estudiantes");
        System.out.println("3. Ver todos los estudiantes");
        System.out.println("4. Buscar por carnet");
        System.out.println("5. Eliminar un estudiante");
        System.out.println("0. Salir");
    }

    private void registrarUno() {
        String nombre = pedirObligatorio("Nombre: ");
        String apellido = pedirObligatorio("Apellido: ");
        Carrera carrera = elegirCarrera();
        if (carrera == null) {
            System.out.println("Registro cancelado.");
            return;
        }
        try {
            EstudianteData nuevo = servicio.registrar(nombre, apellido, carrera);
            System.out.println("Listo, estudiante registrado.");
            System.out.println(nuevo.tarjeta());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registrarVarios() {
        Integer cantidad = leerEntero("Cuantos estudiantes quieres registrar? ");
        if (cantidad == null || cantidad <= 0) {
            System.out.println("Escribe un numero mayor que 0.");
            return;
        }
        for (int i = 1; i <= cantidad; i++) {
            System.out.println("--- Estudiante " + i + " de " + cantidad + " ---");
            registrarUno();
        }
    }

    private void verEstudiantes() {
        List<EstudianteData> lista = servicio.listar();
        if (lista.isEmpty()) {
            System.out.println("Todavia no hay estudiantes registrados.");
            return;
        }
        System.out.println("Estudiantes registrados: " + lista.size());
        for (EstudianteData e : lista) {
            System.out.println("  " + e);
        }
    }

    private void buscar() {
        Integer carnet = leerEntero("Numero de carnet: ");
        if (carnet == null) {
            System.out.println("El carnet debe ser un numero.");
            return;
        }
        EstudianteData e = servicio.buscar(carnet);
        System.out.println(e == null ? "No hay ningun estudiante con el carnet " + carnet + "." : e.tarjeta());
    }

    private void eliminar() {
        Integer carnet = leerEntero("Carnet del estudiante a eliminar: ");
        if (carnet == null) {
            System.out.println("El carnet debe ser un numero.");
            return;
        }
        EstudianteData e = servicio.buscar(carnet);
        if (e == null) {
            System.out.println("No hay ningun estudiante con el carnet " + carnet + ".");
            return;
        }
        System.out.println(e.tarjeta());
        System.out.print("Seguro que quieres eliminarlo? s/n: ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        if (respuesta.equals("s") || respuesta.equals("si")) {
            System.out.println(servicio.eliminar(carnet) ? "Estudiante eliminado." : "No se pudo eliminar.");
        } else {
            System.out.println("No se elimino a nadie.");
        }
    }

    /** Muestra las carreras con su numero y repite la pregunta hasta acertar. Enter cancela. */
    private Carrera elegirCarrera() {
        System.out.println("Elige la carrera:");
        for (Carrera c : Carrera.values()) {
            System.out.println("  " + c.getOpcion() + ". " + c.getDescripcion());
        }
        while (true) {
            System.out.print("Escribe el numero, o Enter para cancelar: ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return null;
            }
            Carrera carrera = Carrera.fromString(texto);
            if (carrera != null) {
                return carrera;
            }
            System.out.println("Esa carrera no esta en la lista, prueba con otro numero.");
        }
    }

    private String pedirObligatorio(String pregunta) {
        while (true) {
            System.out.print(pregunta);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este dato no puede quedar vacio.");
        }
    }

    private Integer leerEntero(String pregunta) {
        System.out.print(pregunta);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
