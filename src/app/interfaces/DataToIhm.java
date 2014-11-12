package app.interfaces;

import java.util.List;

/**
 * Interface called by the DATA layer to send back asynchronous results to the IHM layer,
 * OR requests/results from other clients.
 * @author Arthur Van Ceulen
 * @param queryId is sent by IHM to DATA when requesting. If equal to 0, means that the result was not previously requested by the IHM layer (like a friend request).
 */
public interface DataToIhm {
    /**
     * Send back the user requested.
     * @param user the user requested.
     */
    //void receiveUser(User user, int queryId);

    /**
     * Send back the list of users requested.
     * @param users the list of users requested.
     */
    //void receiveUsers(List<User> users, int queryId);

    /**
     * Send back the picture requested.
     * @param picture the picture requested.
     */
    //void receivePicture(Picture picture, int queryId);

    /**
     * Send back the list of pictures requested.
     * @param pictures the list of pictures requested.
     */
    //void receivePictures(List<Picture> pictures, int queryId);

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

    /**
     * Receive a friend request by another user
     * @param user the other user who sent the request.
     */
    //void receiveFriendRequest(User user, int queryId);
}
