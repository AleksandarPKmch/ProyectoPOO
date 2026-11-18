package carnets.vista;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

/** Colores y fuentes compartidos por todas las pantallas. */
final class Estilo {

    static final Color VERDE = new Color(46, 125, 50);
    static final Color ROJO = new Color(198, 40, 40);
    static final Color GRIS = new Color(69, 90, 100);
    static final Color NARANJA = new Color(230, 81, 0);
    static final Color AZUL = new Color(21, 101, 192);
    static final Color FONDO_TARJETA = new Color(245, 247, 250);

    private Estilo() {
    }

    static Font fuente(int estilo, float tamano) {
        return new Font(Font.SANS_SERIF, estilo, Math.round(tamano));
    }

    static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente(Font.BOLD, 24));
        return l;
    }

    static Border margen(int px) {
        return BorderFactory.createEmptyBorder(px, px, px, px);
    }

    /** Texto corto para mostrar errores de conexion sin la cadena de conexion completa. */
    static String limpiar(String mensaje) {
        if (mensaje == null) {
            return "Error desconocido";
        }
        String sinUri = mensaje.replaceAll("mongodb(\\+srv)?://\\S+", "[conexion oculta]");
        String linea = sinUri.split("\n")[0];
        return linea.length() > 140 ? linea.substring(0, 140) + "..." : linea;
    }
}
