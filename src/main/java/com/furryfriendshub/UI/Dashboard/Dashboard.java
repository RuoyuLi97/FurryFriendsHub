package com.furryfriendshub.UI.Dashboard;

import com.furryfriendshub.UI.Components.ActionHandler;
import com.furryfriendshub.management.*;

import javax.swing.*;
import java.awt.*;

public class Dashboard {
    private JFrame frame;
    private AdoptionListingManagement listingManagement;
    private ForumPostManagement postManagement;
    private UserManagement userManagement;
    private NotificationManagement notificationManagement;
    
    private JPanel currentContentPanel;  // To hold the current content panel (Adoption Listings, Forum Posts, etc.)
    private TopPanel topPanel;           // Store a reference to TopPanel

    public Dashboard() {
        listingManagement = new AdoptionListingManagement();
        postManagement = new ForumPostManagement();
        userManagement = new UserManagement();
        notificationManagement = new NotificationManagement();
    }

    public void initialize() {
        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLayout(new BorderLayout());

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Create and initialize the TopPanel
        topPanel = new TopPanel("Admin Name", "Add New",
            e -> addNewListing(),   // Action for "Add New" button
            e -> performSearch()    // Action for "Search" button
        );
        frame.add(topPanel.getPanel(), BorderLayout.NORTH);

        // Side Panel with navigation buttons
        SidePanel sidePanel = new SidePanel(
                e -> showUsersPanel(),
                e -> showAdoptionListingsPanel(),
                e -> showPostsPanel(),
                e -> showNotificationsPanel()
        );
        frame.add(sidePanel.getPanel(), BorderLayout.WEST);

        // Default Content Panel (Adoption Listings by default)
        currentContentPanel = new UsersPanel(userManagement);
        frame.add(currentContentPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    // Method to show Adoption Listings Panel
    public void showAdoptionListingsPanel() {
        updateContentPanel(new ListingsPanel(listingManagement));
    }

    // Method to show Forum Posts Panel
    public void showPostsPanel() {
        updateContentPanel(new PostsPanel(postManagement));
    }

    // Method to show Users Panel
    public void showUsersPanel() {
        updateContentPanel(new UsersPanel(userManagement));
    }

    // Method to show Notifications Panel
    public void showNotificationsPanel() {
        updateContentPanel(new NotificationsPanel(notificationManagement));
    }

    // Method to update the content panel with a new panel
    private void updateContentPanel(JPanel newPanel) {
        if (frame != null) {
            Container contentPane = frame.getContentPane();
            contentPane.remove(currentContentPanel);  // Remove the previous content panel
            contentPane.add(newPanel, BorderLayout.CENTER);  // Add the new content panel
            currentContentPanel = newPanel;  // Update the reference for the current content panel
            frame.revalidate();
            frame.repaint();
        } else {
            System.err.println("Error: frame is null in updateContentPanel.");
        }
    }

    // Method to handle the add new button functionality
    private void addNewListing() {
        if (currentContentPanel instanceof ActionHandler) {
            ((ActionHandler) currentContentPanel).addNewListing();
        }
    }

    // Method to handle the search bar functionality
    private void performSearch() {
        String query = topPanel.getSearchQuery();  // Use the topPanel reference to get search query

        // Handle search based on the selected content panel
        if (currentContentPanel instanceof ListingsPanel) {
            listingManagement.getListingByID(query);
        } else if (currentContentPanel instanceof PostsPanel) {
            postManagement.getPost(query);
        } else if (currentContentPanel instanceof UsersPanel) {
            userManagement.getUserByID(query);
        } else if (currentContentPanel instanceof NotificationsPanel) {
            notificationManagement.getNotificationByID(query);
        }
    }

    public static void main(String[] args) {
        // Ensure the UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.initialize();  // Initialize the dashboard
        });
    }
}