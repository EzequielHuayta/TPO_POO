package view.sucursal;

import controller.SucursalController;
import model.sucursal.SucursalDTO;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SucursalDeleteView extends JDialog {

    private JToolBar toolBar;
    private JButton backButton;
    private JButton deleteButton;
    private JComboBox<String> sucursalesComboBox;
    private SucursalController sucursalController;
    private final MainFrame mainFrame = MainFrame.getInstance();
    private Frame parentFrame;

    public SucursalDeleteView(Frame parent, SucursalDTO sucursal) {
        super(parent, "Borrar Sucursal", true);
        this.parentFrame = parent;
        initializeView(sucursal);
    }

    private void initializeView(SucursalDTO sucursal) {
        // Controllers
        sucursalController = SucursalController.getInstance();

        List<SucursalDTO> sucursales = new ArrayList<>();
        for (SucursalDTO suc : sucursalController.getAllSucursales()) {
            if (suc.getNumero() != sucursal.getNumero()) {
                sucursales.add(suc);
            }
        }

        List<String> sucursalDirecciones = new ArrayList<>();
        for (SucursalDTO sucursalDTO : sucursales) {
            sucursalDirecciones.add(sucursalDTO.getDireccion());
        }

        // Configurar el Layout
        setLayout(new BorderLayout());

        // ToolBar
        toolBar = new JToolBar();
        toolBar.setLayout(new BorderLayout());

        toolBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, toolBar.getMinimumSize().height));
        add(toolBar, BorderLayout.NORTH);

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // ComboBox para seleccionar la sucursal
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Seleccione Sucursal:"), gbc);
        sucursalesComboBox = new JComboBox<>(sucursalDirecciones.toArray(new String[0]));
        gbc.gridx = 1;
        formPanel.add(sucursalesComboBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Botón borrar sucursal
        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Borrar Sucursal");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSucursal(sucursales.get(sucursalesComboBox.getSelectedIndex()).getNumero());
            }
        });
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parentFrame);
    }

    private void deleteSucursal(int numero) {
        sucursalController.deleteSucursal(numero);
        JOptionPane.showMessageDialog(this, "Sucursal borrada");
        dispose();
    }
}
