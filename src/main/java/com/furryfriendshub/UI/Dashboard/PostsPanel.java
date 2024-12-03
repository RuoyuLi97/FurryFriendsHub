package com.furryfriendshub.UI.Dashboard;

import com.furryfriendshub.model.*;
import com.furryfriendshub.management.*;
import com.furryfriendshub.UI.Components.ButtonEditor;
import com.furryfriendshub.UI.Components.ButtonRenderer;
import com.furryfriendshub.UI.Components.ActionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PostsPanel extends JPanel implements ActionHandler {
    private DefaultTableModel tableModel;
    private JTable postTable;
    private ForumPostManagement postManagement;

    public PostsPanel(ForumPostManagement postManagement) {
        this.postManagement = postManagement;
        initializePanel();
    }

    private void initializePanel() {
        this.setLayout(new BorderLayout());

        String[] columnNames = {"PostID", "UserID", "Title", "Content", "Tags", "Created At", "Updated At", "Is Read", "Last Read At", "Operation"};
        tableModel = new DefaultTableModel(getForumPostsData(), columnNames);
        postTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8 || column == 9; // Allow editing in the "Operation" column
            }
        };

        // Set ButtonRenderer and ButtonEditor for the "Operation" column
        postTable.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        postTable.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(this));

        JScrollPane scrollPane = new JScrollPane(postTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Fetch forum posts data
    private Object[][] getForumPostsData() {
        List<ForumPost> posts = postManagement.getAllPosts();
        Object[][] data = new Object[posts.size()][10];

        for (int i = 0; i < posts.size(); i++) {
            ForumPost post = posts.get(i);
            if (post != null){
                data[i] = new Object[] {
                    post.getForumPostID(),
                    post.getUserID(),
                    post.getTitle(),
                    post.getContent(),
                    post.getTags(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    post.getIsRead() ? "Read" : "Unread", // Read status
                    post.getLastReadAt() != null ? post.getLastReadAt() : null,
                    "Edit/Delete"
                };
            } else {
                data[i] = new Object[] {
                    null,
                    "N/A",                                              
                    "N/A",                                              
                    "N/A",                                         
                    "N/A",                                        
                    "N/A",                                                 
                    null,                                                  
                    null,                                                  
                    null,                                                
                    "Edit/Delete"
                };
            }
        }
        return data;
    }

    @Override
    public void addNewListing() {
        // Create the frame for adding a new post
        JFrame frame = new JFrame("Add New Forum Post");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Create the main panel with GridLayout for alignment
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns, dynamic rows

        // User ID field
        JLabel userIDLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField();
        panel.add(userIDLabel);
        panel.add(userIDField);

        // Title field
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        panel.add(titleLabel);
        panel.add(titleField);

        // Content field
        JLabel contentLabel = new JLabel("Content:");
        JTextArea contentField = new JTextArea(3, 20);
        JScrollPane contentScrollPane = new JScrollPane(contentField); // Wrap JTextArea in JScrollPane
        panel.add(contentLabel);
        panel.add(contentScrollPane);

        // Tags field
        JLabel tagsLabel = new JLabel("Tags:");
        JTextField tagsField = new JTextField();
        panel.add(tagsLabel);
        panel.add(tagsField);

        // Save button
        JButton saveButton = new JButton("Save Post");
        saveButton.addActionListener(e -> {
            // Get input values
            String userID = userIDField.getText().trim();
            String title = titleField.getText().trim();
            String content = contentField.getText().trim();
            String tags = tagsField.getText().trim();

            // Check required fields
            if (title.isEmpty() || content.isEmpty() || userID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "User ID, Title, and Content are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convert tags string to List<String>
            List<String> tagList = Arrays.asList(tags.split(",\\s*")); // Split tags by comma and trim spaces

            // Create the ForumPost object
            ForumPost newPost = new ForumPost(userID, title, content, tagList);

            // Create an instance of ForumPostManagement
            ForumPostManagement postManagement = new ForumPostManagement();

            // Save the post using savePost method from ForumPostManagement
            boolean success = postManagement.savePost(newPost); // Call savePost method from ForumPostManagement
            if (success) {
                JOptionPane.showMessageDialog(frame, "New post added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshPostData(); // Refresh the table with new data
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add the post. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add save button at the end
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(saveButton);

        // Add the panel to the frame and display it
        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void editListing(String forumPostID) {
        ForumPostManagement postManager = new ForumPostManagement();
        ForumPost existingPost = postManager.getPost(forumPostID);
    
        if (existingPost == null) {
            JOptionPane.showMessageDialog(null, "Forum post with ID " + forumPostID + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Frame for the edit post form
        JFrame frame = new JFrame("Edit Forum Post");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);
    
        // Create the main panel with GridLayout for alignment
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns, dynamic rows
    
        // Title field
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(existingPost.getTitle() != null ? existingPost.getTitle() : "");
        panel.add(titleLabel);
        panel.add(titleField);
    
        // Content field
        JLabel contentLabel = new JLabel("Content:");
        JTextArea contentField = new JTextArea(3, 20);
        contentField.setText(existingPost.getContent());
        JScrollPane contentScrollPane = new JScrollPane(contentField);
        panel.add(contentLabel);
        panel.add(contentScrollPane);
    
        // Tags field
        JLabel tagsLabel = new JLabel("Tags:");
        // Convert List<String> to a comma-separated string for display in JTextField
        String tagsText = existingPost.getTags() != null ? String.join(", ", existingPost.getTags()) : "";
        JTextField tagsField = new JTextField(tagsText);
        panel.add(tagsLabel);
        panel.add(tagsField);
    
        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Collect updated data
            String newTitle = titleField.getText().trim();
            String newContent = contentField.getText().trim();
            String newTags = tagsField.getText().trim();
    
            // Convert the tags string back to a List<String>
            List<String> newTagList = Arrays.asList(newTags.split(",\\s*"));
    
            // Update the post using ForumPostManagement
            boolean updateSuccess = postManager.updatePost(forumPostID, newTitle, newContent, newTagList);
    
            if (updateSuccess) {
                JOptionPane.showMessageDialog(frame, "Forum post updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the edit form
                refreshPostData();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update forum post. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        // Add save button to the panel
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(saveButton);
    
        // Add the panel to the frame and display it
        frame.add(panel);
        frame.setVisible(true);
    }    

    @Override
    public void deleteListing(String forumPostID) {
        boolean success = postManagement.deletePost(forumPostID);
        if (success) {
            JOptionPane.showMessageDialog(this, "Post deleted successfully!");
            refreshPostData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete post.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshPostData() {
        Object[][] newData = getForumPostsData();
        tableModel.setDataVector(newData, new Object[]{
            "PostID", "UserID", "Title", "Content", "Tags", "Created At", "Updated At", "Is Read", "Last Read At", "Operation"
        });
    }
}
