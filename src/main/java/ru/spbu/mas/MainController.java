package ru.spbu.mas;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class MainController {
    private static final int numberOfAgents = 5;
    void initAgents() {
        // Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();
        //Create a container to host the Default Agent
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "8080");
        p.setParameter(Profile.GUI, "false");
        ContainerController cc = rt.createMainContainer(p);
        try {
            HashMap<Integer, String> neighbors = new HashMap<Integer, String>();
            //neighbors.put(0, "1");
            //neighbors.put(1, "2");
            //neighbors.put(2, "3");
            //neighbors.put(3, "4");
            //neighbors.put(4, "0");
            neighbors.put(0, "1, 2, 3");
            neighbors.put(1, "0, 3");
            neighbors.put(2, "0, 3, 4");
            neighbors.put(3, "0, 1, 2, 4");
            neighbors.put(4, "2, 3");

            Map.Entry<Integer, String> maxEntry = Collections.max(neighbors.entrySet(), new Comparator<Map.Entry<Integer, String>>() {
                @Override
                public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                    int s1 = o1.getValue().split(",").length;
                    int s2 = o2.getValue().split(",").length;
                    return s1 - s2;                }
            });
            final int dmax = maxEntry.getValue().split(",").length;
            float alpha = 1.f/dmax;
            int numIterations = 15;
            AgentController agent;
            agent = cc.createNewAgent(Integer.toString(MainController.numberOfAgents + 1),
                    "ru.spbu.mas.SynchronizationAgent", new Object[]{numIterations});
            agent.start();
            for(int i=0; i < MainController.numberOfAgents; i++) {
                float number = i*10;
                agent = cc.createNewAgent(Integer.toString(i),
                            "ru.spbu.mas.DefaultAgent", new Object[]{neighbors.get(i), number, alpha, MainController.numberOfAgents + 1});

                agent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
