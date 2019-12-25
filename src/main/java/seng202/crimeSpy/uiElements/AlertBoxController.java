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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;

/**
 * Alert and general message scene for user messages.
 * <p>This is used instead of the javafx alert as it is not
 * available to pre jdk 8.40
 * (which unfortunately as of Sep 2015 the lab machines at UC don't have)</p>
 */
public class AlertBoxController {

    @FXML
    private Label detailsLabel;

    @FXML
    private Button okButton;

    @FXML
    private Label messageLabel;


    /**
     * Initializes Data in this alert.
     * @param header The title of the message
     * @param message The message to be displayed
     */
    public void initData(String header, String message) {
        messageLabel.setText(header);
        detailsLabel.setText(message);
    }

    @FXML
    /**
     * Hides the current menu form view and returns user to the main UI
     */
    void goOKButton() {
        Scene scene = okButton.getScene();
        if (scene != null) {
            Window window = scene.getWindow();
            if (window != null) {
                window.hide();
            }
        }
    }
}
