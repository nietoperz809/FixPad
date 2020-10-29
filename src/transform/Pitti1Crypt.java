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
            {'g', 'm', 'j', 's', 'o', 'r', 'v', 'e', 'w', 'f', 'i', 'a', 'p', 'c', 'q', 'u', 't', 'z', 'l', 'b', 'd', 'x', 'y', 'n', 'h', 'k'};
    static final char[] arr2 =
            {'l', 't', 'n', 'u', 'h', 'j', 'a', 'y', 'k', 'c', 'z', 's', 'b', 'x', 'e', 'm', 'o', 'f', 'd', 'q', 'p', 'g', 'i', 'v', 'w', 'r'};
    static final char[] z1 = {'3', '0', '6', '8', '9', '2', '7', '4', '5', '1'};
    static final char[] z2 = {'1', '9', '5', '0', '7', '8', '2', '6', '3', '4'};

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
        char[] exz = mode ? z1 : z2;

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
                    c1 = exchg[c1 - 'a'];
                }
                else if (c1 >= '0' && c1 <= '9')
                {
                    c1 = exz[c1 - '0'];
                }
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
