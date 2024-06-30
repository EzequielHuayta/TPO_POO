package controller;


import model.resultadopeticion.ResultadoPeticionDTO;
import model.resultadopeticion.ResultadoPeticionModel;
import view.RefreshableView;
import view.resultados.ResultadosListView;

import java.util.ArrayList;
import java.util.List;

public class ResultadosController {
    private static ResultadosController instance;

    private static ResultadoPeticionModel model;

    private ArrayList<RefreshableView> attachedViews;


    private ResultadosController() {
        model = new ResultadoPeticionModel();
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

    public List<ResultadoPeticionDTO> getAllResultados() {
        return model.getAllResultados();
    }
}
