package IHM.utils;

import Dialogs.DialogFX;

/**
 * The Class Dialogs.
 */
public class Dialogs {

    /**
     * Show information dialog.
     *
     * @param message the message
     */
    public static void showInformationDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.INFO);
        dialog.setMessage(message);
        dialog.showDialog();
    }

    /**
     * Show error dialog.
     *
     * @param message the message
     */
    public static void showErrorDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.ERROR);
        dialog.setMessage(message);
        dialog.showDialog();
    }

    /**
     * Show warning dialog.
     *
     * @param message the message
     */
    public static void showWarningDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.WARNING);
        dialog.setMessage(message);
        dialog.showDialog();
    }

    /**
     * Show confirmation dialog.
     *
     * @param message the message
     * @return true, if successful
     */
    public static boolean showConfirmationDialog(final String message) {
        DialogFX dialog = new DialogFX(DialogFX.Type.QUESTION);
        dialog.setMessage(message);
        int response = dialog.showDialog();
        return response == 0;
    }
}
