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
package autotreno;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import common.IOrdine;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.SwingWorker;

/**
 * Classe che rappresenta un autotreno, si occupa di gestire la consegna effettiva 
 * degli ordini presso la base di destinazione prescelta
 * 
 * @author Pezzutti Marco 1008804
 */
public class Autotreno extends UnicastRemoteObject implements IAutotreno {
    private final String nomeAutotreno;
    private IBase basePartenza;
    private IBase baseDestinazione;
    private IOrdine ordine;
    
    private final Queue<IOrdine> listaOrdini;
    
    private Viaggio viaggio;
    
    private final AutotrenoGUI gui;
    private final IDitta ditta;
    
    private boolean terminato;
    private boolean viaggioEseguito;
    
    /**
     * Costruttore che inizializza le strutture dati e imposta il proprio nome,
     * la GUI e la Ditta di trasporti passati come parametri
     * 
     * @param nomeAutotreno         nome dell'autotreno
     * @param gui                   riferimento all'interfaccia grafica
     * @param ditta                 riferimento remoto alla Ditta di trasporti
     * @throws RemoteException 
     */
    public Autotreno(String nomeAutotreno, AutotrenoGUI gui, IDitta ditta) throws RemoteException {
        this.nomeAutotreno = nomeAutotreno;
        this.gui = gui;
        this.ditta = ditta;
        terminato = false;
        viaggioEseguito = false;
        
        listaOrdini = new LinkedList<IOrdine>();
    }
    
    /**
     * Metodo che imposta la base di partenza e aggiorna la GUI
     * 
     * @param partenza              riferimento alla base di partenza
     */
    final void setBasePartenza(final IBase partenza) {
        this.basePartenza = partenza;
        try{
            gui.setPartenzaTextField(basePartenza.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Asbp: Errore di comunicazione con la base di partenza "
                    + "dell'autotreno " + getNomeAutotreno());
        }
    }
    
    /**
     * Metodo che ritorna il riferimento alla base di partenza
     * 
     * @return                      riferimento alla base di partenza
     */
    final IBase getBasePartenza() {
        return basePartenza;
    }
    
    /**
     * Metodo che imposta la base di destinazione e aggiorna la GUI
     * 
     * @param destinazione          riferimento alla base di destinazione
     */
    final void setBaseDestinazione(final IBase destinazione) {
        this.baseDestinazione = destinazione;
        try{
            gui.setDestinazioneTextField(baseDestinazione.getNomeBase());
        } catch(RemoteException e) {
            System.out.println("Asbd: Errore di comunicazione con la base di destinazione "
                    + "dell'autotreno " + getNomeAutotreno());
        }
    }
    
    /**
     * Metodo che ritorna il riferimento alla base di destinazione
     * 
     * @return                      riferimento alla base di destinazione
     */
    final IBase getBaseDestinazione() {
        return baseDestinazione;
    }
    
    @Override
    final public String getNomeAutotreno() {
        return nomeAutotreno;
    }
    
    @Override
    final public void registraOrdine(IOrdine ordine) {
        //imposto lo stato dell'ordine
        try {
            ordine.setStato("in transito");
        } catch(RemoteException e) {
            System.out.println("Aro: Errore di comunicazione con un ordine");
            avvisaDitta(ordine);
        }
        
        //prendo il lock sulla lista degli ordini e aggiungo l'ordine in arrivo
        synchronized(listaOrdini) {
            listaOrdini.add(ordine);
            listaOrdini.notify();
        }
    }
    
    @Override
    public final void parcheggiaAutotreno(final IBase destinazione) {
        setBasePartenza(destinazione);
        gui.setDestinazioneTextField("");
    }
    
    @Override
    public final void aggiornaBasePartenza() {
        //se la base di partenza esite non faccio nulla, altrimenti richiedo 
        //una nuova base alla ditta; se la ditta non esiste più termino l'attività
        try {
            if(basePartenza.stato()) {}
        } catch(RemoteException e) {
            if(!terminato) {
                IBase base = null;
                try {
                    base = ditta.impostaNuovaBase(this);
                } catch(RemoteException e1) {
                    System.out.println("Aabp: Errore di comunicazione con la ditta di trasporti");
                    terminaAttivita();
                }

                //controllo che sia disponibile almeno una base, altrimenti termino l'attività
                if(base != null) {
                    try {
                        base.parcheggiaAutotreno(this);
                    } catch(RemoteException e2) {
                        System.out.println("Aabp: Errore di comunicazione con la base di destinazione");
                        terminaAttivita();
                    }
                }
                else {
                    terminaAttivita();
                }
            }
        }
    }
    
    @Override
    public final boolean getViaggioEseguito() {
        return viaggioEseguito;
    }
    
    @Override
    public final boolean stato() {
        return true;
    }
    
    @Override
    public final void terminaAttivita() {
        terminato = true;       
        synchronized(listaOrdini) {
            listaOrdini.notify();
        }
        gui.dispose();
        System.exit(0);
    }
    
    /**
     * Metodo usato per notificare alla ditta lo stato dell'ordine
     * 
     * @param ordine                    riferimento all'ordine da notificare
     */
    //
    private void avvisaDitta(IOrdine ordine) {
        try {
            ditta.notificaEsito(ordine);
        } catch(RemoteException e) {
            System.out.println("Aad: Errore di comunicazione con la ditta di trasporti");
            terminaAttivita();
        }
    }
    
    /**
     * thread che gestisce la consegna degli ordini ricevuti
     */
    final class RecapitaOrdine implements Runnable {
        /**
         * Metodo che esegue la consegna degli ordini; recupera il primo e unico ordine 
         * dalla lista degli ordini; imposta la base di destinazione ed esegue il viaggio;
         * al termine del viaggio avvisa la base di destinazione di ricevere la merce
         */
        @Override
        public void run() {
            while(!terminato) {
                try {
                    //prendo il lock sulla lista degli ordini
                    //che conterrà al più un ordine alla volta
                    synchronized(listaOrdini) {
                        while(!terminato && listaOrdini.isEmpty()) {
                            listaOrdini.wait(); 
                        }
                        if(!terminato) {
                            //recupero l'ordine da evadere e imposto la destinazione
                            ordine = listaOrdini.poll();
                            try {
                                setBaseDestinazione(ordine.getBaseDestinazione());
                            } catch(RemoteException e1) {
                                System.out.println("ARO: Errore di comunicazione con un ordine");
                            }
                            //eseguo il viaggio e fino a quando non è finito aspetto
                            eseguiViaggio();
                            while(!viaggioEseguito) {
                                Thread.currentThread().sleep(1000);
                            }
                            //terminato il viaggio faccio ricevere la merce alla base di destinazione
                            //se la base non è più attiva, l'ordine viene abortito
                            //vengono avvisate la ditta e la base di partenza
                            //viene fatto tornare l'autotreno alla base di partenza
                            //se non esiste più l'autotreno richiede una nuova base dove parcheggiarsi
                            if(!terminato) {
                                try {
                                    viaggioEseguito = false;
                                    baseDestinazione.riceviMerce(ordine);
                                //in caso di errore con la base abortisco l'ordine e avviso la ditta
                                //cerco di avvisare la base di partenza
                                //provo a far tornare verso la base di aprtenza l'autotreno
                                //altrimenti ne richedo una nuova alla ditta
                                } catch(RemoteException e) {
                                    System.out.println("ARO: Errore di comunicazione con la base "
                                            + "di destinazione");
                                    try {
                                        ordine.setStato("abortito");
                                    } catch(RemoteException e2) {
                                        System.out.println("ARO: Errore di comunicazione con un ordine");
                                    }
                                    try {
                                        avvisaDitta(ordine);
                                        basePartenza.notificaOrdine(ordine);
                                        basePartenza.parcheggiaAutotreno(ordine.getAutotreno());
                                    } catch(RemoteException e2) {
                                        System.out.println("ARO: Errore di comunicazione con la base di partenza");
                                        try {
                                            ordine.getAutotreno().aggiornaBasePartenza();
                                        } catch(RemoteException e3) {
                                            System.out.println("ARO: Errore di comunicazione con un ordine o "
                                                    + "con l'autotreno");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Metodo che crea un thread worker usato per animare la barra di scorrimento, 
     * presente nella GUI, che rappresenta la durata del viaggio
     */
    private void eseguiViaggio() {
        viaggio = new Viaggio();
        viaggio.addPropertyChangeListener(gui);
        viaggio.execute();
    }
    
    /**
     * Thread che aggiorna la barra di scorrimento presente nella GUI
     */
    final class Viaggio extends SwingWorker<Void, Void> {
        /**
         * Metodo che esegue virtualmente il viaggio tra le basi di partenza e 
         * destinazione; aggiorna progressivamente la barra di scorrimento con 
         * un valore casuale dopo aver dormito per un tempo casuale
         * @return 
         */
        @Override
        protected Void doInBackground() {
            gui.setStatoTerminaAttivitaButton(false);
            Random random = new Random();
            int progress = 0;
            setProgress(0);
            while (progress < 100) {
                try {
                    Thread.sleep(random.nextInt(300));
                } catch (InterruptedException ignore) {}
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }
        /**
         * Metodo invocato al termine dell'esecuzione del viaggio che resetta 
         * la barra di scorrimento e notifica la fine del viaggio
         */
        @Override
        public void done() {
            viaggioEseguito = true;
            setProgress(0);
            gui.setStatoTerminaAttivitaButton(true);
        }
    }
}
