package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import com.furryfriendshub.config.MongoDBConnection;
import org.springframework.data.annotation.Id;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class); // Logger instance

    @Id // Marking userID as the primary key (ID) for MongoDB
    protected String userID;
    private String userName;
    private String email;
    private String password;
    protected String role;

    // Constructor to initialize User with required information (only if needed
    // outside of Lombok)
    public User(String userName, String email, String password, String role) {
        this.userID = IDGenerator.generateId(IDGenerator.EntityType.USER); // Generate User ID
        this.userName = userName;
        this.email = email;
        this.password = hashPassword(password); // Hash the password
        this.role = role;
    }

    // Hash the password using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Check if the password matches the stored hashed password
    private boolean checkPassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }

    // Register a new user by storing the user data in MongoDB
    public boolean register() {
        MongoDatabase database = MongoDBConnection.getDatabase(); // Use MongoDBConnection class to get the database
        MongoCollection<Document> usersCollection = database.getCollection("users");

        // Check if the user already exists by email
        Document existingUser = usersCollection.find(new Document("email", this.email)).first();
        if (existingUser != null) {
            logger.warn("User with email {} already exists.", this.email);
            return false; // User already exists
        }

        // Create a new user document to insert into MongoDB
        Document newUser = new Document("userID", this.userID)
                .append("userName", this.userName)
                .append("email", this.email)
                .append("password", this.password) // Store the hashed password
                .append("role", this.role);

        // Insert the user document into the users collection
        usersCollection.insertOne(newUser);
        logger.info("User with email {} registered successfully.", this.email);
        return true;
    }

    // Login: Check if the credentials are correct
    public boolean login(String inputPassword) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> usersCollection = database.getCollection("users");

        // Find the user by email
        Document userDoc = usersCollection.find(new Document("email", this.email)).first();
        if (userDoc != null && checkPassword(inputPassword, userDoc.getString("password"))) {
            logger.info("Login successful for user {}", this.userName);
            return true; // Login successful
        } else {
            logger.warn("Invalid login attempt for email {}", this.email);
            return false; // Invalid credentials
        }
    }

    // Update user profile
    public boolean updateProfile(String newUserName, String newEmail, String newPassword) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> usersCollection = database.getCollection("users");

        // Find the user by userID
        Document userDoc = usersCollection.find(new Document("userID", this.userID)).first();
        if (userDoc == null) {
            logger.error("User with ID {} not found.", this.userID);
            return false; // User not found
        }

        // Update the user details if provided
        if (newUserName != null) {
            userDoc.put("userName", newUserName);
        }
        if (newEmail != null) {
            userDoc.put("email", newEmail);
        }
        if (newPassword != null) {
            userDoc.put("password", hashPassword(newPassword)); // Hash the new password before saving
        }

        // Save the updated document back into the collection
        usersCollection.replaceOne(new Document("userID", this.userID), userDoc);
        logger.info("Profile for user {} updated successfully.", this.userID);
        return true;
    }
}
