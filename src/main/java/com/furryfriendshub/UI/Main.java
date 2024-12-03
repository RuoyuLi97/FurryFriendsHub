package com.furryfriendshub.UI;

import com.furryfriendshub.config.MongoDBConnection;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize MongoDB connection
            MongoDBConnection.getDatabase();
            System.out.println("MongoDB connection established successfully!");

            // Launch the LoginForm
            new LoginUI();

        } catch (Exception e) {
            System.err.println("Error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        }

        // Close the MongoDB connection when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MongoDBConnection.closeConnection();
            System.out.println("MongoDB connection closed.");
        }));
    }
}
