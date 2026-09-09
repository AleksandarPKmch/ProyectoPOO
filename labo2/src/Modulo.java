public class Modulo {
    private String codigo;
    private String nombre;
    private String tipo;
    private double consumoEnergia;
    private String estado;

    public Modulo(String codigo, String nombre, String tipo, double consumoEnergia, String estado) {
        this.codigo = validarTexto(codigo, "El codigo");
        this.nombre = validarTexto(nombre, "El nombre");
        this.tipo = validarTexto(tipo, "El tipo");
        this.estado = validarTexto(estado, "El estado");
        setConsumoEnergia(consumoEnergia);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public double getConsumoEnergia() {
        return consumoEnergia;
    }

    public String getEstado() {
        return estado;
    }

    public void setConsumoEnergia(double consumoEnergia) {
        if (consumoEnergia <= 0) {
            throw new IllegalArgumentException("El consumo de energia debe ser mayor que 0.");
        }
        this.consumoEnergia = consumoEnergia;
    }

    public void setEstado(String estado) {
        this.estado = validarTexto(estado, "El estado");
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo + " | Nombre: " + nombre + " | Tipo: " + tipo
                + " | Consumo: " + consumoEnergia + " | Estado: " + estado;
    }
}
