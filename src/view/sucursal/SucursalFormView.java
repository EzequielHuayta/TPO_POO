package view.sucursal;

import controller.SucursalController;
import controller.UsuarioController;
import model.sucursal.SucursalDTO;
import model.usuario.UsuarioDTO;
import utils.ABMResult;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SucursalFormView extends JPanel {
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;
    private JTextField direccionField;
    private JComboBox responsableComboBox;
    private UsuarioController usuarioController;
    private SucursalController sucursalController;
    private final MainFrame mainFrame = MainFrame.getInstance();
    private UsuarioDTO[] usuariosArray;

    public SucursalFormView() {
        initializeView();
        // Botón crear sucursal
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear sucursal");
        createButton.addActionListener(e -> createSucursal());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public SucursalFormView(SucursalDTO sucursal) {
        initializeView();
        // Botón editar sucursal
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar sucursal");
        createButton.addActionListener(e -> updateSucursal(sucursal.getNumero()));
        buttonPanel.add(createButton);

        direccionField.setText(sucursal.getDireccion());
        responsableComboBox.setSelectedItem(usuarioController.getUsuarioByID(sucursal.getResponsableTecnico()));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView() {
        // Controllers
        usuarioController = UsuarioController.getInstance();
        sucursalController = SucursalController.getInstance();
        usuariosArray = usuarioController.getAllUsuariosAsArray();
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
        // Dirección
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Dirección:"), gbc);
        direccionField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(direccionField, gbc);
        // Responsable Técnico
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Responsable Técnico:"), gbc);
        responsableComboBox = new JComboBox<>(usuariosArray);
        gbc.gridx = 1;
        formPanel.add(responsableComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    private void createSucursal() {
        if (!validateNonEmptyFields(direccionField)) {
            return;
        }
        String direccion = direccionField.getText();
        UsuarioDTO usuario = (UsuarioDTO) responsableComboBox.getSelectedItem();

        // Hacemos el ABM y verificamos si fue exitoso
        assert usuario != null;
        ABMResult abmResult = sucursalController.addSucursal(new SucursalDTO(direccion, usuario.getId()));
        if (abmResult.getResult()) {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSucursal(int numeroSucursal) {
        if (!validateNonEmptyFields(direccionField)) {
            return;
        }
        String direccion = direccionField.getText();
        UsuarioDTO usuario = (UsuarioDTO) responsableComboBox.getSelectedItem();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un usuario válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Hacemos el ABM y verificamos si fue exitoso
        ABMResult abmResult = sucursalController.updateSucursal(new SucursalDTO(numeroSucursal, direccion, usuario.getId()));
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
}
