/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package common;

import java.rmi.*;

/**
 * Interfaccia remota che pubblicizza i metodi di un autotreno
 * 
 * @author Pezzutti Marco 1008804
 */
public interface IAutotreno extends Remote {
    /**
     * Metodo che ritorna il nome dell'autotreno
     * 
     * @return                      nome dell'autotreno
     * @throws RemoteException 
     */
    String getNomeAutotreno()throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta di trasporti per registrare un nuovo ordine
     * 
     * @param ordine                riferimento al nuovo ordine da registrare
     * @throws RemoteException 
     */
    void registraOrdine(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione per parcheggiare l'autotreno presso 
     * di essa
     * 
     * @param destinazione          riferimento alla base di destinazione
     * @throws RemoteException 
     */
    void parcheggiaAutotreno(IBase destinazione) throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta di trasporti quando la base dove è parcheggiato 
     * l'autotreno non è più attiva; richiede una nuova base dove parcheggiarsi
     * 
     * @throws RemoteException 
     */
    void aggiornaBasePartenza() throws RemoteException;
    
    /**
     * Metodo chiamato dalla ditta per controllare se l'autotreno è in viaggio
     * 
     * @return                      true se è in viaggio, false altrimenti
     * @throws RemoteException 
     */
    boolean getViaggioEseguito() throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta per testare l'attività dell'autotreno; ritorna 
     * sempre true tranne quando non è più attiva, in quel caso viene lanciata 
     * l'eccezione, sinonimo della cessata attività della base
     * 
     * @return                      true, ovvero che l'autotreno è attivo
     * @throws RemoteException 
     */
    boolean stato() throws RemoteException;
    
    /**
     * Metodo che termina l'attività dell'autotreno
     * 
     * @throws RemoteException 
     */
    void terminaAttivita() throws RemoteException;
}
