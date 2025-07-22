package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Professeur;
import dao.ProfesseurDAO;
import java.awt.*;
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
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Appliquer un look and feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialisation des composants
        initComponents();
        
        // Configuration du menu
        setupMenuBar();
        
        // Affichage initial des données
        afficherTousLesProfesseurs();
    }

    private void initComponents() {
        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Panel de formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                "Informations du Professeur",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)));
        formPanel.setBackground(Color.WHITE);
        
        // Ajout des champs de formulaire
        JLabel lblNom = new JLabel("Nom:");
        lblNom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNom = new JTextField();
        txtNom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblPrenoms = new JLabel("Prénoms:");
        lblPrenoms.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrenoms = new JTextField();
        txtPrenoms.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        String[] grades = {
            "Professeur titulaire", "Maître de Conférences",
            "Assistant d'Enseignement Supérieur et de Recherche",
            "Docteur HDR", "Docteur en Informatique", "Doctorant en informatique"
        };
        
        cbGrade = new JComboBox<>(grades);
        cbGrade.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbGrade.setBackground(Color.WHITE);
        
        formPanel.add(lblNom);
        formPanel.add(txtNom);
        formPanel.add(lblPrenoms);
        formPanel.add(txtPrenoms);
        formPanel.add(lblGrade);
        formPanel.add(cbGrade);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnAjouter = createStyledButton("Ajouter", new Color(46, 125, 50));
        btnAjouter.addActionListener(this::ajouterProfesseur);
        
        JButton btnModifier = createStyledButton("Modifier", new Color(30, 136, 229));
        btnModifier.addActionListener(this::modifierProfesseur);
        
        JButton btnSupprimer = createStyledButton("Supprimer", new Color(198, 40, 40));
        btnSupprimer.addActionListener(this::supprimerProfesseur);
        
        JButton btnVider = createStyledButton("Vider", new Color(158, 158, 158));
        btnVider.addActionListener(e -> viderChamps());
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnVider);
        
        // Tableau des professeurs avec style amélioré
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénoms", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        
        // Ajout d'un écouteur pour remplir les champs lors de la sélection
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
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Professeurs"));
        
        // Organisation des panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(70, 130, 180));
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        // Menu Navigation
        JMenu mnNavigation = new JMenu("Navigation");
        mnNavigation.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mnNavigation.setForeground(Color.BLACK);
        
        // Item Professeur
        JMenuItem miProfesseur = createMenuItem("Professeurs");
        miProfesseur.addActionListener(e -> {
            new ProfesseurView().setVisible(true);
            dispose();
        });
        
        // Item Classe
        JMenuItem miClasse = createMenuItem("Classes");
        miClasse.addActionListener(e -> {
            new ClasseView().setVisible(true);
            dispose();
        });
        
        // Item Salle
        JMenuItem miSalle = createMenuItem("Salles");
        miSalle.addActionListener(e -> {
            new SalleView().setVisible(true);
            dispose();
        });
        
        // Item Emploi du temps
        JMenuItem miEDT = createMenuItem("Emploi du temps");
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
    
    private JMenuItem createMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return menuItem;
    }

    private void ajouterProfesseur(ActionEvent e) {
        String nom = txtNom.getText().trim();
        String prenoms = txtPrenoms.getText().trim();
        String grade = cbGrade.getSelectedItem().toString();

        if (nom.isEmpty() || prenoms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Professeur p = new Professeur(nom, prenoms, grade);
        boolean ok = ProfesseurDAO.ajouter(p);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Ajout réussi ! ID généré = " + p.getIdprof(), "Succès", JOptionPane.INFORMATION_MESSAGE);
            viderChamps();
            afficherTousLesProfesseurs();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierProfesseur(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un professeur à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nom = txtNom.getText().trim();
        String prenoms = txtPrenoms.getText().trim();
        String grade = cbGrade.getSelectedItem().toString();

        if (nom.isEmpty() || prenoms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Professeur p = new Professeur(id, nom, prenoms, grade);
        boolean ok = ProfesseurDAO.modifier(p);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Modification réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            viderChamps();
            afficherTousLesProfesseurs();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerProfesseur(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un professeur à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment supprimer ce professeur ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = ProfesseurDAO.supprimer(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderChamps();
                afficherTousLesProfesseurs();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression !", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> {
            ProfesseurView frame = new ProfesseurView();
            frame.setVisible(true);
        });
    }
}