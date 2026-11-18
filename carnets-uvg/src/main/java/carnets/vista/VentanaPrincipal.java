package carnets.vista;

import carnets.controlador.Controlador;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Vista grafica: tres pestanas que comparten el mismo controlador. */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(Controlador controlador) {
        super("Bus UVG - Control de abordaje");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.cerrar();
                dispose();
                System.exit(0);
            }
        });

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(Estilo.fuente(Font.BOLD, 14));
        pestanas.addTab("Abordaje", new PanelAbordaje(controlador));
        pestanas.addTab("Personas", new PanelPersonas(controlador));
        pestanas.addTab("Bitacora y estadisticas", new PanelBitacora(controlador));
        pestanas.addChangeListener(e -> {
            Component elegida = pestanas.getSelectedComponent();
            if (elegida instanceof Actualizable a) {
                a.actualizar();
            }
        });

        String donde = controlador.almacenamiento();
        JLabel estado = new JLabel("Guardando en: " + donde);
        estado.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        estado.setOpaque(true);
        estado.setForeground(java.awt.Color.WHITE);
        estado.setBackground(donde.startsWith("MongoDB") ? Estilo.VERDE : Estilo.NARANJA);

        getContentPane().add(pestanas, BorderLayout.CENTER);
        getContentPane().add(estado, BorderLayout.SOUTH);
        setSize(1080, 700);
        setLocationRelativeTo(null);
    }
}
