package carnets.repositorio;

import carnets.modelo.Carrera;
import carnets.modelo.Catedratico;
import carnets.modelo.Estudiante;
import carnets.modelo.Persona;
import carnets.modelo.Personal;
import carnets.modelo.RegistroAbordaje;
import carnets.modelo.TipoPersona;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Guarda en MongoDB Atlas, base Universidad, colecciones personas y abordajes. */
public class RepositorioMongo implements Repositorio {

    private static final ZoneId ZONA = ZoneId.systemDefault();

    private final MongoClient client;
    private final MongoCollection<Document> personas;
    private final MongoCollection<Document> abordajes;

    /** Lanza una excepcion si no logra conectarse en unos 5 segundos. */
    public RepositorioMongo(String uri) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .applyToClusterSettings(b -> b.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();
        MongoClient nuevo = MongoClients.create(settings);
        try {
            nuevo.getDatabase("admin").runCommand(new Document("ping", 1));
            this.personas = nuevo.getDatabase("Universidad").getCollection("personas");
            this.abordajes = nuevo.getDatabase("Universidad").getCollection("abordajes");
            personas.createIndex(Indexes.ascending("carnet"), new IndexOptions().unique(true));
            abordajes.createIndex(Indexes.compoundIndex(Indexes.ascending("carnetLeido"), Indexes.descending("fechaHora")));
        } catch (RuntimeException e) {
            nuevo.close();
            throw e;
        }
        this.client = nuevo;
    }

    @Override
    public String nombreAlmacenamiento() {
        return "MongoDB Atlas";
    }

    @Override
    public boolean insertarPersona(Persona p) {
        try {
            personas.insertOne(aDocumento(p));
            return true;
        } catch (MongoException e) {
            return false; // carnet repetido por el indice unico, o fallo de red
        }
    }

    @Override
    public Persona buscarPersona(int carnet) {
        Document d = personas.find(Filters.eq("carnet", carnet)).first();
        return d == null ? null : aPersona(d);
    }

    @Override
    public List<Persona> listarPersonas() {
        List<Persona> lista = new ArrayList<>();
        for (Document d : personas.find().sort(Sorts.ascending("carnet"))) {
            Persona p = aPersona(d);
            if (p != null) {
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public boolean eliminarPersona(int carnet) {
        return personas.deleteOne(Filters.eq("carnet", carnet)).getDeletedCount() > 0;
    }

    @Override
    public boolean actualizarActivo(int carnet, boolean activo) {
        return personas.updateOne(Filters.eq("carnet", carnet), Updates.set("activo", activo)).getMatchedCount() > 0;
    }

    @Override
    public void guardarAbordaje(RegistroAbordaje r) {
        abordajes.insertOne(new Document("fechaHora", Date.from(r.getFechaHora().atZone(ZONA).toInstant()))
                .append("carnetLeido", r.getCarnetLeido())
                .append("tipo", r.getTipo() == null ? null : r.getTipo().name())
                .append("aceptado", r.isAceptado())
                .append("motivo", r.getMotivo()));
    }

    @Override
    public List<RegistroAbordaje> listarAbordajes() {
        List<RegistroAbordaje> lista = new ArrayList<>();
        for (Document d : abordajes.find().sort(Sorts.descending("fechaHora")).limit(5000)) {
            lista.add(aRegistro(d));
        }
        return lista;
    }

    @Override
    public RegistroAbordaje ultimoAceptado(int carnet) {
        Document d = abordajes.find(Filters.and(Filters.eq("carnetLeido", String.valueOf(carnet)),
                        Filters.eq("aceptado", true)))
                .sort(Sorts.descending("fechaHora")).first();
        return d == null ? null : aRegistro(d);
    }

    @Override
    public void close() {
        client.close();
    }

    // ---------- conversion entre objetos y documentos ----------

    private static Document aDocumento(Persona p) {
        Document d = new Document("carnet", p.getCarnet())
                .append("nombre", p.getNombre())
                .append("apellido", p.getApellido())
                .append("tipo", p.getTipo().name())
                .append("activo", p.isActivo());
        if (p instanceof Estudiante e) {
            d.append("carrera", e.getCarrera().name());
        } else if (p instanceof Catedratico c) {
            d.append("departamento", c.getDepartamento());
        } else if (p instanceof Personal s) {
            d.append("area", s.getArea());
        }
        return d;
    }

    /** Devuelve null si el documento esta incompleto, asi un dato malo no tumba la lista. */
    private static Persona aPersona(Document d) {
        try {
            int carnet = ((Number) d.get("carnet")).intValue();
            String nombre = d.getString("nombre");
            String apellido = d.getString("apellido");
            boolean activo = d.getBoolean("activo", true);
            switch (TipoPersona.valueOf(d.getString("tipo"))) {
                case ESTUDIANTE:
                    Carrera carrera = Carrera.fromString(d.getString("carrera"));
                    return carrera == null ? null : new Estudiante(carnet, nombre, apellido, carrera, activo);
                case CATEDRATICO:
                    return new Catedratico(carnet, nombre, apellido, d.getString("departamento"), activo);
                default:
                    return new Personal(carnet, nombre, apellido, d.getString("area"), activo);
            }
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static RegistroAbordaje aRegistro(Document d) {
        String tipo = d.getString("tipo");
        return new RegistroAbordaje(
                LocalDateTime.ofInstant(d.getDate("fechaHora").toInstant(), ZONA),
                d.getString("carnetLeido"),
                tipo == null ? null : TipoPersona.valueOf(tipo),
                d.getBoolean("aceptado", false),
                d.getString("motivo"));
    }
}
