/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transform;

/**
 *
 * @author Administrator
 */
public class Rot13 implements Transformation
{
    private static final String a1 = "abcdefghijklm";
    private static final String a2 = "nopqrstuvwxyz";

    private static char change (char c)
    {
        for (int s=0; s<a1.length(); s++)
        {
            if (a1.charAt(s) == c)
                return a2.charAt(s);
            if (a2.charAt(s) == c)
                return a1.charAt(s);
        }
        return c;
    }

    @Override
    public String transform(String in)
    {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < in.length(); n++)
        {
            char c = in.charAt(n);
            boolean upper = Character.isUpperCase(c);
            if (upper)
            {
                c = Character.toLowerCase(c);
            }
            c = change (c);
            if (upper)
            {
                c = Character.toUpperCase(c);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public String retransform(String in)
    {
        return transform (in);
    }
}
