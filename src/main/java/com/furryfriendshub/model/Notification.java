package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import com.furryfriendshub.config.MongoDBConnection;
import org.springframework.data.annotation.Id;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private static final Logger logger = LoggerFactory.getLogger(Notification.class);

    @Id // Marking notificationID as the primary key (ID) for MongoDB
    private String notificationID;
    private String senderID;
    private String receiverID;
    private String content;
    private boolean isRead;
    private Date timestamp; // Timestamp for when the notification was created

    // Constructor to initialize Notification
    public Notification(String senderID, String receiverID, String content) {
        this.notificationID = IDGenerator.generateId(IDGenerator.EntityType.NOTIFICATION);
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.isRead = false;
        this.timestamp = new Date();  // Set timestamp to current time
    }

    // Create a new notification
    public static boolean createNotification(String senderID, String receiverID, String content) {
        Notification newNotification = new Notification(senderID, receiverID, content);
        return newNotification.saveNotification();
    }

    // Save notification to the MongoDB collection with a check for existing ones
    public boolean saveNotification() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");

            // Check if a notification with the same senderID, receiverID, and content
            // already exists
            Document existingNotification = collection.find(Filters.and(
                    Filters.eq("senderID", this.senderID),
                    Filters.eq("receiverID", this.receiverID),
                    Filters.eq("content", this.content),
                    Filters.eq("isRead", this.isRead))).first();

            if (existingNotification != null) {
                logger.warn("Notification already exists for senderID: {} and receiverID: {}", this.senderID,
                        this.receiverID);
                return false; // Don't save if the same notification exists
            }

            // Create a document representing the notification
            Document notificationDoc = new Document("notificationID", this.notificationID)
                    .append("senderID", this.senderID)
                    .append("receiverID", this.receiverID)
                    .append("content", this.content)
                    .append("isRead", this.isRead)
                    .append("timestamp", this.timestamp);

            // Insert the document into the "notifications" collection
            collection.insertOne(notificationDoc);
            logger.info("Notification created with ID: {}", this.notificationID);
            return true;
        } catch (Exception e) {
            logger.error("Error saving notification with ID: {}", this.notificationID, e);
            return false;
        }
    }

    // Mark notification as read
    public void markAsRead() {
        this.isRead = true;
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("notifications");
            collection.updateOne(
                    Filters.eq("notificationID", this.notificationID),
                    new Document("$set", new Document("isRead", true)));
            logger.info("Notification marked as read: {}", this.notificationID);
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}", this.notificationID, e);
        }
    }

    // Send notification (do it later)
    // public void sendNotification() {
    // Ideally, here you would have an external service or system sending
    // notifications (email, SMS, etc.)
    // logger.info("Notification sent from: {} to: {}", senderID, receiverID);
    // }

    // Delete a notification
    public static boolean deleteNotification(String notificationID) {
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
}
