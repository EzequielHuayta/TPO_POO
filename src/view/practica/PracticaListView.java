package view.practica;

import controller.PracticaController;
import model.practica.PracticaDTO;
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

public class PracticaListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();
    private final PracticaController practicaController = PracticaController.getInstance();
    private DefaultTableModel tableModel;

    public PracticaListView() {
        // Controller
        practicaController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            practicaController.detachView(this);
            mainFrame.goBack();
        });

        JLabel titleLabel = new JLabel("Prácticas");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton createButton = new JButton("Crear práctica");
        createButton.addActionListener(e -> {
            mainFrame.addPanel(new PracticaFormView(), "practicaform");
            mainFrame.showPanel("practicaform");
        });

        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(titleLabel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(createButton);
        add(toolBar, BorderLayout.NORTH);

        createTable(practicaController);
    }

    private void createTable(PracticaController practicaController) {
        String[] columnNames = {"Codigo", "Nombre", "Grupo", "Valores Criticos", "Valores Reservados", "Cant. Horas Resultado", "Habilitado", "Acciones"};
        java.util.List<PracticaDTO> practicas = practicaController.getAllPracticas();
        Object[][] data = new Object[practicas.size()][8];

        for (int i = 0; i < practicas.size(); i++) {
            PracticaDTO practica = practicas.get(i);
            data[i][0] = practica.getCodigo();
            data[i][1] = practica.getNombre();
            data[i][2] = practica.getGrupo();
            data[i][3] = practica.getValoresCriticos();
            data[i][4] = practica.getValoresReservados();
            data[i][5] = practica.getCantidadHorasResultados();
            data[i][6] = habilitadaToString(practica.isHabilitada());
            data[i][7] = "Acciones";
        }

        tableModel = new DefaultTableModel(data, columnNames);
        // Custom JTable implementation
        JTable userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }

        };

        userTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, new ButtonListener() {
            @Override
            public void onEditButtonClicked(int codigoPractica) {
                mainFrame.addPanel(new PracticaFormView(practicaController.getPracticaByCodigo(codigoPractica)), "practicaform");
                mainFrame.showPanel("practicaform");
            }

            @Override
            public void onDeleteButtonClicked(int codigoPractica) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar esta práctica?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    practicaController.deletePractica(codigoPractica);
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

        List<PracticaDTO> practicas = practicaController.getAllPracticas();

        for (PracticaDTO practica : practicas) {
            Object[] row = {
                    practica.getCodigo(),
                    practica.getNombre(),
                    practica.getGrupo(),
                    practica.getValoresCriticos(),
                    practica.getValoresReservados(),
                    practica.getCantidadHorasResultados(),
                    habilitadaToString(practica.isHabilitada()),
                    "Acciones"
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }

    private String habilitadaToString(boolean valorHabilitada) {
        if (valorHabilitada) {
            return "Si";
        } else return "No";
    }
}

