package controller;

import model.practica.PracticaDTO;
import model.practica.PracticaModel;
import model.resultadopeticion.ResultadoPeticionDTO;
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

    public PracticaDTO getCodigoPractica(int codigoPractica) {
        List<PracticaDTO> resultados = model.readAll();

        for (PracticaDTO resultado : resultados) {
            if (resultado.getCodigoPractica() == codigoPractica) {
                return resultado;
            }
        }
        return null;
    }

    public void deleteUsuario(int codigoPractica) {
        //TODO
    }


    public void addPractica(String nombrePractica, int grupo, int valoresCriticos, int valoresReservados, int cantidadHorasResultados, boolean habilitado) {
        model.create(new PracticaDTO(model.getLatestCodigoPractica(), nombrePractica, grupo, valoresCriticos, valoresReservados, cantidadHorasResultados, habilitado));
        refreshViews();
    }

    public void updateResultado(int codigoUsuario, String nombrePractica, int grupo, int valoresCriticos, int valoresReservados, int cantidadHorasResultados, boolean habilitado) {

    }
}
