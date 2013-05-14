/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import common.IAutotreno;
import common.IBase;
import common.IOrdine;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author marco
 */
public class Ordine extends UnicastRemoteObject implements IOrdine {
    private IBase partenza;
    private IBase destinazione;
    private IAutotreno autotreno;
    private boolean consegnato;
    
    private static int numeroOrdine = 0;

    public Ordine (IBase partenza, IBase destinazione) throws RemoteException {
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.autotreno = null;
        this.consegnato = false;
        
        numeroOrdine += 1;
    }

    @Override
    public IBase getBasePartenza() {
        return partenza;
    }

    @Override
    public IBase getBaseDestinazione() {
        return destinazione;
    }
    
    @Override
    public int getNumeroOrdine() {
        return numeroOrdine;
    }
    
    @Override
    public IAutotreno getAutotreno() {
        return autotreno;
    }
    
    @Override
    public void setAutotreno(IAutotreno autotreno) {
        this.autotreno = autotreno;
    }
    
    @Override
    public boolean getConsegnato() {
        return consegnato;
    }
    
    @Override
    public void setConsegnato(boolean consegnato) {
        this.consegnato = consegnato;
    }
}
