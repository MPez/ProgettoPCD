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
package base;

import common.IDitta;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Classe deputata alla creazione di una base e della relativa GUI
 * 
 * @author Pezzutti Marco
 */
public class BaseStarter {
    private Base base;
    private BaseGUI gui;
    
    private IDitta ditta;
    private static final String HOST = "localhost:";
    
    /**
     * Costruttore che recupera il riferimento remoto della Ditta di trasporti e 
     * crea la base e la sua GUI e le avvia
     * 
     * @param nomeBase              nome proprio della base
     * @throws RemoteException 
     */
    public BaseStarter(String nomeBase) throws RemoteException {
        //recupero il riferimento remoto della Ditta di trasporti
        try {
            ditta = (IDitta) Naming.lookup("rmi://" + HOST + "/dittaTrasporti");
        } catch (ConnectException e) {
            System.out.println("Errore di comunicazione con la ditta di trasporti");
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        
        gui = new BaseGUI(nomeBase);
        base = new Base(nomeBase, gui, ditta);
        gui.setBase(base);
        this.avviaGUI();
        this.avviaBase();
    }
    
    /**
     * Metodo che avvia il thread della base che effettua la consegna degli ordini
     */
    private void avviaBase() {
        new Thread(base.new ConsegnaOrdine()).start();
    }
    
    /**
     * Metodo che avvia la GUI 
     */
    private void avviaGUI() {
        new Thread(gui).start();
    }
    
    /**
     * Metodo che registra la base presso la ditta di trasporti 
     */
    private void registra() {
        try {
            ditta.registraBase(base);
        } catch(RemoteException e) {
            System.out.println("Errore di comunicazione con la ditta durante la "
                    + "registrazione della base " + base.getNomeBase());
        }
    }
    
    /**
     * Metodo main che recupera il parametro in input, crea un'istanza di BaseStarter
     * ed effettua la richiesta di registrazione della base
     * @param args 
     */
    public static void main(String[] args) {
        String nomeBase = args[0];
        try {
            BaseStarter baseStarter = new BaseStarter(nomeBase);
            baseStarter.registra();
        } catch(RemoteException e) {
            System.out.println("Errore di connessione nella creazione della base "
                    + nomeBase);
        }
    }
}
