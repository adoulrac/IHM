package IHM.validators;


import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 3 to 15 characters with any lower case character, digit or special symbol â€œ_-â€? only
 */
/**
 * The Class LoginValidator.
 */
public class LoginValidator extends SimpleStringValidator {

    /** The pattern. */
    private Pattern pattern;

    /** The matcher. */
    private Matcher matcher;

    /** The Constant MESSAGE. */
    public static final String MESSAGE = "Invalid Login: 3 to 15 characters with any lower case character, digit or special symbol only.";

    /** The Constant USERNAME_PATTERN. */
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

    /**
     * Instantiates a new login validator.
     */
    public LoginValidator() {
        pattern = Pattern.compile(USERNAME_PATTERN);
    }

    /**
     * Validate username with regular expression.
     *
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
