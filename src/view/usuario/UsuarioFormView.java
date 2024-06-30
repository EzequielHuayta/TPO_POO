package view.usuario;

import controller.UsuarioController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import model.usuario.Rol;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class UsuarioFormView extends JPanel {
    private final JButton backButton;
    private final JButton createButton;
    private final JToolBar toolBar;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JTextField nombreField;
    private final JTextField domicilioField;
    private final JTextField dniField;
    private DatePicker datePicker;
    private final JComboBox<Rol> rolComboBox;
    private final UsuarioController usuarioController;
    private final MainFrame mainFrame = MainFrame.getInstance();


    public UsuarioFormView() {
        // Controllers
        usuarioController = UsuarioController.getInstance();

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
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Contraseña:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Nombre:"), gbc);
        nombreField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        // Domicilio
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Domicilio:"), gbc);
        domicilioField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(domicilioField, gbc);

        // DNI
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("DNI:"), gbc);
        dniField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(dniField, gbc);

        // Fecha de Nacimiento
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Fecha de Nacimiento:"), gbc);

        // Date Picker using JavaFX
        JFXPanel datePanel = new JFXPanel();
        datePanel.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(datePanel, gbc);

        // Set up JavaFX DatePicker
        Platform.runLater(() -> {
            StackPane root = new StackPane();
            datePicker = new DatePicker();
            root.getChildren().add(datePicker);
            Scene scene = new Scene(root);
            datePanel.setScene(scene);
        });

        // Rol
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Rol:"), gbc);
        rolComboBox = new JComboBox<>(Rol.values());
        gbc.gridx = 1;
        formPanel.add(rolComboBox, gbc);

        // Botones
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear usuario");
        createButton.addActionListener(e -> createUsuario());
        buttonPanel.add(createButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createUsuario() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String nombre = nombreField.getText();
        String domicilio = domicilioField.getText();
        Rol rol = (Rol) rolComboBox.getSelectedItem();
        int dni;
        Date fechaNacimiento;

        // Validar DNI
        try {
            dni = Integer.parseInt(dniField.getText());
            if (Integer.toString(dni).length() != 8) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            fechaNacimiento = java.sql.Date.valueOf(datePicker.getValue());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha de nacimiento válida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar campos vacíos
        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || domicilio.isEmpty() || fechaNacimiento.toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar email
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Ingrese un email válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar contraseña
        if (password.length() < 5) {
            JOptionPane.showMessageDialog(this, "Ingrese una contraseña de al menos 5 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioController.addUsuario(email, password, nombre, domicilio, dni, fechaNacimiento, rol);
        JOptionPane.showMessageDialog(this, "Usuario creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.goBack();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}

