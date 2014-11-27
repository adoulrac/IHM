package IHM.helpers;

import IHM.validators.IPAddressValidator;
import IHM.validators.LoginValidator;
import IHM.validators.PasswordValidator;
import IHM.validators.SimpleStringValidator;
import com.google.common.base.Strings;

public class ValidatorHelper {

    public ValidatorHelper() {
        super();
    }

    public static boolean validatePassword(final String password) {
        return new PasswordValidator().validate(password);
    }

    public static boolean validateIPs(final String ipsText) {
        if(Strings.isNullOrEmpty(ipsText)) {
            return true;
        }
        String[] lines = ipsText.split(System.getProperty("line.separator"));
        boolean valid = true;

        for(String line : lines) {
            return new IPAddressValidator().validate(line);
        }

        return valid;
    }

    public static boolean validateLogin(final String login) {
        return new LoginValidator().validate(login);
    }

    public static boolean validateString(final String string) {
        return new SimpleStringValidator().validate(string);
    }
}
