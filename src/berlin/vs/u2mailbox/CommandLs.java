package berlin.vs.u2mailbox;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lukas on 11/05/15.
 */
public class CommandLs extends CommandHandler {

    public CommandLs(String in, int msgCounter, ClientMainFrame actClient) {
        super(in, msgCounter, actClient);
    }

    @Override
    public void execute(){
        ArrayList<String> dirs = new ArrayList<>();

        File dir = new File(this.msg.params.get(0));
        if (dir.exists()) {
            File[] fileList = dir.listFiles();

            for (File f : fileList) {
                dirs.add(f.getName());
            }
        } else {
            dirs.add("Verzeichnis existiert nicht");
        }

        this.sendResponse(this.createResponse(dirs));
    }
}
