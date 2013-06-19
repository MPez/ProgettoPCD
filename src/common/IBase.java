/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia remota che pubblicizza i metodi di una base artica
 * 
 * @author Pezzutti Marco 1008804
 */
public interface IBase extends Remote {
    /**
     * Metodo che ritorna il nome della base
     * @return                      nome della base
     * @throws RemoteException 
     */
    String getNomeBase() throws RemoteException;
    
    /**
     * Metodo ciamato dalla Ditta di trasporti che inserisce l'ordine nella lista 
     * degli ordini da evadere
     * 
     * @param ordine                riferimento al nuovo ordine da inserire
     * @throws RemoteException 
     */
    void registraOrdine(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione per avvisare la base di partenza 
     * dell'avvenuta ricezione dell'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine consegnato
     * @throws RemoteException 
     */
    void ordineConsegnato(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dall'autotreno che effettua l'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine in consegna
     * @throws RemoteException 
     */
    void riceviMerce(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione alla ricezione dell'ordine per 
     * parcheggiare l'autotreno passato come parametro
     * 
     * @param autotreno             riferimento all'autotreno che ha consegnato l'ordine
     * @throws RemoteException 
     */
    void parcheggiaAutotreno(IAutotreno autotreno) throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta di trasporti quando un'autotreno cessa la propria 
     * attività; si occupa di rimuovere dalla lista l'autotreno passato come parametro
     * 
     * @param autotreno             riferimento all'autotreno non più attivo
     * @throws RemoteException 
     */
    void aggiornaListaAutotreni(IAutotreno autotreno) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione per avvisare la base di partenza 
     * dell'avvenuta consegna dell'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine consegnato
     * @throws RemoteException 
     */
    void notificaOrdine(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta per testare l'attività di una base, ritorna sempre 
     * true tranne quando non è più attiva, in quel caso viene lanciata l'eccezione, 
     * sinonimo della cessata attività della base
     * 
     * @return                  true, ovvero che la base è attiva
     * @throws RemoteException 
     */
    boolean stato() throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta o dalla GUI per terminare correttamente l'attività
     * della base
     * 
     * @throws RemoteException 
     */
    void terminaAttivita() throws RemoteException;
}
