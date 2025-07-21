package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import dao.EmploiDuTempsDAO;
import model.EmploiDuTemps;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.Date;
import javax.swing.SpinnerDateModel;
import java.util.Calendar;
import java.util.Date;


public class EmploiDuTempsView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTable tableSallesLibres;
    private JTextField textField_idsalle;
    private JTextField textField_idprof;
    private JTextField textField_idclasse;
    private JTextField textField_cours;
    private JDateChooser dateChooser;
    private DefaultTableModel tableModel;
    private DefaultTableModel sallesLibresModel;
    private EmploiDuTempsDAO dao;
    private JSpinner spinnerHeure;
    private JSpinner hourSpinner;


    public EmploiDuTempsView() {
        setTitle("Gestion Emploi du Temps");
        setSize(999, 600); // Augmenté pour accommoder la nouvelle fonctionnalité
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        dao = new EmploiDuTempsDAO();
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        initComponents();
        setupMenuBar();
        loadTable();
    }

    private void initComponents() {
        // Labels
        JLabel lblIdsalle = new JLabel("ID Salle:");
        lblIdsalle.setBounds(20, 40, 80, 25);
        contentPane.add(lblIdsalle);

        JLabel lblIdprof = new JLabel("ID Professeur:");
        lblIdprof.setBounds(20, 80, 100, 25);
        contentPane.add(lblIdprof);

        JLabel lblIdclasse = new JLabel("ID Classe:");
        lblIdclasse.setBounds(20, 120, 80, 25);
        contentPane.add(lblIdclasse);

        JLabel lblCours = new JLabel("Cours:");
        lblCours.setBounds(20, 160, 80, 25);
        contentPane.add(lblCours);

        JLabel lblDate = new JLabel("Date:");
        lblDate.setBounds(20, 200, 80, 25);
        contentPane.add(lblDate);
        
        JLabel lblHeure = new JLabel("Heure:");
        lblDate.setBounds(20, 200, 80, 25);
        contentPane.add(lblHeure);

        // Champs de texte
        textField_idsalle = new JTextField();
        textField_idsalle.setBounds(120, 40, 200, 25);
        contentPane.add(textField_idsalle);

        textField_idprof = new JTextField();
        textField_idprof.setBounds(120, 80, 200, 25);
        contentPane.add(textField_idprof);

        textField_idclasse = new JTextField();
        textField_idclasse.setBounds(120, 120, 200, 25);
        contentPane.add(textField_idclasse);

        textField_cours = new JTextField();
        textField_cours.setBounds(120, 160, 200, 25);
        contentPane.add(textField_cours);

        // Sélecteur de date
     // DateChooser
        dateChooser = new JDateChooser();
        dateChooser.setBounds(120, 200, 120, 25);
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Affiche uniquement la date
        contentPane.add(dateChooser);

        // HeureSpinner
        SpinnerDateModel hourModel = new SpinnerDateModel();
        hourSpinner = new JSpinner(hourModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(hourSpinner, "HH:mm");
        hourSpinner.setEditor(editor);
        hourSpinner.setBounds(240, 200, 80, 25);
        contentPane.add(hourSpinner);
        
        // Tableau principal
        tableModel = new DefaultTableModel(new Object[]{"ID Salle", "ID Prof", "ID Classe", "Cours", "Date"}, 0);
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(350, 40, 623, 200);
        contentPane.add(scrollPane);

        // Boutons
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(120, 250, 100, 30);
        btnAjouter.addActionListener(e -> ajouterEmploiAction());
        contentPane.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(240, 250, 100, 30);
        btnModifier.addActionListener(e -> modifierEmploiAction());
        contentPane.add(btnModifier);

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(360, 250, 100, 30);
        btnSupprimer.addActionListener(e -> supprimerEmploiAction());
        contentPane.add(btnSupprimer);

        // Section Recherche de salles libres
        JLabel lblRechercheTitre = new JLabel("Recherche de salles libres:");
        lblRechercheTitre.setBounds(20, 300, 200, 25);
        contentPane.add(lblRechercheTitre);

        JLabel lblDateRecherche = new JLabel("Date:");
        lblDateRecherche.setBounds(20, 340, 80, 25);
        contentPane.add(lblDateRecherche);

        JDateChooser dateRechercheChooser = new JDateChooser();
        dateRechercheChooser.setBounds(120, 340, 200, 25);
        contentPane.add(dateRechercheChooser);

        JLabel lblHeureRecherche = new JLabel("Heure:");
        lblHeureRecherche.setBounds(20, 380, 80, 25);
        contentPane.add(lblHeureRecherche);

        SpinnerModel spinnerModel = new SpinnerDateModel();
        JSpinner spinnerHeure1 = new JSpinner(spinnerModel);
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spinnerHeure1, "HH:mm");
        spinnerHeure1.setEditor(editor1);
        spinnerHeure1.setBounds(120, 380, 200, 25);
        contentPane.add(spinnerHeure1);

        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBounds(120, 420, 200, 30);
        btnRechercher.addActionListener(e -> {
            Date date = dateRechercheChooser.getDate();
            Date heure = (Date) spinnerHeure1.getValue();
            
            if (date == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date");
                return;
            }
            
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
            String dateHeureComplete = sdfDate.format(date) + " " + sdfHeure.format(heure) + ":00";
            
            rechercherSallesLibresAction(dateHeureComplete);
        });
        contentPane.add(btnRechercher);

        // Tableau des salles libres
        sallesLibresModel = new DefaultTableModel(new Object[]{"ID Salle", "Statut"}, 0);
        tableSallesLibres = new JTable(sallesLibresModel);
        JScrollPane scrollPaneSallesLibres = new JScrollPane(tableSallesLibres);
        scrollPaneSallesLibres.setBounds(350, 340, 550, 150);
        contentPane.add(scrollPaneSallesLibres);

        // Sélection dans le tableau principal
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    textField_idsalle.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    textField_idprof.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    textField_idclasse.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    textField_cours.setText(tableModel.getValueAt(selectedRow, 3).toString());

                    try {
                        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .parse(tableModel.getValueAt(selectedRow, 4).toString());
                        dateChooser.setDate(date);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu mnNavigation = new JMenu("Navigation");
        
        JMenuItem miProfesseur = new JMenuItem("Professeurs");
        miProfesseur.addActionListener(e -> {
            new ProfesseurView().setVisible(true);
            dispose();
        });
        
        JMenuItem miClasse = new JMenuItem("Classes");
        miClasse.addActionListener(e -> {
            new ClasseView().setVisible(true);
            dispose();
        });
        
        JMenuItem miSalle = new JMenuItem("Salles");
        miSalle.addActionListener(e -> {
            new SalleView().setVisible(true);
            dispose();
        });
        
        JMenuItem miEDT = new JMenuItem("Emploi du temps");
        miEDT.setEnabled(false);

        JMenuItem miEmploiClasse = new JMenuItem("Emploi par Classe");
        miEmploiClasse.addActionListener(e -> {
            new EmploiClasseView().setVisible(true);
        });

        mnNavigation.add(miProfesseur);
        mnNavigation.add(miClasse);
        mnNavigation.add(miSalle);
        mnNavigation.add(miEDT);
        mnNavigation.add(miEmploiClasse);
        
        menuBar.add(mnNavigation);
        setJMenuBar(menuBar);
    }

    private void ajouterEmploiAction() {
        EmploiDuTemps edt = getEmploiFromFields();
        if (edt == null) return;

        if (dao.ajouterEmploi(edt)) {
            JOptionPane.showMessageDialog(this, "Ajout réussi !");
            loadTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "La salle est déjà occupée à ce créneau horaire !", 
                "Salle occupée", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modifierEmploiAction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un emploi à modifier.");
            return;
        }

        int ancienIdsalle = (int) tableModel.getValueAt(selectedRow, 0);
        String ancienneDate = tableModel.getValueAt(selectedRow, 4).toString();

        EmploiDuTemps edt = getEmploiFromFields();
        if (edt == null) return;

        if (dao.modifierEmploi(edt, ancienIdsalle, ancienneDate)) {
            JOptionPane.showMessageDialog(this, "Modification réussie !");
            loadTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Échec de la modification. La salle est peut-être occupée à ce nouveau créneau.",
                "Erreur", JOptionPane.WARNING_MESSAGE);
        }
        
        
    }

    private void supprimerEmploiAction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un emploi à supprimer.");
            return;
        }

        int idsalle = (int) tableModel.getValueAt(selectedRow, 0);
        String idprof = tableModel.getValueAt(selectedRow, 1).toString();
        String idclasse = tableModel.getValueAt(selectedRow, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment supprimer cet emploi du temps ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.supprimerEmploi(idsalle, idprof, idclasse)) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !");
                loadTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    private void rechercherSallesLibresAction(String dateHeure) {
        List<Integer> sallesLibres = dao.getSallesLibres(dateHeure);
        
        sallesLibresModel.setRowCount(0);
        
        if (sallesLibres.isEmpty()) {
            sallesLibresModel.addRow(new Object[]{"Aucune", "salle disponible"});
        } else {
            for (Integer idSalle : sallesLibres) {
                sallesLibresModel.addRow(new Object[]{idSalle, "Libre"});
            }
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<EmploiDuTemps> liste = dao.getAllEmplois();
        for (EmploiDuTemps edt : liste) {
            tableModel.addRow(new Object[]{
                edt.getIdsalle(),
                edt.getIdprof(),
                edt.getIdclasse(),
                edt.getCours(),
                edt.getDate()
            });
        }
    }

    private void clearFields() {
        textField_idsalle.setText("");
        textField_idprof.setText("");
        textField_idclasse.setText("");
        textField_cours.setText("");
        dateChooser.setDate(null);
    }

    private EmploiDuTemps getEmploiFromFields() {
        try {
            int idsalle = Integer.parseInt(textField_idsalle.getText().trim());
            String idprof = textField_idprof.getText().trim();
            String idclasse = textField_idclasse.getText().trim();
            String cours = textField_cours.getText().trim();

            // Vérifie que la date est sélectionnée
            Date date = dateChooser.getDate();
            if (date == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date.");
                return null;
            }

            // Récupère l’heure depuis le spinner (tu dois avoir défini hourSpinner quelque part)
            Date heure = (Date) hourSpinner.getValue();

            // Fusionne date + heure
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(date);

            Calendar calendarHeure = Calendar.getInstance();
            calendarHeure.setTime(heure);

            calendarDate.set(Calendar.HOUR_OF_DAY, calendarHeure.get(Calendar.HOUR_OF_DAY));
            calendarDate.set(Calendar.MINUTE, calendarHeure.get(Calendar.MINUTE));
            calendarDate.set(Calendar.SECOND, 0);

            // Format final au format String
            Date dateTimeFinal = calendarDate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(dateTimeFinal);

            return new EmploiDuTemps(idsalle, idprof, idclasse, cours, dateStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Salle doit être un entier.");
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des champs.");
            ex.printStackTrace();
            return null;
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmploiDuTempsView().setVisible(true));
    }
}