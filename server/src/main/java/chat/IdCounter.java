package chat;

/**
 * Created by Lemba on 05.09.2018.
 */
public class IdCounter {
    static IdCounter idCounter = new IdCounter();
    private int id = 0;

    private IdCounter() {

    }

    public static IdCounter getInstance() {
        return idCounter;
    }

    public synchronized int getId(){
        //System.out.println(id+"<- id");
        return ++id;
    }
}
