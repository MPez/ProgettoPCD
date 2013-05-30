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
public interface IBase extends Remote {
    String getNomeBase() throws RemoteException;
    void registraOrdine(IOrdine ordine) throws RemoteException;
    void ordineConsegnato(IOrdine ordine) throws RemoteException;
    void riceviMerce(IOrdine ordine) throws RemoteException;
    void parcheggiaAutotreno(IAutotreno autotreno) throws RemoteException;
    void aggiornaListaAutotreni(IAutotreno autotreno) throws RemoteException;
    boolean stato() throws RemoteException;
    void terminaAttivita() throws RemoteException;
}
