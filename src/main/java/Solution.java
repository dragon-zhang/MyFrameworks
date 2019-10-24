/**
 * @author SuccessZhang
 */
public class Solution {

    public boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        boolean[] flags = new boolean[matrix.length];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (hasPath(matrix, rows, cols, i, j, str, 0, flags)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPath(char[] matrix,
                           int rows, int cols,
                           int row, int col,
                           char[] str,
                           int k, boolean[] flags) {
        int index = row * cols + col;
        //终止条件
        if (row < 0 || col < 0 || row >= rows || col >= cols ||
                str[k] != matrix[index] ||
                flags[index]) {
            return false;
        }
        if (k == str.length - 1) {
            return true;
        }
        //标志该点走过
        flags[index] = true;
        if (hasPath(matrix, rows, cols, row - 1, col, str, k + 1, flags) ||
                hasPath(matrix, rows, cols, row + 1, col, str, k + 1, flags) ||
                hasPath(matrix, rows, cols, row, col - 1, str, k + 1, flags) ||
                hasPath(matrix, rows, cols, row, col + 1, str, k + 1, flags)) {
            return true;
        }
        //行不通，回溯
        flags[index] = false;
        return false;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.hasPath(
                "ABCESFCSADEE".toCharArray(),
                3, 4,
                "ABCCED".toCharArray()));
    }
}