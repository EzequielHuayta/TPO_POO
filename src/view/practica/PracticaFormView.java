package view.practica;

import controller.PracticaController;
import controller.ResultadosController;
import model.practica.PracticaDTO;
import model.resultadopeticion.ResultadoPeticionDTO;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PracticaFormView extends JPanel{

    private JButton backButton;
    private JButton createButton;
    private JToolBar toolBar;

    private JTextField nombrePracticaField;
    private JTextField grupoField;
    private JTextField valoresCriticosField;
    private JTextField valoresReservadosField;
    private JTextField cantidadHorasResultado;

    private JComboBox<String> habilitadoComboBox;

    private PracticaController practicaController;
    private final MainFrame mainFrame = MainFrame.getInstance();


    public PracticaFormView() {
        initializeView();
        // Botón crear resultado
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Crear practica");
        createButton.addActionListener(e -> createPractica());
        buttonPanel.add(createButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public PracticaFormView(PracticaDTO practica) {
        initializeView();
        // Botón editar resultado
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Modificar practica");
        createButton.addActionListener(e -> updateResultado(practica.getCodigoPractica()));
        buttonPanel.add(createButton);

        nombrePracticaField.setText(practica.getNombrePractica());
        grupoField.setText(Integer.toString(practica.getGrupo()));
        valoresCriticosField.setText(Integer.toString(practica.getValoresCriticos()));
        valoresReservadosField.setText(Integer.toString(practica.getValoresReservados()));
        cantidadHorasResultado.setText(Integer.toString(practica.getCantidadHorasResultados()));
        habilitadoComboBox.setSelectedItem(practica.getNombrePractica());

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeView(){
        // Controllers
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
        // Nombre practica
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre:"), gbc);
        nombrePracticaField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nombrePracticaField, gbc);
        // Grupo
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Grupo:"), gbc);
        grupoField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(grupoField, gbc);
        // Valores Criticos
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Valores Criticos:"), gbc);
        valoresCriticosField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(valoresCriticosField, gbc);
        // Valores reservados
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Valores reservados:"), gbc);
        valoresReservadosField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(valoresReservadosField, gbc);
        //Cantidad horas Resultado
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Cantidad horas Resultado:"), gbc);
        cantidadHorasResultado = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(cantidadHorasResultado, gbc);
        // Habilitado
        gbc.gridx = 0;
        gbc.gridy = 6;
        habilitadoComboBox = new JComboBox<>();
        populatePracticaComboBox();
        gbc.gridx = 1;
        formPanel.add(habilitadoComboBox, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void populatePracticaComboBox() {

        habilitadoComboBox.addItem("Habilitado");
        habilitadoComboBox.addItem("Deshabilitado");

    }

    private void createPractica() {
        if (!validateNonEmptyFields(nombrePracticaField) || !validateNonEmptyFields(grupoField) || !validateNonEmptyFields(valoresCriticosField) || !validateNonEmptyFields(valoresReservadosField) || !validateNonEmptyFields(cantidadHorasResultado)){
            return;
        }
        String nombrePractica = nombrePracticaField.getText();
        int grupo = Integer.parseInt(grupoField.getText());
        int valoresCriticos = Integer.parseInt(valoresCriticosField.getText());
        int valoresReservados = Integer.parseInt(valoresReservadosField.getText());
        int cantidadHorasResultados =  Integer.parseInt(cantidadHorasResultado.getText());
        boolean habilitado = ((String) habilitadoComboBox.getSelectedItem() == "Habilitado" );
        practicaController.addPractica(nombrePractica,grupo, valoresCriticos,valoresReservados, cantidadHorasResultados, habilitado);
        JOptionPane.showMessageDialog(this, "Resultado creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.goBack();
    }

    private void updateResultado(int codigoUsuario) {
        if (!validateNonEmptyFields(nombrePracticaField) || !validateNonEmptyFields(grupoField) || !validateNonEmptyFields(valoresCriticosField) || !validateNonEmptyFields(valoresReservadosField) || !validateNonEmptyFields(cantidadHorasResultado)){
            return;
        }
        String nombrePractica = nombrePracticaField.getText();
        int grupo = Integer.parseInt(grupoField.getText());
        int valoresCriticos = Integer.parseInt(valoresCriticosField.getText());
        int valoresReservados = Integer.parseInt(valoresReservadosField.getText());
        int cantidadHorasResultados =  Integer.parseInt(cantidadHorasResultado.getText());
        boolean habilitado = ((String) habilitadoComboBox.getSelectedItem() == "Habilitado" );
        practicaController.updateResultado(codigoUsuario, nombrePractica, grupo, valoresCriticos, valoresReservados, cantidadHorasResultados, habilitado);
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


