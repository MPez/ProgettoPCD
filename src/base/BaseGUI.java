/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

/**
 *
 * @author marco
 */
public class BaseGUI extends javax.swing.JFrame implements Runnable {
    private Base base;

    /**
     * Creates new form baseGUI
     */
    public BaseGUI(String nomeBase) {
        initComponents();
        this.setTitle(nomeBase);
    }
    
    void setBase(Base base) {
        this.base = base;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statoPanel = new javax.swing.JPanel();
        statoScrollPanel = new javax.swing.JScrollPane();
        statoTextArea = new javax.swing.JTextArea();
        terminaAttivitaButton = new javax.swing.JButton();
        informazioneBasePanel = new javax.swing.JPanel();
        ordiniLabel = new javax.swing.JLabel();
        autotreniParcheggiatiLabel = new javax.swing.JLabel();
        ordiniScrollPane = new javax.swing.JScrollPane();
        ordiniTextArea = new javax.swing.JTextArea();
        autotreniScrollPane = new javax.swing.JScrollPane();
        autotreniTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(400, 595));
        setResizable(false);

        statoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Stato"));

        statoTextArea.setColumns(20);
        statoTextArea.setRows(5);
        statoTextArea.setEnabled(false);
        statoScrollPanel.setViewportView(statoTextArea);

        javax.swing.GroupLayout statoPanelLayout = new javax.swing.GroupLayout(statoPanel);
        statoPanel.setLayout(statoPanelLayout);
        statoPanelLayout.setHorizontalGroup(
            statoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statoScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );
        statoPanelLayout.setVerticalGroup(
            statoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statoScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );

        terminaAttivitaButton.setText("Termina Attività");
        terminaAttivitaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminaAttivitaButtonActionPerformed(evt);
            }
        });

        informazioneBasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informazioni Base"));

        ordiniLabel.setText("Ordini da evadere");

        autotreniParcheggiatiLabel.setText("Autotreni parcheggiati");

        ordiniTextArea.setColumns(20);
        ordiniTextArea.setRows(5);
        ordiniScrollPane.setViewportView(ordiniTextArea);

        autotreniTextArea.setColumns(20);
        autotreniTextArea.setRows(5);
        autotreniScrollPane.setViewportView(autotreniTextArea);

        javax.swing.GroupLayout informazioneBasePanelLayout = new javax.swing.GroupLayout(informazioneBasePanel);
        informazioneBasePanel.setLayout(informazioneBasePanelLayout);
        informazioneBasePanelLayout.setHorizontalGroup(
            informazioneBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informazioneBasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(informazioneBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ordiniScrollPane)
                    .addGroup(informazioneBasePanelLayout.createSequentialGroup()
                        .addGroup(informazioneBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ordiniLabel)
                            .addComponent(autotreniParcheggiatiLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(autotreniScrollPane))
                .addContainerGap())
        );
        informazioneBasePanelLayout.setVerticalGroup(
            informazioneBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informazioneBasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ordiniLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ordiniScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotreniParcheggiatiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotreniScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(informazioneBasePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(terminaAttivitaButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(informazioneBasePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(terminaAttivitaButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void terminaAttivitaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminaAttivitaButtonActionPerformed
        base.terminaAttività();
    }//GEN-LAST:event_terminaAttivitaButtonActionPerformed


    @Override
    public void run() {
        this.setVisible(true);
    }
    
    void setOrdiniTextArea(String text) {
        ordiniTextArea.setText(text);
    }
    
    void setAutotreniTextArea(String text) {
        autotreniTextArea.setText(text);
    }
    
    void aggiornaStatoTextArea(String text) {
        statoTextArea.append(text + ".\n");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel autotreniParcheggiatiLabel;
    private javax.swing.JScrollPane autotreniScrollPane;
    private javax.swing.JTextArea autotreniTextArea;
    private javax.swing.JPanel informazioneBasePanel;
    private javax.swing.JLabel ordiniLabel;
    private javax.swing.JScrollPane ordiniScrollPane;
    private javax.swing.JTextArea ordiniTextArea;
    private javax.swing.JPanel statoPanel;
    private javax.swing.JScrollPane statoScrollPanel;
    private javax.swing.JTextArea statoTextArea;
    private javax.swing.JButton terminaAttivitaButton;
    // End of variables declaration//GEN-END:variables
}
