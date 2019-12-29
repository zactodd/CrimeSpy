


package crimeSpy.uiElements;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Window;
import crimeSpy.Main;


/**
 * The About Menu Dialog
 * Shows the user information about crimeSpy
 */
public class AboutController {

    @FXML
    private Label versionLabel;


    /**
     * Initializes the crimeSpy version number in this menu.
     */
    public void initData() {
        versionLabel.setText(Main.currentVersion);
    }


    /**
     * Hides the current menu form view and returns user to the main UI
     */
    public void goOKButton() {
        Scene scene = versionLabel.getScene();
        if (scene != null) {
            Window window = scene.getWindow();
            if (window != null) {
                window.hide();
            }
        }
    }

}