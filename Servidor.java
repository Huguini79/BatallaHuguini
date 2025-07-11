package BatallaHuguini;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.HashMap;
import java.util.Map;

public class Servidor {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Map<String, String> usuarios = new HashMap<>();
        Map<String, String> mensajes = new HashMap<>();
        Remote remote = UnicastRemoteObject.exportObject(new Interfaz() {
            @Override
            public void enviar_datos_login(String usuario, String contrasena) throws RemoteException {
                usuarios.put(usuario, contrasena);
            }

            @Override
            public String validar_datos_login(String usuario, String contrasena) throws RemoteException {
                if(!usuarios.containsKey(usuario)) {
                    return "Usuario no encontrado";
                }
                if(!usuarios.get(usuario).equals(contrasena)) {
                    return "Contraseña incorrecta";
                }

                return "Login correcto";

            }

            private int contadorMensajes = 0;

            @Override
            public void enviar_mensaje(String mensaje) throws RemoteException {
                mensajes.put("Mensaje #" + (++contadorMensajes)+ " ", mensaje);
            }



            @Override
            public Map<String, String> recibir_mensaje() throws RemoteException {
                return mensajes;
            }
        }, 0);
        Registry registry = LocateRegistry.createRegistry(1100);
        System.out.println("Servidor iniciado correctamente en el puerto 1100");
        registry.bind("BatallaHuguini", remote);
    }
}