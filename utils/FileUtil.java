package IHM.utils;


import com.google.common.io.Files;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * The Class FileUtil.
 */
public class FileUtil {

    /**
     * Choose file.
     *
     * @return the file
     */
    public static File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(new Stage());
        if (f != null) {
            return f;
        }
        return null;
    }

    public static String chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Directory chooser");
        File selectedDirectory = chooser.showDialog(new Stage());

        return selectedDirectory.getAbsolutePath();
    }

    public static String buildFullPath(String path, String filename) {
        return path + File.separator + filename;
    }

    public static String getFilenameFromPath(String path) {
        return Files.getNameWithoutExtension(path) + "." + Files.getFileExtension(path);
    }
}
