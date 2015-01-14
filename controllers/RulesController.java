package IHM.controllers;

import DATA.model.Group;
import DATA.model.Picture;
import DATA.model.Rule;
import DATA.model.User;
import IHM.utils.Dialogs;
import com.google.common.base.Strings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static IHM.utils.Dialogs.*;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * The Class GroupsController.
 */
public class RulesController implements Initializable {
    
    /** The rules pane. */
    @FXML
    private TitledPane manageRules;
    
    /** The groups. */
    @FXML
    private ListView groups;

    /** The rules. */
    @FXML
    private ListView rules;

    /** The finish button. */
    @FXML
    private Button finishBtn;

    /** The application. */
    private MainController application;

    /** The picture (to be ruled) */
    private Picture picture;

    /** The obs groups list. */
    private final ObservableList obsGroupsList= observableArrayList();
    
    /** The obs rules list. */
    private final ObservableList obsRulesList= observableArrayList();


    /**
     * The Class UserHBoxCell.
     */
    private class UserHBoxCell extends HBox
    {

        /** The user. */
        private User user;

        /** The label. */
        private Label label;

        /** The icon. */
        private ImageView icon;

        /**
         * Instantiates a new user h box cell.
         *
         * @param vUser the user
         * @param status the status
         */
        public UserHBoxCell(final User vUser, final boolean status) {
            this.user = vUser;

            label = new Label();
            label.setText(user.getLogin());
            HBox.setHgrow(label, Priority.ALWAYS);

            icon = new ImageView();
            //icon.setImage(new Image(status ? ON_PATH : OFF_PATH));
            final float iconHeight = 11.0f;
            final float iconWidth = 13.0f;
            icon.setFitHeight(iconHeight);
            icon.setFitWidth(iconWidth);

            this.getChildren().addAll(icon, label);

            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent mouseEvent) {
                    application.goToProfile(user);
                }
            });
        }

        /**
         * Gets the user.
         *
         * @return the user
         */
        public User getUser() {
            return user;
        }
    }

    /**
     * Sets the app.
     *
     * @param application the new app
     */
    public void setApp(MainController application){
        this.application = application;
    }

    /**
     * Sets the picture.
     *
     * @param p the picture
     */
    public void setPicture(Picture p) {
        this.picture = p;
    }

    /* (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Load groups.
     */
    public void loadGroups() {
        groups.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            try {
                if (groups.getSelectionModel().getSelectedItem() != null) {
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            }
        });
        List<Rule> listRules = picture.getListRules();
        if(listRules != null) {
            for (Rule rule : listRules) {
                obsGroupsList.add( rule.getGroup().getNom() );
                // add in obsRulesList
                //obsRulesList.add();
            }
        }
    }

    @FXML
    private void finish() {
        ((Stage) manageRules.getScene().getWindow()).close();
    }
}
