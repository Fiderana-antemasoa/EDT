package model;

public class Professeur {
    private int idprof;
    private String nom;
    private String prenoms;
    private String grade;

    
    public Professeur(int idprof, String nom, String prenoms, String grade) {
        this.idprof = idprof;
        this.nom = nom;
        this.prenoms = prenoms;
        this.grade = grade;
    }

   
    public Professeur(String nom, String prenoms, String grade) {
        this.nom = nom;
        this.prenoms = prenoms;
        this.grade = grade;
    }

    public int getIdprof() { return idprof; }
    public void setIdprof(int idprof) { this.idprof = idprof; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenoms() { return prenoms; }
    public void setPrenoms(String prenoms) { this.prenoms = prenoms; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
