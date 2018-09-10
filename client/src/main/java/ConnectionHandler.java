import java.util.Scanner;

/**
 * Created by Lemba on 05.09.2018.
 */
public class ConnectionHandler {
    private Client client;

    public ConnectionHandler(Client client) {
        this.client = client;
    }

    void startListening() {
        ClavaReader clavaReader = new ClavaReader();
        Thread thread = new Thread(clavaReader);
        thread.start();
        FromServerReader fromServerReader = new FromServerReader();
        Thread thread2 = new Thread(fromServerReader);
        thread2.start();
//        client.close();
//        clavaReader.close();
//        fromServerReader.close();
    }

    class ClavaReader implements Runnable {
        Scanner scanner = new Scanner(System.in);

        void close() {
            scanner.close();
        }

        public void run() {
            while (scanner.hasNext()) {
                client.sendMessage(scanner.nextLine());
            }
        }

    }

    class FromServerReader implements Runnable {
        Scanner scanner = new Scanner(client.getIn());

        void close() {
            scanner.close();
        }

        public void run() {
            while (scanner.hasNext()) {
                System.out.println("->" + scanner.nextLine());
            }
        }
    }
}