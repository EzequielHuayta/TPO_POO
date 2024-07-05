package utils;

import model.practica.PracticaDTO;

public class CheckListItem {
    private final PracticaDTO practica;
    private boolean isSelected;

    public CheckListItem(PracticaDTO practica) {
        this.practica = practica;
        this.isSelected = false;
    }

    public PracticaDTO getPractica() {
        return practica;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return practica.getNombre();
    }
}
