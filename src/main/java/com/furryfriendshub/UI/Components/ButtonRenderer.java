package com.furryfriendshub.UI.Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JComboBox<String> implements TableCellRenderer {

    public ButtonRenderer() {
        super(new String[] {"Edit", "Delete"});
        setRenderer(new DefaultListCellRenderer());
        setEditable(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setSelectedItem(value);
        return this;
    }
}