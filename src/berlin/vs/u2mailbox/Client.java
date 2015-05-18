package berlin.vs.u2mailbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {

	private ChatWindow chatWindow;
	private String hostname;
	private String ipAddress;
	private Socket client;
	private int msgCounter;

	public static void main(String[] args) {
		Client c = new Client();
	}

	private String getIPAddress(String hostname) {
		InetAddress ipAddress = null;
		try {
			ipAddress = InetAddress.getByName(hostname);
			System.out.println("IP address: " + ipAddress.getHostAddress());
		} catch (UnknownHostException e) {
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
			/*
			 * Create new socket connection with server host using port
			 * (port can be anything)
			 */
			Socket client = new Socket(host, port);
			/* Get server's OutputStream */
			OutputStream outToServer = client.getOutputStream();
			/* Get server's DataOutputStream to write/send message */
			DataOutputStream out = new DataOutputStream(outToServer);
			/* Write message to DataOutputStream */
			out.writeBytes(message);
			/* Get InputStream to get message from server */
			InputStream inFromServer = client.getInputStream();
			/* Get DataInputStream to read message of server */
			DataInputStream in = new DataInputStream(inFromServer);
			/* Print message received from server */
			System.out.println("Server says..." + Byte.toString(in.readByte()));
			/* Close connection of client socket */
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startListener() {
		try {
			this.hostname = InetAddress.getLocalHost().getHostName();
			this.ipAddress = this.getIPAddress(hostname);
			this.msgCounter = 0;
			System.out.println("Host: " + hostname);
			System.out.println("IP: " + ipAddress);
		} catch (UnknownHostException exc) {
			System.err.print("Host konnte nicht gelesen werden");
		}

		try {
			/*
			 * Create new socket connection with server host using port 8090
			 * (port can be anything)
			 */
			this.client = new Socket(
					this.chatWindow.getServerField().getText(),
					Integer.parseInt(this.chatWindow.getPortField().getText()));

			ClientListener cl = new ClientListener();

			cl.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void messageSend() {
		try {

			if (client != null) {

				/* Create JSON variable */
				JSONObject transmitJSON = new JSONObject();
				JSONArray para = new JSONArray();
				String cmd = null;
				// Timestamp currentTimestamp = new
				// Timestamp(Calendar.getInstance().getTime().getTime());
				this.msgCounter++;

				String[] commandArr = this.chatWindow.getMsgField().getText()
						.split(":");
				int i = 0;
				int commandCounter = 0;

				for (String s : commandArr) {
					if (i == 0) {
						cmd = s;
					} else {
						JSONObject elem = new JSONObject();
						elem.put(Integer.toString(commandCounter), s);
						para.put(elem);
						commandCounter++;
					}
					i++;
				}

				transmitJSON.put("sequence", this.msgCounter);
				transmitJSON.put("command", cmd);
				transmitJSON.put("params", para);

				/* Get server's OutputStream */
				OutputStream outToServer = client.getOutputStream();
				/* Get server's DataOutputStream to write/send message */
				DataOutputStream out = new DataOutputStream(outToServer);
				/* Write message to DataOutputStream */
				out.writeBytes(transmitJSON.toString());
				
				System.out.println(transmitJSON.toString());

				chatWindow.clearMsgField();
			}else{
				String text = chatWindow.getjTextArea1().getText();
				text = text + " \n" + "Keine Verbindung zu Server!";
				chatWindow.getjTextArea1().setText(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ClientListener extends Thread {

		public Socket serverSocket;

		public ClientListener() throws IOException {
			// serverSocket = new ServerSocket(serverPort);
		}

		public void run() {
			while (true) {
				try {
					String response = "";

					/* DataInputStream to get message sent by client program */
					DataInputStream in = new DataInputStream(
							client.getInputStream());

					JSONObject clientMessage = null;
					try {
						clientMessage = new JSONObject(Byte.toString(in.readByte()));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (clientMessage != null) {

						int sequence = clientMessage.getInt("sequence");
						String statuscode = clientMessage.getString("statuscode");
						ArrayList<String> params = new ArrayList();

						JSONArray arr = clientMessage.getJSONArray("response");
						for (int i = 0; i < arr.length(); i++) {
							JSONObject object = arr.getJSONObject(i);
							String value = object
									.getString(Integer.toString(i));
							params.add(value);
						}

						int t=0;
						/* Print message received from server */
						for (String s : params) {
							if(t>=1){
								response = response + "\n"+s;
							}else{
								response = statuscode + " - " + response +s;
							}
							t++;
						}
						System.out.println("Server says..." + response);
					} else {
						response = "Response null";
					}

					String text = chatWindow.getjTextArea1().getText();
					text = text + " \n" + response;
					chatWindow.getjTextArea1().setText(text);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private String readJson(JSONObject json) {
			int sequence = json.getInt("sequence");
			ArrayList<String> params = new ArrayList();
			String response = Integer.toString(sequence) + ": ";

			JSONArray arr = json.getJSONArray("response");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject object = arr.getJSONObject(i);
				String value = object.getString(Integer.toString(i));
				params.add(value);
				response = response + " " + value;
			}

			return response;
		}

	}

	public Client() {
		/* ChatWindow is not open for user sent message to server */
		/* Create an Object of ChatWindow */
		chatWindow = new ChatWindow(this);
		/**
		 * We are setting title of window to identify user for next message we
		 * gonna receive You can set hidden value in ChatWindow.java file.
		 */
		chatWindow.setTitle("Chat");
		/* Set message to TextArea */
		chatWindow.getjTextArea1().setText("start Chat...");
		/* Make ChatWindow visible */
		chatWindow.setVisible(true);
	}
}
