package IHM.controllers;

import DATA.model.Group;
import DATA.model.Picture;
import DATA.model.Rule;
import DATA.model.User;
import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Class GroupsController.
 */
public class RulesController implements Initializable {
    
    /** The rules pane. */
    @FXML
    private TitledPane manageRules;
    
    /** The rules. */
    @FXML
    private ListView<RuleHBoxCell> rules;

    /** The finish button. */
    @FXML
    private Button finishBtn;

    /** The application. */
    private MainController application;

    /** The picture (to be ruled) */
    private Picture picture;

    /** The obs groups list. */
    private List<RuleHBoxCell> groupsRules;

    @FXML
    private HBox checkAll;

    /**
     * The Class UserHBoxCell.
     */
    private class RuleHBoxCell extends HBox {

        /** The rule. */
        private Rule rule;

        /** The label. */
        private Label groupLbl;

        private CheckBox canComment;

        private CheckBox canRate;

        private CheckBox canView;

        public RuleHBoxCell(final Rule rule) {
            this.rule = rule;

            groupLbl = new Label();
            groupLbl.setText(rule.getGroup().getNom());

            canView = new CheckBox();
            canRate = new CheckBox();
            canComment = new CheckBox();

            canView.setSelected(rule.isCanView());
            canComment.setSelected(rule.isCanComment());
            canRate.setSelected(rule.isCanRate());

            this.setSpacing(10);
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
        checkAll = new HBox(10);
        checkAll.toFront();

        final CheckBox checkAllComment = new CheckBox();
        final CheckBox checkAllView = new CheckBox();
        final CheckBox checkAllRate = new CheckBox();

        checkAllComment.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(checkAllComment.isSelected());
                updateCells(checkAllComment.isSelected());
            }
        });

        checkAllView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(checkAllComment.isSelected());
                updateCells(checkAllView.isSelected());
            }
        });

        checkAllRate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(checkAllRate.isSelected());
                updateCells(checkAllRate.isSelected());
            }
        });

        Label label = new Label("Groupes: ");
        label.setTextFill(Color.WHITE);
        checkAll.getChildren().addAll(label, checkAllView, checkAllComment, checkAllRate);
    }

    private void updateCells(boolean checked) {
        for(RuleHBoxCell cell : groupsRules) {
            cell.setCanComment(checked);
        }
    }

    public void loadRules(Picture p) {
        picture = getStaticPicture(p);
        groupsRules = Lists.newArrayList();
        ObservableList<RuleHBoxCell> myObservableList = FXCollections.observableList(groupsRules);

        rules = new ListView<RuleHBoxCell>();
        rules.setItems(myObservableList);

        // Add rules to the view
        for(Rule rule : picture.getListRules()) {
            RuleHBoxCell cell = new RuleHBoxCell(rule);
            groupsRules.add(cell);
        }
    }

    @FXML
    private void finish() {
        // Save rules
        List<Rule> rules = Lists.newArrayList();
        for(RuleHBoxCell ruleBox : groupsRules) {
            rules.add(ruleBox.getRule());
        }

        picture.setListRules(rules);
        application.getIHMtoDATA().updatePicture(picture);

        ((Stage) manageRules.getScene().getWindow()).close();
    }

    // TODO delete this fuckin method
    private Picture getStaticPicture(Picture p) {
        p.setListRules(Arrays.asList(new Rule(true, true, true, p, new Group("Autres")),
                new Rule(true, true, true, p, new Group("Amis"))
        ));
        return p;
    }
}
