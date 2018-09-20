package chat.Client;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Lemba on 05.09.2018.
 */
public interface Client {
    int getId();
    void sendMessage(String message,String name) throws IOException;
    boolean isBusy();
    Socket getSocket();
    String getName();
    Client getCompanion();
    void setCompanion(Client companion);
    void sendMessageMyself(String message) throws IOException;

}
