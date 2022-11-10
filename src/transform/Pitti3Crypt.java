package transform;

import common.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static common.Tools.fixedSplit;

public class Pitti3Crypt implements Transformation
{
    @Override
    public String transform(String in)
    {
        return substituteText(in, true);
    }

    @Override
    public String retransform(String in)
    {
        return substituteText(in, false);
    }

    static class ATables
    {
        private final int[] ints;     // Original
        private final Integer[] arr1; // shuffled
        private final Integer[] arr2; // reversed shuffled

        public ATables (long key, int len)
        {
            // Build arrays
            ints = new int[len];
            arr2 = new Integer[len];
            for (int s=0; s<len; s++) ints[s] = s;
            arr1 = Arrays.stream(ints).boxed().toArray(Integer[]::new);
            Collections.shuffle(Arrays.asList(arr1), new Random(key));
            for (int s=0; s<len; s++) arr2[arr1[s]] = s;
        }
    }

    long hash;
    final ATables charTable;
    final ATables numTable;

    public Pitti3Crypt(String passwd)
    {
        hash = passwd.hashCode();
        hash <<= 32;
        hash |= new StringBuffer(passwd).reverse().toString().hashCode();

        charTable = new ATables(hash, 26);
        numTable = new ATables(hash, 10);
    }

    private String substituteWord (String in, boolean mode)
    {
        Integer[] exchg = mode ? charTable.arr1 : charTable.arr2;
        Integer[] numxg = mode ? numTable.arr1 : numTable.arr2;

        boolean[] cases = Tools.getCases(in);
        in = in.toLowerCase();
        StringBuilder out = new StringBuilder();

        for (int s = 0; s < in.length(); s++)
        {
            char c1 = in.charAt(s);
            for (int n=0; n<(s+1); n++)
            {
                if (c1 >= 'a' && c1 <= 'z')
                {
                    c1 = (char)(exchg[c1 - 'a'] + 'a');
                }
                else if (c1 >= '0' && c1 <= '9')
                {
                    c1 = (char)(numxg[c1 - '0'] + '0');
                }
            }
            out.append(c1);
        }
        return Tools.adjustCases(out.toString(), cases);
    }

    public String substituteText(String in, boolean mode)
    {
        StringBuilder out = new StringBuilder();
        ArrayList<String> splitted = fixedSplit(in, 12);

        for (String value : splitted) {
            out.append(substituteWord(value, mode));
        }

        return out.toString();
    }


    // Test
    public static void main(String[] args)
    {
        Pitti3Crypt p3 = new Pitti3Crypt("fickenderfick");
        System.out.println(p3.hash);
        System.out.println(Arrays.toString(p3.charTable.ints));
        System.out.println(Arrays.toString(p3.charTable.arr1));
        System.out.println(Arrays.toString(p3.charTable.arr2));

        String s1 = "Peter ist liebb und Süss und Geil 0815 1234567890";
        String s2 = p3.substituteText(s1, true);
        String s3 = p3.substituteText(s2, false);
        System.out.println(s2);
        System.out.println(s3);

    }

}
