/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

/**
 *
 * @author marco
 */
public class DittaGUI extends javax.swing.JFrame implements Runnable {
    Ditta ditta;

    /**
     * Creates new form DittaGUI
     */
    public DittaGUI() {
        initComponents();
        
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
        statoPanel = new javax.swing.JPanel();
        statoScrollPane = new javax.swing.JScrollPane();
        statoTextArea = new javax.swing.JTextArea();
        terminaAttivitaButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema Trasporti Artici");
        setResizable(false);

        inserimentoOrdiniPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Inserimento Ordini"));

        basePartenzaLabel.setText("Base Partenza");

        baseDestinazioneLabel.setText("Base Destinazione");

        quantitaOrdiniLabel.setText("Quantità di ordini");

        quantitaOrdiniSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));

        inviaOrdineButton.setText("Invia Ordine");

        javax.swing.GroupLayout inserimentoOrdiniPanelLayout = new javax.swing.GroupLayout(inserimentoOrdiniPanel);
        inserimentoOrdiniPanel.setLayout(inserimentoOrdiniPanelLayout);
        inserimentoOrdiniPanelLayout.setHorizontalGroup(
            inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inserimentoOrdiniPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inserimentoOrdiniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inserimentoOrdiniPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
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
                .addComponent(inviaOrdineButton)
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
            .addComponent(statoScrollPane)
        );
        statoPanelLayout.setVerticalGroup(
            statoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statoScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
        );

        terminaAttivitaButton.setText("Termina Attività");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statoPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inserimentoOrdiniPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                .addComponent(statoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(terminaAttivitaButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    @Override
    public void run() {
        this.setVisible(true);
    }
    
    void addBaseComboBox(String base) {
        baseDestinazioneComboBox.addItem(base);
        basePartenzaComboBox.addItem(base);
    }
    
    void removeBaseComboBox(String base) {
        baseDestinazioneComboBox.removeItem(base);
        basePartenzaComboBox.removeItem(base);
    }
    
    void aggiornaStatoTextArea(String text) {
        statoTextArea.append(text + "\n");
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baseDestinazioneComboBox;
    private javax.swing.JLabel baseDestinazioneLabel;
    private javax.swing.JComboBox basePartenzaComboBox;
    private javax.swing.JLabel basePartenzaLabel;
    private javax.swing.JPanel inserimentoOrdiniPanel;
    private javax.swing.JButton inviaOrdineButton;
    private javax.swing.JLabel quantitaOrdiniLabel;
    private javax.swing.JSpinner quantitaOrdiniSpinner;
    private javax.swing.JPanel statoPanel;
    private javax.swing.JScrollPane statoScrollPane;
    private javax.swing.JTextArea statoTextArea;
    private javax.swing.JButton terminaAttivitaButton;
    // End of variables declaration//GEN-END:variables
}
