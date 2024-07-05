package model.paciente;

import com.google.gson.reflect.TypeToken;
import model.peticion.PeticionDTO;
import persist.GenericDAO;
import utils.ABMResult;

import java.lang.reflect.Type;
import java.util.List;

public class PacienteModel extends GenericDAO<PacienteDTO> {
    private static final String FILE_PATH = "src/persist/pacientes.json";
    private static final Type LIST_TYPE = new TypeToken<List<PacienteDTO>>() {}.getType();

    public PacienteModel() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected int getId(PacienteDTO paciente) {
        return paciente.getId();
    }

    public int getLatestId() {
        List<PacienteDTO> pacientes = readAll();
        if(!pacientes.isEmpty()){
            return pacientes.get(pacientes.size()-1).getId() + 1;
        }
        return 0;
    }

    private boolean verifyInUseDNI(int dni, int id) {
        List<PacienteDTO> pacientes = readAll();
        for (PacienteDTO paciente : pacientes) {
            if (paciente.getDni() == dni && paciente.getId() != id) {
                return true;
            }
        }
        return false;
    }

    public ABMResult addPaciente(PacienteDTO pacienteDTO){
        pacienteDTO.setId(getLatestId());
        if(verifyInUseDNI(pacienteDTO.getDni(), -1)){
            return new ABMResult(false, "Ya existe un paciente registrado con ese DNI");
        }
        create(pacienteDTO);
        return new ABMResult(true, "Paciente creado con éxito");
    }

    public ABMResult updatePaciente(PacienteDTO pacienteDTO){
        if(verifyInUseDNI(pacienteDTO.getDni(), pacienteDTO.getId())){
            return new ABMResult(false, "Ya existe un paciente registrado con ese DNI");
        }
        update(pacienteDTO);
        return new ABMResult(true, "Paciente actualizado con éxito");
    }

    public ABMResult deletePaciente(int id){;
        delete(id);
        return new ABMResult(true, "Paciente eliminado con éxito");
    }

    public List<PacienteDTO> getAllPacientes() {
        return readAll();
    }
}
