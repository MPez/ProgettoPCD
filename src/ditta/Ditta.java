/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
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
import java.util.Random;

/**
 *
 * @author marco
 */
public class Ditta extends UnicastRemoteObject implements IDitta {
    private HashMap<IBase, Boolean> basiAttive;
    private HashMap<String, IBase> nomiBasi;
    private HashMap<IBase, String> basiNomi;
    
    private HashMap<IAutotreno, Boolean> autotreniAttivi;
    private HashMap<String, IAutotreno> nomiAutotreni;
    private HashMap<IAutotreno, String> autotreniNomi;
    
    private LinkedList<IOrdine> elencoOrdini;
    private LinkedList<IOrdine> storicoOrdini;
    
    private DittaGUI gui;
    
    private boolean terminato;
    
    private static final String HOST = "localhost:";
    
    Ditta(DittaGUI gui) throws RemoteException {
        basiAttive = new HashMap<IBase, Boolean>();
        nomiBasi = new HashMap<String, IBase>();
        basiNomi = new HashMap<IBase, String>();
        
        autotreniAttivi = new HashMap<IAutotreno, Boolean>();
        nomiAutotreni = new HashMap<String, IAutotreno>();
        autotreniNomi = new HashMap<IAutotreno, String>();
        
        elencoOrdini = new LinkedList<IOrdine>();
        storicoOrdini = new LinkedList<IOrdine>();
        
        this.gui = gui;
        terminato = false;
    }
    
    //metodo chiamato dalla GUI che ritorna un nuovo thread che genera ordini automaticamente
    Thread avviaCreaOrdini() {
        gui.aggiornaStatoTextArea("Generazione automatica degli ordini avviata");
        return new Thread(new CreaOrdini());
    }
    
    //metodo utilizzato per inserire nuovi ordini creati dalla GUI
    void inserisciOrdine(String partenza, String destinazione, int quantita) {
        IBase basePartenza;
        IBase baseDestinazione;
        basePartenza = nomiBasi.get(partenza);
        baseDestinazione = nomiBasi.get(destinazione);
        
        //prendo il lock sull'elenco degli ordini
        //aggiungo un ordine fino al raggiungimento della quantità desiderata
        synchronized(elencoOrdini) {
            try {
                for(int i = 0; i < quantita; i++) {
                    IOrdine ordine = new Ordine(basePartenza, baseDestinazione);
                    elencoOrdini.add(ordine);
                    storicoOrdini.add(ordine);
                    aggiornaOrdiniGUI();
                }
            } catch(RemoteException e) {
                System.out.println("Dio: Errore durante la creazione di un nuovo ordine");
            }
            elencoOrdini.notify();
        }
    }
    
    //metodo che aggiorna la lista degli ordini nell'interfaccia grafica
    private void aggiornaOrdiniGUI() {
        String text = "";
        for(IOrdine ordine : storicoOrdini) {
            try {
                text += ordine.stampaStato() + "\n";
            } catch(RemoteException e) {
                System.out.println("Dao: Errore di comunicazione con un ordine");
            }
        }
        gui.aggiornaOrdiniTextArea(text);
    }
    
    //thread che controlla ogni tempo millisecondi che le basi siano attive
    private class ControllaBasi implements Runnable {
        private final int tempo = 2000;
        
        @Override
        public void run() {
            while(!terminato) {
                //prendo il lock sulla lista di basi attive
                //controllo che le basi esistano, altrimenti le cancello dall'elenco
                synchronized(basiAttive) {
                    if(!terminato) {
                        for(IBase base : basiAttive.keySet()) {
                            //controllo solo le basi ancora attive
                            if(basiAttive.get(base)) {
                                try {
                                    base.stato();
                                } catch(RemoteException e) {
                                    System.out.println("DCB: La base " + basiNomi.get(base)
                                            + " non è più attiva");
                                    aggiornaBasiAttive(base);
                                    //aggiorno le basi di partenza degli autotreni
                                    //in quanto potrebbero essere parcheggiati presso
                                    //la base che non è più attiva
                                    for(IAutotreno autotreno : autotreniAttivi.keySet()) {
                                        //controllo solo gli autotreni ancora attivi
                                        //e che non siano in viaggio
                                        try {
                                            if(autotreniAttivi.get(autotreno) 
                                                    && !autotreno.getViaggioEseguito()) {
                                                autotreno.aggiornaBasePartenza();
                                            }
                                        } catch(RemoteException e1) {
                                            System.out.println("DCB: Errore di comunicazione"
                                                    + "con l'autotreno "
                                                    + autotreniNomi.get(autotreno));
                                            aggiornaAutotreniAttivi(autotreno);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.currentThread().sleep(tempo);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //thread che controlla ogni tempo millisecondi che gli autotreni siano attivi
    private class ControllaAutotreni implements Runnable{
        private final int tempo = 2000;
        
        @Override
        public void run() {
            while(!terminato) {
                //prendo il lock sulla  lista degli autotreni attivi
                //controllo che gli autotreni esistano, altrimenti li cancello dall'elenco
                synchronized(autotreniAttivi) {
                    if(!terminato) {
                        for(IAutotreno autotreno : autotreniAttivi.keySet()) {
                            //controllo solo gli autotreni ancora attivi
                            if(autotreniAttivi.get(autotreno)) {
                                try {
                                    autotreno.stato();
                                } catch(RemoteException e) {
                                    System.out.println("DCA: L'autotreno " 
                                            + autotreniNomi.get(autotreno)
                                            + " non è più attivo");
                                    aggiornaAutotreniAttivi(autotreno);
                                    for(IBase base : basiAttive.keySet()) {
                                        //controllo solo le basi ancora attive
                                        if(basiAttive.get(base)) {
                                            try {
                                                base.aggiornaListaAutotreni(autotreno);
                                            } catch(RemoteException e1) {
                                                System.out.println("DCA: Errore di comunicazione "
                                                        + "con la base "
                                                        + basiNomi.get(base));
                                                aggiornaBasiAttive(base);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.currentThread().sleep(tempo);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //thread gestito dalla pulsante Auto della GUI che, se attivo, crea ordini casuali ogni tempo millisecondi
    class CreaOrdini implements Runnable {
        private IBase basePartenza;
        private IBase baseDestinazione;
        
        private LinkedList<IBase> listaBasi;
        
        private final int tempo = 1000;
        private final int quantitaOrdini = 5;

        public CreaOrdini() {
            super();
            listaBasi = new LinkedList<IBase>();
        }
        
        //metodo che aggiunge alla lista una base solo se è attiva
        private void creaListaBasi() {
            listaBasi.clear();
            synchronized(basiAttive) {
                for(IBase base : basiAttive.keySet()) {
                    if(basiAttive.get(base)) {
                        listaBasi.add(base);
                    }
                }
            }
        }
        
        @Override
        public void run() {
            try {
                while(!terminato && !Thread.currentThread().isInterrupted()) {
                    Random random = new Random();
                    creaListaBasi();
                    //proseguo solo se ci sono basi attive
                    //controllo che il thread sia attivo
                    if(!terminato && !listaBasi.isEmpty()) {
                        basePartenza = listaBasi.get(random.nextInt(listaBasi.size()));
                        baseDestinazione = listaBasi.get(random.nextInt(listaBasi.size()));
                        //controllo che il thread sia attivo
                        //controllo che le basi siano diverse
                        while(!terminato && basiNomi.get(basePartenza).equals(basiNomi.get(baseDestinazione))) {
                            baseDestinazione = listaBasi.get(random.nextInt(listaBasi.size()));
                        }
                        inserisciOrdine(basiNomi.get(basePartenza), basiNomi.get(baseDestinazione), random.nextInt(quantitaOrdini));

                        Thread.currentThread().sleep(tempo);
                    }
                }
            } catch(InterruptedException e) {
                gui.aggiornaStatoTextArea("Generazione automatica degli ordini interrotta");
            }
        }
    }
    
    //metodo che termina l'attività di tutte le basi e di tutti gli autotreni attivi
    //termina l'attività della ditta di trasporti
    void terminaAttivita() {
        terminato = true;
        //risveglio gli eventuali thread dormienti
        synchronized(elencoOrdini) {
            elencoOrdini.notify();
        }
        //chiudo l'interfaccia utente
        gui.dispose();
        //termina tutte le basi
        for(IBase base : basiAttive.keySet()) {
            if(basiAttive.get(base)) {
                try {
                    base.terminaAttivita();
                } catch(RemoteException ignore) {}
            }
        }
        //termina tutti gli autotreni
        for(IAutotreno autotreno : autotreniAttivi.keySet()) {
            if(autotreniAttivi.get(autotreno)) {
                try {
                    autotreno.terminaAttivita();
                } catch(RemoteException ignore) {}
            }
        }
        //rimuove dal registro RMI la ditta di trasporti
        try {
            String rmiNomeDitta = "rmi://" + HOST + "/dittaTrasporti";
            Naming.unbind(rmiNomeDitta);
        } catch(RemoteException e) {
            System.out.println("Dta: Errore nella cancellazione della registrazione "
                    + "della ditta dal registro RMI");
        } catch(MalformedURLException e1) {
            e1.printStackTrace();
        } catch(NotBoundException e2) {
            e2.printStackTrace();
        }
        //chiudo la ditta
        System.exit(0);
    }
    
    //metodo chiamato da una base in fase di registrazione
    @Override
    public void registraBase(IBase base) {
        //prendo il lock sulla lista delle basi attive
        //aggiungo la nuova base
        synchronized(basiAttive) {
            basiAttive.put(base, true);
        }
        
        try {
            //prendo il lock sulla mappa dei nomi delle basi
            //aggiorno la mappa dei nomi delle basi
            synchronized(nomiBasi) {
                    nomiBasi.put(base.getNomeBase(), base);
                    nomiBasi.notifyAll();
            }
            
            //aggiorno la mappa delle basi
            synchronized(basiNomi) {
                    basiNomi.put(base, base.getNomeBase());
            }
        } catch(RemoteException e) {
            System.out.println("Drb: Errore di connessione con una base in fase "
                    + "di registrazione");
        }
        
        gui.aggiornaStatoTextArea("La base "+ basiNomi.get(base) 
            + " si è registrata");
        //aggiorno i combo box della gui che contengono le basi
        gui.aggiungiBaseComboBox(basiNomi.get(base));
        //avvio il thread che controlla l'esistenza delle basi
        new Thread(this.new ControllaBasi()).start();
    }
    
    //metodo chiamato da un autotreno in fase di registrazione
    @Override
    public IBase registraAutotreno(IAutotreno autotreno, String nomeBasePartenza) {
        IBase partenza = null;
        try {
            //prendo il lock sulla mappa dei nomi delle basi
            //controllo che la base richiesta dall'autoreno sia già registrata
            synchronized(nomiBasi) {
                while(!nomiBasi.containsKey(nomeBasePartenza)) {
                    nomiBasi.wait();
                }
                //prendo il lock sulla lista di autotreni attivi
                synchronized(autotreniAttivi) {
                    autotreniAttivi.put(autotreno, true);
                }
                partenza = nomiBasi.get(nomeBasePartenza);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        try {
            //prendo il lock sulla mappa dei nomi degli autotreni
            //aggiorno la mappa dei nomi delgi autotreni
            synchronized(nomiAutotreni) {
                nomiAutotreni.put(autotreno.getNomeAutotreno(), autotreno);
            }

            //prendo il lock sulla mappa degli autotreni
            //aggiorno la mappa degli autotreni
            synchronized(autotreniNomi) {
                autotreniNomi.put(autotreno, autotreno.getNomeAutotreno());
            }
        } catch(RemoteException e) {
            System.out.println("Dra: Errore di comunicazione con un autotreno in "
                    + "fase di registrazione");
        }

        gui.aggiornaStatoTextArea("L'autotreno " + autotreniNomi.get(autotreno)
                + " si è registrato presso la base " + nomeBasePartenza);
        
        //avvio il thread che controlla l'esistenza degli autotreni
        new Thread(this.new ControllaAutotreni()).start();
        
        return partenza;
    }

    //metodo chiamato da una base al momento della ricezione di un ordine
    @Override
    public void notificaEsito(IOrdine ordine) {
        try {
            gui.aggiornaStatoTextArea(ordine.stampaEsito());
        } catch(RemoteException e) {
            System.out.println("Dne: Errore di comunicazione con un ordine consegnato");
        }
        aggiornaOrdiniGUI();
    }

    //metodo che aggiorna la lista delle basi attive
    @Override
    public void aggiornaBasiAttive(IBase base) {
        rimuoviBase(base);
        gui.aggiornaStatoTextArea("La base " + basiNomi.get(base)
                + " non è più attiva");
    }
    
    //metodo che rimuove una base dalla lista delle basi attive
    private void rimuoviBase(IBase base) {
        synchronized(basiAttive) {
            basiAttive.put(base, false);
        }
        gui.rimuoviBaseComboBox(basiNomi.get(base));
    }

    //metodo che aggiorna la lista degli autotreni attivi
    @Override
    public void aggiornaAutotreniAttivi(IAutotreno autotreno) {
        for(IOrdine ordine : storicoOrdini) {
            try {
                if("in transito".equals(ordine.getStato())) {
                    if(autotreniNomi.get(ordine.getAutotreno()).equals(autotreniNomi.get(autotreno))) {
                        ordine.setStato("abortito");
                        ordine.getBasePartenza().notificaOrdine(ordine);
                    }
                }
            } catch(RemoteException e) {
                System.out.println("Daaa: Errore di connessione con un ordine");
            }
        }
        rimuoviAutotreno(autotreno);
        gui.aggiornaStatoTextArea("L'autotreno " + autotreniNomi.get(autotreno)
                + " non è più attivo");
    }
    
    //metodo che rimuove un autotreno dalla lista degli autotreni attivi
    private void rimuoviAutotreno(IAutotreno autotreno) {
        synchronized(autotreniAttivi) {
            autotreniAttivi.put(autotreno, false);
        }
    }
    
    //metodo chiamato da un autotreno quando la base in cui era parcheggiato cessa la propria attività
    @Override
    public IBase impostaNuovaBase(IAutotreno autotreno) throws RemoteException {
        IBase nuovaBase = null;
        for(IBase base : basiAttive.keySet()) {
            if(basiAttive.get(base)) {
                nuovaBase = base;
            }
        }
        return nuovaBase;
    }
    
    //thread che gestisce l'invio degli ordini alle rispettive basi
    class InviaOrdini implements Runnable {
        private IBase partenza;
        private IOrdine ordine;
        
        @Override
        public void run() {
            while(!terminato) {
                try {
                    //prendo il lock sull'elenco degli ordini e controllo che non sia vuoto
                    synchronized(elencoOrdini) {
                        while(!terminato && elencoOrdini.isEmpty()) {
                            elencoOrdini.wait();
                        }
                        if(!terminato) {
                            ordine = elencoOrdini.poll();
                        }
                    }
                    if(!terminato) {
                        try {
                            partenza = ordine.getBasePartenza();
                        } catch(RemoteException e) {
                            System.out.println("DIO: Errore di comunicazione con un ordine");
                        }
                        try {
                            partenza.registraOrdine(ordine);
                        } catch(RemoteException e) {
                            System.out.println("DIO: Errore di comunicazione con la base "
                                    + basiNomi.get(partenza));
                            aggiornaBasiAttive(partenza);
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
