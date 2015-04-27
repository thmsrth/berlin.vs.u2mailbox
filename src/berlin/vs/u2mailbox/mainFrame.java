package berlin.vs.u2mailbox;

/*
 * mainFrame.java
 * This program demonstarte client server message application.
 * mainFrame.java will act as a server program aimed to receive messages and respond to client.
 */

import java.awt.Frame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class mainFrame extends javax.swing.JFrame {

	/* Inner class to create socket server */
	private class mainServer extends Thread {

		/* Create ServerSocket variable */
		private ServerSocket serverSocket;
		private ArrayList<String> users = new ArrayList();
		private String userName;

		/* Constructor to initialize serverSocket */
		public mainServer() throws IOException {
			serverSocket = new ServerSocket(6666);
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
					/*
					 * We are receiving message in JSON format from client.
					 * Parse String to JSONObject
					 */
					JSONObject clientMessage = new JSONObject(in.readUTF());
					
					// Pruefung bei Anmeldung
					if (clientMessage.get("Username") != null) {
						if (users.size() <= 5) {
							userName = clientMessage.get("Username").toString();
							if (users.contains(userName)) {
								// Return Fehlermeldung Username bereits belegt
								System.out.println(userName +" bereits belegt.");
							} else {
								users.add(userName);
								System.out.println("Angemeldete User");
								for(String ausgabe : users)
								{
									System.out.println(ausgabe);
								}
							}
						} else {
							// Return Fehlermeldung Max. User schon erreicht
							System.out.println("Max. Anzahl User bereits erreicht");
						}
					}else{
						System.out.println("JSON Objekt ohne Username erhalten!");
					}
					
					String message = clientMessage.getString("Message").toString();
					String[] words = message.split(" ");
					String key = words[0];
					
					switch(key){
						case "time":
							SimpleDateFormat format = new SimpleDateFormat("HH:mm");
							String zeit = format.format(Calendar.getInstance().getTime());
							System.out.println(format.format(Calendar.getInstance().getTime()));
							break;
						case "ls":
							File dir = new File(words[1]);
							if(dir.exists()){
								File[] fileList = dir.listFiles();
								for(File f : fileList) {
								    System.out.println(f.getName());
								}
							}else{
								System.out.println("Verzeichnis existiert nicht");
								}
							break;
						case "who":
							System.out.println("Angemeldete User:");
							for(String ausgabe : users)
							{
								System.out.println(ausgabe);
							}
							break;
						case "msg":
							String nachricht="";
							for(int i=1; i<words.length-1; i++) {
								nachricht = nachricht + words[i]+ " ";
							}
							String client = words[words.length-1];
							System.out.println(client);
							System.out.println(nachricht);
							break;
						case "exit":
							
							break;
						default:
							System.out.println("Ungueltiger Kommand");
					}

					/*
					 * Flag to check chat window is opened for user that sent
					 * message
					 */
					boolean flagChatWindowOpened = false;
					/* Reading Message and Username from JSONObject */
					userName = clientMessage.get("Username").toString();
					//String message = clientMessage.getString("Message").toString();

					/* Get list of Frame/Windows opened by mainFrame.java */
					for (Frame frame : Frame.getFrames()) {
						/* Check Frame/Window is opened for user */
						if (frame.getTitle().equals(userName)) {
							/* Frame/ Window is already opened */
							flagChatWindowOpened = true;
							/* Get instance of ChatWindow */
							ChatWindow chatWindow = (ChatWindow) frame;
							/* Get previous messages from TextArea */
							String previousMessage = chatWindow.getjTextArea1()
									.getText();
							/* Set message to TextArea with new message */
							chatWindow.getjTextArea1().setText(
									previousMessage + "\n" + message);
						}
					}

					/* ChatWindow is not open for user sent message to server */
					if (!flagChatWindowOpened) {
						/* Create an Object of ChatWindow */
						ChatWindow chatWindow = new ChatWindow();
						/**
						 * We are setting title of window to identify user for
						 * next message we gonna receive You can set hidden
						 * value in ChatWindow.java file.
						 */
						chatWindow.setTitle(userName);
						/* Set message to TextArea */
						chatWindow.getjTextArea1().setText(message);
						/* Make ChatWindow visible */
						chatWindow.setVisible(true);
					}

					/* Get DataOutputStream of client to repond */
					DataOutputStream out = new DataOutputStream(
							server.getOutputStream());
					/* Send response message to client */
					out.writeUTF("Received from "
							+ clientMessage.get("Username").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * To start SocketServer when mainFrame.java loads
	 */
	public void startServer() {
		try {
			/* Create thread of Inner class mainServer */
			Thread t = new mainServer();
			/* Start Thread */
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Creates new form mainFrame */
	public mainFrame() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	//
	private void initComponents() {

		lblInformation = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Server");
		setAlwaysOnTop(true);

		lblInformation.setText("Client - Server Sample Application");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap()
						.addComponent(lblInformation)
						.addContainerGap(229, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap()
						.addComponent(lblInformation)
						.addContainerGap(275, Short.MAX_VALUE)));

		pack();
	}//

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				/* To set new look and feel */
				JFrame.setDefaultLookAndFeelDecorated(true);
				try {
					/**
					 * Change look and feel of JFrame to Nimbus For other look
					 * and feel check
					 * 
					 */
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				/* Create an Object of mainFrame */
				mainFrame mFrame = new mainFrame();
				/* make mainFrame visible */
				mFrame.setVisible(true);
				/* Call startServer method */
				mFrame.startServer();
			}
		});
	}

	// Variables declaration - do not modify
	private javax.swing.JLabel lblInformation;
	// End of variables declaration
}