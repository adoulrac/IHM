package IHM.controllers;

import DATA.model.Picture;
import DATA.model.Tag;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PictureController extends Tab implements Initializable
{
    //TODO refresh button
    //TODO admin mode

    private final MainController app;
    private Picture picture;

    private ScrollPane ihm = new ScrollPane();
    private SplitPane content = new SplitPane();

    private ImageView avatar_img = new ImageView();
    private Text partage_txt = new Text();

    private ImageView picture_img = new ImageView();

    private Text note_title = new Text("Note : ");
    private ImageView note_img = new ImageView();
    private Text vote_txt = new Text();
    private TextField vote_field = new TextField("3");
    private Button vote_btn = new Button("Voter");

    private Text tags_title = new Text("Tags : ");
    private Text tags_txt = new Text();

    private Text desc_title = new Text("Description : ");
    private Text desc_txt = new Text();

    private Text com_title = new Text("Commentaires : ");
    private List<CommentPane> comments = new ArrayList<CommentPane>();
    private TextArea write_area = new TextArea("Ecrire un commentaire...");
    private Button send_btn = new Button("Envoyer");


    public PictureController(Picture picture, MainController app) {
        super(picture.getFilename().substring(picture.getFilename().lastIndexOf("/") + 1));

        this.app = app;
        this.picture = picture;

        build();

        app.getIHMtoDATA().getPictureById(this.picture.getUid(), app.addRequest(this));
    }

    public void build(){
        if (app.currentUser().getAvatar()==null) {
            setImage(avatar_img, new Image("IHM/resources/avatar_icon.png"), 75, 75);
        } else {
            setImage(avatar_img, new Image("file:"+app.currentUser().getAvatar()), 75, 75);
        }
        partage_txt.setText("L'image "+picture.getFilename().substring(picture.getFilename().lastIndexOf("/") + 1)+" a été partagée par "+app.currentUser().getLogin()+".");

        setImage(picture_img, new Image(picture.getFilename()), 300, 300);

        setImage(note_img, new Image("IHM/resources/Etoile_note4.png"), 120, 40);
        vote_txt.setText("(" + picture.getListNotes().size() + " votes)");
        vote_field.setMaxWidth(30.0);
        vote_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO vote
            }
        });

        StringBuilder sb_tags = new StringBuilder();
        for (Tag tag : picture.getListTags()) {
            sb_tags.append(tag.getValue());
            sb_tags.append(", ");
        }
        if (sb_tags.length() > 2) {
            sb_tags.setLength(sb_tags.length() - 2);
        }
        tags_txt.setText(sb_tags.toString());

        desc_txt.setText("DATA needs to add description to Picture model");

        for (CommentPane c : comments) {
            c.build();
        }

        send_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO comment
            }
        });
        // Finish
        addContent();
        addCssClasses();
    }

    private void addContent(){

        // Left side
        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.HORIZONTAL);
        sp.getItems().addAll(avatar_img, partage_txt);
        content.getItems().addAll(sp, picture_img);

        // Right side
        sp = new SplitPane();
        sp.setOrientation(Orientation.HORIZONTAL);
        sp.getItems().addAll(note_title, note_img, vote_txt, vote_field, vote_btn);
        content.getItems().addAll(sp);

        sp = new SplitPane();
        sp.setOrientation(Orientation.HORIZONTAL);
        sp.getItems().addAll(tags_title, tags_txt);
        content.getItems().addAll(sp, desc_title, desc_txt, com_title);

        for (CommentPane c : comments) {
            content.getItems().add(c);
        }

        sp = new SplitPane();
        sp.setOrientation(Orientation.HORIZONTAL);
        sp.getItems().addAll(write_area, send_btn);
        content.getItems().addAll(sp);

        // Finish
        ihm.setFitToWidth(true);
        content.setOrientation(Orientation.VERTICAL);
        ihm.setContent(content);
        setContent(ihm);
    }
    private void addCssClasses(){
        ihm.getStyleClass().add("pic-ihm");
        content.getStyleClass().add("pic-content");

        partage_txt.getStyleClass().add("pic-title");
        note_title.getStyleClass().add("pic-title");
        tags_title.getStyleClass().add("pic-title");
        desc_title.getStyleClass().add("pic-title");
        com_title.getStyleClass().add("pic-title");
    }
    public void receiveFullImage(Picture picture) {

    }

    private void setImage(ImageView img, Image source, int fitWidth, int fitHeight) {
        img.setImage(source);
        img.setFitWidth(fitWidth);
        img.setFitHeight(fitHeight);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);
    }

    private class CommentPane extends StackPane {
        //TODO Comments : implement components, build, addContent, addCssClasses, admin mode
        public void build(){


            addContent();
            addCssClasses();
        }

        private void addContent(){ //into this pane
            getChildren().addAll();
        }
        private void addCssClasses(){

        }
    }

    @Override
    public void initialize( final URL url, final ResourceBundle resourceBundle ){
        // NOP
    }
}
