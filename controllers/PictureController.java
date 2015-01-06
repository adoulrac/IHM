package IHM.controllers;

import DATA.exceptions.BadInformationException;
import DATA.model.*;
import IHM.helpers.NoteHelper;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.validators.VoteValidator;
import com.google.common.io.Files;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
/**
 * The Class PictureController.
 */
public class PictureController extends Tab implements Initializable
{
    //TODO Edition mode
    //TODO Change underscore case to camel case

    private static final int AVATAR_SIZE = 75;

    private static final int PICTURE_SIZE = 300;

    /** The app. */
    private final MainController app;
    
    /** The picture. */
    private Picture picture;

    /** The ihm. */
    private ScrollPane ihm = new ScrollPane();
    
    /** The content. */
    private VBox content = new VBox(8);

    /** The avatarImg. */
    private ImageView avatarImg = new ImageView();
    
    /** The partageTxt. */
    private Text partageTxt = new Text();
    
    /** The refreshBtn. */
    private Button refreshBtn = new Button("Refresh");

    /** The pictureImg. */
    private ImageView pictureImg = new ImageView();

    /** The noteTitle. */
    private Text noteTitle = new Text("Note : ");
    
    /** The noteImg. */
    private HBox noteImg = new HBox();
    
    /** The voteTxt. */
    private Text voteTxt = new Text();
    
    /** The voteField. */
    private TextField voteField = new TextField("3");
    
    /** The voteBtn. */
    private Button voteBtn = new Button("Voter");

    /** The tagsTitle. */
    private Text tagsTitle = new Text("Tags : ");
    
    /** The tagsTxt. */
    private Text tagsTxt = new Text();

    /** The descTitle. */
    private Text descTitle = new Text("Description : ");
    
    /** The descTxt. */
    private Text descTxt = new Text();

    /** The comTitle. */
    private Text comTitle = new Text("Commentaires : ");
    
    /** The comments. */
    private List<CommentPane> comments = new ArrayList<CommentPane>();
    
    /** The writeArea. */
    private TextArea writeArea = new TextArea();
    
    /** The sendBtn. */
    private Button sendBtn = new Button("Envoyer");


    /**
     * Instantiates a new picture controller.
     *
     * @param picture the picture
     * @param app the app
     */
    public PictureController(Picture picture, MainController app) {
        super(picture.getFilename().substring(picture.getFilename().lastIndexOf("/") + 1));
        System.out.println(picture.getFilename());

        this.app = app;
        this.picture = picture;

        build();
        app.getIHMtoDATA().getPictureById(this.picture.getUid(), app.addRequest(this));
    }

    /**
     * Builds the.
     */
    public void build(){
        //Set Avatar
        setImage(avatarImg, app.currentUser().getAvatar()==null ? new Image("IHM/resources/avatar_icon.png"):new Image("file:"+app.currentUser().getAvatar()), AVATAR_SIZE, AVATAR_SIZE);

        //Set picture name
        String filename = Files.getNameWithoutExtension(picture.getFilename());

        //Set Owner name
        partageTxt.setText("L'image " + filename + " a été partagée par " + picture.getUser().getLogin() + ".");

        //Refresh button
        final PictureController current = this;
        refreshBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                app.getIHMtoDATA().getPictureById(picture.getUid(), app.addRequest(current));
            }
        });

        //TODO miniature/full image tests and displays
        Image img = picture.getImageObject();
        setImage(pictureImg, img, 300, 300);

        buildVotes();
        buildTags();

        //Set Description
        descTxt.setWrappingWidth(480);
        descTxt.setText(picture.getDescription());

        if (picture.getComments() != null) {
            for (Comment c : picture.getComments()) {
                CommentPane cp = new CommentPane(c);
                comments.add(cp);
            }
        }
        writeArea.setWrapText(true);
        writeArea.setMaxHeight(100);
        writeArea.setPromptText("Ecrire un commentaire...");
        sendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                comment(mouseEvent);
            }
        });

        addContent();
        addCssClasses();
    }

    /**
     * Builds the votes.
     */
    private void buildVotes() {
        noteImg.getChildren().clear();
        float average = NoteHelper.getPictureAverage(picture, noteImg);
        if (average > 0.f) {
            voteTxt.setText(String.format("%.1f", average) + "/5 (" +   picture.getListNotes().size() + " votes)");
        } else {
            voteTxt.setText("(Aucun vote pour le moment)");
        }
        voteField.setMaxWidth(30.0);
        voteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                vote(mouseEvent);
            }
        });
    }

    /**
     * Builds the tags.
     */
    private void buildTags() {
        StringBuilder sb_tags = new StringBuilder();
        for (Tag tag : picture.getListTags()) {
            sb_tags.append(tag.getValue());
            sb_tags.append(", ");
        }
        if (sb_tags.length() > 2) {
            sb_tags.setLength(sb_tags.length() - 2);
        }
        tagsTxt.setText(sb_tags.toString());
    }

    /**
     * Adds the content.
     */
    private void addContent(){

        // Left side
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(avatarImg, partageTxt, refreshBtn);
        content.getChildren().addAll(hbox, pictureImg);

        // Right side
        hbox = new HBox(5);
        hbox.getChildren().addAll(noteTitle, noteImg, voteTxt, voteField, voteBtn);
        content.getChildren().addAll(hbox);

        hbox = new HBox(5);
        hbox.getChildren().addAll(tagsTitle, tagsTxt);
        content.getChildren().addAll(hbox, descTitle, descTxt, comTitle);

        for (CommentPane c : comments) {
            content.getChildren().add(c);
        }

        hbox = new HBox(5);
        hbox.getChildren().addAll(writeArea, sendBtn);
        content.getChildren().addAll(hbox);

        // Finish
        ihm.setFitToWidth(true);
        //content.setOrientation(Orientation.VERTICAL);
        ihm.setContent(content);
        setContent(ihm);
    }

    /**
     * Adds the css classes.
     */
    private void addCssClasses(){
        ihm.getStyleClass().add("pic-ihm");
        content.getStyleClass().add("pic-content");

        partageTxt.getStyleClass().add("pic-title");
        noteTitle.getStyleClass().add("pic-title");
        tagsTitle.getStyleClass().add("pic-title");
        descTitle.getStyleClass().add("pic-title");
        comTitle.getStyleClass().add("pic-title");
    }

    /**
     * Clear and build.
     */
    private void clearAndBuild() {
        ihm = new ScrollPane();
        content = new VBox(8);

        avatarImg = new ImageView();
        partageTxt = new Text();
        refreshBtn = new Button("Refresh");

        pictureImg = new ImageView();

        noteTitle = new Text("Note : ");
        noteImg = new HBox();
        voteTxt = new Text();
        voteField = new TextField("3");
        voteBtn = new Button("Voter");

        tagsTitle = new Text("Tags : ");
        tagsTxt = new Text();

        descTitle = new Text("Description : ");
        descTxt = new Text();

        comTitle = new Text("Commentaires : ");
        comments = new ArrayList<CommentPane>();
        writeArea = new TextArea();
        sendBtn = new Button("Envoyer");

        build();
    }

    /**
     * Receive full image.
     *
     * @param picture the picture
     */
    public void receiveFullImage(Picture picture) {
        this.picture = picture;
        clearAndBuild();
    }

    /**
     * Comment.
     *
     * @param mouseEvent the mouse event
     */
    private void comment(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 1) {
                String msg = writeArea.getText();
                if (ValidatorHelper.validateString(msg)) {
                    User currentUser = app.currentUser();
                    try {
                        Comment c = new Comment(msg, new Date(), currentUser, picture.getUid(), picture.getUser().getUid());
                        app.getIHMtoDATA().addComment(c);
                        content.getChildren().add(content.getChildren().size() - 1, new CommentPane(c));
                    }catch(BadInformationException e) {
                        Dialogs.showWarningDialog(e.getMessage());
                    }
                    writeArea.setText("");
                    writeArea.setPromptText("Ecrire un commentaire...");
                } else {
                    Dialogs.showWarningDialog("Veuillez entrer un commentaire pour pouvoir l'envoyer.");
                }
            }
        }
    }

    /**
     * Vote.
     *
     * @param mouseEvent the mouse event
     */
    private void vote(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 1) {
                if (VoteValidator.validate(voteField.getText())) {
                    int vote = Integer.parseInt(voteField.getText());
                    try {
                        Note note = new Note(vote, app.currentUser(), picture.getUid(), picture.getUser().getUid());
                        picture.getListNotes().add(note);
                        buildVotes();
                        app.getIHMtoDATA().addNote(note);
                    }catch(BadInformationException e) {
                        Dialogs.showWarningDialog(e.getMessage());
                    }
                } else {
                    Dialogs.showWarningDialog(VoteValidator.MESSAGE);
                }
            }
        }
    }


    /**
     * The Class CommentPane.
     */
    private class CommentPane extends HBox {
        //TODO admin mode

        /** The comment. */
        public Comment comment;

        /** The avatar_img. */
        public ImageView avatar_img = new ImageView();
        
        /** The user_txt. */
        public Text user_txt = new Text();
        
        /** The comment_txt. */
        public Text comment_txt = new Text();

        /**
         * Instantiates a new comment pane.
         *
         * @param comment the comment
         */
        public CommentPane(Comment comment) {
            super(10);
            this.comment = comment;
            build();
        }

        /**
         * Builds the.
         */
        public void build(){
            if (comment.getCommentUser().getAvatar()==null) {
                setImage(avatar_img, new Image("IHM/resources/avatar_icon.png"), 75, 75);
            } else {
                setImage(avatar_img, new Image("file:"+comment.getCommentUser().getAvatar()), 75, 75);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("'Le' dd/MM/yyyy 'à' HH'h'mm");
            user_txt.setText(sdf.format(comment.getDateTime())+", "+comment.getCommentUser().getLogin()+ " a commenté : ");

            comment_txt.setWrappingWidth(400);
            comment_txt.setText(comment.getValue());

            addContent();
            addCssClasses();
        }

        /**
         * Adds the content.
         */
        private void addContent(){
            VBox sp = new VBox(5);
            sp.getChildren().addAll(user_txt, comment_txt);

            getChildren().addAll(avatar_img, sp);
        }

        /**
         * Adds the css classes.
         */
        private void addCssClasses(){
            user_txt.getStyleClass().add("pic-title");
        }
    }

    /**
     * Sets the image.
     *
     * @param img the img
     * @param source the source
     * @param fitWidth the fit width
     * @param fitHeight the fit height
     */
    private static void setImage(ImageView img, Image source, int fitWidth, int fitHeight) {
        img.setImage(source);
        img.setFitWidth(fitWidth);
        img.setFitHeight(fitHeight);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize( final URL url, final ResourceBundle resourceBundle ){
        // NOP
    }
}
