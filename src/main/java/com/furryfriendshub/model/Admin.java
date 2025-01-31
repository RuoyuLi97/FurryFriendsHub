package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import com.furryfriendshub.config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    private static final Logger logger = LoggerFactory.getLogger(Admin.class); // Logger instance

    // Constructor for Admin class
    public Admin(String userName, String email, String password, String phoneNumber) {
        super(userName, email, password, "Admin", phoneNumber);
        this.userID = IDGenerator.generateId(IDGenerator.EntityType.ADMIN); // Admin-specific ID format
    }

    // Admin-only method to access analytics
    public void accessAnalytics() {
        if (!"Admin".equals(this.role)) {
            logger.warn("Access denied. Admins only.");
            return; // Only Admin can access analytics
        }

        try {
            MongoDatabase database = MongoDBConnection.getDatabase();
            MongoCollection<Document> analyticsCollection = database.getCollection("analytics");

            // Sample query to fetch analytics data
            Document analyticsData = analyticsCollection.find().first();
            if (analyticsData != null) {
                logger.info("Analytics Data: {}", analyticsData.toJson());
            } else {
                logger.info("No analytics data available.");
            }
        } catch (Exception e) {
            logger.error("Error accessing analytics: {}", e.getMessage(), e);
        }
    }

    // Admin-only method to assign or modify user roles
    public void assignRole(String userID, String newRole) {
        if (!"Admin".equals(this.role)) {
            logger.warn("Access denied. Admins only.");
            return; // Only Admin can assign roles
        }

        try {
            MongoDatabase database = MongoDBConnection.getDatabase();
            MongoCollection<Document> usersCollection = database.getCollection("users");

            // Find the user by userID
            Document userDoc = usersCollection.find(new Document("userID", userID)).first();
            if (userDoc != null) {
                // Update the user's role
                userDoc.put("role", newRole);
                usersCollection.replaceOne(new Document("userID", userID), userDoc);
                logger.info("User role for userID {} updated successfully to {}", userID, newRole);
            } else {
                logger.warn("User with userID {} not found.", userID);
            }
        } catch (Exception e) {
            logger.error("Error assigning role: {}", e.getMessage(), e);
        }
    }
}