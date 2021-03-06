/*
 * Copyright (c) 2013 Pezzutti Marco
 * 
 * This file is part of Sistema Trasporti Artici (progetto per l'insegnamento 
 * di Programmazione Concorrente e Distribuita).

 * Sistema Trasporti Artici is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sistema Trasporti Artici is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sistema Trasporti Artici.  If not, see <http://www.gnu.org/licenses/>.
 */
package autotreno;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Interfaccia grafica di un autotreno
 * 
 * @author Pezzutti Marco 1008804
 */
public class AutotrenoGUI extends javax.swing.JFrame implements Runnable, PropertyChangeListener {
    Autotreno autotreno;

    /**
     * Creates new form AutotrenoGUI
     */
    AutotrenoGUI(String nome) {
        initComponents();
        this.setTitle(nome);
    }
    
    /**
     * Metodo che imposta l'autotreno proprio della GUI
     * @param autotreno 
     */
    void setAutotreno(Autotreno autotreno) {
        this.autotreno = autotreno;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        terminaAttivitaButton = new javax.swing.JButton();
        infoAutotrenoPanel = new javax.swing.JPanel();
        basePartenzaTextField = new javax.swing.JTextField();
        basePartenzaLabel = new javax.swing.JLabel();
        durataViaggioLabel = new javax.swing.JLabel();
        baseDestinazioneLabel = new javax.swing.JLabel();
        durataViaggioProgressBar = new javax.swing.JProgressBar();
        baseDestinazioneTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        terminaAttivitaButton.setText("Termina Attività");
        terminaAttivitaButton.setToolTipText("");
        terminaAttivitaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaAttivitaButtonActionPerformed(evt);
            }
        });

        infoAutotrenoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informazioni di Viaggio"));
        infoAutotrenoPanel.setName(""); // NOI18N

        basePartenzaTextField.setToolTipText("");
        basePartenzaTextField.setEnabled(false);

        basePartenzaLabel.setText("Base di Partenza");

        durataViaggioLabel.setText("Durata viaggio");

        baseDestinazioneLabel.setText("Base di Destinazione");

        baseDestinazioneTextField.setEnabled(false);

        javax.swing.GroupLayout infoAutotrenoPanelLayout = new javax.swing.GroupLayout(infoAutotrenoPanel);
        infoAutotrenoPanel.setLayout(infoAutotrenoPanelLayout);
        infoAutotrenoPanelLayout.setHorizontalGroup(
            infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoAutotrenoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(baseDestinazioneLabel)
                    .addComponent(basePartenzaLabel)
                    .addComponent(durataViaggioLabel))
                .addGap(18, 18, 18)
                .addGroup(infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(durataViaggioProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(basePartenzaTextField)
                    .addComponent(baseDestinazioneTextField))
                .addContainerGap())
        );
        infoAutotrenoPanelLayout.setVerticalGroup(
            infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoAutotrenoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(basePartenzaLabel)
                    .addComponent(basePartenzaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseDestinazioneLabel)
                    .addComponent(baseDestinazioneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(infoAutotrenoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(durataViaggioLabel)
                    .addComponent(durataViaggioProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoAutotrenoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(terminaAttivitaButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoAutotrenoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(terminaAttivitaButton)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo invocato alla pressione del pulsante Termina Attività che avvisa 
     * l'autotreno di cessare la propria attività
     * @param evt                   riferimento all'evento generato dal pulsante
     */
    private void terminaAttivitaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaAttivitaButtonActionPerformed
        autotreno.terminaAttivita();
    }//GEN-LAST:event_terminaAttivitaButtonActionPerformed

    /**
     * Metodo chiamato dall'autotreno per impostare la base di destinazione
     * 
     * @param base                  nome della base di destinazione
     */
    void setDestinazioneTextField(String base) {
        baseDestinazioneTextField.setText(base);
    }
    
    /**
     * Metodo chiamato dall'autotreno per impostare la base di partenza
     * 
     * @param base                  nome della base di partenza
     */
    void setPartenzaTextField(String base) {
        basePartenzaTextField.setText(base);
    }
    
    /**
     * Metodo chiamato dall'autotreno che imposta lo stato del pulsante Termina Attività,
     * se il valore è true lo attiva, altrimenti lo disattiva
     * 
     * @param stato                 booleano che indica lo stato del pulsante
     */
    void setStatoTerminaAttivitaButton(boolean stato) {
        terminaAttivitaButton.setEnabled(stato);
    }
    
    /**
     * Metodo che rende visibile l'interfaccia grafica
     */
    @Override
    public void run() {
        this.setVisible(true);
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel baseDestinazioneLabel;
    private javax.swing.JTextField baseDestinazioneTextField;
    private javax.swing.JLabel basePartenzaLabel;
    private javax.swing.JTextField basePartenzaTextField;
    private javax.swing.JLabel durataViaggioLabel;
    private javax.swing.JProgressBar durataViaggioProgressBar;
    private javax.swing.JPanel infoAutotrenoPanel;
    private javax.swing.JButton terminaAttivitaButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo che aggiorna la barra di progesso ad ogni modifica effettuata 
     * dallo SwingWorker Viaggio
     * 
     * @param pce                   riferimento alla proprietà modificata
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if ("progress" == pce.getPropertyName()) {
            int progress = (Integer) pce.getNewValue();
            durataViaggioProgressBar.setValue(progress);
        } 
    }
}
