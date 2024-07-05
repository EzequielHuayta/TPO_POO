package view.peticion;

import controller.PeticionController;
import model.peticion.PeticionDTO;
import model.practica.PracticaDTO;
import model.resultado.ResultadoDTO;
import utils.ButtonEditor;
import utils.ButtonListener;
import utils.ButtonRenderer;
import view.MainFrame;
import view.RefreshableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PeticionListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();
    private final PeticionController peticionController = PeticionController.getInstance();
    private DefaultTableModel tableModel;

    public PeticionListView(boolean isCritico) {


        // Controller
        peticionController.attachView(this);

        if(!isCritico) {

            // UI
            setLayout(new BorderLayout());
            JToolBar toolBar = new JToolBar();
            JButton backButton = new JButton("Atrás");
            backButton.addActionListener(e -> {
                peticionController.detachView(this);
                mainFrame.goBack();
            });

            JButton createUserButton = new JButton("Crear petición");
            createUserButton.addActionListener(e -> {
                mainFrame.addPanel(new PeticionFormView(), "peticionform");
                mainFrame.showPanel("peticionform");
            });

            JButton showCriticButton = new JButton("Listar peticiones criticas");
            showCriticButton.addActionListener(e -> {
                mainFrame.addPanel(new PeticionListView(true), "peticionlist");
                mainFrame.showPanel("peticionlist");
            });

            toolBar.setLayout(new BorderLayout());
            toolBar.add(backButton, BorderLayout.WEST);
            toolBar.add(showCriticButton, BorderLayout.SOUTH);
            toolBar.add(createUserButton, BorderLayout.EAST);
            add(toolBar, BorderLayout.NORTH);

            createTable(peticionController, isCritico);
        } else {
            // UI
            setLayout(new BorderLayout());
            JToolBar toolBar = new JToolBar();
            JButton backButton = new JButton("Atrás");
            backButton.addActionListener(e -> {
                peticionController.detachView(this);
                mainFrame.goBack();
            });

            toolBar.setLayout(new BorderLayout());
            toolBar.add(backButton, BorderLayout.WEST);
            add(toolBar, BorderLayout.NORTH);

            createTable(peticionController, isCritico);
        }
    }

    private void createTable(PeticionController peticionController, Boolean isCritico) {
        java.util.List<PeticionDTO> peticiones = null;
        String[] columnNames = {"ID", "Obra Social", "Fecha de carga", "Fecha entrega estimada", "Paciente", "Practicas", "Finalizada", "Acciones"};
        if (!isCritico) {
            peticiones = peticionController.getAllPeticiones();
        }
        else {
            peticiones = peticionController.getAllPeticionesCriticas();
        }
        Object[][] data = new Object[peticiones.size()][8];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < peticiones.size(); i++) {
            PeticionDTO peticion = peticiones.get(i);
            data[i][0] = peticion.getId();
            data[i][1] = peticion.getObraSocial();
            data[i][2] = dateFormat.format(peticion.getFechaCarga());
            data[i][3] = dateFormat.format(peticion.getFechaCalculadaEntrega());
            data[i][4] = peticion.getPaciente().getNombre();
            data[i][5] = practicasToString(peticion.getListPracticas());
            data[i][6] = finalizadaToString(isPeticionFinalizada(peticion.getListPracticas(), peticion.getListResultados()));
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
            public void onEditButtonClicked(int id) {
                mainFrame.addPanel(new PeticionFormView(peticionController.getPeticionByID(id)), "peticionform");
                mainFrame.showPanel("peticionform");
            }

            @Override
            public void onDeleteButtonClicked(int id) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar esta petición?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    peticionController.deletePeticion(id);
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

        List<PeticionDTO> peticones = peticionController.getAllPeticiones();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (PeticionDTO peticion : peticones) {
            Object[] row = {
                    peticion.getId(),
                    peticion.getObraSocial(),
                    dateFormat.format(peticion.getFechaCarga()),
                    dateFormat.format(peticion.getFechaCalculadaEntrega()),
                    peticion.getPaciente().getNombre(),
                    practicasToString(peticion.getListPracticas()),
                    finalizadaToString(isPeticionFinalizada(peticion.getListPracticas(), peticion.getListResultados())),
                    "Acciones"
            };
            tableModel.addRow(row);
        }

        tableModel.fireTableDataChanged();
    }

    private String practicasToString(List<PracticaDTO> practicasList){
        StringBuilder practicas;
        if(practicasList.isEmpty()){
            return "Sin prácticas";
        } else {
            practicas = new StringBuilder(practicasList.get(0).getNombre());
            practicasList.remove(0);
        }
        if(!practicasList.isEmpty()){
            for (PracticaDTO practica : practicasList){
                practicas.append(", ").append(practica.getNombre());
            }
        }
        return practicas.toString();
    }

    private Boolean isPeticionFinalizada(List<PracticaDTO> practicasList, List<ResultadoDTO> resultadosList){
        if(resultadosList.isEmpty()){
            return false;
        } else return practicasList.size() == resultadosList.size();
    }


    private String finalizadaToString(boolean valorFinalizada){
        if(valorFinalizada){
            return "Si";
        }else return "No";
    }
}

