package berlin.vs.u2mailbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandTime extends CommandHandler {
    public CommandTime(String in, int msgCounter, ClientMainFrame actClient) {
        super(in, msgCounter, actClient);
    }

    @Override
    public void execute(){
        ArrayList<String> responseArr = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String time = format.format(Calendar.getInstance().getTime());

        responseArr.add(format.format(Calendar.getInstance().getTime()));

        System.out.println(responseArr.get(0));

        this.sendResponse(this.createResponse(responseArr));
    }
}
