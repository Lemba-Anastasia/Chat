package chat;

import chat.Client.Agent;
import chat.Client.Client;
import chat.Client.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lemba on 05.09.2018.
 */
public class ParticipantsBase {
    private final List<User> userList;
    private final List<Agent> agentList;

    public ParticipantsBase(){
        userList=new ArrayList<>(5);
        agentList=new ArrayList<>(5);
    }

    public void addUser(User user){
        synchronized (userList){
            userList.add(user);
        }
    }
    public void addAgents(Agent agent){
        synchronized (agentList) {
            agentList.add(agent);
        }
    }
    public void removeAgent(Agent agent){
        agentList.remove(agent);
    }

    public Agent getFreeAgent(){
        for(Agent a:agentList){
            if(!a.isBusy()){
                return a;
            }
        }
        return null;
    }


    public Client seachClientById(int id) {
        for(Client client:userList){
            if(client.getId()==id) return client;
        }
        for (Client client:agentList){
            if(client.getId()==id) return client;
        }
        return null;
    }
}
