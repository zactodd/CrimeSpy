package crimeSpy.uiElements;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import crimeSpy.crimeData.CrimeCollectionManager;

import java.io.File;


/**
 * The Export Menu Dialog
 * Allows the user to export contents of the current database to a specific file.
 */
public class ExportController {

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
        saveLocationText.appendText("Enter save destination here...");
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
     * Enter the dialog for Browsing and selecting the file
     */
    public void goBrowseButton() {
        Scene scene = browseButton.getScene();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        Window window = scene.getWindow();
        File selectedFile = fileChooser.showSaveDialog(window);
        if (selectedFile != null) {
            saveLocationText.setText(selectedFile.getAbsolutePath()); //Saves the path of the selected file
        }
    }


    /**
     * First checks that all values have been entered and shows validation
     * Then calls the export Methods
     */
    public void goExportButton() {
        if (saveLocationText.getText().equals("") || saveLocationText.getText().equals("Enter save destination here...")) {
            displayWarning("Warning", "Save Location has not been set");
        } else {
            // Ensures correct suffix on save file name
            if (!saveLocationText.getText().substring(saveLocationText.getText().length() - 4).equals(".csv")) {
                saveLocationText.setText(saveLocationText.getText() + ".csv");
            }
            Integer resCode = CrimeCollectionManager.exportCrimeCSV(saveLocationText.getText());
            if (resCode == 0) {
                displayWarning("Warning", "Data successfully exported");
                goCancelButton();
            } else if (resCode == 1) {
                displayWarning("Error", "Could not export this data, unknown error occurred");
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