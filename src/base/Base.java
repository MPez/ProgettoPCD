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
    private LinkedList<IOrdine> storicoOrdini;
    private LinkedList<IAutotreno> listaAutotreni;
    private HashMap<String, Boolean> statoConsegne;
    
    private BaseGUI gui;
    private IDitta ditta;
    
    private boolean terminato;

    Base(String nomeBase, BaseGUI gui, IDitta ditta) throws RemoteException {
        listaOrdini = new LinkedList<IOrdine>();
        storicoOrdini = new LinkedList<IOrdine>();
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

    //metodo chiamato dalla ditta di trasporti per registrare un nuovo ordine
    @Override
    public void registraOrdine(IOrdine ordine) {
        try {
            ordine.setStato("non consegnato");
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con un ordine in arrivo");
        }
        //prendo il lock sulla lista degli ordini per aggiungere quello in arrivo
        synchronized(listaOrdini) {
            listaOrdini.add(ordine);
            storicoOrdini.add(ordine);
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
    
    //metodo chiamato dalla base di destinazione dell'ordine alla consegna dell'ordine
    @Override
    public void ordineConsegnato(IOrdine ordine) {
        //prendo il lock sullo stato delle consegne per aggiornare l'avvenuta consegna
        synchronized(statoConsegne) {
            try {
                statoConsegne.put(ordine.getBaseDestinazione().getNomeBase(), false);
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un ordine");
            }
            statoConsegne.notify();
        }
        try {
            ordine.setStato("consegnato");
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con un ordine");
        }
        try {
            ditta.notificaEsito(ordine);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta di trasporti");
        }
        aggiornaOrdiniGUI();
    }

    //metodo chiamato dall'autotreno che effettua l'ordine
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

    //metodo chiamato dalla base di destinazione per parcheggiare l'autotreno
    @Override
    public void parcheggiaAutotreno(IAutotreno autotreno) {
        parcheggia(autotreno);
    }
    
    //metodo chiamato dalla ditta quando un autotreno cessa la propria attività
    @Override
    public void aggiornaListaAutotreni(IAutotreno autotreno) throws RemoteException {
        synchronized(listaAutotreni) {
            listaAutotreni.remove(autotreno);
        }
        aggiornaAutotreniGUI();
    }
    
    //metodo chiamato dalla ditta per testare l'attività di una base
    @Override
    public boolean stato()  {
        return true;
    }

    //metodo che termina l'attività della base
    @Override
    public void terminaAttivita() {
        terminato = true;
        gui.aggiornaStatoTextArea("La base ha ricevuto l'ordine di terminare "
                + "la propria attività");
        //prendo il lock sulle liste di autotreni, ordini e consegne
        //risveglio gli eventuali thread in attesa
        synchronized(listaAutotreni) {
            listaAutotreni.notifyAll();
        }
        synchronized(listaOrdini) {
            listaOrdini.notifyAll();
        }
        synchronized(statoConsegne) {
            statoConsegne.notifyAll();
        }
        //chiudo l'interfaccia grafica
        gui.dispose();
        //chiudo la base
        System.exit(0);
    }
    
    //metodo che parcheggia l'autotreno in arrivo e lo aggiunge alla lista
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
    
    //metodo che aggiorna la lista degli autotreni sulla GUI
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
    
    //metodo che aggiorna la lista degli ordini sulla GUI
    private void aggiornaOrdiniGUI() {
        String text = "";
        for(IOrdine ordine : storicoOrdini) {
            try {
                text += ordine.stampaNumeroDestinazione() + "\n";
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con un ordine");
            }
        }
        gui.setOrdiniTextArea(text);
    }
    
    //thread che gestisce la consegna degli ordini ricevuti
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
                            autotreno = listaAutotreni.poll();
                            try {
                                ordine.setAutotreno(autotreno);
                            } catch(RemoteException e) {
                                System.out.println("Errore di comunicazione con "
                                        + "l'ordine da evadere");
                            }
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
                            aggiornaOrdiniGUI();
                            aggiornaAutotreniGUI();
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