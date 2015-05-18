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
                client.messageSend();
            }
        });

        lblPortServer = new JLabel("Port / Server");
        lblUser = new JLabel("User");
        portField = new JTextField("6879");
        serverField = new JTextField("localhost");
        userField = new JTextField();
        loginButton = new JButton("anmelden");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("Message Box");

        chatArea.setColumns(20);
        chatArea.setRows(5);
        scrollPane.setViewportView(chatArea);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

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
     * @param jTextArea1 the chatArea to set
     */
    public void setjTextArea1(javax.swing.JTextArea jTextArea1) {
        this.chatArea = jTextArea1;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public JTextArea getChatArea() {
        return chatArea;
    }

    public void setChatArea(JTextArea chatArea) {
        this.chatArea = chatArea;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JPanel getBottomPanel() {
        return bottomPanel;
    }

    public void setBottomPanel(JPanel bottomPanel) {
        this.bottomPanel = bottomPanel;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    public JTextField getMsgField() {
        return msgField;
    }

    public void setMsgField(JTextField msgField) {
        this.msgField = msgField;
    }
    
    public void clearMsgField(){
    	this.msgField.setText("");
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public JLabel getLblPortServer() {
        return lblPortServer;
    }

    public void setLblPortServer(JLabel lblPortServer) {
        this.lblPortServer = lblPortServer;
    }

    public JLabel getLblUser() {
        return lblUser;
    }

    public void setLblUser(JLabel lblUser) {
        this.lblUser = lblUser;
    }

    public JTextField getPortField() {
        return portField;
    }

    public void setPortField(JTextField portField) {
        this.portField = portField;
    }

    public JTextField getServerField() {
        return serverField;
    }

    public void setServerField(JTextField serverField) {
        this.serverField = serverField;
    }

    public JTextField getUserField() {
        return userField;
    }

    public void setUserField(JTextField userField) {
        this.userField = userField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }
}
