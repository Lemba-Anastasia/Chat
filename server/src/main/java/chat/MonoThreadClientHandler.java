package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Lemba on 05.09.2018.
 */
public class MonoThreadClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String message;
    private View view;
    private Server server;
    private Client client;

    public MonoThreadClientHandler(Socket client, View view, Server server) throws IOException {
        this.socket = client;
        this.view = view;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        System.out.println("Сервер читает канал");
        try {
            while (!socket.isClosed()) {
                if ((message = in.readLine()) != null) {
                    if (message.charAt(0) == '/') handlingCommandsMessage(message);
                    else {
                        handlingMessage(message);
                    }
                }
            }
        } catch (SocketException s) {
            view.addComandInArea("Клиент отсоединился");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlingMessage(String m) {
        try {
            server.onMessageReseived2(m,client);
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.addComandInArea(m + " -----");
    }

    private void handlingCommandsMessage(String m) {
        if (m.matches("/reg(\\s+)user(\\s+)\\w+")) {
            String clientName = m.split("/reg(\\s+)user(\\s+)")[1];
            System.out.println("UserNAME:" + clientName);
            User user = new User(clientName, socket);
            server.getParticipantsBase().addUser(user);
            client=user;
            server.sendServerRequest("Вы зарегистрировались",client);
            server.addToQWaitingUsers(user);
        }else  if (m.matches("/reg(\\s+)agent(\\s+)\\w+")) {
            String clientName = m.split("/reg(\\s+)agent(\\s+)")[1];
            System.out.println("AgentNAME:" + clientName);
            Agent agent = new Agent(clientName, socket);
            server.getParticipantsBase().addAgents(agent);
            client=agent;
            server.sendServerRequest("Вы зарегистрировались",client);
            server.addToQWaitingAgents(agent);
            server.connectNewFreeAgentToWaitingUser(agent);
        }else if(m.matches("/leave(\\s*)")){
            server.leaveChat(client);
            server.sendServerRequest("Вы покинули чат с предыдущим агентом",client);
        }else if(m.matches("/close(\\s*)")){
            server.sendServerRequest("Вы вышли",client);
            server.exit(client,this);
        }else{
            server.sendServerRequest("Не корректная команда",client);
        }
    }

    public void close() {
        this.close();
    }
}
