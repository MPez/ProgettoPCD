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
public interface IDitta extends Remote{
    void registraBase(IBase base) throws RemoteException;
    IBase registraAutotreno(IAutotreno autotreno, String nomeBasePartenza) throws RemoteException;
    void notificaEsito(IOrdine ordine) throws RemoteException;
    void aggiornaBasiAttive(IBase base) throws RemoteException;
    void aggiornaAutotreniAttivi(IAutotreno autotreno) throws RemoteException;
    IBase impostaNuovaBase(IAutotreno autotreno) throws RemoteException;
}
