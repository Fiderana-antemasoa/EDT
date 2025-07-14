package model;

public class Classe {
	private String idclasse;
    private String niveau;

    public Classe(String idclasse, String niveau) {
        this.idclasse = idclasse;
        this.niveau = niveau;
    }

    public String getIdclasse() {
        return idclasse;
    }

    public void setIdclasse(String idclasse) {
        this.idclasse = idclasse;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

}
