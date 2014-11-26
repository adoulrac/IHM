package IHM.controllers;

import DATA.model.Picture;
import IHM.utils.FileUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TabbedPicturesSubController extends TabPane implements Initializable{
    private MainController application;

    @FXML
    private Tab myImgTab;

    private static double HGAP_PICTURES = 40.0;
    private static double VGAP_PICTURES = 20.0;

    private static int PICTURE_DIM = 100;

    public TabbedPicturesSubController()
    {
        super();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
    }

    public void setApp(final MainController app) {
        this.application = app;
    }


    /**
     * Set local data
     */
    public void build() {
        this.addPicturesInTab(this.application.currentUser().getListPictures() , myImgTab );
        //this.addPicturesInTab(this.application.currentUser().getListPictures() , allImgTab );
    }

    public StackPane createMiniPicture(final Picture p)
    {
        final Rectangle r = new Rectangle(PICTURE_DIM, PICTURE_DIM);
        ImagePattern imagePattern = new ImagePattern(new Image( p.getFilename() ));
        r.setFill( imagePattern );
        r.setOnMouseClicked( new EventHandler<MouseEvent>()
        {
            @Override
            public void handle( MouseEvent mouseEvent )
            {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        Tab t = new Tab(p.getFilename());
                        t.setContent( new AnchorPane() );
                        addTab( t );
                     //   PictureController pC = new PictureController(p);
                     //   pC.build();
                     //   addTab(pC);
                    }
                }
            }
        } );
        StackPane sP = new StackPane();
        sP.getChildren().addAll( r );
        sP.setAlignment( Pos.CENTER );
        return sP;
    }

    public void addTab(final Tab tab) {
        this.getTabs().add( tab );
    }

    public void addPicturesInTab(final List<Picture> pictures, Tab tab) {
        TilePane tP = new TilePane();
        tP.setVgap( VGAP_PICTURES );
        tP.setHgap( HGAP_PICTURES );
        for(Picture p : pictures)
        {
            tP.getChildren().add(createMiniPicture( p ));
        }
        SplitPane sP = (SplitPane) tab.getContent();
        sP.getItems().add( tP );
        tab.setContent( sP );
    }

    public void addLocalePicture()
    {
        File f = FileUtil.chooseFile();
        if( f != null )
        {
            SplitPane split = (SplitPane) myImgTab.getContent();
            boolean exit = false;
            for(int i=0; i < split.getItems().size() && !exit; ++i)
            {
                if(split.getItems().get( i ) instanceof TilePane)
                {
                    TilePane tile = (TilePane) split.getItems().get( i );
                    Picture p = new Picture( f.toURI().toString() );
                    tile.getChildren().add(createMiniPicture(p));
                    exit = true;
                }
            }
        }
    }
}
