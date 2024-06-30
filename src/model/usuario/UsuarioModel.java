package model.usuario;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;

import java.lang.reflect.Type;
import java.util.List;

public class UsuarioModel extends GenericDAO<UsuarioDTO> {
    private static final String FILE_PATH = "src/persist/usuarios.json";
    private static final Type LIST_TYPE = new TypeToken<List<UsuarioDTO>>() {}.getType();

    public UsuarioModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(UsuarioDTO usuario) {
        return usuario.getId();
    }

    public int getLatestId() {
        List<UsuarioDTO> usuarios = readAll();
        if(!usuarios.isEmpty()){
            return usuarios.get(usuarios.size()-1).getId() + 1;
        }
        return 0;
    }

    private UsuarioDTO findByEmail(String email) {
        List<UsuarioDTO> usuarios = readAll();
        for (UsuarioDTO usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
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

    public void addUsuario(UsuarioDTO usuario) {
        create(usuario);
    }

    public List<UsuarioDTO> getAllUsuarios() {
        return readAll();
    }
}
