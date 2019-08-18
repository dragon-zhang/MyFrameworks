import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
        //"5 5 3 8 1 5 3 4"
        int n = Integer.parseInt(bf.readLine());
        String[] strings = bf.readLine().split(" ");
        int[] high = new int[n];
        for (int i = 0; i < n; i++) {
            high[i] = Integer.parseInt(strings[i]);
        }
        int[] canSeeCounts = new int[high.length];
        for (int i = 0; i < high.length; i++) {
            int maxHigh;
            //从左找
            int left = i - 1;
            if (i - 1 >= 0) {
                maxHigh = high[left];
                for (int j = left; j >= 0; j--) {
                    if (maxHigh < high[j]) {
                        maxHigh = high[j];
                        canSeeCounts[i]++;
                    }
                }
                //至少能看见左边的第一栋
                canSeeCounts[i]++;
            }
            //从右找
            int right = i + 1;
            if (i + 1 < high.length) {
                maxHigh = high[right];
                for (int j = right; j < high.length; j++) {
                    if (maxHigh < high[j]) {
                        maxHigh = high[j];
                        canSeeCounts[i]++;
                    }
                }
                //至少能看见右边的第一栋
                canSeeCounts[i]++;
            }
            //至少能看见自己所在的大楼
            canSeeCounts[i]++;
        }
        System.out.println(Arrays.toString(canSeeCounts));
    }
}