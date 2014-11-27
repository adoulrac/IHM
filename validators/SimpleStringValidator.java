package IHM.validators;


import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleStringValidator {

    public static final String MESSAGE = "The input cannot be empty.";

    private Pattern pattern;

    private Matcher matcher;

    public SimpleStringValidator() {
        super();
    }

    public boolean validate(final String login){
        return !Strings.isNullOrEmpty(login);
    }
}
