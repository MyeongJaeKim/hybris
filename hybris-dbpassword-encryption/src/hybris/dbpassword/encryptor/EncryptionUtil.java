package hybris.dbpassword.encryptor;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class : EncryptionUtil
 * 
 * Utility class for encryption and decryption.
 *  
 * @author Architect
 *
 */
public class EncryptionUtil
{
    private static final String AES = "AES";
    private static final String SECRET_KEY_DEFAULT = "HybrisAESEncryptionUtil";
    
    private String secretKey;
    
    private Key generatedKey;
    
    public EncryptionUtil() throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
	this.secretKey = SECRET_KEY_DEFAULT;
	generatedKey = generateKey();
    }
    
    private Key generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        byte[] key = getSecretKey().getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(256);

        return new SecretKeySpec(key, AES);
    }
    
    public String encrypt(String plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
    {
	Cipher cipher = Cipher.getInstance(AES);
	cipher.init(Cipher.ENCRYPT_MODE, getGeneratedKey());
	
	byte[] encrypted = cipher.doFinal(plaintext.getBytes());
	
	return new String(encrypted);
    }
    
    public String decrypt(String ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
	Cipher cipher = Cipher.getInstance(AES);
	cipher.init(Cipher.DECRYPT_MODE, getGeneratedKey());
	
	byte[] decrypted = cipher.doFinal(ciphertext.getBytes());
	
	return new String(decrypted);
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public Key getGeneratedKey()
    {
        return generatedKey;
    }

    public void setGeneratedKey(Key generatedKey)
    {
        this.generatedKey = generatedKey;
    }
    
    public static void main(String[] args) throws Exception
    {
	if (args.length != 2)
	{
        	System.out.println("Encryption : java -jar hybris-dbpassword-encryption.jar enc plaintext");
        	System.out.println("Decryption : java -jar hybris-dbpassword-encryption.jar dec ciphertext");
        	System.out.println("Secret key <key> is optional.");
        	System.exit(-1);
	}
	else
	{
	    String method = args[0];
	    String text = args[1];
	    
	    EncryptionUtil encryptionUtil = new EncryptionUtil();
	    
	    switch(method)
	    {
	    case "enc":
		System.out.println("Encrypted --> " + encryptionUtil.encrypt(text));
		break;
	    case "dec":
	    	System.out.println("Decrypted --> " + encryptionUtil.decrypt(text));
		break;
	    default:
		System.out.println("Choose enc or dec.");
	    }
	}
    }
}
