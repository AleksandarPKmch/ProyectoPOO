public class EstudianteUVG extends EstudianteData {
    private boolean estudianteValido;
    private RegistrarEstudiante registrarEstudiante;

    public EstudianteUVG() {
        this.estudianteValido = false;
        this.registrarEstudiante = new RegistrarEstudiante();
    }

    public boolean registrarEstudiante(String nombre, String apellido, String carrera) {
        if (nombre == null || nombre.trim().isEmpty()) {
            this.estudianteValido = false;
            return false;
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            this.estudianteValido = false;
            return false;
        }
        if (carrera == null || carrera.trim().isEmpty()) {
            this.estudianteValido = false;
            return false;
        }

        Carrera carreraEnum = Carrera.fromString(carrera);
        if (carreraEnum == null) {
            this.estudianteValido = false;
            return false;
        }

        int carnet = 1000 + registrarEstudiante.getEstudiantes().size() + 1;
        boolean ok = registrarEstudiante.registrarEstudiante(nombre, apellido, carreraEnum, carnet);

        if (ok) {
            setNombre(nombre.trim());
            setApellido(apellido.trim());
            setCarrera(carreraEnum);
            setCarnet(carnet);
            this.estudianteValido = true;
        } else {
            this.estudianteValido = false;
        }

        return ok;
    }

    public boolean isEstudianteValido() {
        return estudianteValido;
    }

    public EstudianteData getEstudianteData() {
        return this;
    }

    public RegistrarEstudiante getRegistrarEstudiante() {
        return registrarEstudiante;
    }
}
