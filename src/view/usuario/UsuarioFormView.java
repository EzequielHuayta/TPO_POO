package view.usuario;

import controller.UsuarioController;
import model.usuario.Rol;
import model.usuario.UsuarioDTO;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import utils.ABMResult;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class UsuarioFormView extends JPanel {
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField nombreField;
    private JTextField domicilioField;
    private JTextField dniField;
    private JDatePickerImpl datePicker;
    private JComboBox<Rol> rolComboBox;
    private UsuarioController usuarioController;
    private final MainFrame mainFrame = MainFrame.getInstance();


    public UsuarioFormView() {
        initializeView();
        // Botón crear usuario
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear usuario");
        createButton.addActionListener(e -> createUsuario());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public UsuarioFormView(UsuarioDTO usuario) {
        initializeView();
        // Botón editar usuario
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar usuario");
        createButton.addActionListener(e -> updateUsuario(usuario.getId()));
        buttonPanel.add(createButton);

        emailField.setText(usuario.getEmail());
        passwordField.setText(usuario.getPassword());
        nombreField.setText(usuario.getNombre());
        domicilioField.setText(usuario.getDomicilio());
        dniField.setText(Integer.toString(usuario.getDni()));
        rolComboBox.setSelectedItem(usuario.getRol());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(usuario.getFechaNacimiento());
        datePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getModel().setSelected(true);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView(){
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
        // Fecha de Nacimiento
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Fecha de Nacimiento:"), gbc);
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(datePicker, gbc);
        // Rol
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Rol:"), gbc);
        rolComboBox = new JComboBox<>(Rol.values());
        gbc.gridx = 1;
        formPanel.add(rolComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    private void createUsuario() {
        if (!validateNonEmptyFields(emailField, passwordField, nombreField, domicilioField, dniField)){
            return;
        }
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String nombre = nombreField.getText();
        String domicilio = domicilioField.getText();
        Rol rol = (Rol) rolComboBox.getSelectedItem();
        Date fechaNacimiento = (Date) datePicker.getModel().getValue();
        String dniString = dniField.getText();
        if(!validateEmail(email) || !validateDNI(dniString) || !validateFechaNacimiento(fechaNacimiento)){
            return;
        }
        int dni = Integer.parseInt(dniString);

        // Hacemos el ABM y verificamos si fue exitoso
        ABMResult abmResult = usuarioController.addUsuario(new UsuarioDTO(email, password, nombre, domicilio, dni, fechaNacimiento, rol));
        if(abmResult.getResult()){
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        }else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUsuario(int idUsuario) {
        if (!validateNonEmptyFields(emailField, passwordField, nombreField, domicilioField, dniField)){
            return;
        }
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String nombre = nombreField.getText();
        String domicilio = domicilioField.getText();
        Rol rol = (Rol) rolComboBox.getSelectedItem();
        Date fechaNacimiento = (Date) datePicker.getModel().getValue();
        String dniString = dniField.getText();
        if(!validateEmail(email) || !validateDNI(dniString) || !validateFechaNacimiento(fechaNacimiento)){
            return;
        }
        int dni = Integer.parseInt(dniString);
        // Hacemos el ABM y verificamos si fue exitoso
        ABMResult abmResult = usuarioController.updateUsuario(new UsuarioDTO(idUsuario, email, password, nombre, domicilio, dni, fechaNacimiento, rol));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        }else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateNonEmptyFields(JTextField... fields){
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
        if(!email.matches(emailRegex)){
            JOptionPane.showMessageDialog(this, "Por favor ingrese un email válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateDNI(String dni) {
        if (dni.length() != 8){
            JOptionPane.showMessageDialog(this, "Ingrese un DNI válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateFechaNacimiento(Date fechaNacimiento) {
        if (fechaNacimiento == null){
            JOptionPane.showMessageDialog(this, "Por favor ingrese una fecha de nacimiento", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "dd-MM-yyyy";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }
}
