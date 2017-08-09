package pl.workreporter.security.login;

/**
 * Created by Sergiusz on 09.08.2017.
 */
public class LoginDataValidationSettings {
    private int minLoginLength;
    private int maxLoginLength;
    private int minPasswordLength;
    private int maxPasswordLength;

    public LoginDataValidationSettings(int minLoginLength, int maxLoginLength, int minPasswordLength, int maxPasswordLength) {
        this.minLoginLength = minLoginLength;
        this.maxLoginLength = maxLoginLength;
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
    }

    public int getMinLoginLength() {
        return minLoginLength;
    }

    public int getMaxLoginLength() {
        return maxLoginLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public int getMaxPasswordLength() {
        return maxPasswordLength;
    }
}
