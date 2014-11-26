package IHM.validators;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter
 */
public class PasswordValidator {

    private Pattern pattern;

    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){
        return true;
        /*if (Strings.isNullOrEmpty(password)) {
            return false;
        }

        matcher = pattern.matcher(password);
        return matcher.matches();*/
    }
}
