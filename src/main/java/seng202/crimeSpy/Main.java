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

package seng202.crimeSpy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;
import seng202.crimeSpy.crimeData.SQLiteDBHandler;
import seng202.crimeSpy.exceptionHandling.ResourceNotFoundException;
import seng202.crimeSpy.uiElements.CrimeSpyController;

import java.io.IOException;
import java.net.URL;
import java.util.logging.*;


/**
 * A class to start the createNewCrimeCollectiondb program, all subprocesses and gui elements are launch and controlled from here
 */
public class Main extends Application {


    /**
     * LOGGER is the class wide instance of java.util.logging
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_MainClass";

    /**
     * A string of the latest version number of the application, used elsewhere in the system
     */
    public static final String currentVersion = "seng202.crimeSpy v0.3.2 - BETA";


    /**
     * Starts the database initializers and Starts the initalizer for the JavaFX GUI
     * Begins a logger
     * creates or opens the crime collection db and reads all exiting crimes
     * Iff there is a existing crime collection, opens the first one in the list
     * @param args optional arguments
     */
    public static void main(String[] args) {
        // initialise the logger for fileloc
        // initialise the logger for fileloc
        System.out.println("Crime Spy Application Started");
        Handler fileHandler;
        Formatter simpleFormatter;

        try {
            simpleFormatter = new SimpleFormatter();
            fileHandler  = new FileHandler(LOG_FILE_STORE_LOC);
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.INFO);
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.INFO);
            LOGGER.config("Configuration done.");
        } catch (IOException e) {
            LOGGER.warning("Failed to initiate file handler");
            LOGGER.setLevel(Level.SEVERE); // for case of console only
        }

        // Attempts to create a new crime collection list DB and reads what currently in there
        try {
            SQLiteDBHandler.createNewCrimeCollectiondb();
            SQLiteDBHandler.readCrimeCollections();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }

        // If there are existing crime collections known to crimespy, set current working collection to the first one and open it
        if (CrimeCollectionManager.getAllCrimeCollections().size() != 0) {
            //Load the first one
            CrimeCollectionManager.setCurrWorkingCollection(CrimeCollectionManager.getAllCrimeCollections().get(0));
        }


        // 3, 2, 1, Mission is a go!!!
        launch(args);
    }


    /**
     * The initializer for the JavaFX GUI.
     * Sets the window title, the size of the window, and the minimum dimensions of the window
     * @param primaryStage the primary stage to start and initalize
     * @throws Exception if the path to the resource is not found
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        String mainPath = "uiElements/crimeSpyUI.fxml";
        try {
            LOGGER.info("Looking in: " + getClass().getResource(mainPath));
            checkResource(mainPath);
        } catch (ResourceNotFoundException e) {
            LOGGER.severe("path not valid");
            System.exit(1);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("uiElements/crimeSpyUI.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(currentVersion);
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(640);
        primaryStage.show();
        CrimeSpyController crimeSpyController = fxmlLoader.getController();
        crimeSpyController.initializeController();
    }


    /**
     * Ensures that the crimeSPy UI resource exists
     * @param resourceLoc the location of the resource
     * @return the url of the resource
     * @throws ResourceNotFoundException if the resource cannot be found
     */
    public URL checkResource(String resourceLoc) throws ResourceNotFoundException {
        URL url = getClass().getResource(resourceLoc);
        if (url == null) {
            String cName = getClass().getName();
            int i = cName.lastIndexOf(".");
            if (i > -1) {
                cName = cName.substring(i + 1);
            }
            cName = cName + ".class";
            Object testPath = this.getClass().getResource(cName); // <-- can add in path but gets kinda clunky for output
            String err = "Could not find resource: " + resourceLoc;
            err += ", from class: " + cName;
            LOGGER.warning(err);
            LOGGER.warning("PATH: " + testPath);
            throw new ResourceNotFoundException(err);
        } else {
            return url;
        }
    }
}
