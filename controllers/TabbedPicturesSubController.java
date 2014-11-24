package IHM.controllers;

import DATA.model.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TabbedPicturesSubController extends TabPane implements Initializable{
    private MainController application;

    @FXML
    private Tab allImgTab;
    @FXML
    private TilePane myImgPane;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle)
    {
//        myImgPane.setHgap( 40.0 );
//        myImgPane.setVgap( 20.0 );
//        for(int i=0; i < 10; ++i)
//        {
//            final Rectangle r = new Rectangle(100, 100);
//            ImagePattern imagePattern = new ImagePattern(new Image( "IHM/resource/avatar_icon.png"));
//            r.setFill( imagePattern );
//            StackPane sP = new StackPane();
//            sP.getChildren().addAll( r );
//            sP.setAlignment( Pos.CENTER );
//            myImgPane.getChildren().add(sP);
//        }
    }

    public void constructMyImg()
    {
        ArrayList<Picture> myPics = new ArrayList<Picture>( this.application.currentUser().getListPictures() );
        myImgPane.setHgap( 40.0 );
        myImgPane.setVgap( 20.0 );
        for(int i=0; i < myPics.size(); ++i)
        {
            final Rectangle r = new Rectangle(100, 100);
            ImagePattern imagePattern = new ImagePattern(new Image( myPics.get( i ).getFilename()));
            r.setFill( imagePattern );
            StackPane sP = new StackPane();
            sP.getChildren().addAll( r );
            sP.setAlignment( Pos.CENTER );
            myImgPane.getChildren().add(sP);
        }
    }

    public void constructAllImg()
    {

    }

    public void setApp(final MainController app) {
        this.application = app;
    }
}
