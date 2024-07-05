package controller;

import model.resultado.ResultadoDTO;
import model.resultado.ResultadoModel;
import utils.ABMResult;
import view.RefreshableView;
import view.resultados.ResultadosListView;

import java.util.ArrayList;
import java.util.List;

public class ResultadosController {
    private static ResultadosController instance;

    private static ResultadoModel model;

    private ArrayList<RefreshableView> attachedViews;


    private ResultadosController() {
        model = new ResultadoModel();
        attachedViews = new ArrayList<>();
    }

    public static ResultadosController getInstance() {
        if (instance == null) {
            instance = new ResultadosController();
        }
        return instance;
    }

    public void attachView(ResultadosListView resultadosListView) {
        attachedViews.add(resultadosListView);
    }

    public void detachView(RefreshableView view){
        attachedViews.remove(view);
    }

    private void refreshViews(){
        for (RefreshableView view : attachedViews){
            view.onRefresh();
        }
    }

    public ABMResult addResultado(ResultadoDTO resultadoDTO) {
        ABMResult abmResult = model.addResultado(resultadoDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult deleteResultado(int id) {
        ABMResult abmResult = model.deleteResultado(id);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ABMResult updateResultado(ResultadoDTO resultadoDTO) {
        ABMResult abmResult = model.updateResultado(resultadoDTO);
        if(abmResult.getResult()){
            refreshViews();
        }
        return abmResult;
    }

    public ResultadoDTO getResultadoByID(int id){
        return model.read(id);
    }

    public List<ResultadoDTO> getAllResultados(){
        return model.getAllResultados();
    }

    public ResultadoDTO[] getAllPacientesAsArray(){
        List<ResultadoDTO> resultadosList = model.getAllResultados();
        ResultadoDTO[] resultadosArray = new ResultadoDTO[resultadosList.size()];
        resultadosArray = resultadosList.toArray(resultadosArray);
        return resultadosArray;
    }

    public int getLastCreatedID(){
        return model.getLatestId()-1;
    }
}
