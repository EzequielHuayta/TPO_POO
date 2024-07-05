package view;

import controller.UsuarioController;
import view.paciente.PacienteListView;
import view.practica.PracticaListView;
import view.resultados.ResultadosListView;
import view.sucursal.SucursalListView;
import view.usuario.UsuarioListView;

import javax.swing.*;
import java.awt.*;

public class OptionsView extends JPanel {

    MainFrame mainFrame;
    UsuarioController usuarioController;

    public OptionsView() {
        // Controllers
        UsuarioController usuarioController = UsuarioController.getInstance();

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton backButton = new JButton("Cerrar sesión");
        backButton.addActionListener(e -> {
            MainFrame mainFrame = MainFrame.getInstance();
            mainFrame.goBack();
            usuarioController.logoutUser();
            JOptionPane.showMessageDialog(null, "La sesión fue cerrada con éxito", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
        });

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        add(toolBar, BorderLayout.NORTH);

        mainFrame = MainFrame.getInstance();
        setAdministratorOptions();
    }

    private void setAdministratorOptions() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        JButton listUsersButton = new JButton("Listar Usuarios");
        JButton listPracticesButton = new JButton("Listar Prácticas");
        JButton listCriticalPracticesButton = new JButton("Listar Prácticas con Resultados Críticos");
        JButton listPatientsButton = new JButton("Listar Pacientes");
        JButton listSucursalesButton = new JButton("Listar Sucursales");
        JButton listResultsButton = new JButton("Listar Resultados");

        // Check user role and disable buttons accordingly
        UsuarioController usuarioController = UsuarioController.getInstance();

        String role = usuarioController.getLoggedUser().getRol().name();
        switch (role) {
            case "ADMINISTRADOR":
                listUsersButton.setEnabled(true);
                listPracticesButton.setEnabled(true);
                listCriticalPracticesButton.setEnabled(true);
                listPatientsButton.setEnabled(true);
                listSucursalesButton.setEnabled(true);
                listResultsButton.setEnabled(true);
                break;
            case "RECEPCION":
                // Receptionists have limited access
                listUsersButton.setEnabled(true);
                listPracticesButton.setEnabled(true);
                listCriticalPracticesButton.setEnabled(true);
                listPatientsButton.setEnabled(true);
                listSucursalesButton.setEnabled(true);
                listResultsButton.setEnabled(true);
                break;
            case "LABORATORISTA":
                listUsersButton.setEnabled(false);
                listPracticesButton.setEnabled(false);
                listCriticalPracticesButton.setEnabled(false);
                listPatientsButton.setEnabled(false);
                listSucursalesButton.setEnabled(false);
                listResultsButton.setEnabled(true);

                break;
            default:
                // Default to no access
                listUsersButton.setEnabled(false);
                listPracticesButton.setEnabled(false);
                listCriticalPracticesButton.setEnabled(false);
                listPatientsButton.setEnabled(false);
                listSucursalesButton.setEnabled(false);
                listResultsButton.setEnabled(false);
                break;
        }

        // Posicionar botones en el GridBagLayout
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(listUsersButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(listPracticesButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(listCriticalPracticesButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel.add(listPatientsButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panel.add(listSucursalesButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panel.add(listResultsButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(panel, BorderLayout.CENTER);

        // Interactions
        listUsersButton.addActionListener(e -> {
            mainFrame.addPanel(new UsuarioListView(), "usuariolist");
            mainFrame.showPanel("usuariolist");
        });

        listPracticesButton.addActionListener(e -> {
            mainFrame.addPanel(new PracticaListView(), "practicalist");
            mainFrame.showPanel("practicalist");
        });

        listCriticalPracticesButton.addActionListener(e -> {
            mainFrame.addPanel(new UsuarioListView(), "usuariolist");
            mainFrame.showPanel("usuariolist");
        });

        listPatientsButton.addActionListener(e -> {
            mainFrame.addPanel(new PacienteListView(), "pacientelist");
            mainFrame.showPanel("pacientelist");
        });

        listSucursalesButton.addActionListener(e -> {
            mainFrame.addPanel(new SucursalListView(), "sucursallist");
            mainFrame.showPanel("sucursallist");
        });

        listResultsButton.addActionListener(e -> {
            mainFrame.addPanel(new ResultadosListView(), "resultadolist");
            mainFrame.showPanel("resultadolist");
        });

    }
}
