package com.furryfriendshub.management;

import com.furryfriendshub.config.MongoDBConnection;
import com.furryfriendshub.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public Boolean updateProfile(User user) {
        // Retrieve user data from the User object
        String userID = user.getUserID();
        String newUserName = user.getUserName();
        String newEmail = user.getEmail();
        String newPassword = user.getPassword();
        String newPhoneNumber = user.getPhoneNumber();
        String newRole = user.getRole();
    
        // Find the user document in the database
        Document userDoc = usersCollection.find(new Document("userID", userID)).first();
        if (userDoc == null) {
            logger.error("User with ID {} not found.", userID);
            return false;
        }
    
        // Prepare the updates
        Document updates = new Document();
        if (newUserName != null && !newUserName.isEmpty()) updates.append("userName", newUserName);
        if (newEmail != null && !newEmail.isEmpty()) updates.append("email", newEmail);
        if (newPassword != null && !newPassword.isEmpty()) updates.append("password", hashPassword(newPassword));
        if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) updates.append("phoneNumber", newPhoneNumber);
        if (newRole != null && !newRole.isEmpty()) updates.append("role", newRole);
    
        // If there are updates, apply them
        if (!updates.isEmpty()) {
            updates.append("lastUpdatedAt", new Date());
            usersCollection.updateOne(Filters.eq("userID", userID), new Document("$set", updates));
            logger.info("User profile updated successfully for userID {}", userID);
            return true;
        }
        return false;
    }
    

    // Add this method to the UserManagement class
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        
        // Retrieve all documents from the "users" collection
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("users");

        for (Document doc : collection.find()) {
            // Extract fields from the document and create User objects
            String userID = doc.getString("userID");
            String userName = doc.getString("userName");
            String email = doc.getString("email");
            String password = doc.getString("password");
            String role = doc.getString("role");
            String phoneNumber = doc.getString("phoneNumber");
            Date registeredAt = doc.getDate("registeredAt");
            Date lastLoginAt = doc.getDate("lastLoginAt");

            // Create a User object and add to the list
            User user = new User(userID, userName, email, password, role, phoneNumber, registeredAt, lastLoginAt);
            usersList.add(user);
        }
        
        return usersList;
    }

    public User getUserByID(String userID) {
        // Retrieve the user by userID from the "users" collection
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("users");
    
        // Find the document with the specified userID
        Document doc = collection.find(Filters.eq("userID", userID)).first();
    
        if (doc != null) {
            // Extract fields from the document and create a User object
            String userName = doc.getString("userName");
            String email = doc.getString("email");
            String password = doc.getString("password");
            String role = doc.getString("role");
            String phoneNumber = doc.getString("phoneNumber");
            Date registeredAt = doc.getDate("registeredAt");
            Date lastLoginAt = doc.getDate("lastLoginAt");
    
            // Return the user object
            return new User(userID, userName, email, password, role, phoneNumber, registeredAt, lastLoginAt);
        }
    
        // Return null if the user with the given userID is not found
        return null;
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
