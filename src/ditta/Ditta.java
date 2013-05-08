/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author marco
 */
public class Ditta extends UnicastRemoteObject implements IDitta{
    private LinkedList<IBase> basiAttive;
    private HashMap<String, IBase> nomiBasi;
    private HashMap<IBase, String> basiNomi;
    private LinkedList<IAutotreno> autotreniAttivi;
    private HashMap<String, IAutotreno> nomiAutotreni;
    private HashMap<IAutotreno, String> autotreniNomi;
    private LinkedList<Ordine> elencoOrdini;
    
    private DittaGUI gui;
    
    private boolean terminato;
    
    Ditta(DittaGUI gui) throws RemoteException {
        basiAttive = new LinkedList<>();
        nomiBasi = new HashMap<>();
        basiNomi = new HashMap<>();
        autotreniAttivi = new LinkedList<>();
        nomiAutotreni = new HashMap<>();
        autotreniNomi = new HashMap<>();
        elencoOrdini = new LinkedList<>();
        
        this.gui = gui;
        terminato = false;
    }

    void inserisciOrdine(String partenza, String destinazione, int quantita) {
        IBase basePartenza;
        IBase baseDestinazione;
        basePartenza = nomiBasi.get(partenza);
        baseDestinazione = nomiBasi.get(destinazione);
        
        synchronized(elencoOrdini) {
            elencoOrdini.add(this.new Ordine(basePartenza, baseDestinazione, quantita));
            elencoOrdini.notify();
        }
        gui.aggiornaStatoTextArea("Ricevuto ordine da " + partenza + " a "
                + destinazione + " da eseguirsi n°" + quantita + " volta/e.");
    }
    
    void terminaAttivita() {
        terminato = true;
        gui.dispose();
        try {
            for(IBase base : basiAttive) {
                base.terminaAttività();
            }
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con una base in fase di chiusura.");
        }
        try {
            for(IAutotreno autotreno : autotreniAttivi) {
                autotreno.terminaAttivita();
            }
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con un autotreno in fase di chiusura.");
        }
        System.exit(0);
    }
    
    @Override
    public void registraBase(IBase base) {
        synchronized(basiAttive) {
            basiAttive.add(base);
        }
        synchronized(nomiBasi) {
            try {
                nomiBasi.put(base.getNomeBase(), base);
                nomiBasi.notifyAll();
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
        gui.aggiungiBaseComboBox(basiNomi.get(base));
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
                + " è stato consegnato da "
                + autotreniNomi.get(autotreno) + ".");
    }

    @Override
    public void aggiornaBasiAttive(IBase base) throws RemoteException {
        rimuoviBase(base);
        gui.aggiornaStatoTextArea("La base " + basiNomi.get(base)
                + " non è più attiva.");
    }
    
    private void rimuoviBase(IBase base) {
        synchronized(basiAttive) {
            basiAttive.remove(base);
        }
        gui.rimuoviBaseComboBox(basiNomi.get(base));
    }

    @Override
    public void aggiornaAutotreniAttivi(IAutotreno autotreno) throws RemoteException {
        rimuoviAutotreno(autotreno);
        gui.aggiornaStatoTextArea("L'autotreno " + autotreniNomi.get(autotreno)
                + " non è più attivo.");
    }
    
    private void rimuoviAutotreno(IAutotreno autotreno) {
        synchronized(autotreniAttivi) {
            autotreniAttivi.remove(autotreno);
        }
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
        private IBase partenza;
        private IBase destinazione;
        private int quantita;
        private Ordine ordine;
        
        @Override
        public void run() {
            while(!terminato) {
                try {
                    synchronized(elencoOrdini) {
                        while(!terminato && elencoOrdini.isEmpty()) {
                            elencoOrdini.wait();
                        }
                        ordine = elencoOrdini.poll();
                        partenza = ordine.getBasePartenza();
                        destinazione = ordine.getBaseDestinazione();
                        quantita = ordine.getQuantita();
                        try {
                            for(int i = 0; i < quantita; i++) {
                                partenza.registraOrdine(destinazione);
                            }
                        } catch(RemoteException e) {
                            System.out.println("Errore di comunicazione con la base "
                                    + basiNomi.get(partenza) + ".");
                        } finally {
                            rimuoviBase(partenza);
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
