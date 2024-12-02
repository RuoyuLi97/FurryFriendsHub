package com.furryfriendshub;

import com.furryfriendshub.config.MongoDBConnection;
import com.mongodb.client.MongoDatabase;

public class FurryFriendsHubApplication {
    public static void main(String[] args) {
        // Attempt to get the MongoDB database
        MongoDatabase database = MongoDBConnection.getDatabase();
        // Close the connection when done
        MongoDBConnection.closeConnection();
    }
}
