package IHM.utils;

import javafx.scene.control.Tooltip;

/**
 * Created by Sylvain_ on 07/01/2015.
 */
public class Tooltips extends Tooltip {

    public static Tooltip getTooltip(String content) {
        if (content != null) {
            Tooltip tooltip = new Tooltip(content);
            tooltip.getStyleClass().add("tooltip-background");
            return tooltip;
        }
        return null;
    }
}
