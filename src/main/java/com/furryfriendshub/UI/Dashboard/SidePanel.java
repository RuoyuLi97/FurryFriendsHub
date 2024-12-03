package com.furryfriendshub.UI.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class SidePanel {
    private JPanel panel;

    public SidePanel(ActionListener usersAction, ActionListener listingsAction, ActionListener postsAction, ActionListener notificationsAction) {
        // Initialize the panel with GridBagLayout
        panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(250, 0)); // Fixed width, dynamic height
        panel.setBackground(new Color(135, 174, 195)); // Background color

        // GridBagConstraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Padding between buttons
        gbc.gridx = 0; // All buttons in the same column
        gbc.gridy = 0; // Start at the top
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make buttons stretch horizontally

        // Define the menu items and their actions
        String[] menuItems = {"Users", "Listings", "Posts", "Notifications"};
        ActionListener[] actions = {usersAction, listingsAction, postsAction, notificationsAction};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createButton(menuItems[i], actions[i]);
            panel.add(menuButton, gbc); // Add button to panel
            gbc.gridy++; // Move to the next row
        }
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for the button
        button.setForeground(Color.WHITE); // Button text color
        button.setBackground(new Color(72, 108, 123)); // Button background color
        button.setBorderPainted(false); // Remove border
        button.setFocusPainted(false); // Remove focus outline
        button.setPreferredSize(new Dimension(200, 40)); // Fixed button size
        button.addActionListener(action);
        return button;
    }

    public JPanel getPanel() {
        return panel;
    }
}