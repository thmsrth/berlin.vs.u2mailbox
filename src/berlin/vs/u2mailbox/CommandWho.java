package berlin.vs.u2mailbox;

import java.util.ArrayList;


public class CommandWho extends CommandHandler {
    private ArrayList<ClientMainFrame> clients;

    public CommandWho(String in, ArrayList<ClientMainFrame> clients, ClientMainFrame actClient) {
        super(in, actClient);
        this.clients = clients;
    }

    @Override
    public void execute(){
        ArrayList<String> users = new ArrayList<>();

        for (ClientMainFrame client : clients) {
            users.add(client.username);
            System.out.println("Angemeldete User:"+ client.username);
        }
        this.sendResponse(this.createResponse(200, users));
    }
}
