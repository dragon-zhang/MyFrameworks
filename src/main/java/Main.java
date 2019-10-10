import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * @author SuccessZhang
 * 舍掉小数取整:Math.floor(3.5)=3
 * 四舍五入取整:Math.rint(3.5)=4
 * 向上取整:Math.ceil(3.1)=4
 * 向下取整:Math.floor(3.1)=3
 * 取绝对值：Math.abs(-3.5)=3.5
 * 求n方根：Math.pow(target, 1d/n)，如Math.pow(27, 1d/3)=3.0
 * 求sin30°：round(Math.sin(Math.toRadians(30)),15)=0.5
 * 注：上一行中round(double original, int digit)为本类底下的自定义函数
 * 取余数：A%B = 余数
 * <p>
 * TreeMap在put时会按照key从小到大排序，
 * 因而在for(Map.Entry<K, V> entry: treeMap.entrySet()){}遍历时
 * 会按照key从小到大遍历;
 * TreeMap.floorEntry(key)可以找到小于或等于key的最大键值对，
 * 如果不存在返回null
 * Map.Entry<K, V> entry可通过entry.getKey()直接获取key;
 * 通过entry.getValue()可直接获取value;
 * <p>
 * Arrays.sort(new int[]{10,3,1,5,9});//快速排序实现，可直接使用
 * Arrays.stream(new int[]{1,2,3}).sum();//数组求和，可直接使用
 * Collections.shuffle(list);//打乱顺序
 * Collections.sort(list);//排序
 * <p>
 * 栈 java.util.Stack
 * 取栈顶值（不出栈）stack.peek();
 * 进栈stack.push(Object);
 * 出栈stack.pop();
 * <p>
 * 队列 java.util.Queue
 * 实现类java.util.LinkedList
 * 取队首值（不出队）queue.peek();
 * 入队queue.offer(Object);
 * 出队queue.poll();
 * <p>
 * 流取最大值stream().max((x, y) -> x > y ? 1 : -1).get()
 */
@SuppressWarnings("unused")
public class Main {

    /**
     * 运用试除法:
     * 1.只有奇数需要被测试
     * 2.测试范围从2与根号n
     */
    private static boolean isPrime(int n) {
        if (n > 2 && (n & 1) == 0) {
            return false;
        }
        for (int i = 3; i <= Math.pow(n, 1.0 / 2); i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串去重，保留第1个，如aaabbbccdde->abcde、aba->ab
     */
    public static String removeRepeatRetainFirst(String original) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            char charWord = original.charAt(i);
            int firstPosition = original.indexOf(charWord);
            int lastPosition = original.lastIndexOf(charWord);
            if (firstPosition == lastPosition || firstPosition == i) {
                sb.append(charWord);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串去重，保留最后1个，如aba->ba
     */
    public static String removeRepeatRetainLast(String original) {
        return original.replaceAll("(.)(?=.*\\1)", "");
    }

    /**
     * 字符串反转，如db->bd
     */
    public static String reverse(String original) {
        return new StringBuilder(original).reverse().toString();
    }

    /**
     * 四舍五入
     *
     * @param original 原始数据
     * @param digit    需要四舍五入的位数
     * @return 四舍五入后的数(可能有小数)
     */
    public static double round(double original, int digit) {
        return new BigDecimal(original).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 求∠B的角度
     *
     * @param a a边的长度
     * @param b b边的长度
     * @param c c边的长度
     * @return ∠B的角度
     */
    private static double getAngleDegree(double a, double b, double c) {
        return Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2.0 * a * c)));
    }

    /**
     * 计算两点之间的距离
     *
     * @param x1 点1的横坐标
     * @param y1 点1的纵坐标
     * @param x2 点2的横坐标
     * @param y2 点2的纵坐标
     */
    private static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.pow(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2), 1d / 2);
    }

    /**
     * 计算最大公约数
     */
    private static int getGreatestCommonDivisor(int a, int b) {
        int num = 1;
        for (int i = 1; i <= a && i <= b; i++) {
            if (a % i == 0 && b % i == 0) {
                num = i;
            }
        }
        return num;
    }

    /**
     * 计算最小公倍数
     */
    private static int getMinimumCommonMultiple(int m, int n) {
        //将m,n转化成最大公约数为1，那之后他们的乘积就是代表最小公倍数
        return n * m / getGreatestCommonDivisor(m, n);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String[] strings = bf.readLine().split(" ");
    }
}