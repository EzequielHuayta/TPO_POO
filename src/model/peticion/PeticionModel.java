package model.peticion;

import com.google.gson.reflect.TypeToken;
import model.practica.PracticaDTO;
import persist.GenericDAO;
import utils.ABMResult;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PeticionModel extends GenericDAO<PeticionDTO> {

    private static final String FILE_PATH = "src/persist/peticiones.json";
    private static final Type LIST_TYPE = new TypeToken<List<PeticionDTO>>() {}.getType();

    public PeticionModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(PeticionDTO entity) {
        return entity.getId();
    }

    public int getLatestId() {
        List<PeticionDTO> peticiones = readAll();
        if (!peticiones.isEmpty()) {
            return peticiones.get(peticiones.size() - 1).getId() + 1;
        }
        return 0;
    }

    public ABMResult addPeticion(PeticionDTO peticionDTO) {
        peticionDTO.setId(getLatestId());
        peticionDTO.setFechaCarga(getCurrentDate());
        peticionDTO.setFechaCalculadaEntrega(calculateExpectedDate(peticionDTO.getFechaCarga(), peticionDTO.getListPracticas()));
        create(peticionDTO);
        return new ABMResult(true, "Petición creada con éxito");
    }

    public ABMResult updatePeticion(PeticionDTO peticionDTO) {
        peticionDTO.setFechaCarga(getCurrentDate());
        peticionDTO.setFechaCalculadaEntrega(calculateExpectedDate(peticionDTO.getFechaCarga(), peticionDTO.getListPracticas()));
        update(peticionDTO);
        return new ABMResult(true, "Petición actualizada con éxito");
    }

    public ABMResult deletePeticion(int id) {
        delete(id);
        return new ABMResult(true, "Petición eliminada con éxito");
    }

    public List<PeticionDTO> getAllPeticiones() {
        return readAll();
    }

    private Date getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(formatter);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date calculateExpectedDate(Date fechaInicial, List<PracticaDTO> listPracticas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaInicial);
        int horas = 0;
        for (PracticaDTO practicaDTO : listPracticas) {
            horas += practicaDTO.getCantidadHorasResultados();
        }
        calendar.add(Calendar.HOUR_OF_DAY, horas);
        return calendar.getTime();
    }
}
