package pl.workreporter.web.service.password.changer;

/**
 * Created by Sergiusz on 23.08.2017.
 */
public enum PasswordChangeResult {
    SUCCESS(0),
    REPEAT_DOESNT_MATCH(1),
    WRONG_EXISTING_PASSWORD(2),
    INVALID_NEW_PASSWORD(3),
    UNKNOWN_ERROR(4);

    private final int resultCode;
    PasswordChangeResult(int code) {
        resultCode = code;
    }

    public int getResultCode() {
        return resultCode;
    }
}
