package com.furryfriendshub;

import com.furryfriendshub.model.*;
import com.furryfriendshub.management.*;

import java.util.List;
import java.util.ArrayList;

import com.furryfriendshub.config.MongoDBConnection;
//import com.mongodb.client.MongoDatabase;
//import com.furryfriendshub.util.IDGenerator;

public class FurryFriendsHubApplication {
    public static void main(String[] args) {
        // Attempt to get the MongoDB database
        // MongoDatabase database = MongoDBConnection.getDatabase();

        // test case for IDGenerator
        // Generate a user ID
        //String userId = IDGenerator.generateId(IDGenerator.EntityType.USER);
        //System.out.println("Generated User ID: " + userId); // Should print something like US2024120100012345
        // Generate a notification ID
        //String notificationId = IDGenerator.generateId(IDGenerator.EntityType.NOTIFICATION);
        //System.out.println("Generated Notification ID: " + notificationId); // Should print something like NO2024120100012345
        // Generate an adoption listing ID
        //String adoptionListingId = IDGenerator.generateId(IDGenerator.EntityType.ADOPTION_LISTING);
        //System.out.println("Generated Adoption Listing ID: " + adoptionListingId); // Should print something like AL2024120100012345
        // Generate a forum post ID
        //String forumPostId = IDGenerator.generateId(IDGenerator.EntityType.FORUM_POST);
        //System.out.println("Generated Forum Post ID: " + forumPostId); // Should print something like FP2024120100012345


        // test case for User
        // User user = new User("JoeDoe", "JoeDoe@example.com", "123xyz", "User", "1234566789");
        // String userID = "US_20241201_4c3da852";
        // System.out.println(userID);
        // UserManagement userManage = new UserManagement();
        
        // boolean isRegistered = userManage.register(user);
        // if (isRegistered){
        //     System.out.println("User registered successfully!");
        // }
        // else{
        //     System.out.println("User failed to register!");
        // }

        // boolean isLoggedIn = userManage.login("JoeDoe@example.com", "123xyz");
        // if (isLoggedIn){
        //     System.out.println("User logged in successfully!");
        // }
        // else{
        //     System.out.println("User failed to log in!");
        // }

        // boolean isUpdateProfile = userManage.updateProfile(userID, null, null, "123abc", "0123456789");
        // if (isUpdateProfile){
        //     System.out.println("User profile updated successfully!");
        // }
        // else{
        //     System.out.println("User profile failed to update!");
        // }
        
        // boolean isDeleted = userManage.delete(userID);
        // if (isDeleted){
        //     System.out.println("User profile deleted successfully!");
        // }
        // else{
        //     System.out.println("User profile failed to delete!");
        // }


        // test case for AdoptionListing
        // Initialize a new AdoptionListing
        // AdoptionListing listing = new AdoptionListing("MINI", "dog", "A black dog, picked at near school.", "US_20241201_2fa89867", null, null, null);
        // AdoptionListingManagement listingMgt = new AdoptionListingManagement();
        
        // boolean isSaved = listing.saveListing();
        // if (isSaved){
        //     System.out.println("Adoption listing saved successfully!");
        // }
        // else{
        //     System.out.println("Adoption listing failed to save!");
        // }
        
        // String listingID = "AL_20241201_03b62e65";
        // boolean isUpdated = listingMgt.updateListing(listingID, "A grey dog with dust, found near the school.", null, 2, "Female", false);
        // if (isUpdated){
        //     System.out.println("Adoption listing updated successfully!");
        // }
        // else{
        //     System.out.println("Adoption listing failed to update!");
        // }

        // boolean isDeleted = listingMgt.deleteListing(listingID);
        // if (isDeleted){
        //     System.out.println("Adoption listing deleted successfully!");
        // }
        // else{
        //     System.out.println("Adoption listing failed to delete!");
        // }

        
        //test case for notification
        // NotificationManagement notificationMgt = new NotificationManagement();
        
        // boolean isCreated = notificationMgt.createNotification("AD_20241201_03b62e65", "US_20241201_4c3da852", "Welcome to Furry Friends Hub!");
        // if (isCreated){
        //     System.out.println("Notification created and saved successfully!");
        // }
        // else{
        //     System.out.println("Notification failed to create or save!");
        // }

        // String notificationID = "NO_20241201_74644f92";
        // boolean isUpdated = notificationMgt.updateNotification(notificationID, "Nihao from Furry Friends Hub! Welcome to our warm community!", null);
        // if (isUpdated){
        //     System.out.println("Notification updated successfully!");
        // }
        // else{
        //     System.out.println("Notification failed to update!");
        // }

        // boolean isDeleted = notificationMgt.deleteNotification(notificationID);
        // if (isDeleted){
        //     System.out.println("Notification deleted successfully!");
        // }
        // else{
        //     System.out.println("Notification failed to delete!");
        // }

        //test case for forum post
        // ForumPost forumPost = new ForumPost("AD_20241201_03b62e65", "This is a forum post content", "Forum Post Title", new ArrayList<>());
        // ForumPostManagement forumPostMgt = new ForumPostManagement();

        // boolean isSaved = forumPostMgt.savePost(forumPost);
        // if (isSaved){
        //     System.out.println("Forum post saved successfully!");
        // }
        // else{
        //     System.out.println("Forum post failed to save!");
        // }

        // String postID = "FP_20241202_ffc3ef3a";

        // boolean isUpdated = forumPostMgt.updatePost(postID, "Updated content", List.of("tag1", "tag2"));
        // if (isUpdated){
        //     System.out.println("Forum post updated successfully!");
        // }
        // else{
        //     System.out.println("Forum post failed to update!");
        // }

        // ForumPost retrievedPost = forumPostMgt.getPost(postID);
        // System.out.println("Retrieved Forum Post by ID: " + retrievedPost);

        // List<ForumPost> postsByTag = forumPostMgt.getPostsByTag("tag1");
        // System.out.println("Posts retrieved by tag 'tag1': " + postsByTag);

        // boolean isRead = forumPostMgt.markAsRead(postID);
        // if (isRead){
        //     System.out.println("Forum post was read successfully!");
        // }
        // else{
        //     System.out.println("Forum post failed to be read!");
        // }

        // boolean isUnread = forumPostMgt.markAsUnread(postID);
        // if (isUnread){
        //     System.out.println("Forum post was unread successfully!");
        // }
        // else{
        //     System.out.println("Forum post failed to be unread!");
        // }

        // boolean isDeleted = forumPostMgt.deletePost(postID);
        // if (isDeleted){
        //     System.out.println("Forum post deleted successfully!");
        // }
        // else{
        //     System.out.println("Forum post failed to delete!");
        // }

        // Close the connection when done
        MongoDBConnection.closeConnection();
    }
}
