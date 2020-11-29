package ru.spbu.mas;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DefaultAgent extends Agent {
    enum State {update, request, end};
    protected ArrayList<AID> linkedAgents = new ArrayList<>();
    protected float number;
    protected float alpha;
    protected int numGet = 0;
    protected int numSent = 0;
    protected float u  = 0;
    protected AID syncAgent;
    protected State state;
    protected HashMap<AID, Float> values = new HashMap<>();
    Random random = new Random();

    @Override
    protected void setup() {
        //parse args
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            String[] neighbors = args[0].toString().split(", ");
            number = Float.parseFloat(args[1].toString());
            alpha = Float.parseFloat(args[2].toString());
            syncAgent = new AID(args[3].toString(), AID.ISLOCALNAME);
            for (String neighbor : neighbors) {
                AID uid = new AID(neighbor, AID.ISLOCALNAME);
                linkedAgents.add(uid);
            }
        }
        int id = Integer.parseInt(this.getAID().getLocalName());
        System.out.println("Agent #" + id + "Name : " + getLocalName() + "Number : " + number);
        ParallelBehaviour concurrent = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);

        concurrent.addSubBehaviour(new ListeningBehaviour());
        concurrent.addSubBehaviour(new CasualBehaviour());
        concurrent.addSubBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                    ACLMessage msgSync = new ACLMessage(ACLMessage.REQUEST);
                    msgSync.addReceiver(((DefaultAgent) myAgent).syncAgent);
                    myAgent.send(msgSync);
            }
        });
        addBehaviour(concurrent);
    }
    float getNumber(){
        return number + (random.nextFloat() * 2.f) - 1.f;
    }
}
