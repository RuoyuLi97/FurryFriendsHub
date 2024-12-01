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
	
	// Enable sharding for a given collection 
	// this needs to be done at the database level, so run the code in the MongoDB shell for full effect
	public static void enableSharding(String collectionName) {
		try {
			// Ensure sharding for the database
			mongoClient.getDatabase("admin").runCommand(new Document("enableSharding","furryFriendsHub"));
			System.out.println("Sharding enabled for database furryFriendsHub");
			
			// Shard the collection (typlically shard by a field such as userID or postID)
			mongoClient.getDatabase("admin").runCommand(new Document("shardCollection", "furryFriendsHub." + collectionName)
									.append("key", new Document("userID", 1))); // Example: shard by userID field
			System.out.println("Sharding enabled for collection: " + collectionName);
	} catch (Exception e){
		e.printStackTrace();
	}
	
	// Enable sharding for multiple collections (example for users, notifications, etc.)
	// do it afterwards on mongoDB Atlas
	public static void enableShardingForAllCollection(){
		enableSharding("users");
		enableSharding("notifications");
		enableSharding("adoptionListings");
		enableSharding("forumPosts");
	}
}