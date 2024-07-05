package controller;

import model.paciente.PacienteDTO;
import model.paciente.PacienteModel;
import utils.ABMResult;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

public class PacienteController {
    private static PacienteController instance;
    private final PacienteModel model;
    private final ArrayList<RefreshableView> attachedViews;
    private PacienteController() {
        model = new PacienteModel();
        attachedViews = new ArrayList<>();
    }

    public static PacienteController getInstance() {
        if (instance == null) {
            instance = new PacienteController();
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

    public ABMResult addPaciente(PacienteDTO pacienteDTO) {
        ABMResult abmResult = model.addPaciente(pacienteDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult deletePaciente(int id) {
        ABMResult abmResult = model.deletePaciente(id);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult updatePaciente(PacienteDTO pacienteDTO) {
        ABMResult abmResult = model.updatePaciente(pacienteDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public PacienteDTO getPacienteByID(int id){
        return model.read(id);
    }

    public List<PacienteDTO> getAllPacientes(){
        return model.getAllPacientes();
    }

    public PacienteDTO[] getAllPacientesAsArray(){
        List<PacienteDTO> pacientesList = model.getAllPacientes();
        PacienteDTO[] pacientesArray = new PacienteDTO[pacientesList.size()];
        pacientesArray = pacientesList.toArray(pacientesArray);
        return pacientesArray;
    }
}
