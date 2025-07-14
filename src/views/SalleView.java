package views;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.SalleDAO;
import model.Salle;
import java.util.List;

public class SalleView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTable table;
    private JComboBox<String> comboBox;
    private DefaultTableModel tableModel;
    private int selectedId = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SalleView frame = new SalleView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SalleView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 801, 459);

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

        JLabel lblDesign = new JLabel("Design:");
        lblDesign.setBounds(32, 79, 46, 14);
        contentPane.add(lblDesign);

        JLabel lblOccupation = new JLabel("Occupation:");
        lblOccupation.setBounds(10, 133, 68, 20);
        contentPane.add(lblOccupation);

        textField = new JTextField();
        textField.setBounds(88, 74, 200, 25);
        contentPane.add(textField);
        textField.setColumns(10);

        comboBox = new JComboBox<>(new String[]{"Libre", "Occupée"});
        comboBox.setBounds(88, 131, 200, 25);
        contentPane.add(comboBox);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(161, 314, 89, 23);
        contentPane.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(343, 314, 89, 23);
        contentPane.add(btnModifier);

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(550, 314, 89, 23);
        contentPane.add(btnSupprimer);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Design", "Occupation"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(298, 23, 450, 250);
        contentPane.add(scrollPane);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                selectedId = (int) tableModel.getValueAt(selectedRow, 0);
                textField.setText((String) tableModel.getValueAt(selectedRow, 1));
                comboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            }
        });

        btnAjouter.addActionListener(e -> {
            String design = textField.getText().trim();
            String occupation = comboBox.getSelectedItem().toString();
            if (design.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un design valide.");
                return;
            }
            Salle salle = new Salle(design, occupation);
            if (SalleDAO.ajouter(salle)) {
                JOptionPane.showMessageDialog(null, "Salle ajoutée avec succès.");
                textField.setText("");
                comboBox.setSelectedIndex(0);
                afficherToutesLesSalles();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la salle.");
            }
        });

        btnModifier.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une salle à modifier.");
                return;
            }
            String design = textField.getText().trim();
            String occupation = comboBox.getSelectedItem().toString();
            Salle salle = new Salle(selectedId, design, occupation);
            if (SalleDAO.modifier(salle)) {
                JOptionPane.showMessageDialog(null, "Salle modifiée avec succès.");
                afficherToutesLesSalles();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification.");
            }
        });

        btnSupprimer.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une salle à supprimer.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (SalleDAO.supprimer(selectedId)) {
                    JOptionPane.showMessageDialog(null, "Salle supprimée.");
                    textField.setText("");
                    comboBox.setSelectedIndex(0);
                    selectedId = -1;
                    afficherToutesLesSalles();
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression.");
                }
            }
        });

        afficherToutesLesSalles();
    }

    private void afficherToutesLesSalles() {
        tableModel.setRowCount(0);
        List<Salle> salles = SalleDAO.lister();
        for (Salle s : salles) {
            tableModel.addRow(new Object[]{s.getIdsalle(), s.getDesign(), s.getOccupation()});
        }
    }
}
