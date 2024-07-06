package view.resultados;

import controller.PeticionController;
import controller.PracticaController;
import controller.ResultadosController;

import controller.UsuarioController;
import model.peticion.PeticionDTO;
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
    private final PeticionController peticionController = PeticionController.getInstance();

    private final PracticaController practicaController = PracticaController.getInstance();
    private DefaultTableModel tableModel;
    private final UsuarioDTO loggedUser;

    public ResultadosListView() {
        // Controller
        resultadosController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            resultadosController.detachView(this);
            mainFrame.goBack();
        });

        JLabel titleLabel = new JLabel("Resultados");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton createButton = new JButton("Crear resultado");
        createButton.addActionListener(e -> {
            mainFrame.addPanel(new ResultadoFormView(), "resultadoForm");
            mainFrame.showPanel("resultadoForm");
        });

        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(titleLabel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(createButton);
        add(toolBar, BorderLayout.NORTH);

        loggedUser = usuarioController.getLoggedUser();
        if (loggedUser.getRol() == Rol.ADMINISTRADOR) {
            createAdminTable(resultadosController);
        } else {
            createTable(resultadosController);
        }
    }

    private void createAdminTable(ResultadosController resultadosController) {
        String[] columnNames = {"ID", "Tipo de practica", "Valor", "Peticion Asociada", "Acciones"};
        List<ResultadoDTO> resultados = resultadosController.getAllResultados();
        Object[][] data = new Object[resultados.size()][5];

        for (int i = 0; i < resultados.size(); i++) {
            ResultadoDTO resultado = resultados.get(i);
            boolean isReservado = resultado.esReservado();
            data[i][0] = resultado.getResultadoID();
            data[i][1] = resultado.getTipoPractica().getNombre();
            data[i][2] = isReservado ? "Retirar por sucursal" : resultado.getValor();
            data[i][3] = resultado.getPeticionAsociada() + " - " + peticionController.getPeticionByID(resultado.getPeticionAsociada()).getPaciente().getNombre();
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
                    ResultadoDTO resultadoDTO = resultadosController.getResultadoByID(id);
                    updatePeticionAfterResultadoDeletion(resultadoDTO.getPeticionAsociada(), resultadoDTO);
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
            boolean isReservado = resultado.esReservado();

            data[i][0] = resultado.getResultadoID();
            data[i][1] = resultado.getTipoPractica().getNombre();
            data[i][2] = isReservado ? "Retirar por sucursal" : resultado.getValor();
            data[i][3] = resultado.getPeticionAsociada() + " - " + peticionController.getPeticionByID(resultado.getPeticionAsociada()).getPaciente().getNombre();
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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

        if (loggedUser.getRol() == Rol.ADMINISTRADOR) {
            for (ResultadoDTO resultado : resultados) {
                boolean isReservado = resultado.esReservado();

                Object[] row = {
                        resultado.getResultadoID(),
                        resultado.getTipoPractica().getNombre(),
                        isReservado ? "Retirar por sucursal" : resultado.getValor(),
                        resultado.getPeticionAsociada() + " - " + peticionController.getPeticionByID(resultado.getPeticionAsociada()).getPaciente().getNombre(),
                        "Acciones"
                };
                tableModel.addRow(row);
            }
        } else {
            for (ResultadoDTO resultado : resultados) {
                Object[] row = {
                        resultado.getResultadoID(),
                        resultado.getTipoPractica().getNombre(),
                        resultado.getValor(),
                        resultado.getPeticionAsociada() + " - " + peticionController.getPeticionByID(resultado.getPeticionAsociada()).getPaciente().getNombre(),
                };
                tableModel.addRow(row);
            }
        }
        tableModel.fireTableDataChanged();
    }

    private void updatePeticionAfterResultadoDeletion(int peticionID, ResultadoDTO resultadoDTO) {
        PeticionDTO peticion = peticionController.getPeticionByID(peticionID);
        peticion.removeResultado(resultadoDTO);
        peticionController.updatePeticion(peticion);
    }
}
