package views;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import dao.SalleDAO;
import model.Salle;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SalleView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldDesign;
    private JTable table;
    private JComboBox<String> comboBoxOccupation;
    private DefaultTableModel tableModel;

    public SalleView() {
        setTitle("Gestion des Salles");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        setupMenuBar();
        afficherSalles();
    }

    private void initComponents() {
        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        setContentPane(mainPanel);

        // Panel de formulaire
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                "Informations Salle",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)));
        formPanel.setBackground(Color.WHITE);

        // Champs de formulaire
        JLabel lblDesign = new JLabel("Design:");
        lblDesign.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textFieldDesign = new JTextField();
        textFieldDesign.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblOccupation = new JLabel("Occupation:");
        lblOccupation.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBoxOccupation = new JComboBox<>(new String[]{"Libre", "Occupée"});
        comboBoxOccupation.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        formPanel.add(lblDesign);
        formPanel.add(textFieldDesign);
        formPanel.add(lblOccupation);
        formPanel.add(comboBoxOccupation);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton btnAjouter = createStyledButton("Ajouter", new Color(46, 125, 50));
        btnAjouter.addActionListener(this::ajouterSalle);

        JButton btnModifier = createStyledButton("Modifier", new Color(30, 136, 229));
        btnModifier.addActionListener(this::modifierSalle);

        JButton btnSupprimer = createStyledButton("Supprimer", new Color(198, 40, 40));
        btnSupprimer.addActionListener(this::supprimerSalle);

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        // Tableau des salles
        tableModel = new DefaultTableModel(new Object[]{"ID", "Design", "Occupation"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Salles"));

        // Organisation des panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
        
        JMenu mnNavigation = new JMenu("Navigation");
        mnNavigation.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mnNavigation.setForeground(Color.BLACK);
        
        JMenuItem miProfesseur = createMenuItem("Professeurs");
        miProfesseur.addActionListener(e -> {
            new ProfesseurView().setVisible(true);
            dispose();
        });
        
        JMenuItem miClasse = createMenuItem("Classes");
        miClasse.addActionListener(e -> {
            new ClasseView().setVisible(true);
            dispose();
        });
        
        JMenuItem miSalle = createMenuItem("Salles");
        miSalle.setEnabled(false);
        
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