package IHM.helpers;

import DATA.model.Picture;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class NoteHelper {

    /** The stars dim. */
    private static final int STARS_DIM = 23;

    /** The number of stars. */
    private static final int NB_STARS = 5;

    private static final String STAR_ACTIVE = "IHM/resources/star_active.png";

    private static final String STAR_INACTIVE = "IHM/resources/star_inactive.png";

    public static float getPictureAverage(Picture picture, HBox hBoxStars) {
        float average = 0.f;
        if (picture.getListNotes() != null && !picture.getListNotes().isEmpty()) {
            for (int i = 1; i <= NB_STARS; ++i) {
                ImageView img = new ImageView();
                if (average >= i) {
                    img.setImage(new Image(STAR_ACTIVE));
                    adaptImage(img, STARS_DIM, STARS_DIM);
                } else {
                    img.setImage(new Image(STAR_INACTIVE));
                    adaptImage(img, STARS_DIM, STARS_DIM);
                }
                hBoxStars.getChildren().add(img);
            }
        } else {
            for (int i = 1; i <= NB_STARS; ++i) {
                ImageView img = new ImageView();
                img.setImage(new Image(STAR_INACTIVE));
                adaptImage(img, STARS_DIM, STARS_DIM);
                hBoxStars.getChildren().add(img);
            }
        }
        return average;
    }

    /**
     * Sets an image to keep its ratio.
     *
     * @param img the img
     * @param fitWidth the fit width
     * @param fitHeight the fit height
     */
    public static void adaptImage(ImageView img, final int fitWidth, final int fitHeight) {
        img.setFitWidth(fitWidth);
        img.setFitHeight(fitHeight);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);
    }
}
