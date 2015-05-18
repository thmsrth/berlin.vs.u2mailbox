package berlin.vs.u2mailbox;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CommandHandler {
    public Message msg;
    public ClientMainFrame actClient;

    public CommandHandler(final String in, ClientMainFrame actClient) {
        this.msg = new Message(in);
        this.actClient = actClient;
    }

    public void execute(){
        ArrayList<String> responseArr = new ArrayList<>();
        responseArr.add("fehlerhafte Anfrage");
        this.sendResponse(this.createResponse(400, responseArr));
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

    public String createResponse(int code, ArrayList<String> arrayList) {
            /* Create JSON variable */
        JSONObject transmitJSON = new JSONObject();
        JSONArray response = new JSONArray();

        int responseCounter = 0;

        for (String s : arrayList) {
            JSONObject elem = new JSONObject();
            elem.put(Integer.toString(responseCounter), s);
            response.put(elem);
            responseCounter++;
        }

        transmitJSON.put("sequence", msg.sequence);
        transmitJSON.put("statuscode", code);
        transmitJSON.put("response", response);

        return transmitJSON.toString();
    }
}
