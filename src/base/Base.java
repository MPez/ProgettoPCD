/*
 * Copyright (c) 2013 Pezzutti Marco
 * 
 * This file is part of Sistema Trasporti Artici (progetto per l'insegnamento 
 * di Programmazione Concorrente e Distribuita).

 * Sistema Trasporti Artici is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sistema Trasporti Artici is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sistema Trasporti Artici.  If not, see <http://www.gnu.org/licenses/>.
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
import java.util.Map;
import java.util.Queue;

/**
 * Classe che rappresenta una base artica, si occupa di gestire gli ordini provenienti 
 * dalla Ditta e di inoltrarli correttamente al primo autotreno libero
 * 
 * @author Pezzutti Marco 1008804
 */
public class Base extends UnicastRemoteObject implements IBase {
    private final String nomeBase;
    
    private final Queue<IOrdine> listaOrdini;
    private final Queue<IOrdine> storicoOrdini;
    private final Queue<IAutotreno> listaAutotreni;
    private final Map<String, Boolean> statoConsegne;
    
    private final BaseGUI gui;
    private final IDitta ditta;
    
    private boolean terminato;

    /**
     * Costruttore che inizializza le strutture dati, imposta il proprio nome, 
     * la GUI e la Ditta passati come parametri
     * 
     * @param nomeBase              nome della base
     * @param gui                   riferimento all'interfaccia grafica
     * @param ditta                 riferimento remoto alla Ditta di trasporti
     * @throws RemoteException 
     */
    public Base(String nomeBase, BaseGUI gui, IDitta ditta) throws RemoteException {
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
    public final String getNomeBase() {
        return nomeBase;
    }

    @Override
    public final void registraOrdine(IOrdine ordine) {
        //imposto lo stato dell'ordine a non consegnato, in caso di errore avviso la Ditta
        try {
            ordine.setStato("non consegnato");
        } catch(RemoteException e) {
            System.out.println("Bro: Errore di comunicazione con un ordine in arrivo");
            avvisaDitta(ordine);
        }
        
        //prendo il lock sulla lista degli ordini per aggiungere quello in arrivo
        synchronized(listaOrdini) {
            listaOrdini.add(ordine);
            listaOrdini.notify();
        }
        
        //prendo il lock sullo storico degli ordini per aggiungere quello in arrivo
        synchronized(storicoOrdini) {
            storicoOrdini.add(ordine);
        }
        
        //prendo il lock sullo stato delle consegne e inserisco il nome della base
        //solo se non è presente nell'elenco
        try {
        synchronized(statoConsegne) {
            if(!statoConsegne.containsKey(ordine.getBaseDestinazione().getNomeBase())) {
                statoConsegne.put(ordine.getBaseDestinazione().getNomeBase(), false);
            }
            statoConsegne.notify();
        }
        gui.aggiornaStatoTextArea("Ricevuto ordine per " 
                + ordine.getBaseDestinazione().getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Bro: Errore di comunicazione con un ordine in arrivo "
                    + "o con la base di destinazione");
        }
        aggiornaOrdiniGUI();
    }
    
    @Override
    public final void ordineConsegnato(IOrdine ordine) {
        //prendo il lock sullo stato delle consegne per aggiornare l'avvenuta consegna
        synchronized(statoConsegne) {
            try {
                statoConsegne.put(ordine.getBaseDestinazione().getNomeBase(), false);
            } catch(RemoteException e) {
                System.out.println("Boc: Errore di comunicazione con un ordine consegnato "
                        + "o con la base di destinazione");
            }
            statoConsegne.notify();
        }
        
        //imposto lo stato dell'ordine a consegnato
        try {
            ordine.setStato("consegnato");
        } catch(RemoteException e) {
            System.out.println("Boc: Errore di comunicazione con un ordine consegnato");
        }
        
        avvisaDitta(ordine);
        aggiornaOrdiniGUI();
    }

    @Override
    public final void riceviMerce(IOrdine ordine) {
        //aggiorno la GUI dell'arrivo del carico e parcheggio l'autotreno, 
        //in caso di errore imposto lo stato dell'ordine ad abortito e avviso la Ditta
        try {
            gui.aggiornaStatoTextArea(ordine.stampaRicevuto());
            parcheggia(ordine.getAutotreno());
            avvisaDitta(ordine);
            
            //avviso la base di partenza dell'avvenuta consegna dell'ordine
            try {
                ordine.getBasePartenza().ordineConsegnato(ordine);
            } catch(RemoteException e) {
                System.out.println("Brm: Errore di comunicazione con la base di partenza");
            }
        } catch(RemoteException e) {
            System.out.println("Brm: Errore di comunicazione con l'ordine in arrivo ");
            try {
                ordine.setStato("abortito");
            } catch(RemoteException e1) {
                System.out.println("Brm: Errore di comunicazione con l'ordine in arrivo");
            }
            avvisaDitta(ordine);
        }
    }

    @Override
    public final void parcheggiaAutotreno(final IAutotreno autotreno) {
        parcheggia(autotreno);
    }
    
    @Override
    public final void aggiornaListaAutotreni(final IAutotreno autotreno) throws RemoteException {
        //prendo il lock sulla lista delgi autotreni per poter rimuovere l'autotreno non più attivo
        synchronized(listaAutotreni) {
            listaAutotreni.remove(autotreno);
        }
        aggiornaAutotreniGUI();
    }
    
    @Override
    public final void notificaOrdine(final IOrdine ordine) throws RemoteException {
        try {
            //prendo il lock sullo stato delle consegne per aggiornare l'ultimo ordine consegnato
            synchronized(statoConsegne) {
                statoConsegne.put(ordine.getNomeDestinazione(), false);
                statoConsegne.notify();
            }
        } catch(RemoteException e) {
            System.out.println("Bno: Errore di comunicazione con un ordine");
        }
        aggiornaOrdiniGUI();
    }
    
    @Override
    public final boolean stato()  {
        return true;
    }

    @Override
    public final void terminaAttivita() {
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
    
    /**
     * Metodo che parcheggia l'autotreno in arrivo e lo aggiunge alla lista
     * 
     * @param autotreno             riferimento all'autotreno in arrivo
     */
    private void parcheggia(final IAutotreno autotreno) {
        try {
            //prendo il lock sulla lista degli autotreni per aggiungere quello in arrivo
            synchronized(listaAutotreni) {
                listaAutotreni.add(autotreno);
                listaAutotreni.notify();
                autotreno.parcheggiaAutotreno(this);
            }
            gui.aggiornaStatoTextArea("Autotreno " + autotreno.getNomeAutotreno() 
                    + " parcheggiato");
        } catch(RemoteException e) {
            System.out.println("Bp: Errore di comunicazione con l'autotreno "
                    + "per il parcheggio");
        }  
        aggiornaAutotreniGUI();
    }
    
    /**
     * Metodo che aggiorna la lista degli autotreni sulla GUI
     */
    private void aggiornaAutotreniGUI() {
        String text = "";
        //prendo il lock sulla lista degli autotreni per estrarne i nomi
        synchronized(listaAutotreni) {
            for(IAutotreno autotreno : listaAutotreni) {
                try {
                    text += autotreno.getNomeAutotreno() + "\n";
                } catch(RemoteException e) {
                    System.out.println("Baa: Errore di comunicazione con un autotreno parcheggiato");
                }
            }
        }
        gui.setAutotreniTextArea(text);
    }
    
    /**
     * Metodo che aggiorna la lista degli ordini sulla GUI stampando numero d'ordine 
     * e base di destinazione
     */
    private void aggiornaOrdiniGUI() {
        String text = "";
        //prendo il lock sullo storico degli ordini per estrarne le informazioni volute
        synchronized(storicoOrdini) {
            for(IOrdine ordine : storicoOrdini) {
                try {
                    text += ordine.stampaNumeroDestinazione() + "\n";
                } catch(RemoteException e) {
                    System.out.println("Bao: Errore di comunicazione con un ordine");
                }
            }
        }
        gui.setOrdiniTextArea(text);
    }
    /**
     * Metodo che notifica alla ditta la consegna o la perdita dell'ordine
     * 
     * @param ordine                    riferimento all'ordine da notificare
     */
    private void avvisaDitta(final IOrdine ordine) {
        try {
            ditta.notificaEsito(ordine);
        } catch(RemoteException e1) {
            System.out.println("Bad: Errore di comunicazione con la ditta di trasporti");
            terminaAttivita();
        }
    }
    
    /**
     * Thread che gestisce la consegna degli ordini ricevuti 
     */
    final class ConsegnaOrdine implements Runnable {
        private IOrdine ordine;
        private IBase destinazione;
        private IAutotreno autotreno = null;
        
        /**
         * Metodo che preleva il primo ordine dalla lista degli ordini e la relativa 
         * base di destinazione; recupera il primo autotreno parcheggiato; controlla 
         * che non siano inviati 2 ordini consecutivi alla stessa base e infine 
         * avvisa l'autotreno di consegnare l'ordine 
         */
        @Override
        public void run() {
            while(!terminato) {
                try {
                    try {
                        //prendo il lock sulla lista delle basi di destinazione
                        synchronized(listaOrdini) {
                            //controllo che non sia stato riecevuto l'ordine di terminare
                            //e che la lista di ordini non sia vuota
                            while(!terminato && listaOrdini.isEmpty()) {
                                listaOrdini.wait();
                            }
                            //recupero l'ordine da evadere e imposto la base di destinazione
                            if(!terminato) {
                                ordine = listaOrdini.poll();
                                destinazione = ordine.getBaseDestinazione();
                            }
                        }

                        //prendo il lock sulla lista degli autotreni parcheggiati
                        synchronized(listaAutotreni) {
                            //controllo che non sia stato riecevuto l'ordine di terminare
                            //e che esistano autotreni parcheggiati da utilizzare
                            while(!terminato && listaAutotreni.isEmpty()) {
                                listaAutotreni.wait();
                            }
                            //recupero l'autotreno pronto nel parcheggio e lo imposto nell'ordine
                            if(!terminato) {
                                autotreno = listaAutotreni.poll();
                                ordine.setAutotreno(autotreno);
                            }
                        }
                        
                    //in caso di errore con l'ordine, avviso la ditta e faccio parcheggiare l'autotreno
                    } catch(RemoteException e) {
                        System.out.println("BCO: Errore di comunicazione con l'ordine da evadere");
                        avvisaDitta(ordine);
                        aggiornaOrdiniGUI();
                        if(autotreno != null) {
                            parcheggia(autotreno);
                        }
                    }
                    
                    try {
                        //prendo il lock sulla mappa degli ordini in evasione
                        synchronized(statoConsegne) {
                            //controllo che non sia stato riecevuto l'ordine di terminare,
                            //controllo che non sia in corso un ordine verso la stessa destinazione
                            while(!terminato && statoConsegne.get(destinazione.getNomeBase())) {
                                statoConsegne.wait();
                            }
                            //imposto verso quale destinazione sto per intraprendere un trasporto
                            if(!terminato) {
                                statoConsegne.put(destinazione.getNomeBase(), true);
                            }
                        }
                        
                        //aggiorno gli stati e avviso l'autotreno del nuovo ordine da trasportare
                        if(!terminato) {
                            gui.aggiornaStatoTextArea("Autotreno " + autotreno.getNomeAutotreno() 
                                    + " in partenza per " + destinazione.getNomeBase());
                            aggiornaOrdiniGUI();
                            aggiornaAutotreniGUI();
                            autotreno.registraOrdine(ordine);
                        }
                        
                    //in caso di errore cerco di abortire l'ordine e avviso la ditta
                    //se l'errore è causato dalla base di destinazione faccio parcheggiare l'autotreno
                    } catch(RemoteException e) {
                        System.out.println("BCO: Errore di comunicazione con la base "
                                + "di destinazione o con l'autotreno");
                        
                        //imposto lo stato dell'ordine
                        try {
                            ordine.setStato("abortito");
                        } catch(RemoteException e1) {
                            System.out.println("BCO: Errore di comunizazione con "
                                    + "l'ordine in evasione");
                        }
                        
                        //avviso la ditta e aggiorno la GUI
                        avvisaDitta(ordine);
                        aggiornaOrdiniGUI();
                        
                        //se l'autotreno è attivo lo parcheggio
                        try {
                            if(autotreno.stato()) {
                                parcheggia(autotreno);
                            }
                        } catch(RemoteException e1) {
                            System.out.println("BCO: Errore di comunicazione con l'autotreno");
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}