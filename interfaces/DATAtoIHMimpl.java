package IHM.interfaces;

import DATA.model.Picture;
import DATA.model.User;
import IHM.controller.MainController;

import java.util.List;
import java.util.UUID;

/**
 * Created by greyna on 19/11/2014.
 */
public class DATAtoIHMimpl implements DATAtoIHM {

    MainController app;

    public DATAtoIHMimpl(MainController app) {
        this.app = app;
    }

    @Override
    public void receiveUser(User user, int queryId) {

    }

    @Override
    public void receiveConnectedUser(UUID userId, String login, int queryId) {

    }

    @Override
    public void receiveUnconnectedUser(UUID userId, String login, int queryId) {

    }

    @Override
    public void receivePicture(Picture picture, int queryId) {

    }

    @Override
    public void receivePictures(List<Picture> pictures, int queryId) {

    }

    @Override
    public void receiveFriendRequest(User user, int queryId) {

    }
}
