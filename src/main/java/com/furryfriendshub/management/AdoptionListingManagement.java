package com.furryfriendshub.management;

import com.furryfriendshub.config.MongoDBConnection;
import com.furryfriendshub.model.AdoptionListing;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdoptionListingManagement {
    private static final Logger logger = LoggerFactory.getLogger(AdoptionListingManagement.class);

    // Get all adoption listings from the database
    public List<AdoptionListing> getAllListings() {
        List<AdoptionListing> listings = new ArrayList<>();
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("adoption_listings");

        for (Document doc : collection.find()) {
            AdoptionListing listing = new AdoptionListing(
                    doc.getString("petName"),
                    doc.getString("petType"),
                    doc.getString("description"),
                    doc.getString("ownerID"),
                    doc.getInteger("petAge"),
                    doc.getString("petGender"),
                    doc.getBoolean("petNeuteredSpayed")
            );
            listing.setListingID(doc.getString("listingID"));
            listing.setCreatedAt(doc.getDate("createdAt"));
            listing.setLastUpdatedAt(doc.getDate("lastUpdatedAt"));
            listing.setIsAvailable(doc.getBoolean("isAvailable"));
            listings.add(listing);
        }
        return listings;
    }

    // Get a single adoption listing by ID
    public AdoptionListing getListingByID(String listingID) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> listingsCollection = database.getCollection("adoption_listings");

        // Find the listing by listingID
        Document listingDoc = listingsCollection.find(new Document("listingID", listingID)).first();
        if (listingDoc != null) {
            AdoptionListing listing = new AdoptionListing(
                    listingDoc.getString("petName"),
                    listingDoc.getString("petType"),
                    listingDoc.getString("description"),
                    listingDoc.getString("ownerID"),
                    listingDoc.getInteger("petAge"),
                    listingDoc.getString("petGender"),
                    listingDoc.getBoolean("petNeuteredSpayed")
            );
            listing.setListingID(listingDoc.getString("listingID"));
            listing.setCreatedAt(listingDoc.getDate("createdAt"));
            listing.setLastUpdatedAt(listingDoc.getDate("lastUpdatedAt"));
            listing.setIsAvailable(listingDoc.getBoolean("isAvailable"));
            return listing;
        } else {
            logger.warn("Adoption listing with ID {} not found.", listingID);
            return null;
        }
    }

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
