package controller;

import model.peticion.PeticionDTO;
import model.peticion.PeticionModel;
import model.practica.PracticaDTO;
import model.resultado.ResultadoDTO;
import utils.ABMResult;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

public class PeticionController {
    private static PeticionController instance;
    private final PeticionModel model;
    private final ArrayList<RefreshableView> attachedViews;
    private PeticionController() {
        model = new PeticionModel();
        attachedViews = new ArrayList<>();
    }

    public static PeticionController getInstance() {
        if (instance == null) {
            instance = new PeticionController();
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

    public ABMResult addPeticion(PeticionDTO peticionDTO) {
        ABMResult abmResult = model.addPeticion(peticionDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult deletePeticion(int id) {
        ABMResult abmResult = model.deletePeticion(id);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult updatePeticion(PeticionDTO peticionDTO) {
        ABMResult abmResult = model.updatePeticion(peticionDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public PeticionDTO getPeticionByID(int id){
        return model.read(id);
    }

    public List<PeticionDTO> getAllPeticiones(){
        return model.getAllPeticiones();
    }

    public PeticionDTO[] getAllPeticionesArray(){
        List<PeticionDTO> peticionesList = model.getAllPeticiones();
        PeticionDTO[] peticionesArray = new PeticionDTO[peticionesList.size()];
        peticionesArray = peticionesList.toArray(peticionesArray);
        return peticionesArray;
    }

    public List<PeticionDTO> getAllPeticionesCriticas() {
        List<PeticionDTO> peticionesList = model.getAllPeticiones();
        List<PeticionDTO> peticionesCriticasList = new ArrayList<>();

        for (PeticionDTO peticion : peticionesList) {
            for (ResultadoDTO resultado : peticion.getListResultados()) {
                if(resultado.esCritico()) {
                    peticionesCriticasList.add(peticion);
                }
            }
        }
        return peticionesCriticasList;
    }
}
