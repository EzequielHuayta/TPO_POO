package model.resultado;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;
import utils.ABMResult;

import java.lang.reflect.Type;
import java.util.List;

public class ResultadoModel extends GenericDAO<ResultadoDTO> {

    private static final String FILE_PATH = "src/persist/resultados.json";
    private static final Type LIST_TYPE = new TypeToken<List<ResultadoDTO>>() {}.getType();

    public ResultadoModel() {

        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(ResultadoDTO entity) {
        return entity.getResultadoID();
    }

    public int getLatestId() {
        List<ResultadoDTO> resultados = readAll();
        if (!resultados.isEmpty()) {
            return resultados.get(resultados.size() - 1).getResultadoID() + 1;
        }
        return 0;
    }

    public ABMResult addResultado(ResultadoDTO resultado) {
        resultado.setResultadoID(getLatestId());
        create(resultado);
        return new ABMResult(true, "Resultado creado con éxito");
    }

    public ABMResult updateResultado(ResultadoDTO resultado) {
        update(resultado);
        return new ABMResult(true, "Resultado actualizado con éxito");
    }

    public ABMResult deleteResultado(int numero) {
        delete(numero);
        return new ABMResult(true, "Resultado eliminado con éxito");
    }

    public List<ResultadoDTO> getAllResultados() {
        return readAll();
    }
}
