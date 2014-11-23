package IHM.validator;


import com.google.common.base.Strings;

public class LoginValidator {

    public LoginValidator() {
        super();
    }

    public boolean validate(final String login){
        return !Strings.isNullOrEmpty(login);
    }
}
