package chat;

import chat.Client.Agent;
import chat.Client.User;
import junit.framework.TestCase;

import java.net.Socket;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Lemba on 17.09.2018.
 */
public class ParticipantsBaseTest extends TestCase {

    public void testAddUser() throws Exception {
        ParticipantsBase participantsBase=new ParticipantsBase();
        User user=new User("Polo",mock(Socket.class));
        participantsBase.addUser(user);
        List<User> listUsers=participantsBase.getUserList();
        for(User u:listUsers){
            if(u.equals(user)) assertEquals(u,user);
        }
    }

    public void testAddAgents() throws Exception {
        ParticipantsBase participantsBase=new ParticipantsBase();
        Agent agent=new Agent("Vasja",mock(Socket.class));
        participantsBase.addAgents(agent);
        List<Agent> listAgents=participantsBase.getAgentList();
        for(Agent a:listAgents){
            if(a.equals(agent)) assertEquals(a,agent);
        }
    }
    public void testGetUsserList(){
        ParticipantsBase participantsBase=new ParticipantsBase();
        User user=new User("Polo",mock(Socket.class));
        participantsBase.addUser(user);
        assertNotNull(participantsBase.getUserList());
    }

    public void testGetAgentList(){
        ParticipantsBase participantsBase=new ParticipantsBase();
        Agent agent=new Agent("Vasja",mock(Socket.class));
        participantsBase.addAgents(agent);
        assertNotNull(participantsBase.getAgentList());
    }
}