/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author marco
 */
public class Base extends UnicastRemoteObject implements IBase {
    private String nomeBase;
    
    private LinkedList<IBase> listaDestinazioni;
    private LinkedList<IAutotreno> listaAutotreni;
    private HashMap<IBase, Boolean> statoConsegne;
    
    private BaseGUI gui;
    private IDitta ditta;
    
    private boolean terminato;

    Base(String nomeBase, BaseGUI gui, IDitta ditta) throws RemoteException {
        listaDestinazioni = new LinkedList<>();
        listaAutotreni = new LinkedList<>();
        statoConsegne = new HashMap<>();
        
        this.nomeBase = nomeBase;
        this.gui = gui;
        this.ditta = ditta;
        terminato = false;
    }
    
    @Override
    public String getNomeBase() {
        return nomeBase;
    }

    @Override
    public void registraOrdine(IBase destinazione) {
        synchronized(listaDestinazioni) {
            listaDestinazioni.add(destinazione);
            listaDestinazioni.notify();
        }
        synchronized(statoConsegne) {
            statoConsegne.put(destinazione, false);
            statoConsegne.notify();
        }
        try {
            gui.aggiornaStatoTextArea("Ricevuto ordine per " + destinazione.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di destinazione");
        }
        aggiornaOrdiniGUI();
    }
    
    @Override
    public void ordineConsegnato(IBase destinazione) throws RemoteException {
        synchronized(statoConsegne) {
            statoConsegne.put(destinazione, false);
            statoConsegne.notify();
        }
    }

    @Override
    public void riceviMerce(IBase partenza, IAutotreno autotreno) {
        try {
            gui.aggiornaStatoTextArea("Ricevuto carico da " 
                    + autotreno.getNomeAutotreno());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno");
        }
        parcheggia(autotreno);
        try {
            partenza.ordineConsegnato(this);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di partenza");
        }
    }

    @Override
    public void parcheggiaAutotreno(IAutotreno autotreno) {
        parcheggia(autotreno);
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
        System.exit(0);
    }
    
    private void parcheggia(IAutotreno autotreno) {
        synchronized(listaAutotreni) {
            listaAutotreni.add(autotreno);
            listaAutotreni.notify();
            try {
                autotreno.parcheggiaAutotreno(this);
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con l'autotreno per il parcheggio");
            }
        }
        try {
            gui.aggiornaStatoTextArea("Autotreno " + autotreno.getNomeAutotreno() 
                    + " parcheggiato");
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno");
        }  
        aggiornaAutotreniGUI();
    }
    
    private void aggiornaAutotreniGUI() {
        String nomi = "";
        for(IAutotreno autotreno : listaAutotreni) {
            try {
                nomi += autotreno.getNomeAutotreno() + "\n";
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un autotreno parcheggiato");
            }
        }
        gui.setAutotreniTextArea(nomi);
    }
    
    private void aggiornaOrdiniGUI() {
        String nomi = "";
        for(IBase base : listaDestinazioni) {
            try {
                nomi += base.getNomeBase() + "\n";
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con una base di destinazione");
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
                        destinazione = listaDestinazioni.poll();
                        aggiornaOrdiniGUI();
                    }

                    //prendo il lock sulla lista degli autotreni parcheggiati
                    synchronized(listaAutotreni) {
                        //controllo che non sia stato riecevuto l'ordine di terminare
                        //e che esistano autotreni parcheggiati da utilizzare
                        while(!terminato && listaAutotreni.isEmpty()) {
                            listaAutotreni.wait();
                        }
                        autotreno = listaAutotreni.poll();
                        aggiornaAutotreniGUI();
                    }
/*
                    //prendo il lock sulla mappa degli ordini in evasione
                    synchronized(statoConsegne) {
                        //controllo che non sia stato riecevuto l'ordine di terminare,
                        //controllo che non sia in corso un ordine verso la stessa destinazione
                        while(!terminato && !statoConsegne.get(destinazione)) {
                            statoConsegne.wait();
                        }
                        statoConsegne.put(destinazione, true);
                    }*/
                    if(!terminato) {
                        try {
                            gui.aggiornaStatoTextArea("Autotreno " 
                                    + autotreno.getNomeAutotreno() 
                                    + " in partenza per " 
                                    + destinazione.getNomeBase());
                            autotreno.consegnaOrdine(destinazione);
                        } catch(RemoteException e) {
                            System.out.println("Errore di comunicazione con una base "
                                    + "o un autotreno");
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}