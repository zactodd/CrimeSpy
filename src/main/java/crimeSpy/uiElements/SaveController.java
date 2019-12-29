package crimeSpy.uiElements;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import crimeSpy.crimeData.CrimeCollectionManager;

import java.io.File;


/**
 * The Save Menu Dialog
 * Allows the user to save a specific crimeSpy Database
 */
public class SaveController {

    private File saveFile;

    @FXML
    private RadioButton saveCurrentRadio;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField saveLocationText;

    @FXML
    private Button browseButton;


    /**
     * Initializes Data in this menu.
     */
    public void initData() {
        saveLocationText.setText("Browse...");
    }


    /**
     * Hides the current menu form view and returns user to the main UI
     */
    public void goCancelButton() {
        Scene scene = cancelButton.getScene();
        if (scene != null) {
            Window window = scene.getWindow();
            if (window != null) {
                window.hide();
            }
        }
    }


    /**
     * enter the dialog for browsing to set save file
     */
    public void goBrowseButton() {
        Scene scene = browseButton.getScene();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DB Files", "*.db"));
        Window window = scene.getWindow();
        saveFile = fileChooser.showSaveDialog(window);
        if (saveFile != null) {
            saveLocationText.setText(saveFile.getAbsolutePath());
        }

    }


    /**
     * Fist checks that all values have been entered and shows validation
     * Then calls functions to save the file
     */
    public void goSaveButton() {
        if (saveLocationText.getText().equals("") || saveLocationText.getText().equals("Browse...")) {
            displayWarning("Warning", "Save Location or name has not been set");
        } else {
            // Ensures correct suffix on save file name
            if (!saveLocationText.getText().substring(saveLocationText.getText().length() - 3).equals(".db")) {
                saveLocationText.setText(saveLocationText.getText() + ".db");
            }
            Integer resCode;
            if (saveCurrentRadio.isSelected()) {
                //Saves with filter selected
                resCode = CrimeCollectionManager.saveCrimeDB(true, saveLocationText.getText());
            } else {
                //Saves with no filter applied
                resCode = CrimeCollectionManager.saveCrimeDB(false, saveLocationText.getText());
            }

            if (resCode == 0) {
                displayWarning("Success", "Successful Save");
                goCancelButton();
            } else if (resCode == 1) {
                displayWarning("Error", "SQL Problem with creating the new Crime Record database file");
            } else if (resCode == 2) {
                displayWarning("Error", "Other Problem with creating the new Crime Record database file");
            } else if (resCode == 3) {
                displayWarning("Error", "SQL Problem with writing to the Crime Record database file");
            } else if (resCode == 4) {
                displayWarning("Error", "Other Problem with writing to the Crime Record database file");
            }
        }
    }


    /**
     * Allows a Alertbox to be shown for any message
     * @param header  The header of the alert box
     * @param message THe message shown to the user.
     */
    private void displayWarning(String header, String message) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alertBox.fxml"));
            Parent root = fxmlLoader.load();
            AlertBoxController alertBoxController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            alertBoxController.initData(header, message);
        } catch (Exception e) {
            //System.out.println(e.getMessage() + " : " + e.getStackTrace());
        }
    }


    /**
     * clear text field on click if the text equals Browse...
     */
    @FXML
    private void clearBrowse(){
        if (saveLocationText.getText().equals("Browse...")) {
            saveLocationText.setText("");
        }
    }


    /**
     * adds the word browse to text field if text is empty
     */
    @FXML
    private void addsBrowse(){
        if(saveLocationText.getText().equals("")){
            saveLocationText.setText("Browse...");
        }
    }
}
