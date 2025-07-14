package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Professeur;
import utils.Connexion;

public class ProfesseurDAO {

    public static boolean ajouter(Professeur p) {
        String sql = "INSERT INTO professeur (nom, prenoms, grade) VALUES (?, ?, ?)";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenoms());
            pst.setString(3, p.getGrade());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        p.setIdprof(generatedId); 
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erreur ajout : " + e.getMessage());
            return false;
        }
    }

    public static boolean modifier(Professeur p) {
        String sql = "UPDATE professeur SET nom=?, prenoms=?, grade=? WHERE idprof=?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenoms());
            pst.setString(3, p.getGrade());
            pst.setInt(4, p.getIdprof());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur modification : " + e.getMessage());
            return false;
        }
    }

    public static boolean supprimer(int idprof) {
        String sql = "DELETE FROM professeur WHERE idprof=?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idprof);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
            return false;
        }
    }

    public static List<Professeur> lister() {
        List<Professeur> liste = new ArrayList<>();
        try (Connection con = Connexion.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM professeur");

            while (rs.next()) {
                Professeur p = new Professeur(
                    rs.getInt("idprof"),
                    rs.getString("nom"),
                    rs.getString("prenoms"),
                    rs.getString("grade")
                );
                liste.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur liste : " + e.getMessage());
        }
        return liste;
    }
}
