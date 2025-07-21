package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.EmploiDuTemps;
import utils.Connexion;

public class EmploiDuTempsDAO {

   
    public boolean estSalleOccupee(int idsalle, String date) {
        String sql = "SELECT COUNT(*) FROM EMPLOI_DU_TEMPS WHERE idsalle = ? AND date = ?";
        
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, idsalle);
            pst.setString(2, date);
            ResultSet rs = pst.executeQuery();
            
            return rs.next() && rs.getInt(1) > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return true; 
        }
    }

   
    public boolean ajouterEmploi(EmploiDuTemps emploi) {
        
        String checkSQL = "SELECT COUNT(*) FROM emploi_du_temps WHERE idsalle = ? AND date = ?";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {

            checkStmt.setInt(1, emploi.getIdsalle());
            checkStmt.setTimestamp(2, Timestamp.valueOf(emploi.getDate()));

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
          
                return false;
            }

            
            String insertSQL = "INSERT INTO emploi_du_temps (idsalle, idprof, idclasse, cours, date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setInt(1, emploi.getIdsalle());
            insertStmt.setString(2, emploi.getIdprof());
            insertStmt.setString(3, emploi.getIdclasse());
            insertStmt.setString(4, emploi.getCours());
            insertStmt.setTimestamp(5, Timestamp.valueOf(emploi.getDate()));

            int rows = insertStmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


  
    public boolean modifierEmploi(EmploiDuTemps edt, int ancienIdsalle, String ancienneDate) {
        
        if (edt.getIdsalle() != ancienIdsalle || !edt.getDate().equals(ancienneDate)) {
            if (estSalleOccupee(edt.getIdsalle(), edt.getDate())) {
                return false;
            }
        }
        
        String sql = "UPDATE EMPLOI_DU_TEMPS SET idsalle=?, idprof=?, idclasse=?, cours=?, date=? " +
                     "WHERE idsalle=? AND idprof=? AND idclasse=? AND date=?";
        
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, edt.getIdsalle());
            pst.setString(2, edt.getIdprof());
            pst.setString(3, edt.getIdclasse());
            pst.setString(4, edt.getCours());
            pst.setString(5, edt.getDate());
            pst.setInt(6, ancienIdsalle);
            pst.setString(7, edt.getIdprof());
            pst.setString(8, edt.getIdclasse());
            pst.setString(9, ancienneDate);
            
            return pst.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprime un emploi du temps
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
        String sql = "SELECT * FROM EMPLOI_DU_TEMPS ORDER BY date";

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


    public List<EmploiDuTemps> getEmploiDuTempsParClasse(String idClasse) {
        List<EmploiDuTemps> liste = new ArrayList<>();
        String sql = "SELECT e.*, p.nom as prof_nom, p.prenoms as prof_prenom, s.design as salle_design " +
                     "FROM EMPLOI_DU_TEMPS e " +
                     "LEFT JOIN PROFESSEUR p ON e.idprof = p.idprof " +
                     "LEFT JOIN SALLE s ON e.idsalle = s.idsalle " +
                     "WHERE e.idclasse = ? " +
                     "ORDER BY e.date";

        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, idClasse);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                EmploiDuTemps edt = new EmploiDuTemps();
                edt.setIdsalle(rs.getInt("idsalle"));
                edt.setIdprof(rs.getString("idprof"));
                edt.setProfNomComplet(rs.getString("prof_nom") + " " + rs.getString("prof_prenom"));
                edt.setIdclasse(idClasse);
                edt.setCours(rs.getString("cours"));
                edt.setDate(rs.getString("date"));
                edt.setSalleDesign(rs.getString("salle_design"));
                liste.add(edt);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emploi du temps: " + e.getMessage());
        }
        return liste;
    }

    // Récupère les salles disponibles pour un créneau horaire donné
    public List<Integer> getSallesLibres(String date) {
        List<Integer> sallesLibres = new ArrayList<>();
        String sql = "SELECT idsalle FROM SALLE WHERE idsalle NOT IN " +
                     "(SELECT idsalle FROM EMPLOI_DU_TEMPS WHERE date = ?) AND occupation = 'libre' " ;
        
        
        
        try (Connection con = Connexion.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, date);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                sallesLibres.add(rs.getInt("idsalle"));
            }
                       
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sallesLibres;
    }
}