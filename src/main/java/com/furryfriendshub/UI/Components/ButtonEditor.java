package com.furryfriendshub.UI.Components;

import com.furryfriendshub.UI.Dashboard.ListingsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private String currentListingID;
    private ActionHandler actionHandler;

    public ButtonEditor(ActionHandler actionHandler) {
        super(new JComboBox<>(new String[] {"Edit", "Delete"}));
        this.actionHandler = actionHandler;

        // Set combobox properties
        JComboBox comboBox = (JComboBox) getComponent();
        comboBox.setEditable(false);
        comboBox.setMaximumRowCount(2);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();
                if ("Edit".equals(selectedOption)) {
                    actionHandler.editListing(currentListingID);
                } else if ("Delete".equals(selectedOption)) {
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
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}