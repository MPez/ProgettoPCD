/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 *
 * @author marco
 */
public class DittaStarter {  
    private Ditta ditta;
    private DittaGUI gui;
    
    private static final String HOST = "localhost:";

    public DittaStarter() throws RemoteException {
        gui = new DittaGUI();
        ditta = new Ditta(gui);
        gui.setDitta(ditta);
        
        avviaGUI();
        avviaDitta();
        
        try {
            String rmiNomeDitta = "rmi://" + HOST + "/dittaTrasporti";
            Naming.rebind(rmiNomeDitta, ditta);
        } catch(RemoteException e) {
            System.out.println("Errore nella registrazione della ditta con il registro RMI");
        } catch(MalformedURLException e1) {
            e1.printStackTrace();
        }
    }
    
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    private void avviaDitta() {
        new Thread(ditta.new InviaOrdini()).start();
    }
    
    public static void main(String[] args) {
        try {
            new DittaStarter();
        } catch(RemoteException e) {
            System.out.println("Errore di connessione nella creazione della ditta");
        }
    }
}
