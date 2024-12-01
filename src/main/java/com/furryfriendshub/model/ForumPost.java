import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPost {
    private static final Logger logger = LoggerFactory.getLogger(ForumPost.class); // Logger instance

    @Id // Marking forumPostID as the primary key (ID) for MongoDB
    private String forumPostID;
    private String userID;
    private String content;
    private String title;
    private List<String> tags;
    private Date createdAt;
    private Date updatedAt;
    private boolean isRead;
    private Date lastReadAt;

    // Constructor that only requires userID, content, title, and tags (for
    // createPost())
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

    // Method to save the ForumPost to MongoDB with check for existing post
    public boolean savePost() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");

            // Check if a post with the same forumPostID already exists
            Document existingPostQuery = new Document("forumPostID", this.forumPostID);
            Document existingPost = collection.find(existingPostQuery).first();

            if (existingPost != null) {
                logger.warn("Post with ID {} already exists. Skipping insert.", this.forumPostID);
                return false; // Return false if post already exists
            }

            // Insert new post if no existing post with the same ID
            Document postDocument = new Document("forumPostID", this.forumPostID)
                    .append("userID", this.userID)
                    .append("content", this.content)
                    .append("title", this.title)
                    .append("tags", this.tags)
                    .append("createdAt", this.createdAt)
                    .append("updatedAt", this.updatedAt)
                    .append("isRead", this.isRead)
                    .append("lastReadAt", this.lastReadAt);

            collection.insertOne(postDocument);
            logger.info("Forum post saved successfully with ID: {}", this.forumPostID);
            return true;
        } catch (Exception e) {
            logger.error("Error saving forum post: ", e);
            return false;
        }
    }

    // Method to update an existing ForumPost
    public boolean updatePost(String postID, String content, List<String> tags) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document updateDocument = new Document("content", content)
                    .append("tags", tags)
                    .append("updatedAt", new Date());

            Document query = new Document("forumPostID", postID);
            collection.updateOne(query, new Document("$set", updateDocument));
            logger.info("Forum post updated successfully with ID: {}", postID);
            return true;
        } catch (Exception e) {
            logger.error("Error updating forum post: ", e);
            return false;
        }
    }

    // Method to delete a ForumPost
    public boolean deletePost(String postID) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document query = new Document("forumPostID", postID);
            collection.deleteOne(query);
            logger.info("Forum post deleted successfully with ID: {}", postID);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting forum post: ", e);
            return false;
        }
    }

    // Method to retrieve a ForumPost by ID
    public static ForumPost getPost(String postID) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document query = new Document("forumPostID", postID);
            Document doc = collection.find(query).first();

            if (doc != null) {
                String userID = doc.getString("userID");
                String content = doc.getString("content");
                String title = doc.getString("title");
                List<String> tags = (List<String>) doc.get("tags");

                ForumPost forumPost = new ForumPost(userID, content, title, tags);
                forumPost.setForumPostID(postID);
                forumPost.setCreatedAt(doc.getDate("createdAt"));
                forumPost.setUpdatedAt(doc.getDate("updatedAt"));
                forumPost.setRead(doc.getBoolean("isRead"));
                forumPost.setLastReadAt(doc.getDate("lastReadAt"));

                logger.info("Forum post retrieved successfully with ID: {}", postID);
                return forumPost;
            }
        } catch (Exception e) {
            logger.error("Error retrieving forum post: ", e);
        }
        return null;
    }

    // Method to retrieve ForumPosts by tag
    public static List<ForumPost> getPostsByTag(String tag) {
        List<ForumPost> posts = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document query = new Document("tags", tag);
            for (Document doc : collection.find(query)) {
                String userID = doc.getString("userID");
                String content = doc.getString("content");
                String title = doc.getString("title");
                List<String> tags = (List<String>) doc.get("tags");

                ForumPost forumPost = new ForumPost(userID, content, title, tags);
                forumPost.setForumPostID(doc.getString("forumPostID"));
                forumPost.setCreatedAt(doc.getDate("createdAt"));
                forumPost.setUpdatedAt(doc.getDate("updatedAt"));
                forumPost.setRead(doc.getBoolean("isRead"));
                forumPost.setLastReadAt(doc.getDate("lastReadAt"));

                posts.add(forumPost);
            }
            logger.info("Forum post retrieved successfully with tag: {}", tag);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error retrieving forum post: ", e);
        }
        return posts;
    }

    // Method to mark a post as read
    public boolean markAsRead() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document updateDocument = new Document("isRead", true)
                    .append("lastReadAt", new Date());

            Document query = new Document("forumPostID", this.forumPostID);
            collection.updateOne(query, new Document("$set", updateDocument));
            logger.info("Forum post marked as read successfully with ID: {}", this.forumPostID);
            return true;
        } catch (Exception e) {
            logger.error("Error marking forum post as read: ", e);
            return false;
        }
    }

    // Method to mark a post as unread
    public boolean markAsUnread() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("forumPosts");
            Document updateDocument = new Document("isRead", false)
                    .append("lastReadAt", null);

            Document query = new Document("forumPostID", this.forumPostID);
            collection.updateOne(query, new Document("$set", updateDocument));
            logger.info("Forum post marked as unread successfully with ID: {}", this.forumPostID);
            return true;
        } catch (Exception e) {
            logger.error("Error marking forum post as unread: ", e);
            return false;
        }
    }
}