package com.furryfriendshub.UI.Dashboard;

import com.furryfriendshub.model.*;
import com.furryfriendshub.management.*;
import com.furryfriendshub.UI.Components.ButtonEditor;
import com.furryfriendshub.UI.Components.ButtonRenderer;
import com.furryfriendshub.UI.Components.ActionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListingsPanel extends JPanel implements ActionHandler {
    private DefaultTableModel tableModel;
    private JTable listingTable;
    private AdoptionListingManagement listingManagement;

    public ListingsPanel(AdoptionListingManagement listingManagement) {
        this.listingManagement = listingManagement;
        initializePanel();
    }

    private void initializePanel() {
        this.setLayout(new BorderLayout());

        String[] columnNames = {"PetID", "Pet Name", "BasicInfo", "Date", "Type", "Owner ID", 
            "Availability", "Age", "Gender", "Neutered/Spayed", "Operation"};
        tableModel = new DefaultTableModel(getAdoptionListingsData(), columnNames);
        listingTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 10; // Allow editing in specific columns
            }
        };

        // Set ButtonRenderer and ButtonEditor for "Operation" column
        listingTable.getColumnModel().getColumn(10).setCellRenderer(new ButtonRenderer());
        listingTable.getColumnModel().getColumn(10).setCellEditor(new ButtonEditor(this));

        JScrollPane scrollPane = new JScrollPane(listingTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Fetch adoption listings data
    private Object[][] getAdoptionListingsData() {
        List<AdoptionListing> listings = listingManagement.getAllListings();
        Object[][] data = new Object[listings.size()][11];

        for (int i = 0; i < listings.size(); i++) {
            AdoptionListing listing = listings.get(i);
            if (listing != null){
                data[i] = new Object[] {
                    listing.getListingID(),
                    listing.getPetName(),
                    listing.getDescription(),
                    listing.getCreatedAt(),
                    listing.getPetType(),
                    listing.getOwnerID(),
                    listing.getIsAvailable() ? "Available" : "Not Available",  // Adoption Status
                    listing.getPetAge() != null ? listing.getPetAge() : null,  // Pet Age (default to null)
                    listing.getPetGender() != null ? listing.getPetGender() : null,  // Pet Gender (default to null)
                    listing.getPetNeuteredSpayed() != null ? listing.getPetNeuteredSpayed() : null,
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
        // Create the frame for adding a new listing
        JFrame frame = new JFrame("Add New Adoption Listing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Create the main panel with GridLayout for alignment
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns, dynamic rows

        // Pet Name field
        JLabel nameLabel = new JLabel("Pet Name:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        // Pet Type field
        JLabel typeLabel = new JLabel("Pet Type:");
        JTextField typeField = new JTextField();
        panel.add(typeLabel);
        panel.add(typeField);

        // Description field
        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionField = new JTextArea(3, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionField); // Wrap JTextArea in JScrollPane
        panel.add(descriptionLabel);
        panel.add(descriptionScrollPane);

        // Owner ID field
        JLabel ownerIDLabel = new JLabel("Owner ID:");
        JTextField ownerIDField = new JTextField();
        panel.add(ownerIDLabel);
        panel.add(ownerIDField);

        // Pet Age field
        JLabel petAgeLabel = new JLabel("Pet Age (optional):");
        JTextField petAgeField = new JTextField();
        panel.add(petAgeLabel);
        panel.add(petAgeField);

        // Pet Gender field
        JLabel petGenderLabel = new JLabel("Pet Gender (optional):");
        JTextField petGenderField = new JTextField();
        panel.add(petGenderLabel);
        panel.add(petGenderField);

        // Neutered/Spayed checkbox
        JLabel neuteredSpayedLabel = new JLabel("Neutered/Spayed:");
        JCheckBox neuteredSpayedCheckbox = new JCheckBox();
        panel.add(neuteredSpayedLabel);
        panel.add(neuteredSpayedCheckbox);

        // Available checkbox
        JLabel isAvailableLabel = new JLabel("Available for Adoption:");
        JCheckBox isAvailableCheckbox = new JCheckBox("", true);
        panel.add(isAvailableLabel);
        panel.add(isAvailableCheckbox);

        // Save button
        JButton saveButton = new JButton("Save Listing");
        saveButton.addActionListener(e -> {
            // Get input values
            String petName = nameField.getText().trim();
            String petType = typeField.getText().trim();
            String description = descriptionField.getText().trim();
            String ownerID = ownerIDField.getText().trim();

            // Check required fields
            if (petName.isEmpty() || petType.isEmpty() || description.isEmpty() || ownerID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Pet Name, Pet Type, Description, and Owner ID are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get optional fields
            Integer petAge = null;
            if (!petAgeField.getText().trim().isEmpty()) {
                try {
                    petAge = Integer.parseInt(petAgeField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Pet Age must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            String petGender = petGenderField.getText().trim().isEmpty() ? "Unknown" : petGenderField.getText().trim();
            boolean neuteredSpayed = neuteredSpayedCheckbox.isSelected();
            boolean isAvailable = isAvailableCheckbox.isSelected();

            // Create and save the adoption listing
            AdoptionListing newListing = new AdoptionListing(petName, petType, description, ownerID, petAge, petGender, neuteredSpayed);
            newListing.setIsAvailable(isAvailable);

            boolean success = newListing.saveListing();
            if (success) {
                JOptionPane.showMessageDialog(frame, "New listing added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshListingData(); // Refresh the table with new data
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add the listing. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
    public void editListing(String listingID) {
        AdoptionListingManagement listingManager = new AdoptionListingManagement();
        AdoptionListing existingListing = listingManager.getListingByID(listingID);
    
        if (existingListing == null) {
            JOptionPane.showMessageDialog(null, "Adoption listing with ID " + listingID + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Frame for the edit listing form
        JFrame frame = new JFrame("Edit Adoption Listing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);
    
        // Create the main panel with GridLayout for alignment
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns, dynamic rows
    
        // Pet Name field
        JLabel nameLabel = new JLabel("Name (optional):");
        JTextField nameField = new JTextField(existingListing.getPetName() != null ? existingListing.getPetName() : "");
        panel.add(nameLabel);
        panel.add(nameField);
    
        // Pet Type field
        JLabel typeLabel = new JLabel("Type (optional):");
        JTextField typeField = new JTextField(existingListing.getPetType() != null ? existingListing.getPetType() : "");
        panel.add(typeLabel);
        panel.add(typeField);
    
        // Description field
        JLabel descriptionLabel = new JLabel("Description (optional):");
        JTextField descriptionField = new JTextField(existingListing.getDescription() != null ? existingListing.getDescription() : "");
        panel.add(descriptionLabel);
        panel.add(descriptionField);
    
        // Available checkbox
        JLabel isAvailableLabel = new JLabel("Available for Adoption:");
        JCheckBox isAvailableCheckbox = new JCheckBox("", existingListing.getIsAvailable());
        panel.add(isAvailableLabel);
        panel.add(isAvailableCheckbox);
    
        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Get input values
            String petName = nameField.getText().trim();
            String petType = typeField.getText().trim();
            String description = descriptionField.getText().trim();
            boolean isAvailable = isAvailableCheckbox.isSelected();

            // Validate required fields and update listing
            existingListing.setPetName(petName);
            existingListing.setPetType(petType);
            existingListing.setDescription(description);
            existingListing.setIsAvailable(isAvailable);

            // Save updated listing
            boolean success = existingListing.saveListing();
            if (success) {
                JOptionPane.showMessageDialog(frame, "Listing updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                refreshListingData(); // Refresh the table with new data
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update the listing. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
    public void deleteListing(String listingID) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this listing?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean success = listingManagement.deleteListing(listingID);
            if (success) {
                JOptionPane.showMessageDialog(null, "Listing deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshListingData(); // Refresh the table with updated data
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete the listing.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Refresh the table data after any changes
    private void refreshListingData() {
        Object[][] newData = getAdoptionListingsData(); // Get updated data
        
        tableModel.setDataVector(newData, new Object[]{
            "PetID", "Pet Name", "BasicInfo", "Date", "Type", "Owner ID", "Availability", "Age", "Gender", "Neutered/Spayed", "Operation"
        });
    }
}
