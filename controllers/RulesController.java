package IHM.controllers;

import DATA.model.Group;
import DATA.model.Picture;
import DATA.model.Rule;
import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Class GroupsController.
 */
public class RulesController implements Initializable {

    /**
     * The rules pane.
     */
    @FXML
    private TitledPane manageRules;

    /**
     * The rules.
     */
    @FXML
    private ListView<RuleHBoxCell> rules;

    /**
     * The finish button.
     */
    @FXML
    private Button finishBtn;

    /**
     * The application.
     */
    private MainController application;

    /**
     * The picture (to be ruled).
     */
    private Picture picture;

    /**
     * The obs groups list.
     */
    private List<RuleHBoxCell> groupsRules;

    /**
     * The checkbox to select all.
     */
    @FXML
    private HBox checkAll;

    /**
     * Sets the app.
     *
     * @param app the new app
     */
    public final void setApp(final MainController app) {
        this.application = app;
    }

    /**
     * Sets the picture.
     *
     * @param p the picture
     */
    private void setPicture(final Picture p) {
        this.picture = p;
    }

    /*
     * (non-Javadoc)
     * @see javafx.fxml.Initializable#initialize
     * (java.net.URL, java.util.ResourceBundle)
     */
    @FXML
    public void initialize(final URL location, final ResourceBundle resources) {
        checkAll.toFront();
        final int spacing = 20;
        checkAll.setSpacing(spacing);

        final CheckBox checkAllComment = new CheckBox();
        final CheckBox checkAllView = new CheckBox();
        final CheckBox checkAllRate = new CheckBox();

        checkAllComment.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for (RuleHBoxCell cell : groupsRules) {
                    cell.setCanComment(checkAllComment.isSelected());
                }
            }
        });

        checkAllView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for (RuleHBoxCell cell : groupsRules) {
                    cell.setCanView(checkAllView.isSelected());
                }
            }
        });

        checkAllRate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for (RuleHBoxCell cell : groupsRules) {
                    cell.setCanRate(checkAllRate.isSelected());
                }
            }
        });

        Label label = new Label("Groupes: ");
        label.setTextFill(Color.WHITE);
        final int prefWidth = 305;
        label.setPrefWidth(prefWidth);
        checkAll.getChildren().addAll(label, checkAllView, checkAllComment, checkAllRate);
    }

    private void updateCells(boolean checked) {
        for (RuleHBoxCell cell : groupsRules) {
            cell.setCanComment(checked);
        }
    }

    public void loadRules(Picture p) {
        picture = p;
        groupsRules = Lists.newArrayList();
        ObservableList<RuleHBoxCell> myObservableList = FXCollections.observableList(groupsRules);

        rules.setItems(myObservableList);
        rules.setEditable(true);

        // Add rules to the view
        for (Rule rule : picture.getListRules()) {
            System.out.println("Creating rule");
            RuleHBoxCell cell = new RuleHBoxCell(rule);
            groupsRules.add(cell);
        }

        manageRules.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent windowEvent) {
                finish();
            }
        });
    }

    @FXML
    private void finish() {
        // Save rules
        List<Rule> rules = Lists.newArrayList();
        for (RuleHBoxCell ruleBox : groupsRules) {
            rules.add(ruleBox.getRule());
        }

        picture.setListRules( rules );
        application.getIHMtoDATA().updatePicture( picture );

        ((Stage) manageRules.getScene().getWindow()).close();
    }

    /**
     * The Class UserHBoxCell.
     */
    private class RuleHBoxCell extends HBox {

        /**
         * The rule.
         */
        private Rule rule;

        /**
         * The label.
         */
        private Label groupLbl;

        private CheckBox canComment;

        private CheckBox canRate;

        private CheckBox canView;

        public RuleHBoxCell(final Rule rule) {
            this.rule = rule;

            groupLbl = new Label();
            groupLbl.setPrefWidth(300);
            groupLbl.setText(rule.getGroup().getNom());

            canView = new CheckBox();
            canRate = new CheckBox();
            canComment = new CheckBox();

            canView.setSelected(rule.isCanView());
            canComment.setSelected(rule.isCanComment());
            canRate.setSelected(rule.isCanRate());

            this.setSpacing(20);
            this.getChildren().addAll(groupLbl, canView, canComment, canRate);
        }

        public Rule getRule() {
            // Update the current rule
            rule.setCanView(canView.isSelected());
            rule.setCanComment(canComment.isSelected());
            rule.setCanRate(canRate.isSelected());

            return this.rule;
        }

        public void setCanComment(final boolean selected) {
            canComment.setSelected(selected);
        }

        public void setCanView(final boolean selected) {
            canView.setSelected(selected);
        }

        public void setCanRate(final boolean selected) {
            canRate.setSelected(selected);
        }
    }
}
