package view;

import controller.UsuarioController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginView() {
        // Controllers
        UsuarioController usuarioController = UsuarioController.getInstance();

        // UI
        JLabel firstnameLabel = new JLabel("Email: ");
        JLabel lastnameLabel = new JLabel("Contraseña: ");
        emailField = new JTextField(25);
        passwordField = new JPasswordField(25);
        loginButton = new JButton("Iniciar sesión");
        loginButton.setPreferredSize(new Dimension(278, 40));
        // Space between fields
        Insets fieldsInset = new Insets(0, 0, 10, 0);
        // Space between buttons
        Insets buttonInset = new Insets(20,0,0,0);

        // Grid Bag Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = fieldsInset;
        gridBagConstraints.fill = GridBagConstraints.NONE;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        add(firstnameLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;

        add(emailField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;

        add(lastnameLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;

        add(passwordField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = buttonInset;

        add(loginButton, gridBagConstraints);

        // Interactions
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if(usuarioController.authenticateUsuario(email, password)){
                emailField.setText("");
                passwordField.setText("");
                MainFrame mainFrame = MainFrame.getInstance();
                mainFrame.addPanel(new OptionsView(), "options");
                mainFrame.showPanel("options");
            }else {
                JOptionPane.showMessageDialog(null, "Email o contraseña invalidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

