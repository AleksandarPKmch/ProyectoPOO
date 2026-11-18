package carnets.modelo;

public class Estudiante extends Persona {

    private final Carrera carrera;

    public Estudiante(int carnet, String nombre, String apellido, Carrera carrera, boolean activo) {
        super(carnet, nombre, apellido, activo);
        this.carrera = carrera;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    @Override
    public TipoPersona getTipo() {
        return TipoPersona.ESTUDIANTE;
    }

    @Override
    public String getDetalle() {
        return carrera.getDescripcion();
    }
}
