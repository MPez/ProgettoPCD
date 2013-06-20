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
 * Interfaccia remota che pubblicizza i metodi della Ditta di trasporti
 * 
 * @author Pezzutti Marco 1008804
 */
public interface IDitta extends Remote{
    /**
     * Metodo chiamato da una base in fase di registrazione, si occupa di inserire 
     * la base passata come parametro nella lista delle basi attive
     * 
     * @param base                  riferimento alla base che chiede di registrarsi
     * @throws RemoteException 
     */
    void registraBase(IBase base) throws RemoteException;
    
    /**
     * Metodo chiamato da un autotreno in fase di registrazione, si occupa di inserire
     * l'autotreno passato come parametro nella lista degli autotreni attivi solo dopo
     * che la base presso cui vuole parcheggiarsi sia attiva
     * 
     * @param autotreno             riferimento all'autotreno che chiede di registrarsi
     * @param nomeBasePartenza      nome della base presso cui l'autotreno chiede di parcheggiarsi
     * @return                      riferimento alla base di partenza presso cui parcheggiarsi
     * @throws RemoteException 
     */
    IBase registraAutotreno(IAutotreno autotreno, String nomeBasePartenza) throws RemoteException;
    
    /**
     * Metodo chiamato da una base al momento della consegna di un ordine, notifica alla Ditta
     * l'avvenuta consegna oppure la mancata consegna dell'ordine passato come parametro
     * 
     * @param ordine                riferimento all'ordine di cui si vuole notificare l'esito
     * @throws RemoteException 
     */
    void notificaEsito(IOrdine ordine) throws RemoteException;
    
    /**
     * Metodo chiamato da un autotreno quando la base in cui era parcheggiato cessa la propria attività,
     * si occupa di cercare una base attiva presso cui parcheggiare l'autotreno
     * 
     * @param autotreno             riferimento all'autotreno che ha perso la base dove era parcheggiato
     * @return                      riferimento alla nuova base presso cui parcheggiarsi
     * @throws RemoteException 
     */
    IBase impostaNuovaBase(IAutotreno autotreno) throws RemoteException;
}
