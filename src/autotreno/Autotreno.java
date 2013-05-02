/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotreno;

import common.IAutotreno;
import common.IBase;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author marco
 */
public class Autotreno extends UnicastRemoteObject implements IAutotreno {
    private String nomeAutotreno;
    private String nomeBasePartenza;
    private IBase basePartenza;
    private IBase baseDestinazione;
    
    private AutotrenoGUI gui;
    
    Autotreno(String nomeAutotreno, String nomeBase, AutotrenoGUI gui) 
            throws RemoteException {
        this.nomeAutotreno = nomeAutotreno;
        this.nomeBasePartenza = nomeBase;
        this.gui = gui;
    }
    
    void setBasePartenza(IBase partenza) {
        this.basePartenza = partenza;
        try{
            gui.setDestinazioneTextField(basePartenza.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di partenza "
                    + "dell'autotreno " + getNomeAutotreno() + ".");
        }
    }
    
    IBase getBasePartenza() {
        return basePartenza;
    }
    
    void setBaseDestinazione(IBase destinazione) {
        this.baseDestinazione = destinazione;
        try{
            gui.setPartenzaTextField(baseDestinazione.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di partenza "
                    + "dell'autotreno " + getNomeAutotreno() + ".");
        }
    }
    
    IBase getBaseDestinazione() {
        return baseDestinazione;
    }
    
    @Override
    public String getNomeAutotreno() {
        return nomeAutotreno;
    }
    
    @Override
    public synchronized void consegnaOrdine(IBase destinazione) {
        try {
            if(destinazione.stato()){
                this.setBaseDestinazione(destinazione);
                int durata = (int) Math.random()*1000;
                gui.inizializzaViaggioProgressBar(0, durata);
                for(int n = 0; n <= durata; n++) {
                    gui.aggiornaViaggioProgressBar(n);
                }
                baseDestinazione.riceviMerce(this);
            }
        } catch (RemoteException e) {
            System.out.println("Errore di comunicazione con la base di "
                    + "destinazione; impossibile evadere l'ordine.");
        }
        
    }
    
    @Override
    public boolean stato() {
        return true;
    }
    
    @Override
    public void terminaAttivita() {
        
    } 

}
