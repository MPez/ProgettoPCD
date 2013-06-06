/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package autotreno;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import common.IOrdine;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.SwingWorker;

/**
 *
 * @author marco
 */
public class Autotreno extends UnicastRemoteObject implements IAutotreno {
    private String nomeAutotreno;
    private IBase basePartenza;
    private IBase baseDestinazione;
    private IOrdine ordine;
    
    private LinkedList<IOrdine> listaOrdini;
    
    private Viaggio viaggio;
    
    private AutotrenoGUI gui;
    private IDitta ditta;
    
    private boolean terminato;
    private Boolean viaggioEseguito;
    
    Autotreno(String nomeAutotreno, AutotrenoGUI gui, IDitta ditta) throws RemoteException {
        this.nomeAutotreno = nomeAutotreno;
        this.gui = gui;
        this.ditta = ditta;
        terminato = false;
        viaggioEseguito = false;
        
        listaOrdini = new LinkedList<IOrdine>();
    }
    
    //metodo che imposta la base di partenza e aggiorna la GUI
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
    
    //metodo che imposta la base di destinazione e aggiorna la GUI
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
    
    //metodo chiamato dalla ditta di trasporti
    //inserisce un nuovo ordine nella lista degli ordini
    @Override
    public void registraOrdine(IOrdine ordine) {
        try {
            ordine.setStato("in transito");
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con un ordine");
            avvisaDitta(ordine);
        }
        synchronized(listaOrdini) {
            listaOrdini.add(ordine);
            listaOrdini.notify();
        }
    }
    
    //metodo chiamato dalla base di destinazione al momento dell'arrivo dell'ordine
    @Override
    public void parcheggiaAutotreno(IBase destinazione) {
        setBasePartenza(destinazione);
        gui.setDestinazioneTextField("");
    }
    
    //metodo chiamato dalla ditta di trasporti quando la base dove è parcheggiato 
    //l'autotreno non è più attiva
    //richiede una nuova base dove parcheggiarsi
    @Override
    public void aggiornaBasePartenza() {
        //se la base di partenza esite non faccio nulla
        try {
            if(basePartenza.stato()) {}
        } catch(RemoteException e) {
            IBase base = null;
            try {
                base = ditta.impostaNuovaBase(this);
            } catch(RemoteException e1) {
                System.out.println("Errore di comunicazione con la ditta di trasporti");
                terminaAttivita();
            }
            //controlla che sia disponibile almeno una base
            //altrimenti termino l'attività
            if(base != null) {
                try {
                    base.parcheggiaAutotreno(this);
                } catch(RemoteException e2) {
                    System.out.println("Errore di comunicazione con la base di destinazione");
                    terminaAttivita();
                }
            }
            else {
                terminaAttivita();
            }
        }
    }
    
    //metodo chiamato dalla ditta per testare l'attività di un autotreno
    @Override
    public boolean stato() {
        return true;
    }
    
    //metodo che termina l'attività dell'autotreno
    @Override
    public void terminaAttivita() {
        terminato = true;       
        synchronized(listaOrdini) {
            listaOrdini.notify();
        }
        gui.dispose();
        System.exit(0);
    }
    
    //metodo usato per notificare alla ditta lo stato dell'ordine
    private void avvisaDitta(IOrdine ordine) {
        try {
            ditta.notificaEsito(ordine);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta di trasporti");
            terminaAttivita();
        }
    }
    
    //thread che gestisce la consegna degli ordini ricevuti
    class RecapitaOrdine implements Runnable {
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
                            ordine = listaOrdini.poll();
                            try {
                                setBaseDestinazione(ordine.getBaseDestinazione());
                            } catch(RemoteException e1) {
                                System.out.println("Errore di comunicazione con un ordine");
                            }
                            
                            eseguiViaggio();
                            while(!viaggioEseguito) {
                                Thread.currentThread().sleep(1000);
                            }
                            
                            try {
                                baseDestinazione.riceviMerce(ordine);
                                viaggioEseguito = false;
                            } catch(RemoteException e) {
                                System.out.println("Errore di comunicazione con la base "
                                        + "di destinazione");
                                try {
                                    ordine.setStato("abortito");
                                } catch(RemoteException e2) {
                                    System.out.println("Errore di comunicazione con un ordine");
                                }
                                try {
                                    avvisaDitta(ordine);
                                    basePartenza.notificaOrdine(ordine);
                                    basePartenza.parcheggiaAutotreno(ordine.getAutotreno());
                                } catch(RemoteException e2) {
                                    System.out.println("Errore di comunicazione con la base di partenza");
                                    aggiornaBasePartenza();
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
    
    //metodo che crea un thread worker usato per animare la barra di scorrimento
    //presente nella GUI che rappresenta la durata del viaggio
    private void eseguiViaggio() {
        viaggio = new Viaggio();
        viaggio.addPropertyChangeListener(gui);
        viaggio.execute();
    }
    
    //thread che aggiorna la barra di scorrimento presente nella GUI
    //rappresenta la durata del viaggio dell'autotreno
    class Viaggio extends SwingWorker<Void, Void> {
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
        
        @Override
        public void done() {
            viaggioEseguito = true;
            setProgress(0);
            gui.setStatoTerminaAttivitaButton(true);
        }
    }
}
