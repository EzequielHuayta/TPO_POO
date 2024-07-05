package view.resultados;

import controller.ResultadosController;

import controller.UsuarioController;
import model.usuario.Rol;
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
import model.resultado.ResultadoDTO;

public class ResultadosListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();

    private final ResultadosController resultadosController = ResultadosController.getInstance();
    private final UsuarioController usuarioController = UsuarioController.getInstance();
    private DefaultTableModel tableModel;
    private UsuarioDTO loggedUser;

    public ResultadosListView() {
        // Controller
        resultadosController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            resultadosController.detachView(this);
            mainFrame.goBack();
        });

        JButton createResult = new JButton("Crear resultado");
        createResult.addActionListener(e -> {
            mainFrame.addPanel(new ResultadoFormView(), "resultadoForm");
            mainFrame.showPanel("resultadoForm");
        });


        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        toolBar.add(createResult, BorderLayout.EAST);
        add(toolBar, BorderLayout.NORTH);

        loggedUser = usuarioController.getLoggedUser();
        if(loggedUser.getRol() == Rol.ADMINISTRADOR){
            createAdminTable(resultadosController);
        }else {
            createTable(resultadosController);
        }
    }

    private void createAdminTable(ResultadosController resultadosController) {
        String[] columnNames = {"ID", "Tipo de practica", "Valor", "Peticion Asociada", "Acciones"};
        List<ResultadoDTO> resultados = resultadosController.getAllResultados();
        Object[][] data = new Object[resultados.size()][5];

        for (int i = 0; i < resultados.size(); i++) {
            ResultadoDTO resultado = resultados.get(i);
            data[i][0] = resultado.getId();
            data[i][1] = resultado.getTipoPractica().getNombre();
            data[i][2] = resultado.getValor();
            data[i][3] = resultado.getPeticionAsociada().getId();
            data[i][4] = "Acciones";
        }

        tableModel = new DefaultTableModel(data, columnNames);
        // Custom JTable implementation
        JTable resultsTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }

        };

        resultsTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
        resultsTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, new ButtonListener() {
            @Override
            public void onEditButtonClicked(int id) {
                mainFrame.addPanel(new ResultadoFormView(resultadosController.getResultadoByID(id)), "resultadoForm");
                mainFrame.showPanel("resultadoForm");
            }

            @Override
            public void onDeleteButtonClicked(int id) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar este resultado?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    resultadosController.deleteResultado(id);
                }
            }
        }));

        // Adjusting column sizes
        TableColumnModel columnModel = resultsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createTable(ResultadosController resultadosController) {
        String[] columnNames = {"ID", "Tipo de practica", "Valor", "Peticion Asociada"};
        List<ResultadoDTO> resultados = resultadosController.getAllResultados();
        Object[][] data = new Object[resultados.size()][4];

        for (int i = 0; i < resultados.size(); i++) {
            ResultadoDTO resultado = resultados.get(i);
            data[i][0] = resultado.getId();
            data[i][1] = resultado.getTipoPractica().getNombre();
            data[i][2] = resultado.getValor();
            data[i][3] = resultado.getPeticionAsociada().getId();
        }

        tableModel = new DefaultTableModel(data, columnNames);
        JTable resultsTable = new JTable(tableModel) {
            // Nothing to do here
        };

        // Adjusting column sizes
        TableColumnModel columnModel = resultsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onRefresh() {
        tableModel.setRowCount(0);

        List<ResultadoDTO> resultados = resultadosController.getAllResultados();

        if(loggedUser.getRol() == Rol.ADMINISTRADOR){
            for (ResultadoDTO resultado : resultados) {
                Object[] row = {
                        resultado.getId(),
                        resultado.getTipoPractica().getNombre(),
                        resultado.getValor(),
                        resultado.getPeticionAsociada().getId(),
                        "Acciones"
                };
                tableModel.addRow(row);
            }
        }else {
            for (ResultadoDTO resultado : resultados) {
                Object[] row = {
                        resultado.getId(),
                        resultado.getTipoPractica().getNombre(),
                        resultado.getValor(),
                        resultado.getPeticionAsociada().getId(),
                };
                tableModel.addRow(row);
            }
        }
        tableModel.fireTableDataChanged();
    }
}
