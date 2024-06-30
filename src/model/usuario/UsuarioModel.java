package model.usuario;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;

import java.lang.reflect.Type;
import java.util.List;

public class UsuarioModel extends GenericDAO<Usuario> {
    private static final String FILE_PATH = "src/persist/usuarios.json";
    private static final Type LIST_TYPE = new TypeToken<List<Usuario>>() {}.getType();

    public UsuarioModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(Usuario usuario) {
        return usuario.getId();
    }

    private Usuario findByEmail(String email) {
        List<Usuario> usuarios = readAll();
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean authenticateUsuario(String email, String password) {
        Usuario usuario = findByEmail(email);
        return usuario != null && usuario.getPassword().equals(password);
    }

    public void addUsuario() {
        Usuario usuario = new Usuario("admin@uade.edu.com", "password", Usuario.Rol.ADMINISTRADOR);
        create(usuario);
    }

    public List<Usuario> getAllUsuarios() {
        return readAll();
    }
}
