package ACO;

public class Test {
    public static void main(String[] args) {
        ACO aco = new ACO(50, 10, 2000, 1.d, 5.d, 0.5d);
        aco.init("E:\\IntelliJ Projects\\TSP_Solver\\tsp_data\\TSP100cities.tsp");
        aco.solve();
    }
}
