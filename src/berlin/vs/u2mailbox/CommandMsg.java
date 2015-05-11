package berlin.vs.u2mailbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandMsg extends CommandHandler {
    private ArrayList<ClientMainFrame> clients;

    public CommandMsg(String in, int msgCounter, ArrayList<ClientMainFrame> clients, ClientMainFrame actClient) {
        super(in, msgCounter, actClient);
        this.clients = clients;
    }

    @Override
    public void execute() {
        ArrayList<String> sendMSG = new ArrayList<>();
        sendMSG.add(msg.params.get(0));
        ArrayList<String> responseMSG = new ArrayList<>();
        ClientMainFrame c = null;
        String clientS = msg.params.get(1);

        for (ClientMainFrame client : clients) {
            if (client.username == clientS) {
                c = client;
            }
        }

        if (c != null) {
            this.sendResponse(this.createResponse(sendMSG), c);
            responseMSG.add("Message: " + sendMSG.get(0) + " send successful to: " + c.username);
        } else {
            responseMSG.add("Message: " + sendMSG.get(0) + " can't be send to: " + clientS);
        }

        sendResponse(this.createResponse(responseMSG));
    }
}
