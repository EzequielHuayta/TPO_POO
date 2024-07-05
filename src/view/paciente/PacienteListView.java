package view.paciente;

import controller.PacienteController;
import controller.SucursalController;
import model.paciente.PacienteDTO;
import model.sucursal.SucursalDTO;
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

public class PacienteListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();
    private final PacienteController pacienteController = PacienteController.getInstance();
    private final SucursalController sucursalController = SucursalController.getInstance();
    private DefaultTableModel tableModel;

    public PacienteListView() {
        // Controller
        pacienteController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            pacienteController.detachView(this);
            mainFrame.goBack();
        });

        JButton createUserButton = new JButton("Crear paciente");
        createUserButton.addActionListener(e -> {
            mainFrame.addPanel(new PacienteFormView(), "pacienteform");
            mainFrame.showPanel("pacienteform");
        });

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        toolBar.add(createUserButton, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        createTable(pacienteController);
    }

    private void createTable(PacienteController pacienteController) {
        String[] columnNames = {"ID", "Email", "Nombre", "Domicilio", "DNI", "Sexo", "Edad", "Sucursal", "Acciones"};
        List<PacienteDTO> pacientes = pacienteController.getAllPacientes();
        Object[][] data = new Object[pacientes.size()][9];

        for (int i = 0; i < pacientes.size(); i++) {
            PacienteDTO paciente = pacientes.get(i);
            data[i][0] = paciente.getId();
            data[i][1] = paciente.getEmail();
            data[i][2] = paciente.getNombre();
            data[i][3] = paciente.getDomicilio();
            data[i][4] = paciente.getDni();
            data[i][5] = paciente.getSexo();
            data[i][6] = paciente.getEdad();
            data[i][7] = getSucursalNameFromNumber(paciente.getSucursalAsignada());
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
                mainFrame.addPanel(new PacienteFormView(pacienteController.getPacienteByID(id)), "pacienteform");
                mainFrame.showPanel("pacienteform");
            }

            @Override
            public void onDeleteButtonClicked(int id) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar este paciente?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    pacienteController.deletePaciente(id);
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
        List<PacienteDTO> pacientes = pacienteController.getAllPacientes();
        for (PacienteDTO paciente : pacientes) {
            Object[] row = {
                    paciente.getId(),
                    paciente.getEmail(),
                    paciente.getNombre(),
                    paciente.getDomicilio(),
                    paciente.getDni(),
                    paciente.getSexo(),
                    paciente.getEdad(),
                    getSucursalNameFromNumber(paciente.getSucursalAsignada()),
                    "Acciones"
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }

    private String getSucursalNameFromNumber(int sucursalNumber) {
        SucursalDTO sucursal = sucursalController.getSucursalByNumero(sucursalNumber);
        if (sucursal != null) {
            return sucursal.getDireccion();
        } else return "Sin asignar";
    }
}
