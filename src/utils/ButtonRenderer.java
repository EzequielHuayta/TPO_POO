package utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setLayout(new GridLayout(1, 2, 0, 0));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButton editButton = new JButton(getScaledIcon("src/resources/pencil-outline.png"));
        JButton deleteButton = new JButton(getScaledIcon("src/resources/trash-can-outline.png"));
        editButton.setPreferredSize(new Dimension(30, table.getRowHeight()));
        deleteButton.setPreferredSize(new Dimension(30, table.getRowHeight()));
        removeAll();
        add(editButton);
        add(deleteButton);
        return this;
    }

    public static ImageIcon getScaledIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Adjust the size as needed
        return new ImageIcon(scaledImg);
    }
}
