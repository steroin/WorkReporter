package pl.workreporter.security.authentication;

/**
 * Created by Sergiusz on 09.08.2017.
 */
public class LoginDataValidator {

    private LoginDataValidationSettings settings;

    public LoginDataValidator(LoginDataValidationSettings settings) {
        this.settings = settings;
    }

    public boolean validateLogin(String login) {
        if (login == null) {
            return false;
        }
        if (login.length() < settings.getMinLoginLength() || login.length() > settings.getMaxLoginLength()) {
            return false;
        }
        return login.matches("[a-zA-Z0-9.$_-]+");
    }

    public boolean validatePassword(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < settings.getMinPasswordLength() || password.length() > settings.getMaxPasswordLength()) {
            return false;
        }
        return password.matches("[a-zA-Z0-9.!@#$%^&*_-]+");
    }

    public boolean isEmail(String login) {
        return login != null && login.matches("[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*");
    }
}
