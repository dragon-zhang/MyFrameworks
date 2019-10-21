package DataStructureAndAlgorithm.bit;

/**
 * @author ojshilu
 * @link https://blog.csdn.net/ojshilu/article/details/11179911
 * @date 2019/10/21
 */
public class BitOperation {

    /**
     * 取一个数的符号，看是正还是负
     */
    public int getSign(int i) {
        return (i >> 31);
    }

    /**
     * 求补码
     */
    public int negative(int i) {
        return add(~i, 1);
    }

    /**
     * 将一个数变为正数，如果本来就是正，则不变；
     * 如果是负，则变为相反数。
     * 对于-2147483648，求负会溢出
     */
    public int bePositive(int i) {
        if ((i >> 31) < 0) {
            return negative(i);
        }
        return i;
    }

    public int add(int num1, int num2) {
        while (num2 != 0) {
            //两个数异或：相当于每一位相加，而不考虑进位
            int sum = num1 ^ num2;
            //两个数相与，并左移一位：相当于求得进位
            num2 = (num1 & num2) << 1;
            num1 = sum;
        }
        return num1;
    }

    public int reduce(int num1, int num2) {
        //求减数的补码（取反加一），再相加
        return add(num1, negative(num2));
    }

    public int multiply(int num1, int num2) {
        boolean flag = true;
        //积的符号判定
        if (getSign(num1) == getSign(num2)) {
            flag = false;
        }
        num1 = bePositive(num1);
        num2 = bePositive(num2);
        int ans = 0;
        while (num2 != 0) {
            if ((num2 & 1) == 1) {
                ans = add(ans, num1);
            }
            //把a错位加在积上
            num1 = (num1 << 1);
            //从最低位开始依次判断b的每一位
            num2 = (num2 >> 1);
        }
        if (flag) {
            ans = negative(ans);
        }
        return ans;
    }

    public int divide(int num1, int num2) {
        boolean flag = true;
        //积的符号判定
        if (getSign(num1) == getSign(num2)) {
            flag = false;
        }

        int x = bePositive(num1);
        int y = bePositive(num2);

        int ans = 0;
        int i = 31;
        while (i >= 0) {
            //比较x是否大于y*(1<<i)=(y<<i)，避免直接比较，因为不确定y*(1<<i)是否溢出
            if ((x >> i) >= y) {
                //如果够减
                ans = add(ans, (1 << i));
                x = reduce(x, (y << i));
            }
            i = reduce(i, 1);
        }
        if (flag) {
            ans = negative(ans);
        }
        return ans;
    }

    public static void main(String[] args) {
        BitOperation solution = new BitOperation();
        System.out.println(solution.add(10, 5));
        System.out.println(solution.reduce(10, 5));
        System.out.println(solution.multiply(10, 5));
        System.out.println(solution.divide(10, 5));
    }
}
