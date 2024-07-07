package model.usuario;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;
import utils.ABMResult;

import java.lang.reflect.Type;
import java.util.List;

public class UsuarioModel extends GenericDAO<UsuarioDTO> {
    private static final String FILE_PATH = "src/persist/usuarios.json";
    private static final Type LIST_TYPE = new TypeToken<List<UsuarioDTO>>() {}.getType();
    private int loggedUserID = -1;

    public UsuarioModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(UsuarioDTO usuario) {
        return usuario.getId();
    }

    public int getLatestId() {
        List<UsuarioDTO> usuarios = readAll();
        if (!usuarios.isEmpty()) {
            return usuarios.get(usuarios.size() - 1).getId() + 1;
        }
        return 0;
    }

    public UsuarioDTO authenticateUsuario(String email, String password) {
        List<UsuarioDTO> usuarios = readAll();
        for (UsuarioDTO usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    private boolean verifyInUseEmail(String email, int id) {
        List<UsuarioDTO> usuarios = readAll();
        for (UsuarioDTO usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.getId() != id) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyInUseDNI(int dni, int id) {
        List<UsuarioDTO> usuarios = readAll();
        for (UsuarioDTO usuario : usuarios) {
            if (usuario.getDni() == dni && usuario.getId() != id) {
                return true;
            }
        }
        return false;
    }

    public ABMResult addUsuario(UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(getLatestId());
        if (verifyInUseEmail(usuarioDTO.getEmail(), -1)) {
            return new ABMResult(false, "Ya existe un usuario registrado con ese email");
        } else if (verifyInUseDNI(usuarioDTO.getDni(), -1)) {
            return new ABMResult(false, "Ya existe un usuario registrado con ese DNI");
        }
        create(usuarioDTO);
        return new ABMResult(true, "Usuario creado con éxito");
    }

    public ABMResult updateUsuario(UsuarioDTO usuarioDTO) {
        if (verifyInUseEmail(usuarioDTO.getEmail(), usuarioDTO.getId())) {
            return new ABMResult(false, "Ya existe un usuario registrado con ese email");
        } else if (verifyInUseDNI(usuarioDTO.getDni(), usuarioDTO.getId())) {
            return new ABMResult(false, "Ya existe un usuario registrado con ese DNI");
        }
        update(usuarioDTO);
        return new ABMResult(true, "Usuario actualizado con éxito");
    }

    public ABMResult deleteUsuario(int id) {
        if (loggedUserID == id) {
            return new ABMResult(false, "El usuario seleccionado tiene la sesión abierta y no puede eliminarse");
        }
        delete(id);
        return new ABMResult(true, "Usuario eliminado con éxito");
    }

    public UsuarioDTO getLoggedUser(){
        if(loggedUserID == -1){
            return null;
        }
        return read(loggedUserID);
    }

    public void setLoggedUserID(int id){
        loggedUserID = id;
    }

    public List<UsuarioDTO> getAllUsuarios() {
        return readAll();
    }
}
