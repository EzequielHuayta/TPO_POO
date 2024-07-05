package view.practica;

import controller.PracticaController;
import model.practica.PracticaDTO;
import utils.ABMResult;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PracticaFormView extends JPanel{
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;

    private JTextField nombreField;
    private JTextField grupoField;
    private JTextField valoresCriticosField;
    private JTextField valoresReservadosField;
    private JTextField cantidadHorasResultado;
    private JComboBox<String> habilitadoComboBox;
    private final String[] estados = {"Habilitado", "Deshabilitado"};

    private PracticaController practicaController;
    private final MainFrame mainFrame = MainFrame.getInstance();


    public PracticaFormView() {
        initializeView();
        // Botón crear práctica
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear práctica");
        createButton.addActionListener(e -> createPractica());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public PracticaFormView(PracticaDTO practica) {
        initializeView();
        // Botón editar práctica
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar práctica");
        createButton.addActionListener(e -> updateResultado(practica.getCodigo()));
        buttonPanel.add(createButton);

        nombreField.setText(practica.getNombre());
        grupoField.setText(Integer.toString(practica.getGrupo()));
        valoresCriticosField.setText(Integer.toString(practica.getValoresCriticos()));
        valoresReservadosField.setText(Integer.toString(practica.getValoresReservados()));
        cantidadHorasResultado.setText(Integer.toString(practica.getCantidadHorasResultados()));
        habilitadoComboBox.setSelectedItem(booleanStatusToString(practica.isHabilitada()));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView(){
        // Controllers
        practicaController = PracticaController.getInstance();

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
        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre:"), gbc);
        nombreField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);
        // Grupo
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Grupo:"), gbc);
        grupoField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(grupoField, gbc);
        // Valores Criticos
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Valores Criticos:"), gbc);
        valoresCriticosField = new JTextField(20);
        valoresCriticosField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        gbc.gridx = 1;
        formPanel.add(valoresCriticosField, gbc);
        // Valores reservados
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Valores reservados:"), gbc);
        valoresReservadosField = new JTextField(20);
        valoresReservadosField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        gbc.gridx = 1;
        formPanel.add(valoresReservadosField, gbc);
        //Cantidad horas Resultado
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Cantidad horas Resultado:"), gbc);
        cantidadHorasResultado = new JTextField(20);
        cantidadHorasResultado.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        gbc.gridx = 1;
        formPanel.add(cantidadHorasResultado, gbc);
        // Habilitado
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Estado:"), gbc);
        habilitadoComboBox = new JComboBox<>(estados);
        gbc.gridx = 1;
        formPanel.add(habilitadoComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    private void createPractica() {
        if (!validateNonEmptyFields(nombreField) || !validateNonEmptyFields(grupoField) || !validateNonEmptyFields(valoresCriticosField) || !validateNonEmptyFields(valoresReservadosField) || !validateNonEmptyFields(cantidadHorasResultado)){
            return;
        }
        String nombre = nombreField.getText();
        int grupo = Integer.parseInt(grupoField.getText());
        int valoresCriticos = Integer.parseInt(valoresCriticosField.getText());
        int valoresReservados = Integer.parseInt(valoresReservadosField.getText());
        int cantidadHorasResultados =  Integer.parseInt(cantidadHorasResultado.getText());
        String habilitadoString = (String) habilitadoComboBox.getSelectedItem();
        assert habilitadoString != null;
        ABMResult abmResult = practicaController.addPractica(new PracticaDTO(nombre, grupo, valoresCriticos,valoresReservados, cantidadHorasResultados, stringnStatusToBoolean(habilitadoString)));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateResultado(int codigo) {
        if (!validateNonEmptyFields(nombreField) || !validateNonEmptyFields(grupoField) || !validateNonEmptyFields(valoresCriticosField) || !validateNonEmptyFields(valoresReservadosField) || !validateNonEmptyFields(cantidadHorasResultado)){
            return;
        }
        String nombre = nombreField.getText();
        int grupo = Integer.parseInt(grupoField.getText());
        int valoresCriticos = Integer.parseInt(valoresCriticosField.getText());
        int valoresReservados = Integer.parseInt(valoresReservadosField.getText());
        int cantidadHorasResultados =  Integer.parseInt(cantidadHorasResultado.getText());
        String habilitadoString = (String) habilitadoComboBox.getSelectedItem();
        assert habilitadoString != null;
        ABMResult abmResult = practicaController.updatePractica(new PracticaDTO(codigo, nombre, grupo, valoresCriticos,valoresReservados, cantidadHorasResultados, stringnStatusToBoolean(habilitadoString)));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    private String booleanStatusToString(boolean status){
        if(status){
            return estados[0];
        }else return estados[1];
    }

    private boolean stringnStatusToBoolean(String status){
        return status.equals(estados[0]);
    }
}


