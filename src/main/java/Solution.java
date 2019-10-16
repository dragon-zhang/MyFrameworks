import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author SuccessZhang
 * <p>
 * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。
 * 例如输入字符串abc,则打印出由字符a,b,c所能排列出来的所有
 * 字符串abc,acb,bac,bca,cab和cba。
 */
public class Solution {

    /**
     * 字典排序用treeSet
     */
    static Set<String> result = new TreeSet<>();

    public static ArrayList<String> Permutation(String str) {
        char[] characters = str.toCharArray();
        if (characters.length == 0) {
            return new ArrayList<>();
        }
        char temp = characters[0];
        for (int i = 0; i < characters.length; i++) {
            StringBuilder sb = new StringBuilder();
            characters[0] = characters[i];
            characters[i] = temp;
            sb.append(characters[0]);
            StringBuilder stringBuilder = new StringBuilder(str);
            stringBuilder.deleteCharAt(i);
            String string = stringBuilder.toString();
            if ("".equals(string)) {
                return new ArrayList<>(Collections.singletonList(str));
            }
            Permutation(string, sb);
        }
        return new ArrayList<>(result);
    }

    public static void Permutation(String str, StringBuilder sb) {
        char[] chars = str.toCharArray();
        if (chars.length > 0) {
            char temp = chars[0];
            String strTemp = sb.toString();
            for (int i = 0; i < chars.length; i++) {
                sb = new StringBuilder(strTemp);
                chars[0] = chars[i];
                chars[i] = temp;
                sb.append(chars[0]);
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.deleteCharAt(i);
                Permutation(stringBuilder.toString(), sb);
            }
        } else {
            result.add(sb.toString());
        }
    }

    public static void main(String[] args) {
        System.out.println(Permutation("aab"));
    }
}