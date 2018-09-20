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

    public List<User> getUserList() {
        return userList;
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public void remove(Client client) {
        for(User u:userList){
            if(u==client){
                userList.remove(u);
                return;
            }
        }
        for(Agent a:agentList){
            if(a==client){
                agentList.remove(a);
                return;
            }
        }
    }
}
