package model;
import java.text.SimpleDateFormat;
public class EmploiDuTemps {
    private int idsalle;
    private String idprof;
    private String idclasse;
    private String cours;
    private String date;
    private String salleDesign; // Nouveau champ pour le nom de la salle
    private String profNomComplet; // Nouveau champ pour le nom complet du professeur
    private String classeNiveau; // Nouveau champ pour le niveau de la classe
    

    public EmploiDuTemps() {}

    public EmploiDuTemps(int idsalle, String idprof, String idclasse, String cours, String date) {
        this.idsalle = idsalle;
        this.idprof = idprof;
        this.idclasse = idclasse;
        this.cours = cours;
        this.date = date;
    }

    // Getters et Setters
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

    public String getSalleDesign() {
        return salleDesign;
    }

    public void setSalleDesign(String salleDesign) {
        this.salleDesign = salleDesign;
    }

    public String getProfNomComplet() {
        return profNomComplet;
    }

    public void setProfNomComplet(String profNomComplet) {
        this.profNomComplet = profNomComplet;
    }

    public String getClasseNiveau() {
        return classeNiveau;
    }

    public void setClasseNiveau(String classeNiveau) {
        this.classeNiveau = classeNiveau;
    }

    // Méthode utilitaire pour obtenir la date formatée
    public String getDateFormatee() {
        try {
            SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEEE dd MMMM yyyy - HH:mm");
            java.util.Date date = sdfDB.parse(this.date);
            return sdfDisplay.format(date);
        } catch (Exception e) {
            return this.date; // Retourne la date originale si le formatage échoue
        }
    }

    @Override
    public String toString() {
        return "EmploiDuTemps{" +
                "idsalle=" + idsalle +
                ", salleDesign='" + salleDesign + '\'' +
                ", idprof='" + idprof + '\'' +
                ", profNomComplet='" + profNomComplet + '\'' +
                ", idclasse='" + idclasse + '\'' +
                ", classeNiveau='" + classeNiveau + '\'' +
                ", cours='" + cours + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}