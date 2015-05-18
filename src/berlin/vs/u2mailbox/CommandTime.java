package berlin.vs.u2mailbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommandTime extends CommandHandler {
    public CommandTime(String in, ClientMainFrame actClient) {
        super(in, actClient);
    }

    @Override
    public void execute(){
        ArrayList<String> responseArr = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        responseArr.add(format.format(Calendar.getInstance().getTime()));

        this.sendResponse(this.createResponse(200, responseArr));
    }
}
