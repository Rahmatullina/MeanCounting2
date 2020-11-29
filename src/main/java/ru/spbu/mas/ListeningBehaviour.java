package ru.spbu.mas;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class ListeningBehaviour extends CyclicBehaviour {
    Random random = new Random();
    @Override
    public void action() {
        ACLMessage msg3 = myAgent.receive();
        while (msg3 != null) {
            if (msg3.getPerformative() == ACLMessage.PROPOSE) {
                ((DefaultAgent)myAgent).state = DefaultAgent.State.valueOf(msg3.getContent());
                //System.out.println(myAgent.getAID().getLocalName() + "Got state: " + ((DefaultAgent)myAgent).state + "from sync agent");
            }
            else if (msg3.getPerformative() == ACLMessage.INFORM) {
                ((DefaultAgent) myAgent).numGet++;
                AID sender = msg3.getSender();
                float val = Float.parseFloat(msg3.getContent());
                ((DefaultAgent) myAgent).values.put(sender, val);
                //System.out.println(myAgent.getAID().getLocalName() + " got value from " + msg3.getSender().getLocalName());

            } else {
                //System.out.println(myAgent.getAID().getLocalName() + " received request from : " + msg3.getSender().getLocalName());
                ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                 if (random.nextInt(30) >= 10) {
                     // current number with noise
                     msg2.setContent(String.valueOf(((DefaultAgent) myAgent).getNumber()));
                 }
                 else{
                     // previous number with noise
                     msg2.setContent(String.valueOf(((DefaultAgent) myAgent).getNumber() - ((DefaultAgent) myAgent).u));
                 }
                msg2.addReceiver(msg3.getSender());
                myAgent.send(msg2);
                //System.out.println(myAgent.getAID().getLocalName() + " answered : " + msg3.getSender().getLocalName());
            }

            msg3 = myAgent.receive();
        }
    }
}
