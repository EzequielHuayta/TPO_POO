package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static utils.ButtonRenderer.getScaledIcon;

public class ButtonEditor extends DefaultCellEditor {
    protected JPanel panel;
    protected JButton editButton;
    protected JButton deleteButton;

    private int selectedRow;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, ButtonListener buttonListener) {
        super(checkBox);
        panel = new JPanel(new GridLayout(1, 2, 0, 0));
        editButton = new JButton(getScaledIcon("src/resources/pencil-outline.png"));
        deleteButton = new JButton(getScaledIcon("src/resources/trash-can-outline.png"));

        editButton.addActionListener(e -> {
            fireEditingStopped();
            buttonListener.onEditButtonClicked((int) tableModel.getValueAt(selectedRow, 0));
        });

        deleteButton.addActionListener(e -> {
            fireEditingStopped();
            buttonListener.onDeleteButtonClicked((int) tableModel.getValueAt(selectedRow, 0));
        });
        panel.add(editButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "Acciones";
    }
}
