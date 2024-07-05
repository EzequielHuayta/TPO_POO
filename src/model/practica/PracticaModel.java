package model.practica;

import com.google.gson.reflect.TypeToken;
import model.usuario.UsuarioDTO;
import persist.GenericDAO;

import java.lang.reflect.Type;
import java.util.List;

public class PracticaModel extends GenericDAO<PracticaDTO> {

    private static final String FILE_PATH = "src/persist/practicas.json";
    private static final Type LIST_TYPE = new TypeToken<List<PracticaDTO>>() {}.getType();

    public PracticaModel() {
        super(FILE_PATH, LIST_TYPE);
    }


    @Override
    protected int getId(PracticaDTO practica) {
        return practica.getCodigoPractica();
    }

    public List<PracticaDTO> getAllPracticas() {
        return readAll();
    }

    public int getLatestCodigoPractica() {
        List<PracticaDTO> practicas = readAll();
        if(!practicas.isEmpty()){
            return practicas.get(practicas.size()-1).getCodigoPractica() + 1;
        }
        return 0;
    }
}
