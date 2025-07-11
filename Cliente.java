package BatallaHuguini;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cliente {
    public static Registry registry;
    public static Interfaz interfaz;

    public static String usuario_logeado;
    public static String contrasena_logeada;

    public static void videojuego_main() {
        JFrame frame_videojuego = new JFrame("BatallaHuguini");
        JButton boton_abrir_chat = new JButton("Abrir chat del servidor");
        JButton boton_ver_tablero = new JButton("Ver tablero");
        frame_videojuego.requestFocusInWindow();
        frame_videojuego.setResizable(false);
        frame_videojuego.setVisible(true);
        frame_videojuego.setSize(660, 550);
        frame_videojuego.getContentPane().setBackground(Color.YELLOW);
        frame_videojuego.setLayout(new BoxLayout(frame_videojuego.getContentPane(), BoxLayout.Y_AXIS));
        frame_videojuego.add(boton_abrir_chat);
        frame_videojuego.add(boton_ver_tablero);
        boton_abrir_chat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame_chat = new JFrame("Chat del servidor");
                JLabel label_chat = new JLabel("Introduce tu mensaje(podrás ver tu mensaje en el cmd/terminal):");
                JTextField textfield_chat = new JTextField(20);
                JButton boton_enviar_mensaje = new JButton("Enviar mensaje");
                frame_chat.requestFocusInWindow();
                frame_chat.setResizable(false);
                frame_chat.setVisible(true);
                frame_chat.setSize(660, 550);
                frame_chat.getContentPane().setBackground(Color.YELLOW);
                frame_chat.setLayout(new BoxLayout(frame_chat.getContentPane(), BoxLayout.Y_AXIS));
                frame_chat.add(label_chat);
                frame_chat.add(textfield_chat);
                frame_chat.add(boton_enviar_mensaje);
                boton_enviar_mensaje.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String mensaje = textfield_chat.getText();
                        try {
                            interfaz.enviar_mensaje(usuario_logeado +": "+ mensaje);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    
    public static void main(String[] args) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry("localhost", 1100);
        interfaz = (Interfaz) registry.lookup("BatallaHuguini");
        JFrame frame = new JFrame("Bienvenido al videojuego BatallaHuguini");
        JButton boton_logear = new JButton("Iniciar sesión");
        JButton boton_registrar = new JButton("Registrar");
        frame.requestFocusInWindow();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(660, 550);
        frame.getContentPane().setBackground(Color.YELLOW);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(boton_logear);
        frame.add(boton_registrar);
        boton_logear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame2 = new JFrame("Iniciar sesión");
                JPanel panel_usuario = new JPanel();
                JPanel panel_contrasena = new JPanel();
                JLabel label_usuario_logear = new JLabel("Introduce el nombre de usuario:");
                JTextField textfield_usuario_logear = new JTextField(20);
                JLabel label_contrasena_logear = new JLabel("Introduce la contraseña:");
                JTextField textfield_contrasena_logear = new JTextField(20);
                JButton boton_enviar = new JButton("Enviar");
                panel_usuario.add(label_usuario_logear);
                panel_usuario.add(textfield_usuario_logear);
                panel_contrasena.add(label_contrasena_logear);
                panel_contrasena.add(textfield_contrasena_logear);
                panel_usuario.setVisible(true);
                panel_usuario.setSize(300, 300);
                panel_usuario.setBackground(Color.YELLOW);
                panel_usuario.setLayout(new FlowLayout());
                panel_contrasena.setVisible(true);
                panel_contrasena.setSize(300, 300);
                panel_contrasena.setBackground(Color.YELLOW);
                panel_contrasena.setLayout(new FlowLayout());
                frame2.requestFocusInWindow();
                frame2.setResizable(false);
                frame2.setVisible(true);
                frame2.setSize(660, 550);
                frame2.getContentPane().setBackground(Color.YELLOW);
                frame2.setLayout(new BoxLayout(frame2.getContentPane(), BoxLayout.Y_AXIS));
                frame2.add(panel_usuario);
                frame2.add(panel_contrasena);
                frame2.add(boton_enviar);
                boton_enviar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        usuario_logeado = textfield_usuario_logear.getText();
                        contrasena_logeada = textfield_contrasena_logear.getText();
                        frame.dispose();
                        frame2.dispose();
                        String validacion = null;
                        try {
                            validacion = interfaz.validar_datos_login(usuario_logeado, contrasena_logeada);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println(validacion);
                        if(validacion.equals("Login correcto")) {
                                videojuego_main();
                            
                            Map<String, String> historial = new HashMap<>();

                        new Thread(() -> {
                            while (true) {
                                try {
                                    Map<String, String> actual = interfaz.recibir_mensaje();
                                    boolean hayNuevo = false;
                                    for (Map.Entry<String, String> entrada : actual.entrySet()) {
                                        String clave = entrada.getKey();
                                        String mensajeNuevo = entrada.getValue();
                                        String mensajeViejo = historial.get(clave);
                                        if (mensajeViejo == null || !mensajeViejo.equals(mensajeNuevo)) {
                                            System.out.println("" + clave + "" + mensajeNuevo);
                                            historial.put(clave, mensajeNuevo);
                                            hayNuevo = true;
                                        }
                                    }
                                    if (!hayNuevo) {
                                        Thread.sleep(1000);
                                    }
                                } catch (RemoteException | InterruptedException e2) {
                                    e2.printStackTrace();
                                    break;
                                }
                            }
                        }).start(); // <-- Muy importante


                        } else {
                            System.exit(1);
                        }
                                    }
                            });
                        }
                });
            boton_registrar.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                    JFrame frame3 = new JFrame("Registrar");
                    JPanel panel_usuario = new JPanel();
                    JPanel panel_contrasena = new JPanel();
                    JLabel label_usuario_registrar = new JLabel("Introduce el nombre de usuario:");
                    JTextField textfield_usuario_registrar = new JTextField(20);
                    JLabel label_contrasena_registrar = new JLabel("Introduce la contraseña:");
                    JTextField textfield_contrasena_registrar = new JTextField(20);
                    JButton boton_enviar = new JButton("Enviar");
                    panel_usuario.add(label_usuario_registrar);
                    panel_usuario.add(textfield_usuario_registrar);
                    panel_contrasena.add(label_contrasena_registrar);
                    panel_contrasena.add(textfield_contrasena_registrar);
                    panel_usuario.setVisible(true);
                    panel_usuario.setSize(300, 300);
                    panel_usuario.setBackground(Color.YELLOW);
                    panel_usuario.setLayout(new FlowLayout());
                    panel_contrasena.setVisible(true);
                    panel_contrasena.setSize(300, 300);
                    panel_contrasena.setBackground(Color.YELLOW);
                    panel_contrasena.setLayout(new FlowLayout());
                    frame3.requestFocusInWindow();
                    frame3.setResizable(false);
                    frame3.setVisible(true);
                    frame3.setSize(660, 550);
                    frame3.getContentPane().setBackground(Color.YELLOW);
                    frame3.setLayout(new BoxLayout(frame3.getContentPane(), BoxLayout.Y_AXIS));
                    frame3.add(panel_usuario);
                    frame3.add(panel_contrasena);
                    frame3.add(boton_enviar);
                    boton_enviar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String usuario_registrado = textfield_usuario_registrar.getText();
                            String contrasena_registrada = textfield_contrasena_registrar.getText();
                            try {
                                interfaz.enviar_datos_login(usuario_registrado, contrasena_registrada);
                                JOptionPane.showMessageDialog(null, "Usuario y contraseña registradas correctamente", "Usuarios y contraseñas registradas correctamente", JOptionPane.INFORMATION_MESSAGE);
                                frame3.dispose();
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
               } 
            });
    }
}