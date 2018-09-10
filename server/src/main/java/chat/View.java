package chat;

/**
 * Created by Lemba on 05.09.2018.
 */
public class View {
    private Server server;
    public View(){
        addComandInArea("Сервер запустился");
        new Thread(new Server(View.this)).start();
        //server.closeServer();!предусмотреть

    }

    public void addComandInArea(String string){
        System.out.println(string);
    }
}
