
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public class Cliente implements InterfaceCliente {

    private InterfaceServidor instanciaLocalServidor;
    private String nombre;
    private static InterfaceCliente cliente;
    private static VentanaCliente ventanaCliente;

    Cliente(String nombreUsuario) {
        nombre = nombreUsuario;
    }

    /**
     * Actualiza la lista de clientes conectados
     * @throws RemoteException 
     */
    @Override
    public void actualizarCliente() throws RemoteException {
        ventanaCliente.actualizarClientes();
    }

    /**
     * Actualiza la lista de mensajes
     * @param mensaje Nuevo mensaje a añadir a la lista
     * @throws RemoteException 
     */
    @Override
    public void actualizarMensajes(String mensaje) throws RemoteException {
        ventanaCliente.actualizarMensajes(mensaje);
    }

    /**
     * Desconecta este cliente del servidor
     */
    public void desconectar() {
        try {
            instanciaLocalServidor.desconectar(cliente, nombre);
        } catch (RemoteException e) {
            System.err.println("CLIENTE exception:" + e.toString());
        }
    }

    public static void main(String args[]) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");

            String nombre_objeto_remoto = "un_nombre_para_obj_remoto";
            InterfaceServidor instanciaLocalServidor = (InterfaceServidor) registry.lookup(nombre_objeto_remoto);

            String nombreCliente = JOptionPane.showInputDialog("Escribe tu nombre:");
            while (nombreCliente.equals("") || !instanciaLocalServidor.nombreLibre(nombreCliente)) {
                nombreCliente = JOptionPane.showInputDialog("Escribe tu nombre:");
            }

            cliente = new Cliente(nombreCliente);

            InterfaceCliente stub
                    = (InterfaceCliente) UnicastRemoteObject.exportObject(cliente, 0);

            registry.rebind(nombreCliente, stub);

            ventanaCliente = new VentanaCliente(nombreCliente, cliente, instanciaLocalServidor);
            ventanaCliente.setVisible(true);

            instanciaLocalServidor.registrar(stub, nombreCliente);

        } catch (Exception e) {
            System.err.println("CLIENTE exception:" + e.toString());
        }

    }

}
