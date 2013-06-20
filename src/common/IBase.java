/*
 * Copyright (c) 2013 Pezzutti Marco
 * 
 * This file is part of Sistema Trasporti Artici (progetto per l'insegnamento 
 * di Programmazione Concorrente e Distribuita).

 * Sistema Trasporti Artici is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sistema Trasporti Artici is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sistema Trasporti Artici.  If not, see <http://www.gnu.org/licenses/>.
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia remota che pubblicizza i metodi di una base artica
 * 
 * @author Pezzutti Marco 1008804
 */
public interface IBase extends Remote {
    /**
     * Metodo che ritorna il nome della base
     * @return                      nome della base
     * @throws RemoteException 
     */
    String getNomeBase() throws RemoteException;
    
    /**
     * Metodo ciamato dalla Ditta di trasporti che inserisce l'ordine nella lista 
     * degli ordini da evadere
     * 
     * @param ordine                riferimento al nuovo ordine da inserire
     * @throws RemoteException 
     */
    void registraOrdine(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione per avvisare la base di partenza 
     * dell'avvenuta ricezione dell'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine consegnato
     * @throws RemoteException 
     */
    void ordineConsegnato(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dall'autotreno che effettua l'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine in consegna
     * @throws RemoteException 
     */
    void riceviMerce(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione alla ricezione dell'ordine per 
     * parcheggiare l'autotreno passato come parametro
     * 
     * @param autotreno             riferimento all'autotreno che ha consegnato l'ordine
     * @throws RemoteException 
     */
    void parcheggiaAutotreno(IAutotreno autotreno) throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta di trasporti quando un'autotreno cessa la propria 
     * attività; si occupa di rimuovere dalla lista l'autotreno passato come parametro
     * 
     * @param autotreno             riferimento all'autotreno non più attivo
     * @throws RemoteException 
     */
    void aggiornaListaAutotreni(IAutotreno autotreno) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione per avvisare la base di partenza 
     * dell'avvenuta consegna dell'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine consegnato
     * @throws RemoteException 
     */
    void notificaOrdine(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta per testare l'attività di una base, ritorna sempre 
     * true tranne quando non è più attiva, in quel caso viene lanciata l'eccezione, 
     * sinonimo della cessata attività della base
     * 
     * @return                  true, ovvero che la base è attiva
     * @throws RemoteException 
     */
    boolean stato() throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta o dalla GUI per terminare correttamente l'attività
     * della base
     * 
     * @throws RemoteException 
     */
    void terminaAttivita() throws RemoteException;
}
