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

    public DittaStarter() {
        gui = new DittaGUI();
        ditta = new Ditta(gui);
        gui.setDitta(ditta);
        
        try {
            String rmiNomeDitta = "rmi://" + HOST + "/dittaTrasporti"; Naming.rebind(rmiNomeDitta, ditta);
        } catch( RemoteException | MalformedURLException e) {
            System.out.println("Errore nella registrazione della ditta con il registro RMI.");
        }
        
        this.avviaGUI();
        this.avviaDitta();
    }
    
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    private void avviaDitta() {
        new Thread(ditta.new InviaOrdini()).start();
    }
    
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        DittaStarter dittaStarter = new DittaStarter();
    }
}
