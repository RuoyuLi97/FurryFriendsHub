package com.furryfriendshub.UI;

import com.furryfriendshub.util.IDGenerator;
import com.furryfriendshub.UI.Dashboard.Dashboard;
import com.furryfriendshub.config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;

public class RegisterUI {
    private JFrame frame;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JComboBox<String> userTypeComboBox;

    public RegisterUI() {
        // Create the main frame
        frame = new JFrame("Registration Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLayout(new GridLayout(1, 2)); // Divide into two parts using GridLayout

        // Left panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(135, 174, 195));
        frame.add(leftPanel);

        // Left panel content
        JLabel logo = new JLabel("LOGO", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setOpaque(true);
        logo.setBackground(new Color(135, 174, 195));
        logo.setForeground(Color.WHITE);

        JLabel welcomeText = new JLabel("Welcome Back!", SwingConstants.CENTER);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 24));

        JButton loginButton = new JButton("LOG IN");
        loginButton.setPreferredSize(new Dimension(120, 40));

        // Login button click event
        loginButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
            // Logic for navigating to the login page
        });

        // Layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(logo, gbc);

        gbc.gridy = 1;
        leftPanel.add(welcomeText, gbc);

        gbc.gridy = 2;
        leftPanel.add(loginButton, gbc);

        // Right panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        frame.add(rightPanel);

        // Right panel title
        JLabel createAccountTitle = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        createAccountTitle.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(createAccountTitle, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Username (*):"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        rightPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(new JLabel("Email (*):"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        rightPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Password (*):"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        rightPanel.add(passwordField, gbc);

        // Phone number
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(new JLabel("Phone Number (*):"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        rightPanel.add(phoneField, gbc);

        // User type
        gbc.gridx = 0;
        gbc.gridy = 5;
        rightPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        userTypeComboBox = new JComboBox<>(new String[] { "Admin", "User" });
        rightPanel.add(userTypeComboBox, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(registerButton, gbc);

        // Button listener for Register Button
        registerButton.addActionListener(e -> {
            // Navigate to Dashboard
            frame.dispose(); // Close the registration form window
            Dashboard dashboard = new Dashboard();
            dashboard.initialize(); // Initialize the dashboard
        });

        // Display the frame
        frame.setVisible(true);
    }

    private void registerUser() {
        try {
            // Get input data
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String phone = phoneField.getText();
            String userType = (String) userTypeComboBox.getSelectedItem();

            // Validate input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || userType == null) {
                JOptionPane.showMessageDialog(frame, "Please fill out all required fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate a unique userID
            String userID = IDGenerator.generateId(IDGenerator.EntityType.USER);

            // Save new user information to MongoDB
            MongoCollection<Document> usersCollection = MongoDBConnection.getDatabase().getCollection("users");
            Document newUser = new Document("userID", userID)
                    .append("userName", username)
                    .append("email", email)
                    .append("password", password)
                    .append("phoneNumber", phone)
                    .append("userType", userType)
                    .append("registeredAt", new java.util.Date()); // Add registration date
            usersCollection.insertOne(newUser);

            // Display success message
            JOptionPane.showMessageDialog(frame, "Registration successful! Your User ID is: " + userID, "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Navigate to UserAccountUI
            frame.dispose(); // Close the registration window
            new UserAccountUI(username); // Navigate to UserAccountUI with the registered username
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error occurred during registration: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
