package IHM.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 6 to 20 characters string with at least one digit, one upper case letter, one lower case letter
 */
/**
 * The Class PasswordValidator.
 */
public class PasswordValidator extends SimpleStringValidator {

    /** The pattern. */
    private Pattern pattern;

    /** The matcher. */
    private Matcher matcher;

    /** The Constant MESSAGE. */
    public static final String MESSAGE = "Mot de passe incorrect: 6 et 20 caractères avec ua moins 1 chiffre, 1 majuscule, 1 minuscule et 1 caractère spécial.";


    /** The Constant PASSWORD_PATTERN. */
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    /**
     * Instantiates a new password validator.
     */
    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression.
     *
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
