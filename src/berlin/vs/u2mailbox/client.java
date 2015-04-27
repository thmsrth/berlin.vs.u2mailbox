package berlin.vs.u2mailbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.json.JSONObject;

public class client {

    public static void main(String[] args) {
        client c = new client();
        /* Create JSON variable */
        JSONObject transmitJSON = new JSONObject();

        /**
         * This is sample program so we are sending three messages from same system with different username.
         * In your case you'll receive message from multiple systems.
         */
        /* Fill up JSON variable with message and username */
        transmitJSON.put("Message", "Hello JavaQuery!");
        transmitJSON.put("Username", "Vicky.Thakor");
        /* Send message to server */
        c.sendMessage("localhost", transmitJSON.toString());
        
        transmitJSON.put("Message", "Hello Apple!");
        transmitJSON.put("Username", "Steve.Jobs");
        /* Send message to server */
        c.sendMessage("localhost", transmitJSON.toString());
        
        transmitJSON.put("Message", "How are you?");
        transmitJSON.put("Username", "Steve.Jobs");
        /* Send message to server */
        c.sendMessage("localhost", transmitJSON.toString());
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
}
