package ACO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
/**
 * 蚂蚁类
 * Cloneable这个类为克隆类，使用Object的clone()方法
 * @author gbs
 *
 */
public class Ant implements Cloneable{

    /**
     * Vector与ArrayList一样，也是通过数组实现的，不同的是它支持线程的同步，
     * 即某一时刻只有一个线程能够写Vector，避免多线程同时写而引起的不一致性，
     * 但实现同步需要很高的花费，因此，访问它比访问ArrayList慢。
     */
    private ArrayList<Integer> allowedCities;//允许访问的城市

    private ArrayList<Integer> tabu;//禁忌表，记录已经访问过的城市

    private double [][] distance;//距离矩阵

    private double[][] delta; //信息素变化矩阵

    private double alpha;

    private double beta;

    private int cityNum;//城市数量

    private double tourLength;//路径的长度

    private int firstCity; //起始城市

    private int currentCity; //当前城市

    public Ant(int cityNum) {
        this.cityNum = cityNum;
        this.tourLength = 0;
    }

    public Ant() {
        this.cityNum = 30;
        this.tourLength = 0;
    }

    /**
     * 初始化蚂蚁，并为蚂蚁随机选择第一个城市
     * @param distance
     * @param a
     * @param b
     */
    public void init(double[][] distance,double a,double b) {
        this.alpha = a;
        this.beta = b;
        this.distance = distance;
        this.allowedCities = new ArrayList<Integer>();
        this.tabu = new ArrayList<Integer>();
        this.delta = new double[cityNum][cityNum];
        for(int i = 0;i < this.cityNum;i++) {
            Integer city = new Integer(i);
            this.allowedCities.add(city);
            for (int j = 0;j < this.cityNum;j++) {
                this.delta[i][j] = 0.d;
            }
        }

        Random random = new Random(System.currentTimeMillis());
        this.firstCity = random.nextInt(this.cityNum);

        for (Integer city:this.allowedCities) {
            if(city.intValue() == this.firstCity) {
                this.allowedCities.remove(city);
                this.tabu.add(city);
                break;
            }
        }

        this.currentCity = this.firstCity;
    }

    /**
     * 选择下一个城市
     * @param pheromone 信息素矩阵
     */
    public void selectNextCity(double[][] pheromone) {
        double[] p = new double[this.cityNum];//转移概率
        double sum = 0.d;//转移概率的分母
        for (Integer city : this.allowedCities) {
            sum += Math.pow(pheromone[this.currentCity][city.intValue()],
                    this.alpha)*Math.pow(1.d/this.distance[this.currentCity][city.intValue()], this.beta);
        }
//      double s = 0.d;
        for (int i = 0; i < this.cityNum; i++) {
            boolean flag = false;
            for (Integer city : this.allowedCities) {
                if(i == city.intValue()) {
                    p[i] = (double) ((Math.pow(pheromone[this.currentCity][i],
                            this.alpha)*Math.pow(1.d/this.distance[this.currentCity][i], this.beta))/sum);
                    flag = true;
                    break;
                }
            }
            if(!flag)
                p[i] = 0.d;
//          s += p[i];
        }
//      if(Double.isNaN(s)) {
//          for(int i =0;i < this.cityNum;i++) {
//              System.out.println(p[i]);
//          }
//      }

        /**
         * 如果每次都直接选择最大概率的城市作为下一个城市，就会使算法过早收敛，
         * 最后停止搜索可能获得的仅仅是次优解，而使用轮盘赌可以提高算法的全局搜索能力，又不失局部搜索
         * 所以这里选择轮盘赌选择下一个城市。参考《计算智能》清华大学出版社
         */
        //轮盘赌选择下一个城市
        double sumSelect = 0.d;
        int selectCity = -1;
        Random random = new Random(System.currentTimeMillis());
        double selectP = random.nextDouble();
        while(selectP == 0.f) {
            selectP = random.nextDouble();
        }
        for(int i = 0;i < this.cityNum;i++) {
            sumSelect += p[i];
            if(sumSelect >= selectP) {
                selectCity = i;
                //从允许选择的城市中去除select city
                this.allowedCities.remove(Integer.valueOf(selectCity));
                //在禁忌表中添加select city
                this.tabu.add(Integer.valueOf(selectCity));
                //将当前城市改为选择的城市
                this.currentCity = selectCity;
                break;
            }
        }
    }

    /**
     * 计算路径长度
     * @return
     */
    public int calculateTourLength() {
        int length = 0;
//      if(this.tabu.size() == 1) {
//          return 0;
//      }
        for(int i = 0;i < this.tabu.size()-1;i++) {
            length += this.distance[this.tabu.get(i).intValue()][this.tabu.get(i+1).intValue()];
        }
        return length;
    }

    public ArrayList<Integer> getAllowedCities() {
        return allowedCities;
    }

    public void setAllowedCities(ArrayList<Integer> allowedCities) {
        this.allowedCities = allowedCities;
    }

    public ArrayList<Integer> getTabu() {
        return tabu;
    }

    public void setTabu(ArrayList<Integer> tabu) {
        this.tabu = tabu;
    }

    public double[][] getDistance() {
        return distance;
    }

    public void setDistance(double[][] distance) {
        this.distance = distance;
    }

    public double[][] getDelta() {
        return delta;
    }

    public void setDelta(double[][] delta) {
        this.delta = delta;
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

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public double getTourLength() {
        return tourLength;
    }

    public void setTourLength(int tourLength) {
        this.tourLength = tourLength;
    }

    public int getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(int firstCity) {
        this.firstCity = firstCity;
    }

    public int getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(int currentCity) {
        this.currentCity = currentCity;
    }

    @Override
    public String toString() {
        return "Ant [allowedCities=" + allowedCities + ", tabu=" + tabu + ", distance=" + Arrays.toString(distance)
                + ", delta=" + Arrays.toString(delta) + ", alpha=" + alpha + ", beta=" + beta + ", cityNum=" + cityNum
                + ", tourLength=" + tourLength + ", firstCity=" + firstCity + ", currentCity=" + currentCity + "]";
    }

}
