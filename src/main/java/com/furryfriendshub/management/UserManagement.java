package com.furryfriendshub.management;

import com.furryfriendshub.config.MongoDBConnection;
import com.furryfriendshub.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class UserManagement {
    private static final Logger logger = LoggerFactory.getLogger(UserManagement.class);

    private final MongoCollection<Document> usersCollection;

    public UserManagement() {
        this.usersCollection = MongoDBConnection.getDatabase().getCollection("users");
    }

    // Register a new user
    public boolean register(User user) {
        // Check if the user already exists by email
        Document existingUser = usersCollection.find(new Document("email", user.getEmail())).first();
        if (existingUser != null) {
            logger.warn("User with email {} already exists.", user.getEmail());
            return false; // User already exists
        }

        // Create a new user document
        Document newUser = new Document("userID", user.getUserID())
                .append("userName", user.getUserName())
                .append("email", user.getEmail())
                .append("password", user.getPassword()) // Store the hashed password
                .append("role", user.getRole())
                .append("phoneNumber", user.getPhoneNumber())
                .append("registeredAt", user.getRegisteredAt());

        usersCollection.insertOne(newUser);
        logger.info("User with email {} registered successfully.", user.getEmail());
        return true;
    }

    // Hash the password using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Check if the password matches the stored hashed password
    private boolean checkPassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }

    // Login a user
    public boolean login(String email, String inputPassword) {
        Document userDoc = usersCollection.find(new Document("email", email)).first();
        if (userDoc != null) {
            String storedHash = userDoc.getString("password");
            if (checkPassword(inputPassword, storedHash)) {
                // Update lastLoginAt field
                usersCollection.updateOne(
                        new Document("email", email),
                        new Document("$set", new Document("lastLoginAt", new Date()))
                );
                logger.info("Login successful for email {}", email);
                return true;
            }
        }
        logger.warn("Invalid login attempt for email {}", email);
        return false;
    }

    // Update a user profile
    public boolean updateProfile(String userID, String newUserName, String newEmail, String newPassword, String newPhoneNumber) {
        Document userDoc = usersCollection.find(new Document("userID", userID)).first();
        if (userDoc == null) {
            logger.error("User with ID {} not found.", userID);
            return false;
        }

        Document updates = new Document();
        if (newUserName != null && !newUserName.isEmpty()) updates.append("userName", newUserName);
        if (newEmail != null && !newEmail.isEmpty()) updates.append("email", newEmail);
        if (newPassword != null && !newPassword.isEmpty()) updates.append("password", hashPassword(newPassword));
        if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) updates.append("phoneNumber", newPhoneNumber);

        if (!updates.isEmpty()) {
            updates.append("lastUpdatedAt", new Date());
            usersCollection.updateOne(Filters.eq("userID", userID), new Document("$set", updates));
            logger.info("User profile updated successfully for userID {}", userID);
            return true;
        }
        return false;
    }

    // Delete a user
    public boolean delete(String userID) {
        Document userDoc = usersCollection.find(new Document("userID", userID)).first();
        if (userDoc != null) {
            usersCollection.deleteOne(new Document("userID", userID));
            logger.info("User with ID {} deleted successfully.", userID);
            return true;
        }
        logger.warn("User with ID {} not found.", userID);
        return false;
    }
}
