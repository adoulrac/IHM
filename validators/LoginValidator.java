package IHM.validators;


import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 3 to 15 characters with any lower case character, digit or special symbol “_-” only
 */
public class LoginValidator extends SimpleStringValidator {

    private Pattern pattern;

    private Matcher matcher;

    public static final String MESSAGE = "Invalid Login: 3 to 15 characters with any lower case character, digit or special symbol only.";

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

    public LoginValidator() {
        pattern = Pattern.compile(USERNAME_PATTERN);
    }

    /**
     * Validate username with regular expression
     * @param login login for validation
     * @return true valid username, false invalid username
     */
    public boolean validate(final String login){
        if(!super.validate(login)) {
            return false;
        }
        matcher = pattern.matcher(login);
        return matcher.matches();
    }
}
