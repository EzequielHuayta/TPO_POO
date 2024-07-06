package view;

import controller.UsuarioController;
import model.usuario.Rol;
import view.paciente.PacienteListView;
import view.peticion.PeticionListView;
import view.practica.PracticaListView;
import view.resultados.ResultadosListView;
import view.sucursal.SucursalListView;
import view.usuario.UsuarioListView;

import javax.swing.*;
import java.awt.*;

public class OptionsView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame;
    private final UsuarioController usuarioController;
    private final JButton listUsersButton;
    private final JButton listPracticesButton;
    private final JButton listarPeticiones;
    private final JButton listPatientsButton;
    private final JButton listSucursalesButton;
    private final JButton listResultsButton;

    public OptionsView() {
        // Controllers
        usuarioController = UsuarioController.getInstance();
        usuarioController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        JButton backButton = new JButton("Cerrar sesión");
        backButton.addActionListener(e -> {
            MainFrame mainFrame = MainFrame.getInstance();
            mainFrame.goBack();
            usuarioController.logoutUser();
            usuarioController.detachView(this);
            JOptionPane.showMessageDialog(null, "La sesión fue cerrada con éxito", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);
        });

        toolBar.setLayout(new BorderLayout());
        toolBar.add(backButton, BorderLayout.WEST);
        add(toolBar, BorderLayout.NORTH);

        mainFrame = MainFrame.getInstance();

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("¡Bienvenido " + usuarioController.getLoggedUser().getNombre() + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(getFont().deriveFont(Font.PLAIN, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(80, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(welcomeLabel, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        listUsersButton = new JButton("Listar Usuarios");
        listPracticesButton = new JButton("Listar Prácticas");
        listarPeticiones = new JButton("Listar Peticiones");
        listPatientsButton = new JButton("Listar Pacientes");
        listSucursalesButton = new JButton("Listar Sucursales");
        listResultsButton = new JButton("Listar Resultados");

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        buttonPanel.add(listUsersButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        buttonPanel.add(listPracticesButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        buttonPanel.add(listarPeticiones, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        buttonPanel.add(listPatientsButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        buttonPanel.add(listSucursalesButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        buttonPanel.add(listResultsButton, gridBagConstraints);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(buttonPanel, gbc);

        // Add the mainPanel to the OptionsView
        add(mainPanel, BorderLayout.CENTER);

        // Interactions
        listUsersButton.addActionListener(e -> {
            mainFrame.addPanel(new UsuarioListView(), "usuariolist");
            mainFrame.showPanel("usuariolist");
        });

        listPracticesButton.addActionListener(e -> {
            mainFrame.addPanel(new PracticaListView(), "practicalist");
            mainFrame.showPanel("practicalist");
        });

        listarPeticiones.addActionListener(e -> {
            mainFrame.addPanel(new PeticionListView(false), "peticionlist");
            mainFrame.showPanel("peticionlist");
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

        setActionsDependingOnRol(usuarioController.getLoggedUser().getRol());
    }

    private void setActionsDependingOnRol(Rol rol) {
        switch (rol) {
            case ADMINISTRADOR:
                listUsersButton.setEnabled(true);
                listPracticesButton.setEnabled(true);
                listarPeticiones.setEnabled(true);
                listPatientsButton.setEnabled(true);
                listSucursalesButton.setEnabled(true);
                listResultsButton.setEnabled(true);
                break;
            case RECEPCION:
                listUsersButton.setEnabled(false);
                listPracticesButton.setEnabled(false);
                listarPeticiones.setEnabled(true);
                listPatientsButton.setEnabled(true);
                listSucursalesButton.setEnabled(false);
                listResultsButton.setEnabled(false);
                break;
            case LABORATORISTA:
                listUsersButton.setEnabled(false);
                listPracticesButton.setEnabled(false);
                listarPeticiones.setEnabled(false);
                listPatientsButton.setEnabled(false);
                listSucursalesButton.setEnabled(false);
                listResultsButton.setEnabled(true);
                break;
        }
    }

    @Override
    public void onRefresh() {
        setActionsDependingOnRol(usuarioController.getLoggedUser().getRol());
        this.revalidate();
        this.repaint();
    }
}
