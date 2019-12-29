

package crimeSpy.crimeData;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;
import java.util.zip.DataFormatException;


/**
 * A class to manage interactions between the crimeSpy system and the various filetypes.
 * <u>Note:</u>
 * <p>The crime record constructor signature differs from the order of the csv.
 * This decision was made due to the fact that the csv ordering may change from
 * location to location or organisation to organisation, while the for our purposes
 * it makes more sense to group CrimeType information and CrimeLocation information
 * in the parameter order of the constructor. CSV parsing should be the only area of
 * confusion with respect to the ordering.<br>
 * Relevant comments are included in the code to assist with managing the differences
 * in order :)</p>
 * <p>The ordering of data in the csv (for Chicago crime data at least) is expected to be in the
 * following order:</p>
 * <ol>
 *     <li>caseID</li>
 *     <li>Date</li>
 *     <li>Block</li>
 *     <li>IUCR</li>
 *     <li>Primary Description</li>
 *     <li>Secondary Description</li>
 *     <li>Location (textual description)</li>
 *     <li>Arrest</li>
 *     <li>Domestic</li>
 *     <li>Beat</li>
 *     <li>Ward</li>
 *     <li>FBI CD</li>
 *     <li>X coordinate</li>
 *     <li>Y coordinate</li>
 *     <li>Latitude</li>
 *     <li>Longitude</li>
 *     <li>(Latitude, Longitude)</li>
 * </ol>
 * <p>More information can be found out about the IUCR codes
 * In <a href="https://www.isp.state.il.us/docs/6-260.pdf">this</a> document, or
 * <a href="https://data.cityofchicago.org/Public-Safety/Chicago-Police-Department-Illinois-Uniform-Crime-R/c7ck-438e">
 * here</a></p>
 */
public class CSVFileHandler {


    /**
     * LOGGER is the class wide instance of java.util.logging for CrimeCollectionManager
     * Logging to root directory: ./LOG_CSVFileHandler.xml
     */
    private static final Logger LOGGER = Logger.getLogger(CSVFileHandler.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_CSVFileHandler";

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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // make it accept empty strings
    // feel free to use an OR_Map for easy saving to csv format as well.
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Read a collection of crime records from persistent storage and return an
     * equivalent CrimeCollection object consisting of CrimeRecord objects.
     * Note the csv is expected to
     * @param location the file location to read from. Note that this file is expected to be in csv format
     * @return a CrimeCollection object
     * @throws java.io.FileNotFoundException The database could not be found
     * @throws IOException The database is corrupted
     */
    public static ArrayList<CrimeRecord> readCrimeRecords(String location) throws Exception {
        BufferedReader br = null;
        String line = "";
        ArrayList<CrimeRecord> results = new ArrayList<CrimeRecord>();
        br = new BufferedReader(new FileReader(location));
        String firstLine = br.readLine(); //Read the first header line and disregard, validation goes here
        String expectedFirstLine = "CASE#,DATE  OF OCCURRENCE,BLOCK, IUCR, PRIMARY DESCRIPTION, " +
                "SECONDARY DESCRIPTION, LOCATION DESCRIPTION,ARREST,DOMESTIC,BEAT,WARD,FBI CD,X COORDINATE," +
                "Y COORDINATE,LATITUDE,LONGITUDE,LOCATION";

        if (!firstLine.equals(expectedFirstLine)) {
            throw new DataFormatException("CSV didn't follow supported data format.");
        }

        while ((line = br.readLine()) != null) {
            // use comma as separator and ignore commas that have a odd number of quotes ahead of them
            while (line.contains(",,")) {
                line = line.replaceAll(",,", ",0,");
            }

            //////////////////////////////////////////////////////////////////////////////////
            //  The csv is expected to follow the format:
            //  [0]caseId, [1]date, [2]block, [3]iucr, [4]priDesc, [5]secDesc
            //  [6]location, [7]arrest, [8]domestic, [9]beat, [10]ward, [11]fbicd
            //  [12]xCoord, [13]yCoord, [14]latitude, [15]longitude, [16]latLong
            //  Where [x] is not in the string, just to assist with referencing :)

            String[] currLine = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            // See javaDoc for class as to why the array ordering is out of kilter
            CrimeRecord newcr;
            newcr = new CrimeRecord(currLine[0],                                                        // caseId
                    convertDateTime(currLine[1]),                                                       // date
                    (currLine[7].equals("Y")),                                                          // arrest
                    (currLine[8].equals("Y")),                                                          // domestic
                    // CrimeType info
                    currLine[3].replaceFirst("^0+(?!$)", ""),                                           // iucr
                    currLine[11],                                                                       // fbicd
                    // CrimeLocation info
                    currLine[2],                                                                        // block
                    Integer.parseInt(currLine[9]),                                                      // beat
                    Integer.parseInt(currLine[10]),                                                     // ward
                    Double.parseDouble(currLine[12]),                                                   // xCoord
                    Double.parseDouble(currLine[13]),                                                   // yCoord
                    Double.parseDouble(currLine[14]),                                                   // latitude
                    Double.parseDouble(currLine[15]),                                                   // longitude
                    currLine[6]                                                                         // location
            );
            results.add(newcr);

        }
        LOGGER.info("csv read");
        br.close();
        return results;
    }


    /**
     * Convert a string from csv to DateTime <i>safely</i>.
     * This should take into account the various problems with daylight savings...
     */
    private static DateTime convertDateTime(String date) {
        try {
            return DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime(date);
        } catch (Exception e) {
            // couldn't convert date time for some reason
            return DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("01/01/0001 01:00:00 AM"); //Placeholder Date Time
        }
    }


    /**
     * Write a collection of crime data to persistent storage.
     * This method expects a CrimeCollection (collection object of CrimeRecords)
     * @param crimes is a CrimeCollection object
     * @param location the file location for which to write the csv data
     */
    public static void writeCrimeRecords(CrimeCollection crimes, String location) throws Exception {
        FileWriter writer = new FileWriter(location);
        String expectedFirstLine = "CASE#,DATE  OF OCCURRENCE,BLOCK, IUCR, PRIMARY DESCRIPTION, " +
                "SECONDARY DESCRIPTION, LOCATION DESCRIPTION,ARREST,DOMESTIC,BEAT,WARD,FBI CD,X COORDINATE," +
                "Y COORDINATE,LATITUDE,LONGITUDE,LOCATION\n";
        writer.append(expectedFirstLine);
        for (CrimeRecord crime : crimes.getCrimes()) {
            OR_Map cr = new OR_Map(crime);
            writer.append(cr.toCSVString());
        }
        writer.flush();
        writer.close();
        LOGGER.info("CSV has been written");
    }


    /**
     * A short method that reads the IUCR codes from a csv file stored in the working directory.
     * @return a Arraylist of String[], each of a different IUCR code
     */
    public static ArrayList<String[]> readIUCRcodes() {
        BufferedReader br = null;
        ArrayList<String[]> result = new ArrayList<String[]>();
        try {
            br = new BufferedReader(new FileReader("IUCRcodes.csv"));
            String line = "";
            br.readLine(); //Read first title line and disregard
            while ((line = br.readLine()) != null) {
                String[] currLine = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                result.add(currLine);
            }
            LOGGER.info("IUCR csv read");
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
        return result;
    }
}


