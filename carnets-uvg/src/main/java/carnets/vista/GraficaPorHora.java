package carnets.vista;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Grafica de barras simple dibujada a mano: abordajes aceptados por hora del dia. */
public class GraficaPorHora extends JPanel {

    private int[] porHora = new int[24];
    private int horaPico = -1;

    public GraficaPorHora() {
        setPreferredSize(new Dimension(420, 260));
        setBackground(Color.WHITE);
    }

    public void setDatos(int[] porHora, int horaPico) {
        this.porHora = porHora;
        this.horaPico = horaPico;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Se muestran las horas de 5 a 21 y se amplia si hay datos fuera de ese rango.
        int desde = 5;
        int hasta = 21;
        int max = 1;
        for (int h = 0; h < 24; h++) {
            if (porHora[h] > 0) {
                desde = Math.min(desde, h);
                hasta = Math.max(hasta, h);
                max = Math.max(max, porHora[h]);
            }
        }

        int izq = 16;
        int arriba = 34;
        int abajo = 26;
        int ancho = getWidth() - izq * 2;
        int alto = getHeight() - arriba - abajo;
        int barras = hasta - desde + 1;
        int paso = Math.max(1, ancho / barras);

        g.setColor(Color.DARK_GRAY);
        g.setFont(Estilo.fuente(Font.BOLD, 14));
        g.drawString("Abordajes aceptados por hora", izq, 20);

        g.setFont(Estilo.fuente(Font.PLAIN, 11));
        FontMetrics fm = g.getFontMetrics();
        for (int h = desde; h <= hasta; h++) {
            int x = izq + (h - desde) * paso;
            int altoBarra = (int) Math.round((double) porHora[h] / max * (alto - 14));
            int y = arriba + alto - altoBarra;
            g.setColor(h == horaPico ? Estilo.NARANJA : Estilo.AZUL);
            g.fillRoundRect(x + 2, y, paso - 4, altoBarra, 4, 4);
            g.setColor(Color.DARK_GRAY);
            if (porHora[h] > 0) {
                String n = String.valueOf(porHora[h]);
                g.drawString(n, x + (paso - fm.stringWidth(n)) / 2, y - 3);
            }
            String hora = String.valueOf(h);
            g.drawString(hora, x + (paso - fm.stringWidth(hora)) / 2, arriba + alto + 16);
        }
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(izq, arriba + alto, izq + barras * paso, arriba + alto);
    }
}
