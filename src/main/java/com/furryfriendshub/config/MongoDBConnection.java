package com.furryfriendshub.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBConnection {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBConnection.class);
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // Initialize MongoDB client
    static {
        try {
            // Use your MongoDB URI (could be extracted from a config file or environment variable)
            String uri = "mongodb://localhost:27017"; // Default URI (adjust for your environment)
            
            // Create MongoClient using the newer MongoClients API
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("furryFriendsHub"); // Database name
            logger.info("MongoDB connection established successfully!");
        } catch (Exception e) {
            logger.error("Failed to establish MongoDB connection: ", e);
        }
    }

    // Get the MongoDB database instance
    public static MongoDatabase getDatabase() {
        return database;
    }

    // Get the MongoDB client instance
    public static MongoClient getClient() {
        return mongoClient;
    }

    // Close the MongoDB client to free resources
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("MongoDB connection closed.");
        }
    }
}
