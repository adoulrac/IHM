package IHM.utils;


import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileUtil {

    public static File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(new Stage());
        if (f != null) {
            return f;
        }
        return null;
    }
}
