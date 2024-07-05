package model.sucursal;

import com.google.gson.reflect.TypeToken;
import persist.GenericDAO;
import utils.ABMResult;

import java.lang.reflect.Type;
import java.util.List;

public class SucursalModel extends GenericDAO<SucursalDTO> {
    private static final String FILE_PATH = "src/persist/sucursales.json";
    private static final Type LIST_TYPE = new TypeToken<List<SucursalDTO>>() {}.getType();

    public SucursalModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(SucursalDTO usuario) {
        return usuario.getNumero();
    }

    public int getLatestNumber() {
        List<SucursalDTO> sucursales = readAll();
        if(!sucursales.isEmpty()){
            return sucursales.get(sucursales.size()-1).getNumero() + 1;
        }
        return 0;
    }

    private boolean verifyInUseDireccion(String email, int numero) {
        List<SucursalDTO> sucursales = readAll();
        for (SucursalDTO sucursal : sucursales) {
            if (sucursal.getDireccion().equals(email) && sucursal.getNumero() != numero) {
                return true;
            }
        }
        return false;
    }

    public ABMResult addSucursal(SucursalDTO sucursal){
        sucursal.setNumero(getLatestNumber());
        if(verifyInUseDireccion(sucursal.getDireccion(), -1)){
            return new ABMResult(false, "Ya existe una sucursal en esa dirección");
        }
        create(sucursal);
        return new ABMResult(true, "Sucursal creada con éxito");
    }

    public ABMResult updateSucursal(SucursalDTO sucursal){
        if(verifyInUseDireccion(sucursal.getDireccion(), sucursal.getNumero())){
            return new ABMResult(false, "Ya existe una sucursal en esa dirección");
        }
        update(sucursal);
        return new ABMResult(true, "Sucursal actualizada con éxito");
    }

    public ABMResult deleteSucursal(int numero){
        delete(numero);
        return new ABMResult(true, "Sucursal eliminada con éxito");
    }

    public List<SucursalDTO> getAllSucursales() {
        return readAll();
    }
}
