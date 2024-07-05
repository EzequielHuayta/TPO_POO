package view.practica;

import controller.PracticaController;
import model.practica.PracticaDTO;
import model.usuario.UsuarioDTO;
import utils.ButtonEditor;
import utils.ButtonListener;
import utils.ButtonRenderer;
import view.MainFrame;
import view.RefreshableView;
import view.usuario.UsuarioFormView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
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
        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            practicaController.detachView(this);
            mainFrame.goBack();
        });

        JButton createUserButton = new JButton("Crear practica");
        createUserButton.addActionListener(e -> {
            mainFrame.addPanel(new PracticaFormView(), "practicaform");
            mainFrame.showPanel("practicaform");
        });

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        toolBar.add(createUserButton, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        createTable(practicaController);
    }

    private void createTable(PracticaController practicaController) {
        String[] columnNames = {"Codigo", "Nombre", "Grupo", "Valores Criticos", "Valores Reservados", "Cant. Horas Resultado", "Habilitado", "Acciones"};
        java.util.List<PracticaDTO> practicas = practicaController.getAllPracticas();
        Object[][] data = new Object[practicas.size()][8];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < practicas.size(); i++) {
            PracticaDTO practica = practicas.get(i);
            data[i][0] = practica.getCodigoPractica();
            data[i][1] = practica.getNombrePractica();
            data[i][2] = practica.getGrupo();
            data[i][3] = practica.getValoresCriticos();
            data[i][4] = practica.getValoresReservados();
            data[i][5] = practica.getCantidadHorasResultados();
            data[i][6] = practica.isHabilitada();
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
                mainFrame.addPanel(new PracticaFormView(practicaController.getCodigoPractica(codigoPractica)), "practicaform");
                mainFrame.showPanel("practicaform");
            }

            @Override
            public void onDeleteButtonClicked(int codigoPractica) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    practicaController.deleteUsuario(codigoPractica);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (PracticaDTO practica : practicas) {
            Object[] row = {
                    practica.getCodigoPractica(),
                    practica.getNombrePractica(),
                    practica.getGrupo(),
                    practica.getValoresCriticos(),
                    practica.getValoresReservados(),
                    practica.getCantidadHorasResultados(),
                    practica.isHabilitada(),
                    "Acciones"
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }
}

