package carnets.controlador;

import carnets.modelo.Persona;
import carnets.modelo.RegistroAbordaje;
import carnets.modelo.TipoPersona;
import carnets.repositorio.Repositorio;
import carnets.servicio.AbordajeService;
import carnets.servicio.Estadisticas;
import carnets.servicio.PersonaService;
import carnets.servicio.ResultadoValidacion;

import java.util.List;

/**
 * El controlador del patron MVC. Las vistas, grafica o de consola, solo hablan con esta clase;
 * nunca tocan los servicios ni la base de datos directamente.
 */
public class Controlador {

    private final Repositorio repositorio;
    private final PersonaService personas;
    private final AbordajeService abordajes;

    public Controlador(Repositorio repositorio) {
        this.repositorio = repositorio;
        this.personas = new PersonaService(repositorio);
        this.abordajes = new AbordajeService(repositorio);
    }

    public String almacenamiento() {
        return repositorio.nombreAlmacenamiento();
    }

    public ResultadoValidacion validarAbordaje(String lectura) {
        return abordajes.validar(lectura);
    }

    public Persona registrarPersona(TipoPersona tipo, String carnet, String nombre, String apellido, String dato) {
        return personas.registrar(tipo, carnet, nombre, apellido, dato);
    }

    public List<Persona> listarPersonas() {
        return personas.listar();
    }

    public boolean eliminarPersona(int carnet) {
        return personas.eliminar(carnet);
    }

    public boolean cambiarEstado(int carnet, boolean activo) {
        return personas.cambiarEstado(carnet, activo);
    }

    public List<RegistroAbordaje> bitacora() {
        return abordajes.bitacora();
    }

    public Estadisticas estadisticas() {
        return abordajes.estadisticas();
    }

    public int aceptadosHoy() {
        return abordajes.aceptadosHoy();
    }

    public void cerrar() {
        repositorio.close();
    }
}
