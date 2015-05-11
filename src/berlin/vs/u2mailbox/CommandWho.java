package berlin.vs.u2mailbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandWho extends CommandHandler {
    private ArrayList<ClientMainFrame> clients;

    public CommandWho(String in, int msgCounter, ArrayList<ClientMainFrame> clients, ClientMainFrame actClient) {
        super(in, msgCounter, actClient);
        this.clients = clients;
    }

    @Override
    public void execute(){
        ArrayList<String> users = new ArrayList<>();

        for (ClientMainFrame client : clients) {
            users.add(client.username);
            System.out.println("Angemeldete User:"+ client.username);
        }
        this.sendResponse(this.createResponse(users));
    }
}
