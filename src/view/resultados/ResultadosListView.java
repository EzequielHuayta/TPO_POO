package view.resultados;

import controller.ResultadosController;

import view.MainFrame;
import view.RefreshableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.resultadopeticion.ResultadoPeticionDTO;

public class ResultadosListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();

    private final ResultadosController resultadosController = ResultadosController.getInstance();
    private DefaultTableModel tableModel;

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

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        add(toolBar, BorderLayout.NORTH);

        createTable(resultadosController);
    }



    private void createTable(ResultadosController resultadosController) {
        String[] columnNames = {"ID", "Practicas", "valor"};
        List<ResultadoPeticionDTO> resultados = resultadosController.getAllResultados();
        Object[][] data = new Object[resultados.size()][8];
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < resultados.size(); i++) {
            ResultadoPeticionDTO resultado = resultados.get(i);
            data[i][0] = resultado.getId();
            data[i][1] = resultado.getTipoPractica();
            data[i][2] = resultado.getValor();
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

        List<ResultadoPeticionDTO> resultados = resultadosController.getAllResultados();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (ResultadoPeticionDTO resultado : resultados) {
            Object[] row = {
                    resultado.getId(),
                    resultado.getNombrePractica(), //falta modificar esto
                    resultado.getValor()

            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }
}

