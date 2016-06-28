package hybris.dbpassword.decryptor;

import hybris.dbpassword.encryptor.EncryptionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Class : DatabasePasswordGetterAspect 
 * 
 * After Tomcat bootstrapping is over, hybris context loader will be trying to make initial connection to database to startup up its tenant.
 * The class de.hybris.platform.util.config.ConfigIntf is used to read database password in local.properties file.
 * 
 * This class is aspect, and will point-cut the method getParameter() of ConfigIntf.
 * Encrypted password will be decrypted by this aspect, and return the plaintext to hybris context loader.
 * 
 * This class should be applied Tomcat layer, because any of components in application will not be loaded until hybris database check is over.
 * 
 * @author MJ Kim (fahrenblue@gmail.com)
 *
 */
@Aspect
public class DatabasePasswordGetterAspect
{
    private static final String DB_PASSWORD = "db.password";
    
    private EncryptionUtil encryptionUtil;
    
    public DatabasePasswordGetterAspect()
    {
        try
        {
            encryptionUtil = new EncryptionUtil();
        }
        catch (final Exception e)
        {
            // Do nothing
        }
    } 
    
    @Around("execution(* de.hybris.platform.util.config.ConfigIntf.getParameter(..))")
    public Object decryptDatabasePassword(ProceedingJoinPoint pjp)
    {
        
        Object[] args = pjp.getArgs();
        
        Object ret = null;
        
        try {
            ret = pjp.proceed();

            // When argument is "db.password"
            if ( args.length > 0 && DB_PASSWORD.equals((String) args[0]) &&  getEncryptionUtil() != null )
            {
                if (ret instanceof String) {
                    String decrypted = getEncryptionUtil().decrypt((String) ret);
                    return decrypted;
                }
            }
        }
        catch (final Throwable e)
        {
            // Do nothing
        }
        return ret;
    }

    public EncryptionUtil getEncryptionUtil() {
        return encryptionUtil;
    }

    public void setEncryptionUtil(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }
}
