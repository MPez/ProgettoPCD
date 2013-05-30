/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author marco
 */
public interface IOrdine extends Remote {
    IBase getBasePartenza() throws RemoteException;
    IBase getBaseDestinazione() throws RemoteException;
    int getNumeroOrdine() throws RemoteException;
    IAutotreno getAutotreno() throws RemoteException;
    void setAutotreno(IAutotreno autotreno) throws RemoteException;
    String getStato() throws RemoteException;
    void setStato(String stato) throws RemoteException;
    String stampaStato() throws RemoteException;
    String stampaEsito() throws RemoteException;
    String stampaNumeroDestinazione() throws RemoteException;
}
