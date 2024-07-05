package controller;

import model.usuario.UsuarioDTO;
import model.usuario.UsuarioModel;
import utils.ABMResult;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

public class UsuarioController {
    private static UsuarioController instance;
    private final UsuarioModel model;
    private final ArrayList<RefreshableView> attachedViews;
    private UsuarioDTO loggedUser;
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

    public ABMResult addUsuario(UsuarioDTO usuarioDTO) {
        ABMResult abmResult = model.addUsuario(usuarioDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult deleteUsuario(int id) {
        ABMResult abmResult = model.deleteUsuario(id);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult updateUsuario(UsuarioDTO usuarioDTO) {
        ABMResult abmResult = model.updateUsuario(usuarioDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public UsuarioDTO getUsuarioByID(int id){
        return model.read(id);
    }

    public List<UsuarioDTO> getAllUsuarios(){
        return model.getAllUsuarios();
    }

    public UsuarioDTO[] getAllUsuariosAsArray(){
        List<UsuarioDTO> usuariosList = model.getAllUsuarios();
        UsuarioDTO[] usuariosArray = new UsuarioDTO[usuariosList.size()];
        usuariosArray = usuariosList.toArray(usuariosArray);
        return usuariosArray;
    }

    public boolean authenticateUsuario(String email, String password) {
        UsuarioDTO usuario = model.authenticateUsuario(email, password);
        if (usuario != null) {
            loggedUser = usuario;
            return true;
        }
        return false;
    }

    public UsuarioDTO getLoggedUser() {
        return loggedUser;
    }

    public void logoutUser() {
        loggedUser = null;
    }
}
