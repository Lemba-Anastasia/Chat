package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Lemba on 16.09.2018.
 */
public class ServerTest extends TestCase {
    public void testRun() throws Exception {
        Server server=new Server(new View());
        ServerSocket mockServerSocket=mock(ServerSocket.class);
        server.setServerSocket(mockServerSocket);
        when(mockServerSocket.isClosed()).thenReturn(false).thenReturn(true);
        Socket socket=mock(Socket.class);
        when(mockServerSocket.accept()).thenReturn(socket);
        when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(socket.getInputStream()).thenReturn(mock(InputStream.class));
        server.run();
    }

    public void testOnMessageReseived2() throws Exception {
        Deque<User> usersDeque=new LinkedList<>();
        Deque<Agent> agentDeque=new LinkedList<>();
        Server server=new Server(usersDeque,agentDeque);
        Socket socket=mock(Socket.class);
        User user=new User("Bob",socket);
        when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
        usersDeque.add(user);
        String message="Это сообщение должно попасть в буфер";
        server.onMessageReseived2(message, user);
        assertEquals(user.getName()+": "+message+"\n",user.getWaitingMessages());
    }

    public void testMessageToClient() throws Exception {
        Deque<User> usersDeque=new LinkedList<>();
        Deque<Agent> agentDeque=new LinkedList<>();
        Server server=new Server(usersDeque,agentDeque);
        Socket socket=mock(Socket.class);
        User user=new User("Bob",socket);
        String message ="for u";
        final String[] checkMessage = {""};
        when(socket.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                checkMessage[0] +=(char)b;
            }
        });
        server.sendMessageToClient(message,user);
        assertEquals("server: "+message+"\n",checkMessage[0]);
    }
    public void testOnMessageReseived22() throws Exception {
        Deque<User> usersDeque=new LinkedList<>();
        Deque<Agent> agentDeque=new LinkedList<>();
        Server server=new Server(usersDeque,agentDeque);
        Socket socket=mock(Socket.class);
        User user=new User("Bob",socket);
        Agent agent=new Agent("Polo",socket);
        final String[] checkMessage = {""};
        when(socket.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                checkMessage[0] +=(char)b;
            }
        });
        usersDeque.add(user);
        agentDeque.add(agent);
        user.setCompanion(agent);
        agent.setCompanion(user);
        String message="hello";
        server.onMessageReseived2(message, user);
        assertEquals(user.getName()+": "+message+"\n",checkMessage[0]);
    }
}