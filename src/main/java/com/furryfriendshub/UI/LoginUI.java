package com.furryfriendshub.UI;

import javax.swing.*;
import java.awt.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import com.furryfriendshub.UI.Dashboard.Dashboard;
import com.furryfriendshub.config.MongoDBConnection;
import com.furryfriendshub.management.UserManagement;

public class LoginUI {
    public LoginUI() {
        // Create the main frame
        JFrame frame = new JFrame("Login UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        frame.setLayout(new GridLayout(1, 2)); // Split layout into two sections

        // Left panel: Login section
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);

        JLabel logoLabel = new JLabel("LOGO", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        logoLabel.setOpaque(true);
        logoLabel.setBackground(new Color(135, 174, 195));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setPreferredSize(new Dimension(80, 80));

        JLabel loginTitle = new JLabel("LOG IN TO THE HUB", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField passwordField = new JPasswordField(20);

        JCheckBox rememberMe = new JCheckBox("Remember Me");
        rememberMe.setBackground(Color.WHITE);
        rememberMe.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton loginButton = new JButton("LOG IN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Login button action listener
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and Password are required!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use UserManagement for login validation
            UserManagement userManagement = new UserManagement();
            boolean isValidUser = userManagement.login(username, password);

            if (isValidUser) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                Dashboard dashboard = new Dashboard();
                dashboard.initialize(); // Navigate to the dashboard
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout management for the left panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(logoLabel, gbc);

        gbc.gridy = 1;
        loginPanel.add(loginTitle, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(rememberMe, gbc);

        gbc.gridy = 5;
        loginPanel.add(loginButton, gbc);

        // Right panel: Sign-up section
        JPanel signUpPanel = new JPanel(new GridBagLayout());
        signUpPanel.setBackground(new Color(135, 174, 195)); // Dark blue background

        JLabel welcomeText = new JLabel("Hello Friend!", SwingConstants.CENTER);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeText.setForeground(Color.WHITE);

        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Sign-up button action listener
        signUpButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
            new RegisterUI(); // Open the RegistrationForm
        });

        // Layout management for the right panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        signUpPanel.add(welcomeText, gbc);

        gbc.gridy = 1;
        signUpPanel.add(signUpButton, gbc);

        frame.add(loginPanel);
        frame.add(signUpPanel);

        frame.setVisible(true);
    }

}
