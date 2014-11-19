package IHM.interfaces;

import java.util.List;
import java.util.UUID;

import DATA.model.*;

/**
 * Interface DATA uses to call IHM with asynchronous results.
 * queryId the id of the query if the result was previously requested by IHM, -1 if not.
 */
public interface DATAtoIHM
{
    /**
     * Send back the user requested.
     * @param user the user requested.
     */
    void receiveUser(User user, int queryId);

    /**
     * Receive a notification of connection.
     * @param userId the id of the user connected.
     * @param login the name of the user.
     */
    void receiveConnectedUser(UUID userId, String login, int queryId);

    /**
     * Receive a notification of unconnection.
     * @param userId the id of the user unconnected.
     * @param login the name of the user.
     */
    void receiveUnconnectedUser(UUID userId, String login, int queryId);

    /**
     * Send back the picture requested.
     * @param picture the picture requested.
     */
    void receivePicture(Picture picture, int queryId);

    /**
     * Send back the list of pictures requested.
     * @param pictures the list of pictures requested.
     */
    void receivePictures(List<Picture> pictures, int queryId);

    /**
     * Receive a friend request by another user
     * @param user the other user who sent the request.
     */
    void receiveFriendRequest(User user, int queryId);

    /**
     * Receive a notification of change in a picture (comment, note, .
     * @param userId the id of the user connected.
     * @param filename the name of the picture.
     */
    void refreshPicture(String filename, int userId, int queryId);


    // deleted
    /**
     * Send back the list of users requested.
     * @param users the list of users requested.
     */
    //void receiveUsers(List<User> users, int queryId);
    /**
     * Receive a comment, or a comment validation if posted by this user on another user's picture.
     * @param comment the comment received or sent.
     */
    //void receiveComment(Comment comment, int queryId);
    /**
     * Receive a note, or a note validation if posted by this user on another user's picture.
     * @param note the note received or sent.
     */
    //void receiveNote(Note note, int queryId);
}
