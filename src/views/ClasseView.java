package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.ClasseDAO;
import model.Classe;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClasseView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textFieldIdClasse;
    private JTextField textFieldNiveau;

    public ClasseView() {
        setTitle("Gestion des Classes");
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
        afficherToutesLesClasses();
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
                "Informations de la Classe",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)));
        formPanel.setBackground(Color.WHITE);
        
        // Ajout des champs de formulaire
        JLabel lblIdClasse = new JLabel("ID Classe:");
        lblIdClasse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textFieldIdClasse = new JTextField();
        textFieldIdClasse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblNiveau = new JLabel("Niveau:");
        lblNiveau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textFieldNiveau = new JTextField();
        textFieldNiveau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        formPanel.add(lblIdClasse);
        formPanel.add(textFieldIdClasse);
        formPanel.add(lblNiveau);
        formPanel.add(textFieldNiveau);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnAjouter = createStyledButton("Ajouter", new Color(46, 125, 50));
        btnAjouter.addActionListener(this::ajouterClasse);
        
        JButton btnModifier = createStyledButton("Modifier", new Color(30, 136, 229));
        btnModifier.addActionListener(this::modifierClasse);
        
        JButton btnSupprimer = createStyledButton("Supprimer", new Color(198, 40, 40));
        btnSupprimer.addActionListener(this::supprimerClasse);
        
        JButton btnVider = createStyledButton("Vider", new Color(158, 158, 158));
        btnVider.addActionListener(e -> viderChamps());
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnVider);
        
        // Tableau des classes avec style amélioré
        tableModel = new DefaultTableModel(new Object[]{"ID Classe", "Niveau"}, 0) {
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
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                textFieldIdClasse.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                textFieldIdClasse.setEnabled(false);
                textFieldNiveau.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Classes"));
        
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
        mnNavigation.setForeground(Color.BLACK); // Supprimez cette ligne ou remplacez par Color.BLACK
        
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

    private void ajouterClasse(ActionEvent e) {
        String idclasse = textFieldIdClasse.getText().trim();
        String niveau = textFieldNiveau.getText().trim();

        if (idclasse.isEmpty() || niveau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Classe c = new Classe(idclasse, niveau);
        boolean ok = ClasseDAO.ajouter(c);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Ajout réussi !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            viderChamps();
            afficherToutesLesClasses();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierClasse(ActionEvent e) {
        String idclasse = textFieldIdClasse.getText().trim();
        String niveau = textFieldNiveau.getText().trim();

        if (idclasse.isEmpty() || niveau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe et remplir le niveau.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Classe c = new Classe(idclasse, niveau);
        boolean ok = ClasseDAO.modifier(c);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Modification réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            textFieldIdClasse.setEnabled(true);
            viderChamps();
            afficherToutesLesClasses();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerClasse(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer la classe " + id + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = ClasseDAO.supprimer(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Suppression réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderChamps();
                afficherToutesLesClasses();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression !", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> {
            ClasseView frame = new ClasseView();
            frame.setVisible(true);
        });
    }
}