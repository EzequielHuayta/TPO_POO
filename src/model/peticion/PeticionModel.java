package model.peticion;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;

import java.lang.reflect.Type;
import java.util.List;

public class PeticionModel extends GenericDAO<PeticionDTO> {

    private static final String FILE_PATH = "src/persist/peticion.json";
    private static final Type LIST_TYPE = new TypeToken<List<PeticionDTO>>() {}.getType();

    public PeticionModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(PeticionDTO entity) {
        return entity.getId();
    }


}
