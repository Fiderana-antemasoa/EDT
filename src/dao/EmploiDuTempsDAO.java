package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.EmploiDuTemps;
import utils.Connexion;

import java.sql.Connection;

public class EmploiDuTempsDAO {

    
    public boolean ajouterEmploi(EmploiDuTemps edt) {
        String sql = "INSERT INTO EMPLOI_DU_TEMPS (idsalle, idprof, idclasse, cours, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, edt.getIdsalle());
            pst.setString(2, edt.getIdprof());
            pst.setString(3, edt.getIdclasse());
            pst.setString(4, edt.getCours());
            pst.setString(5, edt.getDate());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public boolean modifierEmploi(EmploiDuTemps edt) {
        String sql = "UPDATE EMPLOI_DU_TEMPS SET cours = ?, date = ? WHERE idsalle = ? AND idprof = ? AND idclasse = ?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, edt.getCours());
            pst.setString(2, edt.getDate());
            pst.setInt(3, edt.getIdsalle());
            pst.setString(4, edt.getIdprof());
            pst.setString(5, edt.getIdclasse());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public boolean supprimerEmploi(int idsalle, String idprof, String idclasse) {
        String sql = "DELETE FROM EMPLOI_DU_TEMPS WHERE idsalle = ? AND idprof = ? AND idclasse = ?";
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idsalle);
            pst.setString(2, idprof);
            pst.setString(3, idclasse);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public List<EmploiDuTemps> getAllEmplois() {
        List<EmploiDuTemps> liste = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOI_DU_TEMPS";

        try (Connection con = Connexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                EmploiDuTemps edt = new EmploiDuTemps();
                edt.setIdsalle(rs.getInt("idsalle"));
                edt.setIdprof(rs.getString("idprof"));
                edt.setIdclasse(rs.getString("idclasse"));
                edt.setCours(rs.getString("cours"));
                edt.setDate(rs.getString("date"));
                liste.add(edt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}