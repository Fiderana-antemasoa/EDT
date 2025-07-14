package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Classe;
import utils.Connexion;

public class ClasseDAO {

    public static boolean ajouter(Classe c) {
        String sql = "INSERT INTO classe (idclasse, niveau) VALUES (?, ?)";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, c.getIdclasse());
            pst.setString(2, c.getNiveau());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur ajout CLASSE : " + e.getMessage());
            return false;
        }
    }

    public static boolean modifier(Classe c) {
        String sql = "UPDATE classe SET niveau=? WHERE idclasse=?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, c.getNiveau());
            pst.setString(2, c.getIdclasse());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur modification CLASSE : " + e.getMessage());
            return false;
        }
    }

    public static boolean supprimer(String idclasse) {
        String sql = "DELETE FROM classe WHERE idclasse=?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, idclasse);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression CLASSE : " + e.getMessage());
            return false;
        }
    }

    public static List<Classe> lister() {
        List<Classe> liste = new ArrayList<>();
        String sql = "SELECT * FROM classe";
        try (Connection con = Connexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Classe c = new Classe(
                    rs.getString("idclasse"),
                    rs.getString("niveau")
                );
                liste.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur liste CLASSE : " + e.getMessage());
        }
        return liste;
    }
}
