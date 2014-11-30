package IHM.validators;


import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoteValidator {

    public static final String MESSAGE = "Le vote doit être un chiffre entre 1 et 5";

    public static boolean validate(final String vote){
        if (Strings.isNullOrEmpty(vote))
            return false;

        boolean result = false;

        try {
            int value = Integer.parseInt(vote);
            if (value >= 1 && value <= 5)
                result = true;
        } catch (NumberFormatException e) {
            result = false;
        }

        return result;
    }
}
