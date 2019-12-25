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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;

import java.io.File;


/**
* The Import Menu Dialog
* Allows the user to import contents of a specific file to the current Database
*/
public class ImportController {

    @FXML
    private RadioButton appendRadio;

    @FXML
    private RadioButton newRadio;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField importLocationText;

    @FXML
    private Button browseButton;


    /**
     * The file directory and location where the currently selected file is stored
     */
    private File file;


    /**
     * Initializes Data in this menu.
     */
    public void initData() {
        importLocationText.setText("Browse...");
        if (CrimeCollectionManager.getFullWorkingCollection() == null) {
            newRadio.setSelected(true);
            appendRadio.setDisable(true);
        }
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
     * enter the dialog for browsing and selecting the file
     */
    public void goBrowseButton() {
        Scene scene = browseButton.getScene();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Window window = scene.getWindow();
        file = fileChooser.showOpenDialog(window);
        if (file != null) {
            importLocationText.setText(file.getName());
        }
    }


    @FXML
    /**
     * Checks to selected .csv file from file chooser and attempts to read crime data into a CrimeCollection, which is
     * then added to the working set and made the working collection.
     */
    public void goImportButton() {
        Integer resCode;
        if (importLocationText.getText().equals("") || importLocationText.getText().equals("Browse...")) {
            displayWarning("Warning", "Import Location has not been set");
        } else {
            if (appendRadio.isSelected() && CrimeCollectionManager.getCurrWorkingCollection() != null) {  //Append
                resCode = CrimeCollectionManager.importNewCrimeDB_Append(file);

            } else {
                resCode = CrimeCollectionManager.importNewCrimeDB_Replace(file);
            }

            if (resCode == 0) {
                displayWarning("Success", "File has been successfully imported");
                goCancelButton();
            } else if (resCode == 4) {
                displayWarning("Error", "Problem with reading your csv");
            } else if (resCode == 5) {
                displayWarning("Error", "SQL Problem with writing to the Crime Record database file");
            } else if (resCode == 6) {
                displayWarning("Error", "Unknown problem during Crime Record database write");
            } else if (resCode == 7) {
                displayWarning("Error", "SQL Problem with writing to the Crime Collection database file");
            } else if (resCode == 8) {
                displayWarning("Error", "Unknown problem during Crime Collection database write");
            } else if (resCode == 1) {
                displayWarning("Error", "SQL error when creating a new crime record database file");
            } else if (resCode == 2) {
                displayWarning("Error", "SQL error when creating a new crime record database file");
            } else if (resCode == 3) {
                displayWarning("Error", "The csv file was in the wrong format");
            } else if (resCode == 9) {
                displayWarning("Error", "Problem merging the two collections");
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
        if (importLocationText.getText().equals("Browse...")) {
            importLocationText.setText("");
        }
    }


    /**
     * adds the word browse to text field if text is empty
     */
    @FXML
    private void addsBrowse(){
        if(importLocationText.getText().equals("")){
            importLocationText.setText("Browse...");
        }
    }
}
