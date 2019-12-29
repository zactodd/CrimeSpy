package crimeSpy.uiElements;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import crimeSpy.Main;
import crimeSpy.exceptionHandling.ResourceNotFoundException;

import java.net.URL;


/**
 * The Help Menu Dialog
 * Allows the user to view a help file for crimeSpy
 */
public class HelpController {

    @FXML
    private Label versionLabel;

    @FXML
    private WebView helphtml;

    @FXML
    private Button okButton;


    /**
     * Initializes the crimeSpy version number in this menu.
     */
    public void initData() {
        versionLabel.setText(Main.currentVersion);
        final WebEngine engine = helphtml.getEngine();
        URL url;
        try {
            //throws ResourceNotFoundException
            url = checkResource("AboutCrimeSpy.htm");
            engine.load(url.toExternalForm());
        } catch (ResourceNotFoundException e) {
            //System.out.println(e.getMessage() + " : " + e.getStackTrace());
        }
    }


    /**
     * Robust check for file url: check the resource location to ensure that it is valid.
     * @param resourceLoc String url of resource
     * @return URL object of legitimate resource url
     * @throws crimeSpy.exceptionHandling.ResourceNotFoundException if the resource is not found
     */
    private URL checkResource(String resourceLoc) throws ResourceNotFoundException {
        //LOGGER.info("Checking for url resource " + resourceLoc);
        URL url = getClass().getResource(resourceLoc);
        if (url == null) {
            String cName = getClass().getName();
            int i = cName.lastIndexOf(".");
            if (i > -1) {
                cName = cName.substring(i + 1);
            }
            cName = cName + ".class";
            // Object testPath = this.getClass().getResource(cName); // <-- can add in path but gets kinda clunky for output
            String err = "Could not find resource: " + resourceLoc;
            err += ", from class: " + cName;
            //System.out.println("url resource location failed: \n" + err);
            throw new ResourceNotFoundException(err);
        } else {
            return url;
        }
    }


    /**
     * Hides the current menu form view and returns user to the main UI
     */
    public void goOKButton() {
        Scene scene = okButton.getScene();
        if (scene != null) {
            Window window = scene.getWindow();
            if (window != null) {
                window.hide();
            }
        }
    }
}



