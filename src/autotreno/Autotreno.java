/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotreno;

import common.IAutotreno;
import common.IBase;
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
    
    private boolean terminato;
    private boolean viaggioEseguito;
    
    Autotreno(String nomeAutotreno, AutotrenoGUI gui) throws RemoteException {
        this.nomeAutotreno = nomeAutotreno;
        this.gui = gui;
        terminato = false;
        viaggioEseguito = false;
        
        listaOrdini = new LinkedList<IOrdine>();
    }
    
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
    
    @Override
    public void registraOrdine(IOrdine ordine) {
        synchronized(listaOrdini) {
            listaOrdini.add(ordine);
            listaOrdini.notify();
        }
    }
    
    @Override
    public void parcheggiaAutotreno(IBase destinazione) {
        basePartenza = destinazione;
        setBasePartenza(basePartenza);
        gui.setDestinazioneTextField("");
    }
    
    @Override
    public boolean stato() {
        return true;
    }
    
    @Override
    public void terminaAttivita() {
        terminato = true;
        synchronized(listaOrdini) {
            listaOrdini.notify();
        }
        gui.dispose();
        System.exit(0);
    }
    
    class ConsegnaOrdine implements Runnable {
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
                                baseDestinazione = ordine.getBaseDestinazione();
                                eseguiViaggio();
                                while(!viaggioEseguito) {
                                    Thread.currentThread().sleep(1000);
                                }
                                baseDestinazione.riceviMerce(ordine);
                            } catch(RemoteException e) {
                                System.out.println("Errore di comunicazione con la base "
                                        + "di destinazione");
                            }
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void eseguiViaggio() {
        viaggio = new Viaggio();
        viaggio.addPropertyChangeListener(gui);
        viaggio.execute();
    }
    
    class Viaggio extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
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
        }
    }
}
