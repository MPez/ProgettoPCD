/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package ditta;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Classe deputata alla creazione della Ditta di trasporti e alla relativa GUI
 * 
 * @author Pezzutti Marco 1008804
 */
public class DittaStarter {  
    private Ditta ditta;
    private DittaGUI gui;
    
    private static final String HOST = "localhost:";

    /**
     * Costruttore che registra la Ditta presso il registro RMI e crea la Ditta e 
     * la sua GUI e le avvia
     * 
     * @throws RemoteException 
     */
    public DittaStarter() throws RemoteException {
        gui = new DittaGUI();
        ditta = new Ditta(gui);
        gui.setDitta(ditta);
        
        avviaGUI();
        avviaDitta();
        
        //registra il nome della ditta presso l'RMI
        try {
            String rmiNomeDitta = "rmi://" + HOST + "/dittaTrasporti";
            Naming.rebind(rmiNomeDitta, ditta);
        } catch(RemoteException e) {
            System.out.println("Errore nella registrazione della ditta con il registro RMI");
        } catch(MalformedURLException e1) {
            e1.printStackTrace();
        }
    }
    
    /**
     * Metodo che avvia l'interfaccia grafica
     */
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    /**
     * Metodo che avvia il thread della Ditta che effettua l'invio degli ordini
     */
    private void avviaDitta() {
        new Thread(ditta.new InviaOrdini()).start();
    }
    
    /**
     * Metodo main che crea un'istanza di DittaStarter
     * @param args 
     */
    public static void main(String[] args) {
        try {
            new DittaStarter();
        } catch(RemoteException e) {
            System.out.println("Errore di connessione nella creazione della ditta");
        }
    }
}
