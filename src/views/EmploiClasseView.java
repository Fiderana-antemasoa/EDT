package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.EmploiDuTempsDAO;
import model.Classe;
import model.EmploiDuTemps;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import dao.ClasseDAO;

// Import iText
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class EmploiClasseView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboBoxClasses;
    private EmploiDuTempsDAO dao;
    private JButton btnGenererPDF;

    public EmploiClasseView() {
        setTitle("Emploi du temps par Classe");
        setSize(900, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        dao = new EmploiDuTempsDAO();
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        initComponents();
        loadClasses();
    }

    private void initComponents() {
        // Panel supérieur pour la sélection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JLabel lblClasse = new JLabel("Sélectionnez une classe:");
        comboBoxClasses = new JComboBox<>();
        JButton btnAfficher = new JButton("Afficher");
        btnGenererPDF = new JButton("Générer PDF");
        
        btnAfficher.addActionListener(this::afficherEmploiClasse);
        btnGenererPDF.addActionListener(this::genererPDF);
        
        topPanel.add(lblClasse);
        topPanel.add(comboBoxClasses);
        topPanel.add(btnAfficher);
        topPanel.add(btnGenererPDF);
        
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Tableau pour l'affichage
        tableModel = new DefaultTableModel(
          new Object[]{"Salle", "Professeur", "Cours", "Date/Heure"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadClasses() {
        comboBoxClasses.removeAllItems();
        
        try {
            List<Classe> classes = ClasseDAO.lister();
            
            if (classes.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucune classe disponible",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Classe c : classes) {
                    comboBoxClasses.addItem(c.getIdclasse() + " - " + c.getNiveau());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur de chargement: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherEmploiClasse(ActionEvent e) {
        String selectedItem = (String) comboBoxClasses.getSelectedItem();
        if (selectedItem == null || selectedItem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe.");
            return;
        }
        
        String idClasse = selectedItem.split(" - ")[0];
        List<EmploiDuTemps> emplois = dao.getEmploiDuTempsParClasse(idClasse);
        tableModel.setRowCount(0);

        if (emplois.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun emploi du temps trouvé pour cette classe.");
            return;
        }

        SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEEE dd MMMM yyyy - HH:mm", Locale.FRENCH);
        SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (EmploiDuTemps edt : emplois) {
            try {
                java.util.Date date = sdfDB.parse(edt.getDate());
                String dateFormatee = sdfDisplay.format(date);
                
                tableModel.addRow(new Object[]{
                    edt.getIdsalle(),
                    edt.getIdprof(),
                    edt.getCours(),
                    dateFormatee
                });
            } catch (Exception ex) {
                System.err.println("Erreur de formatage de date pour: " + edt.getDate());
                tableModel.addRow(new Object[]{
                    edt.getIdsalle(),
                    edt.getIdprof(),
                    edt.getCours(),
                    edt.getDate()
                });
            }
        }
    }

    private void genererPDF(ActionEvent e) {
        String selectedItem = (String) comboBoxClasses.getSelectedItem();
        if (selectedItem == null || selectedItem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe.");
            return;
        }
        
        String[] parts = selectedItem.split(" - ");
        if (parts.length < 2) {
            JOptionPane.showMessageDialog(this, "Format de classe invalide.");
            return;
        }
        
        String idClasse = parts[0];
        String nomClasse = parts[1];
        List<EmploiDuTemps> emplois = dao.getEmploiDuTempsParClasse(idClasse);
        

        if (emplois.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun emploi du temps à exporter pour cette classe.");
            return;
        }

        Document document = new Document(PageSize.A4.rotate());
        try {
            // Création du fichier PDF
            String fileName = "EmploiDuTemps_" + nomClasse.replace(" ", "_") + "_" + 
                            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String filePath = System.getProperty("user.home") + File.separator + fileName;
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Titre du document
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Emploi du temps - " + nomClasse, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Sous-titre avec la date de génération
            Font subFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph subTitle = new Paragraph("Généré le " + 
                new SimpleDateFormat("dd/MM/yyyy à HH:mm").format(new Date()), subFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            subTitle.setSpacingAfter(15f);
            document.add(subTitle);

            // Création du tableau avec 6 colonnes (heures + 5 jours)
            PdfPTable pdfTable = new PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(10f);

            // En-têtes du tableau
            String[] jours = {"Heures", "lundi", "mardi", "mercredi", "jeudi", "vendredi"};
            
            for (String jour : jours) {
                PdfPCell cell = new PdfPCell(new Phrase(jour));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(200, 200, 200));
                cell.setPadding(8f);
                pdfTable.addCell(cell);
            }

            // Heures standard
           // String[] heures = {"08:00", "10:00", "12:00", "14:00", "16:00"};
         // Définissez vos plages horaires
            String[] heures = {"08:00-10:00", "10:00-12:00", "12:00-14:00", "14:00-16:00", "16:00-18:00"};

            // Organiser les données dans une structure plus pratique
            String[] joursSemaine = {"lundi", "mardi", "mercredi", "jeudi", "vendredi"};
            
            Map<String, Map<String, List<String>>> emploiOrganise = new LinkedHashMap<>();           
            

            // Initialisation de la structure
            for (String jour : joursSemaine) {
                emploiOrganise.put(jour, new LinkedHashMap<>());
                for (String plage : heures) {
                    emploiOrganise.get(jour).put(plage, new ArrayList<>());
                }
            }
         // Remplissage avec les données réelles
            SimpleDateFormat sdfJour = new SimpleDateFormat("EEEE", new Locale("fr", "FR")); // Français de France
            SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            

            for (EmploiDuTemps edt : emplois) {
            
                try {
                    Date date = sdfDB.parse(edt.getDate());
                    String jour = sdfJour.format(date).toLowerCase();
                    String heure = sdfHeure.format(date);
                    
                 // Dans la méthode de génération PDF
                    if (Arrays.asList("samedi", "dimanche").contains(jour)) continue;
                    String plage = trouverPlageHoraire(heure, heures);
                	
                    // Vérifie si le jour existe dans notre structure
                    if (plage != null) {
                        String info = edt.getCours() + " (" + edt.getIdprof() + ", " + edt.getIdsalle() + ")";
                        emploiOrganise.get(jour).get(plage).add(info);
                    }
                } catch (Exception ex) {
                    System.err.println("Erreur de parsing de date: " + edt.getDate());
                    ex.printStackTrace();
                }
            }
            
            

         // Remplissage du tableau PDF
            for (String heure : heures) {
                // Cellule heure
                PdfPCell cellHeure = new PdfPCell(new Phrase(heure));
                cellHeure.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellHeure.setPadding(8f);
                pdfTable.addCell(cellHeure);
                
                // Cellules pour chaque jour
                for (String jour : joursSemaine) {
                	List<String> coursList = emploiOrganise.get(jour).get(heure);
                    
                    
                    
                    // Création du contenu détaillé
                    Paragraph details = new Paragraph();
                    
                    if (!coursList.isEmpty()) {
                        for (int i = 0; i < coursList.size(); i++) {
                            if (i > 0) details.add(new Phrase("\n\n")); // Double saut de ligne entre cours
                            
                            String[] elements = coursList.get(i).split("\\(")[1].replace(")", "").split(",");
                            String matiere = coursList.get(i).split("\\(")[0].trim();
                            
                            details.add(new Phrase(matiere + "\n", 
                                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                            details.add(new Phrase("Prof: " + elements[0].trim() + "\n",
                                new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL)));
                            details.add(new Phrase("Salle: " + elements[1].trim(),
                                new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL)));
                        }
                    } else {
                        details.add(new Phrase("-"));
                    }
                    
                    PdfPCell cell = new PdfPCell(details);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(50f);
                    cell.setPadding(5f);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(this, 
                "PDF généré avec succès:\n" + filePath,
                "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la génération du PDF:\n" + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
           
    private String trouverPlageHoraire(String heure, String[] plagesHoraires) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date heureCourante = sdf.parse(heure);
            
            for (String plage : plagesHoraires) {
                String[] limites = plage.split("-");
                Date debut = sdf.parse(limites[0]);
                Date fin = sdf.parse(limites[1]);
                
                if (!heureCourante.before(debut) && heureCourante.before(fin)) {
                    return plage;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EmploiClasseView().setVisible(true);
        });
    }
}