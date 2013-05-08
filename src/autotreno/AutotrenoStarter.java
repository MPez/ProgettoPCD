/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        
        gui = new AutotrenoGUI(nomeAutotreno);
        autotreno = new Autotreno(nomeAutotreno, nomeBasePartenza, gui);
        gui.setAutotreno(autotreno);
        this.avviaGUI();
    }
    
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    private void registra(String nomeBasePartenza) {
        try {
            IBase basePartenza = ditta.registraAutotreno(autotreno, nomeBasePartenza);
            autotreno.setBasePartenza(basePartenza);
            basePartenza.parcheggiaAutotreno(autotreno);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta durante la "
                    + "registrazione dell'autotreno" + autotreno.getNomeAutotreno() + ".");
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
                    + nomeAutotreno + ".");
        }
    }
}
