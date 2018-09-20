package chat.Client;

import junit.framework.TestCase;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Lemba on 17.09.2018.
 */
public class AgentTest extends TestCase {
    public void testIsBusy() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        Agent agent=new Agent("Vasja",mock);
        agent.setCompanion(new User("Polo",mock));
        assertEquals(true,agent.isBusy());
    }

    public void testGetSocket() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        Agent agent=new Agent("Vasja",mock);
        assertNotNull(agent.getSocket());
    }

    public void testGetId() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        Agent agent=new Agent("Vasja",mock);
        assertNotSame(0,agent.getId());
    }

    public void testSendMessage() throws Exception {
        final String message="hello";
        final String[] getterMessage = {""};
        Socket mock = Mockito.mock(Socket.class);
        Agent agent = new Agent("Vasja",mock);
        agent.setCompanion(mock(User.class));
        when(mock.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                getterMessage[0] +=(char)b;
            }
        });
        agent.sendMessage(message,agent.getName());
        assertEquals("Vasja: hello\n",getterMessage[0]);
    }

    public void testSendMessageMyself() throws Exception {
        final String[] getterMessage = {""};
        Socket mockSocket = Mockito.mock(Socket.class);
        Agent agent= new Agent("Vasja",mockSocket);
        when(mockSocket.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                getterMessage[0]+=(char)b;
            }
        });
        agent.sendMessageMyself("hello");
        assertEquals("server: hello\n",getterMessage[0]);
    }

    public void testGetName() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        Agent agent= new Agent("Vasja",mock);
        assertNotSame("",agent.getName());
    }

    public void testGetCompanion() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        Agent agent= new Agent("Vasja",mock);
        agent.setCompanion(new User("Polo",mock));
        assertNotNull(agent.getCompanion());
    }

    public void testSetCompanion() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        Agent agent= new Agent("Vasja",mock);
        agent.setCompanion(new User("Polo",mock));
        assertNotNull(agent.getCompanion());
    }

}