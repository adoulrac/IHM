package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;

import java.util.List;

/**
 * Interface DATA uses to call IHM with asynchronous results.
 * queryId the id of the query if the result was previously requested by IHM, -1 if not.
 */
public interface DATAtoIHM {
    /**
     * Receive a notification of connection.
     *
     * @param user the user connected.
     */
    public void receiveConnectedUser(User user);

    /**
     * Receive a notification of unconnection.
     *
     * @param user the user unconnected.
     */
    public void receiveUnconnectedUser(User user);

    /**
     * Send back the picture requested.
     *
     * @param picture the picture requested.
     * @param queryId the query id
     */
    public void receivePicture(Picture picture, int queryId);

    /**
     * Send back the list of pictures requested.
     *
     * @param pictures the list of pictures requested.
     * @param queryId  the query id
     */
    public void receivePictures(List<Picture> pictures, int queryId);

    /**
     * Receive a friend request by another user.
     *
     * @param user the other user who sent the request.
     */
    public void receiveFriendRequest(User user);

    /**
     * Receive a friend response by another user.
     *
     * @param user     the other user who sent the request.
     * @param response the response
     */
    public void receiveFriendResponse(User user, boolean response);

    /**
     * Receive reload all user groups
     */
    public void receiveReloadUserGroups();
}
