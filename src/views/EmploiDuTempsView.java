package views;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import dao.EmploiDuTempsDAO;
import model.EmploiDuTemps;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class EmploiDuTempsView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textField_idsalle;
    private JTextField textField_idprof;
    private JTextField textField_idclasse;
    private JTextField textField_cours;
    private JDateChooser dateChooser;
    private DefaultTableModel tableModel;
    private EmploiDuTempsDAO dao;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmploiDuTempsView frame = new EmploiDuTempsView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public EmploiDuTempsView() {
        dao = new EmploiDuTempsDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 920, 420);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuBar.add(new JMenu("Professeur"));
        menuBar.add(new JMenu("Classe"));
        menuBar.add(new JMenu("Salle"));
        menuBar.add(new JMenu("Emploi du temps"));

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // === TABLE ET MODEL ===
        tableModel = new DefaultTableModel(
            new Object[]{"Idsalle", "Idprof", "Idclasse", "Cours", "Date"}, 0
        );
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(298, 23, 590, 250);
        contentPane.add(scrollPane);

        // === LABELS ===
        contentPane.add(new JLabel("Idsalle:")).setBounds(21, 67, 60, 14);
        contentPane.add(new JLabel("Idprof:")).setBounds(21, 117, 60, 14);
        contentPane.add(new JLabel("Idclasse:")).setBounds(21, 159, 60, 14);
        contentPane.add(new JLabel("Cours:")).setBounds(21, 206, 60, 14);
        contentPane.add(new JLabel("Date:")).setBounds(21, 248, 60, 14);

        // === CHAMPS ===
        textField_idsalle = new JTextField();
        textField_idsalle.setBounds(88, 62, 200, 25);
        contentPane.add(textField_idsalle);

        textField_idprof = new JTextField();
        textField_idprof.setBounds(88, 112, 200, 25);
        contentPane.add(textField_idprof);

        textField_idclasse = new JTextField();
        textField_idclasse.setBounds(88, 154, 200, 25);
        contentPane.add(textField_idclasse);

        textField_cours = new JTextField();
        textField_cours.setBounds(88, 201, 200, 25);
        contentPane.add(textField_cours);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(88, 237, 200, 25);
        contentPane.add(dateChooser);

        // === BOUTONS ===
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(320, 306, 89, 23);
        contentPane.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(499, 306, 89, 23);
        contentPane.add(btnModifier);

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(654, 306, 89, 23);
        contentPane.add(btnSupprimer);

        // === EVENEMENTS ===
        loadTable(); // charger les données au démarrage

        // Quand on clique sur une ligne → remplir les champs
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

        btnAjouter.addActionListener(e -> {
            if (ajouterEmploi()) {
                JOptionPane.showMessageDialog(this, "Ajout réussi !");
                loadTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
            }
        });

        btnModifier.addActionListener(e -> {
            if (modifierEmploi()) {
                JOptionPane.showMessageDialog(this, "Modification réussie !");
                loadTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
            }
        });

        btnSupprimer.addActionListener(e -> {
            if (supprimerEmploi()) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !");
                loadTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        });
    }

    // === FONCTIONS ===

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

            java.util.Date date = dateChooser.getDate();
            if (date == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date.");
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(date);

            return new EmploiDuTemps(idsalle, idprof, idclasse, cours, dateStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Idsalle doit être un entier.");
            return null;
        }
    }

    private boolean ajouterEmploi() {
        EmploiDuTemps edt = getEmploiFromFields();
        if (edt == null) return false;
        return dao.ajouterEmploi(edt);
    }

    private boolean modifierEmploi() {
        EmploiDuTemps edt = getEmploiFromFields();
        if (edt == null) return false;
        return dao.modifierEmploi(edt);
    }

    private boolean supprimerEmploi() {
        try {
            int idsalle = Integer.parseInt(textField_idsalle.getText().trim());
            String idprof = textField_idprof.getText().trim();
            String idclasse = textField_idclasse.getText().trim();
            return dao.supprimerEmploi(idsalle, idprof, idclasse);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Idsalle doit être un entier.");
            return false;
        }
    }
}
