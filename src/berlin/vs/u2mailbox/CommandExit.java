package berlin.vs.u2mailbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandExit extends CommandHandler {
    ArrayList<ClientMainFrame> clients;

    public CommandExit(String in,  ArrayList<ClientMainFrame> clients, ClientMainFrame actClient) {
        super(in, actClient);
        this.clients = clients;
    }

    @Override
    public void execute(){
        ArrayList<String> responseArr = new ArrayList<>();

        responseArr.add("Du bist abgemeldet. Viel Spass!");
        this.sendResponse(this.createResponse(200, responseArr));

        this.clients.remove(this);
        this.actClient.stop();
    }
}
