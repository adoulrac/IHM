package IHM.controllers;

import DATA.model.Note;
import DATA.model.Picture;
import DATA.model.Tag;
import IHM.helpers.NoteHelper;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Class TabbedPicturesSubController.
 */
public class TabbedPicturesSubController extends TabPane implements Initializable {

    /** The Constant TAG_SEPARATOR. */
    private static final String TAG_SEPARATOR = ",";

    private static final String USER_SEPARATOR = ",";

    private static final int LEFT_PADDING = 20;

    /** The Constant HGAP_PICTURES. */
    private static final double HGAP_PICTURES = 40.0;

    /** The Constant VGAP_PICTURES. */
    private static final double VGAP_PICTURES = 20.0;

    /** The Constant PICTURE_DIM. */
    private static final int PICTURE_DIM = 100;

    /** The Constant CONFIRM_SUPPRESSION. */
    private static final String CONFIRM_SUPPRESSION = "Confirmez-vous la suppression ?";

    /** The application. */
    private MainController application;

    /** The search field. */
    @FXML
    private TextField searchField;

    /** The tag search. */
    @FXML
    private CheckBox tagSearch;

    /** The user search. */
    @FXML
    private CheckBox userSearch;

    /** The tabbed pictures sub. */
    @FXML
    private TabPane tabbedPicturesSub;

    /** The my img tab. */
    @FXML
    private Tab myImgTab;

    /** The all img tab. */
    @FXML
    private Tab allImgTab;

    /** The delete btn. */
    @FXML
    private Button deleteBtn;

    private int pendingRequestId;

    /** The my img list. */
    private CopyOnWriteArrayList<PicturePane> myImgList; // thread-safe
    
    /** The all img list. */
    private CopyOnWriteArrayList<PicturePane> allImgList; // thread-safe

    /**
     * The Class PicturePane.
     */
    private class PicturePane extends BorderPane {

        /** The picture. */
        private Picture picture;

        /** The picture is selected. */
        private boolean isSelected;

        /** The lbl votes. */
        private Label lblVotes;

        /** The hbox containing stars (marks). */
        private HBox hBoxStars;

        /** The glow depth. */
        private static final int GLOW_DEPTH = 50;

        /**
         * Instantiates a new picture pane.
         *
         * @param p the p
         */
        public PicturePane(Picture p) {
            picture = p;
            isSelected = false;
            hBoxStars = new HBox();
            lblVotes = new Label();
            build();
        }

        /**
         * Builds the Picture box.
         */
        private void build() {
            final ImageView imgView = new ImageView(new Image(picture.getFilename()));
            NoteHelper.adaptImage(imgView, PICTURE_DIM, PICTURE_DIM);
            imgView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if(mouseEvent.getClickCount() == 2) {
                            imgView.setEffect(null);
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
            });
            NoteHelper.getPictureAverage(picture, hBoxStars);
            lblVotes.setText("(" + picture.getListNotes().size() + " votes)");
            setCenter(imgView);
            Label publisher = new Label("Posté par " + picture.getUser().getLogin());
            VBox vBox = new VBox();
            vBox.getChildren().addAll(publisher, hBoxStars);
            vBox.setAlignment(Pos.CENTER);
            setBottom(vBox);
        }

        /**
         * Creates the glow.
         *
         * @param img the img
         */
        private void createGlow(ImageView img) {
            DropShadow borderGlow = new DropShadow();
            borderGlow.setOffsetY(0.0f);
            borderGlow.setOffsetX(0.0f);
            borderGlow.setColor(Color.ORANGE);
            borderGlow.setWidth(GLOW_DEPTH);
            borderGlow.setHeight(GLOW_DEPTH);
            img.setEffect(borderGlow);
        }

        /**
         * Gets the picture.
         *
         * @return the picture
         */
        public Picture getPicture() {
            return picture;
        }

        /**
         * Checks if is selected.
         *
         * @return true, if is selected
         */
        public boolean isSelected() {
            return isSelected;
        }
    }

    /**
     * Instantiates a new tabbed pictures sub controller.
     */
    public TabbedPicturesSubController() {
        super();

        pendingRequestId = 0;
        myImgList = new CopyOnWriteArrayList<PicturePane>();
        allImgList = new CopyOnWriteArrayList<PicturePane>();
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        //NOP
    }

    /**
     * Builds the Tabbed Pictures.
     */
    public void build() {
        deleteBtn.setDisable( true );
        tabbedPicturesSub.setTabClosingPolicy( TabClosingPolicy.SELECTED_TAB );
        buildStaticTab(myImgTab);
        buildStaticTab(allImgTab);

        // Set the user pictures
        List<Picture> myPictures = application.currentUser().getListPictures();
        addPicturesInTab(myPictures, myImgTab);

        // Set All pictures asynchronously
        requestAllPictures();

        // Add enter key press handler on search text field
        searchField.setOnKeyPressed( new EventHandler<KeyEvent>()
        {
            @Override
            public void handle( KeyEvent keyEvent )
            {
                if( keyEvent.getCode().equals( KeyCode.ENTER ) )
                {
                    if( tagSearch.isSelected() )
                    {
                        searchPicturesByTag( searchField.getText() );
                    }
                    else
                    {
                        searchPicturesByUser( searchField.getText() );
                    }
                    // We clear the tab because it will be fill in asynchronously
                    clearTabContent( allImgTab );
                }
            }
        } );
    }

    private void buildStaticTab(Tab tab) {
        TilePane tP2 = new TilePane();
        tP2.setPadding(new Insets(LEFT_PADDING, 0, 0, 0 ));
        tP2.setVgap(VGAP_PICTURES);
        tP2.setHgap(HGAP_PICTURES);
        SplitPane sP2 = (SplitPane) tab.getContent();
        sP2.getItems().add(tP2);
        tab.setClosable(false);
    }

    /**
     * Search pictures by tag.
     *
     * @param text the text
     */
    private void searchPicturesByTag(final String text) {
        if(Strings.isNullOrEmpty(text)) {
            return;
        }
        Integer requestId = application.addRequest(this);
        if(requestId != null) {
            pendingRequestId = requestId;
            ArrayList<String> arrayListStr = new ArrayList<String>(Arrays.asList(text.split(TAG_SEPARATOR)));
            ArrayList<Tag> arrayListTag = new ArrayList<Tag>();
            for(String name : arrayListStr)
            {
                arrayListTag.add(new Tag(name));
            }
            application.getIHMtoDATA().getPictures(arrayListTag, requestId);
        }
    }

    /**
     * Search pictures by user.
     *
     * @param text the text
     */
    private void searchPicturesByUser(final String text) {
        if(Strings.isNullOrEmpty(text)) {
            return;
        }
        Integer requestId = application.addRequest(this);
        if(requestId != null) {
            pendingRequestId = requestId;
            //TODO Add a new method with a string parameter to look for all pictures using username/login, etc.
            //application.getIHMtoDATA().getPictures(Arrays.asList(text.split(USER_SEPARATOR));
        }
    }

    /**
     * Adds the tab to the tab Pane.
     *
     * @param tab the tab
     */
    public void addTab(Tab tab) {
        tab.setClosable(true);
        tabbedPicturesSub.getTabs().add(tab);
        tabbedPicturesSub.getSelectionModel().select(tab);
    }

    /**
     * Adds the pictures in tab.
     *
     * @param pictures the pictures
     * @param tab the tab
     */
    public void addPicturesInTab(final List<Picture> pictures, Tab tab) {
        SplitPane split = (SplitPane) tab.getContent();
        for (int i = 0; i < split.getItems().size(); ++i) { // thread-safe
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
                tab.setContent(split);
            }
        }
    }

    /**
     * Clear tab.
     *
     * @param tab the tab
     */
    private void clearTabContent(Tab tab){
        SplitPane split = (SplitPane) tab.getContent();
        for (Node n : split.getItems()) {
            if(n instanceof TilePane) {
                TilePane tile = (TilePane) n;
                tile.getChildren().clear();
            }
        }
    }

    /**
     * Adds pictures in All Images tab sent by asynchronous call.
     *
     * @param pictures the pictures
     */
    public void addPictures(List<Picture> pictures) {
        if(pictures != null){
            addPicturesInTab(pictures, allImgTab);
        }
    }

    /**
     * Adds a picture in All Images tab sent by asynchronous call.
     *
     * @param picture the picture
     */
    public void addPicture(Picture picture) {
       if(picture != null){
           addPicturesInTab(Arrays.asList(picture), allImgTab);
       }
    }

    /**
     * Adds a local picture.
     */
    public void addLocalPicture() {
        File f = FileUtil.chooseFile();
        if(f != null) {
            Picture p = new Picture(f.toURI().toString(), "", application.currentUser());
            PicturePane pP = new PicturePane(p);
            myImgList.add(pP);
            displayMyImg();
            try {
                application.getIHMtoDATA().addPicture(p);
            } catch (IOException e) {
                Dialogs.showWarningDialog(e.getMessage());
            }
        }
    }

    /**
     * Delete selected picture(s).
     */
    public void deleteSelectedPicture() {
        if(Dialogs.showConfirmationDialog(CONFIRM_SUPPRESSION)) {
            for(PicturePane picturePane : myImgList) {
                if(picturePane.isSelected()) {
                    myImgList.remove(picturePane);
                    application.getIHMtoDATA().deletePicture(picturePane.getPicture());
                }
            }
        }
        displayMyImg();
    }

    /**
     * Refresh the display of My Images tab.
     */
    private void displayMyImg() {
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

    /**
     * Handles the display of the delete button.
     */
    private void deleteBtnDisplay() {
        for (PicturePane picturePane : myImgList) {
            if(picturePane.isSelected()) {
                deleteBtn.setDisable(false);
                return;
            }
        }
        deleteBtn.setDisable(true);
    }

    private void requestAllPictures() {
        Integer requestId = application.addRequest(this);
        if(requestId != null) {
            pendingRequestId = requestId;
            application.getIHMtoDATA().getPictures(requestId);
        }
    }

    public void loadAllPictures(){
        clearTabContent(allImgTab);
        requestAllPictures();
    }

    /**
     * Sets the app.
     *
     * @param app the new app
     */
    public void setApp(final MainController app) {
        this.application = app;
    }

    public boolean isPendingRequest(int requestId) {
        return pendingRequestId == requestId;
    }
}
