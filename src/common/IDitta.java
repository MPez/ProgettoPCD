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
    IBase registraAutotreno(IAutotreno autotreno, String nomeBasePartenza) throws RemoteException;
    void notificaEsito(IOrdine ordine) throws RemoteException;
    void aggiornaBasiAttive(IBase base) throws RemoteException;
    void aggiornaAutotreniAttivi(IAutotreno autotreno) throws RemoteException;
}
