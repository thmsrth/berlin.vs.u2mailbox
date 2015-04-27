package berlin.vs.u2mailbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatWindow extends javax.swing.JFrame {

    private Client client;

    private JTextArea chatArea;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JTextField msgField;
    private JButton sendButton;

    private JLabel lblPortServer;
    private JLabel lblUser;
    private JTextField portField;
    private JTextField serverField;
    private JTextField userField;
    private JButton loginButton;


    /** Creates new form ChatWindow */
    public ChatWindow() {
        initComponents();
    }

    /** Creates new form ChatWindow */
    public ChatWindow(Client client) {
        this.client = client;
        initComponents();
    }

    private void initComponents() {

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2,3));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,2));

        scrollPane = new JScrollPane();
        chatArea = new JTextArea();

        msgField = new JTextField();
        sendButton = new JButton("send");

        //Add action listener to button
        sendButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
				/* Call startServer method */

            }
        });

        lblPortServer = new JLabel("Port / Server");
        lblUser = new JLabel("User");
        portField = new JTextField();
        serverField = new JTextField();
        userField = new JTextField();
        loginButton = new JButton("anmelden");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("Message Box");

        chatArea.setColumns(20);
        chatArea.setRows(5);
        scrollPane.setViewportView(chatArea);

        topPanel.add(lblPortServer);
        topPanel.add(portField);
        topPanel.add(serverField);
        topPanel.add(lblUser);
        topPanel.add(userField);
        topPanel.add(loginButton);

        //Add action listener to button
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
				/* Call startClient method */
                client.startListener();
            }
        });

        bottomPanel.add(msgField);
        bottomPanel.add(sendButton);

        panel.add(topPanel, BorderLayout.PAGE_START);
        //panel.add(lblInfo, BorderLayout.PAGE_START);
        panel.add(chatArea,BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.PAGE_END);
        add(panel);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ChatWindow().setVisible(true);
            }
        });
    }

    /**
     * @return the chatArea
     */
    public javax.swing.JTextArea getjTextArea1() {
        return chatArea;
    }

    /**
     * @param chatArea the chatArea to set
     */
    public void setjTextArea1(javax.swing.JTextArea jTextArea1) {
        this.chatArea = jTextArea1;
    }
}
