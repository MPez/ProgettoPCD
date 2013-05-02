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
public interface IDitta extends Remote{
    void registraBase(IBase base) throws RemoteException;
    IBase registraAutotreno(IAutotreno autotreno) throws RemoteException;
    void notificaEsito(IBase partenza, IBase destinazione, IAutotreno autotreno, boolean esito) 
            throws RemoteException;
}
