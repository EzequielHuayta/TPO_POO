package controller;

import model.practica.PracticaDTO;
import model.practica.PracticaModel;
import utils.ABMResult;
import view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

public class PracticaController {
    private static PracticaController instance;
    private final PracticaModel model;
    private final ArrayList<RefreshableView> attachedViews;

    private PracticaController() {
        model = new PracticaModel();
        attachedViews = new ArrayList<>();
    }

    public static PracticaController getInstance() {
        if (instance == null) {
            instance = new PracticaController();
        }
        return instance;
    }

    public void attachView(RefreshableView view) {
        attachedViews.add(view);
    }

    public void detachView(RefreshableView view) {
        attachedViews.remove(view);
    }

    private void refreshViews() {
        for (RefreshableView view : attachedViews) {
            view.onRefresh();
        }
    }

    public ABMResult addPractica(PracticaDTO practicaDTO) {
        ABMResult abmResult = model.addPractica(practicaDTO);
        if (abmResult.getResult()) {
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult deletePractica(int codigo) {
        ABMResult abmResult = model.deletePractica(codigo);
        if (abmResult.getResult()) {
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult updatePractica(PracticaDTO practicaDTO) {
        ABMResult abmResult = model.updatePractica(practicaDTO);
        if (abmResult.getResult()) {
            refreshViews();
        }
        return abmResult;
    }

    public PracticaDTO getPracticaByCodigo(int codigo) {
        return model.read(codigo);
    }

    public List<PracticaDTO> getAllPracticas() {
        return model.getAllPracticas();
    }

    public PracticaDTO[] getAllPracticasAsArray() {
        List<PracticaDTO> practicasList = model.getAllPracticas();
        PracticaDTO[] practicasArray = new PracticaDTO[practicasList.size()];
        practicasArray = practicasList.toArray(practicasArray);
        return practicasArray;
    }
}
