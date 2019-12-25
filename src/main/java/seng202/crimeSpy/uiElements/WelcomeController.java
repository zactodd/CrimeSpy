package seng202.crimeSpy.uiElements;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WelcomeController {

    @FXML
    private Label contentLabel;

    @FXML
    private Button HelpButton;


    /**
     * LOGGER is the class wide instance of java.util.logging
     */
    private static final Logger LOGGER = Logger.getLogger(WelcomeController.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_CrimeSpyController.xml";

    // initialise the logger for fileloc
    private static Handler fileHandler;
    static {
        try {
            fileHandler  = new FileHandler(LOG_FILE_STORE_LOC);
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.INFO);
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.INFO);
            LOGGER.config("Configuration done.");
        } catch (IOException e) {
            LOGGER.warning("Failed to initiate file handler");
            LOGGER.setLevel(Level.SEVERE); // for case of console only
        }
    }


    /**
     * Hides the current menu form view and returns user to the main UI
     * Iff there is a crime collection in the system (from either open or import)
     */
    private void goCancel() {
        if (CrimeCollectionManager.getLength() != 0) {
            Scene scene = HelpButton.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.hide();
                }
            }
        }
    }


    @FXML
    /**
     * Begins the process of opening the help menu
     */
    void goToHelpMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("helpUI.fxml"));
            Parent root = fxmlLoader.load();
            HelpController helpController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Help Menu");
            stage.initModality(Modality.APPLICATION_MODAL);
            helpController.initData();
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    @FXML
    /**
     * Begins the process of opening the import menu and the attempts to close the welcome menu
     */
    void goToImportMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("importUI.fxml"));
            Parent root = fxmlLoader.load();
            ImportController importController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Import File");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            importController.initData();
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
        goCancel();
    }


    @FXML
    /**
     * Begins the process of opening the open menu and the attempts to close the welcome menu
     */
    void goToOpenMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("openUI.fxml"));
            Parent root = fxmlLoader.load();
            OpenController openController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Open File");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            openController.initData();
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
        goCancel();
    }
}
