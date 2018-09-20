import java.io.*;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * Created by Lemba on 05.09.2018.
 */
public class Client {
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Socket socket = null;
    private static final Logger log = Logger.getLogger(Client.class);


    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
    }
    public Client(BufferedReader in,PrintWriter out)throws IOException{
        this.in=in;
        this.out=out;
    }



    public void sendMessage(String message){
//        byte[] arrCharsMessage=new byte[message.length()+1];
//        for(int i=0;i<message.length();i++){
//            arrCharsMessage[i]= (byte) message.charAt(i);
//        }
//        arrCharsMessage[message.length()]=0;
        //out.write(arrCharsMessage);
        out.println(message);
        out.flush();
    }

    public BufferedReader getIn() {
        return in;
    }
}
