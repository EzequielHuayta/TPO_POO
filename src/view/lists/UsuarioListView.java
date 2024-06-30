package view.lists;

import controller.UsuarioController;
import model.usuario.Usuario;
import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioListView extends JPanel {

    public UsuarioListView() {
        // Controllers
        UsuarioController usuarioController = UsuarioController.getInstance();

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            MainFrame mainFrame = MainFrame.getInstance();
            mainFrame.goBack();
        });

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        add(toolBar, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Email", "Rol"};
        List<Usuario> usuarios = usuarioController.getAllUsuarios();
        Object[][] data = new Object[usuarios.size()][4];
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            data[i][0] = usuario.getId();
            data[i][1] = usuario.getEmail();
            data[i][3] = usuario.getRol();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
