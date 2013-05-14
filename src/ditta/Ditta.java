/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import common.IOrdine;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
    
    private static final String HOST = "localhost:";
    
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
            try {
                for(int i = 0; i < quantita; i++) {
                    elencoOrdini.add(new Ordine(basePartenza, baseDestinazione));
                    gui.aggiornaStatoTextArea("Ricevuto ordine da " + partenza 
                            + " a " + destinazione);
                }
            } catch(RemoteException e) {
                System.out.println("Errore durante la creazione di un nuovo ordine");
            }
            elencoOrdini.notify();
        }
    }
    
    void terminaAttivita() {
        terminato = true;
        gui.dispose();
        for(IBase base : basiAttive) {
            try {
                base.terminaAttivita();
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con una base in "
                        + "fase di chiusura");
            }
        }
        for(IAutotreno autotreno : autotreniAttivi) {
            try {
                autotreno.terminaAttivita();
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un autotreno "
                        + "in fase di chiusura");
            }
        }
        try {
            String rmiNomeDitta = "rmi://" + HOST + "/dittaTrasporti";
            Naming.unbind(rmiNomeDitta);
        } catch( RemoteException | MalformedURLException | NotBoundException e) {
            System.out.println("Errore nella cancellazione della registrazione "
                    + "della ditta dal registro RMI");
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
                        + "di registrazione");
            }
        }
        synchronized(basiNomi) {
            try {
                basiNomi.put(base, base.getNomeBase());
            } catch(RemoteException e) {
                System.out.println("Errore di connessione con una base in fase "
                        + "di registrazione");
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
                        + "fase di registrazione");
            }
        }
        synchronized(autotreniNomi) {
            try {
                autotreniNomi.put(autotreno, autotreno.getNomeAutotreno());
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un autotreno in "
                        + "fase di registrazione");
            }
        }
        return partenza;
    }

    @Override
    public void notificaEsito(IOrdine ordine) {
        String text = "";
        try {
            if(!ordine.getConsegnato()) {
                text = "NON ";
            }
            gui.aggiornaStatoTextArea("Ordine proveniente da " 
                    + basiNomi.get(ordine.getBasePartenza()) + " e diretto verso "
                    + basiNomi.get(ordine.getBaseDestinazione()) + " " + text 
                    + " è stato consegnato da "
                    + autotreniNomi.get(ordine.getAutotreno()));
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con un ordine consegnato");
        }
    }

    @Override
    public void aggiornaBasiAttive(IBase base) throws RemoteException {
        rimuoviBase(base);
        gui.aggiornaStatoTextArea("La base " + basiNomi.get(base)
                + " non è più attiva");
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
                + " non è più attivo");
    }
    
    private void rimuoviAutotreno(IAutotreno autotreno) {
        synchronized(autotreniAttivi) {
            autotreniAttivi.remove(autotreno);
        }
    }
    
    class InviaOrdini implements Runnable {
        private IBase partenza;
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
                        try {
                            partenza.registraOrdine(ordine);
                        } catch(RemoteException e) {
                            System.out.println("Errore di comunicazione con la base "
                                    + basiNomi.get(partenza));
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
