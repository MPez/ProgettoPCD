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
    private IBase basePartenza;
    private IBase baseDestinazione;
    
    private AutotrenoGUI gui;
    
    Autotreno(String nomeAutotreno, AutotrenoGUI gui) throws RemoteException {
        this.nomeAutotreno = nomeAutotreno;
        this.gui = gui;
    }
    
    void setBasePartenza(IBase partenza) {
        this.basePartenza = partenza;
        try{
            gui.setPartenzaTextField(basePartenza.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di partenza "
                    + "dell'autotreno " + getNomeAutotreno());
        }
    }
    
    IBase getBasePartenza() {
        return basePartenza;
    }
    
    void setBaseDestinazione(IBase destinazione) {
        this.baseDestinazione = destinazione;
        try{
            gui.setDestinazioneTextField(baseDestinazione.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di destinazione "
                    + "dell'autotreno " + getNomeAutotreno());
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
    public void consegnaOrdine(IBase destinazione) {
        try {
            //controllo che la base di destinazione sia attiva
            if(destinazione.stato()){
                this.setBaseDestinazione(destinazione);
                int durata = (int) (Math.random())*10000;
                gui.inizializzaViaggioProgressBar(0, durata);
                for(int n = 0; n <= durata; n++) {
                    gui.aggiornaViaggioProgressBar(n);
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                baseDestinazione.riceviMerce(basePartenza,this);
            }
        } catch (RemoteException e) {
            System.out.println("Errore di comunicazione con la base di "
                    + "destinazione; impossibile evadere l'ordine");
        }
    }
    
    @Override
    public void parcheggiaAutotreno(IBase destinazione) {
        basePartenza = destinazione;
        setBasePartenza(basePartenza);
        gui.setDestinazioneTextField("");
    }
    
    @Override
    public boolean stato() {
        return true;
    }
    
    @Override
    public void terminaAttivita() {
        gui.dispose();
        System.exit(0);
    } 
}
