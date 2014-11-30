package IHM.controllers;

import DATA.model.Comment;
import DATA.model.Note;
import DATA.model.Picture;
import DATA.model.Tag;
import IHM.utils.Dialogs;
import IHM.validators.SimpleStringValidator;
import IHM.validators.VoteValidator;
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
    //TODO how to close tab + admin mode

    /** The app. */
    private final MainController app;
    
    /** The picture. */
    private Picture picture;

    /** The ihm. */
    private ScrollPane ihm = new ScrollPane();
    
    /** The content. */
    private VBox content = new VBox(8);

    /** The avatar_img. */
    private ImageView avatar_img = new ImageView();
    
    /** The partage_txt. */
    private Text partage_txt = new Text();
    
    /** The refresh_btn. */
    private Button refresh_btn = new Button("Refresh");

    /** The picture_img. */
    private ImageView picture_img = new ImageView();

    /** The note_title. */
    private Text note_title = new Text("Note : ");
    
    /** The note_img. */
    private HBox note_img = new HBox();
    
    /** The vote_txt. */
    private Text vote_txt = new Text();
    
    /** The vote_field. */
    private TextField vote_field = new TextField("3");
    
    /** The vote_btn. */
    private Button vote_btn = new Button("Voter");

    /** The tags_title. */
    private Text tags_title = new Text("Tags : ");
    
    /** The tags_txt. */
    private Text tags_txt = new Text();

    /** The desc_title. */
    private Text desc_title = new Text("Description : ");
    
    /** The desc_txt. */
    private Text desc_txt = new Text();

    /** The com_title. */
    private Text com_title = new Text("Commentaires : ");
    
    /** The comments. */
    private List<CommentPane> comments = new ArrayList<CommentPane>();
    
    /** The write_area. */
    private TextArea write_area = new TextArea();
    
    /** The send_btn. */
    private Button send_btn = new Button("Envoyer");


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
     * Builds the.
     */
    public void build(){
        if (app.currentUser().getAvatar()==null) {
            setImage(avatar_img, new Image("IHM/resources/avatar_icon.png"), 75, 75);
        } else {
            setImage(avatar_img, new Image("file:"+app.currentUser().getAvatar()), 75, 75);
        }
        partage_txt.setText("L'image "+picture.getFilename().substring(picture.getFilename().lastIndexOf("/") + 1)+" a été partagée par "+app.currentUser().getLogin()+".");
        refresh_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                refresh(mouseEvent);
            }
        });

        //TODO display miniature or full image if available
        setImage(picture_img, new Image(picture.getFilename()), 300, 300);

        buildVotes();
        buildTags();

        desc_txt.setWrappingWidth(480);
        desc_txt.setText("DATA needs to add description to Picture model\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce malesuada eleifend tristique. Sed vel orci velit. Quisque consequat euismod nibh, dignissim bibendum nibh ullamcorper et. Nunc venenatis mollis nunc nec tempor. Vestibulum varius accumsan augue, eu lacinia nisl lacinia eu. Mauris porta, orci et porttitor congue, dolor lorem rutrum mi, ut viverra mauris nisl in ex. Maecenas vel turpis in urna vulputate dictum. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas finibus euismod magna, rutrum facilisis felis placerat nec. Cras et semper nibh. In hac habitasse platea dictumst. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer tristique sit amet urna in commodo. Quisque efficitur tincidunt ultrices.");

        if (picture.getComments()!=null) {
            for (Comment c : picture.getComments()) {
                CommentPane cp = new CommentPane(c);
                comments.add(cp);
            }
        }

        write_area.setWrapText(true);
        write_area.setMaxHeight(100);
        write_area.setPromptText("Ecrire un commentaire...");
        send_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                comment(mouseEvent);
            }
        });

        // Finish
        addContent();
        addCssClasses();
    }

    /**
     * Builds the votes.
     */
    private void buildVotes() {
        note_img.getChildren().clear();
        if (picture.getListNotes()!=null && !picture.getListNotes().isEmpty()) {
            float note = 0;
            for (Note n : picture.getListNotes()) {
                note += n.getValue();
            }
            note = note / (float) picture.getListNotes().size();
            int note_round = Math.round(note);
            for (int i = 1; i <= 5; i++) {
                ImageView img = new ImageView();
                if (note_round >= i) {
                    setImage(img, new Image("IHM/resources/star_active.png"), 27, 23);
                } else {
                    setImage(img, new Image("IHM/resources/star_inactive.png"), 24, 23);
                }
                note_img.getChildren().add(img);
            }
            vote_txt.setText(String.format("%.1f", note)+"/5 (" + picture.getListNotes().size() + " votes)");
        } else {
            for (int i = 1; i <= 5; i++) {
                ImageView img = new ImageView();
                setImage(img, new Image("IHM/resources/star_inactive.png"), 24, 23);
                note_img.getChildren().add(img);
            }
            vote_txt.setText("(Aucun vote pour le moment)");
        }
        vote_field.setMaxWidth(30.0);
        vote_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
        tags_txt.setText(sb_tags.toString());
    }

    /**
     * Adds the content.
     */
    private void addContent(){

        // Left side
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(avatar_img, partage_txt, refresh_btn);
        content.getChildren().addAll(hbox, picture_img);

        // Right side
        hbox = new HBox(5);
        hbox.getChildren().addAll(note_title, note_img, vote_txt, vote_field, vote_btn);
        content.getChildren().addAll(hbox);

        hbox = new HBox(5);
        hbox.getChildren().addAll(tags_title, tags_txt);
        content.getChildren().addAll(hbox, desc_title, desc_txt, com_title);

        for (CommentPane c : comments) {
            content.getChildren().add(c);
        }

        hbox = new HBox(5);
        hbox.getChildren().addAll(write_area, send_btn);
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

        partage_txt.getStyleClass().add("pic-title");
        note_title.getStyleClass().add("pic-title");
        tags_title.getStyleClass().add("pic-title");
        desc_title.getStyleClass().add("pic-title");
        com_title.getStyleClass().add("pic-title");
    }

    /**
     * Clear and build.
     */
    private void clearAndBuild() {
        ihm = new ScrollPane();
        content = new VBox(8);

        avatar_img = new ImageView();
        partage_txt = new Text();
        refresh_btn = new Button("Refresh");

        picture_img = new ImageView();

        note_title = new Text("Note : ");
        note_img = new HBox();
        vote_txt = new Text();
        vote_field = new TextField("3");
        vote_btn = new Button("Voter");

        tags_title = new Text("Tags : ");
        tags_txt = new Text();

        desc_title = new Text("Description : ");
        desc_txt = new Text();

        com_title = new Text("Commentaires : ");
        comments = new ArrayList<CommentPane>();
        write_area = new TextArea();
        send_btn = new Button("Envoyer");

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
     * Refresh.
     *
     * @param mouseEvent the mouse event
     */
    public void refresh(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 1) {
                app.getIHMtoDATA().getPictureById(this.picture.getUid(), app.addRequest(this));
            }
        }
    }

    /**
     * Comment.
     *
     * @param mouseEvent the mouse event
     */
    private void comment(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 1) {
                String msg = write_area.getText();
                if (new SimpleStringValidator().validate(msg)) {
                    Comment c = new Comment(msg, new Date(), app.currentUser());
                    app.getIHMtoDATA().addComment(c);
                    content.getChildren().add(content.getChildren().size() - 1, new CommentPane(c)); // add before write_box and button
                    write_area.setText("");
                    write_area.setPromptText("Ecrire un commentaire...");
                }
                else {
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
                if (VoteValidator.validate(vote_field.getText())) {
                    int vote = Integer.parseInt(vote_field.getText());
                    Note note = new Note(vote, app.currentUser(), picture);
                    picture.getListNotes().add(note);
                    buildVotes();
                    app.getIHMtoDATA().addNote(note);
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
            if (comment.getUser().getAvatar()==null) {
                setImage(avatar_img, new Image("IHM/resources/avatar_icon.png"), 75, 75);
            } else {
                setImage(avatar_img, new Image("file:"+comment.getUser().getAvatar()), 75, 75);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("'Le' dd/MM/yyyy 'à' HH'h'mm");
            user_txt.setText(sdf.format(comment.getDateTime())+", "+comment.getUser().getLogin()+ " a commenté : ");

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
