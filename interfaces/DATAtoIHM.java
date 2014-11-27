package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Interface DATA uses to call IHM with asynchronous results.
 * queryId the id of the query if the result was previously requested by IHM, -1 if not.
 */
public interface DATAtoIHM
{
    /**
     * Receive a notification of connection.
     * @param user the user connected.
     */
    void receiveConnectedUser(User user);

    /**
     * Receive a notification of unconnection.
     * @param user the user unconnected.
     */
    void receiveUnconnectedUser(User user);

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
    void receiveFriendRequest(User user, Integer queryId);
}
