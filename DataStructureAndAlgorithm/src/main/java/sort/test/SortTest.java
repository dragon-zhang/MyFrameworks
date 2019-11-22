package sort.test;

import sort.SortUtil;

import java.util.Arrays;

/**
 * @author SuccessZhang
 */
public class SortTest {
    public static void main(String[] args) {
        int[] array = new int[]{5, 2, 6, 0, 3, 9, 1, 7, 4, 8, 15, 14, 13, 12, 11, 10};
        int[] arrayCopy1 = array.clone();
        int[] arrayCopy2 = array.clone();
        int[] arrayCopy3 = array.clone();
        int[] arrayCopy4 = array.clone();
        SortUtil.insertSort(array);
        System.out.println("insertSort:" + Arrays.toString(array));
        SortUtil.shellSort(arrayCopy1);
        System.out.println("shellSort:" + Arrays.toString(arrayCopy1));
        SortUtil.heapSort(arrayCopy2);
        System.out.println("heapSort:" + Arrays.toString(arrayCopy2));
        SortUtil.mergeSort(arrayCopy3);
        System.out.println("mergeSort:" + Arrays.toString(arrayCopy3));
        SortUtil.quickSort(arrayCopy4);
        System.out.println("quickSort:" + Arrays.toString(arrayCopy4));
    }
}
