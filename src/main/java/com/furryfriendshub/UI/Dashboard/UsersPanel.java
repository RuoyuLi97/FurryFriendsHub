package com.furryfriendshub.UI.Dashboard;

import com.furryfriendshub.model.User;
import com.furryfriendshub.management.UserManagement;
import com.furryfriendshub.UI.Components.ButtonEditor;
import com.furryfriendshub.UI.Components.ButtonRenderer;
import com.furryfriendshub.UI.Components.ActionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsersPanel extends JPanel implements ActionHandler {
    private DefaultTableModel tableModel;
    private JTable userTable;
    private UserManagement userManagement;

    public UsersPanel(UserManagement userManagement) {
        this.userManagement = userManagement;
        initializePanel();
    }

    private void initializePanel() {
        this.setLayout(new BorderLayout());

        // Exclude "password" from the column names
        String[] columnNames = {"UserID", "Username", "Email", "Role", "PhoneNumber", "RegisteredAt", "LastLoginAt", "Operation"};
        tableModel = new DefaultTableModel(getUserData(), columnNames);
        userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make all columns non-editable
            }
        };

        // Set ButtonRenderer and ButtonEditor for "Operation" column
        userTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(this));

        JScrollPane scrollPane = new JScrollPane(userTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Fetch user data from UserManagement, exclude password from the columns
    private Object[][] getUserData() {
        List<User> users = userManagement.getAllUsers();
        Object[][] data = new Object[users.size()][8];  // 8 columns (excluding password)

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i] = new Object[]{
                user.getUserID(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getRegisteredAt(),
                user.getLastLoginAt(),
                "Edit/Delete"  // Operation column for actions
            };
        }
        return data;
    }

    @Override
    public void addNewListing() {
        JFrame frame = new JFrame("Add New User");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // UserName field
        JLabel nameLabel = new JLabel("User Name:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        // PhoneNumber field
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        panel.add(phoneLabel);
        panel.add(phoneField);

        // Role field
        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField();
        panel.add(roleLabel);
        panel.add(roleField);

        // Password field (optional)
        JLabel passwordLabel = new JLabel("Password (optional):");
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Save button
        JButton saveButton = new JButton("Save User");
        saveButton.addActionListener(e -> {
            // Get input values
            String userName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String role = roleField.getText().trim();
            String password = new String(passwordField.getPassword()).trim(); // Get password value

            // Validate fields
            if (userName.isEmpty() || email.isEmpty() || phone.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields except password are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and save the user, including password if provided
            User newUser;
            if (password.isEmpty()) {
                newUser = new User(userName, email, "", role, phone); // Create user without password
            } else {
                newUser = new User(userName, email, password, role, phone); // Create user with password
            }

            boolean success = userManagement.register(newUser);
            if (success) {
                JOptionPane.showMessageDialog(frame, "New user added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshUserData();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add the user. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void editListing(String userID) {
        User existingUser = userManagement.getUserByID(userID);

        if (existingUser == null) {
            JOptionPane.showMessageDialog(null, "User with ID " + userID + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame frame = new JFrame("Edit User");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // UserName field
        JLabel nameLabel = new JLabel("User Name:");
        JTextField nameField = new JTextField(existingUser.getUserName());
        panel.add(nameLabel);
        panel.add(nameField);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(existingUser.getEmail());
        panel.add(emailLabel);
        panel.add(emailField);

        // PhoneNumber field
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField(existingUser.getPhoneNumber());
        panel.add(phoneLabel);
        panel.add(phoneField);

        // Role field
        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField(existingUser.getRole());
        panel.add(roleLabel);
        panel.add(roleField);

        // Password field (optional)
        JLabel passwordLabel = new JLabel("New Password (optional):");
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Get updated values
            String newUserName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String newRole = roleField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim(); // Get password value

            // Update the user
            existingUser.setUserName(newUserName);
            existingUser.setEmail(newEmail);
            existingUser.setPhoneNumber(newPhone);
            existingUser.setRole(newRole);

            // Only update password if a new password is provided
            if (!newPassword.isEmpty()) {
                existingUser.setPassword(newPassword); // Update password if provided
            }

            boolean success = userManagement.updateProfile(existingUser);
            if (success) {
                JOptionPane.showMessageDialog(frame, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshUserData();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update user. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel());
        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void deleteListing(String userID) {
        boolean success = userManagement.delete(userID);
        if (success) {
            JOptionPane.showMessageDialog(this, "User deleted successfully!");
            refreshUserData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshUserData() {
        Object[][] newData = getUserData();
        tableModel.setDataVector(newData, new Object[]{
            "UserID", "Username", "Email", "Role", "PhoneNumber", "RegisteredAt", "LastLoginAt", "Operation"
        });
    }
}