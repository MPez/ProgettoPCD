/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package autotreno;

import common.IBase;
import common.IDitta;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 *
 * @author marco
 */
public class AutotrenoStarter {
    private Autotreno autotreno;
    private AutotrenoGUI gui;
    
    private IDitta ditta;
    private static final String HOST = "localhost:";
    
    AutotrenoStarter(String nomeAutotreno, String nomeBasePartenza) throws RemoteException {
        try {
            ditta = (IDitta) Naming.lookup("rmi://" + HOST + "/dittaTrasporti");
        } catch (ConnectException e) {
            System.out.println("Errore di comunicazione con la ditta di trasporti");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        gui = new AutotrenoGUI(nomeAutotreno);
        autotreno = new Autotreno(nomeAutotreno, gui, ditta);
        gui.setAutotreno(autotreno);
        this.avviaGUI();
        this.avviaAutotreno();
    }
    
    //metodo che avvia la gui dell'autotreno
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    //metodo che avvia 
    private void avviaAutotreno() {
        new Thread(autotreno.new RecapitaOrdine()).start();
    }
    
    //metodo che si connette alla ditta per la registrazione dell'autotreno e
    //che si connette alla base di partenza per parcheggiarlo
    private void registra(String nomeBasePartenza) {
        try {
            IBase basePartenza = ditta.registraAutotreno(autotreno, nomeBasePartenza);
            autotreno.setBasePartenza(basePartenza);
            try {
                basePartenza.parcheggiaAutotreno(autotreno);
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con la base di partenza "
                        + "durante la registrazione dell'autotreno "
                        + autotreno.getNomeAutotreno());
            }
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta durante la "
                    + "registrazione dell'autotreno " + autotreno.getNomeAutotreno());
        }
    }
    
    public static void main(String[] args) {
        String nomeAutotreno = args[0];
        String nomeBasePartenza = args[1];
        try {
            AutotrenoStarter autotrenoStarter = new AutotrenoStarter(nomeAutotreno, nomeBasePartenza);
            autotrenoStarter.registra(nomeBasePartenza);
        } catch(RemoteException e) {
            System.out.println("Errore di connessione nella creazione dell'autotreno "
                    + nomeAutotreno);
        }
    }
}
