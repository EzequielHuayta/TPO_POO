package controller;

import model.usuario.Usuario;
import model.usuario.UsuarioModel;

import java.util.List;

public class UsuarioController {
    private static UsuarioController instance;
    private final UsuarioModel model;

    private UsuarioController() {
        model = new UsuarioModel();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    public void addUsuario() {
        model.addUsuario();
    }

    public boolean authenticateUsuario(String email, String password) {
        return model.authenticateUsuario(email, password);
    }

    public List<Usuario> getAllUsuarios(){
        return model.getAllUsuarios();
    }
}

