package com.furryfriendshub.model;

import com.furryfriendshub.util.IDGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPost {

    private String forumPostID;
    private String userID;
    private String content;
    private String title;
    private List<String> tags;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isRead;
    private Date lastReadAt;

    // Constructor that only requires userID, content, title, and tags (for createPost())
    public ForumPost(String userID, String content, String title, List<String> tags) {
        this.forumPostID = IDGenerator.generateId(IDGenerator.EntityType.FORUM_POST);
        this.userID = userID;
        this.content = content;
        this.title = title;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
        this.isRead = false; // Default to unread
        this.lastReadAt = null;
    }


}