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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Window;
import seng202.crimeSpy.Main;


/**
 * The About Menu Dialog
 * Shows the user information about seng202.crimeSpy
 */
public class AboutController {

    @FXML
    private Label versionLabel;


    /**
     * Initializes the seng202.crimeSpy version number in this menu.
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