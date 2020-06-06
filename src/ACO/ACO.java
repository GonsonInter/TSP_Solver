package ACO;

import tools.MatrixTool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;



/**
 * 蚁群算法类
 *
 * @author gbs
 */
public class ACO {

    private Ant[] ants; //蚂蚁
    private int antNum; //蚂蚁数量
    private int cityNum; //城市数量
    private int MAX_GEN; //运行代数
    private double[][] pheromone; //信息素矩阵
    private double[][] distance; //距离矩阵
    private double bestLength; //最佳长度
    private int[] bestTour; //最佳路径
    //三个参数
    private double alpha;
    private double beta;
    private double rho;

    public ACO() {

    }

    public ACO(int antNum, int cityNum, int mAX_GEN, double alpha, double beta, double rho) {
        this.antNum = antNum;
        this.cityNum = cityNum;
        this.MAX_GEN = mAX_GEN;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.ants = new Ant[this.antNum];
    }

    public void init(String path) {
//        int []x;
//        int []y;
//        String buffer;
//        BufferedReader br;
//        try {
//            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
//            this.distance = new int[this.cityNum][this.cityNum];
//            x = new int[cityNum];
//            y = new int[cityNum];
//            //读取城市的坐标
//            for (int i = 0; i < cityNum; i++) {
//                buffer = br.readLine();
//                String[] str = buffer.split(" +");
//                x[i] = Integer.valueOf(str[1]);
//                y[i] = Integer.valueOf(str[2]);
//            }
//            /**
//             * 计算距离矩阵
//             */
//            for(int i = 0;i < this.cityNum - 1;i++) {
//                for(int j = i + 1;j < this.cityNum;j++) {
//                    double rij = Math.sqrt(((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j])));
//                    int tij = (int)Math.round(rij);
//                    if(tij < rij)
//                        tij++;
//                    this.distance[i][j] = tij;
//                    this.distance[j][i] = tij;
//                }
//            }
//            this.distance[this.cityNum-1][this.cityNum-1] = 0;
        //初始化信息素矩阵

        try {

            this.distance = MatrixTool.getDisMatrix(path, this.cityNum);

            this.pheromone = new double[this.cityNum][this.cityNum];
            for (int i = 0; i < this.cityNum; i++) {
                for (int j = 0; j < this.cityNum; j++) {
                    this.pheromone[i][j] = 0.1d;
                }
            }
            //初始化最优路径的长度
            this.bestLength = Integer.MAX_VALUE;
            //初始化最优路径
            this.bestTour = new int[this.cityNum + 1];
            //随机放置蚂蚁
            for (int i = 0; i < this.antNum; i++) {
                this.ants[i] = new Ant(this.cityNum);
                this.ants[i].init(this.distance, this.alpha, this.beta);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新信息素
     */
    private void updatePheromone() {
        //信息素挥发
        for (int i = 0; i < this.cityNum; i++)
            for (int j = 0; j < this.cityNum; j++)
                this.pheromone[i][j] = this.pheromone[i][j] * (1 - this.rho);
        //信息素更新
        for (int i = 0; i < this.cityNum; i++) {
            for (int j = 0; j < this.cityNum; j++) {
                for (int k = 0; k < this.antNum; k++) {
                    this.pheromone[i][j] += this.ants[k].getDelta()[i][j];
                }
            }
        }
    }

    public void solve() {
        for (int g = 0; g < this.MAX_GEN; g++) {
            //每一只蚂蚁移动的过程
            for (int i = 0; i < this.antNum; i++) {
                for (int j = 0; j < this.cityNum; j++) {
                    this.ants[i].selectNextCity(this.pheromone);
                }
                this.ants[i].getTabu().add(this.ants[i].getFirstCity());

                //计算蚂蚁获得的路径长度
                this.ants[i].setTourLength(this.ants[i].calculateTourLength());
                if (this.ants[i].getTourLength() < this.bestLength) {
                    //保留最优路径
                    this.bestLength = this.ants[i].getTourLength();
                    System.out.println("第" + g + "代，发现新的解" + this.bestLength);
                    for (int k = 0; k < this.ants[i].getTabu().size(); k++)
                        this.bestTour[k] = this.ants[i].getTabu().get(k).intValue();
                }
                //更新信息素变化矩阵
                for (int j = 0; j < this.ants[i].getTabu().size() - 1; j++) {
                    this.ants[i].getDelta()[this.ants[i].getTabu().get(j).intValue()][this.ants[i].getTabu().get(j + 1).intValue()] = (double) (1.0 / this.ants[i].getTourLength());
                    this.ants[i].getDelta()[this.ants[i].getTabu().get(j + 1).intValue()][this.ants[i].getTabu().get(j).intValue()] = (double) (1.0 / this.ants[i].getTourLength());
                }
            }
            //更新信息素
            this.updatePheromone();
            //重新初始化蚂蚁
            for (int i = 0; i < this.antNum; i++) {
                this.ants[i].init(this.distance, this.alpha, this.beta);
            }
        }
        //打印最佳结果
        this.printOptimal();
    }

    public void printOptimal() {
        System.out.println("最佳路径长度: " + this.bestLength);
        System.out.println("最佳路径: ");
        for (int i = 0; i < this.bestTour.length; i++) {
            System.out.print(this.bestTour[i] + 1 + " ");
        }
    }

    public Ant[] getAnts() {
        return ants;
    }

    public void setAnts(Ant[] ants) {
        this.ants = ants;
    }

    public int getAntNum() {
        return antNum;
    }

    public void setAntNum(int antNum) {
        this.antNum = antNum;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public int getMAX_GEN() {
        return MAX_GEN;
    }

    public void setMAX_GEN(int mAX_GEN) {
        MAX_GEN = mAX_GEN;
    }

    public double[][] getPheromone() {
        return pheromone;
    }

    public void setPheromone(double[][] pheromone) {
        this.pheromone = pheromone;
    }

    public double[][] getDistance() {
        return distance;
    }

    public void setDistance(double[][] distance) {
        this.distance = distance;
    }

    public double getBestLength() {
        return bestLength;
    }

    public void setBestLength(int bestLength) {
        this.bestLength = bestLength;
    }

    public int[] getBestTour() {
        return bestTour;
    }

    public void setBestTour(int[] bestTour) {
        this.bestTour = bestTour;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    @Override
    public String toString() {
        return "ACO [ants=" + Arrays.toString(ants) + ", antNum=" + antNum + ", cityNum=" + cityNum + ", MAX_GEN="
                + MAX_GEN + ", pheromone=" + Arrays.toString(pheromone) + ", distance=" + Arrays.toString(distance)
                + ", bestLength=" + bestLength + ", bestTour=" + Arrays.toString(bestTour) + ", alpha=" + alpha
                + ", beta=" + beta + ", rho=" + rho + "]";
    }

}