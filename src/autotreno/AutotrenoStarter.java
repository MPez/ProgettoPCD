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
package autotreno;

import common.IBase;
import common.IDitta;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Classe deputata alla creazione di un autotreno e della relativa GUI
 * 
 * @author Pezzutti Marco 1008804
 */
public class AutotrenoStarter {
    private Autotreno autotreno;
    private AutotrenoGUI gui;
    
    private IDitta ditta;
    private static final String HOST = "localhost:";
    
    /**
     * Costruttore che recupera il riferimento remoto della Ditta di trasporti e 
     * crea l'autotreno e la sua GUI e le avvia
     * 
     * @param nomeAutotreno         
     * @param nomeBasePartenza
     * @throws RemoteException 
     */
    public AutotrenoStarter(String nomeAutotreno, String nomeBasePartenza) throws RemoteException {
        //recupero il riferimento remoto della Ditta di trasporti
        try {
            ditta = (IDitta) Naming.lookup("rmi://" + HOST + "/dittaTrasporti");
        } catch (ConnectException e) {
            System.out.println("Errore di comunicazione con la ditta di trasporti");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        gui = new AutotrenoGUI(nomeAutotreno);
        autotreno = new Autotreno(nomeAutotreno, gui, ditta);
        gui.setAutotreno(autotreno);
        this.avviaGUI();
        this.avviaAutotreno();
    }
    
    /**
     * Metodo che avvia la gui dell'autotreno
     */
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    /**
     * Metodo che avvia il thread dell'autotreno che effettua il recapito degli organi
     */
    private void avviaAutotreno() {
        new Thread(autotreno.new RecapitaOrdine()).start();
    }
    
    /**
     * Metodo che si connette alla Ditta per la registrazione dell'autotreno e 
     * che si connette alla base di partenza per parcheggiarlo
     * 
     * @param nomeBasePartenza          nome della base di partenza
     */
    private void registra(String nomeBasePartenza) {
        try {
            IBase basePartenza = ditta.registraAutotreno(autotreno, nomeBasePartenza);
            autotreno.setBasePartenza(basePartenza);
            try {
                basePartenza.parcheggiaAutotreno(autotreno);
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione con la base di partenza "
                        + "durante la registrazione dell'autotreno "
                        + autotreno.getNomeAutotreno());
            }
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta durante la "
                    + "registrazione dell'autotreno " + autotreno.getNomeAutotreno());
        }
    }
    
    /**
     * Metodo main che recupera i paramentri in input, crea un istanza di AutotrenoStarter
     * ed effettua la richiesta di registrazione dell'autotreno
     * 
     * @param args[0]                   nome dell'autotreno
     * @param args[1]                   nome della base di partenza
     */
    public static void main(String[] args) {
        String nomeAutotreno = args[0];
        String nomeBasePartenza = args[1];
        try {
            AutotrenoStarter autotrenoStarter = new AutotrenoStarter(nomeAutotreno, nomeBasePartenza);
            autotrenoStarter.registra(nomeBasePartenza);
        } catch(RemoteException e) {
            System.out.println("Errore di connessione nella creazione dell'autotreno "
                    + nomeAutotreno);
        }
    }
}
