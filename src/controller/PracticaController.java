package controller;

import model.practica.PracticaDTO;
import model.practica.PracticaModel;
import model.usuario.UsuarioDTO;
import model.usuario.UsuarioModel;
import view.RefreshableView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PracticaController {

    private static PracticaController instance;
    private final PracticaModel model;
    private final ArrayList<RefreshableView> attachedViews;
    private PracticaDTO currentUser;
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

    public PracticaDTO getPracticaByID(int id){
        return model.read(id);
    }


    public List<PracticaDTO> getAllPracticas() {
        return model.getAllPracticas();

    }
}
