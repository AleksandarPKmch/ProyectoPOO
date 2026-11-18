package carnets.vista;

import carnets.controlador.Controlador;
import carnets.modelo.RegistroAbordaje;
import carnets.modelo.TipoPersona;
import carnets.servicio.Estadisticas;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/** Bitacora de todos los intentos y estadisticas para decidir rutas y horarios. */
public class PanelBitacora extends JPanel implements Actualizable {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Controlador controlador;
    private final JLabel total = cifra();
    private final JLabel aceptados = cifra();
    private final JLabel rechazados = cifra();
    private final JLabel porcentaje = cifra();
    private final JLabel pico = cifra();
    private final JLabel porTipo = new JLabel(" ");
    private final GraficaPorHora grafica = new GraficaPorHora();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Fecha y hora", "Carnet", "Tipo", "Resultado", "Motivo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public PanelBitacora(Controlador controlador) {
        super(new BorderLayout(0, 14));
        this.controlador = controlador;
        setBorder(Estilo.margen(20));

        JButton refrescar = new JButton("Actualizar");
        refrescar.addActionListener(e -> actualizar());
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(Estilo.titulo("Bitacora y estadisticas"), BorderLayout.WEST);
        encabezado.add(refrescar, BorderLayout.EAST);

        JPanel tarjetas = new JPanel(new GridLayout(1, 5, 10, 0));
        tarjetas.add(tarjeta("Intentos", total, Color.DARK_GRAY));
        tarjetas.add(tarjeta("Aceptados", aceptados, Estilo.VERDE));
        tarjetas.add(tarjeta("Rechazados", rechazados, Estilo.ROJO));
        tarjetas.add(tarjeta("% de rechazo", porcentaje, Estilo.ROJO));
        tarjetas.add(tarjeta("Hora pico", pico, Estilo.NARANJA));

        JPanel arriba = new JPanel(new BorderLayout(0, 12));
        arriba.add(encabezado, BorderLayout.NORTH);
        arriba.add(tarjetas, BorderLayout.CENTER);
        add(arriba, BorderLayout.NORTH);

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(140);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(105);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int f, int c) {
                super.getTableCellRendererComponent(t, v, sel, foco, f, c);
                setFont(getFont().deriveFont(Font.BOLD));
                if (!sel) {
                    setForeground("Aceptado".equals(v) ? Estilo.VERDE : Estilo.ROJO);
                }
                return this;
            }
        });

        porTipo.setFont(Estilo.fuente(Font.PLAIN, 13));
        porTipo.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 0));
        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230)));
        derecha.add(grafica, BorderLayout.CENTER);
        derecha.add(porTipo, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new BorderLayout(14, 0));
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        centro.add(derecha, BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);

        actualizar();
    }

    @Override
    public void actualizar() {
        try {
            Estadisticas e = controlador.estadisticas();
            total.setText(String.valueOf(e.getTotal()));
            aceptados.setText(String.valueOf(e.getAceptados()));
            rechazados.setText(String.valueOf(e.getRechazados()));
            porcentaje.setText(String.format("%.1f %%", e.getPorcentajeRechazo()));
            pico.setText(e.getHoraPico() < 0 ? "-" : String.format("%02d:00", e.getHoraPico()));
            grafica.setDatos(e.getAceptadosPorHora(), e.getHoraPico());

            StringBuilder sb = new StringBuilder("<html>Aceptados por tipo:&nbsp;&nbsp;");
            for (Map.Entry<TipoPersona, Integer> par : e.getAceptadosPorTipo().entrySet()) {
                sb.append(par.getKey().getDescripcion()).append(" <b>").append(par.getValue()).append("</b>&nbsp;&nbsp;");
            }
            porTipo.setText(sb.append("</html>").toString());

            modelo.setRowCount(0);
            for (RegistroAbordaje r : controlador.bitacora()) {
                modelo.addRow(new Object[]{r.getFechaHora().format(FORMATO), r.getCarnetLeido(),
                        r.getTipo() == null ? "Desconocido" : r.getTipo().getDescripcion(),
                        r.isAceptado() ? "Aceptado" : "Rechazado", r.getMotivo()});
            }
        } catch (RuntimeException ex) {
            porTipo.setText("Sin conexion: " + Estilo.limpiar(ex.getMessage()));
        }
    }

    private static JLabel cifra() {
        JLabel l = new JLabel("0", SwingConstants.CENTER);
        l.setFont(Estilo.fuente(Font.BOLD, 26));
        return l;
    }

    private static JPanel tarjeta(String titulo, JLabel valor, Color color) {
        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setFont(Estilo.fuente(Font.PLAIN, 13));
        valor.setForeground(color);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Estilo.FONDO_TARJETA);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230)), Estilo.margen(8)));
        p.add(t, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }
}
