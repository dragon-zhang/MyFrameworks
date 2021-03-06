package sort;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unused")
public class SortUtil {

    /**
     * 直接插入排序
     * 最好情况：O(n)
     * 最坏情况：O(n^2)
     * 辅助空间：O(1)
     * 稳定性：稳定
     *
     * @param array 原始数组
     */
    public static void insertSort(int[] array) {
        int i, j, temp;
        for (i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                temp = array[i];
                for (j = i - 1; j >= 0 && array[j] > temp; j--) {
                    array[j + 1] = array[j];
                }
                array[j + 1] = temp;
            }
        }
    }

    /**
     * 希尔排序
     * 最好情况：O(n^1.3)
     * 最坏情况：O(n^2)
     * 辅助空间：O(1)
     * 稳定性：不稳定
     *
     * @param array 原始数组
     */
    public static void shellSort(int[] array) {
        int i, j, temp;
        int interval = array.length;
        while (interval > 1) {
            interval = (int) Math.pow(interval, 1 / 2);
            for (i = interval; i < array.length; i++) {
                if (array[i] < array[i - interval]) {
                    temp = array[i];
                    for (j = i - interval; j >= 0 && array[j] > temp; j = j - interval) {
                        array[j + interval] = array[j];
                    }
                    array[j + interval] = temp;
                }
            }
        }
    }

    /**
     * 堆排序
     * 注意：虽然数组下标从0开始，但是大/小顶堆的根节点却是从1开始编号
     * 最好情况：O(nlogn)
     * 最坏情况：O(nlogn)
     * 辅助空间：O(1)
     * 稳定性：不稳定
     *
     * @param array 原始数组
     */
    public static void heapSort(int[] array) {
        int length = array.length;
        int temp;
        for (int i = length / 2; i >= 0; i--) {
            heapAdjust(array, i, length - 1);
        }
        for (int i = length - 1; i > 0; i--) {
            temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            heapAdjust(array, 0, i - 1);
        }
    }

    private static void heapAdjust(int[] array, int start, int length) {
        int temp = array[start];
        for (int i = 2 * start; i <= length; i *= 2) {
            if (i < length && array[i] < array[i + 1]) {
                i++;
            }
            if (temp >= array[i]) {
                break;
            }
            array[start] = array[i];
            start = i;
        }
        array[start] = temp;
    }

    /**
     * 归并排序
     * 最好情况：O(nlogn)
     * 最坏情况：O(nlogn)
     * 辅助空间：O(n)
     * 稳定性：稳定
     *
     * @param array 原始数组
     */
    public static void mergeSort(int[] array) {
        if (array == null || array.length <= 0) {
            return;
        }
        int width = 1;
        int[] temp = new int[array.length];
        while (width < array.length) {
            mergePass(array, width, temp);
            width *= 2;
        }
    }

    private static void mergePass(int[] array, int width, int[] temp) {
        int start = 0;
        while (start + 2 * width - 1 < array.length) {
            int mid = start + width - 1;
            int end = start + 2 * width - 1;
            merge(array, start, mid, end, temp);
            start = start + 2 * width;
        }
        //剩余无法构成完整的两组也要进行处理
        if (start + width - 1 < array.length) {
            merge(array, start, start + width - 1, array.length - 1, temp);
        }
    }

    private static void merge(int[] array, int start, int mid, int end, int[] temp) {
        int i = start;
        int j = mid + 1;
        int index = 0;
        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[index++] = array[i++];
            } else {
                temp[index++] = array[j++];
            }
        }
        while (i <= mid) {
            temp[index++] = array[i++];
        }
        while (j <= end) {
            temp[index++] = array[j++];
        }
        if (end + 1 - start >= 0) {
            System.arraycopy(temp, 0, array, start, end + 1 - start);
        }
    }

    /**
     * 快速排序
     * 最好情况：O(nlogn)
     * 最坏情况：O(n^2)
     * 辅助空间：O(nlogn)~O(n)
     * 稳定性：不稳定
     *
     * @param array 原始数组
     */
    public static void quickSort(int[] array) {
        if (array.length > 1) {
            quickSort(array, 0, array.length - 1);
        }
    }

    private static void quickSort(int[] a, int low, int high) {
        //递归算法的出口
        if (low > high) {
            return;
        }
        int i = low;
        int j = high;
        int temp;
        int key = a[low];
        while (i < j) {
            //完成一趟排序
            while (i < j && a[j] > key) {
                //从右往左找到第一个小于key的数
                j--;
            }
            while (i < j && a[i] <= key) {
                //从左往右找到第一个大于key的数
                i++;
            }
            if (i < j) {
                //交换两个数位置
                temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        //调整key的位置
        temp = a[i];
        a[i] = a[low];
        a[low] = temp;
        //key左边的数
        quickSort(a, low, i - 1);
        //key右边的数
        quickSort(a, i + 1, high);
    }
}
