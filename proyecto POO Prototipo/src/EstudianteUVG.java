public class EstudianteUVG {
    private boolean estudianteValido;
    private RegistrarEstudiante registrarEstudiante;
    private EstudianteData estudianteData;

    public EstudianteUVG() {
        this.estudianteValido = false;
        this.registrarEstudiante = new RegistrarEstudiante();
        this.estudianteData = new EstudianteData();
    }

    // validar nombre, apellido y carrera.
    // Luego crear estudiante y delegar registro a RegistrarEstudiante.
    public boolean registrarEstudiante(String nombre, String apellido, String carrera) {
        return false;
    }

    public boolean isEstudianteValido() {
        return estudianteValido;
    }

    public EstudianteData getEstudianteData() {
        return estudianteData;
    }

    public RegistrarEstudiante getRegistrarEstudiante() {
        return registrarEstudiante;
    }
}

