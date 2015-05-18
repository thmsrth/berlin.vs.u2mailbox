package berlin.vs.u2mailbox;

import java.util.ArrayList;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandLogin extends CommandHandler {
    ArrayList<ClientMainFrame> clients;

    public CommandLogin(String in, ArrayList<ClientMainFrame> clients, ClientMainFrame actClient) {
        super(in, actClient);
        this.clients = clients;
    }

    @Override
    public void execute(){
        ArrayList<String> responseArr = new ArrayList<>();
        boolean freeUsername = true;

        if(this.msg.command.equals("login") && this.msg.params.get(0) != null){

            for(ClientMainFrame c : clients){
                if (c.username.equals(msg.params.get(0))){
                    freeUsername = false;
                }
            }

            if (freeUsername){
                this.actClient.username = msg.params.get(0);
                this.actClient.loggedIN = true;
                responseArr.add("You are logged in now. You can start to chat!");
                this.sendResponse(this.createResponse(200, responseArr));
            } else{
                responseArr.add("(Service Unavailable) username already in use");
                this.sendResponse(this.createResponse(400, responseArr));
            }
        } else {
            responseArr.add("(Unauthorized) Please login with command: 'login:username'");
            this.sendResponse(this.createResponse(401, responseArr));
        }


    }
}
