package view.peticion;

import controller.PacienteController;
import controller.PeticionController;
import controller.PracticaController;
import model.paciente.PacienteDTO;
import model.peticion.PeticionDTO;
import model.practica.PracticaDTO;
import model.resultado.ResultadoDTO;
import utils.ABMResult;
import utils.CheckListItem;
import utils.CheckListRenderer;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PeticionFormView extends JPanel{
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;

    private JTextField obraSocialField;
    private JComboBox<PacienteDTO> pacienteComboBox;
    private JList<CheckListItem> practicasCheckList;

    private PeticionController peticionController;
    private PacienteController pacienteController;
    private PracticaController practicaController;
    private PracticaDTO[] practicasArray;
    private PacienteDTO[] pacientesArray;

    private final MainFrame mainFrame = MainFrame.getInstance();


    public PeticionFormView() {
        initializeView();
        // Botón crear práctica
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear petición");
        createButton.addActionListener(e -> createPractica());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public PeticionFormView(PeticionDTO peticion) {
        initializeView();
        // Botón editar práctica
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar petición");
        createButton.addActionListener(e -> updateResultado(peticion.getId(), peticion.getListResultados()));
        buttonPanel.add(createButton);

        obraSocialField.setText(peticion.getObraSocial());
        marcarPracticasSeleccionadas(peticion.getListPracticas());
        pacienteComboBox.setSelectedItem(pacienteController.getPacienteByID(peticion.getPaciente().getId()));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView(){
        // Controllers
        practicaController = PracticaController.getInstance();
        peticionController = PeticionController.getInstance();
        pacienteController = PacienteController.getInstance();
        practicasArray = practicaController.getAllPracticasAsArray();
        pacientesArray = pacienteController.getAllPacientesAsArray();

        // UI
        setLayout(new BorderLayout());
        // ToolBar
        toolBar = new JToolBar();
        toolBar.setLayout(new BorderLayout());
        backButton = new JButton("Cancelar");
        backButton.addActionListener(e -> {
            mainFrame.goBack();
        });
        toolBar.add(backButton, BorderLayout.WEST);
        toolBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, toolBar.getMinimumSize().height));
        add(toolBar, BorderLayout.NORTH);
        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        // Obra social
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Obra Social:"), gbc);
        obraSocialField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(obraSocialField, gbc);
        // Practicas
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Practicas:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        DefaultListModel<CheckListItem> listModel = new DefaultListModel<>();
        for (PracticaDTO practica : practicasArray) {
            if(practica.isHabilitada()){
                listModel.addElement(new CheckListItem(practica));
            }
        }
        practicasCheckList = new JList<>(listModel);
        practicasCheckList.setCellRenderer(new CheckListRenderer());
        practicasCheckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        practicasCheckList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList<CheckListItem> list = (JList<CheckListItem>) event.getSource();
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                list.repaint(list.getCellBounds(index, index));
            }
        });
        JScrollPane scrollPane = new JScrollPane(practicasCheckList);
        scrollPane.setPreferredSize(new Dimension(300, 150)); // Adjust size as needed
        formPanel.add(scrollPane, gbc);
        // Paciente
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Paciente:"), gbc);
        pacienteComboBox = new JComboBox<>(pacientesArray);
        gbc.gridx = 1;
        formPanel.add(pacienteComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    private void createPractica() {
        if (!validateNonEmptyFields(obraSocialField)){
            return;
        }
        String obraSocial = obraSocialField.getText();
        List<PracticaDTO> practicasSeleccionadas = new ArrayList<>();
        DefaultListModel<CheckListItem> listModel = (DefaultListModel<CheckListItem>) practicasCheckList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            CheckListItem item = listModel.getElementAt(i);
            if (item.isSelected()) {
                practicasSeleccionadas.add(item.getPractica());
            }
        }
        PacienteDTO paciente = (PacienteDTO) pacienteComboBox.getSelectedItem();
        ABMResult abmResult = peticionController.addPeticion(new PeticionDTO(obraSocial, practicasSeleccionadas, paciente));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateResultado(int id, List<ResultadoDTO> listaResultados) {
        if (!validateNonEmptyFields(obraSocialField)){
            return;
        }
        String obraSocial = obraSocialField.getText();
        List<PracticaDTO> practicasSeleccionadas = new ArrayList<>();
        DefaultListModel<CheckListItem> listModel = (DefaultListModel<CheckListItem>) practicasCheckList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            CheckListItem item = listModel.getElementAt(i);
            if (item.isSelected()) {
                practicasSeleccionadas.add(item.getPractica());
            }
        }
        PacienteDTO paciente = (PacienteDTO) pacienteComboBox.getSelectedItem();
        ABMResult abmResult = peticionController.updatePeticion(new PeticionDTO(id, obraSocial, practicasSeleccionadas, listaResultados, paciente));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarPracticasSeleccionadas(List<PracticaDTO> practicasSeleccionadas) {
        DefaultListModel<CheckListItem> listModel = (DefaultListModel<CheckListItem>) practicasCheckList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            CheckListItem item = listModel.getElementAt(i);
            for (PracticaDTO practicaSeleccionada : practicasSeleccionadas) {
                if (item.getPractica().equals(practicaSeleccionada)) {
                    item.setSelected(true);
                    break;
                }
            }
        }
        practicasCheckList.repaint();
    }


    private boolean validateNonEmptyFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}


