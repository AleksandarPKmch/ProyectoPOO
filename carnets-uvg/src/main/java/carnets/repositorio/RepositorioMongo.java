package carnets.repositorio;

import carnets.modelo.Carrera;
import carnets.modelo.EstudianteData;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Guarda los estudiantes en MongoDB, base Universidad, coleccion estudiantes. */
public class RepositorioMongo implements EstudianteRepository {

    private static final int PRIMER_CARNET = 1001;

    private final MongoClient client;
    private final MongoCollection<Document> coleccion;

    /** Lanza una excepcion si no logra conectarse en unos 5 segundos. */
    public RepositorioMongo(String uri) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .applyToClusterSettings(b -> b.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();
        MongoClient nuevo = MongoClients.create(settings);
        MongoCollection<Document> col;
        try {
            nuevo.getDatabase("admin").runCommand(new Document("ping", 1));
            col = nuevo.getDatabase("Universidad").getCollection("estudiantes");
            col.createIndex(Indexes.ascending("carnet"), new IndexOptions().unique(true));
        } catch (RuntimeException e) {
            nuevo.close();
            throw e;
        }
        this.client = nuevo;
        this.coleccion = col;
    }

    @Override
    public String nombreAlmacenamiento() {
        return "MongoDB Atlas";
    }

    @Override
    public boolean insertar(EstudianteData e) {
        try {
            coleccion.insertOne(new Document("nombre", e.getNombre())
                    .append("apellido", e.getApellido())
                    .append("carrera", e.getCarrera().name())
                    .append("carnet", e.getCarnet()));
            return true;
        } catch (MongoException ex) {
            return false;
        }
    }

    @Override
    public EstudianteData buscarPorCarnet(int carnet) {
        Document d = coleccion.find(Filters.eq("carnet", carnet)).first();
        return d == null ? null : aEstudiante(d);
    }

    @Override
    public List<EstudianteData> listar() {
        List<EstudianteData> resultado = new ArrayList<>();
        for (Document d : coleccion.find().sort(Sorts.ascending("carnet"))) {
            resultado.add(aEstudiante(d));
        }
        return resultado;
    }

    @Override
    public boolean eliminarPorCarnet(int carnet) {
        return coleccion.deleteOne(Filters.eq("carnet", carnet)).getDeletedCount() > 0;
    }

    @Override
    public int siguienteCarnet() {
        Document ultimo = coleccion.find().sort(Sorts.descending("carnet")).limit(1).first();
        return ultimo == null ? PRIMER_CARNET : ultimo.getInteger("carnet") + 1;
    }

    @Override
    public void close() {
        client.close();
    }

    private EstudianteData aEstudiante(Document d) {
        return new EstudianteData(
                d.getString("nombre"),
                d.getString("apellido"),
                Carrera.valueOf(d.getString("carrera")),
                d.getInteger("carnet"));
    }
}
