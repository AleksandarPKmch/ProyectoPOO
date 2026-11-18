package carnets.vista;

import carnets.controlador.Controlador;
import carnets.modelo.Carrera;
import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;
import carnets.modelo.TipoPersona;
import carnets.servicio.Estadisticas;
import carnets.servicio.ResultadoValidacion;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Segunda vista sobre el mismo controlador. Sirve de respaldo si la interfaz grafica falla
 * y demuestra que en MVC la vista se puede cambiar sin tocar el modelo.
 */
public class VistaConsola {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    private final Controlador controlador;
    private final Scanner scanner = new Scanner(System.in);

    public VistaConsola(Controlador controlador) {
        this.controlador = controlador;
    }

    public void ejecutar() {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println();
            System.out.println("===== BUS UVG, CONTROL DE ABORDAJE =====");
            System.out.println("Guardando en: " + controlador.almacenamiento());
            System.out.println("1. Validar abordaje");
            System.out.println("2. Registrar persona");
            System.out.println("3. Ver personas");
            System.out.println("4. Bitacora y estadisticas");
            System.out.println("0. Salir");
            opcion = leer("Opcion: ");
            try {
                switch (opcion) {
                    case "1" -> modoAbordaje();
                    case "2" -> registrar();
                    case "3" -> controlador.listarPersonas().forEach(p -> System.out.println("  " + p));
                    case "4" -> bitacora();
                    case "0" -> System.out.println("Hasta luego.");
                    default -> System.out.println("Esa opcion no existe.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println(e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Sin conexion: " + Estilo.limpiar(e.getMessage()));
            }
        }
    }

    private void modoAbordaje() {
        System.out.println("Escanee o escriba carnets. Enter vacio para volver al menu.");
        while (true) {
            String lectura = leer("Carnet: ");
            if (lectura.isEmpty()) {
                return;
            }
            ResultadoValidacion r = controlador.validarAbordaje(lectura);
            Persona p = r.getPersona();
            System.out.println((r.isAceptado() ? "  [ ACEPTADO ]  " : "  [ RECHAZADO ] ")
                    + (p == null ? "" : p.getNombreCompleto() + ", " + p.getTipo().getDescripcion() + ". ")
                    + r.getRegistro().getMotivo());
        }
    }

    private void registrar() {
        TipoPersona[] tipos = TipoPersona.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println("  " + (i + 1) + ". " + tipos[i].getDescripcion());
        }
        String t = leer("Tipo: ");
        if (!t.matches("[1-3]")) {
            System.out.println("Elija 1, 2 o 3.");
            return;
        }
        TipoPersona tipo = tipos[Integer.parseInt(t) - 1];
        String carnet = leer("Carnet UVG: ");
        String nombre = leer("Nombre: ");
        String apellido = leer("Apellido: ");
        String dato;
        if (tipo == TipoPersona.ESTUDIANTE) {
            for (Carrera c : Carrera.values()) {
                System.out.println("  " + c.getOpcion() + ". " + c.getDescripcion());
            }
            dato = leer("Numero de carrera: ");
        } else {
            dato = leer(tipo == TipoPersona.CATEDRATICO ? "Departamento: " : "Area: ");
        }
        Persona p = controlador.registrarPersona(tipo, carnet, nombre, apellido, dato);
        System.out.println("Registrado: " + p);
    }

    private void bitacora() {
        Estadisticas e = controlador.estadisticas();
        System.out.printf("Intentos %d | aceptados %d | rechazados %d | rechazo %.1f %% | hora pico %s%n",
                e.getTotal(), e.getAceptados(), e.getRechazados(), e.getPorcentajeRechazo(),
                e.getHoraPico() < 0 ? "-" : String.format("%02d:00", e.getHoraPico()));
        List<RegistroAbordaje> lista = controlador.bitacora();
        System.out.println("Ultimos intentos:");
        for (RegistroAbordaje r : lista.subList(0, Math.min(15, lista.size()))) {
            System.out.println("  " + r.getFechaHora().format(FORMATO) + "  " + r.getCarnetLeido() + "  "
                    + (r.isAceptado() ? "aceptado " : "rechazado") + "  " + r.getMotivo());
        }
    }

    private String leer(String pregunta) {
        System.out.print(pregunta);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "0";
    }
}
