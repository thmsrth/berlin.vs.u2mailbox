package berlin.vs.u2mailbox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class Message {
    public int sequence;
    public String command;
    public ArrayList<String> params = new ArrayList();

    public Message(final String msg) {
        /*
         * We are receiving message in JSON format from client.
		 * Parse String to JSONObject
		 */
        JSONObject clientMessage = null;
        try {
            clientMessage = new JSONObject(msg);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.sequence = clientMessage.getInt("sequence");
        this.command = clientMessage.getString("command");

        JSONArray arr = clientMessage.getJSONArray("params");
        if (arr.length() > 0){
            for (int i = 0; i < arr.length(); i++) {
                String value = arr.getString(i);
                this.params.add(value);
            }
        }
    }
}