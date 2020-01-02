package crimeSpy.crimeData;


import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;


/**
 * A class to manage interactions between the crimeSpy system and the SQLite Database.
 * CollectionManager will use this to open/save/edit crime data to persistent storage.
 * Relies on: sqllite-jdbc.3.8.11.1.jar
 */
public class SQLiteDBHandler {


    /**
     * LOGGER is the class wide instance of java.util.logging
     */
    private static final Logger LOGGER = Logger.getLogger(SQLiteDBHandler.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_SQLiteDBHandler";

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



    private static HashMap<String, ArrayList<String>> iucrToCrimeType = new HashMap<String, ArrayList<String>>();


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Database WRITERS
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Write a single block of crime record data to persistent storage.
     * This method expects a CrimeRecord object.
     * @throws Exception crime record is not able to be written to the db
     * @param crime is a CrimeCollection object
     */
    public static void writeCrimeRecord(CrimeRecord crime) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
        c.setAutoCommit(false);
        LOGGER.info("Opened Crime Record database for single write successfully");

        stmt = c.createStatement();

        OR_Map cr = new OR_Map(crime);


        String sql = "SELECT * FROM CRIME_RECORD WHERE CRIME_RECORD_ID == '" + cr.getId() + "'";
        ResultSet resultSet = stmt.executeQuery(sql);
        if (!resultSet.next()) {
            sql = "INSERT INTO CRIME_RECORD (CRIME_RECORD_ID, CRIME_RECORD_DATE, CRIME_RECORD_BLOCK, " +
                    "CRIME_RECORD_LOCATION_DESCRIPTION, CRIME_RECORD_ARREST, CRIME_RECORD_DOMESTIC, " +
                    "CRIME_RECORD_BEAT, CRIME_RECORD_WARD, CRIME_RECORD_XCOORDINATE, CRIME_RECORD_YCOORDINATE, " +
                    "CRIME_RECORD_LATITUDE, CRIME_RECORD_LONGITUDE, CRIME_RECORD_LOCATIONSTR, PREV_CRIME_RECORD, " +
                    "NEXT_CRIME_RECORD, CRIME_TYPE_ID, CRIME_RECORD_FBICD) " +
                    "VALUES (" + cr.toDBCreateString() + ")";
            stmt.executeUpdate(sql);
            c.commit();
        }

        LOGGER.info("Record created successfully");
        stmt.close();
        c.close();
    }


    /**
     * Write a collection of crime data to persistent storage.
     * This method expects a CrimeCollection (collection object of CrimeRecords)
     * @throws Exception crime collection is not able to be written to the db
     * @param crimes is a CrimeCollection object
     */
    public static void writeCrimeRecords(CrimeCollection crimes, String location) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + location);
        c.setAutoCommit(false);
        LOGGER.info("Opened Crime Record database for collection write successfully");
        for (CrimeRecord crime : crimes.getCrimes()) {
            stmt = c.createStatement();

            OR_Map cr = new OR_Map(crime);

            String sql = "INSERT INTO CRIME_RECORD (CRIME_RECORD_ID, CRIME_RECORD_DATE, CRIME_RECORD_BLOCK, " +
                        "CRIME_RECORD_LOCATION_DESCRIPTION, CRIME_RECORD_ARREST, CRIME_RECORD_DOMESTIC, " +
                        "CRIME_RECORD_BEAT, CRIME_RECORD_WARD, CRIME_RECORD_XCOORDINATE, CRIME_RECORD_YCOORDINATE, " +
                        "CRIME_RECORD_LATITUDE, CRIME_RECORD_LONGITUDE, CRIME_RECORD_LOCATIONSTR, PREV_CRIME_RECORD, " +
                        "NEXT_CRIME_RECORD, CRIME_TYPE_ID, CRIME_RECORD_FBICD) " +
                        "VALUES (" + cr.toDBCreateString() + ")";
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                LOGGER.warning(crime.getCaseID() + " is a duplicate crime record, it was not added to the crime database");
                LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage() + crime.getCaseID());
            }
            stmt.close();
            LOGGER.info("Saved");
        }
        LOGGER.info("Commit");
        c.commit();
        LOGGER.info("Records created successfully");

        stmt.close();
        c.close();
    }


    /**
     * Writes a new crime collection to the list of known crime collection database files
     * @param crimes A crime collection object
     */
    public static void writeNewCrimeCollection(CrimeCollection crimes) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Crime_Collection_List.db");
        c.setAutoCommit(false);
        LOGGER.info("Opened Crime Collection database for write successfully");
        stmt = c.createStatement();

        String sql = "INSERT INTO CRIME_COLLECTION_LIST (CRIME_COLLECTION_ID, CRIME_COLLECTION_NAME, " +
                    "CRIME_COLLECTION_LOCATION) " +
                    "VALUES (" + crimes.getId() + ", '" + crimes.getName() + "', '" + crimes.getDirectory() + "')";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
        c.close();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Database READERS
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Read a collection of crime records from persistent storage and return an
     * equivalent CrimeCollection object consisting of CrimeRecord objects
     * @return a CrimeRecord object
     * @throws SQLException could not read from database
     */
    public static CrimeCollection readCrimeRecords() throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
        c.setAutoCommit(false);
        LOGGER.info("Opened database for getID successfully");

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CRIME_RECORD;");

        CrimeCollection results = new CrimeCollection();
        results.setDirectory(CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
        while (rs.next()) {
            results.addCrimeRecord(createTempCrimeRecord(rs));
        }
        rs.close();
        stmt.close();
        LOGGER.info("Read All done successfully");
        c.close();
        return results;
    }


    /**
     * Read a collection of crime records from persistent storage and return an
     * equivalent CrimeCollection object consisting of CrimeRecord objects
     * @return a CrimeRecord object
     * @param location Location of CrimeDB to read crime data from
     * @throws SQLException could not read from database
     */
    public static ArrayList<CrimeRecord> readCrimeRecords(String location) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + location);
        c.setAutoCommit(false);
        LOGGER.info("Opened database for getID successfully");

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CRIME_RECORD;");

        ArrayList<CrimeRecord> results = new ArrayList<CrimeRecord>();
        while (rs.next()) {
            //System.out.println(rs.getString("CRIME_RECORD_ID"));  //Reads all data and prints the ID to sys.out
            try {
                results.add(createTempCrimeRecord(rs));
            } catch (Exception e) {
                String err;
                err = "Exception encountered while attempting to add crime record to a new crime collection";
                LOGGER.info(err);
                LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
            }
        }
        rs.close();
        stmt.close();
        LOGGER.info("Read All done successfully");
        c.close();
        return results;
    }


    /**
     * Read a collection of crime records from persistent storage
     * @throws SQLException could not read from database
     */
    public static void readCrimeCollections() throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Crime_Collection_List.db");
        c.setAutoCommit(false);
        LOGGER.info("Opened database for collection read successfully");

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CRIME_COLLECTION_LIST;");
        while (rs.next()) {
            CrimeCollection cc;
            //System.out.println(rs.getString("CRIME_RECORD_ID"));  //Reads all data and prints the ID to sys.out
            cc = new CrimeCollection(rs.getString("CRIME_COLLECTION_NAME"),
                    rs.getInt("CRIME_COLLECTION_ID"),
                    rs.getString("CRIME_COLLECTION_LOCATION"));
            CrimeCollectionManager.addCrimeCollection(cc);
            LOGGER.info("Read All done successfully");
        }
        rs.close();
        stmt.close();
        c.close();

    }


    /**
     * Read a single crime record from persistent storage and return an
     * equivalent CrimeRecord object
     * @param crimeID A CrimeRecord ID
     * @return a CrimeRecord object
     * @throws SQLException unable to read the crime record from the database
     */
    public static CrimeRecord readCrimeRecord(String crimeID) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
        c.setAutoCommit(false);
        LOGGER.info("Opened database for getID successfully");

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CRIME_RECORD WHERE CRIME_RECORD_ID='" + crimeID + "';");
        CrimeRecord cr = createTempCrimeRecord(rs);
        rs.close();
        stmt.close();
        c.close();
        LOGGER.info("Read All done successfully");
        return cr;
    }


    /**
     * Remove all crime records from the current database
     * Not currently used - Untested
     * Design change now using new and append rather than replace and append
     */
    @Deprecated
    public static void deleteAllCrimeRecords() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
            c.setAutoCommit(false);
            LOGGER.info("Opened database for Delete successfully");

            stmt = c.createStatement();
            String sql = "DELETE FROM CRIME_RECORD;";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
            LOGGER.info("Delete done successfully");
        } catch (Exception e) {
            LOGGER.warning(e.getClass().getName() + ": " + e.getMessage());
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Database UPDATERS
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Deletes a single crime record object given a crimeID
     * @param crimeID the crime if of the crime record to be deleted
     * @throws SQLException
     */
    public static void deleteCrimeRecord(String crimeID) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
        LOGGER.info("Opened database for Deletion successfully");

        stmt = c.createStatement();
        stmt.executeUpdate("DELETE FROM CRIME_RECORD WHERE CRIME_RECORD_ID = '" + crimeID + "';");
        LOGGER.info("Deletion done successfully");
        stmt.close();
        c.close();
    }


    /**
     * Read a collection of crime records from persistent storage and return an
     * equivalent CrimeCollection object consisting of CrimeRecord objects
     */
    public static void editCrimeRecord(CrimeRecord crime) throws Exception {
        int exitCode = 1;
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + CrimeCollectionManager.getCurrWorkingCollection().getDirectory());
        c.setAutoCommit(false);
        LOGGER.info("Opened database for Update successfully");
        stmt = c.createStatement();
        OR_Map cr = new OR_Map(crime);
        String sql = "UPDATE CRIME_RECORD SET " + cr.toDBUpdateString() + " WHERE CRIME_RECORD_ID = '" + crime.getCaseID() + "';";
        stmt.executeUpdate(sql);
        c.commit();
        LOGGER.info("Operation done successfully");

        stmt.close();
        c.close();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Database INITIALIZER
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Creates a new empty crime collection database, a database in the working directory that
     * keeps track of the various crime databases imported to crimeSpy
     * @throws SQLException new Crime_Collection_List database was not able to be created
     */
    public static void createNewCrimeCollectiondb() throws Exception {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        // In the future we need to specify the working directory better here
        c = DriverManager.getConnection("jdbc:sqlite:Crime_Collection_List.db");
        LOGGER.info("Opened collection list successfully");
        createNewCrimeCollectionTable();
    }


    /**
     * creates the database structure, including all the tables inside the database.
     * @throws SQLException crime collection table could not be added to the database
     */
    private static void createNewCrimeCollectionTable() throws Exception{
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Crime_Collection_List.db");
        LOGGER.info("Opened database successfully");
        try {
            stmt = c.createStatement();

            String sql = "CREATE TABLE CRIME_COLLECTION_LIST " +
                    "(CRIME_COLLECTION_ID INT PRIMARY KEY     NOT NULL," +
                    " CRIME_COLLECTION_NAME           VARCHAR(100)    NOT NULL, " +
                    " CRIME_COLLECTION_LOCATION            VARCHAR(500)     NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            LOGGER.info("Crime collection table already exists");
        }
        LOGGER.info("collection list table created successfully");
        stmt.close();
        c.close();
    }


    /**
     * Creates a new empty crime database, a database currently in the working directory that
     * keeps track of the various crime records in the crime collection Database
     * @throws SQLException crime collection database could not be created
     */
    public static void createNewCrimedb(String location) throws Exception {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + location);
        LOGGER.info("Opened collection successfully");
        c.close();
        createNewCrimeTable(location);
    }


    /**
     * creates the database structure, including all the tables inside the database.
     * CRIME_TYPE table, CRIME_RECORD table
     * @throws SQLException tables could not be added
     */
    private static void createNewCrimeTable(String location) throws Exception {
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + location);
        LOGGER.info("Opened database successfully");

        stmt = c.createStatement();
        String sql = "";
        if (!tableExist("CRIME_TYPE", c)) {
            sql = "CREATE TABLE CRIME_TYPE " +
                    " (CRIME_TYPE_ID            VARCHAR(20) PRIMARY KEY     NOT NULL, " +
                    " CRIME_TYPE_NAME            VARCHAR(50)     NOT NULL, " +
                    " CRIME_TYPE_SECONDARY        VARCHAR(50)     NOT NULL)";
            stmt.executeUpdate(sql);
        }

        if (!tableExist("CRIME_RECORD", c)) {
            sql = "CREATE TABLE CRIME_RECORD " +
                    "(CRIME_RECORD_ID VARCHAR(20) PRIMARY KEY     NOT NULL," +
                    " CRIME_RECORD_DATE           VARCHAR(50), " +
                    " CRIME_RECORD_BLOCK            VARCHAR(50)     NOT NULL, " +
                    " CRIME_RECORD_LOCATION_DESCRIPTION        VARCHAR(50), " +
                    " CRIME_RECORD_ARREST        BOOLEAN, " +
                    " CRIME_RECORD_DOMESTIC        BOOLEAN, " +
                    " CRIME_RECORD_BEAT        INT, " +
                    " CRIME_RECORD_WARD        INT, " +
                    " CRIME_RECORD_XCOORDINATE        INT, " +
                    " CRIME_RECORD_YCOORDINATE        INT, " +
                    " CRIME_RECORD_LATITUDE        INT, " +
                    " CRIME_RECORD_LONGITUDE        INT, " +
                    " CRIME_RECORD_LOCATIONSTR        VARCHAR(50), " +
                    " PREV_CRIME_RECORD        INT, " +
                    " NEXT_CRIME_RECORD        INT, " +
                    " CRIME_TYPE_ID        VARCHAR(20)  NOT NULL, " +
                    " CRIME_RECORD_FBICD        VARCHAR(20), " +
                    " FOREIGN KEY(PREV_CRIME_RECORD) REFERENCES CRIME_RECORD(CRIME_RECORD_ID), " +
                    " FOREIGN KEY(NEXT_CRIME_RECORD) REFERENCES CRIME_RECORD(CRIME_RECORD_ID), " +
                    " FOREIGN KEY(CRIME_TYPE_ID) REFERENCES CRIME_TYPE(CRIME_TYPE_ID))";
            stmt.executeUpdate(sql);
        }

        populateIUCR(location);
        LOGGER.info("Crime table created successfully");
        stmt.close();
        c.close();
    }

    /**
     * Checks if a table with the name already exists.
     * @param tableName the name of the table being checked.
     * @param c the connection being checked.
     * @return True if the table exists otherwise false.
     * @throws SQLException
     */
    private static boolean tableExist(String tableName, Connection c) throws SQLException {
        DatabaseMetaData dbm = c.getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        return tables.next();
    }

    /**
     * Quick method to populate the crime record database with IUCR codes from a csv file
     * stored in the working directory
     * @param location the directory of the IUCR codes to populate
     * @throws SQLException could not populate the IUCR codes
     */
    private static void populateIUCR(String location) throws Exception {
        List<String[]> iucrCodes = CSVFileHandler.readIUCRcodes();
        Connection c = null;
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + location);
        c.setAutoCommit(false);

        LOGGER.info("Opened IUCR database for write successfully");
        for (String[] code : iucrCodes) {
            stmt = c.createStatement();
            String crimeId = code[0].replaceFirst("^0+(?!$)", "");
            String sql = "SELECT * FROM CRIME_TYPE WHERE CRIME_TYPE_ID == '" + crimeId + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            if (!resultSet.next()){
                sql = "INSERT INTO CRIME_TYPE (CRIME_TYPE_ID, CRIME_TYPE_NAME, CRIME_TYPE_SECONDARY) " +
                        "VALUES ('" + crimeId + "', '" + code[1] + "', '" + code[2] + "')";
                stmt.executeUpdate(sql);
            }
        }

        c.commit();
        populateIucrHashMap(location);
        LOGGER.info("IUCR codes populated successfully");
        c.close();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Database Function HELPERS
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets all of the CrimeType data from the CRIME_TYPE db in one go and puts it in a HashMap for more efficient
     * CrimeType retrieval
     */
    private static void populateIucrHashMap(String location) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + location);
            c.setAutoCommit(false);
            LOGGER.info("Populating IUCR HashMap");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CRIME_TYPE");

            while (rs.next()) {
                ArrayList<String> crimeType = new ArrayList<String>();
                String iucr = rs.getString("CRIME_TYPE_ID");
                crimeType.add(rs.getString("CRIME_TYPE_NAME"));
                crimeType.add(rs.getString("CRIME_TYPE_SECONDARY"));

                iucrToCrimeType.put(iucr, crimeType);
            }
            rs.close();
            stmt.close();
            c.close();
            LOGGER.info("Finished the hashmap");
        } catch (Exception e) {
            LOGGER.warning("Failure in populateIucrHashMap " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Gets the CrimeType data from the CrimeType hashmap, and populates that HashMap if that hasn't been done already
     * @param iucr The IUCR for which to retrieve the primary and secondary descriptions
     * @return An ArrayList of length 2 containing a primary and secondary description
     */
    public static ArrayList<String> getCrimeTypeData (String iucr) {
        ArrayList<String> result;
        if (iucrToCrimeType.size() == 0) {
            populateIucrHashMap(CrimeCollectionManager.getFullWorkingCollection().getDirectory());
        }
        if (iucrToCrimeType.get(iucr) == null) {
            String[] resArray = {"UNKNOWN", "UNKNOWN"};
            result = new ArrayList<String>(Arrays.asList(resArray));
        } else {
            result = iucrToCrimeType.get(iucr);
        }
        return result;
    }


    /**
     * a Method to quickly create a CrimeRecord from data retrieved from the database
     * @param rs the database record location
     * @return A CrimeRecord of the crime record from the db. null if error
     */
    private static CrimeRecord createTempCrimeRecord(ResultSet rs) {
        try {
            boolean arrest = rs.getInt("CRIME_RECORD_ARREST") == 1;
            boolean domestic = rs.getInt("CRIME_RECORD_DOMESTIC") == 1;
            CrimeRecord cr;
            cr = new CrimeRecord(rs.getString("CRIME_RECORD_ID"),               // caseID
                    DateTime.parse(rs.getString("CRIME_RECORD_DATE")),          // date
                    arrest,                                                     // arrest
                    domestic,                                                   // domestic
                    // CrimeType info
                    rs.getString("CRIME_TYPE_ID"),                              // iucr
                    rs.getString("CRIME_RECORD_FBICD"),                         // fbi cd
                    // CrimeLocation info
                    rs.getString("CRIME_RECORD_BLOCK"),                         // block
                    rs.getInt("CRIME_RECORD_BEAT"),                             // beat
                    rs.getInt("CRIME_RECORD_WARD"),                             // ward
                    rs.getDouble("CRIME_RECORD_XCOORDINATE"),                   // x-coordinate
                    rs.getDouble("CRIME_RECORD_YCOORDINATE"),                   // y co-ordinate
                    rs.getDouble("CRIME_RECORD_LATITUDE"),                      // latitude
                    rs.getDouble("CRIME_RECORD_LONGITUDE"),                     // longitude
                    rs.getString("CRIME_RECORD_LOCATION_DESCRIPTION")           // location description
            );
            return cr;
        } catch (Exception e) {
            LOGGER.warning(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }
}

