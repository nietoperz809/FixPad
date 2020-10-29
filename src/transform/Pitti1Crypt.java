package transform;

import common.Tools;

import java.util.ArrayList;

import static common.Tools.fixedSplit;

/**
 * Created by Administrator on 4/26/2016.
 */
public class Pitti1Crypt implements Transformation
{
    static final char[] arr1 =
            {
                    'g', 'm', 'j', 's', 'o', 'r', 'v', 'e', 'w', 'f', 'i', 'a', 'p', 'c', 'q', 'u', 't', 'z', 'l', 'b', 'd', 'x', 'y', 'n', 'h', 'k'
            };
    static final char[] arr2 =
            {
                    'l', 't', 'n', 'u', 'h', 'j', 'a', 'y', 'k', 'c', 'z', 's', 'b', 'x', 'e', 'm', 'o', 'f', 'd', 'q', 'p', 'g', 'i', 'v', 'w', 'r'
            };
    @Override
    public String transform (String in)
    {
        return substituteText(in, true);
    }

    @Override
    public String retransform (String in)
    {
        return substituteText(in, false);
    }

    //////////////////////////////////////////////////////////////
    
    private static String substituteWord (String in, boolean mode)
    {
        char[] exchg = mode ? arr1 : arr2;

        boolean[] cases = Tools.getCases(in);
        in = in.toLowerCase();
        StringBuilder out = new StringBuilder();

        for (int s = 0; s < in.length(); s++)
        {
            char c1 = in.charAt(s);
            for (int n=0; n<(s+1); n++)
            {
                int idx = c1-'a';
                if (idx >= 0 && idx <= exchg.length)
                    c1 = exchg[idx];
            }
            out.append(c1);
        }

        return Tools.adjustCases(out.toString(), cases);
    }

    private static String substituteWord(String in, boolean mode, int times)
    {
        times = times % 19;
        while (times-- != 0)
        {
            in = substituteWord(in, mode);
        }
        return in;
    }

    public static String substituteText(String in, boolean mode)
    {
        StringBuilder out = new StringBuilder();
        ArrayList<String> splitted = fixedSplit(in, 12);

        for (int s = 0; s < splitted.size(); s++)
        {
            out.append (substituteWord(splitted.get(s), mode, s + 1));
        }

        return out.toString();
    }


//    public static String substituteText(String in, boolean mode)
//    {
//        StringBuilder out = new StringBuilder();
//        String[] splitted = in.split(" ");
//
//        for (int s = 0; s < splitted.length; s++)
//        {
//            out.append(substituteWord(splitted[s], mode, s + 1)).append(" ");
//        }
//
//        return out.toString();
//    }

    public static void main(String... args)
    {
//        Integer a[] = new Integer[19];
//        for (int s = 0; s < 19; s++)
//        {
//            a[s] = s;
//        }
//
//        List<Integer> l = Arrays.asList(a);
//        Collections.shuffle(l);
//
//        System.out.print("int exchg[] = {");
//        for (Integer i : l)
//        {
//            System.out.print("'" + i + "', ");
//        }
//        System.out.println("};");

        String s = substituteText("abc abc abc abc abc abc", true);
        System.out.println(s);

        String t = substituteText(s, false);
        System.out.println(t);
    }

}
