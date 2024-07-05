package model.resultado;

import com.google.gson.reflect.TypeToken;
import model.practica.PracticaDTO;
import persist.GenericDAO;
import utils.ABMResult;

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

//    public ABMResult addPractica(PracticaDTO practica){
//        practica.setCodigo(getLatestCodigo());
//        if(verifyInUseNombre(practica.getNombre(), -1)){
//            return new ABMResult(false, "Ya existe una práctica con ese nombre");
//        }
//        create(practica);
//        return new ABMResult(true, "Práctica creada con éxito");
//    }
//
//    public ABMResult updatePractica(PracticaDTO practica){
//        if(verifyInUseNombre(practica.getNombre(), practica.getCodigo())){
//            return new ABMResult(false, "Ya existe una práctica con ese nombre");
//        }
//        update(practica);
//        return new ABMResult(true, "Práctica actualizada con éxito");
//    }
//
//    public ABMResult deletePractica(int numero){
//        delete(numero);
//        return new ABMResult(true, "Práctica eliminada con éxito");
//    }
//
//    public List<PracticaDTO> getAllPracticas() {
//        return readAll();
//    }

}
