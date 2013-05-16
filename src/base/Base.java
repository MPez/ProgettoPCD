/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import common.IOrdine;
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
    
    private LinkedList<IOrdine> listaOrdini;
    private LinkedList<IAutotreno> listaAutotreni;
    private HashMap<String, Boolean> statoConsegne;
    
    private BaseGUI gui;
    private IDitta ditta;
    
    private boolean terminato;

    Base(String nomeBase, BaseGUI gui, IDitta ditta) throws RemoteException {
        listaOrdini = new LinkedList<IOrdine>();
        listaAutotreni = new LinkedList<IAutotreno>();
        statoConsegne = new HashMap<String, Boolean>();
        
        
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
    public void registraOrdine(IOrdine ordine) {
        //prendo il lock sulla lista degli ordini per aggiungere quello in arrivo
        synchronized(listaOrdini) {
            listaOrdini.add(ordine);
            listaOrdini.notify();
        }
        //prendo il lock sullo stato delle consegne e inserisco il nome della base
        //solo se non è presente nell'elenco
        synchronized(statoConsegne) {
            try {
                if(!statoConsegne.containsKey(ordine.getBaseDestinazione().getNomeBase())) {
                    statoConsegne.put(ordine.getBaseDestinazione().getNomeBase(), false);
                }
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con la base di destinazione");
            }
            statoConsegne.notify();
        }
        try {
            gui.aggiornaStatoTextArea("Ricevuto ordine per " + ordine.getBaseDestinazione().getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la base di destinazione");
        }
        aggiornaOrdiniGUI();
    }
    
    @Override
    public void ordineConsegnato(IOrdine ordine) throws RemoteException {
        //prendo il lock sullo stato delle consegne per aggiornare l'avvenuta consegna
        synchronized(statoConsegne) {
            statoConsegne.put(ordine.getBaseDestinazione().getNomeBase(), false);
            statoConsegne.notify();
        }
    }

    @Override
    public void riceviMerce(IOrdine ordine) {
        try {
            gui.aggiornaStatoTextArea("Ricevuto carico da " 
                    + ordine.getAutotreno().getNomeAutotreno());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno");
        }
        try {
            parcheggia(ordine.getAutotreno());
            ordine.getBasePartenza().ordineConsegnato(ordine);
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
    public void terminaAttivita() {
        terminato = true;
        gui.aggiornaStatoTextArea("La base ha ricevuto l'ordine di terminare "
                + "la propria attività");
        synchronized(listaAutotreni) {
            listaAutotreni.notifyAll();
        }
        synchronized(listaOrdini) {
            listaOrdini.notifyAll();
        }
        synchronized(statoConsegne) {
            statoConsegne.notifyAll();
        }
        gui.dispose();
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
        String text = "";
        for(IAutotreno autotreno : listaAutotreni) {
            try {
                text += autotreno.getNomeAutotreno() + "\n";
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un autotreno parcheggiato");
            }
        }
        gui.setAutotreniTextArea(text);
    }
    
    private void aggiornaOrdiniGUI() {
        String text = "";
        for(IOrdine ordine : listaOrdini) {
            try {
                text += ((Integer)ordine.getNumeroOrdine()).toString() + ": "
                        + ordine.getBaseDestinazione().getNomeBase() + "\n";
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un ordine");
            }
        }
        gui.setOrdiniTextArea(text);
    }
    
    class ConsegnaOrdine implements Runnable {
        private IOrdine ordine;
        private IBase destinazione;
        private IAutotreno autotreno;
        
        @Override
        public void run() {
            while(!terminato) {
                try {
                    //prendo il lock sulla lista delle basi di destinazione
                    synchronized(listaOrdini) {
                        //controllo che non sia stato riecevuto l'ordine di terminare
                        //e che la lista di ordini non sia vuota
                        while(!terminato && listaOrdini.isEmpty()) {
                            listaOrdini.wait();
                        }
                        if(!terminato) {
                            try {
                                ordine = listaOrdini.poll();
                                destinazione = ordine.getBaseDestinazione();
                            } catch(RemoteException e) {
                                System.out.println("Errore di comunicazione con un ordine");
                            }
                            aggiornaOrdiniGUI();
                        }
                    }

                    //prendo il lock sulla lista degli autotreni parcheggiati
                    synchronized(listaAutotreni) {
                        //controllo che non sia stato riecevuto l'ordine di terminare
                        //e che esistano autotreni parcheggiati da utilizzare
                        while(!terminato && listaAutotreni.isEmpty()) {
                            listaAutotreni.wait();
                        }
                        if(!terminato) {
                            try {
                                autotreno = listaAutotreni.poll();
                                ordine.setAutotreno(autotreno);
                            } catch(RemoteException e) {
                                System.out.println("Errore di connessione con un ordine");
                            }
                            aggiornaAutotreniGUI();
                        }
                    }
                    //prendo il lock sulla mappa degli ordini in evasione
                    synchronized(statoConsegne) {
                        //controllo che non sia stato riecevuto l'ordine di terminare,
                        //controllo che non sia in corso un ordine verso la stessa destinazione
                        try {
                            while(!terminato && statoConsegne.get(destinazione.getNomeBase())) {
                                statoConsegne.wait();
                            }
                            if(!terminato) {
                                statoConsegne.put(destinazione.getNomeBase(), true);
                            }
                        } catch(RemoteException e) {
                            System.out.println("Errore di comunicazione con la base di destinazione");
                        }
                    }
                    if(!terminato) {
                        try {
                            gui.aggiornaStatoTextArea("Autotreno " 
                                    + autotreno.getNomeAutotreno() 
                                    + " in partenza per " 
                                    + destinazione.getNomeBase());
                            autotreno.registraOrdine(ordine);
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