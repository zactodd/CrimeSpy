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

package seng202.crimeSpy.crimeData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.*;
import java.util.zip.DataFormatException;


/**
 * Manages collections of crimes (CrimeCollection object)
 * Provides search and filter functionality.
 * Provides the ability to merge or split collections.
 * Keeps track of all caseId's seen across all CrimeCollections
 * At this stage we are not concerned if two
 * <b>separate</b>crime collections have the same caseId.
 */
public class CrimeCollectionManager {


    /**
     * LOGGER is the class wide instance of java.util.logging for CrimeCollectionManager
     * Logging to root directory: ./LOG_CrimeCollectionManager.xml
     */
    private static final Logger LOGGER = Logger.getLogger(CrimeCollectionManager.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_CrimeCollectionManager";

    // initialise the logger for fileloc
    private static Handler fileHandler;
    private static SimpleFormatter simpleFormatter;

    static {
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
    }

    private static CrimeCollection currWorkingCollection = null;
    private static ArrayList<CrimeCollection> allCrimeCollections = new ArrayList<CrimeCollection>(0);
    public static ObservableList<String> observableList = FXCollections.observableArrayList();
    private static CrimeCollection filteredCollection = currWorkingCollection;


    /**
     * Returns the current working crime collection with filters applied to it.
     * @return currWorkingCollection
     */
    public static CrimeCollection getCurrWorkingCollection() {
        return CrimeCollectionManager.filteredCollection;
    }


    /**
     * Returns the current working crime collection (no filters).
     * @return currWorkingCollection
     */
    public static CrimeCollection getFullWorkingCollection() {
        return CrimeCollectionManager.currWorkingCollection;
    }


    /**
     * Sets the current working collection. Assumes CrimeCollection has already been
     * added to working set.
     * Then opens the crime collection for display
     * @param workingCollection The new working collection
     */
    public static void setCurrWorkingCollection(CrimeCollection workingCollection) {
        CrimeCollectionManager.currWorkingCollection = workingCollection;
        CrimeCollectionManager.filteredCollection = currWorkingCollection;
        if (workingCollection.getCrimes().isEmpty()) {
            openKnownCrimeDB(workingCollection);
        }
    }


    /**
     * Sets the current working collection to the one with specified name. Assumes CrimeCollection has already been
     * added to working set.
     * Then opens the crime collection for display
     * @param workingCollection The name of the current working collection in string form
     */
    public static void setCurrWorkingCollection(String workingCollection) {
        for (CrimeCollection cc : CrimeCollectionManager.allCrimeCollections) {
            if (cc.getName().equals(workingCollection)) {
                CrimeCollectionManager.currWorkingCollection = cc;
                CrimeCollectionManager.filteredCollection = currWorkingCollection;
                if (cc.getCrimes().isEmpty()) {
                    openKnownCrimeDB(cc);
                }
            }
        }
    }


    /**
     * Adds a crime collection to the list of all crime collections and sets it as current
     * @param cc The new crime collection
     */
    public static void addCrimeCollection(CrimeCollection cc) {
        CrimeCollectionManager.allCrimeCollections.add(cc);
        CrimeCollectionManager.observableList.add(cc.getName());
        CrimeCollectionManager.currWorkingCollection = cc;
        CrimeCollectionManager.filteredCollection = cc;
    }


    /**
     * @return the number of crime collection known to the system
     */
    public static int getLength() {
        if (CrimeCollectionManager.allCrimeCollections == null) {
            return 0;
        } else {
            return CrimeCollectionManager.allCrimeCollections.size();
        }
    }


    /**
     * @return an Arraylist of all known crime collections
     */
    public static ArrayList<CrimeCollection> getAllCrimeCollections() {
        return CrimeCollectionManager.allCrimeCollections;
    }


    /**
     * Sets the current filtered collection
     * @param filteredCollection new filtered collection to be set to
     */
    public static void setFilteredCollection(CrimeCollection filteredCollection) {
        CrimeCollectionManager.filteredCollection = filteredCollection;
    }


    /**
     * Sets the current list of Crime Collections.
     * @param allCrimeCollections Crime Collections to be put into the system
     */
    public static void setAllCrimeCollections(ArrayList<CrimeCollection> allCrimeCollections) {
        CrimeCollectionManager.allCrimeCollections = allCrimeCollections;
    }

    /**
     * Adds the contents of the second CrimeCollection to the first CrimeCollection. The
     * first CrimeCollection will have already been loaded into the system. The second list is deleted
     * to free space
     * @param cC1 CrimeCollection 1 to be merged
     * @param cC2 CrimeCollection 2 to be merged. Will be altered
     * @return The first CrimeCollection with the contents of the second one that were successfully added to it.
     */
    public static CrimeCollection mergeCollections(CrimeCollection cC1, CrimeCollection cC2) {
        int index = 0;
        int arraySize = cC2.getCrimes().size();

        while (index < arraySize) {
                // Check for uniqueness of CrimeID's by trying to add it
            int res = cC1.addCrimeRecordtoDB(cC2.getCrimes().get(index));
            if (res == 0) {
                index++;
            } else if (res == 1) {
                // Non unique crimeID. If CrimeID is 19 characters long drop it as the database can't store it.
                if (cC2.getCrimes().get(index).getCaseID().length() >= 19) {
                    index++;

                // Else add a random letter/number to the CrimeID of the CrimeRecord to be appended and try add it again.
                } else {
                    String newID = cC2.getCrimes().get(index).getCaseID() + getRandomChar();
                    cC2.getCrimes().get(index).setCaseID(newID);
                }
            } else {
                String err;
                err = "Exception encountered while attempting to add crime record to a new crime collection";
                LOGGER.warning(err);
            }
        }
        cC2 = null; // Dispose of list
        return cC1;
    }


    /**
     * Generates a random number or character to put on the end of a non-unique crimeID
     * @return Random number or character
     */
    public static String getRandomChar() {
        Random r = new Random();
        String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char randomChar = alphabet.charAt(r.nextInt(36));
        return Character.toString(randomChar);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION
    // An improvement would be to abstract the common functionality of the validation methods
    //
    // Notes:
    //  - Date validation is not performed as all selection will be made from a date picker and time validation is in UI
    //  - Primary and secondary descriptions are not checked at this time as in most cases they
    //    will be auto filled based on th iucr code.
    //  - These methods are here as it makes more sense than having them passed up through the classes to
    //    CrimeCollectionManager and then to the gui
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * This method can be called on a complete iucr code or partial to
     * progressively provide feedback, for example as the user types in the iucr code
     * the key press could call this method with the field passed as the iucr parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid iucr.
     *
     * @param iucrIn String of proposed iucr
     * @return empty string if iucrIN is valid otherwise return a string desciption of the problem with it
     */
    public static String validateIUCR(String iucrIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        boolean gotMessage = false;
        String returnMessage = "";
        int length = iucrIn.length();
        // Illegal character
        Character c;
        for (int i = 0; i < length; i++) {
            c = iucrIn.charAt(i);
            if (!Character.isDigit(c) && !Character.isAlphabetic(c)) {
                returnMessage = "IUCR should be alphanumerical.";
                gotMessage = true;
            }
        }
        // Minimum length constraint
        if (!gotMessage && (iucrIn.length() < CrimeType.MIN_IUCR_LENGTH)) {
            returnMessage = "Minimum of " + CrimeType.MIN_IUCR_LENGTH + " characters.";
        }
        return returnMessage;
    }


    /**
     * This method can be called on a complete or partial ward number to
     * progressively provide feedback, for example as the user types in the ward number
     * the key press could call this method with the field passed as the parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid ward.<br>
     * Takes ward as a string as it could come from a non controlled text box
     * @param wardIn String of proposed ward
     * @return empty string if wardIn is valid otherwise return a string description of the problem with it
     */
    public static String validateWard(String wardIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        boolean gotMessage = false;
        String returnMessage = "";
        int length = wardIn.length();
        // Illegal character
        Character c;
        for (int i = 0; i < length; i++) {
            c = wardIn.charAt(i);
            if (!Character.isDigit(c)) {
                returnMessage = "Ward should be numerical.";
                gotMessage = true;
            }
        }
        // Minimum length constraint
        if (!gotMessage && (length < CrimeLocation.MIN_WARD_LENGTH)) {
            returnMessage = "Minimum of " + CrimeLocation.MIN_WARD_LENGTH + " digits.";
            gotMessage = true;
        }
        // Maximum length constraint
        if (!gotMessage && (length > CrimeLocation.MAX_WARD_LENGTH)) {
            returnMessage = "Maximum of " + CrimeLocation.MAX_WARD_LENGTH + " digits.";
            gotMessage = true;
        }
        return returnMessage;
    }


    /**
     * This method can be called on a complete or partial beat number to
     * progressively provide feedback, for example as the user types in the beat number
     * the key press could call this method with the field passed as the parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid beat.<br>
     * Takes beat as a string as it could come from a non controlled text box
     * @param beatIn String of proposed beat
     * @return empty string if beatIn is valid otherwise return a string description of the problem with it
     */
    public static String validateBeat(String beatIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        boolean gotMessage = false;
        String returnMessage = "";
        int length = beatIn.length();
        // Illegal character
        Character c;
        for (int i = 0; i < length; i++) {
            c = beatIn.charAt(i);
            if (!Character.isDigit(c)) {
                returnMessage = "A beat should be numerical.";
                gotMessage = true;
            }
        }
        // Minimum length constraint
        if (!gotMessage && (length < CrimeLocation.MIN_BEAT_LENGTH)) {
            returnMessage = "Minimum of " + CrimeLocation.MIN_BEAT_LENGTH + " digits.";
            gotMessage = true;
        }
        // Maximum length constraint
        if (!gotMessage && (length > CrimeLocation.MAX_BEAT_LENGTH)) {
            returnMessage = "Maximum of " + CrimeLocation.MAX_BEAT_LENGTH + " digits.";
            gotMessage = true;
        }
        return returnMessage;
    }


    /**
     * This method can be called on a complete or partial latitude to
     * progressively provide feedback, for example as the user types in the latitude
     * the key press could trigger this method with the field passed as the parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid latitude.<br>
     * Takes latitude as a string as it could come from a non controlled text box
     * @param latitudeIn String of proposed latitude
     * @return empty string if latitude is valid otherwise return a string description of the problem with it
     */
    public static String validateLatitude(String latitudeIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        boolean gotMessage = false;
        String returnMessage = "";

        // Illegal character - ensure we only have numerical input first
        Double latitudeAsDouble = 0.0;
        try {
            latitudeAsDouble = Double.parseDouble(latitudeIn);
        } catch (NullPointerException e) {
            returnMessage = "Please enter a valid latitude.";
            gotMessage = true;
        } catch (NumberFormatException e) {
            returnMessage = "The latitude must be a number.";
            gotMessage = true;
        }

        // Only for numerical input
        if (!gotMessage) {
            // Minimum latitude constraint
            if (latitudeAsDouble < CrimeLocation.MIN_LATITUDE) {
                returnMessage = "Latitude must greater than " + CrimeLocation.MIN_LATITUDE + ".";
                gotMessage = true;
            }

            // Maximum latitude constraint
            if (!gotMessage && (latitudeAsDouble > CrimeLocation.MAX_LATITUDE)) {
                returnMessage = "Latitude must be less than " + CrimeLocation.MAX_LATITUDE + ".";
                gotMessage = true;
            }
        }
        return returnMessage;
    }


    /**
     * This method can be called on a complete or partial longitude to
     * progressively provide feedback, for example as the user types in the longitude
     * the key press could trigger this method with the field passed as the parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid longitude.<br>
     * Takes longitude as a string as it could come from a non controlled text box
     * @param longitudeIn String of proposed longitude
     * @return empty string if longitude is valid otherwise return a string description of the problem with it.
     */
    public static String validateLongitude(String longitudeIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        boolean gotMessage = false;
        String returnMessage = "";

        // Illegal character - ensure we only have numerical input first
        Double longitudeAsDouble = 0.0;
        try {
            longitudeAsDouble = Double.parseDouble(longitudeIn);
        } catch (NullPointerException e) {
            returnMessage = "Please enter a valid longitude.";
            gotMessage = true;
        } catch (NumberFormatException e) {
            returnMessage = "The longitude must be a number.";
            gotMessage = true;
        }

        // Only for numerical input
        if (!gotMessage) {
            // Minimum latitude constraint
            if (longitudeAsDouble < CrimeLocation.MIN_LONGITUDE) {
                returnMessage = "Longitude must greater than " + CrimeLocation.MIN_LONGITUDE + ".";
                gotMessage = true;
            }

            // Maximum latitude constraint
            if (!gotMessage && (longitudeAsDouble > CrimeLocation.MAX_LONGITUDE)) {
                returnMessage = "Longitude must be less than " + CrimeLocation.MAX_LONGITUDE + ".";
                gotMessage = true;
            }
        }
        return returnMessage;
    }


    /**
     * This method can be called on a complete or partial hour to
     * progressively provide feedback, for example as the user types in the hour
     * the key press could trigger this method with the field passed as the parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid hour.<br>
     * Takes hour as a string as it could come from a non controlled text box
     * @param hourIn String of proposed longitude
     * @return empty string if hour is valid otherwise return a string description of the problem with it.
     */
    public static String validateHour(String hourIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        String returnMessage = "";

        if (hourIn.length() == 0) {
            returnMessage = ("The Hour must be entered");
            return returnMessage;
        }

        // Illegal character - ensure we only have numerical input first

        Character c;
        //Check all digits
        for (int i = 0; i < hourIn.length(); i++) {
            c = hourIn.charAt(i);
            if (!Character.isDigit(c)) {
                returnMessage = ("The Hour must be a digit");
                return returnMessage;
            }
        }
        //Check if within bounds

        if (0 > Integer.parseInt(hourIn) || Integer.parseInt(hourIn) > 23) {
            returnMessage = ("The Hour must be a digit between 0 and 24");
            return returnMessage;
        }

        return returnMessage;
    }


    /**
     * This method can be called on a complete or partial minute of an hour to
     * progressively provide feedback, for example as the user types in the minute
     * the key press could trigger this method with the field passed as the parameter
     * this method would then (keypress by keypress) provide hints as to how to enter a
     * valid minute.<br>
     * Takes minute of an hour as a string as it could come from a non controlled text box
     * @param minuteIn String of proposed longitude
     * @return empty string if minute is valid otherwise return a string description of the problem with it.
     */
    public static String validateMinute(String minuteIn) {
        // Only the most pertinent message is sent back.
        // If you are adding constraints note that they are ordered from most to least important
        String returnMessage = "";

        if (minuteIn.length() == 0) {
            returnMessage = ("The Minute must be entered");
            return returnMessage;
        }

        // Illegal character - ensure we only have numerical input first
        Character c;
        //Check all digits
        for (int i = 0; i < minuteIn.length(); i++) {
            c = minuteIn.charAt(i);
            if (!Character.isDigit(c)) {
                returnMessage = ("The Minute must be a digit");
                return returnMessage;
            }
        }
        //Check if within bounds

        if (0 > Integer.parseInt(minuteIn) || Integer.parseInt(minuteIn) > 59) {
            returnMessage = ("The Minute must be a digit between 0 and 59");
            return returnMessage;
        }

        return returnMessage;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPEN / IMPORT / EXPORT / SAVE
    // These functions primarily provide a bridge between the gui (user actions)
    // the DAL (data access layer) and the Model (objects that store the crime data
    // during runtime)
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Used in the open file menu for a known db file, populates a given crime collection with crime records from open
     * @param cc Crime collection to populate with open crime records
     * @return 0 for success, 1 for read sql error, 2 for other read error
     */
    public static int openKnownCrimeDB(CrimeCollection cc) {
        try {
            cc.populateCrimeRecords(SQLiteDBHandler.readCrimeRecords(cc.getDirectory()));
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 1;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }

        return 0;
    }


    /**
     * Used in the open file menu for a unknown db file, creates a crime collection with crime records from open
     * @param file a file that will be opened and read from
     * @return 0 for success, 1 for read sql error, 2 for other read error, 3 for write sql error, 4 for other write error
     */
    public static int openUnknownCrimeDB(File file) {
        String name = file.getName().replaceFirst(".db", "");
        CrimeCollection newCC = new CrimeCollection(name, getLength(), file.getAbsolutePath());
        CrimeCollectionManager.currWorkingCollection = newCC;

        try {
            newCC.populateCrimeRecords(SQLiteDBHandler.readCrimeRecords(file.getAbsolutePath()));
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 1;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }


        try {
            SQLiteDBHandler.writeNewCrimeCollection(newCC);
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 3;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 4;
        }

        addCrimeCollection(newCC);
        return 0;
    }


    /**
     * Imports a csv to a temp crime collection and appends it to current collection
     * @param file the csv file to import from
     * @return 0  is success, 3 is a csv data format exception, 4 if exception on csv read, 8 if merging failed
     * <ul>
     *     <li>0 - success, data imported and database created successfully</li>
     * <ul/>
     */
    public static Integer importNewCrimeDB_Append(File file) {
        // Retrieve data from CSV
        CrimeCollection mergeCC = new CrimeCollection();
        try {
            mergeCC.populateCrimeRecords(CSVFileHandler.readCrimeRecords(file.getAbsolutePath()));
        } catch (DataFormatException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 3;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 4;
        }

        try {
            mergeCollections(currWorkingCollection, mergeCC);
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 9;
        }
        return 0;
    }





    /**
     * Imports a csv to a new crime collection and sets it as current collection
     * First creates a crime database file, then populates it with read csv records, then writes those records to the database,
     * then writes the collection info to the database and finally adds it to the collection list
     * @param file the csv file to import and create a database file from
     * @return 0 if success,
     * 1 for SQL error when creating new crime record database, 2 for other error when creating new crime record database,
     * 3 is a csv data format exception, 4 if exception on csv read,
     * 5 for write records sql error, 6 for other write record error,
     * 7 for write collection sql error, 8 for other collection write error,
     */
    public static int importNewCrimeDB_Replace(File file) {
        String name = file.getName().replaceFirst(".csv", "");
        CrimeCollection newCC = new CrimeCollection(name, getLength(), file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 3) + "db");
        CrimeCollectionManager.currWorkingCollection = newCC;

        try {
            SQLiteDBHandler.createNewCrimedb(newCC.getDirectory());
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 1;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }

        try {
            newCC.populateCrimeRecords(CSVFileHandler.readCrimeRecords(file.getAbsolutePath()));
        } catch (DataFormatException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 3;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 4;
        }

        try {
            SQLiteDBHandler.writeCrimeRecords(newCC, newCC.getDirectory());
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 5;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 6;
        }

        try {
            SQLiteDBHandler.writeNewCrimeCollection(newCC);
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 7;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 8;
        }

        addCrimeCollection(newCC);

        return 0;
    }


    /**
     * Exports a the current working collection (with filters applied) to a csv file at a given location
     * @param saveLocation the save location and name of the file
     * @return 0 if success, 1 for error when writing crime records to csv
     */
    public static int exportCrimeCSV(String saveLocation) {
        try {
            CSVFileHandler.writeCrimeRecords(CrimeCollectionManager.getCurrWorkingCollection(), saveLocation);
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
        return 0;
    }


    /**
     * Saves the current crime collection to a database file in a user specified location
     * @param filtered if the save collection should be filtered or not
     * @param saveLocation the location on disk to save to
     * @return 0 if success, 1 if SQL exception on database creation, 2 if other exception on database creation,
     * 3 if SQL exception on crime record writing, 4 if other exception on crime record writing,
     */
    public static int saveCrimeDB(Boolean filtered, String saveLocation) {
        try {
            SQLiteDBHandler.createNewCrimedb(saveLocation);
        } catch (SQLException e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 1;
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            return 2;
        }

        if (filtered) {
            try {
                SQLiteDBHandler.writeCrimeRecords(getCurrWorkingCollection(), saveLocation);
            } catch (SQLException e) {
                LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
                return 3;
            } catch (Exception e) {
                LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
                return 4;
            }

        } else {
            try {
                SQLiteDBHandler.writeCrimeRecords(getFullWorkingCollection(), saveLocation);
            } catch (SQLException e) {
                LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
                return 3;
            } catch (Exception e) {
                LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
                return 4;
            }
        }
        return 0;
    }

}
