package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lemba on 06.09.2018.
 */
public class DialogClients {
    private int idUser;
    private int idAgent;
    private List<Client> clientDialogList;
    DialogClients(User user, Agent agent){
        this.idUser=user.getId();
        this.idAgent=agent.getId();
        this.clientDialogList=new ArrayList<>(2);
        clientDialogList.add(user);
        clientDialogList.add(agent);
    }
    List<Client> getClientDialogList(){
        return clientDialogList;
    }
}
