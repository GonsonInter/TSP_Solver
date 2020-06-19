package Greedy;

import java.io.IOException;

public class Solver {
    public static void main(String[] args) throws IOException {
        System.out.println("Start....");
        Greedy ts = new Greedy(10);
        ts.init("C:\\Users\\Public\\TSP_Data\\TSP10cities.tsp");
        //ts.printinit();
        ts.solve();
    }
}
