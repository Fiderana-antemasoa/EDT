package model;

public class Salle {
    private int idsalle;
    private String design;
    private String occupation;

    public Salle(int idsalle, String design, String occupation) {
        this.idsalle = idsalle;
        this.design = design;
        this.occupation = occupation;
    }

    public Salle(String design, String occupation) {
        this.design = design;
        this.occupation = occupation;
    }

    public int getIdsalle() {
        return idsalle;
    }

    public void setIdsalle(int idsalle) {
        this.idsalle = idsalle;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
