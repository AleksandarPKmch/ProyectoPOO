package carnets;

import carnets.controlador.Controlador;
import carnets.repositorio.Repositorio;
import carnets.repositorio.RepositorioMemoria;
import carnets.repositorio.RepositorioMongo;
import carnets.vista.VentanaPrincipal;
import carnets.vista.VistaConsola;
import com.mongodb.MongoException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Arranque. Por defecto abre la interfaz grafica.
 * Opciones: --consola abre el menu de texto, --sin-datos no carga datos de prueba en modo memoria.
 */
public class Main {

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        List<String> opciones = Arrays.asList(args);

        Repositorio repo = crearRepositorio();
        if (repo instanceof RepositorioMemoria && !opciones.contains("--sin-datos")) {
            DatosDePrueba.cargar(repo);
            System.out.println("Se cargaron personas de prueba, por ejemplo el carnet 24101.");
        }
        Controlador controlador = new Controlador(repo);

        if (opciones.contains("--consola") || GraphicsEnvironment.isHeadless()) {
            try {
                new VistaConsola(controlador).ejecutar();
            } finally {
                controlador.cerrar();
            }
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                // si falla se queda el aspecto por defecto de Java
            }
            new VentanaPrincipal(controlador).setVisible(true);
        });
    }

    private static Repositorio crearRepositorio() {
        String uri = Config.mongoUri();
        if (uri == null) {
            System.out.println("No encontre MONGO_URI ni el archivo .env. Sigo en modo memoria.");
            return new RepositorioMemoria();
        }
        System.out.println("Conectando con MongoDB...");
        try {
            RepositorioMongo repo = new RepositorioMongo(uri);
            System.out.println("Conectado a MongoDB.");
            return repo;
        } catch (IllegalArgumentException e) {
            System.out.println("La cadena de conexion no tiene un formato valido, revisa el archivo .env.");
        } catch (MongoException e) {
            System.out.println("No pude conectar con MongoDB: " + limpiar(e.getMessage()));
            System.out.println("Revisa usuario, contrasena y que tu IP este en Network Access de Atlas.");
        }
        System.out.println("Sigo en modo memoria, los datos se pierden al cerrar.");
        return new RepositorioMemoria();
    }

    private static String limpiar(String mensaje) {
        if (mensaje == null) {
            return "";
        }
        String linea = mensaje.replaceAll("mongodb(\\+srv)?://\\S+", "[conexion oculta]").split("\n")[0];
        return linea.length() > 200 ? linea.substring(0, 200) + "..." : linea;
    }
}
