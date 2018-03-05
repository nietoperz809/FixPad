/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transform;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class GrayCode implements Transformation
{
    private byte grayByte(byte in)
    {
        return (byte) (in ^ (in & 0xff) >>> 1);
    }
    
    private byte ungrayByte(byte in)
    {
        int r = 8;
        while (--r != 0)
        {
            in ^= (in & 0xff) >>> 1;
        }
        return in;
    }

    private byte[] grayByteArray(byte[] in)
    {
        int s;
        byte[] out = new byte[in.length];
        for (s = 0; s < in.length; s++)
        {
            out[s] = grayByte(in[s]);
        }
        return out;
    }
    
    private byte[] ungrayByteArray(byte[] in)
    {
        int s;
        byte[] out = new byte[in.length];
        for (s = 0; s < in.length; s++)
        {
            out[s] = ungrayByte(in[s]);
        }
        return out;
    }
    
    @Override
    public String transform(String in)
    {
        try
        {
            byte[] b = in.getBytes(utf8);
            byte[] b2 = grayByteArray(b);
            return new String (b2, utf8);
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(GrayCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String retransform(String in)
    {
        try
        {
            byte[] b = in.getBytes(utf8);
            byte[] b2 = ungrayByteArray(b);
            return new String (b2, utf8);
        }
        catch (UnsupportedEncodingException ex)
        {
            return ex.toString();
        }
    }
    
}
