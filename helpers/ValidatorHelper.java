package IHM.helpers;

import IHM.validators.IPAddressValidator;
import IHM.validators.LoginValidator;
import IHM.validators.PasswordValidator;

public class ValidatorHelper {

    public ValidatorHelper() {
        super();
    }

    public static boolean validatePassword(final String password) {
        return true;
        //TODO return new PasswordValidator().validate(password);
    }

    public static boolean validateIPs(final String ipsText) {
        String[] lines = ipsText.split(System.getProperty("line.separator"));
        boolean valid = true;

        for(String line : lines) {
            return new IPAddressValidator().validate(line);
        }

        return valid;
    }

    public static boolean validateLogin(final String login) {
        return true;
        //TODO return new LoginValidator().validate(login);
    }
}
