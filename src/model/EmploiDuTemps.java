package model;

public class EmploiDuTemps {
    private int idsalle;
    private String idprof;
    private String idclasse;
    private String cours;
    private String date; 

    public EmploiDuTemps() {}

    public EmploiDuTemps(int idsalle, String idprof, String idclasse, String cours, String date) {
        this.idsalle = idsalle;
        this.idprof = idprof;
        this.idclasse = idclasse;
        this.cours = cours;
        this.date = date;
    }

    public int getIdsalle() {
        return idsalle;
    }

    public void setIdsalle(int idsalle) {
        this.idsalle = idsalle;
    }

    public String getIdprof() {
        return idprof;
    }

    public void setIdprof(String idprof) {
        this.idprof = idprof;
    }

    public String getIdclasse() {
        return idclasse;
    }

    public void setIdclasse(String idclasse) {
        this.idclasse = idclasse;
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}



