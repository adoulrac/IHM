package IHM.helpers;

import IHM.utils.StringUtil;
import IHM.validators.IPAddressValidator;
import IHM.validators.LoginValidator;
import IHM.validators.PasswordValidator;
import IHM.validators.SimpleStringValidator;
import com.google.common.base.Strings;

/**
 * The Class ValidatorHelper.
 */
public class ValidatorHelper {

    /**
     * Instantiates a new validator helper.
     */
    public ValidatorHelper() {
        super();
    }

    /**
     * Validate password.
     *
     * @param password the password
     * @return true, if successful
     */
    public static boolean validatePassword(final String password) {
        return new PasswordValidator().validate(password);
    }

    /**
     * Validate i ps.
     *
     * @param ipsText the ips text
     * @return true, if successful
     */
    public static boolean validateIPs(final String ipsText) {
        if (Strings.isNullOrEmpty(ipsText)) {
            return true;
        }
        String[] lines = ipsText.split(StringUtil.SYSTEM_SEPARATOR);
        boolean valid = true;

        for (String line : lines) {
            valid = new IPAddressValidator().validate(line);
        }

        return valid;
    }

    /**
     * Validate login.
     *
     * @param login the login
     * @return true, if successful
     */
    public static boolean validateLogin(final String login) {
        return new LoginValidator().validate(login);
    }

    /**
     * Validate string.
     *
     * @param string the string
     * @return true, if successful
     */
    public static boolean validateString(final String string) {
        return new SimpleStringValidator().validate(string);
    }
}
