package app.interfaces;

import java.util.List;

/**
 * Created by greyna on 11/12/14.
 */
public interface DataToIhm {
    void receiveUser(User user, int queryId);
    void receiveUsers(List<User> users, int queryId);
    void receivePicture(Picture picture, int queryId);
    void receivePictures(List<Picture> pictures, int queryId);
    void receiveComment(Comment comment, int queryId);
    void receiveNote(Note note, int queryId);
    void receiveFriendRequest(User user, int queryId);
}
