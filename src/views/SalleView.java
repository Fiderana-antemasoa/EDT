package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.SalleDAO;
import model.Salle;
import java.awt.event.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class SalleView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldDesign;
    private JTable table;
    private JComboBox<String> comboBoxOccupation;
    private DefaultTableModel tableModel;

    public SalleView() {
        setTitle("Gestion des Salles");
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
        afficherSalles();
    }

    private void initComponents() {
        JLabel lblDesign = new JLabel("Design:");
        lblDesign.setBounds(31, 108, 80, 25);
        contentPane.add(lblDesign);

        textFieldDesign = new JTextField();
        textFieldDesign.setBounds(110, 108, 200, 25);
        contentPane.add(textFieldDesign);

        JLabel lblOccupation = new JLabel("Occupation:");
        lblOccupation.setBounds(20, 171, 80, 25);
        contentPane.add(lblOccupation);

        comboBoxOccupation = new JComboBox<>(new String[]{"Libre", "Occupée"});
        comboBoxOccupation.setBounds(110, 171, 200, 25);
        contentPane.add(comboBoxOccupation);

        // Tableau des salles
        tableModel = new DefaultTableModel(new Object[]{"ID", "Design", "Occupation"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(320, 50, 450, 228);
        contentPane.add(scrollPane);

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(47, 289, 100, 30);
        btnAjouter.addActionListener(this::ajouterSalle);
        contentPane.add(btnAjouter);

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(245, 289, 100, 30);
        btnModifier.addActionListener(this::modifierSalle);
        contentPane.add(btnModifier);

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(499, 289, 100, 30);
        btnSupprimer.addActionListener(this::supprimerSalle);
        contentPane.add(btnSupprimer);

        // Sélection dans le tableau
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    textFieldDesign.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    comboBoxOccupation.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
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
        
        // Item Salle (désactivé car on est déjà sur cette vue)
        JMenuItem miSalle = new JMenuItem("Salles");
        miSalle.setEnabled(false);
        
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

    private void ajouterSalle(ActionEvent e) {
        String design = textFieldDesign.getText().trim();
        String occupation = comboBoxOccupation.getSelectedItem().toString();

        if (design.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un design.");
            return;
        }

        Salle salle = new Salle(design, occupation);
        boolean ok = SalleDAO.ajouter(salle);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Ajout réussi !");
            viderChamps();
            afficherSalles();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de l'ajout !");
        }
    }

    private void modifierSalle(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle à modifier.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String design = textFieldDesign.getText().trim();
        String occupation = comboBoxOccupation.getSelectedItem().toString();

        Salle salle = new Salle(id, design, occupation);
        boolean ok = SalleDAO.modifier(salle);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Modification réussie !");
            afficherSalles();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la modification !");
        }
    }

    private void supprimerSalle(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle à supprimer.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment supprimer cette salle ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = SalleDAO.supprimer(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !");
                viderChamps();
                afficherSalles();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la suppression !");
            }
        }
    }

    private void afficherSalles() {
        tableModel.setRowCount(0);
        List<Salle> salles = SalleDAO.lister();
        for (Salle s : salles) {
            tableModel.addRow(new Object[]{s.getIdsalle(), s.getDesign(), s.getOccupation()});
        }
    }

    private void viderChamps() {
        textFieldDesign.setText("");
        comboBoxOccupation.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalleView().setVisible(true));
    }
}