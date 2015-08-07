
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public class Servidor implements InterfaceServidor {

    private ArrayList<InterfaceCliente> listaClientes;
    private ArrayList<String> nombresClientes;

    public Servidor() throws RemoteException {
        listaClientes = new ArrayList<>();
        nombresClientes = new ArrayList<>();
    }

    /**
     * Registra un nuevo cliente en el servidor y se lo indica a los demás
     * @param cliente El nuevo cliente a registrar
     * @param nombre El nombre del nuevo cliente a registrar
     * @throws RemoteException 
     */
    @Override
    public void registrar(InterfaceCliente cliente, String nombre) throws RemoteException {
        listaClientes.add(cliente);
        nombresClientes.add(nombre);

        for (InterfaceCliente c : listaClientes) {
            c.actualizarCliente();
        }
        
        difundirMensaje(nombre + " SE HA CONECTADO AL CHAT");
    }

    /**
     * Desconectar un cliente del servidor y se lo indica a los demás
     * @param cliente El cliente a desconectar
     * @param nombre El nombre del cliente a desconectar
     * @throws RemoteException 
     */
    @Override
    public void desconectar(InterfaceCliente cliente, String nombre) throws RemoteException {
        listaClientes.remove(cliente);
        nombresClientes.remove(nombre);

        for (InterfaceCliente c : listaClientes) {
            c.actualizarCliente();
        }
        
        difundirMensaje(nombre + " SE HA DESCONECTADO DEL CHAT");
    }

    /**
     * Obtiene la lista de los clientes conectados
     * @return
     * @throws RemoteException 
     */
    @Override
    public ArrayList<String> getClientes() throws RemoteException {
        return nombresClientes;
    }
    
    @Override
    public boolean nombreLibre(String nombre) throws RemoteException{
        return !nombresClientes.contains(nombre);
    }

    /**
     * Manda un mensaje a todos los clientes conectados
     * @param mensaje El mensaje a mandar
     * @throws RemoteException 
     */
    @Override
    public void difundirMensaje(String mensaje) throws RemoteException {
        for (InterfaceCliente listaCliente : listaClientes) {
            listaCliente.actualizarMensajes(mensaje);
        }
    }

    public static void main(String[] args) throws RemoteException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            InterfaceServidor interfaceServidor = new Servidor();
            InterfaceServidor stub
                    = (InterfaceServidor) UnicastRemoteObject.exportObject(interfaceServidor, 0);
            Registry registry = LocateRegistry.getRegistry();
            String nombre_objeto_remoto = "un_nombre_para_obj_remoto";
            registry.rebind(nombre_objeto_remoto, stub);
            System.out.println("Servidor ejecutándose...");
        } catch (Exception e) {
            System.err.println("SERVIDOR exception:");
            e.printStackTrace();
        }
    }

}
