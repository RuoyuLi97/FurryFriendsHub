package com.furryfriendshub.UI.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TopPanel {
    private JPanel panel;
    private JTextField searchField;

    public TopPanel(String adminName, String buttonLabel, ActionListener addNewAction, ActionListener searchAction) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(1100, 90));
        panel.setBackground(Color.WHITE);

        // Create the search bar panel (left side of the top panel)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        // Search field with reduced height
        searchField = new JTextField("Search by ID", 20);
        searchField.setPreferredSize(new Dimension(150, 25)); // Set custom size for height and width

        // Search button with reduced height
        JButton searchButton = new JButton("SEARCH");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(new Color(135, 175, 195));
        searchButton.setPreferredSize(new Dimension(80, 25)); // Set custom size for height
        searchButton.addActionListener(searchAction); // Add search functionality

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Add the search panel to the top of the panel
        panel.add(searchPanel, BorderLayout.WEST);

        // Create the container for the right-aligned content (admin name and button)
        Box rightBox = Box.createVerticalBox();
        rightBox.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Admin Label
        JLabel adminLabel = new JLabel("Welcome, " + adminName + "!");
        adminLabel.setFont(new Font("Arial", Font.BOLD, 18));
        adminLabel.setForeground(new Color(135, 174, 195));
        adminLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        adminLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Add New Button
        JButton actionButton = new JButton(buttonLabel);
        actionButton.setFont(new Font("Arial", Font.BOLD, 14));
        actionButton.setBackground(Color.WHITE);
        actionButton.setForeground(new Color(135, 174, 195));
        actionButton.setFocusPainted(false);
        actionButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        actionButton.addActionListener(addNewAction); // Add "Add New" action

        // Add components to the right-aligned box
        rightBox.add(adminLabel);
        rightBox.add(Box.createVerticalStrut(10)); // Spacer between label and button
        rightBox.add(actionButton);

        // Add the right-aligned box to the panel
        panel.add(rightBox, BorderLayout.EAST);
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getSearchQuery() {
        return searchField.getText();  // Return the search text from the search field
    }
}
