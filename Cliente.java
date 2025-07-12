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

    public static boolean dibujar_barco = false;
    public static boolean agua = false;
    public static boolean tocado = false;

    public static JTextArea area_chat;

    public static void videojuego_main() {
        
                JFrame frame_tablero = new JFrame("Tablero");
                JButton boton_agua = new JButton("Agua");
                JButton boton_tocado = new JButton("Tocado");
                JButton boton_dibujar_barco = new JButton("Dibujar barco");
                JButton boton_continuar_normal = new JButton("Continuar normal sin dibujar barco");
                JButton boton_dejar_normal = new JButton("Dejar normal sin elegir agua ni tocado por el momento");
                frame_tablero.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame_tablero.setResizable(true);
                frame_tablero.setSize(900, 700);
                frame_tablero.getContentPane().setBackground(Color.YELLOW);
                frame_tablero.setLayout(new BorderLayout());
                frame_tablero.add(boton_agua, BorderLayout.SOUTH);
                frame_tablero.add(boton_tocado, BorderLayout.SOUTH);
                frame_tablero.add(boton_dibujar_barco, BorderLayout.SOUTH);
                frame_tablero.add(boton_continuar_normal, BorderLayout.SOUTH);
                frame_tablero.add(boton_dejar_normal, BorderLayout.SOUTH);
                frame_tablero.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                boton_dibujar_barco.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dibujar_barco = true;
                    }
                });
                boton_continuar_normal.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dibujar_barco = false;
                    }
                });

                boton_dejar_normal.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        agua = false;
                        tocado = false;
                    }
                });

                JPanel panel_columnas = new JPanel(new GridLayout(1, 11));
                panel_columnas.setBackground(Color.YELLOW);
                panel_columnas.add(new JLabel(""));
                for (int col = 1; col <= 10; col++) {
                    JLabel label = new JLabel(String.valueOf(col), SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 20));
                    panel_columnas.add(label);
                }

                JPanel panel_centro = new JPanel(new GridLayout(10, 11));
                panel_centro.setBackground(Color.YELLOW);
                char fila = 'A';
                // Matriz para guardar los botones
                JButton[][] botones = new JButton[10][10];
                for (int i = 0; i < 10; i++) {
                    JLabel label_fila = new JLabel(String.valueOf((char)(fila + i)), SwingConstants.CENTER);
                    label_fila.setFont(new Font("Arial", Font.BOLD, 20));
                    panel_centro.add(label_fila);
                    for (int j = 0; j < 10; j++) {
                        final int filaBtn = i;
                        final int colBtn = j;
                        JButton boton = new JButton();
                        boton.setPreferredSize(new Dimension(50, 50));
                        botones[i][j] = boton;
                        boton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if(dibujar_barco == true) {
                                    boton.setBackground(Color.RED);
                                
                                } else {
                                    try {
                                        interfaz.enviar_mensaje(usuario_logeado+" ha elegido: " + (char)('A' + filaBtn) + "," + (colBtn + 1));
                                    } catch (RemoteException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                if(agua == true) {
                                    boton.setText(".");
                                }
                                
                                if(tocado == true) {
                                    boton.setText("X");
                                }
                            }
                        });
                        panel_centro.add(boton);
                        boton_agua.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                agua = true;
                            }
                        });

                        boton_tocado.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                tocado = true;
                            }
                        });
                    }
                }

                frame_tablero.add(panel_columnas, BorderLayout.NORTH);
                frame_tablero.add(panel_centro, BorderLayout.CENTER);
                JLabel label_chat = new JLabel("Introduce tu mensaje:");
                JTextField textfield_chat = new JTextField(20);
                JButton boton_enviar_mensaje = new JButton("Enviar mensaje");
                frame_tablero.requestFocusInWindow();
                frame_tablero.setResizable(true);
                frame_tablero.setVisible(true);
                frame_tablero.setSize(660, 550);
                frame_tablero.getContentPane().setBackground(Color.YELLOW);
                frame_tablero.setLayout(new BoxLayout(frame_tablero.getContentPane(), BoxLayout.Y_AXIS));
                frame_tablero.add(label_chat);
                frame_tablero.add(textfield_chat);
                frame_tablero.add(boton_enviar_mensaje);

                area_chat = new JTextArea(10, 30);
                area_chat.setEditable(false);
                area_chat.setLineWrap(true);
                area_chat.setWrapStyleWord(true);

                JScrollPane scroll_chat = new JScrollPane(area_chat);
                scroll_chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                frame_tablero.add(scroll_chat, BorderLayout.EAST);

                area_chat.setCaretPosition(area_chat.getDocument().getLength());

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
                frame_tablero.setVisible(true);
        
       
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
                                         SwingUtilities.invokeLater(() -> {
                                            area_chat.append(clave + ": " + mensajeNuevo + "\n");
                                            });
                                            System.out.println(clave + ": " + mensajeNuevo);
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
                        }).start();


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
                                JOptionPane.showMessageDialog(null, "Usuario y contraseña registradas correctamente", "Usuario y contraseña registradas correctamente", JOptionPane.INFORMATION_MESSAGE);
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