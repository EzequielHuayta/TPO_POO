package model.resultado;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;

import java.lang.reflect.Type;
import java.util.List;

public class ResultadoModel extends GenericDAO<ResultadoDTO> {

    private static final String FILE_PATH = "src/persist/resultados.json";
    private static final Type LIST_TYPE = new TypeToken<List<ResultadoDTO>>() {
    }.getType();

    public ResultadoModel() {

        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(ResultadoDTO entity) {
        return 0;
    }

    public List<ResultadoDTO> getAllResultados() {
        return readAll();
    }

    public int getLatestId() {
        List<ResultadoDTO> resultados = readAll();
        if(!resultados.isEmpty()){
            return resultados.get(resultados.size()-1).getId() + 1;
        }
        return 0;
    }

}
