package com.furryfriendshub.management;

import com.furryfriendshub.model.Notification;
import com.furryfriendshub.config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationManagement {
    private static final Logger logger = LoggerFactory.getLogger(NotificationManagement.class);

    // Create a new notification
    public boolean createNotification(String senderID, String receiverID, String content) {
        Notification newNotification = new Notification(senderID, receiverID, content);
        return saveNotification(newNotification);
    }

    // Save notification to the MongoDB collection with a check for existing ones
    public boolean saveNotification(Notification notification) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");

            // Check if a notification with the same senderID, receiverID, and content already exists
            Document existingNotification = collection.find(Filters.and(
                    Filters.eq("senderID", notification.getSenderID()),
                    Filters.eq("receiverID", notification.getReceiverID()),
                    Filters.eq("content", notification.getContent()),
                    Filters.eq("isRead", notification.getIsRead()))).first();

            if (existingNotification != null) {
                logger.warn("Notification already exists for senderID: {} and receiverID: {}", notification.getSenderID(), notification.getReceiverID());
                return false; // Don't save if the same notification exists
            }

            // Create a document representing the notification
            Document notificationDoc = new Document("notificationID", notification.getNotificationID())
                    .append("senderID", notification.getSenderID())
                    .append("receiverID", notification.getReceiverID())
                    .append("content", notification.getContent())
                    .append("isRead", notification.getIsRead())
                    .append("timestamp", notification.getTimestamp());

            // Insert the document into the "notifications" collection
            collection.insertOne(notificationDoc);
            logger.info("Notification created with ID: {}", notification.getNotificationID());
            return true;
        } catch (Exception e) {
            logger.error("Error saving notification with ID: {}", notification.getNotificationID(), e);
            return false;
        }
    }

    // Update a notification
    public boolean updateNotification(String notificationID, String newContent, Boolean isRead) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");

            // Check if the notification exists
            Document existingNotification = collection.find(Filters.eq("notificationID", notificationID)).first();
            if (existingNotification == null) {
                logger.warn("Notification with ID {} not found.", notificationID);
                return false; // Notification not found
            }

            // Prepare update fields
            Document updates = new Document();
            if (newContent != null && !newContent.isEmpty()) {
                updates.append("content", newContent);
            }
            if (isRead != null) {
                updates.append("isRead", isRead);
            }
            updates.append("timestamp", new Date()); // Update the timestamp

            // Update the notification in the database
            collection.updateOne(Filters.eq("notificationID", notificationID),
                    new Document("$set", updates));

            logger.info("Notification with ID {} updated successfully.", notificationID);
            return true;
        } catch (Exception e) {
            logger.error("Error updating notification with ID {}: ", notificationID, e);
            return false;
        }
    }

    // Send notification (do it later)
    // public void sendNotification() {
    // Ideally, here you would have an external service or system sending
    // notifications (email, SMS, etc.)
    // logger.info("Notification sent from: {} to: {}", senderID, receiverID);
    // }

    // Delete a notification
    public boolean deleteNotification(String notificationID) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");
            collection.deleteOne(Filters.eq("notificationID", notificationID));
            logger.info("Notification with ID: {} deleted", notificationID);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting notification with ID: {}", notificationID, e);
            return false;
        }
    }

    // Get all notifications from the database
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");

            // Fetch all notifications
            for (Document doc : collection.find()) {
                Notification notification = new Notification(
                        doc.getString("senderID"),
                        doc.getString("receiverID"),
                        doc.getString("content")
                );
                notification.setNotificationID(doc.getString("notificationID"));
                notification.setIsRead(doc.getBoolean("isRead"));
                notification.setTimestamp(doc.getDate("timestamp"));
                notifications.add(notification);
            }
            logger.info("Retrieved all notifications.");
        } catch (Exception e) {
            logger.error("Error fetching notifications.", e);
        }
        return notifications;
    }

    // Get notification by notificationID
    public Notification getNotificationByID(String notificationID) {
        Notification notification = null;
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");
    
            // Query the collection for the notification with the specified notificationID
            Document query = new Document("notificationID", notificationID);
            Document doc = collection.find(query).first();  // Fetch the first matching document
    
            // If a notification is found, map it to a Notification object
            if (doc != null) {
                notification = new Notification(
                        doc.getString("senderID"),
                        doc.getString("receiverID"),
                        doc.getString("content")
                );
                notification.setNotificationID(doc.getString("notificationID"));
                notification.setIsRead(doc.getBoolean("isRead"));
                notification.setTimestamp(doc.getDate("timestamp"));
                logger.info("Retrieved notification with ID: " + notificationID);
            } else {
                logger.warn("Notification with ID: " + notificationID + " not found.");
            }
        } catch (Exception e) {
            logger.error("Error fetching notification with ID: " + notificationID, e);
        }
        return notification;
    }    
}
