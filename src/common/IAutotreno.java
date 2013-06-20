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

import java.rmi.*;

/**
 * Interfaccia remota che pubblicizza i metodi di un autotreno
 * 
 * @author Pezzutti Marco 1008804
 */
public interface IAutotreno extends Remote {
    /**
     * Metodo che ritorna il nome dell'autotreno
     * 
     * @return                      nome dell'autotreno
     * @throws RemoteException 
     */
    String getNomeAutotreno()throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta di trasporti per registrare un nuovo ordine
     * 
     * @param ordine                riferimento al nuovo ordine da registrare
     * @throws RemoteException 
     */
    void registraOrdine(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato dalla base di destinazione per parcheggiare l'autotreno presso 
     * di essa
     * 
     * @param destinazione          riferimento alla base di destinazione
     * @throws RemoteException 
     */
    void parcheggiaAutotreno(IBase destinazione) throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta di trasporti quando la base dove è parcheggiato 
     * l'autotreno non è più attiva; richiede una nuova base dove parcheggiarsi
     * 
     * @throws RemoteException 
     */
    void aggiornaBasePartenza() throws RemoteException;
    
    /**
     * Metodo chiamato dalla ditta per controllare se l'autotreno è in viaggio
     * 
     * @return                      true se è in viaggio, false altrimenti
     * @throws RemoteException 
     */
    boolean getViaggioEseguito() throws RemoteException;
    
    /**
     * Metodo chiamato dalla Ditta per testare l'attività dell'autotreno; ritorna 
     * sempre true tranne quando non è più attiva, in quel caso viene lanciata 
     * l'eccezione, sinonimo della cessata attività della base
     * 
     * @return                      true, ovvero che l'autotreno è attivo
     * @throws RemoteException 
     */
    boolean stato() throws RemoteException;
    
    /**
     * Metodo che termina l'attività dell'autotreno
     * 
     * @throws RemoteException 
     */
    void terminaAttivita() throws RemoteException;
}
