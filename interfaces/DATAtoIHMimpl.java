package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;
import IHM.controllers.*;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by greyna on 19/11/2014.
 */
public class DATAtoIHMimpl implements DATAtoIHM {

    /**
     * The app.
     */
    private MainController app;

    /**
     * The friend requests.
     */
    private List<User> friendRequests = Lists.newArrayList();
    /**
     * The connected users.
     */
    private List<UUID> connectedUsers = new ArrayList<UUID>();

    /**
     * Instantiates a new DAT ato ih mimpl.
     *
     * @param app the app
     */
    public DATAtoIHMimpl(MainController app) {
        this.app = app;
    }

    /**
     * Empty pending friend requests.
     *
     * @return the list
     */
    public List<User> emptyPendingFriendRequests() {
        List<User> result = new ArrayList<User>(friendRequests);
        friendRequests = new ArrayList<User>();
        return result;
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receiveConnectedUser(DATA.model.User)
     */
    @Override
    public void receiveConnectedUser(User user) {
        if (app.getWelcomeController() != null)
            app.getWelcomeController().getFriendsSubController().connectUser(user);
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receiveUnconnectedUser(DATA.model.User)
     */
    @Override
    public void receiveUnconnectedUser(User user) {
        if (app.getWelcomeController() != null)
            app.getWelcomeController().getFriendsSubController().disconnectUser(user);
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receivePicture(DATA.model.Picture, int)
     */
    @Override
    public void receivePicture(Picture picture, int queryId) {
        Initializable controller = app.getRequests().get(new Integer(queryId));
        if (controller == null) {
            //no entry or it's deleted in the map
            // do nothing (we don't know for what these pictures are for)
        } else {
            if (controller instanceof TabbedPicturesSubController && ((TabbedPicturesSubController) controller).isPendingRequest(queryId)) {
                ((TabbedPicturesSubController) controller).addPicture(picture);
                app.removeRequest(queryId);
            } else if (controller instanceof PictureController) {
                ((PictureController) controller).receiveFullImage(picture);
                app.removeRequest(queryId);
            }
        }
    }

    @Override
    public void receiveReloadUserGroups() {
        WelcomeController welcome = app.getWelcomeController();
        if (welcome != null) {
            welcome.getFriendsSubController().reloadUserGroups();
        }
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receivePictures(java.util.List, int)
     */
    @Override
    public void receivePictures(List<Picture> pictures, int queryId) {
        Initializable controller = app.getRequests().get(new Integer(queryId));
        if (controller == null) {
            //no entry or it's deleted in the map
            // do nothing (we don't know for what these pictures are for)
        } else {
            if (controller instanceof TabbedPicturesSubController) {
                ((TabbedPicturesSubController) controller).addPictures(pictures);
            }
        }
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receiveFriendRequest(DATA.model.User)
     */
    @Override
    public void receiveFriendRequest(User user) {
        WelcomeController welcome = app.getWelcomeController();
        if (welcome != null) {
            welcome.getFriendsSubController().receiveFriendRequest(user);
        }
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receiveFriendResponse(DATA.model.User, boolean)
     */
    @Override
    public void receiveFriendResponse(User user, boolean response) {
        WelcomeController welcome = app.getWelcomeController();
        if (welcome != null) {
            welcome.getFriendsSubController().receiveFriendResponse(user, response);
        }
    }

    /* (non-Javadoc)
     * @see IHM.interfaces.DATAtoIHM#receiveFullUser(DATA.model.User, integer)
     */
    @Override
    public void receiveFullUser(final User user, int queryId) {
        Initializable controller = app.getRequests().get(new Integer(queryId));
        if (controller == null) {
            //no entry or it's deleted in the map
            // do nothing (we don't know for what these pictures are for)
        } else {
            if (controller instanceof ProfileController) {
                final ProfileController profileController = ((ProfileController) controller);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        profileController.build(user);
                    }
                });
            }
        }
    }

    /**
     * Gets the connected users.
     *
     * @return the connected users
     */
    public List<UUID> getConnectedUsers() {
        return connectedUsers;
    }
}
