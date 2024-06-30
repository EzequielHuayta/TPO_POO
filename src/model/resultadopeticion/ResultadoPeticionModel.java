package model.resultadopeticion;

import com.google.gson.reflect.TypeToken;
import model.usuario.UsuarioDTO;
import persist.GenericDAO;

import java.lang.reflect.Type;
import java.util.List;

public class ResultadoPeticionModel extends GenericDAO<ResultadoPeticionDTO> {

    private static final String FILE_PATH = "src/persist/resultadoPeticion.json";
    private static final Type LIST_TYPE = new TypeToken<List<ResultadoPeticionDTO>>() {
    }.getType();

    public ResultadoPeticionModel() {

        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(ResultadoPeticionDTO entity) {
        return 0;
    }

    public List<ResultadoPeticionDTO> getAllResultados() {
        return readAll();
    }
}
