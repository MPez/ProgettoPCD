/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author marco
 */
public class Ditta implements IDitta{
    private LinkedList<IBase> basiAttive;
    private LinkedList<IAutotreno> autotreniAttivi;
    
    private DittaGUI gui;
    
    private boolean terminato;

    @Override
    public void registraBase(IBase base) {
        synchronized(basiAttive) {
            basiAttive.add(base);
        }
    }

    @Override
    public IBase registraAutotreno(IAutotreno autotreno, String nomeBasePartenza) {
        synchronized(autotreniAttivi) {
            autotreniAttivi.add(autotreno);
        }
        IBase partenza = null;
        Iterator<IBase> it = basiAttive.iterator();
        while(it.hasNext()) {
            try {
                String nome = it.next().getNomeBase();
                if(nome.equals(nomeBasePartenza)) {
                    partenza = it.next();
                }
            } catch(RemoteException e) {
                System.out.println("Errore di comunicazione della ditta con la base.");
            }
        }
        return partenza;
    }

    @Override
    public void notificaEsito(IBase partenza, IBase destinazione, IAutotreno autotreno, boolean esito) {
        
    }

    @Override
    public void aggiornaBasiAttive(IBase base) throws RemoteException {
        
    }

    @Override
    public void aggiornaAutotreniAttivi(IAutotreno autotreno) throws RemoteException {
        
    }
    
}
