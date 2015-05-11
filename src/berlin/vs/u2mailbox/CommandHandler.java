package berlin.vs.u2mailbox;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CommandHandler {
    public Message msg;
    public int msgCounter;
    public ClientMainFrame actClient;

    public CommandHandler(final String in, final int msgCounter, ClientMainFrame actClient) {
        this.msg = new Message(in);
        this.msgCounter = msgCounter;
        this.actClient = actClient;
    }

    public void execute(){
        this.sendResponse("Test");
    }

    public void sendResponse(String response, ClientMainFrame client){
        try{
            client.out.writeUTF(response);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void sendResponse(String response){
        try{
            this.actClient.out.writeUTF(response);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public String createResponse(ArrayList<String> arrayList) {
            /* Create JSON variable */
        JSONObject transmitJSON = new JSONObject();
        JSONArray response = new JSONArray();

        //Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.msgCounter++;
        int responseCounter = 0;

        for (String s : arrayList) {
            JSONObject elem = new JSONObject();
            elem.put(Integer.toString(responseCounter), s);
            response.put(elem);
            responseCounter++;
        }

        transmitJSON.put("sequence", this.msgCounter);
        transmitJSON.put("response", response);

        return transmitJSON.toString();
    }
}
