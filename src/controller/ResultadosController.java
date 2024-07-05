package controller;


import model.practica.PracticaDTO;
import model.resultadopeticion.ResultadoPeticionDTO;
import model.resultadopeticion.ResultadoPeticionModel;
import model.usuario.Rol;
import model.usuario.UsuarioDTO;
import view.RefreshableView;
import view.resultados.ResultadosListView;

import java.util.ArrayList;
import java.util.Date;
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

    public ResultadoPeticionDTO getResultadoByID(int id) {
        List<ResultadoPeticionDTO> resultados = model.readAll();

        for (ResultadoPeticionDTO resultado : resultados) {
            if (resultado.getId() == id) {
                return resultado;
            }
        }
        return null;
    }

    public void addResultado(PracticaDTO practicas, int valor) {
        model.create(new ResultadoPeticionDTO(model.getLatestId(), practicas, valor));

        refreshViews();
    }

    public void updateResultado(int id, PracticaDTO practica, int valor) {
        model.update(new ResultadoPeticionDTO(id, practica, valor));
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
