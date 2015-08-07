/**
 *
 * @author Antonio Espinosa Jiménez
 */
public interface InterfaceCliente extends java.rmi.Remote {
    
    public void actualizarCliente() throws java.rmi.RemoteException;
    
    public void actualizarMensajes(String mensaje) throws java.rmi.RemoteException;
}
