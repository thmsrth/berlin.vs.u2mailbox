package berlin.vs.u2mailbox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lukas on 11/05/15.
 */
public class ClientMainFrame extends Thread {
    public String username;
    public String ipAddress;
    public int port;
    public DataInputStream in;
    public DataOutputStream out;
    private int msgCounter;
    public boolean loggedIN;

    private ArrayList<ClientMainFrame> clients;

    public ClientMainFrame(final String ipAddress, final int port, final DataInputStream in, final DataOutputStream out, ArrayList<ClientMainFrame> clients) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.in = in;
        this.out = out;
        this.loggedIN = false;
        this.msgCounter = 0;
        this.clients = clients;

        System.out.println("session starts...");
        initiateLogin();
    }

    public void run() {
        while (true) {
            try {
                if (!this.in.readUTF().isEmpty()) {
                    if (this.loggedIN) {
                        handleMessages(in.readUTF());
                    } else {
                        //login handle
                        handleLogin(in.readUTF());
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void initiateLogin() {
        JSONObject transmitJSON = new JSONObject();
        JSONArray response = new JSONArray();

        //Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.msgCounter++;
        int responseCounter = 0;

        JSONObject elem = new JSONObject();
        elem.put(Integer.toString(responseCounter), "Please login with command: 'login:username'");
        response.put(elem);

        transmitJSON.put("sequence", this.msgCounter);
        transmitJSON.put("response", response);

        try {
            out.writeUTF(transmitJSON.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    public void handleLogin(final String command) {
        CommandHandler cmd;
        cmd = new CommandLogin(command, this.msgCounter, this);
        cmd.execute();
    }

    public void handleMessages(final String command) {
        CommandHandler cmd;
        Message msg = new Message(command);
        ArrayList<String> users = new ArrayList<>();

        for (ClientMainFrame c : clients) {
            users.add(c.username);
        }

        switch (msg.command) {
            case Commands.TIME:
                cmd = new CommandTime(command, this.msgCounter, this);
                cmd.execute();
                break;
            case Commands.LS:
                cmd = new CommandLs(command, this.msgCounter, this);
                cmd.execute();
                break;
            case Commands.WHO:
                cmd = new CommandWho(command, this.msgCounter, clients, this);
                cmd.execute();
                break;
            case Commands.MSG:
                cmd = new CommandMsg(command, this.msgCounter, clients, this);
                cmd.execute();
                break;
            case Commands.EXIT:

                break;
            default:
                System.out.println("Ungueltiger Command");
        }
        this.msgCounter++;

    }
}
