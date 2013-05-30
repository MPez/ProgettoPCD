/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
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
    private final IBase partenza;
    private final IBase destinazione;
    private IAutotreno autotreno;
    private String stato;
    private final int numeroOrdine;
    
    private static int totaleOrdini = 0;

    public Ordine (IBase partenza, IBase destinazione) throws RemoteException {
        this.partenza = partenza;
        this.destinazione = destinazione;
        
        autotreno = null;
        stato = "ricevuto";
        totaleOrdini +=1;
        numeroOrdine = totaleOrdini;
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
    public String getStato() {
        return stato;
    }
    
    @Override
    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public String stampaStato() {
        String s;
        s = "Ordine " + getNumeroOrdine() + " " + getStato();
        return s;
    }

    @Override
    public String stampaEsito() throws RemoteException {
        String e;
        e = "Ordine da " + getBasePartenza().getNomeBase() + " a " 
                + getBaseDestinazione().getNomeBase() + " " + getStato()
                + " da " + getAutotreno().getNomeAutotreno();
        return e;
    }

    @Override
    public String stampaNumeroDestinazione() throws RemoteException {
        String s;
        s = getNumeroOrdine() + ": per " + getBaseDestinazione().getNomeBase()
                + " " + getStato();
        return s;
    }
}
