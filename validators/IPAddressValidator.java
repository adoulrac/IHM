package IHM.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class IPAddressValidator.
 */
public class IPAddressValidator{

    /** The pattern. */
    private Pattern pattern;
    
    /** The matcher. */
    private Matcher matcher;

    /** The Constant IP_ADDRESS_PATTERN. */
    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Instantiates a new IP address validator.
     */
    public IPAddressValidator(){
        pattern = Pattern.compile(IP_ADDRESS_PATTERN);
    }

    /**
     * Validate ip address with regular expression.
     *
     * @param ip ip address for validation
     * @return true valid ip address, false invalid ip address
     */
    public boolean validate(final String ip){
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}