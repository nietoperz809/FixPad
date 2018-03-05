/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transform;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SHA256 extends SHA1
{
    public SHA256 ()
    {
        try        
        {
            crypt = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
