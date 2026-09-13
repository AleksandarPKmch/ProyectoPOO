import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Repository {

    public Repository() {
        String uri = "mongodb+srv://JavierBac:7NzjsWBBgGSDnl85@estudiantes.u5acmlq.mongodb.net/?appName=Estudiantes"; 

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Universidad");
            MongoCollection<Document> collection = database.getCollection("Estudiantes");

            Document estudiante = new Document("nombre", "NombrePrueba")
                    .append("edad", 25)
                    .append("carrera", "CarreraPrueba");

            collection.insertOne(estudiante);
            System.out.println("Estudiante insertado correctamente.");

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        
    }
}
