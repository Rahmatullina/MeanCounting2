package ru.spbu.mas;

import java.util.Arrays;
import java.util.HashMap;

public class CircleTest {
    private HashMap<Integer, String> neighbors = new HashMap<Integer, String>();
    float [] values = {100.f, 150.f, 50.f, 200.f, 300.f};
    float [] u = new float[5];
    float alpha = 0.6f;
    CircleTest() {
        neighbors.put(0, "1");
        neighbors.put(1, "2");
        neighbors.put(2, "3");
        neighbors.put(3, "4");
        neighbors.put(4, "0");
    }
    void countMiddle(){
        boolean contin = true;
        int k =0;
        while(contin) {
            System.out.println("k = " + k + "\n");
            contin = false;
            for (int i = 0; i < values.length; i++)
            {
                u[i] = 0;
                for (String neigh : neighbors.get(i).split(","))
                {
                    u[i] += alpha * (values[Integer.parseInt(neigh)] - values[i]);
                }
                values[i] += u[i];
                for (float u : u)
                {
                    if (Math.abs(u) >= 0.0001)
                    {
                        contin = true;
                    }
                }
            }
            k++;
            System.out.println(Arrays.toString(values));
        }
    }
    public static void main(String[] args) {
        CircleTest test = new CircleTest();
        test.countMiddle();
    }
};

