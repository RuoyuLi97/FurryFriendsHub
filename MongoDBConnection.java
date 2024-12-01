import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDBConnection{
	private static MongoClient mongoClient;
	private static MongoDatabase database;
	
	// Initialize MongoDB client
	static{
		try {
			String uri = "mongodb://localhost:27017"; // Change to your MongoDB URI if needed
			mongoClient = new MongoClient(new MongoClientURI(uri));
			database = mongoClient.getDatabase("furryFriendsHub");
			System.out.println("MongoDB connection established successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to establish MongoDB connection.");
		}
	}
	
	// Get the MongoDB database instance
	public static MongoDatabase getDatabase(){
		return database;
	}
	
	// Get the MongoDB client instance
	public static MongoClient getClient(){
		return mongoClient;
	}
	
	// Close the MongoDB client to free resources
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}