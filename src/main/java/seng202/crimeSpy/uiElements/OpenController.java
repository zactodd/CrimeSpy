/*
    crimeSpy is a FOSS crime analysis software.
    Copyright (C) 2015 SENG Team Supreme

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package seng202.crimeSpy.uiElements;


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
import seng202.crimeSpy.crimeData.CrimeCollectionManager;

import java.io.File;

//import javafx.scene.control.Alert;


/**
 * The Open Menu Dialog
 * Allows the user to open a specific seng202.crimeSpy Database
 */
public class OpenController {

    private File selectedFile; // Crime DB file chosen

    @FXML
    private TextField openLocationText;

    @FXML
    private Button cancelButton;

    @FXML
    private Button browseButton;


    /**
     * Initializes Data in this menu.
     */
    public void initData() {
        openLocationText.setText("Browse...");
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
     * enter the dialog for browsing and selecting file
     */
    public void goBrowseButton() {
        Scene scene = browseButton.getScene();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DB Files", "*.db"));
        Window window = scene.getWindow();
        selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            openLocationText.setText(selectedFile.getAbsolutePath());
        }
    }


    /**
     * Checks the file chooser and tries to load the crime data in the CrimeDB into a CrimeCollection object, which
     * is then added to the CrimeCollectionManager and made the working collection.
     */
    public void goOpenButton() {

        // CHeck if a file is actually selected
        if (openLocationText.getText().equals("") || openLocationText.getText().equals("Browse...")) {
            displayWarning("Warning", "Open Location has not been set");

        } else {

            // Try reading file into CrimeCollection, fail if file not valid
            File dbFile = new File(selectedFile.getAbsolutePath());

            Integer resCode = CrimeCollectionManager.openUnknownCrimeDB(dbFile);
            if (resCode == 0) {
                displayWarning("Success", "File has been successfully opened");
                goCancelButton();
            } else if (resCode == 1) {
                displayWarning("Error", "Problem with accessing the Crime Record database file");
            }else if (resCode == 2) {
                displayWarning("Error", "Unknown problem with during file read");
            } else if (resCode == 3) {
                displayWarning("Error", "Problem with accessing the Crime Collection database file");
            } else if (resCode == 4) {
                displayWarning("Error", "Unknown problem with during file write");
            }
        }
    }


    /**
     * Allows a Alertbox to be shown for any message
     * @param header  The header of the alert box
     * @param message The message shown to the user.
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
     * clear text field on click if the text equals Browse..
     */
    @FXML
    private void clearBrowse(){
        if (openLocationText.getText().equals("Browse...")) {
            openLocationText.setText("");
        }
    }


    /**
     * adds the word browse to text field if text is empty
     */
    @FXML
    private void addsBrowse(){
        if(openLocationText.getText().equals("")){
            openLocationText.setText("Browse...");
        }
    }
}



