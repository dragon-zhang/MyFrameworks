import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

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

    private static class Index {
        String symbol;
        int index;
        int level;

        public Index(String symbol, int index, int level) {
            this.symbol = symbol;
            this.index = index;
            this.level = level;
        }
    }

    private static void addIndex(String in, int level, LinkedList<Index> queue) {
        queue.clear();
        char[] chars = in.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '[') {
                queue.offer(new Index(String.valueOf(chars[i]), i, level));
            } else if (chars[i] == '|') {
                queue.offer(new Index(String.valueOf(chars[i]), i, level++));
            } else if (chars[i] == ']') {
                queue.offer(new Index(String.valueOf(chars[i]), i, --level));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        //如"A[2|B[2|C[2|D]]][2|E[2|F[2|G]]]H"
        String in = bf.readLine();
        int level = 0;
        LinkedList<Index> queue = new LinkedList<>();
        addIndex(in, level, queue);
        while (!queue.isEmpty()) {
            Index left = queue.poll();
            Index middle = queue.poll();
            Index right = null;
            for (int i = 0; i < queue.size(); i++) {
                Index index = queue.get(i);
                if ("]".equals(index.symbol) && index.level == left.level) {
                    right = index;
                    queue.remove(index);
                    break;
                }
            }
            int number = Integer.parseInt(in.substring(left.index + 1, middle.index));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < number; i++) {
                sb.append(in, middle.index + 1, right.index);
            }
            in = in.replace(in.substring(left.index, right.index + 1), sb.toString());
            addIndex(in, level, queue);
        }
        System.out.println(in);
    }
}