package carnets.vista;

import carnets.controlador.Controlador;
import carnets.modelo.Carrera;
import carnets.modelo.Persona;
import carnets.modelo.TipoPersona;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;

/** Pantalla del administrador: alta, suspension y baja de personas. */
public class PanelPersonas extends JPanel implements Actualizable {

    private static final String ACTIVO = "Activo";
    private static final String SUSPENDIDO = "Suspendido";

    private final Controlador controlador;

    private final JComboBox<TipoPersona> tipo = new JComboBox<>(TipoPersona.values());
    private final JTextField carnet = new JTextField(14);
    private final JTextField nombre = new JTextField(14);
    private final JTextField apellido = new JTextField(14);
    private final JLabel etiquetaDato = new JLabel();
    private final JComboBox<Carrera> carrera = new JComboBox<>(Carrera.values());
    private final JTextField datoTexto = new JTextField(14);
    private final CardLayout tarjetasDato = new CardLayout();
    private final JPanel panelDato = new JPanel(tarjetasDato);
    private final JLabel mensaje = new JLabel(" ");

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Carnet", "Nombre", "Tipo", "Detalle", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columna) {
            return columna == 0 ? Integer.class : String.class;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final TableRowSorter<DefaultTableModel> orden = new TableRowSorter<>(modelo);
    private final JTextField buscar = new JTextField(16);

    public PanelPersonas(Controlador controlador) {
        super(new BorderLayout(20, 0));
        this.controlador = controlador;
        setBorder(Estilo.margen(20));
        add(crearFormulario(), BorderLayout.WEST);
        add(crearLista(), BorderLayout.CENTER);
        cambiarTipo();
        actualizar();
    }

    private JPanel crearFormulario() {
        carrera.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int i, boolean sel, boolean foco) {
                super.getListCellRendererComponent(lista, valor, i, sel, foco);
                if (valor instanceof Carrera c) {
                    setText(c.getOpcion() + ". " + c.getDescripcion());
                }
                return this;
            }
        });
        panelDato.add(carrera, "carrera");
        panelDato.add(datoTexto, "texto");
        tipo.addActionListener(e -> cambiarTipo());

        JButton registrar = new JButton("Registrar");
        registrar.setFont(Estilo.fuente(Font.BOLD, 15));
        registrar.addActionListener(e -> registrar());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Registrar persona"), Estilo.margen(10)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        fila(form, g, 0, new JLabel("Tipo"), tipo);
        fila(form, g, 1, new JLabel("Carnet UVG"), carnet);
        fila(form, g, 2, new JLabel("Nombre"), nombre);
        fila(form, g, 3, new JLabel("Apellido"), apellido);
        fila(form, g, 4, etiquetaDato, panelDato);

        g.gridx = 0;
        g.gridy = 5;
        g.gridwidth = 2;
        form.add(registrar, g);
        g.gridy = 6;
        form.add(mensaje, g);
        g.gridy = 7;
        g.weighty = 1;
        form.add(new JPanel(), g);

        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.add(form, BorderLayout.NORTH);
        return envoltura;
    }

    private static void fila(JPanel form, GridBagConstraints g, int y, Component etiqueta, Component campo) {
        g.gridy = y;
        g.gridx = 0;
        g.weightx = 0;
        form.add(etiqueta, g);
        g.gridx = 1;
        g.weightx = 1;
        form.add(campo, g);
    }

    private JPanel crearLista() {
        tabla.setRowSorter(orden);
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);

        buscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        arriba.add(Estilo.titulo("Personas registradas"));
        arriba.add(new JLabel("   Buscar:"));
        arriba.add(buscar);

        JButton estado = new JButton("Suspender o activar");
        estado.addActionListener(e -> cambiarEstado());
        JButton eliminar = new JButton("Eliminar");
        eliminar.addActionListener(e -> eliminar());
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        abajo.add(estado);
        abajo.add(eliminar);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(arriba, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private void cambiarTipo() {
        TipoPersona t = (TipoPersona) tipo.getSelectedItem();
        if (t == TipoPersona.ESTUDIANTE) {
            etiquetaDato.setText("Carrera");
            tarjetasDato.show(panelDato, "carrera");
        } else {
            etiquetaDato.setText(t == TipoPersona.CATEDRATICO ? "Departamento" : "Area");
            tarjetasDato.show(panelDato, "texto");
        }
    }

    private void registrar() {
        TipoPersona t = (TipoPersona) tipo.getSelectedItem();
        String dato = t == TipoPersona.ESTUDIANTE ? ((Carrera) carrera.getSelectedItem()).name() : datoTexto.getText();
        try {
            Persona p = controlador.registrarPersona(t, carnet.getText(), nombre.getText(), apellido.getText(), dato);
            avisar("Registrado: " + p.getNombreCompleto() + ", carnet " + p.getCarnet(), Estilo.VERDE);
            carnet.setText("");
            nombre.setText("");
            apellido.setText("");
            datoTexto.setText("");
            carnet.requestFocusInWindow();
            actualizar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            avisar(ex.getMessage(), Estilo.ROJO);
        } catch (RuntimeException ex) {
            avisar("Sin conexion: " + Estilo.limpiar(ex.getMessage()), Estilo.NARANJA);
        }
    }

    /** Carnet de la fila elegida, o null si no hay ninguna. */
    private Integer carnetSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            avisar("Primero elija una persona de la lista.", Estilo.ROJO);
            return null;
        }
        return (Integer) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
    }

    private void cambiarEstado() {
        Integer c = carnetSeleccionado();
        if (c == null) {
            return;
        }
        String actual = (String) modelo.getValueAt(tabla.convertRowIndexToModel(tabla.getSelectedRow()), 4);
        boolean activar = SUSPENDIDO.equals(actual);
        try {
            controlador.cambiarEstado(c, activar);
            avisar("Carnet " + c + (activar ? " activado." : " suspendido, ya no podra abordar."), Estilo.AZUL);
            actualizar();
        } catch (RuntimeException ex) {
            avisar("Sin conexion: " + Estilo.limpiar(ex.getMessage()), Estilo.NARANJA);
        }
    }

    private void eliminar() {
        Integer c = carnetSeleccionado();
        if (c == null) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Seguro que quiere eliminar el carnet " + c + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            boolean ok = controlador.eliminarPersona(c);
            avisar(ok ? "Carnet " + c + " eliminado." : "No se encontro el carnet " + c + ".", ok ? Estilo.AZUL : Estilo.ROJO);
            actualizar();
        } catch (RuntimeException ex) {
            avisar("Sin conexion: " + Estilo.limpiar(ex.getMessage()), Estilo.NARANJA);
        }
    }

    private void filtrar() {
        String t = buscar.getText().trim();
        orden.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(t)));
    }

    private void avisar(String texto, java.awt.Color color) {
        mensaje.setText("<html><div style='width:220px'>" + texto + "</div></html>");
        mensaje.setForeground(color);
    }

    @Override
    public void actualizar() {
        try {
            modelo.setRowCount(0);
            for (Persona p : controlador.listarPersonas()) {
                modelo.addRow(new Object[]{p.getCarnet(), p.getNombreCompleto(), p.getTipo().getDescripcion(),
                        p.getDetalle(), p.isActivo() ? ACTIVO : SUSPENDIDO});
            }
        } catch (RuntimeException ex) {
            avisar("Sin conexion: " + Estilo.limpiar(ex.getMessage()), Estilo.NARANJA);
        }
    }
}
