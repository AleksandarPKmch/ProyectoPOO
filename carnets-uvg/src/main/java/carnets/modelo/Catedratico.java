package carnets.modelo;

public class Catedratico extends Persona {

    private final String departamento;

    public Catedratico(int carnet, String nombre, String apellido, String departamento, boolean activo) {
        super(carnet, nombre, apellido, activo);
        this.departamento = departamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    @Override
    public TipoPersona getTipo() {
        return TipoPersona.CATEDRATICO;
    }

    @Override
    public String getDetalle() {
        return "Depto. " + departamento;
    }
}
