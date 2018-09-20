import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Lemba on 19.09.2018.
 */
public class ReaderRunnableTest extends TestCase {
    public void testRun() throws Exception {
        final BufferedReader bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine()).thenReturn("asdf").thenThrow(new IOException());
        ReaderRunnable.Callback callback = new ReaderRunnable.Callback() {
            public void onDataReceived(String data) {
                assertEquals(data, "asdf");
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(new ReaderRunnable(bufferedReader, callback)).start();
    }

}