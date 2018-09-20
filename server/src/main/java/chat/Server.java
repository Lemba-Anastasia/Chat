package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Lemba on 05.09.2018.
 */
public class Server implements Runnable {
    private static final Logger log = Logger.getLogger(Server.class);
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ServerSocket servers = null;
    private Socket serverSocket = null;
    private View view;
    private List<MonoThreadClientHandler> threadClientHandlerList;
    private ParticipantsBase participantsBase;
    private List<Thread> threadList;
    private Deque<User> queueOfWaitingUsers;
    private Deque<Agent> queueOfWaitingAgents;
    private ServerSocket server;
    private Socket socket;

    public Server(View view) {
        this.view = view;
        threadClientHandlerList = new ArrayList<>(10);
        participantsBase = new ParticipantsBase();
        threadList = new ArrayList<>();
        queueOfWaitingUsers = new LinkedList<>();
        queueOfWaitingAgents = new LinkedList<>();
        try {
            server = new ServerSocket(1357);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public Server(Deque<User> usersDeque, Deque<Agent> agentDeque) {
        this.queueOfWaitingUsers=usersDeque;
        this.queueOfWaitingAgents=agentDeque;
    }

    public void run() {
        //view.addComandInArea("Client connection waiting");
        log.info("Waiting for client connection");
        while (!server.isClosed()) {
            try {
                socket = server.accept();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            MonoThreadClientHandler monoThreadClientHandler = null;
            try {
                monoThreadClientHandler = new MonoThreadClientHandler(socket,
                        new BufferedReader(new InputStreamReader(socket.getInputStream())),
                        new PrintWriter(socket.getOutputStream(), true),
                        Server.this);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            threadClientHandlerList.add(monoThreadClientHandler);
            //view.addComandInArea("Кто-то подключился");
            log.info("Someone connected");
            Thread thead = new Thread(monoThreadClientHandler);
            thead.start();
            threadList.add(thead);
        }
    }

    public void onMessageReseived2(String message, Client client) throws IOException {
        User freeUser = null;
        Agent freeAgent;
        if (!client.isBusy()) {
            client.sendMessageMyself("Waiting for a companion");
            if (client instanceof User) {
                queueOfWaitingUsers.add((User) client);
                if ((freeAgent = queueOfWaitingAgents.peekFirst()) != null) {
                    client.setCompanion(freeAgent);
                    freeAgent.setCompanion(client);
                    freeUser = (User) client;
                    sendMessageToClient("\n" + "You are connected to " + freeAgent.getName(), client);
                    if (freeUser.getWaitingMessages().equals(""))
                        sendMessage(freeUser.getWaitingMessages() + message, client.getCompanion(), client.getName());
                    else{
                        sendBufMessage(freeUser.getWaitingMessages() + message, client.getCompanion());
                        freeUser.clearBuffer();
                    }
                    queueOfWaitingAgents.removeFirst();
                    Iterator<User> iteratorOnUsers = queueOfWaitingUsers.iterator();
                    while (iteratorOnUsers.hasNext()) {
                        if (iteratorOnUsers.next() == client) iteratorOnUsers.remove();
                    }
                } else {
                    ((User) client).setBufferMessages(message);
                }
            }
        } else sendMessage(message, client.getCompanion(), client.getName());
    }

    private void sendBufMessage(String message, Client client) {
        try {
            Agent a = (Agent) client;
            a.sendByfMessage(message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessageToClient(String message, Client client) {
        try {
            client.sendMessageMyself(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, Client client, String name) throws IOException {
        client.sendMessage(message, name);
    }

    public ParticipantsBase getParticipantsBase() {
        return participantsBase;
    }


    public void leaveChat(Client client) {
        if (client.getCompanion() != null) {
            try {
                client.getCompanion().sendMessageMyself("Companion left the chat");
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            if (client instanceof User) {
                queueOfWaitingAgents.add((Agent) client.getCompanion());
                client.getCompanion().setCompanion(null);
                client.setCompanion(null);
                connectNewFreeAgentToWaitingUser();
            }
            if (client instanceof Agent) {
                queueOfWaitingAgents.add((Agent) client);
                client.getCompanion().setCompanion(null);
                client.setCompanion(null);
                connectNewFreeAgentToWaitingUser();
            }
        }
    }

    public void connectNewFreeAgentToWaitingUser() {
        Iterator<User> iteratorOnUsers = queueOfWaitingUsers.iterator();
        while (iteratorOnUsers.hasNext()) {
            User freeUser = iteratorOnUsers.next();
            if (!freeUser.getWaitingMessages().equals("")) {
                try {
                    onMessageReseived2("", freeUser);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }
    }

    public void exit(Client client, MonoThreadClientHandler mtch) {
        if (client.getCompanion() instanceof Agent) {
            queueOfWaitingAgents.add((Agent) client.getCompanion());
            connectNewFreeAgentToWaitingUser();
        }else {
            client.getCompanion().setCompanion(null);
            connectNewFreeAgentToWaitingUser();
        }
        log.info(client.getName() + " left the chat");
        try {
            client.getCompanion().sendMessageMyself(" Companion disconnected");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try {
            client.getSocket().close();
            participantsBase.remove(client);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public void addToQWaitingAgents(Agent agent) {
        queueOfWaitingAgents.add(agent);
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.server = serverSocket;
    }

    public void sendMessageToNotDefinete(String message, MonoThreadClientHandler monoThreadClientHandler) {
        for (MonoThreadClientHandler mtch : threadClientHandlerList) {
            if (monoThreadClientHandler == mtch) {
                try {
                    mtch.getSocket().getOutputStream().write(("server: " + message + "\n").getBytes());
                    mtch.getSocket().getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
