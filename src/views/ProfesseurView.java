package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Professeur;
import dao.ProfesseurDAO;
import java.awt.event.*;
import java.util.List;

public class ProfesseurView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtNom;
    private JTextField txtPrenoms;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbGrade;

    public ProfesseurView() {
        setTitle("Gestion des Professeurs");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // Initialisation des composants
        initComponents();
        
        // Configuration du menu
        setupMenuBar();
        
        // Affichage initial des données
        afficherTousLesProfesseurs();
    }

    private void initComponents() {
        JLabel lblNom = new JLabel("Nom:");
        lblNom.setBounds(19, 98, 50, 25);
        getContentPane().add(lblNom);

        txtNom = new JTextField();
        txtNom.setBounds(99, 98, 200, 25);
        getContentPane().add(txtNom);

        JLabel lblPrenoms = new JLabel("Prénoms:");
        lblPrenoms.setBounds(19, 170, 70, 25);
        getContentPane().add(lblPrenoms);

        txtPrenoms = new JTextField();
        txtPrenoms.setBounds(99, 170, 200, 25);
        getContentPane().add(txtPrenoms);

        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setBounds(20, 242, 50, 25);
        getContentPane().add(lblGrade);

        String[] grades = {
            "Professeur titulaire", "Maître de Conférences",
            "Assistant d'Enseignement Supérieur et de Recherche",
            "Docteur HDR", "Docteur en Informatique", "Doctorant en informatique"
        };

        cbGrade = new JComboBox<>(grades);
        cbGrade.setBounds(99, 242, 200, 25);
        getContentPane().add(cbGrade);

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(114, 374, 100, 30);
        btnAjouter.addActionListener(this::ajouterProfesseur);
        getContentPane().add(btnAjouter);

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(367, 374, 100, 30);
        btnModifier.addActionListener(this::modifierProfesseur);
        getContentPane().add(btnModifier);

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(565, 374, 100, 30);
        btnSupprimer.addActionListener(this::supprimerProfesseur);
        getContentPane().add(btnSupprimer);

        // Tableau des professeurs
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénoms", "Grade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(320, 40, 450, 287);
        getContentPane().add(scrollPane);

        // Sélection dans le tableau
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    txtNom.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtPrenoms.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    cbGrade.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Navigation
        JMenu mnNavigation = new JMenu("Navigation");
        
        // Item Professeur
        JMenuItem miProfesseur = new JMenuItem("Professeurs");
        miProfesseur.addActionListener(e -> {
            new ProfesseurView().setVisible(true);
            dispose();
        });
        
        // Item Classe
        JMenuItem miClasse = new JMenuItem("Classes");
        miClasse.addActionListener(e -> {
            new ClasseView().setVisible(true);
            dispose();
        });
        
        // Item Salle
        JMenuItem miSalle = new JMenuItem("Salles");
        miSalle.addActionListener(e -> {
            new SalleView().setVisible(true);
            dispose();
        });
        
        // Item Emploi du temps
        JMenuItem miEDT = new JMenuItem("Emploi du temps");
        miEDT.addActionListener(e -> {
            new EmploiDuTempsView().setVisible(true);
            dispose();
        });

        mnNavigation.add(miProfesseur);
        mnNavigation.add(miClasse);
        mnNavigation.add(miSalle);
        mnNavigation.add(miEDT);
        
        menuBar.add(mnNavigation);
        setJMenuBar(menuBar);
    }

    private void ajouterProfesseur(ActionEvent e) {
        String nom = txtNom.getText();
        String prenoms = txtPrenoms.getText();
        String grade = cbGrade.getSelectedItem().toString();

        if (nom.isEmpty() || prenoms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        Professeur p = new Professeur(nom, prenoms, grade);
        boolean ok = ProfesseurDAO.ajouter(p);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Ajout réussi ! ID généré = " + p.getIdprof());
            viderChamps();
            afficherTousLesProfesseurs();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de l'ajout !");
        }
    }

    private void modifierProfesseur(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un professeur à modifier.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = txtNom.getText();
        String prenoms = txtPrenoms.getText();
        String grade = cbGrade.getSelectedItem().toString();

        Professeur p = new Professeur(id, nom, prenoms, grade);
        boolean ok = ProfesseurDAO.modifier(p);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Modification réussie !");
            viderChamps();
            afficherTousLesProfesseurs();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la modification !");
        }
    }

    private void supprimerProfesseur(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un professeur à supprimer.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment supprimer ce professeur ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = ProfesseurDAO.supprimer(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !");
                afficherTousLesProfesseurs();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la suppression !");
            }
        }
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