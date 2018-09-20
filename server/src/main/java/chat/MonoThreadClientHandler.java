package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;
import org.apache.log4j.Logger;

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
    private static final Logger log = Logger.getLogger(MonoThreadClientHandler.class);
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;
    private Client client;

    public MonoThreadClientHandler(Socket socket, BufferedReader in, PrintWriter out, Server server) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.server = server;
    }

    @Override
    public void run() {
        log.info("Server reads the channel");
//        try {
//            while (!socket.isClosed()) {
//                StringBuilder stringBuilder = new StringBuilder();
//                String message;
//                int ch;
//                while ((ch = in.read()) != 0) {
//                    stringBuilder.append((char) ch);
//                }
//                message = stringBuilder.toString();
//                if(!message.equals("") ){
//                    if (message.charAt(0) == '/' ) handlingCommandsMessage(message);
//                    else handlingMessage(message);
//                }
//            }
//        } catch (SocketException s) {
        try {
            String message;
               while (!socket.isClosed()&&(message = in.readLine()) != null) {
                   if (message.charAt(0) == '/') handlingCommandsMessage(message);
                   else {
                       handlingMessage(message);
                   }
               }

        } catch (SocketException s) {
            log.error(s.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handlingMessage(String m) {
        try {
            if (client != null) server.onMessageReseived2(m, client);
            else out.println("You are not registered");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handlingCommandsMessage(String m) {
        if (m.matches("/reg(\\s+)user(\\s+)\\w+")) {
            if (client != null) {
                out.println("You are aleady registered");
                return;
            }
            String clientName = m.split("/reg(\\s+)user(\\s+)")[1];
            log.info("UserNAME:" + clientName);
            User user = new User(clientName, socket);
            server.getParticipantsBase().addUser(user);
            client = user;
            server.sendMessageToClient("You have registered", client);
            //server.addToQWaitingUsers(user);
        } else if (m.matches("/reg(\\s+)agent(\\s+)\\w+")) {
            if (client != null) {
                out.println("You are aleady registered");
                return;
            }
            String clientName = m.split("/reg(\\s+)agent(\\s+)")[1];
            log.info("AgentNAME:" + clientName);
            Agent agent = new Agent(clientName, socket);
            server.getParticipantsBase().addAgents(agent);
            client = agent;
            server.sendMessageToClient("You have registered", client);
            server.addToQWaitingAgents(agent);
            server.connectNewFreeAgentToWaitingUser();
        } else if (m.matches("/leave(\\s*)")) {
            server.sendMessageToClient("You left the chat with the previous companion", client);
            server.leaveChat(client);

        } else if (m.matches("/close(\\s*)")) {
            server.sendMessageToClient("You have left", client);
            server.exit(client, this);
        } else {
            if (client != null)
                server.sendMessageToClient("Uncorrect command", client);
            else
                server.sendMessageToNotDefinete("Uncorrect command", this);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Socket getSocket() {
        return socket;
    }
}
