package pl.workreporter.web.beans.entities.userdata;

/**
 * Created by Sergiusz on 23.08.2017.
 */
public interface UserDataDao {
    UserData getUserData(long userId);
    void updateUserData(UserData userData);
}
