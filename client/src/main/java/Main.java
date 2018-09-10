import java.io.IOException;

/**
 * Created by Lemba on 05.09.2018.
 */
public class Main {
    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client("localhost",9234);
        } catch (IOException e) {
            System.out.println("Error during connection to server");
        }
        new ConnectionHandler(client).startListening();
    }
}