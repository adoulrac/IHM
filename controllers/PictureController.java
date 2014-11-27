package IHM.controllers;

import DATA.model.Picture;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class PictureController extends Tab implements Initializable
{
    public PictureController(Picture p)
    {
        super(p.getFilename());
    }

    @Override
    public void initialize( final URL url, final ResourceBundle resourceBundle )
    {

    }


    public void build()
    {}
}
