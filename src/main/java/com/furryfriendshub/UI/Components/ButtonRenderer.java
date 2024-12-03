package com.furryfriendshub.UI.Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButton actionButton = new JButton("Edit/Delete");
        actionButton.setEnabled(true); // Set button as active
        return actionButton;
    }
}
