package IHM.controllers;

import DATA.model.Picture;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PictureController extends Pane implements Initializable
{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addPictures(List<Picture> pictures) {
        //TODO receive async pictures
    }

    public void addPicture(Picture picture) {
        //TODO receive async picture
    }
}
