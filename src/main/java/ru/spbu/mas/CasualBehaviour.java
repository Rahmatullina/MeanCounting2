package ru.spbu.mas;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class CasualBehaviour extends Behaviour {
    Random random = new Random();
    boolean didSent = false;
    @Override
    public void action()  {

        if( ((DefaultAgent) myAgent).state == DefaultAgent.State.request && !didSent) {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                for (AID neighbour : ((DefaultAgent) myAgent).linkedAgents) {
                    if (random.nextInt(30) >= 6) {
                    //System.out.println(myAgent.getAID().getLocalName() + " added receiver: " + neighbour.getLocalName());
                    msg.addReceiver(neighbour);
                    ((DefaultAgent) myAgent).numSent++;
                    }
                }
                //System.out.println(myAgent.getAID().getLocalName() + " sent request all neighbours");
                myAgent.send(msg);
                didSent = true;
        }
        else if (((DefaultAgent) myAgent).state == DefaultAgent.State.update) {

           if (((DefaultAgent) myAgent).numGet == ((DefaultAgent) myAgent).numSent && ((DefaultAgent) myAgent).numGet > 0) {
               ((DefaultAgent) myAgent).u = 0;
               for (float val : ((DefaultAgent) myAgent).values.values())
               {
                   ((DefaultAgent) myAgent).u += ((DefaultAgent) myAgent).alpha * (val - ((DefaultAgent) myAgent).getNumber());
               }
               ((DefaultAgent) myAgent).number += ((DefaultAgent) myAgent).u;
               System.out.println(myAgent.getAID().getLocalName()  + " new value : " + ((DefaultAgent) myAgent).getNumber());
                   ((DefaultAgent) myAgent).numGet = 0;
                   ((DefaultAgent) myAgent).numSent = 0;

           }
           didSent = false;
        }
        else if (((DefaultAgent) myAgent).state == DefaultAgent.State.end){
            System.out.println("Final value of  = " + myAgent.getAID().getLocalName() + "is " + ((DefaultAgent)myAgent).getNumber());
        }

    }

    @Override
    public boolean done() {
        return ((DefaultAgent)myAgent).state == DefaultAgent.State.end;
    }
}
