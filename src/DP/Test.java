package DP;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        long a=System.currentTimeMillis();
        DynamicProgramming tsp=new DynamicProgramming(10);//建立对象，根据需要初始化10,25或100
        tsp.readData("C:\\Users\\Public\\TSP_Data\\TSP10cities.tsp");//读取数据
        tsp.solve();
        long b=System.currentTimeMillis();
        long c=b-a;
        System.out.println("运行时间为:"+c+"秒");//输出运行时间
    }
}
