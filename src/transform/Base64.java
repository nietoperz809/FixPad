package transform;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Administrator
 */
public class Base64 implements Transformation
{
    @Override
    public String transform(String in)
    {
        return java.util.Base64.getEncoder().encodeToString(in.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String retransform(String in)
    {
        byte[] barr = java.util.Base64.getDecoder().decode(in);
        return new String (barr, StandardCharsets.UTF_8);
    }
}
