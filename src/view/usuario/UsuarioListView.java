package view.usuario;

import controller.UsuarioController;
import model.usuario.UsuarioDTO;
import utils.ButtonEditor;
import utils.ButtonListener;
import utils.ButtonRenderer;
import view.MainFrame;
import view.RefreshableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        String[] columnNames = {"ID", "Email", "Password", "Nombre", "Domicilio", "DNI", "Fecha Nacimiento", "Rol", "Acciones"};
        List<UsuarioDTO> usuarios = usuarioController.getAllUsuarios();
        Object[][] data = new Object[usuarios.size()][9];
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
            data[i][8] = "Acciones";
        }

        tableModel = new DefaultTableModel(data, columnNames);
        // Custom JTable implementation
        JTable userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }

        };

        userTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, new ButtonListener() {
            @Override
            public void onEditButtonClicked(int id) {
                mainFrame.addPanel(new UsuarioFormView(usuarioController.getUsuarioByID(id)), "usuarioform");
                mainFrame.showPanel("usuarioform");
            }

            @Override
            public void onDeleteButtonClicked(int id) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    usuarioController.deleteUsuario(id);
                }
            }
        }));

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
                    usuario.getRol().toString(),
                    "Acciones"
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }
}
