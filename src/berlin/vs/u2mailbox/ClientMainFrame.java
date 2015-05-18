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
    public boolean loggedIN;

    private ArrayList<ClientMainFrame> clients;

    public ClientMainFrame(final String ipAddress, final int port, final DataInputStream in, final DataOutputStream out, ArrayList<ClientMainFrame> clients) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.in = in;
        this.out = out;
        this.loggedIN = false;
        this.clients = clients;

        System.out.println("session starts...");
        initiateLogin();
    }

    public void run() {
        while (true) {
            try {
                String input = Byte.toString(in.readByte());
                if (!input.isEmpty()) {
                    if (this.loggedIN) {
                        handleMessages(input);
                    } else {
                        //login handle
                        handleLogin(input);
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void initiateLogin() {
        JSONObject transmitJSON = new JSONObject();
        JSONArray response = new JSONArray();

        //Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        int responseCounter = 0;

        JSONObject elem = new JSONObject();
        elem.put(Integer.toString(responseCounter), "Please login with command: 'login:username'");
        response.put(elem);

        transmitJSON.put("sequence", 0);
        transmitJSON.put("statuscode", 200);
        transmitJSON.put("response", response);

        try {
            out.writeBytes(transmitJSON.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    public void handleLogin(final String command) {
        CommandHandler cmd;
        cmd = new CommandLogin(command, clients, this);
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
                cmd = new CommandTime(command, this);
                cmd.execute();
                break;
            case Commands.LS:
                cmd = new CommandLs(command, this);
                cmd.execute();
                break;
            case Commands.WHO:
                cmd = new CommandWho(command, clients, this);
                cmd.execute();
                break;
            case Commands.MSG:
                cmd = new CommandMsg(command, clients, this);
                cmd.execute();
                break;
            case Commands.EXIT:
                cmd = new CommandExit(command, clients, this);
                cmd.execute();
                break;
            default:
                cmd = new CommandHandler(command, this);
                cmd.execute();
                break;
        }
    }
}
