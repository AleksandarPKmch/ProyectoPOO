package carnets;

import carnets.modelo.Carrera;
import carnets.modelo.Catedratico;
import carnets.modelo.Estudiante;
import carnets.modelo.Persona;
import carnets.modelo.Personal;
import carnets.modelo.RegistroAbordaje;
import carnets.repositorio.Repositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Personas inventadas y un dia simulado de abordajes, para que la demostracion
 * funcione sin internet y las estadisticas no se vean vacias.
 */
public final class DatosDePrueba {

    private DatosDePrueba() {
    }

    public static void cargar(Repositorio repo) {
        if (!repo.listarPersonas().isEmpty()) {
            return;
        }
        List<Persona> gente = List.of(
                new Estudiante(24101, "Ana", "Lopez", Carrera.INGENIERIA, true),
                new Estudiante(24102, "Luis", "Perez", Carrera.ARQUITECTURA, true),
                new Estudiante(24103, "Maria", "Diaz", Carrera.EDUCACION, true),
                new Estudiante(24104, "Jose", "Morales", Carrera.BUSINESS, true),
                new Estudiante(24105, "Sofia", "Ramirez", Carrera.CIENCIAS_SOCIALES, true),
                new Estudiante(24106, "Diego", "Castillo", Carrera.ART_SCHOOL, true),
                new Estudiante(24107, "Valeria", "Ortiz", Carrera.CIENCIAS_Y_HUMANIDADES, false),
                new Catedratico(10501, "Carlos", "Mendez", "Ciencias de la Computacion", true),
                new Catedratico(10502, "Lucia", "Herrera", "Matematica", true),
                new Personal(30801, "Pedro", "Gomez", "Mantenimiento", true),
                new Personal(30802, "Rosa", "Juarez", "Biblioteca", true));
        for (Persona p : gente) {
            repo.insertarPersona(p);
        }

        // Un dia simulado con mas gente en la entrada, al mediodia y a la salida, y algunos rechazos.
        // Si una hora todavia no ha pasado hoy, el abordaje se pone en el dia de ayer.
        int[] horas = {6, 7, 7, 7, 8, 9, 10, 11, 12, 12, 12, 13, 14, 15, 16, 17, 17, 17, 18, 19};
        Random azar = new Random(7);
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 0; i < 60; i++) {
            LocalDateTime cuando = ahora.toLocalDate()
                    .atTime(horas[azar.nextInt(horas.length)], azar.nextInt(60), azar.nextInt(60));
            if (!cuando.isBefore(ahora.minusMinutes(10))) {
                cuando = cuando.minusDays(1);
            }
            if (azar.nextInt(10) == 0) {
                repo.guardarAbordaje(new RegistroAbordaje(cuando, String.valueOf(90000 + azar.nextInt(9000)),
                        null, false, "Carnet no registrado en UVG"));
                continue;
            }
            Persona p = gente.get(azar.nextInt(gente.size()));
            boolean ok = p.isActivo();
            repo.guardarAbordaje(new RegistroAbordaje(cuando, String.valueOf(p.getCarnet()), p.getTipo(), ok,
                    ok ? "Acceso permitido" : "Carnet suspendido, debe pasar a Registro"));
        }
    }
}
