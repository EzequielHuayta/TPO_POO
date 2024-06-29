package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Stack<String> panelHistory;
    private final Map<String, JPanel> panelMap;

    private MainFrame() {
        super("Sistema de gestión");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        panelHistory = new Stack<>();
        panelMap = new HashMap<>();

        int FRAME_WIDTH = 1200;
        int FRAME_HEIGHT = 700;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public void addPanel(JPanel panel, String name) {
        mainPanel.add(panel, name);
        panelMap.put(name, panel);
    }

    public void showPanel(String name) {
        if (!panelHistory.isEmpty() && !panelHistory.peek().equals(name)) {
            panelHistory.push(name);
        } else if (panelHistory.isEmpty()) {
            panelHistory.push(name);
        }
        cardLayout.show(mainPanel, name);
    }

    public void goBack() {
        if (!panelHistory.isEmpty()) {
            String currentPanel = panelHistory.pop();
            mainPanel.remove(panelMap.get(currentPanel));
            panelMap.remove(currentPanel);
        }
    }
}
