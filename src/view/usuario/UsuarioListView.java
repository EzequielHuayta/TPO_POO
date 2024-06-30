package view.usuario;

import controller.UsuarioController;
import model.usuario.UsuarioDTO;
import view.MainFrame;
import view.RefreshableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class UsuarioListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();
    private final UsuarioController usuarioController = UsuarioController.getInstance();
    private DefaultTableModel tableModel;

    public UsuarioListView() {
        // Controller
        usuarioController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            usuarioController.detachView(this);
            mainFrame.goBack();
        });

        JButton createUserButton = new JButton("Crear usuario");
        createUserButton.addActionListener(e -> {
            mainFrame.addPanel(new UsuarioFormView(), "usuarioform");
            mainFrame.showPanel("usuarioform");
        });

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        toolBar.add(createUserButton, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        createTable(usuarioController);
    }

    private void createTable(UsuarioController usuarioController) {
        String[] columnNames = {"ID", "Email", "Password", "Nombre", "Domicilio", "DNI", "Fecha Nacimiento", "Rol"};
        List<UsuarioDTO> usuarios = usuarioController.getAllUsuarios();
        Object[][] data = new Object[usuarios.size()][8];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < usuarios.size(); i++) {
            UsuarioDTO usuario = usuarios.get(i);
            data[i][0] = usuario.getId();
            data[i][1] = usuario.getEmail();
            data[i][2] = usuario.getPassword();
            data[i][3] = usuario.getNombre();
            data[i][4] = usuario.getDomicilio();
            data[i][5] = usuario.getDni();
            data[i][6] = dateFormat.format(usuario.getFechaNacimiento());
            data[i][7] = usuario.getRol().toString();
        }

        tableModel = new DefaultTableModel(data, columnNames);
        JTable userTable = new JTable(tableModel);

        // Adjusting column sizes
        TableColumnModel columnModel = userTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onRefresh() {
        tableModel.setRowCount(0);

        List<UsuarioDTO> usuarios = usuarioController.getAllUsuarios();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (UsuarioDTO usuario : usuarios) {
            Object[] row = {
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getPassword(),
                    usuario.getNombre(),
                    usuario.getDomicilio(),
                    usuario.getDni(),
                    dateFormat.format(usuario.getFechaNacimiento()),
                    usuario.getRol().toString()
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }
}
