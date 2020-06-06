package tools;

import java.io.*;

public class MatrixTool {

    public static double[][] getDisMatrix(String path, int cityNum) throws IOException {
        int[] x;
        int[] y;
        double distance[][];
        String buffer;
        BufferedReader br;

            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            distance = new double[cityNum][cityNum];
            x = new int[cityNum];
            y = new int[cityNum];
            //读取城市的坐标
            for (int i = 0; i < cityNum; i++) {
                buffer = br.readLine();
                String[] str = buffer.split(" +");
                x[i] = Integer.valueOf(str[1]);
                y[i] = Integer.valueOf(str[2]);
            }
            /**
             * 计算距离矩阵
             */
            for (int i = 0; i < cityNum; i++) {
                for (int j = i; j < cityNum; j++) {
                    double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])));
                    distance[i][j] = rij;
                    distance[j][i] = rij;
                }
            }

            return distance;


    }

}