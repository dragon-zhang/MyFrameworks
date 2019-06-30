package DataStructureAndAlgorithm.sort;

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

    }
}
