import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author SuccessZhang
 * 舍掉小数取整:Math.floor(3.5)=3
 * 四舍五入取整:Math.rint(3.5)=4
 * 向上取整:Math.ceil(3.1)=4
 * 向下取整:Math.floor(3.1)=3
 * 取绝对值：Math.abs(-3.5)=3.5
 * 取余数：A%B = 余数
 * Arrays.sort(new int[]{10,3,1,5,9});//快速排序实现，可直接使用
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

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bf.readLine().split(" ");
        String[] vis = bf.readLine().split(" ");
        String[] gis = bf.readLine().split(" ");
        //有n个物品，分别有价值vi和重量gi，背包最多承重m，则最多装多少价值的物品？
        /*测试用例
6 100
49 80 56 40 39 45
60 45 43 20 50 35
        */
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        int[] v = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            v[i] = Integer.parseInt(vis[i - 1]);
        }
        int[] g = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            g[i] = Integer.parseInt(gis[i - 1]);
        }
        //放n个物品，其重量为m，它的价值为result[][]
        int[][] result = new int[m + 1][n + 1];
        //基础情况，放0个物品或者物品总重量为0，其价值都必定是0
        for (int i = 1; i <= m; i++) {
            result[i][0] = 0;
        }
        for (int i = 1; i <= n; i++) {
            result[0][i] = 0;
        }
        //求解
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int v1 = 0;
                if (i >= g[j]) {
                    //只允许放一次，可放入物品数量-1
                    v1 = v[j] + result[i - g[j]][j - 1];
                    //允许放多次，可放入物品数量不变
                    //v1 = v[j] + result[i - g[j]][j];
                }
                int v2 = result[i][j - 1];
                result[i][j] = v1 > v2 ? v1 : v2;
            }
        }
        System.out.println(result[m][n]);
    }
}