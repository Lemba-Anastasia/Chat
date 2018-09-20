package chat.Client;

import chat.IdCounter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lemba on 05.09.2018.
 */
public class Agent implements Client{
    private String name;
    private Socket socket;
    private int id;
    private User companion;
    public Agent(String name, Socket socket) {
        this.name=name;
        this.socket=socket;
        id= IdCounter.getInstance().getId();
        companion=null;
    }
    @Override
    public boolean isBusy(){
        return (companion!=null);
    }

    @Override
    public Socket getSocket() {
        return socket;
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
        socket.getOutputStream().write(("server: "+message+"\n").getBytes());
        socket.getOutputStream().flush();

    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public Client getCompanion() {
        return companion;
    }

    @Override
    public void setCompanion(Client companion) {
        this.companion=(User)companion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;

        if (name != agent.getName()) return false;
        return socket == agent.getSocket();
    }
    public void sendByfMessage(String message) throws IOException{
        socket.getOutputStream().write((message+"\n").getBytes());
        socket.getOutputStream().flush();
    }
}
