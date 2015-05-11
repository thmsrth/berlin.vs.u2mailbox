package berlin.vs.u2mailbox;

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
        private boolean loggedIN;

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
            //Send message to login
        }

        public void run() {
            while (true) {
                try {
                    if (!this.in.readUTF().isEmpty()) {
                        if (!this.loggedIN) {
                            handleMessages(in.readUTF());
                        } else{
                            //login handle
                            handleLogin(in.readUTF());
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        public void handleLogin(final String command) {
            CommandHandler cmd;
            Message msg = new Message(command);

            if(msg.command == "login" && msg.params.get(0) != null){
                this.username = msg.params.get(0);
                this.loggedIN = true;
            }
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
            this.msgCounter ++;

        }
}
