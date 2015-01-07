package IHM.controllers;

import DATA.model.*;
import IHM.helpers.NoteHelper;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.validators.VoteValidator;
import com.google.common.io.Files;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static IHM.utils.Dialogs.showInformationDialog;

/**
 * The Class PictureController.
 */
public class PictureController extends Tab implements Initializable
{
    private static final int AVATAR_SIZE = 50;

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

    /** The name of the picture */
    private Text filename = new  Text();
    
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

    private Button descEditBtn = new Button("Modifier");

    private Button descDeleteBtn = new Button("Supprimer");

    private Button validateDescBtn = new Button ("Terminer");

    private TextArea editDescTxt = new TextArea();

    private Button tagsEditBtn = new Button ("Terminer");

    private TextField tagsEditTxt = new TextField();


    /**
     * Instantiates a new picture controller.
     *
     * @param picture the picture
     * @param app the app
     */
    public PictureController(Picture picture, MainController app) {
        super(picture.getFilename().substring(picture.getFilename().lastIndexOf("/") + 1));

        this.app = app;
        this.picture = picture;

        build();
        app.getIHMtoDATA().getPictureById(this.picture.getUid(), app.addRequest(this));
    }

    /**
     * Builds the picture.
     */
    public void build(){
        //Set Avatar
        setImage(avatarImg, app.currentUser().getAvatar()==null ? new Image("IHM/resources/avatar_icon.png"):new Image("file:"+app.currentUser().getAvatar()), AVATAR_SIZE, AVATAR_SIZE);

        //Set picture name
        filename.setFont(new Font(20));
        filename.setWrappingWidth(200);
        filename.setText(Files.getNameWithoutExtension(picture.getFilename()));

        //Set partage text
        partageTxt.setFont(Font.font("Verdana", FontWeight.LIGHT, 8));
        partageTxt.setText("a partagé l'image");
        partageTxt.setTextAlignment(TextAlignment.JUSTIFY);


        //Refresh button
        final PictureController current = this;
        refreshBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                app.getIHMtoDATA().getPictureById(picture.getUid(), app.addRequest(current));
            }
        });

        Image img = picture.getImageObject();
        setImage(pictureImg, img, 300, 300);
        FadeTransition ft = new FadeTransition(Duration.millis(3000), pictureImg);
        ft.setFromValue(0.);
        ft.setToValue(1.);
        ft.play();

        buildVotes();
        buildTags();

        //Set Description
        descTxt.setWrappingWidth(480);
        picture.setDescription("Lorem ipsum."); //TODO delete this
        descTxt.setText(picture.getDescription());
        descEditBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                editDescription(mouseEvent);
            }
        });

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
        final StringBuilder sb_tags = new StringBuilder();
        for (Tag tag : picture.getListTags()) {
            sb_tags.append(tag.getValue());
            sb_tags.append(", ");
        }
        if (sb_tags.length() > 2) {
            sb_tags.setLength(sb_tags.length() - 2);
        }
        tagsTxt.setText(sb_tags.toString());

        if(picture.getUser().getLogin().equals(app.currentUser().getLogin())) {
            tagsEditTxt.setText(sb_tags.toString());
            tagsEditBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    editTags(mouseEvent);
                }
            });
        }
    }

    /**
     * Adds the content.
     */
    private void addContent(){

        // Left side
        HBox hbDesc = new HBox(3);

        VBox textPartage = new VBox(2);
        textPartage.setSpacing(0);
        Text userPartageTxt = new Text();
        userPartageTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        userPartageTxt.setText(picture.getUser().getLogin());
        textPartage.getChildren().addAll(userPartageTxt, partageTxt);

        hbDesc.getChildren().addAll(avatarImg, textPartage);
        HBox hbox = new HBox(2);
        hbox.setSpacing(50);
        hbox.getChildren().addAll(hbDesc, filename, refreshBtn);
        content.getChildren().addAll(hbox);
        content.getChildren().addAll(pictureImg);

        // Right side
        VBox vbox = new VBox(6);
        vbox.setSpacing(20);

        // Note + Votes
        HBox hboxNote = new HBox(2);
        HBox hboxNoteImage = new HBox(2);
        hboxNoteImage.getChildren().addAll(noteTitle, noteImg);
        hboxNote.setSpacing(30);
        hboxNote.getChildren().addAll(hboxNoteImage, voteTxt);

        HBox hboxVote = new HBox(2);
        hboxVote.getChildren().addAll(voteField, voteBtn);

        vbox.getChildren().addAll(hboxNote, hboxVote, tagsTitle, tagsTxt, descTitle, descTxt);
        HBox pictureAndDesc = new HBox(2);
        pictureAndDesc.getChildren().addAll(pictureImg, vbox);
        content.getChildren().addAll(pictureAndDesc);

        //content.getChildren().add(comTitle);
        hbox = new HBox(5);

        if(app.currentUser().getLogin().equals(picture.getUser().getLogin())) {
            hbox.getChildren().addAll(tagsTitle, tagsEditTxt, tagsEditBtn);
        }
        else {
            hbox.getChildren().addAll(tagsTitle, tagsTxt);
        }

        //Check if owner
        if(app.currentUser().getLogin().equals(picture.getUser().getLogin())) {
            content.getChildren().addAll(hbox, descTitle, descTxt, descEditBtn, comTitle);
        }
        else {
            content.getChildren().addAll(hbox, descTitle, descTxt, comTitle);
        }

        for (CommentPane c : comments) {
            content.getChildren().add(c);
        }

        hbox = new HBox(5);
        hbox.getChildren().addAll(writeArea, sendBtn);
        content.getChildren().addAll(hbox);

        // Finish
        ihm.setFitToWidth(true);
        ihm.setContent(content);
        setContent(ihm);
    }

    /**
     * Adds the css classes.
     */
    private void addCssClasses(){
        ihm.getStyleClass().add("pic-ihm");
        ihm.setStyle("-fx-padding: 20;");
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
        content.setSpacing(30);

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
        descTxt.setWrappingWidth(345);
        descTxt.setTextAlignment(TextAlignment.JUSTIFY);

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
                    } catch (Exception e) {
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
                    }catch(Exception e) {
                        Dialogs.showWarningDialog(e.getMessage());
                    }
                } else {
                    Dialogs.showWarningDialog(VoteValidator.MESSAGE);
                }
            }
        }
    }

    /***
     * Edit Tags
     *
     *  @param mouseEvent
     */
    private void  editTags(MouseEvent mouseEvent) {
        String str = tagsEditTxt.getText();
        String[] array = str.split("(?<!\\\\),");
        List<Tag> tags = picture.getListTags();
        tags.removeAll(tags);
        for (int i = 0; i < array.length; i++) {
            tags.add(new Tag(array[i]));
        }
        //Appel IHMtoDATA setListTags(tags);
        picture.setListTags(tags);
        showInformationDialog("Liste de tags modifiée avec succès !");
    }

    /**
     * Edit description.
     *
     * @param mouseEvent the mouse event
     */
    private void editDescription(MouseEvent mouseEvent) {
        editDescTxt.setText(picture.getDescription());
        content.getChildren().add(content.getChildren().size() - 4, editDescTxt);
        content.getChildren().remove(descEditBtn);
        content.getChildren().add(content.getChildren().size() - 3, validateDescBtn);
        descTxt.setText("");
        validateDescBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //appel IHMtoDATA setDescription(editDescTxt.getText());
                picture.setDescription(editDescTxt.getText());
                descTxt.setText(editDescTxt.getText());
                content.getChildren().remove(editDescTxt);
                content.getChildren().remove(validateDescBtn);
                content.getChildren().add(content.getChildren().size() - 2, descEditBtn);
                showInformationDialog("Description modifiée avec succès !");
            }
        });

    }

    private void deleteComment(CommentPane cp) {
        //TODO try catch on delete comment
        //app.getIHMtoDATA() delete comment todo
        content.getChildren().remove(cp);
        comments.remove(cp);
    }

    /**
     * The Class CommentPane.
     */
    private class CommentPane extends HBox {
        //TODO test to activate admin mode

        // VBox that contains userTxt/buttons on first line and commentTxt on second line
        VBox vb = new VBox(5);

        /** The comment. */
        public Comment comment;

        /** The avatarImg. */
        public ImageView avatarImg = new ImageView();
        
        /** The userTxt. */
        public Text userTxt = new Text();

        // Click to switch to edit. In edition, becomes validate button
        public Button editBtn = new Button("Editer");

        // Click to delete comment. In edition, becomes cancel button
        public Button delBtn = new Button("Supprimer");

        /** The commentTxt. */
        public Text commentTxt = new Text();

        private TextField commentField = new TextField();

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
                setImage(avatarImg, new Image("IHM/resources/avatar_icon.png"), 75, 75);
            } else {
                setImage(avatarImg, new Image("file:"+comment.getCommentUser().getAvatar()), 75, 75);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("'Le' dd/MM/yyyy 'à' HH'h'mm");
            userTxt.setText(sdf.format(comment.getDateTime()) + ", " + comment.getCommentUser().getLogin() + " a commenté : ");

            commentTxt.setWrappingWidth(400);
            commentTxt.setText(comment.getValue());

            addContent();
            addCssClasses();

            switchEditionMode(false);
        }


        private void switchEditionMode(boolean edition) {
            final CommentPane current = this;

            if (edition) {
                if (!vb.getChildren().contains(commentField)) {
                    commentField.setText(commentTxt.getText());
                    vb.getChildren().remove(commentTxt);
                    vb.getChildren().add(commentField);
                }

                editBtn.setText("Valider");
                editBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            comment.setValue(commentField.getText());
                            app.getIHMtoDATA().addComment(comment);
                            commentTxt.setText(commentField.getText());
                            switchEditionMode(false);
                        } catch (Exception e) {
                            Dialogs.showWarningDialog("Informations du commentaire invalides.");
                        }
                    }
                });

                delBtn.setText("Annuler");
                delBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        switchEditionMode(false);
                    }
                });
            }
            else {
                if (!vb.getChildren().contains(commentTxt)) {
                    vb.getChildren().remove(commentField);
                    vb.getChildren().add(commentTxt);
                }

                editBtn.setText("Editer");
                editBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        switchEditionMode(true);
                    }
                });

                delBtn.setText("Supprimer");
                delBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        deleteComment(current);
                    }
                });
            }
        }

        /**
         * Adds the content.
         */
        private void addContent(){
            HBox hb = new HBox(8);

            hb.getChildren().add(userTxt);
            if (comment.getCommentUser().getUid()==app.currentUser().getUid()) {
                hb.getChildren().addAll(editBtn, delBtn);
            }
            else if (picture.getUser().getUid()==app.currentUser().getUid()) {
                hb.getChildren().add(delBtn);
            }


            vb.getChildren().addAll(hb, commentTxt);

            getChildren().addAll(avatarImg, vb);
        }

        /**
         * Adds the css classes.
         */
        private void addCssClasses(){
            userTxt.getStyleClass().add("pic-title");
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
