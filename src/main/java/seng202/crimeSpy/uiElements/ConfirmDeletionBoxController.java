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
import seng202.crimeSpy.crimeData.CrimeRecord;


/**
 * A custom confirmation box used for confirming if user wants to delete the selected crime record
 */
public class ConfirmDeletionBoxController {

    @FXML
    private Button cancelButton;

    @FXML
    private Label detailsLabel;

    @FXML
    private Label messageLabel;

    private CrimeRecord cR;


    /**
     * Initializes Data in this menu.
     */
    public void initData(CrimeRecord cR) {
        messageLabel.setText("Delete Crime Record");
        detailsLabel.setText("Are you sure your want to delete Crime Record " + cR.getCaseID() + ",\n This action cannot be undone");
        this.cR = cR;
    }


    /**
     * Hides the current menu form view and returns user to the main UI
     */
    @FXML
    private void goCancelButton() {
        Scene scene = cancelButton.getScene();
        if (scene != null) {
            Window window = scene.getWindow();
            if (window != null) {
                window.hide();
            }
        }
    }


    @FXML
    /**
     * Attempts to delete the current crime record - if successful then
     * hides the current menu form view and returns user to the main UI
     */
    private void goOKButton() {
        Integer resCode = CrimeCollectionManager.getFullWorkingCollection().removeCrimeRecord(cR);

        if (resCode == 0) {
            displayWarning("Success", "Record has been deleted");
        } else if (resCode == 1) {
            displayWarning("Error", "An unexpected error occurred, could not delete this record");
        }
        goCancelButton();
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
}
