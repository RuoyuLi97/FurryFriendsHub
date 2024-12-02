package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private String notificationID;
    private String senderID;
    private String receiverID;
    private String content;
    private Boolean isRead;
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
}
