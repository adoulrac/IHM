package IHM.controllers;

import DATA.exceptions.BadInformationException;
import DATA.model.*;
import IHM.helpers.NoteHelper;
import IHM.helpers.ValidatorHelper;
import IHM.utils.Dialogs;
import IHM.utils.FileUtil;
import IHM.utils.Tooltips;
import IHM.validators.VoteValidator;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tab;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class PictureController.
 */
public class PictureController extends Tab implements Initializable {

    /**
     * The avatar size.
     */
    private static final int AVATAR_SIZE = 50;

    /**
     * The maximum length of the title.
     */
    private static final int MAX_TITLE_LENGTH = 65;

    /**
     * The app.
     */
    private final MainController app;

    /**
     * The picture.
     */
    private Picture picture;

    /**
     * The ihm.
     */
    private ScrollPane ihm;

    /**
     * The content.
     */
    private VBox content;

    /**
     * The avatarImg.
     */
    private ImageView avatarImg;

    /**
     * The shareTxt.
     */
    private Text shareTxt;

    /**
     * The name of the file.
     */
    private TextArea pictureName;

    /**
     * The refreshBtn.
     */
    private Button refreshBtn;

    /**
     * The pictureImg.
     */
    private ImageView pictureImg;

    /**
     * The noteTitle.
     */
    private Text noteTitle;

    /**
     * The note image.
     */
    private HBox noteImg;

    /**
     * The vote text.
     */
    private Text voteTxt;

    /**
     * The vote field.
     */
    private TextField voteField;

    /**
     * The vote button.
     */
    private Button voteBtn;

    /**
     * The save picture button.
     */
    private Button savePictureBtn;

    /**
     * The button for the rules.
     */
    private Button rulesBtn;

    /**
     * The tagsTitle.
     */
    private Text tagsTitle;

    /**
     * The tagsTxt.
     */
    private Text tagsTxt;

    /**
     * The descTitle.
     */
    private Text descTitle;

    /**
     * The descTxt.
     */
    private Text descTxt;

    /**
     * The comTitle.
     */
    private Text comTitle;

    /**
     * The comments.
     */
    private List<CommentPane> comments;

    /**
     * The writeArea.
     */
    private TextArea writeArea;

    /**
     * The sendBtn.
     */
    private Button sendBtn;

    /**
     * The edition of description button.
     */
    private Button descEditBtn;

    /**
     * The validation of description button.
     */
    private Button validateDescBtn;

    /**
     * The description text area.
     */
    private TextArea editDescTxt;

    /**
     * The edition of tages button.
     */
    private Button tagsEditBtn;

    /**
     * The edition of tags text.
     */
    private TextField tagsEditTxt;

    /**
     * The validation of tags button.
     */
    private Button tagsValidateBtn;

    /**
     * The current request ID.
     */
    private Integer currentRequestId;

    /**
     * Instantiates a new picture controller.
     *
     * @param pic the picture
     * @param application    the app
     */
    public PictureController(final Picture pic, final MainController application) {
        super(FileUtil.getFilenameFromPath(pic.getFilename()));

        this.app = application;
        this.picture = pic;
        this.currentRequestId = application.addRequest(this);

        build();
        application.getIHMtoDATA()
                .getPictureById(this.picture.getUid(), currentRequestId);
    }

    /**
     * Sets the image.
     *
     * @param img       the img
     * @param source    the source
     * @param fitWidth  the fit width
     * @param fitHeight the fit height
     */
    private static void setImage(final ImageView img, final Image source, final int fitWidth, final int fitHeight) {
        img.setImage(source);
        img.setFitWidth(fitWidth);
        img.setFitHeight(fitHeight);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize
     * (java.net.URL, java.util.ResourceBundle)
    */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        // NOP
    }

    /**
     * Builds the picture.
     */
    public void build() {
        ihm = new ScrollPane();
        final double contentSize = 8.0;
        content = new VBox(contentSize);
        avatarImg = new ImageView();
        shareTxt = new Text();
        pictureName = new TextArea();
        refreshBtn = new Button("Rafraîchir");
        pictureImg = new ImageView();
        noteTitle = new Text("Note : ");
        noteImg = new HBox();
        voteTxt = new Text();
        voteField = new TextField("3");
        voteBtn = new Button("Voter");
        savePictureBtn = new Button("Sauver");
        tagsTitle = new Text("Tags : ");
        tagsTxt = new Text();
        descTitle = new Text("Description : ");
        descTxt = new Text();
        comTitle = new Text("Commentaires : ");
        comments = new ArrayList<CommentPane>();
        writeArea = new TextArea();
        sendBtn = new Button("Envoyer");
        descEditBtn = new Button("Modifier la description");
        validateDescBtn = new Button("Terminer");
        editDescTxt = new TextArea();
        tagsEditBtn = new Button("Modifier");
        tagsEditTxt = new TextField();
        tagsValidateBtn = new Button("Terminer");
        rulesBtn = new Button("Droits");
        // Avatar
        setImage(avatarImg, app.currentUser().getAvatar() == null
                ? new Image("IHM/resources/avatar_icon.png")
                : new Image("file:" + app.currentUser().getAvatar()), AVATAR_SIZE, AVATAR_SIZE);

        // Picture name
        Tooltip.install(pictureName, Tooltips.getTooltip("Press enter to save"));
        if (picture.getTitle() == null) {
            pictureName.setPromptText("(sans titre)");
        } else {
            pictureName.setText(picture.getTitle());
        }

        final int prefWidth = 210;
        final int prefHeight = 70;
        pictureName.setPrefSize(prefWidth, prefHeight);
        pictureName.setEditable(true);
        pictureName.setWrapText(true);

        final int fontSize = 8;
        shareTxt.setFont(Font.font("Verdana", FontWeight.LIGHT, fontSize));
        shareTxt.setText("a partagé l'image");
        shareTxt.setTextAlignment(TextAlignment.JUSTIFY);

        // Refresh button
        final PictureController current = this;
        refreshBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                app.removeRequest(currentRequestId);
                currentRequestId = app.addRequest(current);
                app.getIHMtoDATA().getPictureById(picture.getUid(), currentRequestId);
            }
        });

        // Save button
        savePictureBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                savePictureLocally();
            }
        });

        Image img = picture.getImageObject();
        setImage(pictureImg, img, 300, 300);
        final int duration = 3000;
        FadeTransition ft = new FadeTransition(Duration.millis(duration), pictureImg);
        ft.setFromValue(0.);
        ft.setToValue(1.);
        ft.play();

        buildVotes();
        buildTags();

        // Description
        descTxt.setWrappingWidth(480);
        descTxt.setText(picture.getDescription());
        descEditBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
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
        writeArea.setMaxHeight(60);
        writeArea.setPromptText("Ecrire un commentaire...");
        sendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                comment(mouseEvent);
            }
        });

        //rules button
        rulesBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                launchRules();
            }
        });

        addContent();
        addCssClasses();
    }

    /**
     * Adds the content.
     */
    private void addContent() {
        // Left side
        final HBox hbDesc = new HBox(3);

        VBox textPartage = new VBox(2);
        textPartage.setSpacing(0);
        Text userPartageTxt = new Text();
        userPartageTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        userPartageTxt.setText(picture.getUser().getLogin());
        textPartage.getChildren().addAll(userPartageTxt, shareTxt);

        hbDesc.getChildren().addAll(avatarImg, textPartage);
        HBox hbox = new HBox();
        hbox.setSpacing(3);
        hbox.getChildren().addAll(hbDesc, pictureName, refreshBtn, rulesBtn, savePictureBtn);

        // Limit the number of characters within the textarea
        pictureName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable,
                                final String oldValue, final String newValue) {
                try {
                    // force correct length
                    // by resetting to old value if longer than maxLength
                    if (newValue.length() > MAX_TITLE_LENGTH) {
                        pictureName.setText(oldValue);
                    }
                } catch (Exception e) {
                    pictureName.setText(oldValue);
                    Logger.getLogger(PictureController.class.getName())
                            .log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });

        // VALIDATE ON ENTER
        pictureName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            final KeyCombination combo = new KeyCodeCombination(KeyCode.ENTER);

            public void handle(final KeyEvent t) {
                if (combo.match(t)) {
                    // Prevents linebreaks
                    pictureName.setText(pictureName.getText().replace("\n", ""));
                    picture.setTitle(pictureName.getText());
                }
            }
        });

        // EDITABLE TITLE
        content.getChildren().addAll(hbox);
        content.getChildren().addAll(pictureImg);

        // Right side
        VBox vbox = new VBox(6);
        vbox.setSpacing(20);

        // Note + Votes
        HBox hboxNote = new HBox(2);
        HBox hboxNoteImage = new HBox(2);
        hboxNoteImage.getChildren().addAll(noteTitle, noteImg);
        hboxNote.setSpacing(10);
        hboxNote.getChildren().addAll(hboxNoteImage, voteTxt);

        HBox hboxVote = new HBox(2);
        if (app.getIHMtoDATA().canRate(picture)) {
            hboxVote.getChildren().addAll(voteField, voteBtn);
        }

        vbox.getChildren().addAll(hboxNote, hboxVote, tagsTitle, tagsTxt, descTitle, descTxt);
        HBox pictureAndDesc = new HBox(2);
        pictureAndDesc.getChildren().addAll(pictureImg, vbox);
        content.getChildren().addAll(pictureAndDesc);

        hbox = new HBox(5);
        if (app.currentUser().getLogin().equals(picture.getUser().getLogin())) {
            hbox.getChildren().addAll(tagsTitle, tagsTxt, tagsEditBtn);
        } else {
            hbox.getChildren().addAll(tagsTitle, tagsTxt);
        }

        //Check if ownerF
        if (app.currentUser().getLogin().equals(picture.getUser().getLogin())) {
            content.getChildren().addAll(hbox, descTitle, descTxt, descEditBtn, comTitle);
        } else {
            content.getChildren().addAll(hbox, descTitle, descTxt, comTitle);
        }

        for (CommentPane c : comments) {
            content.getChildren().add(c);
        }

        if (app.getIHMtoDATA().canComment(picture)) {
            hbox = new HBox(5);
            hbox.getChildren().addAll(writeArea, sendBtn);
            content.getChildren().addAll(hbox);
        }

        // Finish
        ihm.setFitToWidth(true);
        ihm.setContent(content);
        setContent(ihm);
    }

    /**
     * Saves a local picture.
     */
    private void savePictureLocally() {
        try {
            String targetPath = FileUtil.chooseDirectory();
            if (Strings.isNullOrEmpty(targetPath)) {
                return;
            }

            try {
                byte[] pixels = picture.getPixels();
                Files.write(pixels, new File(FileUtil.buildFullPath(targetPath,
                        FileUtil.getFilenameFromPath(picture.getFilename()))));
                Dialogs.showInformationDialog("L'image a été sauvegardée avec succès.");
            } catch (Exception e) {
                Dialogs.showInformationDialog("Erreur pendant la sauvegarde de l'image.");
                Logger.getLogger(PictureController.class.getName())
                        .log(Level.SEVERE, "Error while saving the picture.", e);
            }

        } catch (NullPointerException npe) {
            Logger.getLogger(PictureController.class.getName())
                    .log(Level.SEVERE, "Nothing selected.", npe);
            return;
        }
    }

    /**
     * Builds the votes.
     */
    private void buildVotes() {
        noteImg.getChildren().clear();
        float average = NoteHelper.getPictureAverage(picture, noteImg);
        if (average > 0.f) {
            voteTxt.setText(String.format("%.1f", average)
                    + "/5 (" + picture.getListNotes().size() + " votes)");
        } else {
            voteTxt.setText("(Aucun vote)");
        }
        voteField.setMaxWidth(30.0);
        voteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                vote(mouseEvent);
            }
        });
    }

    /**
     * Builds the tags.
     */
    private void buildTags() {
        final StringBuilder sbTags = new StringBuilder();
        for (Tag tag : picture.getListTags()) {
            sbTags.append(tag.getValue());
            sbTags.append(", ");
        }
        if (sbTags.length() > 2) {
            sbTags.setLength(sbTags.length() - 2);
        }
        tagsTxt.setText(sbTags.toString());

        if (picture.getUser().getLogin().equals(app.currentUser().getLogin())) {
            tagsEditTxt.setText(sbTags.toString());
            tagsEditBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    editTags(mouseEvent);
                }
            });
        }
    }

    /**
     * Adds the css classes.
     */
    private void addCssClasses() {
        ihm.getStyleClass().add("pic-ihm");
        ihm.setStyle("-fx-padding: 20;");
        content.getStyleClass().add("pic-content");
        shareTxt.getStyleClass().add("pic-titles");
        noteTitle.getStyleClass().add("pic-titles");
        tagsTitle.getStyleClass().add("pic-titles");
        descTitle.getStyleClass().add("pic-titles");
        comTitle.getStyleClass().add("pic-titles");

        // Picture Title textarea style
        pictureName.getStyleClass().add("pic-title");
        pictureName.setStyle("-fx-background-insets: 0px ;");
        pictureName.setStyle("-fx-text-fill: black;"
                + "-fx-background-color: transparent;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 20;");
    }

    /**
     * Launch rules view.
     */
    public void launchRules() {
        if (this.app != null) {
            this.app.goToRules(this.picture);
        }
    }

    /**
     * Clear and build.
     */
    private void refreshAllComponents() {
        build();
    }

    /**
     * Receive full image.
     *
     * @param pic the picture
     */
    public void receiveFullImage(final Picture pic) {
        this.picture = pic;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshAllComponents();
            }
        });
    }

    /**
     * Add a comment to the picture.
     *
     * @param mouseEvent the mouse event
     */
    private void comment(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 1) {
                String msg = writeArea.getText();
                if (ValidatorHelper.validateString(msg)) {
                    User currentUser = app.currentUser();
                    try {
                        Comment c = new Comment(msg, new Date(), currentUser, picture.getUid(), picture.getUser().getUid());
                        app.getIHMtoDATA().addComment(c);
                        content.getChildren().add(content.getChildren().size() - 1, new CommentPane(c));
                    } catch (Exception e) {
                        Dialogs.showWarningDialog(e.getMessage());
                        Logger.getLogger(PictureController.class.getName())
                                .log(Level.SEVERE, e.getMessage(), e);
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
     * Add a vote to the image.
     *
     * @param mouseEvent the mouse event
     */
    private void vote(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 1) {
            if (VoteValidator.validate(voteField.getText())) {
                int vote = Integer.parseInt(voteField.getText());
                Note note = picture.getNoteFromUser(picture, app.currentUser());

                if (note != null) {
                    // user has already voted
                    boolean ok = Dialogs.showConfirmationDialog("Vous avez déjà voté pour cette image. Souhaitez-vous modifier votre vote ?");
                    if (ok) {
                        note.setValue(vote);
                        addNote(note);
                    }
                } else {
                    // first vote of the user
                    note = new Note(vote, app.currentUser(), picture.getUid(), picture.getUser().getUid());
                    addNote(note);
                }

            } else {
                Dialogs.showWarningDialog(VoteValidator.MESSAGE);
            }
        }
    }

    /**
     * Adds a note.
     * @param note the note given
     */
    private void addNote(final Note note) {
        try {
            app.getIHMtoDATA().addNote(note);
            buildVotes();
        } catch (Exception e) {
            Dialogs.showWarningDialog(e.getMessage());
            Logger.getLogger(PictureController.class.getName())
                    .log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Edit Tags.
     *
     * @param mouseEvent the mouse event.
     */
    private void editTags(final MouseEvent mouseEvent) {
        String str = tagsEditTxt.getText();
        List<Tag> tags = picture.getListTags();
        tags.removeAll(tags);

        final HBox hbox = new HBox(4);
        final Text exampleTxt = new Text("(Ex : tag1,tag2,tag3)");
        hbox.getChildren().addAll(tagsTitle, exampleTxt, tagsEditTxt, tagsValidateBtn);
        content.getChildren().remove(2);
        content.getChildren().add(2, hbox);

        tagsValidateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                String str = tagsEditTxt.getText();
                if (!str.matches("[a-zA-Z,0-9 ]*")) {
                    Dialogs.showErrorDialog("Format non conforme. Veuillez séparer les tags par des virgules, sans utiliser d'espaces.");
                    return;
                } else {
                    String[] array = str.split("(?<!\\\\),");
                    List<Tag> tags = picture.getListTags();
                    tags.removeAll(tags);
                    for (int i = 0; i < array.length; i++) {
                        //remove spaces
                        if (array[i].contains(" ")) {
                            array[i] = array[i].replaceAll("\\s", "");
                        }
                        tags.add(new Tag(array[i]));
                    }
                    picture.setListTags(tags);
                    hbox.getChildren().removeAll(tagsTitle, exampleTxt, tagsEditTxt, tagsValidateBtn);
                    hbox.getChildren().addAll(tagsTitle, tagsTxt, tagsEditBtn);
                    buildTags();
                    content.getChildren().remove(2);
                    content.getChildren().add(2, hbox);

                    Dialogs.showInformationDialog("Liste de tags modifiée avec succès !");
                }
            }
        });
    }

    /**
     * Edit description.
     *
     * @param mouseEvent the mouse event
     */
    private void editDescription(final MouseEvent mouseEvent) {
        editDescTxt.setText(picture.getDescription());
        content.getChildren().add(content.getChildren().size() - 4, editDescTxt);
        content.getChildren().remove(descEditBtn);
        content.getChildren().add(content.getChildren().size() - 3, validateDescBtn);
        descTxt.setText("");
        validateDescBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                picture.setDescription(editDescTxt.getText());
                descTxt.setText(editDescTxt.getText());
                content.getChildren().remove(editDescTxt);
                content.getChildren().remove(validateDescBtn);
                content.getChildren().add(content.getChildren().size() - 2, descEditBtn);
                Dialogs.showInformationDialog("Description modifiée avec succès !");
            }
        });

    }

    /**
     * Handles the suppression of a comment.
     * @param cp The pane of the comment.
     */
    private void deleteComment(final CommentPane cp) {
        try {
            app.getIHMtoDATA().deleteComment(cp.comment);
            content.getChildren().remove(cp);
            comments.remove(cp);
        } catch (BadInformationException e) {
            Dialogs.showWarningDialog("Informations du commentaire invalides pour la suppression.");
            Logger.getLogger(PictureController.class.getName())
                    .log(Level.SEVERE, "Invalid information about the comment.", e);
        }
    }

    /**
     * The Class CommentPane.
     */
    private class CommentPane extends HBox {
        /**
         * The comment.
         */
        private Comment comment;
        /**
         * The avatarImg of the comment author.
         */
        private ImageView avatarImg;
        /**
         * The userTxt contains the name of the user and some metadata (date, etc.).
         */
        private Text userTxt;
        /**
         * Click to switch to edition mode. In edition mode, this button becomes the validate button.
         */
        private Button editBtn;
        /**
         * Click to delete comment. In edition, becomes cancel button.
         */
        private Button delBtn;
        /**
         * The commentTxt contains the text of the comment.
         */
        private Text commentTxt;
        /**
         * VBox that contains userTxt/buttons on first line and commentTxt on second line.
         */
        private VBox vb;
        /**
         * The commentField replace the commentTxt when editing.
         */
        private TextField commentField;

        /**
         * Instantiates a new comment pane.
         *
         * @param com the comment
         */
        public CommentPane(final Comment com) {
            super(10);
            this.comment = com;
            avatarImg = new ImageView();
            userTxt = new Text();
            editBtn = new Button("Editer");
            delBtn = new Button("Supprimer");
            commentTxt = new Text();
            vb = new VBox(5);
            commentField = new TextField();
            build();
        }

        /**
         * Builds the UI for a comment.
         */
        public void build() {
            if (comment.getCommentUser().getAvatar() == null) {
                setImage(avatarImg, new Image("IHM/resources/avatar_icon.png"), 75, 75);
            } else {
                setImage(avatarImg, new Image("file:" + comment.getCommentUser().getAvatar()), 75, 75);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("'Le' dd/MM/yyyy 'à' HH'h'mm");
            userTxt.setText(sdf.format(comment.getDateTime()) + ", " + comment.getCommentUser().getLogin() + " a commenté : ");

            commentTxt.setWrappingWidth(400);
            commentTxt.setText(comment.getValue());

            addContent();
            switchEditionMode(false);
        }

        /**
         * If called with true, switch the UI to edition mode.
         * If called with false, switch back to the displaying of the comment.
         * The method sets the correct behavior of the button callbacks, which calls the method itself to activate and deactivate edition mode.
         * @param edition switch or not to edition mode
         */
        private void switchEditionMode(final boolean edition) {
            final CommentPane current = this;

            // switch to edition mode
            if (edition) {
                if (!vb.getChildren().contains(commentField)) {
                    // if we are not already in edition mode, hide text and display field
                    commentField.setText(commentTxt.getText());
                    vb.getChildren().remove(commentTxt);
                    vb.getChildren().add(commentField);
                }

                editBtn.setText("Valider");
                editBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(final MouseEvent mouseEvent) {
                        try {
                            // Behavior when validating a comment modification (saving then switch back to display mode)
                            comment.setValue(commentField.getText());
                            app.getIHMtoDATA().addComment(comment);
                            commentTxt.setText(commentField.getText());
                            switchEditionMode(false);
                        } catch (Exception e) {
                            Dialogs.showWarningDialog("Informations du commentaire invalides.");
                            Logger.getLogger(PictureController.class.getName())
                                    .log(Level.SEVERE, "Invalid information about the comment.", e);
                        }
                    }
                });

                delBtn.setText("Annuler");
                delBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(final MouseEvent mouseEvent) {
                        // Behavior when canceling a comment modification (switch back to display mode)
                        switchEditionMode(false);
                    }
                });
                // switch back to display
            } else {
                if (!vb.getChildren().contains(commentTxt)) {
                    // if we are not already in display mode, hide field and display text
                    vb.getChildren().remove(commentField);
                    vb.getChildren().add(commentTxt);
                }

                editBtn.setText("Editer");
                editBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(final MouseEvent mouseEvent) {
                        // Behavior when editing (switch edition mode)
                        switchEditionMode(true);
                    }
                });

                delBtn.setText("Supprimer");
                delBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(final MouseEvent mouseEvent) {
                        // Behavior when deleting (delete comment from PictureController)
                        deleteComment(current);
                    }
                });
            }
        }

        /**
         * Adds the UI correctly layed out.
         * Test the rights of the user to edit or delete the comment
         * and hides the buttons if he has no right.
         */
        private void addContent() {
            HBox hb = new HBox(8);

            hb.getChildren().add(userTxt);
            if (comment.getCommentUser().getUid().equals(app.currentUser().getUid())) {
                hb.getChildren().addAll(editBtn, delBtn);
            } else if (picture.getUser().getUid().equals(app.currentUser().getUid())) {
                hb.getChildren().add(delBtn);
            }


            vb.getChildren().addAll(hb, commentTxt);

            getChildren().addAll(avatarImg, vb);
        }
    }

}
