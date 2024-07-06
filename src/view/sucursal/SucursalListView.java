package view.sucursal;

import controller.PeticionController;
import controller.SucursalController;
import controller.UsuarioController;
import model.peticion.PeticionDTO;
import model.sucursal.SucursalDTO;
import model.usuario.UsuarioDTO;
import utils.ButtonEditor;
import utils.ButtonListener;
import utils.ButtonRenderer;
import view.MainFrame;
import view.RefreshableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class SucursalListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();
    private final SucursalController sucursalController = SucursalController.getInstance();
    private final UsuarioController usuarioController = UsuarioController.getInstance();
    private final PeticionController peticionController = PeticionController.getInstance();
    private DefaultTableModel tableModel;

    public SucursalListView() {
        // Controller
        sucursalController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            sucursalController.detachView(this);
            mainFrame.goBack();
        });

        JLabel titleLabel = new JLabel("Sucursales");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton createButton = new JButton("Crear sucursal");
        createButton.addActionListener(e -> {
            mainFrame.addPanel(new SucursalFormView(), "sucursalform");
            mainFrame.showPanel("sucursalform");
        });

        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(titleLabel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(createButton);
        add(toolBar, BorderLayout.NORTH);

        createTable(sucursalController);
    }

    private void createTable(SucursalController sucursalController) {
        String[] columnNames = {"Número", "Dirección", "Responsable técnico", "Acciones"};
        List<SucursalDTO> sucursales = sucursalController.getAllSucursales();
        Object[][] data = new Object[sucursales.size()][4];

        for (int i = 0; i < sucursales.size(); i++) {
            SucursalDTO sucursal = sucursales.get(i);
            data[i][0] = sucursal.getNumero();
            data[i][1] = sucursal.getDireccion();
            data[i][2] = getUsuarioNameFromID(sucursal.getResponsableTecnico());
            data[i][3] = "Acciones";
        }

        tableModel = new DefaultTableModel(data, columnNames);
        // Custom JTable implementation
        JTable userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }

        };

        userTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, new ButtonListener() {
            @Override
            public void onEditButtonClicked(int numero) {
                mainFrame.addPanel(new SucursalFormView(sucursalController.getSucursalByNumero(numero)), "sucursalform");
                mainFrame.showPanel("sucursalform");
            }

            @Override
            public void onDeleteButtonClicked(int id) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar esta sucursal?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    if (hasPeticionesFinalizadas(id)) {
                        JOptionPane.showMessageDialog(mainFrame, "No se permite borrar sucursales con resultados finalizados", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        SucursalDeleteView dialog = new SucursalDeleteView(mainFrame, sucursalController.getSucursalByNumero(id));
                        dialog.setVisible(true);
                    }
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

        List<SucursalDTO> sucursales = sucursalController.getAllSucursales();

        for (SucursalDTO sucursal : sucursales) {
            Object[] row = {
                    sucursal.getNumero(),
                    sucursal.getDireccion(),
                    getUsuarioNameFromID(sucursal.getResponsableTecnico()),
                    "Acciones"
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }

    private String getUsuarioNameFromID(int idResponsable) {
        UsuarioDTO usuario = usuarioController.getUsuarioByID(idResponsable);
        if (usuario != null) {
            return usuario.getNombre();
        } else return "Sin asignar";
    }

    private boolean hasPeticionesFinalizadas(int numeroSucursal) {
        List<PeticionDTO> peticiones = peticionController.getAllPeticiones();
        for (PeticionDTO peticionDTO : peticiones) {
            if (peticionDTO.getNumeroSucursal() == numeroSucursal && peticionDTO.isFinalizada()) {
                return true;
            }
        }
        return false;
    }
}
