package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import com.furryfriendshub.config.MongoDBConnection;
import lombok.Data;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import java.util.Date;

@Data // Lombok generates getters, setters, toString, equals, hashCode for all fields
public class AdoptionListing {

    private static final Logger logger = LoggerFactory.getLogger(AdoptionListing.class);

    private String listingID;
    private String petName;
    private String petType; // Type of pet (e.g., dog, cat)
    private String description; // Description of the pet
    private String ownerID; // ID of the user who owns the pet
    private Integer petAge; // Age of the pet (can be null if not provided)
    private String petGender; // Gender of the pet (can be null if not provided)
    private Boolean petNeuteredSpayed; // Neutered/spayed status (can be null if not provided)
    private Date createdAt; // Timestamp for when the listing was created
    private Date lastUpdatedAt; // Timestamp for when the listing was last updated
    private Boolean isAvailable; // Whether the pet is still available for adoption

    // Constructor to create a new adoption listing
    // check if ownerID is in the database
    public AdoptionListing(String petName, String petType, String description, String ownerID,
            Integer petAge, String petGender, Boolean petNeuteredSpayed) {
        this.listingID = IDGenerator.generateId(IDGenerator.EntityType.ADOPTION_LISTING); // Generate a unique listing id
        this.petName = petName;
        this.petType = petType;
        this.description = description;
        this.ownerID = ownerID;
        this.petAge = petAge; // Default is null if not provided
        this.petGender = petGender; // Default is null if not provided
        this.petNeuteredSpayed = petNeuteredSpayed; // Default is null if not provided
        this.createdAt = new Date(); // Set the creation date to the current time
        this.lastUpdatedAt = this.createdAt; // Initially, the last updated date is the same as the created date
        this.isAvailable = true; // Default to true, meaning the pet is available for adoption
    }

    // Save the adoption listing to the database (MongoDB)
    public boolean saveListing() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("adoption_listings");

            // Check if a listing with the same pet details already exists
            Document existingListing = collection.find(Filters.and(
                    Filters.eq("petName", this.petName),
                    Filters.eq("petType", this.petType),
                    Filters.eq("ownerID", this.ownerID),
                    Filters.eq("isAvailable", true) // Ensure the pet is still available for adoption
            )).first();

            if (existingListing != null) {
                logger.warn("Adoption listing already exists for this pet: {} ({})", this.petName, this.petType);
                return false; // Return false if a listing with the same pet already exists
            }

            // Create a document representing the adoption listing
            Document listingDoc = new Document("listingID", this.listingID)
                    .append("petName", this.petName)
                    .append("petType", this.petType)
                    .append("description", this.description)
                    .append("ownerID", this.ownerID)
                    .append("petAge", this.petAge) // Pet age can be null if not provided
                    .append("petGender", this.petGender) // Pet gender can be null if not provided
                    .append("petNeuteredSpayed", this.petNeuteredSpayed) // Pet neutered/spayed status can be null if not provided
                    .append("createdAt", this.createdAt)
                    .append("lastUpdatedAt", this.lastUpdatedAt)
                    .append("isAvailable", this.isAvailable);

            // Insert the document into the "adoption_listings" collection
            collection.insertOne(listingDoc);

            logger.info("Adoption listing saved successfully with ID: {}", this.listingID);
            return true;
        } catch (Exception e) {
            logger.error("Error saving adoption listing: ", e);
            return false;
        }
    }

}
