/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import common.IDitta;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 *
 * @author marco
 */
public class BaseStarter {
    private Base base;
    private BaseGUI gui;
    
    private IDitta ditta;
    private static final String HOST = "localhost:";
    
    BaseStarter(String nomeBase) throws RemoteException {
        try {
            ditta = (IDitta) Naming.lookup("rmi://" + HOST + "/dittaTrasporti");
        } catch (ConnectException e) {
            System.out.println("Errore di comunicazione con la ditta di trasporti");
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        
        gui = new BaseGUI(nomeBase);
        base = new Base(nomeBase, gui);
        gui.setBase(base);
        this.avviaGUI();
        this.avviaBase();
    }
    
    private void avviaBase() {
        new Thread(base.new ConsegnaOrdine()).start();
    }
    
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    private void registra() {
        try {
            ditta.registraBase(base);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta durante la "
                    + "registrazione della base " + base.getNomeBase() + ".");
        }
    }
    
    public static void main(String[] args) {
        String nomeBase = args[0];
        try {
            BaseStarter baseStarter = new BaseStarter(nomeBase);
            baseStarter.registra();
        } catch(RemoteException e) {
            System.out.println("Errore di connessione nella creazione della base "
                    + nomeBase + ".");
        }
    }
}
