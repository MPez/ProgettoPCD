/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author marco
 */
public class Ditta implements IDitta{
    private LinkedList<IBase> basiAttive;
    private HashMap<String, IBase> nomiBasi;
    private HashMap<IBase, String> basiNomi;
    private LinkedList<IAutotreno> autotreniAttivi;
    private HashMap<String, IAutotreno> nomiAutotreni;
    private HashMap<IAutotreno, String> autotreniNomi;
    
    private DittaGUI gui;
    
    private boolean terminato;
    
    Ditta(DittaGUI gui) {
        this.gui = gui;
        terminato = false;
    }

    @Override
    public void registraBase(IBase base) {
        synchronized(basiAttive) {
            basiAttive.add(base);
        }
        synchronized(nomiBasi) {
            try {
                nomiBasi.put(base.getNomeBase(), base);
                nomiBasi.notify();
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con una base in fase "
                        + "di registrazione.");
            }
        }
        synchronized(basiNomi) {
            try {
                basiNomi.put(base, base.getNomeBase());
            } catch(RemoteException e) {
                System.out.println("Errore di connessione con una base in fase "
                        + "di registrazione.");
            }
        }
    }

    @Override
    public IBase registraAutotreno(IAutotreno autotreno, String nomeBasePartenza) {
        IBase partenza = null;
        try {
            synchronized(nomiBasi) {
                while(!nomiBasi.containsKey(nomeBasePartenza)) {
                    nomiBasi.wait();
                }
                synchronized(autotreniAttivi) {
                    autotreniAttivi.add(autotreno);
                }
                partenza = nomiBasi.get(nomeBasePartenza);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(nomiAutotreni) {
            try {
                nomiAutotreni.put(autotreno.getNomeAutotreno(), autotreno);
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un autotreno in "
                        + "fase di registrazione.");
            }
        }
        synchronized(autotreniNomi) {
            try {
                autotreniNomi.put(autotreno, autotreno.getNomeAutotreno());
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un autotreno in "
                        + "fase di registrazione.");
            }
        }
        return partenza;
    }

    @Override
    public void notificaEsito(IBase partenza, IBase destinazione, IAutotreno autotreno, boolean esito) {
        String text = "";
        if(!esito) {
            text = "NON ";
        }
        gui.aggiornaStatoTextArea("Ordine proveniente da " 
                + basiNomi.get(partenza) + " e diretto verso "
                + basiNomi.get(destinazione) + " " + text 
                + "è stato consegnato da "
                + autotreniNomi.get(autotreno) + ".");
    }

    @Override
    public void aggiornaBasiAttive(IBase base) throws RemoteException {
        synchronized(basiAttive) {
            basiAttive.remove(base);
        }
        gui.aggiornaStatoTextArea("Una base non è più attiva.");
    }

    @Override
    public void aggiornaAutotreniAttivi(IAutotreno autotreno) throws RemoteException {
        synchronized(autotreniAttivi) {
            autotreniAttivi.remove(autotreno);
        }
        gui.aggiornaStatoTextArea("Un autotreno non è più attivo.");
    }
    
    class Ordine {
        private IBase partenza;
        private IBase destinazione;
        private int quantita;
        
        Ordine (IBase partenza, IBase destinazione, int quantita) {
            this.partenza = partenza;
            this.destinazione = destinazione;
            this.quantita = quantita;
        }
        
        IBase getBasePartenza() {
            return partenza;
        }
        
        IBase getBaseDestinazione() {
            return destinazione;
        }
        
        int getQuantita() {
            return quantita;
        }
    }
    
    class InviaOrdini implements Runnable {
        private Ordine ordine;

        public InviaOrdini(Ordine ordine) {
            this.ordine = ordine;
        }
        
        @Override
        public void run() {
            
        }
    }
}
