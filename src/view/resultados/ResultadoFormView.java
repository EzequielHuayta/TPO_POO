package view.resultados;

import controller.PeticionController;
import controller.PracticaController;
import controller.ResultadosController;
import model.peticion.PeticionDTO;
import model.practica.PracticaDTO;
import model.resultado.ResultadoDTO;
import utils.ABMResult;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ResultadoFormView extends JPanel {
    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;

    private JComboBox<PracticaDTO> practicaComboBox;
    private JComboBox<PeticionDTO> peticionComboBox;
    private JTextField valorField;

    private ResultadosController resultadosController;
    private PracticaController practicaController;
    private PeticionController peticionController;
    private PracticaDTO[] practicasArray;
    private PeticionDTO[] peticionesArray;
    private ResultadoDTO oldResultado = null;

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

    public ResultadoFormView(ResultadoDTO resultado) {
        initializeView();
        // Botón editar resultado
        oldResultado = resultado;
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar resultado");
        createButton.addActionListener(e -> updateResultado(resultado.getResultadoID()));
        buttonPanel.add(createButton);

        practicaComboBox.setSelectedItem(practicaController.getPracticaByCodigo(resultado.getTipoPractica().getCodigo()));
        peticionComboBox.setSelectedItem(peticionController.getPeticionByID(resultado.getPeticionAsociada()));
        valorField.setText(Integer.toString(resultado.getValor()));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView() {
        // Controllers
        resultadosController = ResultadosController.getInstance();
        practicaController = PracticaController.getInstance();
        peticionController = PeticionController.getInstance();
        practicasArray = practicaController.getAllPracticasAsArray();
        peticionesArray = peticionController.getAllPeticionesArray();

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
        // Valor
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Valor:"), gbc);
        valorField = new JTextField(20);
        valorField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || valorField.getText().length() >= 8) {
                    e.consume();
                }
            }
        });
        gbc.gridx = 1;
        formPanel.add(valorField, gbc);
        // Practicas
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Tipo de practica:"), gbc);
        practicaComboBox = new JComboBox<>(practicasArray);
        gbc.gridx = 1;
        formPanel.add(practicaComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
        // Practicas
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Petición asociada:"), gbc);
        peticionComboBox = new JComboBox<>(peticionesArray);
        gbc.gridx = 1;
        formPanel.add(peticionComboBox, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    private void createResultado() {
        if (!validateNonEmptyFields(valorField)) {
            return;
        }
        PracticaDTO tipoPractica = (PracticaDTO) practicaComboBox.getSelectedItem();
        PeticionDTO peticionAsociada = (PeticionDTO) peticionComboBox.getSelectedItem();
        int valor = Integer.parseInt(valorField.getText());
        assert peticionAsociada != null;
        ResultadoDTO resultadoDTO = new ResultadoDTO(tipoPractica, valor, peticionAsociada.getId());
        ABMResult abmResult = resultadosController.addResultado(resultadoDTO);
        if (abmResult.getResult()) {
            resultadoDTO.setResultadoID(resultadosController.getLastCreatedID());
            updatePeticionWithNewResultado(resultadoDTO, oldResultado);
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.goBack();
        } else {
            JOptionPane.showMessageDialog(this, abmResult.getResultMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateResultado(int id) {
        if (!validateNonEmptyFields(valorField)) {
            return;
        }
        PracticaDTO tipoPractica = (PracticaDTO) practicaComboBox.getSelectedItem();
        PeticionDTO peticionAsociada = (PeticionDTO) peticionComboBox.getSelectedItem();
        int valor = Integer.parseInt(valorField.getText());
        assert peticionAsociada != null;
        ResultadoDTO resultadoDTO = new ResultadoDTO(id, tipoPractica, valor, peticionAsociada.getId());
        ABMResult abmResult = resultadosController.updateResultado(resultadoDTO);
        if (abmResult.getResult()) {
            updatePeticionWithNewResultado(resultadoDTO, oldResultado);
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

    private void updatePeticionWithNewResultado(ResultadoDTO newResultadoDTO, ResultadoDTO oldResultadoDTO) {
        if (oldResultadoDTO != null) {
            PeticionDTO oldPeticion = peticionController.getPeticionByID(oldResultadoDTO.getPeticionAsociada());
            oldPeticion.removeResultado(oldResultadoDTO);
        }
        PeticionDTO peticion = peticionController.getPeticionByID(newResultadoDTO.getPeticionAsociada());
        peticion.addResultado(newResultadoDTO);
        peticionController.updatePeticion(peticion);
    }
}
