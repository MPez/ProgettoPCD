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
 * Classe che rappresenta un ordine da consegnare
 * 
 * @author Pezzutti Marco 1008804
 */
class Ordine extends UnicastRemoteObject implements IOrdine {
    private final IBase partenza;
    private String nomeBasePartenza;
    
    private final IBase destinazione;
    private String nomeBaseDestinazione;
    
    private IAutotreno autotreno;
    private String nomeAutotreno;
    
    private String stato;
    private final int numeroOrdine;
    
    private static int totaleOrdini = 0;

    /**
     * Costruttore che imposta la base di partenza e di destinazione passate come 
     * paramentri, imposta lo stato iniziale dell'ordine e il numero d'ordine
     * 
     * @param partenza              riferimento alla base di partenza
     * @param destinazione          riferimento alla base di destinazione
     * @throws RemoteException 
     */
    Ordine (IBase partenza, IBase destinazione) throws RemoteException {
        this.partenza = partenza;
        this.destinazione = destinazione;
        
        stato = "ricevuto";
        totaleOrdini +=1;
        numeroOrdine = totaleOrdini;
        
        //recupero i nomi delle basi di partenza e destinazione
        try {
            nomeBasePartenza = this.partenza.getNomeBase();
            nomeBaseDestinazione = this.destinazione.getNomeBase();
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con una base in fase "
                    + "di creazione dell'ordine " + getNumeroOrdine());
        }
    }
    
    @Override
    public final String getNomePartenza() {
        return nomeBasePartenza;
    }
    
    @Override
    public final String getNomeDestinazione() {
        return nomeBaseDestinazione;
    }
    
    /**
     * Metodo che imposta il nome dell'autotreno che consegnerà l'ordine
     * 
     * @param nomeAutotreno         nome dell'autotreno
     */
    private void setNomeAutotreno(final String nomeAutotreno) {
        this.nomeAutotreno = nomeAutotreno;
    }
    
    /**
     * Metodo che ritorna il nome dell'autotreno che consegna l'ordine
     * 
     * @return                      nome dell'autotreno
     */
    private String getNomeAutotreno() {
        return nomeAutotreno;
    }
    
    /**
     * Metodo che ritorna il numero d'ordine
     * 
     * @return                      numero d'ordine
     */
    private int getNumeroOrdine() {
        return numeroOrdine;
    }

    @Override
    public final IBase getBasePartenza() {
        return partenza;
    }

    @Override
    public final IBase getBaseDestinazione() {
        return destinazione;
    }
    
    @Override
    public final IAutotreno getAutotreno() {
        return autotreno;
    }
    
    @Override
    public final void setAutotreno(final IAutotreno autotreno) {
        this.autotreno = autotreno;
        try {
            setNomeAutotreno(autotreno.getNomeAutotreno());
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con l'autotreno dell'ordine "
                    + getNumeroOrdine());
        }
    }
    
    @Override
    public final String getStato() {
        return stato;
    }
    
    @Override
    public final void setStato(final String stato) {
        this.stato = stato;
    }

    @Override
    public String stampaStato() {
        return "Ordine " + getNumeroOrdine() + " " + getStato();
    }

    @Override
    public String stampaEsito() throws RemoteException {
        return "Ordine " + getNumeroOrdine() + " da " + getNomePartenza() + " a " 
                + getNomeDestinazione() + " " + getStato()
                + " da " + getNomeAutotreno();
    }

    @Override
    public String stampaNumeroDestinazione() throws RemoteException {
        return getNumeroOrdine() + ": per " + getNomeDestinazione() + " " + getStato();
    }

    @Override
    public String stampaRicevuto() throws RemoteException {
        return "Ricevuto ordine " + getNumeroOrdine() + " da " + getNomeAutotreno();
    }
}
