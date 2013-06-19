/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia remota che pubblicizza i metodi di un ordine
 * 
 * @author Pezzutti Marco 1008804
 */
public interface IOrdine extends Remote {
    /**
     * Metodo che ritorna il nome della base di partenza dell'ordine
     * 
     * @return                      nome della base di partenza
     * @throws RemoteException 
     */
    String getNomePartenza() throws RemoteException;
    
    /**
     * Metodo che ritorna il nome della base di destinazione dell'ordine
     * 
     * @return                      nome della base di destinazione
     * @throws RemoteException 
     */
    String getNomeDestinazione() throws RemoteException;
    
    /**
     * Metodo che ritorna il riferimento alla base di partenza dell'ordine
     * 
     * @return                      riferimento alla base di partenza
     * @throws RemoteException 
     */
    IBase getBasePartenza() throws RemoteException;
    
    /**
     * Metodo che ritorna il riferimento alla base di destinazione
     * 
     * @return                      riferimento alla base di destinazione
     * @throws RemoteException 
     */
    IBase getBaseDestinazione() throws RemoteException;
    
    /**
     * Metodo che ritorna il riferimento all'autotreno che consegna l'ordine
     * 
     * @return                      riferimento all'autotreno
     * @throws RemoteException 
     */
    IAutotreno getAutotreno() throws RemoteException;
    
    /**
     * Metodo che imposta l'autotreno che consegnerà l'ordine
     * 
     * @param autotreno             riferimento all'autotreno
     * @throws RemoteException 
     */
    void setAutotreno(IAutotreno autotreno) throws RemoteException;
    
    /**
     * Metodo che ritorna lo stato dell'ordine; gli stati possibili sono i seguenti:
     * ricevuto, non consegnato, in transito, consegnato, abortito
     * 
     * @return                      stringa che rappresenta lo stato dell'ordine
     * @throws RemoteException 
     */
    String getStato() throws RemoteException;
    
    /**
     * Metodo che imposta lo stato dell'ordine; gli stati possibili sono i seguenti:
     * ricevuto, non consegnato, in transito, consegnato, abortito
     * 
     * @param stato                 stringa che rappresenta lo stato dell'ordine
     * @throws RemoteException 
     */
    void setStato(String stato) throws RemoteException;
    
    /**
     * Metodo che ritorna lo stato dell'ordine
     * 
     * @return                      stringa che descrive lo stato dell'ordine
     * @throws RemoteException 
     */
    String stampaStato() throws RemoteException;
    
    /**
     * Metodo che ritorna l'esito della consegna dell'ordine
     * 
     * @return                      stringa che descrive l'esito della consegna dell'ordine
     * @throws RemoteException 
     */
    String stampaEsito() throws RemoteException;
    
    /**
     * Metodo che ritorna il numero e il nome della base di destinazione dell'ordine
     * 
     * @return                      stringa che contiene numero e destinazione dell'ordine
     * @throws RemoteException 
     */
    String stampaNumeroDestinazione() throws RemoteException;
    
    /**
     * Metodo che ritorna il numero dell'ordine e l'autotreno che lo ha consegnato
     * 
     * @return                      stringa che contiene numero d'ordine e autotreno
     * @throws RemoteException 
     */
    String stampaRicevuto() throws RemoteException;
}
