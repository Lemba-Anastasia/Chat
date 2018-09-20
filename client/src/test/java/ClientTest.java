import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by Lemba on 18.09.2018.
 */
public class ClientTest extends TestCase {
    /*public void testSendMessage() throws Exception {
        InputStream inputStream=mock(InputStream.class);
        final String[] getterMessage = {""};
        OutputStream outputStream=new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                getterMessage[0] +=(char)b;
            }
        };
        Client client=new Client(inputStream,outputStream);
        client.sendMessage("hello");
        assertEquals("hello",getterMessage[0]);
    }*/
}