package controller;

import model.usuario.Usuario;
import model.usuario.UsuarioModel;

public class UsuarioController {
    private static UsuarioController instance;
    private UsuarioModel model;

    private UsuarioController() {
        model = new UsuarioModel();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    public void addUsuario(Usuario usuario) {
        model.addUsuario(usuario);
    }

    public boolean verificarLogin(String email, String password) {
        //TODO MODIFICAR
        return true;
    }

    // Métodos para modificar, borrar y listar usuarios
}

