package berlin.vs.u2mailbox;

import java.util.ArrayList;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandLogin extends CommandHandler {

    public CommandLogin(String in, int msgCounter, ClientMainFrame actClient) {
        super(in, msgCounter, actClient);
    }

    @Override
    public void execute(){
        ArrayList<String> responseArr = new ArrayList<>();

        if(this.msg.command.equals("login") && this.msg.params.get(0) != null){
            this.actClient.username = msg.params.get(0);
            this.actClient.loggedIN = true;
            responseArr.add("You are logged in now. You can start to chat!");
        } else {
            responseArr.add("Please login with command: 'login:username'");
        }

        this.sendResponse(this.createResponse(responseArr));
    }
}
