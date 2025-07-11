package BatallaHuguini;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface Interfaz extends Remote {
    public void enviar_datos_login(String usuario, String contrasena) throws RemoteException;
    public String validar_datos_login(String usuario, String contrasena) throws RemoteException;
    public void enviar_mensaje(String mensaje) throws RemoteException;
    public Map<String, String> recibir_mensaje() throws RemoteException;
}