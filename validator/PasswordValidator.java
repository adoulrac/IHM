package IHM.validator;

import com.google.common.base.Strings;

public class PasswordValidator {

    public PasswordValidator() {
        super();
    }

    public boolean validate(final String password){
        return !Strings.isNullOrEmpty(password);
    }
}
