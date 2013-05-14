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
public interface IAutotreno extends Remote {
    String getNomeAutotreno()throws RemoteException;
    void registraOrdine(IOrdine ordine) throws RemoteException;
    void parcheggiaAutotreno(IBase destinazione) throws RemoteException;
    boolean stato() throws RemoteException;
    void terminaAttivita() throws RemoteException;
}
