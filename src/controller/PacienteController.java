package controller;

import model.paciente.PacienteDTO;
import model.paciente.PacienteModel;
import model.usuario.Rol;
import model.usuario.UsuarioDTO;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteController {
    private static PacienteController instance;
    private final PacienteModel model;
    private final ArrayList<RefreshableView> attachedViews;

    private PacienteController() {
        model = new PacienteModel();
        attachedViews = new ArrayList<>();
    }

    public static PacienteController getInstance() {
        if (instance == null) {
            instance = new PacienteController();
        }
        return instance;
    }

    public void attachView(RefreshableView view){
        attachedViews.add(view);
    }

    public void detachView(RefreshableView view){
        attachedViews.remove(view);
    }

    private void refreshViews(){
        for (RefreshableView view : attachedViews){
            view.onRefresh();
        }
    }

//    public void addPaciente() {
//        model.create(new UsuarioDTO(model.getLatestId(), email, password, nombre, domicilio, dni, fechaNacimiento, rol));
//        refreshViews();
//    }
//
//    public void deletePaciente(int id) {
//        model.delete(id);
//        refreshViews();
//    }
//
//    public void updatePaciente(int id, String email, String password, String nombre, String domicilio, int dni, Date fechaNacimiento, Rol rol) {
//        model.update(new UsuarioDTO(id, email, password, nombre, domicilio, dni, fechaNacimiento, rol));
//        refreshViews();
//    }
//
//    public UsuarioDTO getPacienteByID(int id){
//        return model.read(id);
//    }
//
//    public List<PacienteDTO> getAllPacientes(){
//        return model.getAllUsuarios();
//    }
}
