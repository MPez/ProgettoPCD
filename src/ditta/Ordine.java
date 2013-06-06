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
    private String nomeBasePartenza = "";
    
    private final IBase destinazione;
    private String nomeBaseDestinazione = "";
    
    private IAutotreno autotreno;
    private String nomeAutotreno;
    
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
        
        try {
            nomeBasePartenza = this.partenza.getNomeBase();
            nomeBaseDestinazione = this.destinazione.getNomeBase();
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con una base in fase "
                    + "di creazione dell'ordine " + getNumeroOrdine());
        }
    }
    
    @Override
    public String getNomePartenza() {
        return nomeBasePartenza;
    }
    
    @Override
    public String getNomeDestinazione() {
        return nomeBaseDestinazione;
    }
    
    private void setNomeAutotreno(String nomeAutotreno) {
        this.nomeAutotreno = nomeAutotreno;
    }
    
    private String getNomeAutotreno() {
        return nomeAutotreno;
    }
    
    private int getNumeroOrdine() {
        return numeroOrdine;
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
    public IAutotreno getAutotreno() {
        return autotreno;
    }
    
    @Override
    public void setAutotreno(IAutotreno autotreno) {
        this.autotreno = autotreno;
        try {
            setNomeAutotreno(autotreno.getNomeAutotreno());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno dell'ordine "
                    + getNumeroOrdine());
        }
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
        e = "Ordine " + getNumeroOrdine() + " da " 
                + getNomePartenza() + " a " 
                + getNomeDestinazione() + " " + getStato()
                + " da " + getNomeAutotreno();
        return e;
    }

    @Override
    public String stampaNumeroDestinazione() throws RemoteException {
        String s;
        s = getNumeroOrdine() + ": per " + getNomeDestinazione()
                + " " + getStato();
        return s;
    }
}
