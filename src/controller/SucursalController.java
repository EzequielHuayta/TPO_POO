package controller;

import model.sucursal.SucursalDTO;
import model.sucursal.SucursalModel;
import utils.ABMResult;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

public class SucursalController {
    private static SucursalController instance;
    private final SucursalModel model;
    private final ArrayList<RefreshableView> attachedViews;
    private SucursalController() {
        model = new SucursalModel();
        attachedViews = new ArrayList<>();
    }

    public static SucursalController getInstance() {
        if (instance == null) {
            instance = new SucursalController();
        }
        return instance;
    }

    public void attachView(RefreshableView view){
        attachedViews.add(view);
    }

    public void detachView(RefreshableView view){
        attachedViews.remove(view);
    }

    private void refreshViews(){
        for (RefreshableView view : attachedViews){
            view.onRefresh();
        }
    }

    public ABMResult addSucursal(SucursalDTO sucursalDTO) {
        ABMResult abmResult = model.addSucursal(sucursalDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult deleteSucursal(int numero) {
        ABMResult abmResult = model.deleteSucursal(numero);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult updateSucursal(SucursalDTO sucursalDTO) {
        ABMResult abmResult = model.updateSucursal(sucursalDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public SucursalDTO getSucursalByNumero(int numero){
        return model.read(numero);
    }

    public List<SucursalDTO> getAllSucursales(){
        return model.getAllSucursales();
    }
}
