package IHM.controllers;

import DATA.model.Picture;
import IHM.utils.Dialogs;
import IHM.utils.FileUtil;
import com.google.common.base.Strings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

public class TabbedPicturesSubController extends TabPane implements Initializable {

    private static final String TAG_SEPARATOR = ",";

    private static final double HGAP_PICTURES = 40.0;

    private static final double VGAP_PICTURES = 20.0;

    private static final int PICTURE_DIM = 100;

    private static final String CONFIRM_SUPPRESSION = "Confirmez-vous la suppression ?";

    private MainController application;

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox tagSearch;

    @FXML
    private CheckBox userSearch;

    @FXML
    private TabPane tabbedPicturesSub;

    @FXML
    private Tab myImgTab;

    @FXML
    private Tab allImgTab;

    @FXML
    private Button deleteBtn;

    private CopyOnWriteArrayList<PicturePane> myImgList; // thread-safe
    private CopyOnWriteArrayList<PicturePane> allImgList; // thread-safe

    private class PicturePane extends StackPane {

        private Picture picture;

        private boolean isSelected;

        private final int GLOW_DEPTH = 50;

        public PicturePane(Picture p) {
            picture = p;
            isSelected = false;
            build();
        }

        private void build() {
            //final Rectangle r = new Rectangle(PICTURE_DIM, PICTURE_DIM);
            final ImageView imgView = new ImageView( new Image(picture.getFilename()) );
            imgView.setFitWidth(PICTURE_DIM);
            imgView.setFitHeight(PICTURE_DIM);
            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);
            imgView.setCache(true);
            //ImagePattern imagePattern = new ImagePattern(new Image(picture.getFilename()));
            //r.setFill(imagePattern);
            imgView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2) {
                            imgView.setEffect( null );
                            isSelected = false;
                            PictureController pC = new PictureController(picture, application);
                            addTab(pC);
                        }
                        else if(mouseEvent.getClickCount() == 1) {
                            if(isSelected == true) {
                                isSelected = false;
                                imgView.setEffect(null);
                                deleteBtnDisplay();
                            }
                            else {
                                isSelected = true;
                                createGlow(imgView);
                                deleteBtnDisplay();
                            }
                        }
                    }
                }
            } );
            this.getChildren().addAll(imgView);
            this.setAlignment(Pos.CENTER);
        }

        private void createGlow(ImageView img) {
            DropShadow borderGlow= new DropShadow();
            borderGlow.setOffsetY(0.0f);
            borderGlow.setOffsetX(0.0f);
            borderGlow.setColor(Color.ORANGE);
            borderGlow.setWidth(GLOW_DEPTH);
            borderGlow.setHeight(GLOW_DEPTH);
            img.setEffect(borderGlow);
        }

        public Picture getPicture() {
            return picture;
        }

        public boolean isSelected() {
            return isSelected;
        }
    }

    public TabbedPicturesSubController() {
        super();
        myImgList = new CopyOnWriteArrayList<PicturePane>();
        allImgList = new CopyOnWriteArrayList<PicturePane>();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        //NOP
    }

    public void build() {
        deleteBtn.setDisable(true);
        // init tabs
        TilePane tP = new TilePane();
        tP.setPadding( new Insets( 20, 0, 0, 0 ) );
        tP.setVgap(VGAP_PICTURES);
        tP.setHgap(HGAP_PICTURES);
        SplitPane sP = (SplitPane) myImgTab.getContent();
        sP.getItems().add(tP);

        TilePane tP2 = new TilePane();
        tP2.setPadding( new Insets( 20, 0, 0, 0 ) );
        tP2.setVgap(VGAP_PICTURES);
        tP2.setHgap(HGAP_PICTURES);
        SplitPane sP2 = (SplitPane) allImgTab.getContent();
        sP2.getItems().add(tP2);

        // Set the user pictures
        List<Picture> myPictures = application.currentUser().getListPictures();
        addPicturesInTab(myPictures, myImgTab);

        // Set All pictures asynchronously
        Integer requestId = application.addRequest(this);
        if(requestId != null) {
            application.getIHMtoDATA().getPictures( requestId );
        }

        // Add enter key press handler on search text field
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    if (tagSearch.isSelected()) {
                        searchPicturesByTag(searchField.getText());
                    } else {
                        searchPicturesByUser(searchField.getText());
                    }
                    // We clear the tab because it will be fill in asynchronously
                    clearTab(allImgTab);
                }
            }
        });
    }

    private void searchPicturesByTag(final String text) {
        if(Strings.isNullOrEmpty(text)) {
            return;
        }
        Integer requestId = application.addRequest(this);
        if(requestId != null) {
            application.getIHMtoDATA().getPictures(Arrays.asList(text.split(TAG_SEPARATOR)), requestId);
        }
    }

    private void searchPicturesByUser(final String text) {
        if(Strings.isNullOrEmpty(text)) {
            return;
        }
        Integer requestId = application.addRequest(this);
        if(requestId != null) {
            //ASK DATA: Add a new method with a string parameter to look for all pictures using username/login, etc.
            //application.getIHMtoDATA().getPictures(, requestId);
        }
    }

    public void addTab(Tab tab) {
        tabbedPicturesSub.getTabs().add(tab);
        tabbedPicturesSub.getSelectionModel().select(tab);
    }

    public void addPicturesInTab(final List<Picture> pictures, Tab tab) {
        SplitPane split = (SplitPane) tab.getContent();
        for (int i=0; i < split.getItems().size(); ++i) { // thread-safe
            if(split.getItems().get(i) instanceof TilePane) {
                for (Picture p : pictures) {
                    PicturePane picturePane = new PicturePane(p);
                    if(tab.getText().equals("Mes images")) {
                        myImgList.add(picturePane);
                    }
                    else {
                        allImgList.add(picturePane);
                    }
                    ((TilePane) split.getItems().get(i)).getChildren().add(picturePane);
                }
                //split.getItems().add(split.getItems().get(i));
                tab.setContent(split);
            }
        }
    }

    private void clearTab(Tab tab){
        SplitPane split = (SplitPane) tab.getContent();
        for (Node n : split.getItems()) {
            if(n instanceof TilePane) {
                TilePane tile = (TilePane) n;
                tile.getChildren().clear();
            }
        }
    }

    public void addPictures(List<Picture> pictures) {
        if(pictures != null){
            addPicturesInTab(pictures, allImgTab);
        }
    }

    public void addPicture(Picture picture) {
       if(picture != null){
           addPicturesInTab(Arrays.asList(picture), allImgTab);
       }
    }

    public void addLocalPicture(){
        File f = FileUtil.chooseFile();
        if(f != null){
            Picture p = new Picture(f./*getName()*/toURI().toString(), application.currentUser().getUid());
            PicturePane pP = new PicturePane(p);
            myImgList.add(pP);
            displayMyImg();
            //TODO : call for DATA to add picture
        }
    }

    public void deleteSelectedPicture() {
        if(Dialogs.showConfirmationDialog(CONFIRM_SUPPRESSION)) {
            for(PicturePane picturePane : myImgList) {
                if(picturePane.isSelected()) {
                    myImgList.remove(picturePane);
                    //TODO : call for DATA to delete picture
                }
            }
        }
        displayMyImg();
    }

    private void displayMyImg()
    {
        SplitPane split = (SplitPane) myImgTab.getContent();
        for (Node n : split.getItems()) {
            if(n instanceof TilePane) {
                TilePane tile = (TilePane) n;
                tile.getChildren().clear();
                for (PicturePane picturePane : myImgList) {
                    tile.getChildren().add(picturePane);
                }
                deleteBtnDisplay();
                return;
            }
        }
    }

    private void displayAllImg()
    {
        SplitPane split = (SplitPane) allImgTab.getContent();
        for (Node n : split.getItems()) {
            if(n instanceof TilePane) {
                TilePane tile = (TilePane) n;
                tile.getChildren().clear();
                for (PicturePane picturePane : allImgList) {
                    tile.getChildren().add(picturePane);
                }
                return;
            }
        }
    }


    public void deleteBtnDisplay() {
        for (PicturePane picturePane : myImgList) {
            if(picturePane.isSelected()) {
                deleteBtn.setDisable(false);
                return;
            }
        }
        deleteBtn.setDisable(true);
    }

    public void setApp(final MainController app) {
        this.application = app;
    }
}
