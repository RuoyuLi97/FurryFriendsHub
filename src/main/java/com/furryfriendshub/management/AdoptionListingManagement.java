package com.furryfriendshub.management;

import com.furryfriendshub.config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AdoptionListingManagement {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionListingManagement.class);

    // Update an existing adoption listing
    public boolean updateListing(String listingID, String newDescription, Boolean isAvailable,
            Integer newPetAge, String newPetGender, Boolean petNeuteredSpayed) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> listingsCollection = database.getCollection("adoption_listings");
        
        // Find the listing by listingID
        Document listingDoc = listingsCollection.find(new Document("listingID", listingID)).first();
        if (listingDoc == null) {
            logger.error("Adoption listing with ID {} not found.", listingID);
            return false; // Adoption listing not found
        }
        
        if (newDescription != null && !newDescription.isEmpty()) {
            listingDoc.put("description", newDescription);
        }

        if (isAvailable != null) {
            listingDoc.put("isAvailable", isAvailable);
        }

        if (newPetAge != null && newPetAge > 0) {
            listingDoc.put("petAge", newPetAge);
        }

        if (newPetGender != null && !newPetGender.isEmpty()) {
            listingDoc.put("petGender", newPetGender);
        }

        if (petNeuteredSpayed != null) {
            listingDoc.put("petNeuteredSpayed", petNeuteredSpayed);
        }
        
        listingDoc.put("lastUpdatedAt", new Date());
        // Save the updated document back into the collection
        listingsCollection.replaceOne(new Document("listingID", listingID), listingDoc);
        logger.info("Adoption listing with ID {} updated successfully.", listingID);
        return true;
    }

    // Delete the adoption listing from the database
    public boolean deleteListing(String listingID) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> listingsCollection = database.getCollection("adoption_listings");

        // Check if the listing exists
        long deletedCount = listingsCollection.deleteOne(new Document("listingID", listingID)).getDeletedCount();
        if (deletedCount > 0) {
            logger.info("Adoption listing with ID {} deleted successfully.", listingID);
            return true;
        } else {
            logger.warn("Adoption listing with ID {} not found.", listingID);
            return false; // Listing not found
        }
    }
}
