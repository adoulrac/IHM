package IHM.utils;

import Dialogs.DialogFX;

public class Dialogs {

    public static void showInformationDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.INFO);
        dialog.setMessage(message);
        dialog.showDialog();
    }

    public static void showErrorDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.ERROR);
        dialog.setMessage(message);
        dialog.showDialog();
    }

    public static void showWarningDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.WARNING);
        dialog.setMessage(message);
        dialog.showDialog();
    }

    public static boolean showConfirmationDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.QUESTION);
        dialog.setMessage(message);
        int response = dialog.showDialog();
        return response == 0;
    }
}
