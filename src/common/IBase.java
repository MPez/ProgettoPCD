/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import common.IAutotreno;
import java.rmi.*;

/**
 *
 * @author marco
 */
public interface IBase extends Remote {
    String getNomeBase() throws RemoteException;
    void registraOrdine(IBase destinazione) throws RemoteException;
    void ordineConsegnato(IBase destinazione) throws RemoteException;
    void riceviMerce(IBase partenza, IAutotreno autotreno) throws RemoteException;
    void parcheggiaAutotreno(IAutotreno autotreno) throws RemoteException;
    boolean stato() throws RemoteException;
    void terminaAttivita() throws RemoteException;
}
