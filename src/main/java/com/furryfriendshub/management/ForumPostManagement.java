package com.furryfriendshub.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.furryfriendshub.config.MongoDBConnection;
import com.furryfriendshub.model.ForumPost;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ForumPostManagement {
    private static final Logger logger = LoggerFactory.getLogger(ForumPostManagement.class); // Logger instance

    // Save the ForumPost to MongoDB with check for existing post
    public boolean savePost(ForumPost forumPost) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");

            // Check if a post with the same forumPostID already exists
            Document existingPostQuery = new Document("forumPostID", forumPost.getForumPostID());
            Document existingPost = collection.find(existingPostQuery).first();

            if (existingPost != null) {
                logger.warn("Post with ID {} already exists. Skipping insert.", forumPost.getForumPostID());
                return false; // Return false if post already exists
            }

            // Insert new post if no existing post with the same ID
            Document postDocument = new Document("forumPostID", forumPost.getForumPostID())
                    .append("userID", forumPost.getUserID())
                    .append("content", forumPost.getContent())
                    .append("title", forumPost.getTitle())
                    .append("tags", forumPost.getTags())
                    .append("createdAt", forumPost.getCreatedAt())
                    .append("updatedAt", forumPost.getUpdatedAt())
                    .append("isRead", forumPost.getIsRead())
                    .append("lastReadAt", forumPost.getLastReadAt());

            collection.insertOne(postDocument);
            logger.info("Forum post saved successfully with ID: {}", forumPost.getForumPostID());
            return true;
        } catch (Exception e) {
            logger.error("Error saving forum post: ", e);
            return false;
        }
    }

    // Update an existing ForumPost
    public boolean updatePost(String postID, String newContent, List<String> newTags) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> postsCollection = database.getCollection("forumPosts");
        
        // Find the listing by listingID
        Document postDoc = postsCollection.find(new Document("forumPostID", postID)).first();
        if (postDoc == null) {
            logger.error("Forum post with ID {} not found.", postID);
            return false; // Forum post not found
        }

        if (newContent != null && !newContent.isEmpty()) {
            postDoc.put("content", newContent);
        }

        if (newTags != null && !newTags.isEmpty()) {
            postDoc.put("tags", newTags);
        }

        postDoc.put("updatedAt", new Date());
        // Save the updated document back into the collection
        postsCollection.replaceOne(new Document("forumPostID", postID), postDoc);
        logger.info("Forum post with ID {} updated successfully.", postID);
        return true;
    }

    // Delete a ForumPost
    public boolean deletePost(String postID) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> postsCollection = database.getCollection("forumPosts");

        // Check if the post exists
        long deletedCount = postsCollection.deleteOne(new Document("forumPostID", postID)).getDeletedCount();
        if (deletedCount > 0) {
            logger.info("Forum post with ID {} deleted successfully.", postID);
            return true;
        } else {
            logger.warn("Forum post with ID {} not found.", postID);
            return false; // Post not found
        }
    }

    // Retrieve a ForumPost by ID
    public static ForumPost getPost(String postID) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document query = new Document("forumPostID", postID);
            Document doc = collection.find(query).first();

            if (doc != null) {
                String userID = doc.getString("userID");
                String content = doc.getString("content");
                String title = doc.getString("title");

                // Safely cast the tags field with type checking
                Object tagsObj = doc.get("tags");
                List<String> tags = new ArrayList<>();
                if (tagsObj instanceof List) {
                    tags = (List<String>) tagsObj;  // Safe cast
                } else {
                    logger.warn("Tags field is not of type List<String>.");
                }

                ForumPost forumPost = new ForumPost(userID, content, title, tags);
                forumPost.setForumPostID(postID);
                forumPost.setCreatedAt(doc.getDate("createdAt"));
                forumPost.setUpdatedAt(doc.getDate("updatedAt"));
                forumPost.setIsRead(doc.getBoolean("isRead"));
                forumPost.setLastReadAt(doc.getDate("lastReadAt"));

                logger.info("Forum post retrieved successfully with ID: {}", postID);
                return forumPost;
            }
        } catch (Exception e) {
            logger.error("Error retrieving forum post: ", e);
        }
        return null;
    }

    // Retrieve ForumPosts by tag
    public static List<ForumPost> getPostsByTag(String tag) {
        List<ForumPost> posts = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document query = new Document("tags", tag);
            for (Document doc : collection.find(query)) {
                String userID = doc.getString("userID");
                String content = doc.getString("content");
                String title = doc.getString("title");

                // Safely cast the tags field with type checking
                Object tagsObj = doc.get("tags");
                List<String> tags = new ArrayList<>();
                if (tagsObj instanceof List) {
                    tags = (List<String>) tagsObj;  // Safe cast
                } else {
                    logger.warn("Tags field is not of type List<String>.");
                }

                ForumPost forumPost = new ForumPost(userID, content, title, tags);
                forumPost.setForumPostID(doc.getString("forumPostID"));
                forumPost.setCreatedAt(doc.getDate("createdAt"));
                forumPost.setUpdatedAt(doc.getDate("updatedAt"));
                forumPost.setIsRead(doc.getBoolean("isRead"));
                forumPost.setLastReadAt(doc.getDate("lastReadAt"));

                posts.add(forumPost);
            }
            logger.info("Forum posts retrieved successfully with tag: {}", tag);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error retrieving forum posts: ", e);
        }
        return posts;
    }

    // Mark a post as read
    public static boolean markAsRead(String postID) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document updateDocument = new Document("isRead", true)
                    .append("lastReadAt", new Date());

            Document query = new Document("forumPostID", postID);
            collection.updateOne(query, new Document("$set", updateDocument));
            logger.info("Forum post marked as read successfully with ID: {}", postID);
            return true;
        } catch (Exception e) {
            logger.error("Error marking forum post as read: ", e);
            return false;
        }
    }

    // Method to mark a post as unread
    public boolean markAsUnread(String postID) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document updateDocument = new Document("isRead", false)
                    .append("lastReadAt", null);

            Document query = new Document("forumPostID", postID);
            collection.updateOne(query, new Document("$set", updateDocument));
            logger.info("Forum post marked as unread successfully with ID: {}", postID);
            return true;
        } catch (Exception e) {
            logger.error("Error marking forum post as unread: ", e);
            return false;
        }
    }
}
