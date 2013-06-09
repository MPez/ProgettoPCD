/*
 * Pezzutti Marco 1008804
 * progetto per l'insegnamento di Programmazione Concorrente e Distribuita
 */
package common;

import java.rmi.*;

/**
 *
 * @author marco
 */
public interface IAutotreno extends Remote {
    String getNomeAutotreno()throws RemoteException;
    void registraOrdine(IOrdine ordine) throws RemoteException;
    void parcheggiaAutotreno(IBase destinazione) throws RemoteException;
    void aggiornaBasePartenza() throws RemoteException;
    boolean getViaggioEseguito() throws RemoteException;
    boolean stato() throws RemoteException;
    void terminaAttivita() throws RemoteException;
}
