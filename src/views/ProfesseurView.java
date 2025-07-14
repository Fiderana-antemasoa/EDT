package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Professeur;
import dao.ProfesseurDAO;

import java.awt.event.*;
import java.util.List;
import java.awt.Color;

public class ProfesseurView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtNom;
    private JTextField txtPrenoms;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbGrade;

    public ProfesseurView() {
        setTitle("Gestion des Professeurs");
        setSize(764, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblNom = new JLabel("Nom:");
        lblNom.setBounds(16, 39, 46, 25);
        getContentPane().add(lblNom);

        txtNom = new JTextField();
        txtNom.setBounds(72, 39, 200, 25);
        getContentPane().add(txtNom);

        JLabel lblPrenoms = new JLabel("Prénoms:");
        lblPrenoms.setBounds(10, 120, 70, 14);
        getContentPane().add(lblPrenoms);

        txtPrenoms = new JTextField();
        txtPrenoms.setBounds(72, 115, 200, 25);
        getContentPane().add(txtPrenoms);

        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setBounds(16, 209, 46, 14);
        getContentPane().add(lblGrade);

        String[] grades = {
            "Professeur titulaire", "Maître de Conférences",
            "Assistant d’Enseignement Supérieur et de Recherche",
            "Docteur HDR", "Docteur en Informatique", "Doctorant en informatique"
        };

        cbGrade = new JComboBox<>(grades);
        cbGrade.setBounds(72, 204, 200, 25);
        getContentPane().add(cbGrade);

        
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(72, 313, 89, 23);
        getContentPane().add(btnAjouter);

        btnAjouter.addActionListener(e -> {
            String nom = txtNom.getText();
            String prenoms = txtPrenoms.getText();
            String grade = cbGrade.getSelectedItem().toString();

            if (nom.isEmpty() || prenoms.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                return;
            }

            Professeur p = new Professeur(nom, prenoms, grade);
            boolean ok = ProfesseurDAO.ajouter(p);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Ajout réussi ! ID généré = " + p.getIdprof());
                viderChamps();
                afficherTousLesProfesseurs();
            } else {
                JOptionPane.showMessageDialog(null, "Échec de l'ajout !");
            }
        });

 
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(309, 313, 89, 23);
        getContentPane().add(btnModifier);

        btnModifier.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Sélectionnez un professeur à modifier.");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nom = txtNom.getText();
            String prenoms = txtPrenoms.getText();
            String grade = cbGrade.getSelectedItem().toString();

            Professeur p = new Professeur(id, nom, prenoms, grade);
            boolean ok = ProfesseurDAO.modifier(p);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Modification réussie !");
                viderChamps();
                afficherTousLesProfesseurs();
            } else {
                JOptionPane.showMessageDialog(null, "Échec de la modification !");
            }
        });

        // BOUTON SUPPRIMER
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(548, 313, 100, 23);
        getContentPane().add(btnSupprimer);

        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Sélectionnez un professeur à supprimer.");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ce professeur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = ProfesseurDAO.supprimer(id);
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Suppression réussie !");
                    afficherTousLesProfesseurs();
                } else {
                    JOptionPane.showMessageDialog(null, "Échec de la suppression !");
                }
            }
        });

        
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénoms", "Grade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(298, 23, 450, 250);
        getContentPane().add(scrollPane);
    
        
        JMenuBar menuBar_1 = new JMenuBar();
        setJMenuBar(menuBar_1);
        
        JMenu mnNewMenu = new JMenu("Professeur");
        menuBar_1.add(mnNewMenu);
        
        JMenu mnClasee = new JMenu("Classe");
        menuBar_1.add(mnClasee);
        
        JMenu mnSalle = new JMenu("Salle");
        menuBar_1.add(mnSalle);
        
        JMenu mnEmploiDuTemps = new JMenu("Emploi du temps");
        menuBar_1.add(mnEmploiDuTemps);

      
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                txtNom.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtPrenoms.setText(tableModel.getValueAt(selectedRow, 2).toString());
                cbGrade.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        afficherTousLesProfesseurs();
    }

    private void afficherTousLesProfesseurs() {
        tableModel.setRowCount(0);
        List<Professeur> profs = ProfesseurDAO.lister();
        for (Professeur p : profs) {
            tableModel.addRow(new Object[]{p.getIdprof(), p.getNom(), p.getPrenoms(), p.getGrade()});
        }
    }

    private void viderChamps() {
        txtNom.setText("");
        txtPrenoms.setText("");
        cbGrade.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProfesseurView().setVisible(true));
    }
}
