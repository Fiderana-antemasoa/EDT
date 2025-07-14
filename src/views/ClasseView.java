package views;

import java.awt.EventQueue;
import java.awt.SystemColor;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import dao.ClasseDAO;
import model.Classe;

public class ClasseView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNiveau;
    private JTextField textFieldIdClasse;
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClasseView frame = new ClasseView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ClasseView() {
        setTitle("Gestion des Classes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 802, 470);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnProf = new JMenu("Professeur");
        mnProf.setForeground(SystemColor.desktop);
        menuBar.add(mnProf);

        JMenu mnClasse = new JMenu("Classe");
        mnClasse.setForeground(SystemColor.desktop);
        menuBar.add(mnClasse);

        JMenu mnSalle = new JMenu("Salle");
        mnSalle.setForeground(SystemColor.desktop);
        menuBar.add(mnSalle);

        JMenu mnEmploi = new JMenu("Emploi du temps");
        mnEmploi.setForeground(SystemColor.desktop);
        menuBar.add(mnEmploi);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(34, 37, 46, 25);
        contentPane.add(lblId);

        JLabel lblNiveau = new JLabel("Niveau:");
        lblNiveau.setBounds(20, 96, 60, 14);
        contentPane.add(lblNiveau);

        textFieldIdClasse = new JTextField();
        textFieldIdClasse.setBounds(75, 39, 200, 25);
        contentPane.add(textFieldIdClasse);
        textFieldIdClasse.setColumns(10);

        textFieldNiveau = new JTextField();
        textFieldNiveau.setBounds(75, 91, 200, 25);
        contentPane.add(textFieldNiveau);
        textFieldNiveau.setColumns(10);

        
        tableModel = new DefaultTableModel(new Object[]{"ID Classe", "Niveau"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(298, 23, 450, 250);
        contentPane.add(scrollPane);

        
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(124, 294, 89, 23);
        contentPane.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(434, 294, 89, 23);
        contentPane.add(btnModifier);

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(611, 294, 89, 23);
        contentPane.add(btnSupprimer);

       
        btnAjouter.addActionListener(e -> {
            String idclasse = textFieldIdClasse.getText().trim();
            String niveau = textFieldNiveau.getText().trim();

            if (idclasse.isEmpty() || niveau.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                return;
            }

            Classe c = new Classe(idclasse, niveau);
            boolean ok = ClasseDAO.ajouter(c);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Ajout réussi !");
                viderChamps();
                afficherToutesLesClasses();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout !");
            }
        });

        
        btnModifier.addActionListener(e -> {
            String idclasse = textFieldIdClasse.getText().trim();
            String niveau = textFieldNiveau.getText().trim();

            if (idclasse.isEmpty() || niveau.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une classe et remplir le niveau.");
                return;
            }

            Classe c = new Classe(idclasse, niveau);
            boolean ok = ClasseDAO.modifier(c);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Modification réussie !");
                textFieldIdClasse.setEnabled(true);
                viderChamps();
                afficherToutesLesClasses();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification !");
            }
        });

    
        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à supprimer.");
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Voulez-vous vraiment supprimer la classe " + id + " ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = ClasseDAO.supprimer(id);
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Suppression réussie !");
                    viderChamps();
                    afficherToutesLesClasses();
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression !");
                }
            }
        });

       
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                String niveau = tableModel.getValueAt(selectedRow, 1).toString();
                textFieldIdClasse.setText(id);
                textFieldIdClasse.setEnabled(false);
                textFieldNiveau.setText(niveau);
            }
        });

       
        afficherToutesLesClasses();
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
}
