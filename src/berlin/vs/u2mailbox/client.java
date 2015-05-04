package berlin.vs.u2mailbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class Client {

    private ChatWindow chatWindow;
    private String hostname;
    private String ipAddress;

    public static void main(String[] args) {
        Client c = new Client();
    }

    private String getIPAddress(String hostname){
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(hostname);
            System.out.println("IP address: " + ipAddress.getHostAddress());
        } catch ( UnknownHostException e ) {
            System.out.println("Could not find IP address for: " + hostname);
        }
        return ipAddress.getHostAddress();
    }

    /**
     * @author javaQuery
     * @param host
     * @param message 
     */
    public void sendMessage(String host, String message) {
        try {
            /* Create new socket connection with server host using port 6666 (port can be anything) */
            Socket client = new Socket(host, 6666);
            /* Get server's OutputStream */
            OutputStream outToServer = client.getOutputStream();
            /* Get server's DataOutputStream to write/send message */
            DataOutputStream out = new DataOutputStream(outToServer);
            /* Write message to DataOutputStream */
            out.writeUTF(message);
            /* Get InputStream to get message from server */
            InputStream inFromServer = client.getInputStream();
            /* Get DataInputStream to read message of server */
            DataInputStream in = new DataInputStream(inFromServer);
            /* Print message received from server */
            System.out.println("Server says..." + in.readUTF());
            /* Close connection of client socket */
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startListener(){
        try{
            this.hostname = InetAddress.getLocalHost().getHostName();
            this.ipAddress = this.getIPAddress(hostname);
            System.out.println("Host: " + hostname);
            System.out.println("IP: " + ipAddress);
        } catch(UnknownHostException exc){
            System.err.print("Host konnte nicht gelesen werden");
        }

        /* Create JSON variable */
        JSONObject transmitJSON = new JSONObject();

        /**
         * This is sample program so we are sending three messages from same system with different username.
         * In your case you'll receive message from multiple systems.
         */
        /* Fill up JSON variable with message and username */
        transmitJSON.put("Message", "msg hallo du netter mann Steve.Jobs");
        transmitJSON.put("Username", "Vicky.Thakor");
        /* Send message to server */
        this.sendMessage("localhost", transmitJSON.toString());

        transmitJSON.put("Message", "ls /Users");
        transmitJSON.put("Username", "Steve.Jobs");
        /* Send message to server */
        this.sendMessage("localhost", transmitJSON.toString());

        transmitJSON.put("Message", "who");
        transmitJSON.put("Username", "Steve.Jobs");
        /* Send message to server */
        this.sendMessage("localhost", transmitJSON.toString());
    }

    public Client(){
         /* ChatWindow is not open for user sent message to server */
						/* Create an Object of ChatWindow */
            chatWindow = new ChatWindow(this);
            /**
             * We are setting title of window to identify user for
             * next message we gonna receive You can set hidden
             * value in ChatWindow.java file.
             */
            chatWindow.setTitle("Chat");
						/* Set message to TextArea */
            chatWindow.getjTextArea1().setText("start Chat...");
						/* Make ChatWindow visible */
            chatWindow.setVisible(true);
    }
}
