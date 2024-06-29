package view;

import controller.UsuarioController;

import javax.swing.*;
import java.awt.*;

public class OptionsView extends JPanel {
    private final JButton backButton;
    private final JToolBar toolBar;

    public OptionsView() {
        // Controllers
        UsuarioController usuarioController = UsuarioController.getInstance();

        // UI
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        toolBar = new JToolBar();
        backButton = new JButton("Cerrar sesión");
        backButton.addActionListener(e -> {
            MainFrame mainFrame = MainFrame.getInstance();
            mainFrame.goBack();
            JOptionPane.showMessageDialog(null, "La sesión fue cerrada con éxito", "Sesión cerrada", JOptionPane.INFORMATION_MESSAGE);

        });
        add(toolBar);
        toolBar.add(backButton);
        toolBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, toolBar.getMinimumSize().height));

    }
}

