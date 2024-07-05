package view.resultados;

import controller.PracticaController;
import controller.ResultadosController;
import model.practica.PracticaDTO;
import model.resultadopeticion.ResultadoPeticionDTO;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ResultadoFormView extends JPanel {
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;
    private JComboBox<String> practicaComboBox;
    private JTextField valorField;
    private ResultadosController resultadosController;

    private PracticaController practicaController;
    private final MainFrame mainFrame = MainFrame.getInstance();


    public ResultadoFormView() {
        initializeView();
        // Botón crear resultado
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear resultado");
        createButton.addActionListener(e -> createResultado());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public ResultadoFormView(ResultadoPeticionDTO resultado) {
        initializeView();
        // Botón editar resultado
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar resultado");
        createButton.addActionListener(e -> updateResultado(resultado.getId()));
        buttonPanel.add(createButton);

        practicaComboBox.setSelectedItem(resultado.getNombrePractica());
        valorField.setText(Integer.toString(resultado.getValor()));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView(){
        // Controllers
        resultadosController = ResultadosController.getInstance();
        practicaController = PracticaController.getInstance();

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
        // Practicas
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Practicas:"), gbc);

        practicaComboBox = new JComboBox<>();
        populatePracticaComboBox();
        gbc.gridx = 1;
        formPanel.add(practicaComboBox, gbc);
        // valor
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("valor:"), gbc);
        valorField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(valorField, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void populatePracticaComboBox() {

        practicaComboBox.removeAllItems();

        List<PracticaDTO> practicasList = practicaController.getAllPracticas();
        for (PracticaDTO practica : practicasList) {
            practicaComboBox.addItem(practica.getNombrePractica());
        }
    }

    private void createResultado() {
        if (!validateNonEmptyFields(valorField)){
            return;
        }
        PracticaDTO selectedPractica = (PracticaDTO) practicaComboBox.getSelectedItem();
        int valor = Integer.parseInt(valorField.getText());

        resultadosController.addResultado(selectedPractica, valor);
        JOptionPane.showMessageDialog(this, "Resultado creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.goBack();
    }

    private void updateResultado(int idUsuario) {
        if (!validateNonEmptyFields(valorField)){
            return;
        }
        PracticaDTO selectedPractica = (PracticaDTO) practicaComboBox.getSelectedItem();
        String valor = valorField.getText();
        if(valor == null ){
            return;
        }
        resultadosController.updateResultado(idUsuario, selectedPractica, Integer.parseInt(valor));
        JOptionPane.showMessageDialog(this, "Usuario modificado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.goBack();
    }


    private boolean validateNonEmptyFields(JTextField... fields){
        // Validar campos vacios
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

}


