package model.practica;

import com.google.gson.reflect.TypeToken;
import model.sucursal.SucursalDTO;
import persist.GenericDAO;
import utils.ABMResult;

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
        return practica.getCodigo();
    }

    public int getLatestCodigo() {
        List<PracticaDTO> practicas = readAll();
        if(!practicas.isEmpty()){
            return practicas.get(practicas.size()-1).getCodigo() + 1;
        }
        return 0;
    }

    private boolean verifyInUseNombre(String nombre, int codigo) {
        List<PracticaDTO> practicas = readAll();
        for (PracticaDTO practica : practicas) {
            if (practica.getNombre().equals(nombre) && practica.getCodigo() != codigo) {
                return true;
            }
        }
        return false;
    }

    public ABMResult addPractica(PracticaDTO practica){
        practica.setCodigo(getLatestCodigo());
        if(verifyInUseNombre(practica.getNombre(), -1)){
            return new ABMResult(false, "Ya existe una práctica con ese nombre");
        }
        create(practica);
        return new ABMResult(true, "Práctica creada con éxito");
    }

    public ABMResult updatePractica(PracticaDTO practica){
        if(verifyInUseNombre(practica.getNombre(), practica.getCodigo())){
            return new ABMResult(false, "Ya existe una práctica con ese nombre");
        }
        update(practica);
        return new ABMResult(true, "Práctica actualizada con éxito");
    }

    public ABMResult deletePractica(int numero){
        delete(numero);
        return new ABMResult(true, "Práctica eliminada con éxito");
    }

    public List<PracticaDTO> getAllPracticas() {
        return readAll();
    }
}
