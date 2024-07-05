package controller;


import model.practica.PracticaDTO;
import model.resultado.ResultadoDTO;
import model.resultado.ResultadoModel;
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

    public void detachView(ResultadosListView resultadosListView) {
        attachedViews.remove(resultadosListView);

    }

    public List<ResultadoDTO> getAllResultados() {
        return model.getAllResultados();
    }

    public ResultadoDTO getResultadoByID(int id) {
        List<ResultadoDTO> resultados = model.readAll();

        for (ResultadoDTO resultado : resultados) {
            if (resultado.getId() == id) {
                return resultado;
            }
        }
        return null;
    }

    public void addResultado(PracticaDTO practicas, int valor) {
        model.create(new ResultadoDTO(model.getLatestId(), practicas, valor));

        refreshViews();
    }

    public void updateResultado(int id, PracticaDTO practica, int valor) {
        model.update(new ResultadoDTO(id, practica, valor));
        refreshViews();
    }

    private void refreshViews(){
        for (RefreshableView view : attachedViews){
            view.onRefresh();
        }
    }

    public void deleteResultado(int id) {
        model.delete(id);
        refreshViews();
    }
}
