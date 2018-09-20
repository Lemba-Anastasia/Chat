import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Created by Lemba on 05.09.2018.
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client("localhost",1357);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        new ConnectionHandler(client).startListening();
    }
}