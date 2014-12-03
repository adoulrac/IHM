package IHM.validators;


import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class SimpleStringValidator.
 */
public class SimpleStringValidator {

    /** The Constant MESSAGE. */
    public static final String MESSAGE = "Les champs doivent être remplis.";

    /** The pattern. */
    private Pattern pattern;

    /** The matcher. */
    private Matcher matcher;

    /**
     * Instantiates a new simple string validator.
     */
    public SimpleStringValidator() {
        super();
    }

    /**
     * Validate.
     *
     * @param login the login
     * @return true, if successful
     */
    public boolean validate(final String login){
        return !Strings.isNullOrEmpty(login);
    }
}
