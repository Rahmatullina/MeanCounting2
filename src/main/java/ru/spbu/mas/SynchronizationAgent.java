package ru.spbu.mas;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SynchronizationAgent extends Agent {

    protected DefaultAgent.State state = DefaultAgent.State.request;
    int numIterations;
    int curNumIterations;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            numIterations = Integer.parseInt(args[0].toString());
        }
        curNumIterations = 0;
        int id = Integer.parseInt(this.getAID().getLocalName());
        System.out.println("Synchronization Agent #" + id + "Name : " + getLocalName());
        ParallelBehaviour concurrent = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL);

        concurrent.addSubBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                if (state == DefaultAgent.State.update)
                    if(curNumIterations< numIterations)
                        state = DefaultAgent.State.request;
                    else state = DefaultAgent.State.end;
                else if (state == DefaultAgent.State.request){
                    state = DefaultAgent.State.update;
                    curNumIterations++;
                    System.out.println("Num iteration: " + curNumIterations);
                }
            }
        });
        concurrent.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
                while (msg != null){
                    ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
                    reply.setContent("" + state);
                    reply.addReceiver(msg.getSender());
                    myAgent.send(reply);
                    msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
                }
            }
        });
        addBehaviour(concurrent);
    }
}
