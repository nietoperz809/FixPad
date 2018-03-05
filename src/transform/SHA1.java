/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transform;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SHA1 implements Transformation
{
    protected MessageDigest crypt = null;
    
    public SHA1 ()
    {
        try        
        {
            crypt = MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String transform(String in)
    {
        crypt.reset();
        try
        {
            crypt.update(in.getBytes(utf8));
            return new BigInteger(1, crypt.digest()).toString(16);
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String retransform(String in)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
