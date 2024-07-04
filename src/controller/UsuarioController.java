package controller;

import model.usuario.Rol;
import model.usuario.UsuarioDTO;
import model.usuario.UsuarioModel;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioController {
    private static UsuarioController instance;
    private final UsuarioModel model;
    private final ArrayList<RefreshableView> attachedViews;
    private UsuarioDTO currentUser;
    private UsuarioController() {
        model = new UsuarioModel();
        attachedViews = new ArrayList<>();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
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

    public void addUsuario(String email, String password, String nombre, String domicilio, int dni, Date fechaNacimiento, Rol rol) {
        model.create(new UsuarioDTO(model.getLatestId(), email, password, nombre, domicilio, dni, fechaNacimiento, rol));
        refreshViews();
    }

    public void deleteUsuario(int id) {
        model.delete(id);
        refreshViews();
    }

    public void updateUsuario(int id, String email, String password, String nombre, String domicilio, int dni, Date fechaNacimiento, Rol rol) {
        model.update(new UsuarioDTO(id, email, password, nombre, domicilio, dni, fechaNacimiento, rol));
        refreshViews();
    }

    public UsuarioDTO getUsuarioByID(int id){
        return model.read(id);
    }

    public boolean authenticateUsuario(String email, String password) {
        UsuarioDTO usuario = model.authenticateUsuario(email, password);
        if (usuario != null) {
            currentUser = usuario;
            return true;
        }
        return false;
    }

    public UsuarioDTO getCurrentUser() {
        return currentUser;
    }

    public List<UsuarioDTO> getAllUsuarios(){
        return model.getAllUsuarios();
    }
}
