package com.furryfriendshub.UI.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private String currentListingID;
    private ActionHandler actionHandler;
    private JButton actionButton;

    public ButtonEditor(ActionHandler actionHandler) {
        super(new JCheckBox()); // Placeholder, we are using a button instead
        this.actionHandler = actionHandler;

        actionButton = new JButton("Edit/Delete");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show confirmation dialog with options to "Edit" or "Delete"
                int option = JOptionPane.showOptionDialog(
                        null,
                        "Would you like to edit or delete this user?",
                        "Select Action",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Edit", "Delete"},
                        "Edit" // Default selection
                );

                if (option == 0) { // Edit option selected
                    actionHandler.editListing(currentListingID);
                } else if (option == 1) { // Delete option selected
                    actionHandler.deleteListing(currentListingID);
                }
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return currentListingID;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentListingID = (String) table.getValueAt(row, 0); // Assume Listing ID is in column 0
        actionButton.setText("Edit/Delete"); // Reset button text
        return actionButton;
    }
}
