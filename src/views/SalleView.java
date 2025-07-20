package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.SalleDAO;
import model.Salle;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SalleView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldDesign;
    private JTable table;
    private JComboBox<String> comboBoxOccupation;
    private DefaultTableModel tableModel;

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
        setTitle("Gestion des Salles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 801, 459);

        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

       
        JMenu mnProfesseur = new JMenu("Professeur");
        mnProfesseur.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new ProfesseurView().setVisible(true);
                dispose();
            }
            public void menuDeselected(MenuEvent e) {}
            public void menuCanceled(MenuEvent e) {}
        });
        menuBar.add(mnProfesseur);

      
        JMenu mnClasse = new JMenu("Classe");
        mnClasse.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new ClasseView().setVisible(true);
                dispose();
            }
            public void menuDeselected(MenuEvent e) {}
            public void menuCanceled(MenuEvent e) {}
        });
        menuBar.add(mnClasse);

       
        JMenu mnSalle = new JMenu("Salle");
        mnSalle.setEnabled(false);
        menuBar.add(mnSalle);
        
        JMenu mnEmloiDuTemps = new JMenu("Emloi du temps");
        menuBar.add(mnEmloiDuTemps);

        

       
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblDesign = new JLabel("Design:");
        lblDesign.setBounds(32, 79, 46, 14);
        contentPane.add(lblDesign);

        textFieldDesign = new JTextField();
        textFieldDesign.setBounds(88, 74, 200, 25);
        contentPane.add(textFieldDesign);
        textFieldDesign.setColumns(10);

        JLabel lblOccupation = new JLabel("Occupation:");
        lblOccupation.setBounds(10, 133, 68, 20);
        contentPane.add(lblOccupation);

        comboBoxOccupation = new JComboBox<>(new String[]{"Libre", "Occupée"});
        comboBoxOccupation.setBounds(88, 131, 200, 25);
        contentPane.add(comboBoxOccupation);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Design", "Occupation"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(298, 23, 450, 250);
        contentPane.add(scrollPane);

        
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(161, 314, 89, 23);
        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String design = textFieldDesign.getText();
                String occupation = comboBoxOccupation.getSelectedItem().toString();

                Salle salle = new Salle(0, design, occupation);
                boolean ok = SalleDAO.ajouter(salle);
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Ajout réussi !");
                    textFieldDesign.setText("");
                    comboBoxOccupation.setSelectedIndex(0);
                    afficherSalles();
                } else {
                    JOptionPane.showMessageDialog(null, "Échec de l'ajout !");
                }
            }
        });
        contentPane.add(btnAjouter);

       
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(343, 314, 89, 23);
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String design = textFieldDesign.getText();
                    String occupation = comboBoxOccupation.getSelectedItem().toString();

                    Salle salle = new Salle(id, design, occupation);
                    if (SalleDAO.modifier(salle)) {
                        JOptionPane.showMessageDialog(null, "Modification réussie !");
                        afficherSalles();
                    } else {
                        JOptionPane.showMessageDialog(null, "Échec de la modification !");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne !");
                }
            }
        });
        contentPane.add(btnModifier);

        
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(550, 314, 89, 23);
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, "Confirmer la suppression ?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (SalleDAO.supprimer(id)) {
                            JOptionPane.showMessageDialog(null, "Suppression réussie !");
                            afficherSalles();
                        } else {
                            JOptionPane.showMessageDialog(null, "Échec de la suppression !");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne !");
                }
            }
        });
        contentPane.add(btnSupprimer);

       
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                textFieldDesign.setText(tableModel.getValueAt(selectedRow, 1).toString());
                comboBoxOccupation.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });

        setLocationRelativeTo(null);
        afficherSalles();
    }

    private void afficherSalles() {
        tableModel.setRowCount(0);
        List<Salle> salles = SalleDAO.lister();
        for (Salle s : salles) {
            tableModel.addRow(new Object[]{s.getIdsalle(), s.getDesign(), s.getOccupation()});
        }
    }
}
