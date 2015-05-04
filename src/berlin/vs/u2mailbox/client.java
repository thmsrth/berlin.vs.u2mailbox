package berlin.vs.u2mailbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;



public class Client {

    private ChatWindow chatWindow;
    private String hostname;
    private String ipAddress;
    private Socket client;

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
    public void sendMessage(String host, int port, String message) {
        try {
            /* Create new socket connection with server host using port 6666 (port can be anything) */
            Socket client = new Socket(host, port);
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

        
        try {
            /* Create new socket connection with server host using port 6666 (port can be anything) */
            this.client = new Socket(this.chatWindow.getServerField().getText(), Integer.parseInt(this.chatWindow.getPortField().getText()));
            
            ClientListener cl = new ClientListener(Integer.parseInt(this.chatWindow.getPortField().getText()));
            
            cl.start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void messageSend(){
    	try {
	    	/* Create JSON variable */
	        JSONObject transmitJSON = new JSONObject();
	        ArrayList<String> para = new ArrayList();
	        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	        
	        transmitJSON.put("sequence", currentTimestamp);
	        transmitJSON.put("command", this.chatWindow.getMsgField().getText());
	        transmitJSON.put("params", para);
	        
	    	/* Get server's OutputStream */
	        OutputStream outToServer = client.getOutputStream();
	        /* Get server's DataOutputStream to write/send message */
	        DataOutputStream out = new DataOutputStream(outToServer);
	        /* Write message to DataOutputStream */
	        out.writeUTF(transmitJSON.toString());
    	} catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ClientListener extends Thread {
    	
    	public ServerSocket serverSocket;
    	
    	public ClientListener(int serverPort) throws IOException {
            serverSocket = new ServerSocket(serverPort);
    	}
    	
    	public void run(){
    		while(true){
    			try {
					/* Accept connection on server */
                    Socket server = serverSocket.accept();

					/* DataInputStream to get message sent by client program */
                    DataInputStream in = new DataInputStream(
                            server.getInputStream());

                    JSONObject serverMessage = new JSONObject(in.readUTF());
                    
                    if(serverMessage.get("") != null){
                    	
                    }
                    
                    /* Print message received from server */
                    System.out.println("Server says..." + in.readUTF());
                    

                } catch (Exception e) {
                    e.printStackTrace();
                }
    		}
    	}
    	
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
