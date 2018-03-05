package transform;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Administrator
 */
public class Base64 implements Transformation
{
    @Override
    public String transform(String in)
    {
        try
        {
            return java.util.Base64.getEncoder().encodeToString(in.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            return e.toString();
        }
    }

    @Override
    public String retransform(String in)
    {
        byte[] barr = java.util.Base64.getDecoder().decode(in);
        try
        {
            return new String (barr, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return e.toString();
        }
    }
}
