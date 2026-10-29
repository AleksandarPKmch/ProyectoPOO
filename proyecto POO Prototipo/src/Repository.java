import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Repository {

    String url = "mongodb+srv://JavierBac:7NzjsWBBgGSDnl85@estudiantes.u5acmlq.mongodb.net/?appName=Estudiantes";
    MongoCollection<Document> collection;

    public Repository() {
        try{
            this.collection = MongoClients.create(url).getDatabase("Universidad").getCollection("Estudiantes");
            
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public void insertarEstudiante(String nombre, String apellido, Carrera carrera, int carnet){
        Document estudiante = new Document("Nombre", nombre)
                .append("Apellido", apellido)
                .append("Carrera", carrera.toString())
                .append("Carnet", carnet);
        
        try{
            collection.insertOne(estudiante);
            System.out.println("Estudiante insertado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al insertar estudiante: " + e.getMessage());
        }
    }

    public void eliminarEstudiante(int carnet){
        Document estudiante = new Document("Carnet", carnet);

        try{
            collection.deleteOne(estudiante);
            System.out.println("Estudiante eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
        }
    }

    public Document consultarEstudiante(int carnet){
        Document estudiante = new Document("Carnet", carnet);

        try{
            Document result = collection.find(estudiante).first();
            if(result != null){
                result.remove("_id");
                return result;
            } else {
                System.out.println("Estudiante no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Error al consultar estudiante: " + e.getMessage());
        }

        return null;
    }
}
