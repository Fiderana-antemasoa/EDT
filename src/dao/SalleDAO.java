package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Salle;
import utils.Connexion;

public class SalleDAO {

    public static boolean ajouter(Salle s) {
        String sql = "INSERT INTO salle (design, occupation) VALUES (?, ?)";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, s.getDesign());
            pst.setString(2, s.getOccupation());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur ajout salle : " + e.getMessage());
            return false;
        }
    }

    public static boolean modifier(Salle s) {
        String sql = "UPDATE salle SET design=?, occupation=? WHERE idsalle=?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, s.getDesign());
            pst.setString(2, s.getOccupation());
            pst.setInt(3, s.getIdsalle());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur modification salle : " + e.getMessage());
            return false;
        }
    }

    public static boolean supprimer(int idsalle) {
        String sql = "DELETE FROM salle WHERE idsalle=?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idsalle);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression salle : " + e.getMessage());
            return false;
        }
    }

    public static List<Salle> lister() {
        List<Salle> liste = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        try (Connection con = Connexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Salle s = new Salle(
                        rs.getInt("idsalle"),
                        rs.getString("design"),
                        rs.getString("occupation")
                );
                liste.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Erreur liste salle : " + e.getMessage());
        }
        return liste;
    }
}
