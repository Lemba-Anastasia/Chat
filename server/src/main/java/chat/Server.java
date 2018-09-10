package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Lemba on 05.09.2018.
 */
public class Server implements Runnable {
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ServerSocket servers = null;
    private Socket serverSocket = null;
    private View view;
    private List<MonoThreadClientHandler> listRunnableServer;
    private ParticipantsBase participantsBase;
    private List<Thread> threadList;
    private Deque<User> queueOfWaitingUsers;
    private Deque<Agent> queueOfWaitingAgents;

    public Server(View view) {
        this.view = view;
        listRunnableServer = new ArrayList<>(10);
        participantsBase = new ParticipantsBase();
        threadList = new ArrayList<>();
        queueOfWaitingUsers=new LinkedList<>();
        queueOfWaitingAgents=new LinkedList<>();
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(9234)) {
            view.addComandInArea("Ожидание подключения клиента");
            while (!server.isClosed()) {
                Socket client = server.accept();
                MonoThreadClientHandler monoThreadClientHandler = new MonoThreadClientHandler(client, view, Server.this);
                listRunnableServer.add(monoThreadClientHandler);
                view.addComandInArea("Кто-то подключился");
                Thread thead=new Thread(monoThreadClientHandler);
                thead.start();
                threadList.add(thead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void onMessageReseived2(String message, String status, Client client) throws IOException {
        User freeUser = null;
        Agent freeAgent;
        if (!client.isBusy()) {
            client.sendMessage("Ожидание собеседника");
            view.addComandInArea(status + ": ожидает подключения");
            if (client instanceof User) {
                if ((freeAgent = participantsBase.getFreeAgent()) != null) {
                    client.setCompanion(freeAgent);
                    freeAgent.setCompanion(client);
                    freeUser=(User) client;
                    sendMessage(freeUser.getWaitingMessages()+message, client.getCompanion());
                }
                else {
                    freeUser.setBufferMessages(message);
                    queueOfWaitingUsers.add(freeUser);
                }
            }
        } else sendMessage(message, client.getCompanion());
    }*/

    public void onMessageReseived2(String message, Client client) throws IOException {
        User freeUser = null;
        Agent freeAgent;
        if (!client.isBusy()) {
            client.sendMessageMyself("Ожидание собеседника");
            view.addComandInArea(client.getName() + ": ожидает подключения");
            if (client instanceof User) {
                if ((freeAgent = queueOfWaitingAgents.peekFirst()) != null) {
                    client.setCompanion(freeAgent);
                    freeAgent.setCompanion(client);
                    freeUser=(User) client;
                    System.out.println(client+"C <-");
                    System.out.println(freeAgent+"A <-");
                    System.out.println(queueOfWaitingUsers+"queue of U <-");
                    sendMessage(freeUser.getWaitingMessages()+message, client.getCompanion(),client.getName());///
                    queueOfWaitingAgents.removeFirst();
                    //queueOfWaitingUsers.removeFirst();
                    System.out.println(queueOfWaitingAgents+"queue of A2 <-");
                    System.out.println(queueOfWaitingUsers+"queue of U2 <-");
                    Iterator<User> iteratorOnUsers =queueOfWaitingUsers.iterator();
                    while (iteratorOnUsers.hasNext()){
                        if(iteratorOnUsers.next()==client) iteratorOnUsers.remove();
                    }
                }
                else {
                    ((User)client).setBufferMessages(message);
                }
            }
        } else sendMessage(message, client.getCompanion(),client.getName());
    }

    public void sendServerRequest(String message,Client client)  {
        try {
            client.sendMessageMyself(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, Client client,String name) throws IOException {
        client.sendMessage(message,name);
    }

    public void closeServer() throws IOException {
        JOptionPane.showMessageDialog(new JFrame(), "Сервер закрыт");
        if (serverSocket != null) {
            out.close();
            in.close();
            serverSocket.close();
        }
        servers.close();
    }

    public ParticipantsBase getParticipantsBase() {
        return participantsBase;
    }


    public  void leaveChat(Client client){
        if (client.getCompanion() != null) {
            try {
                client.getCompanion().sendMessage("Собеседник вышел из чата");
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.setCompanion(null);
            client.getCompanion().setCompanion(null);
            if(client instanceof User){
                queueOfWaitingUsers.add((User) client);
                connectNewFreeAgentToWaitingUser((Agent)client.getCompanion());
            }
            if(client instanceof Agent){
                queueOfWaitingAgents.add((Agent) client);
                connectNewFreeAgentToWaitingUser((Agent) client);
            }
        }
    }
    public void connectNewFreeAgentToWaitingUser(Agent agent){
        Iterator<User> iteratorOnUsers=queueOfWaitingUsers.iterator();
        while (iteratorOnUsers.hasNext()){
            User freeUser=iteratorOnUsers.next();
            if(freeUser.getWaitingMessages()!="") {
                try {
                    onMessageReseived2("",freeUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exit(Client client,MonoThreadClientHandler mtch){
        if(client.getCompanion() instanceof Agent){
            connectNewFreeAgentToWaitingUser((Agent) client.getCompanion());
        }
        view.addComandInArea(client.getName()+" вышел из чата");
        try {
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mtch.close();
    }

    public void addToQWaitingUsers(User user) {
        queueOfWaitingUsers.add(user);
    }
    public void addToQWaitingAgents(Agent agent) {
        queueOfWaitingAgents.add(agent);
    }
}
