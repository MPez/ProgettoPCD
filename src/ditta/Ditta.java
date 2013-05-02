/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ditta;

import common.IAutotreno;
import common.IBase;
import common.IDitta;
import java.rmi.RemoteException;

/**
 *
 * @author marco
 */
public class Ditta implements IDitta{
    

    @Override
    public void registraBase(IBase base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IBase registraAutotreno(IAutotreno autotreno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notificaEsito(IBase partenza, IBase destinazione, IAutotreno autotreno, boolean esito) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
