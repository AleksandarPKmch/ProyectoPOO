package carnets.vista;

import carnets.controlador.Controlador;
import carnets.modelo.Persona;
import carnets.servicio.ResultadoValidacion;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

/**
 * Pantalla del piloto. Un lector USB de codigo de barras o QR escribe como si fuera un teclado
 * y termina con Enter, asi que funciona en este campo sin programar nada extra.
 */
public class PanelAbordaje extends JPanel implements Actualizable {

    private final Controlador controlador;
    private final JTextField campo = new JTextField(10);
    private final JPanel tarjeta = new JPanel();
    private final JLabel veredicto = etiqueta(46, Font.BOLD);
    private final JLabel nombre = etiqueta(28, Font.BOLD);
    private final JLabel detalle = etiqueta(20, Font.PLAIN);
    private final JLabel motivo = etiqueta(18, Font.PLAIN);
    private final JLabel contador = new JLabel();
    private final Timer reinicio;

    public PanelAbordaje(Controlador controlador) {
        super(new BorderLayout(0, 16));
        this.controlador = controlador;
        setBorder(Estilo.margen(20));

        JLabel ayuda = new JLabel("Escanee el carnet con el lector o escribalo y presione Enter.");
        ayuda.setFont(Estilo.fuente(Font.PLAIN, 15));

        JLabel etiquetaCampo = new JLabel("Carnet:");
        etiquetaCampo.setFont(Estilo.fuente(Font.BOLD, 22));
        campo.setFont(Estilo.fuente(Font.BOLD, 30));
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.addActionListener(e -> validar());
        JButton boton = new JButton("Validar");
        boton.setFont(Estilo.fuente(Font.BOLD, 18));
        boton.addActionListener(e -> validar());

        JPanel entrada = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        entrada.add(etiquetaCampo);
        entrada.add(campo);
        entrada.add(boton);

        JPanel arriba = new JPanel();
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        for (Component c : new Component[]{Estilo.titulo("Validacion de abordaje"), ayuda, Box.createVerticalStrut(12), entrada}) {
            ((javax.swing.JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
            arriba.add(c);
        }
        add(arriba, BorderLayout.NORTH);

        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(veredicto);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(nombre);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(detalle);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(motivo);
        tarjeta.add(Box.createVerticalGlue());
        add(tarjeta, BorderLayout.CENTER);

        contador.setFont(Estilo.fuente(Font.PLAIN, 16));
        add(contador, BorderLayout.SOUTH);

        reinicio = new Timer(6000, e -> mostrarEspera());
        reinicio.setRepeats(false);

        // Cada vez que la pestana se muestra, el cursor queda listo en el campo para el lector.
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent e) {
                campo.requestFocusInWindow();
            }

            public void ancestorRemoved(AncestorEvent e) {
            }

            public void ancestorMoved(AncestorEvent e) {
            }
        });

        mostrarEspera();
        actualizar();
    }

    private void validar() {
        String lectura = campo.getText();
        campo.setText("");
        campo.requestFocusInWindow();
        if (lectura.trim().isEmpty()) {
            return;
        }
        try {
            mostrar(controlador.validarAbordaje(lectura));
        } catch (RuntimeException ex) {
            pintar(Estilo.NARANJA, "SIN CONEXION", "No se pudo validar", "Intente de nuevo o use la lista en papel",
                    Estilo.limpiar(ex.getMessage()));
        }
        actualizar();
        reinicio.restart();
    }

    /** Muestra en grande el veredicto para que el piloto lo vea de un vistazo. */
    public void mostrar(ResultadoValidacion r) {
        Persona p = r.getPersona();
        String quien = p == null ? "Persona no registrada" : p.getNombreCompleto();
        String datos = p == null
                ? "Lectura: " + r.getRegistro().getCarnetLeido()
                : p.getTipo().getDescripcion() + "  |  " + p.getDetalle() + "  |  Carnet " + p.getCarnet();
        if (r.isAceptado()) {
            pintar(Estilo.VERDE, "ACEPTADO", quien, datos, r.getRegistro().getMotivo());
        } else {
            Toolkit.getDefaultToolkit().beep();
            pintar(Estilo.ROJO, "RECHAZADO", quien, datos, r.getRegistro().getMotivo());
        }
    }

    private void mostrarEspera() {
        pintar(Estilo.GRIS, "ESPERANDO CARNET", " ", "El resultado aparecera aqui", " ");
    }

    private void pintar(Color fondo, String v, String n, String d, String m) {
        tarjeta.setBackground(fondo);
        veredicto.setText(v);
        nombre.setText(n);
        detalle.setText(d);
        motivo.setText(m);
    }

    @Override
    public void actualizar() {
        try {
            contador.setText("Abordajes aceptados hoy: " + controlador.aceptadosHoy());
        } catch (RuntimeException ex) {
            contador.setText("Abordajes aceptados hoy: sin conexion");
        }
        campo.requestFocusInWindow();
    }

    private static JLabel etiqueta(int tamano, int estilo) {
        JLabel l = new JLabel(" ", SwingConstants.CENTER);
        l.setFont(Estilo.fuente(estilo, tamano));
        l.setForeground(Color.WHITE);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
}
