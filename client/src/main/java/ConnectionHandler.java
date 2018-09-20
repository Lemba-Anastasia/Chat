import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Lemba on 05.09.2018.
 */
public class ConnectionHandler {
    private Client client;
    private static final Logger log = Logger.getLogger(ConnectionHandler.class);

    public ConnectionHandler(Client client) {
        this.client = client;
    }

    void startListening() {
        final BufferedReader bufferedReaderforReadClava = new BufferedReader(new InputStreamReader(System.in));
        final BufferedReader bufferedReaderFromServer=client.getIn();

        ReaderRunnable clavaReader = new ReaderRunnable(bufferedReaderforReadClava, new ReaderRunnable.Callback() {
            public void onDataReceived(String data) {
                if(data.equals("/close")){
                    try {
                        client.sendMessage(data);
                        bufferedReaderforReadClava.close();
                        bufferedReaderFromServer.close();
                        System.exit(0);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
                else client.sendMessage(data);
            }
        });
        Thread thread1=new Thread(clavaReader);
        thread1.start();

        ReaderRunnable readerFromServer=new  ReaderRunnable(bufferedReaderFromServer, new ReaderRunnable.Callback() {
                    public void onDataReceived(String data) {
                        System.out.println("->"+data);
                    }
                });
        Thread thread2 = new Thread(readerFromServer);
        thread2.start();
    }
}