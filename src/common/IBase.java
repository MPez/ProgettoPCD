/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.rmi.*;

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
    boolean stato() throws RemoteException;
    void terminaAttivita() throws RemoteException;
}
