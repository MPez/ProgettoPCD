/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package ditta;

import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author marco
 */
public class DittaGUI extends javax.swing.JFrame implements Runnable {
    Ditta ditta;
    
    private Thread creaOrdini;
    private boolean creaOrdiniAttivo;

    /**
     * Creates new form DittaGUI
     */
    DittaGUI() {
        initComponents();
        statoTextArea.setLineWrap(true);
        
        creaOrdiniAttivo = false;
        
        DefaultCaret  caret;
        //imposto l'autoscrolling di statoTextArea
        caret = (DefaultCaret) statoTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //imposto l'autoscrolling di ordiniInseritiTextArea
        caret = (DefaultCaret) ordiniInseritiTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
    }

    void setDitta(Ditta ditta) {
        this.ditta = ditta;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inserimentoOrdiniPanel = new javax.swing.JPanel();
        basePartenzaLabel = new javax.swing.JLabel();
        baseDestinazioneLabel = new javax.swing.JLabel();
        basePartenzaComboBox = new javax.swing.JComboBox();
        baseDestinazioneComboBox = new javax.swing.JComboBox();
        quantitaOrdiniLabel = new javax.swing.JLabel();
        quantitaOrdiniSpinner = new javax.swing.JSpinner();
        inviaOrdineButton = new javax.swing.JButton();
        creaOrdiniButton = new javax.swing.JButton();
        statoPanel = new javax.swing.JPanel();
        statoScrollPane = new javax.swing.JScrollPane();
        statoTextArea = new javax.swing.JTextArea();
        terminaAttivitaButton = new javax.swing.JButton();
        ordiniInseritiPanel = new javax.swing.JPanel();
        ordiniInseritiScrollPane = new javax.swing.JScrollPane();
        ordiniInseritiTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema Trasporti Artici");
        setResizable(false);

        inserimentoOrdiniPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Inserimento Ordini"));

        basePartenzaLabel.setText("Base Partenza");

        baseDestinazioneLabel.setText("Base Destinazione");

        quantitaOrdiniLabel.setText("Quantità di ordini");

        quantitaOrdiniSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));

        inviaOrdineButton.setText("Invia Ordine");
        inviaOrdineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inviaOrdineButtonActionPerformed(evt);
            }
        });

        creaOrdiniButton.setText("Auto");
        creaOrdiniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creaOrdiniButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inserimentoOrdiniPanelLayout = new javax.swing.GroupLayout(inserimentoOrdiniPanel);
        inserimentoOrdiniPanel.setLayout(inserimentoOrdiniPanelLayout);
        inserimentoOrdiniPanelLayout.setHorizontalGroup(
            inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inserimentoOrdiniPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inserimentoOrdiniPanelLayout.createSequentialGroup()
                        .addComponent(creaOrdiniButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inviaOrdineButton))
                    .addGroup(inserimentoOrdiniPanelLayout.createSequentialGroup()
                        .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(baseDestinazioneLabel)
                            .addComponent(basePartenzaLabel)
                            .addComponent(quantitaOrdiniLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantitaOrdiniSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(basePartenzaComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(baseDestinazioneComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        inserimentoOrdiniPanelLayout.setVerticalGroup(
            inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inserimentoOrdiniPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(basePartenzaLabel)
                    .addComponent(basePartenzaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseDestinazioneLabel)
                    .addComponent(baseDestinazioneComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quantitaOrdiniLabel)
                    .addComponent(quantitaOrdiniSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inviaOrdineButton)
                    .addComponent(creaOrdiniButton))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        statoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Stato"));

        statoTextArea.setColumns(20);
        statoTextArea.setRows(5);
        statoTextArea.setEnabled(false);
        statoScrollPane.setViewportView(statoTextArea);

        javax.swing.GroupLayout statoPanelLayout = new javax.swing.GroupLayout(statoPanel);
        statoPanel.setLayout(statoPanelLayout);
        statoPanelLayout.setHorizontalGroup(
            statoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statoScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );
        statoPanelLayout.setVerticalGroup(
            statoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statoScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
        );

        terminaAttivitaButton.setText("Termina Attività");
        terminaAttivitaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaAttivitaButtonActionPerformed(evt);
            }
        });

        ordiniInseritiPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordini Inseriti"));

        ordiniInseritiTextArea.setColumns(20);
        ordiniInseritiTextArea.setRows(5);
        ordiniInseritiTextArea.setEnabled(false);
        ordiniInseritiScrollPane.setViewportView(ordiniInseritiTextArea);

        javax.swing.GroupLayout ordiniInseritiPanelLayout = new javax.swing.GroupLayout(ordiniInseritiPanel);
        ordiniInseritiPanel.setLayout(ordiniInseritiPanelLayout);
        ordiniInseritiPanelLayout.setHorizontalGroup(
            ordiniInseritiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ordiniInseritiScrollPane)
        );
        ordiniInseritiPanelLayout.setVerticalGroup(
            ordiniInseritiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ordiniInseritiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ordiniInseritiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inserimentoOrdiniPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(terminaAttivitaButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inserimentoOrdiniPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ordiniInseritiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(terminaAttivitaButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inviaOrdineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviaOrdineButtonActionPerformed
        String partenza = (String) basePartenzaComboBox.getSelectedItem();
        String destinazione = (String) baseDestinazioneComboBox.getSelectedItem();
        int quantita = (Integer) quantitaOrdiniSpinner.getValue();
        
        if(!partenza.equals(destinazione)) {
            ditta.inserisciOrdine(partenza, destinazione, quantita);
        }
        else {
            JOptionPane.showMessageDialog(this, "La base di partenza e la base di "
                    + "destinazione devono essere diverse", "Attenzione", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_inviaOrdineButtonActionPerformed

    private void terminaAttivitaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaAttivitaButtonActionPerformed
        ditta.terminaAttivita();
    }//GEN-LAST:event_terminaAttivitaButtonActionPerformed

    //metodo che crea e avvia un thread che crea ordini automaticamente
    private void creaOrdiniButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creaOrdiniButtonActionPerformed
        if(!creaOrdiniAttivo) {
            creaOrdini = ditta.avviaCreaOrdini();
            creaOrdini.start();
            creaOrdiniButton.setText("Stop");
            creaOrdiniAttivo = true;
        }
        else {
            creaOrdini.interrupt();
            creaOrdiniButton.setText("Auto");
            creaOrdiniAttivo = false;
        }
    }//GEN-LAST:event_creaOrdiniButtonActionPerformed

    @Override
    public void run() {
        this.setVisible(true);
    }
    
    void aggiungiBaseComboBox(String base) {
        baseDestinazioneComboBox.addItem(base);
        basePartenzaComboBox.addItem(base);
    }
    
    void rimuoviBaseComboBox(String base) {
        baseDestinazioneComboBox.removeItem(base);
        basePartenzaComboBox.removeItem(base);
    }
    
    void aggiornaStatoTextArea(String text) {
        statoTextArea.append(text + "\n");
    }
    
    void aggiornaOrdiniTextArea(String text) {
        ordiniInseritiTextArea.setText(text + "\n");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baseDestinazioneComboBox;
    private javax.swing.JLabel baseDestinazioneLabel;
    private javax.swing.JComboBox basePartenzaComboBox;
    private javax.swing.JLabel basePartenzaLabel;
    private javax.swing.JButton creaOrdiniButton;
    private javax.swing.JPanel inserimentoOrdiniPanel;
    private javax.swing.JButton inviaOrdineButton;
    private javax.swing.JPanel ordiniInseritiPanel;
    private javax.swing.JScrollPane ordiniInseritiScrollPane;
    private javax.swing.JTextArea ordiniInseritiTextArea;
    private javax.swing.JLabel quantitaOrdiniLabel;
    private javax.swing.JSpinner quantitaOrdiniSpinner;
    private javax.swing.JPanel statoPanel;
    private javax.swing.JScrollPane statoScrollPane;
    private javax.swing.JTextArea statoTextArea;
    private javax.swing.JButton terminaAttivitaButton;
    // End of variables declaration//GEN-END:variables
}