import java.util.Arrays;

/**
 * @author SuccessZhang
 */
public class Solution {

    /**
     * @param n 总人数
     * @param m 约瑟夫环编号为m的人退出，剩下的继续组成约瑟夫环
     * @param k 最后幸存的人数
     */
    public int[] LastRemaining_Solution(int n, int m, int k) {
        if (n <= 0 || m <= 0) {
            return new int[]{-1};
        }
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = i;
        }
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < k; j++) {
                result[j] = (result[j] + m) % i;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.LastRemaining_Solution(5, 3, 0)));
    }
}