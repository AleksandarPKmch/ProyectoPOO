package carnets.modelo;

/** Personal administrativo, de servicio o colaboradores de UVG. */
public class Personal extends Persona {

    private final String area;

    public Personal(int carnet, String nombre, String apellido, String area, boolean activo) {
        super(carnet, nombre, apellido, activo);
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    @Override
    public TipoPersona getTipo() {
        return TipoPersona.PERSONAL;
    }

    @Override
    public String getDetalle() {
        return "Area " + area;
    }
}
