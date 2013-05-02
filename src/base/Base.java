/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import common.IAutotreno;
import common.IBase;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 *
 * @author marco
 */
public class Base implements IBase {
    private String nomeBase;
    private LinkedList<IBase> listaDestinazioni;
    private LinkedList<IAutotreno> listaAutotreni;
    private LinkedList<IBase> basiAttive;
    private LinkedList<IAutotreno> autotreniAttivi;
    
    private BaseGUI gui;

    Base(String nomeBase, BaseGUI gui) {
        this.nomeBase = nomeBase;
        this.gui = gui;
        
    }
    
    @Override
    public String getNomeBase() {
        return nomeBase;
    }

    @Override
    public synchronized void registraOrdine(IBase destinazione) {
        listaDestinazioni.add(destinazione);
    }

    @Override
    public void riceviMerce(IAutotreno autotreno) {
        parcheggia(autotreno);
    }

    @Override
    public boolean stato()  {
        return true;
    }

    @Override
    public void terminaAttività() {
        
    }
    
    private synchronized void parcheggia(IAutotreno autotreno) {
        listaAutotreni.add(autotreno);
    }
    
    private class ConsegnaOrdine implements Runnable {
        private IBase destinazione;
        private IAutotreno autotreno;
        
        ConsegnaOrdine(IBase destinazione, IAutotreno autotreno) {
            this.destinazione = destinazione;
            this.autotreno = autotreno;
        }
        
        @Override
        public void run() {
            
        }
    }
    
}
