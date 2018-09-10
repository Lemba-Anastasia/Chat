package chat.Client;
import chat.IdCounter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lemba on 05.09.2018.
 */
public class User implements Client {
    private String name;
    private Socket socket;
    private Agent companion;
    private int id;
    private String waitingPutMessages;
    public User(String name, Socket socket) {
        this.name=name;
        this.socket=socket;
        id= IdCounter.getInstance().getId();
        companion=null;
        waitingPutMessages="";
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void sendMessage(String message,String name) throws IOException {
        if(companion!=null){
            socket.getOutputStream().write((name+": "+message+"\n").getBytes());
            socket.getOutputStream().flush();
        }
    }

    @Override
    public void sendMessageMyself(String message) throws IOException {
        socket.getOutputStream().write(("сервер: "+message+"\n").getBytes());
        socket.getOutputStream().flush();

    }

    @Override
    public boolean isBusy() {
        return (companion!=null);
    }

    @Override
    public void setCompanion(Client companion) {
        this.companion= (Agent) companion;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }
    @Override
    public Agent getCompanion(){
        return companion;
    }
    @Override
    public String getName(){
        return name;
    }

    public void setBufferMessages(String m){
        waitingPutMessages+=name+": "+m+"\n";
    }
    public String getWaitingMessages() {
        return waitingPutMessages;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", socket=" + socket +
                ", companion=" + companion.getName() +
                ", id=" + id +
                ", waitingPutMessages='" + waitingPutMessages + '\'' +
                '}';
    }
}
