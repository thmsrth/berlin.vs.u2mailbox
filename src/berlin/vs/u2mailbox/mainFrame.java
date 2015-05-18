package berlin.vs.u2mailbox;

/*
 * mainFrame.java
 * This program demonstarte client server message application.
 * mainFrame.java will act as a server program aimed to receive messages and respond to client.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;


public class MainFrame extends JFrame {

    private ArrayList<ClientMainFrame> clients = new ArrayList();

    public int serverPort;

    // Variables declaration
    private JPanel panelMain;
    private JPanel panelPort;
    private JPanel panelMessages;
    private JTextArea txtInformation;
    private JLabel lblPort;
    private JTextField txtPort;
    private JButton button;

    /**
     * To start SocketServer when mainFrame.java loads
     */
    public void startServer() {
        try {
            this.txtInformation.setText("run... on port: " + this.serverPort);
            /* Create thread of Inner class mainServer */
            MainServer m = new MainServer(this.serverPort);
            /* Start Thread */
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents(final MainFrame mainFrame) {
        panelMain = new JPanel();
        panelMessages = new JPanel();
        panelPort = new JPanel();

        panelMain.setLayout(new BorderLayout());
        panelMessages.setLayout(new FlowLayout());
        panelPort.setLayout(new GridLayout(0, 3));
        panelMain.add(panelPort, BorderLayout.PAGE_START);
        panelMain.add(panelMessages, BorderLayout.CENTER);

        txtInformation = new JTextArea();
        txtInformation.setColumns(25);
        txtInformation.setRows(15);

        lblPort = new JLabel();
        txtPort = new JTextField("6879");
        button = new JButton("start");

        lblPort.setText("Server Port");

        //Add action listener to button
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                /* Call startServer method */
                mainFrame.serverPort = Integer.valueOf(txtPort.getText());
                mainFrame.startServer();
            }
        });

        panelMessages.add(txtInformation);

        panelPort.add(lblPort);
        panelPort.add(txtPort);
        panelPort.add(button);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("Server");

        add(panelMain);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                /* To set new look and feel */
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

				/* Create an Object of mainFrame */
                MainFrame mFrame = new MainFrame();
                mFrame.initComponents(mFrame);

				/* make mainFrame visible */
                mFrame.setVisible(true);
            }
        });
    }


    /* Inner class to create socket server */
    private class MainServer extends Thread {

        /* Create ServerSocket variable */
        public ServerSocket serverSocket;

        /* Constructor to initialize serverSocket */
        public MainServer(int serverPort) throws IOException {
            serverSocket = new ServerSocket(serverPort);
        }

        private ClientMainFrame openClientConnection(final String ipAddress, final int port, final DataInputStream in, final DataOutputStream out) {
            ClientMainFrame client = null;

            System.out.println("connection try to open...");

            if (clients.size() >= 5) {
                // Return Fehlermeldung Max. User schon erreicht
                System.out.println("Max. Anzahl User bereits erreicht");

                JSONObject transmitJSON = new JSONObject();
                JSONArray response = new JSONArray();

                JSONObject elem = new JSONObject();
                elem.put(Integer.toString(0), "too many requests, server is busy");
                response.put(elem);

                transmitJSON.put("sequence", 0);
                transmitJSON.put("statuscode", 429);
                transmitJSON.put("response", response);

                try {
                    out.writeUTF(transmitJSON.toString());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                return null;
            }

            client =  new ClientMainFrame(ipAddress, port, in, out, clients);
            clients.add(client);

            return client;
        }

        /* Implement run() for Thread */
        public void run() {
            /* Keep Thread running */
            while (true) {
                try {
                    InetAddress ipAddress = null;
                    try {
                        ipAddress = InetAddress.getLocalHost();
                        System.out.println("IP address: " + ipAddress.getHostAddress());
                    } catch (UnknownHostException e) {
                        System.out.println("Could not find local IP address");
                    }
					/* Accept connection on server */
                    Socket server = serverSocket.accept();

					/* DataInputStream to get message sent by client program */
                    DataInputStream in = new DataInputStream(
                            server.getInputStream());

                    /* Get DataOutputStream of client to respond */
                    DataOutputStream out = new DataOutputStream(
                            server.getOutputStream());

                    ClientMainFrame client = openClientConnection(server.getInetAddress().getHostName(), server.getPort(), in, out);

                    if (client != null) {
                        client.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}