import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Lemba on 19.09.2018.
 */
public class ReaderRunnable implements Runnable {
    private Callback callback;
    private BufferedReader bufferedReader;
    private static final Logger log = Logger.getLogger(ReaderRunnable.class);

    public ReaderRunnable(BufferedReader bufferedReader, Callback callback) {
        this.callback = callback;
        this.bufferedReader = bufferedReader;
    }

    public void run() {
        try {
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                callback.onDataReceived(message);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public interface Callback {
        void onDataReceived(String data);
    }
}
