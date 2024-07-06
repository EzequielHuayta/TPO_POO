package view.peticion;

import controller.PeticionController;
import controller.UsuarioController;
import model.peticion.PeticionDTO;
import model.practica.PracticaDTO;
import model.usuario.Rol;
import model.usuario.UsuarioDTO;
import utils.ButtonEditor;
import utils.ButtonListener;
import utils.ButtonRenderer;
import view.MainFrame;
import view.RefreshableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PeticionListView extends JPanel implements RefreshableView {

    private final MainFrame mainFrame = MainFrame.getInstance();
    private final PeticionController peticionController = PeticionController.getInstance();
    private final UsuarioController usuarioController = UsuarioController.getInstance();
    private DefaultTableModel tableModel;
    private final UsuarioDTO loggedUser;
    private final boolean isCritico;

    public PeticionListView(boolean isCritico) {
        this.isCritico = isCritico;

        // Controller
        peticionController.attachView(this);

        // UI
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            peticionController.detachView(this);
            mainFrame.goBack();
        });
        JLabel titleLabel = new JLabel(isCritico ? "Peticiones con resultados críticos" : "Peticiones");
        titleLabel.setForeground(isCritico ? Color.RED : Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton createButton = new JButton("Crear petición");
        createButton.addActionListener(e -> {
            mainFrame.addPanel(new PeticionFormView(), "peticionform");
            mainFrame.showPanel("peticionform");
        });
        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(titleLabel);
        toolBar.add(Box.createHorizontalGlue());

        if (!isCritico) {
            JButton showCriticButton = new JButton("Listar peticiones criticas");
            showCriticButton.addActionListener(e -> {
                mainFrame.addPanel(new PeticionListView(true), "peticioncriticallist");
                mainFrame.showPanel("peticioncriticallist");
            });

            toolBar.add(showCriticButton);
        }

        toolBar.add(createButton);
        add(toolBar, BorderLayout.NORTH);

        loggedUser = usuarioController.getLoggedUser();
        if (loggedUser.getRol() == Rol.ADMINISTRADOR) {
            createAdminTable(peticionController, isCritico);
        } else {
            createTable(peticionController, isCritico);
        }
    }

    private void createAdminTable(PeticionController peticionController, Boolean isCritico) {
        java.util.List<PeticionDTO> peticiones;
        String[] columnNames = {"ID", "Obra Social", "Fecha de carga", "Fecha entrega estimada", "Paciente", "Practicas", "Finalizada", "Acciones"};
        if (!isCritico) {
            peticiones = peticionController.getAllPeticiones();
        } else {
            peticiones = peticionController.getAllPeticionesCriticas();
        }
        Object[][] data = new Object[peticiones.size()][8];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < peticiones.size(); i++) {
            PeticionDTO peticion = peticiones.get(i);
            data[i][0] = peticion.getId();
            data[i][1] = peticion.getObraSocial();
            data[i][2] = dateFormat.format(peticion.getFechaCarga());
            data[i][3] = dateFormat.format(peticion.getFechaCalculadaEntrega());
            data[i][4] = peticion.getPaciente().getNombre();
            data[i][5] = practicasToString(peticion.getListPracticas());
            data[i][6] = finalizadaToString(peticion.isFinalizada());
            data[i][7] = "Acciones";
        }

        tableModel = new DefaultTableModel(data, columnNames);
        // Custom JTable implementation
        JTable userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }

        };

        userTable.getColumn("Acciones").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, new ButtonListener() {
            @Override
            public void onEditButtonClicked(int id) {
                mainFrame.addPanel(new PeticionFormView(peticionController.getPeticionByID(id)), "peticionform");
                mainFrame.showPanel("peticionform");
            }

            @Override
            public void onDeleteButtonClicked(int id) {
                int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de borrar esta petición?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    peticionController.deletePeticion(id);
                }
            }
        }));

        // Adjusting column sizes
        TableColumnModel columnModel = userTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createTable(PeticionController peticionController, Boolean isCritico) {
        java.util.List<PeticionDTO> peticiones;
        String[] columnNames = {"ID", "Obra Social", "Fecha de carga", "Fecha entrega estimada", "Paciente", "Practicas", "Finalizada"};
        if (!isCritico) {
            peticiones = peticionController.getAllPeticiones();
        } else {
            peticiones = peticionController.getAllPeticionesCriticas();
        }
        Object[][] data = new Object[peticiones.size()][7];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < peticiones.size(); i++) {
            PeticionDTO peticion = peticiones.get(i);
            data[i][0] = peticion.getId();
            data[i][1] = peticion.getObraSocial();
            data[i][2] = dateFormat.format(peticion.getFechaCarga());
            data[i][3] = dateFormat.format(peticion.getFechaCalculadaEntrega());
            data[i][4] = peticion.getPaciente().getNombre();
            data[i][5] = practicasToString(peticion.getListPracticas());
            data[i][6] = finalizadaToString(peticion.isFinalizada());
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel = new DefaultTableModel(data, columnNames);
        JTable userTable = new JTable(tableModel) {
            // Nothing to do here
        };

        // Adjusting column sizes
        TableColumnModel columnModel = userTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onRefresh() {
        tableModel.setRowCount(0);

        List<PeticionDTO> peticones;
        if (isCritico) {
            peticones = peticionController.getAllPeticionesCriticas();
        } else {
            peticones = peticionController.getAllPeticiones();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        if (loggedUser.getRol() == Rol.ADMINISTRADOR) {
            for (PeticionDTO peticion : peticones) {
                Object[] row = {
                        peticion.getId(),
                        peticion.getObraSocial(),
                        dateFormat.format(peticion.getFechaCarga()),
                        dateFormat.format(peticion.getFechaCalculadaEntrega()),
                        peticion.getPaciente().getNombre(),
                        practicasToString(peticion.getListPracticas()),
                        finalizadaToString(peticion.isFinalizada()),
                        "Acciones"
                };
                tableModel.addRow(row);
            }
        } else {
            for (PeticionDTO peticion : peticones) {
                Object[] row = {
                        peticion.getId(),
                        peticion.getObraSocial(),
                        dateFormat.format(peticion.getFechaCarga()),
                        dateFormat.format(peticion.getFechaCalculadaEntrega()),
                        peticion.getPaciente().getNombre(),
                        practicasToString(peticion.getListPracticas()),
                        finalizadaToString(peticion.isFinalizada()),
                };
                tableModel.addRow(row);
            }
        }
        tableModel.fireTableDataChanged();
    }

    private String practicasToString(List<PracticaDTO> practicasList) {
        if (practicasList.isEmpty()) {
            return "Sin prácticas";
        } else {
            StringBuilder practicas = new StringBuilder();
            for (int i = 0; i < practicasList.size(); i++) {
                if (i > 0) {
                    practicas.append(", ");
                }
                practicas.append(practicasList.get(i).getNombre());
            }
            return practicas.toString();
        }
    }

    private String finalizadaToString(boolean valorFinalizada) {
        if (valorFinalizada) {
            return "Si";
        } else return "No";
    }
}

