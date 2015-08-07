import java.util.ArrayList;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public interface InterfaceServidor extends java.rmi.Remote {

    public void registrar(InterfaceCliente cliente, String nombre) throws java.rmi.RemoteException;
    
    public void desconectar(InterfaceCliente cliente, String nombre) throws java.rmi.RemoteException;
    
    public void difundirMensaje(String mensaje) throws java.rmi.RemoteException;
    
    public ArrayList<String> getClientes() throws java.rmi.RemoteException;
    
    public boolean nombreLibre(String nombre) throws java.rmi.RemoteException;

}
