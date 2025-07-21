package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.ClasseDAO;
import model.Classe;
import java.awt.event.*;
import java.util.List;

public class ClasseView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNiveau;
    private JTextField textFieldIdClasse;
    private JTable table;
    private DefaultTableModel tableModel;

    public ClasseView() {
        setTitle("Gestion des Classes");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Initialisation des composants
        initComponents();
        
        // Configuration du menu
        setupMenuBar();
        
        // Affichage initial des données
        afficherToutesLesClasses();
    }

    private void initComponents() {
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(36, 108, 50, 25);
        contentPane.add(lblId);

        textFieldIdClasse = new JTextField();
        textFieldIdClasse.setBounds(110, 108, 200, 25);
        contentPane.add(textFieldIdClasse);

        JLabel lblNiveau = new JLabel("Niveau:");
        lblNiveau.setBounds(26, 182, 60, 25);
        contentPane.add(lblNiveau);

        textFieldNiveau = new JTextField();
        textFieldNiveau.setBounds(110, 182, 200, 25);
        contentPane.add(textFieldNiveau);

        // Tableau des classes
        tableModel = new DefaultTableModel(new Object[]{"ID Classe", "Niveau"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(320, 40, 450, 278);
        contentPane.add(scrollPane);

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(109, 344, 100, 30);
        btnAjouter.addActionListener(this::ajouterClasse);
        contentPane.add(btnAjouter);

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(373, 344, 100, 30);
        btnModifier.addActionListener(this::modifierClasse);
        contentPane.add(btnModifier);

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(636, 344, 100, 30);
        btnSupprimer.addActionListener(this::supprimerClasse);
        contentPane.add(btnSupprimer);

        // Sélection dans le tableau
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    String niveau = tableModel.getValueAt(selectedRow, 1).toString();
                    textFieldIdClasse.setText(id);
                    textFieldIdClasse.setEnabled(false);
                    textFieldNiveau.setText(niveau);
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

    private void ajouterClasse(ActionEvent e) {
        String idclasse = textFieldIdClasse.getText().trim();
        String niveau = textFieldNiveau.getText().trim();

        if (idclasse.isEmpty() || niveau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        Classe c = new Classe(idclasse, niveau);
        boolean ok = ClasseDAO.ajouter(c);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Ajout réussi !");
            viderChamps();
            afficherToutesLesClasses();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout !");
        }
    }

    private void modifierClasse(ActionEvent e) {
        String idclasse = textFieldIdClasse.getText().trim();
        String niveau = textFieldNiveau.getText().trim();

        if (idclasse.isEmpty() || niveau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe et remplir le niveau.");
            return;
        }

        Classe c = new Classe(idclasse, niveau);
        boolean ok = ClasseDAO.modifier(c);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Modification réussie !");
            textFieldIdClasse.setEnabled(true);
            viderChamps();
            afficherToutesLesClasses();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification !");
        }
    }

    private void supprimerClasse(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.");
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer la classe " + id + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = ClasseDAO.supprimer(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !");
                viderChamps();
                afficherToutesLesClasses();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression !");
            }
        }
    }

    private void afficherToutesLesClasses() {
        tableModel.setRowCount(0);
        List<Classe> classes = ClasseDAO.lister();
        for (Classe c : classes) {
            tableModel.addRow(new Object[]{c.getIdclasse(), c.getNiveau()});
        }
    }

    private void viderChamps() {
        textFieldIdClasse.setText("");
        textFieldIdClasse.setEnabled(true);
        textFieldNiveau.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClasseView().setVisible(true));
    }
}