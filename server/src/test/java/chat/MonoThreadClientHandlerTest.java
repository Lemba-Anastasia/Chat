package chat;

import chat.Client.Client;
import junit.framework.TestCase;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Lemba on 16.09.2018.
 */
public class MonoThreadClientHandlerTest extends TestCase {
//    public void testAbs() throws IOException {
//        Socket mockSocket = mock(Socket.class);
//        InputStream mockInputStream = Mockito.mock(InputStream.class);
//        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
//        String message = "/reg user u";
//        int[] arr = new int[message.length()];
//        for (int i = 0; i < message.length(); i++) {
//            arr[i] = (int) message.charAt(i);
//        }
//        when(mockInputStream.read()).thenReturn(arr[0]).thenReturn(arr[1]).thenReturn(arr[2]).thenReturn(arr[3]).thenReturn(arr[4]).thenReturn(arr[5]).thenReturn(arr[6]).thenReturn(arr[7]).thenReturn(arr[8]).thenReturn(arr[9]).thenReturn(arr[10]).thenReturn(-1);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mockInputStream));
//        int ch;
//        StringBuilder sb = new StringBuilder();
//        while((ch = mockInputStream.read()) != -1)
//            sb.append((char)ch);
//        System.out.println(sb.toString());
//    }

    public void testRun() throws Exception {
        Socket mockSocket = mock(Socket.class);
        String message = "/reg user u";
        Server mockServer = mock(Server.class);
        BufferedReader mockBufferedReader = Mockito.mock(BufferedReader.class);
        when(mockBufferedReader.readLine()).thenReturn(message);
        PrintWriter mockPrintWriter = mock(PrintWriter.class);
        when(mockSocket.getInputStream()).thenReturn(mock(InputStream.class));
        when(mockSocket.getOutputStream()).thenReturn(mock(OutputStream.class));
        MonoThreadClientHandler mtch = new MonoThreadClientHandler(mockSocket, mockBufferedReader, mockPrintWriter, mockServer);
        ParticipantsBase participantsBase = new ParticipantsBase();
        when(mockServer.getParticipantsBase()).thenReturn(participantsBase);
        when(mockSocket.isClosed()).thenReturn(false).thenReturn(true);
        mtch.run();
        assertEquals(participantsBase.getUserList().get(0).getName(),"u");
    }

    public void testRun2() throws Exception {
        Socket mockSocket = mock(Socket.class);
        String message = "/reg agent a";
        Server mockServer = mock(Server.class);
        BufferedReader mockBufferedReader = Mockito.mock(BufferedReader.class);
        when(mockBufferedReader.readLine()).thenReturn(message);
        PrintWriter mockPrintWriter = mock(PrintWriter.class);
        when(mockSocket.getInputStream()).thenReturn(mock(InputStream.class));
        when(mockSocket.getOutputStream()).thenReturn(mock(OutputStream.class));
        MonoThreadClientHandler mtch = new MonoThreadClientHandler(mockSocket, mockBufferedReader, mockPrintWriter, mockServer);
        ParticipantsBase participantsBase = new ParticipantsBase();
        when(mockServer.getParticipantsBase()).thenReturn(participantsBase);
        when(mockSocket.isClosed()).thenReturn(false).thenReturn(true);
        mtch.run();
        assertEquals(participantsBase.getAgentList().get(0).getName(),"a");
    }

//    public void testRun3() throws Exception {
//        Socket mockSocket = mock(Socket.class);
//        String message1 = "/reg user u";
//        String message2 = "/close";
//        Server mockServer = mock(Server.class);
//        BufferedReader mockBufferedReader = Mockito.mock(BufferedReader.class);
//        when(mockBufferedReader.readLine()).thenReturn(message1).thenReturn(message2);
//        BufferedReader mockBufferedReader2 = Mockito.mock(BufferedReader.class);
//        when(mockBufferedReader.readLine()).thenReturn(message1).thenReturn(message2);
//        PrintWriter mockPrintWriter = mock(PrintWriter.class);
//        when(mockSocket.getInputStream()).thenReturn(mock(InputStream.class));
//        when(mockSocket.getOutputStream()).thenReturn(mock(OutputStream.class));
//        MonoThreadClientHandler mtch = new MonoThreadClientHandler(mockSocket, mockBufferedReader, mockPrintWriter, mockServer);
//        MonoThreadClientHandler mtch2 = new MonoThreadClientHandler(mockSocket, mockBufferedReader2, mockPrintWriter, mockServer);
//        ParticipantsBase participantsBase = new ParticipantsBase();
//        //do(participantsBase.removeAgent("a"));
//        //do(participantsBase.removeAgent("a")).when(mockServer).exit(any(Client.class),any(MonoThreadClientHandler.class));
//        when(mockSocket.isClosed()).thenReturn(false).thenReturn(false).thenReturn(true);
//        mtch.run();
//        assertEquals(null,participantsBase.getAgentList());
//    }

}