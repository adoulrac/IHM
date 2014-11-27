package IHM.validators;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter
 */
public class PasswordValidator extends SimpleStringValidator {

    private Pattern pattern;

    private Matcher matcher;

    public static final String MESSAGE = "Invalid Password: 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter.";


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
    @Override
    public boolean validate(final String password){
        if(!super.validate(password)) {
            return false;
        }
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
