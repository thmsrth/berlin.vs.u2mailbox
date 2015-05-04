package berlin.vs.u2mailbox;

/*
 * mainFrame.java
 * This program demonstarte client server message application.
 * mainFrame.java will act as a server program aimed to receive messages and respond to client.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainFrame extends JFrame {

    private ArrayList<Client> clients = new ArrayList();

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
        txtPort = new JTextField();
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

    private class Client {
        public String username;
        public String ipAddress;
        public int port;
        public ClientSession session;

        public Client(final String ipAddress, final int port) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.session = new ClientSession(this);
            System.out.println("session starts...");
            this.session.start();
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private class ClientSession extends Thread {

        private Client client;
        private DataOutputStream out;

        public ClientSession(final Client client) {
            this.client = client;
        }

        /* Implement run() for Thread */
        public void run() {
        /* Keep Thread running */
            while (true) {
                /* Create new socket connection with server host using port 6666 (port can be anything) */
                Socket receiver = null;
                try {
                    receiver = new Socket(this.client.ipAddress, this.client.port);
                     /* Get server's OutputStream */
                    OutputStream outToServer = receiver.getOutputStream();
                    /* Get server's DataOutputStream to write/send message */
                    this.out = new DataOutputStream(outToServer);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void handleMessages(final Message msg) {

            switch (msg.command) {
                case "time":
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    String time = format.format(Calendar.getInstance().getTime());
                    System.out.println(format.format(Calendar.getInstance().getTime()));
                    break;
                case "ls":
                    File dir = new File(msg.params.get(0));
                    if (dir.exists()) {
                        File[] fileList = dir.listFiles();
                        for (File f : fileList) {
                            System.out.println(f.getName());
                        }
                    } else {
                        System.out.println("Verzeichnis existiert nicht");
                    }
                    break;
                case "who":
                    System.out.println("Angemeldete User:");
                    for (Client c : clients) {
                        System.out.println(c.username);
                    }
                    break;
                case "msg":
                    String nachricht = "";
                    for (int i = 1; i < msg.params.size() - 1; i++) {
                        nachricht = nachricht + msg.params.get(i) + " ";
                    }
                    String client = msg.params.get(msg.params.size() - 1);
                    System.out.println(client);
                    System.out.println(nachricht);
                    break;
                case "exit":

                    break;
                default:
                    System.out.println("Ungueltiger Command");
            }

            try {
                out.writeBytes("received Data");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class Message {
        public int sequence;
        public String command;
        public ArrayList<String> params;

        public Message(final String msg) {
        /*
         * We are receiving message in JSON format from client.
		 * Parse String to JSONObject
		 */
            JSONObject clientMessage = null;
            try {
                clientMessage = new JSONObject(msg);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.sequence = clientMessage.getInt("sequence");
            this.command = clientMessage.getString("command");

            JSONArray arr = clientMessage.getJSONArray("params");
            for (int i = 0; i < arr.length(); i++) {
                this.params.add(arr.getJSONObject(i).getString("name"));
            }
        }
    }


    /* Inner class to create socket server */
    private class MainServer extends Thread {

        /* Create ServerSocket variable */
        public ServerSocket serverSocket;

        /* Constructor to initialize serverSocket */
        public MainServer(int serverPort) throws IOException {
            serverSocket = new ServerSocket(serverPort);
        }

        private Client openClientConnection(final String ipAddress, final int port) {
            Client client = null;

            System.out.println("connection try to open...");

            for (Client c : clients) {
                if (c.ipAddress == ipAddress && c.port == port) {
                    return client;
                }
            }

            if (clients.size() >= 5) {
                // Return Fehlermeldung Max. User schon erreicht
                System.out.println("Max. Anzahl User bereits erreicht");
                return null;
            }
            return new Client(ipAddress, port);
        }

        /* Implement run() for Thread */
        public void run() {
			/* Keep Thread running */
            while (true) {
                try {
					/* Accept connection on server */
                    Socket server = serverSocket.accept();

					/* DataInputStream to get message sent by client program */
                    DataInputStream in = new DataInputStream(
                            server.getInputStream());

                    Message msg = new Message(in.readUTF());

                    Client client = openClientConnection(server.getInetAddress().getHostName(), server.getPort());

                    if (client == null) {
                             /* Get DataOutputStream of client to respond */
                        DataOutputStream out = new DataOutputStream(
                                server.getOutputStream());

                            /* Send response message to client */
                        out.writeUTF("Could not connect to server. Server is full!");
                    } else {
                        client.session.handleMessages(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}