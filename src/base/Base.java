/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import common.IAutotreno;
import common.IBase;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author marco
 */
public class Base implements IBase {
    private String nomeBase;
    
    private LinkedList<IBase> listaDestinazioni;
    private LinkedList<IAutotreno> listaAutotreni;
    private LinkedList<IBase> basiAttive;
    private LinkedList<IAutotreno> autotreniAttivi;
    private HashMap<IBase, Boolean> statoConsegne;
    
    private BaseGUI gui;
    
    private boolean terminato;

    Base(String nomeBase, BaseGUI gui) {
        this.nomeBase = nomeBase;
        this.gui = gui;
        terminato = false;
    }
    
    @Override
    public String getNomeBase() {
        return nomeBase;
    }

    @Override
    public void registraOrdine(IBase destinazione) {
        synchronized(basiAttive) {
            if(!basiAttive.contains(destinazione)) {
                basiAttive.add(destinazione);
            }
        }
        synchronized(listaDestinazioni) {
            listaDestinazioni.add(destinazione);
            listaDestinazioni.notify();
        }
        try {
            gui.aggiornaStatoTextArea("Ricevuto ordine per " 
                    + destinazione.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di destinazione.");
        }
        aggiornaOrdiniGUI();
    }
    
    @Override
    public void ordineConsegnato(IBase destinazione) throws RemoteException {
        synchronized(statoConsegne) {
            statoConsegne.put(destinazione, true);
            statoConsegne.notify();
        }
    }

    @Override
    public void riceviMerce(IBase partenza, IAutotreno autotreno) {
        synchronized(autotreniAttivi) {
            if(!autotreniAttivi.contains(autotreno)) {
                autotreniAttivi.add(autotreno);
            }
        }
        try {
            gui.aggiornaStatoTextArea("Ricevuto carico da " 
                    + autotreno.getNomeAutotreno());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno.");
        }
        parcheggia(autotreno);
        try {
            partenza.ordineConsegnato(this);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di partenza");
        }
    }

    @Override
    public boolean stato()  {
        return true;
    }

    @Override
    public void terminaAttività() {
        terminato = true;
        gui.aggiornaStatoTextArea("La base ha ricevuto l'ordine di terminare "
                + "la propria attività");
        gui.dispose();
        synchronized(listaAutotreni) {
            listaAutotreni.notifyAll();
        }
        synchronized(listaDestinazioni) {
            listaDestinazioni.notifyAll();
        }
        synchronized(statoConsegne) {
            statoConsegne.notifyAll();
        }
    }
    
    private void parcheggia(IAutotreno autotreno) {
        synchronized(listaAutotreni) {
            listaAutotreni.add(autotreno);
            listaAutotreni.notify();
            try {
                autotreno.parcheggiaAutotreno(this);
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con l'autotreno per il parcheggio.");
            }
        }
        try {
            gui.aggiornaStatoTextArea("Autotreno " + autotreno.getNomeAutotreno() 
                    + " parcheggiato.");
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno.");
        }
        aggiornaAutotreniGUI();
    }
    
    private void aggiornaAutotreniGUI() {
        String auto = "";
        for(IAutotreno autotreno : listaAutotreni) {
            try {
                auto += autotreno.getNomeAutotreno() + "\n";
            } catch(RemoteException e) {
                System.err.println("Errore di comunicazione con un autotreno parcheggiato.");
            }
        }
        gui.setAutotreniTextArea(auto);
    }
    
    private void aggiornaOrdiniGUI() {
        String nomi = "";
        for(IBase base : listaDestinazioni) {
            try {
                nomi += base.getNomeBase() + "\n";
                
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con una base di destinazione.");
            }
        }
        gui.setOrdiniTextArea(nomi);
    }
    
    class ConsegnaOrdine implements Runnable {
        private IBase destinazione;
        private IAutotreno autotreno;
        
        @Override
        public void run() {
            while(!terminato) {
                try {
                    //prendo il lock sulla lista delle basi di destinazione
                    synchronized(listaDestinazioni) {
                        //controllo che non sia stato riecevuto l'ordine di terminare
                        //e che la lista di ordini non sia vuota
                        while(!terminato && listaDestinazioni.isEmpty()) {
                            listaDestinazioni.wait();
                        }
                        //prendo il lock sulla lista degli autotreni parcheggiati
                        synchronized(listaAutotreni) {
                            //controllo che non sia stato riecevuto l'ordine di terminare
                            //e che esistano autotreni parcheggiati da utilizzare
                            while(!terminato && listaAutotreni.isEmpty()) {
                                listaAutotreni.wait();
                            }
                            destinazione = listaDestinazioni.poll();
                            statoConsegne.put(destinazione, false);
                            autotreno = listaAutotreni.poll();
                            try {
                                //controllo che non sia stato riecevuto l'ordine di terminare,
                                //controllo che la base sia attiva
                                //in caso contrario la rimuovo dalla lista degli autotreni
                                if(!terminato && destinazione.stato()) {
                                    try {
                                        //controllo che non sia stato riecevuto l'ordine di terminare,
                                        //controllo che l'autotreno sia attivo
                                        if(!terminato && autotreno.stato()) {
                                            //prendo il lock sulla mappa degli ordini in evasione
                                            synchronized(statoConsegne) {
                                                //controllo che non sia stato riecevuto l'ordine di terminare,
                                                //controllo che non sia in corso un ordine verso la stessa destinazione
                                                while(!terminato && statoConsegne.get(destinazione)) {
                                                    statoConsegne.wait();
                                                }
                                                autotreno.consegnaOrdine(destinazione);
                                                gui.aggiornaStatoTextArea("Autotreno " 
                                                        + autotreno.getNomeAutotreno() 
                                                        + "in partenza per " 
                                                        + destinazione.getNomeBase());
                                            }
                                        }
                                    } catch(RemoteException e) {
                                        System.out.println("Errore di comunicazione con "
                                        + "l'autotreno per la consegna dell'ordine.");
                                    } finally {
                                        //segnalo l'errore ed elimino l'autotreno dalle liste
                                        gui.aggiornaStatoTextArea("In attesa di un nuovo autotreno");
                                        listaAutotreni.remove(autotreno);
                                        autotreniAttivi.remove(autotreno);
                                    }
                                }
                            } catch(RemoteException e) {
                                System.out.println("Errore di comunicazione con la "
                                        + "base di destinazione.");
                            } finally {
                                //segnalo l'errore ed elimino la base dalle liste
                                //aggiorno l'ultima destinazione
                                gui.aggiornaStatoTextArea("Ordine annullato.");
                                listaDestinazioni.remove(destinazione);
                                statoConsegne.remove(destinazione);
                                basiAttive.remove(destinazione);
                            }
                        }
                    }
                } catch(InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}