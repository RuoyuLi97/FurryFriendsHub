package com.furryfriendshub.UI.Dashboard;

import com.furryfriendshub.model.Notification;
import com.furryfriendshub.management.NotificationManagement;
import com.furryfriendshub.UI.Components.ButtonEditor;
import com.furryfriendshub.UI.Components.ButtonRenderer;
import com.furryfriendshub.UI.Components.ActionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationsPanel extends JPanel implements ActionHandler {
    private DefaultTableModel tableModel;
    private JTable notificationTable;
    private NotificationManagement notificationManagement;

    public NotificationsPanel(NotificationManagement notificationManagement) {
        this.notificationManagement = notificationManagement;
        initializePanel();
    }

    private void initializePanel() {
        this.setLayout(new BorderLayout());

        String[] columnNames = {"NotificationID", "SenderID", "ReceiverID", "Content", "IsRead", "Timestamp", "Operation"};
        tableModel = new DefaultTableModel(getNotificationsData(), columnNames);
        notificationTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 6; // Allow editing in specific columns (IsRead, Operation)
            }
        };

        // Set ButtonRenderer and ButtonEditor for "Operation" column (Edit/Delete)
        notificationTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        notificationTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(this));

        JScrollPane scrollPane = new JScrollPane(notificationTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Fetch notifications data
    private Object[][] getNotificationsData() {
        List<Notification> notifications = notificationManagement.getAllNotifications();
        Object[][] data = new Object[notifications.size()][7];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            if (notification != null) {
                data[i] = new Object[] {
                    notification.getNotificationID(),
                    notification.getSenderID(),
                    notification.getReceiverID(),
                    notification.getContent(),
                    notification.getIsRead() ? "Read" : "Unread", // Notification read status
                    dateFormat.format(notification.getTimestamp()), // Format the timestamp
                    "Mark as Read / Delete"
                };
            } else {
                data[i] = new Object[] {
                    "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "Mark as Read / Delete"
                };
            }
        }
        return data;
    }

    @Override
    public void addNewListing() {
        // Create the frame for adding a new notification
        JFrame frame = new JFrame("Add New Notification");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Create the main panel with GridLayout for alignment
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns, dynamic rows

        // Sender ID field
        JLabel senderIDLabel = new JLabel("Sender ID:");
        JTextField senderIDField = new JTextField();
        panel.add(senderIDLabel);
        panel.add(senderIDField);

        // Receiver ID field
        JLabel receiverIDLabel = new JLabel("Receiver ID:");
        JTextField receiverIDField = new JTextField();
        panel.add(receiverIDLabel);
        panel.add(receiverIDField);

        // Content field
        JLabel contentLabel = new JLabel("Content:");
        JTextArea contentField = new JTextArea(3, 20);
        JScrollPane contentScrollPane = new JScrollPane(contentField); // Wrap JTextArea in JScrollPane
        panel.add(contentLabel);
        panel.add(contentScrollPane);

        // Save button
        JButton saveButton = new JButton("Save Notification");
        saveButton.addActionListener(e -> {
            // Get input values
            String senderID = senderIDField.getText().trim();
            String receiverID = receiverIDField.getText().trim();
            String content = contentField.getText().trim();

            // Check required fields
            if (senderID.isEmpty() || receiverID.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Sender ID, Receiver ID, and Content are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and save the notification
            boolean success = notificationManagement.createNotification(senderID, receiverID, content);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Notification created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshNotificationData(); // Refresh the table with new data
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to create notification. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
    public void editListing(String notificationID) {
        Notification existingNotification = notificationManagement.getNotificationByID(notificationID);
    
        if (existingNotification == null) {
            JOptionPane.showMessageDialog(this, "Notification not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        JFrame frame = new JFrame("Edit Notification");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);
    
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
    
        // Sender ID field (not editable)
        JLabel senderLabel = new JLabel("Sender ID:");
        JTextField senderField = new JTextField(existingNotification.getSenderID());
        senderField.setEditable(false);  // Sender ID is fixed
        panel.add(senderLabel);
        panel.add(senderField);
    
        // Receiver ID field (not editable)
        JLabel receiverLabel = new JLabel("Receiver ID:");
        JTextField receiverField = new JTextField(existingNotification.getReceiverID());
        receiverField.setEditable(false);  // Receiver ID is fixed
        panel.add(receiverLabel);
        panel.add(receiverField);
    
        // Notification Content field (editable)
        JLabel contentLabel = new JLabel("Content:");
        JTextArea contentArea = new JTextArea(existingNotification.getContent());
        contentArea.setRows(5);
        panel.add(contentLabel);
        panel.add(new JScrollPane(contentArea));
    
        // Is Read status (checkbox for editing read/unread)
        JLabel isReadLabel = new JLabel("Is Read:");
        JCheckBox isReadCheckBox = new JCheckBox("Read", existingNotification.getIsRead());
        panel.add(isReadLabel);
        panel.add(isReadCheckBox);
    
        // Timestamp field (not editable)
        JLabel timestampLabel = new JLabel("Timestamp:");
        JTextField timestampField = new JTextField(existingNotification.getTimestamp().toString());
        timestampField.setEditable(false);  // Timestamp is fixed
        panel.add(timestampLabel);
        panel.add(timestampField);
    
        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Get updated values
            String newContent = contentArea.getText().trim();
            boolean isReadStatus = isReadCheckBox.isSelected();  // Get the read/unread status
    
            // Update the notification
            existingNotification.setContent(newContent);
            existingNotification.setIsRead(isReadStatus);
    
            boolean success = notificationManagement.updateNotification(existingNotification.getNotificationID(), newContent, isReadStatus);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Notification updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshNotificationData();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update notification. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(saveButton);
    
        frame.add(panel);
        frame.setVisible(true);
    }    

    @Override
    public void deleteListing(String notificationID) {
        boolean success = notificationManagement.deleteNotification(notificationID);
        if (success) {
            JOptionPane.showMessageDialog(this, "Notification deleted successfully!");
            refreshNotificationData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete notification.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshNotificationData() {
        Object[][] newData = getNotificationsData();
        tableModel.setDataVector(newData, new Object[]{
            "NotificationID", "SenderID", "ReceiverID", "Content", "IsRead", "Timestamp", "Operation"
        });
    }
}

