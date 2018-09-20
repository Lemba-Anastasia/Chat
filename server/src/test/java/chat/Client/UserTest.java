package chat.Client;
import junit.framework.TestCase;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.*;
/**
 * Created by Lemba on 16.09.2018.
 */
public class UserTest extends TestCase {

    public void testSendMessage() throws Exception {
        final String message="hello";
        final String[] getterMessage = {""};
        Socket mock = Mockito.mock(Socket.class);
        User user = new User("Polo",mock);
        user.setCompanion(mock(Agent.class));
        when(mock.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                 getterMessage[0] +=(char)b;
            }
        });
        user.sendMessage(message,user.getName());
        assertEquals("Polo: hello\n",getterMessage[0]);
    }

    public void testSendMessageMyself() throws Exception {
        final String[] getterMessage = {""};
        Socket mockSocket = Mockito.mock(Socket.class);
        User user = new User("Polo",mockSocket);
        when(mockSocket.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                getterMessage[0]+=(char)b;
            }
        });
        user.sendMessageMyself("hello");
        assertEquals("server: hello\n",getterMessage[0]);
    }

    public void testIsBusy() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        user.setCompanion(new Agent("Vasja",mock));
        assertEquals(true,user.isBusy());
    }

    public void testSetCompanion() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        user.setCompanion(new Agent("Vasja",mock));
        assertNotNull(user.getCompanion());
    }

    public void testGetSocket() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        assertNotNull(user.getSocket());
    }

    public void testGetCompanion() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        user.setCompanion(new Agent("Vasja",mock));
        assertNotNull(user.getCompanion());
    }

    public void testGetName() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        assertNotSame("",user.getName());
    }

    public void testSetBufferMessages() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        user.setBufferMessages("sam message");
        assertNotSame(null,user.getWaitingMessages());
    }

    public void testGetWaitingMessages() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        user.setBufferMessages("sam message");
        assertEquals(user.getName()+": "+"sam message"+"\n",user.getWaitingMessages());
    }
    public void testGetId() throws Exception {
        Socket mock = Mockito.mock(Socket.class);
        User user=new User("Polo",mock);
        assertNotSame(0,user.getId());
    }

}