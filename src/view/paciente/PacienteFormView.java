package view.paciente;

import controller.PacienteController;
import controller.SucursalController;
import model.paciente.PacienteDTO;
import model.sucursal.SucursalDTO;

import utils.ABMResult;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacienteFormView extends JPanel {
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;
    private JTextField nombreField;
    private JTextField domicilioField;
    private JTextField dniField;
    private JTextField emailField;
    private JComboBox<Character> sexoComboBox;
    private JTextField edadField;
    private JComboBox<SucursalDTO> sucursalComboBox;
    private PacienteController pacienteController;
    private SucursalController sucursalController;
    private final MainFrame mainFrame = MainFrame.getInstance();
    private final Character[] sexos = {'M', 'F'};
    private SucursalDTO[] sucursalesArray;

    public PacienteFormView() {
        initializeView();
        // Botón crear paciente
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear paciente");
        createButton.addActionListener(e -> createPaciente());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public PacienteFormView(PacienteDTO paciente) {
        initializeView();
        // Botón editar paciente
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar paciente");
        createButton.addActionListener(e -> updatePaciente(paciente.getId()));
        buttonPanel.add(createButton);

        nombreField.setText(paciente.getNombre());
        domicilioField.setText(paciente.getDomicilio());
        dniField.setText(Integer.toString(paciente.getDni()));
        emailField.setText(paciente.getEmail());
        sexoComboBox.setSelectedItem(paciente.getSexo());
        edadField.setText(Integer.toString(paciente.getEdad()));
        sucursalComboBox.setSelectedItem(sucursalController.getSucursalByNumero(paciente.getSucursalAsignada()));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView() {
        // Controllers
        pacienteController = PacienteController.getInstance();
        sucursalController = SucursalController.getInstance();
        sucursalesArray = sucursalController.getAllSucursalesAsArray();
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
        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);
        nombreField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);
        // Domicilio
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Domicilio:"), gbc);
        domicilioField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(domicilioField, gbc);
        // DNI
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("DNI:"), gbc);
        dniField = new JTextField(20);
        dniField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || dniField.getText().length() >= 8) {
                    e.consume();
                }
            }
        });
        gbc.gridx = 1;
        formPanel.add(dniField, gbc);
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        // Sexo
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Sexo:"), gbc);
        sexoComboBox = new JComboBox<>(sexos);
        gbc.gridx = 1;
        formPanel.add(sexoComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
        // Edad
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Edad:"), gbc);
        edadField = new JTextField(20);
        edadField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || edadField.getText().length() >= 3) {
                    e.consume();
                }
            }
        });
        gbc.gridx = 1;
        formPanel.add(edadField, gbc);
        // Sucursal
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Sucursal:"), gbc);
        sucursalComboBox = new JComboBox<>(sucursalesArray);
        gbc.gridx = 1;
        formPanel.add(sucursalComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    private void createPaciente() {
        if (!validateNonEmptyFields(nombreField, domicilioField, dniField, emailField, edadField)) {
            return;
        }
        String nombre = nombreField.getText();
        String domicilio = domicilioField.getText();
        String dniString = dniField.getText();
        String email = emailField.getText();
        Character sexoCharacter = (Character) sexoComboBox.getSelectedItem();
        String edadString = edadField.getText();
        SucursalDTO sucursal = (SucursalDTO) sucursalComboBox.getSelectedItem();

        if (!validateEmail(email) || !validateDNI(dniString)) {
            return;
        }
        int dni = Integer.parseInt(dniString);
        int edad = Integer.parseInt(edadString);
        char sexo = 'x';
        if (sexoCharacter != null) {
            sexo = sexoCharacter;
        }

        // Hacemos el ABM y verificamos si fue exitoso
        assert sucursal != null;
        ABMResult abmResult = pacienteController.addPaciente(new PacienteDTO(dni, nombre, email, domicilio, sexo, edad, sucursal.getNumero()));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePaciente(int idPaciente) {
        if (!validateNonEmptyFields(nombreField, domicilioField, dniField, emailField, edadField)) {
            return;
        }
        String nombre = nombreField.getText();
        String domicilio = domicilioField.getText();
        String dniString = dniField.getText();
        String email = emailField.getText();
        Character sexoCharacter = (Character) sexoComboBox.getSelectedItem();
        String edadString = edadField.getText();
        SucursalDTO sucursal = (SucursalDTO) sucursalComboBox.getSelectedItem();

        if (!validateEmail(email) || !validateDNI(dniString)) {
            return;
        }
        int dni = Integer.parseInt(dniString);
        int edad = Integer.parseInt(edadString);
        char sexo = 'x';
        if (sexoCharacter != null) {
            sexo = sexoCharacter;
        }

        if (sucursal == null) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese una sucursal válida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hacemos el ABM y verificamos si fue exitoso
        ABMResult abmResult = pacienteController.updatePaciente(new PacienteDTO(idPaciente, dni, nombre, email, domicilio, sexo, edad, sucursal.getNumero()));
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

    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un email válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateDNI(String dni) {
        if (dni.length() != 8) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
