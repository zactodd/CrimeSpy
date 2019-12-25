package seng202.unitTests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.*;
import org.junit.rules.TestName;
import seng202.crimeSpy.crimeData.CrimeCollection;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;
import seng202.crimeSpy.crimeData.CrimeRecord;
import seng202.crimeSpy.crimeData.SQLiteDBHandler;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class SQLiteDBHandlerTest {

    private File testCollectionDB1 = new File("TestCollectionDB.db");
    private File testCrimeDB1 = new File("TestCrimeDB1.db");
    private File testCrimeDB2 = new File("TestCrimeDB2.db");
    private File testCrimeDB3 = new File("TestCrimeDB3.db");
    private File testCrimeDB4 = new File("TestCrimeDB4.db");
    private File crimeCollectionList = new File("Crime_Collection_List.db");

    private static File testCrimeDB_gen   = new File("TestCrimeDB_gen.db");


    @Rule public TestName name = new TestName(); // For dealing with resources used by particular tests

    @BeforeClass
    public static void setupDB () throws Exception{
        try {
            SQLiteDBHandler.createNewCrimedb(testCrimeDB_gen.getAbsolutePath());
        } catch (Exception e) {
            // Only create if it doesn't already exist
        }
    }

    @Before
    public void setUp() throws Exception {
        crimeCollectionList.delete();
        testCollectionDB1.delete();
        testCrimeDB1.delete();
        testCrimeDB2.delete();
        testCrimeDB3.delete();
        testCrimeDB4.delete();

        CrimeCollection cc = new CrimeCollection("test",0,"TestCrimeDB1.db");
        CrimeCollectionManager.setCurrWorkingCollection(cc);
        SQLiteDBHandler.createNewCrimeCollectiondb();
        SQLiteDBHandler.createNewCrimedb(testCrimeDB1.getAbsolutePath());

        if (name.getMethodName().equals("testAddNewCrimeCollection_multipleDatabases")) {

            SQLiteDBHandler.createNewCrimedb(testCrimeDB2.getAbsolutePath());
            SQLiteDBHandler.createNewCrimedb(testCrimeDB3.getAbsolutePath());
            SQLiteDBHandler.createNewCrimedb(testCrimeDB4.getAbsolutePath());
        }
    }

    @After
    public void tearDown() throws Exception {
        testCollectionDB1.delete();
        crimeCollectionList.delete();
        testCrimeDB1.delete();
        testCrimeDB2.delete();
        testCrimeDB3.delete();
        testCrimeDB4.delete();


        if (name.getMethodName().equals("testAddNewCrimeCollection_multipleDatabases")) {
            File f3 = new File("TestCrimeDB1.db");
            File f4 = new File("TestCrimeDB2.db");
            File f5 = new File("TestCrimeDB3.db");
            f3.delete();
            f4.delete();
            f5.delete();
        }
    }


    /** Assumes that database already exists
     *
     */
    @Test
    public void test_ifTableWasCreatedFor_testCrimeDB_gen() throws Exception {
        Connection c = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:TestCrimeDB_gen.db");
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM CRIME_RECORD;");
        assertFalse("Newly created crime database had something in it when it should be empty", rs.isBeforeFirst());

        rs.close();
        stmt.close();
        c.close();
    }


    @Test
    public void testWriteandReadCrimeRecords_multiple() throws Exception {
        //Make CrimeRecords to add
        DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
        DateTime date02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00 PM");
        CrimeRecord crime1;
        CrimeRecord crime2;
        // Prepare objects
        crime1 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        crime2 = new CrimeRecord("HX111112", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeCollection cc = new CrimeCollection();
        cc.addCrimeRecord(crime1);
        cc.addCrimeRecord(crime2);

        //Write to database
        SQLiteDBHandler.writeCrimeRecords(cc, testCrimeDB1.getAbsolutePath());

        // Retrieve data and compare
        CrimeCollection currColl = new CrimeCollection();
        currColl.populateCrimeRecords(SQLiteDBHandler.readCrimeRecords(testCrimeDB1.getAbsolutePath()));
        assertEquals("Crime data retrieved from database is not the same what was submitted", cc.getCrimes().get(0).toString(), currColl.getCrimes().get(0).toString());
    }


    @Test
    public void testWriteandReadCrimeRecords_multiple2() throws Exception {
        //Make CrimeRecords to add
        DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
        DateTime date02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00 PM");
        CrimeRecord crime1;
        CrimeRecord crime2;
        // Prepare objects
        crime1 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        crime2 = new CrimeRecord("HX111112", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeCollection cc = new CrimeCollection("testDB", 2, testCrimeDB1.getAbsolutePath());

        cc.addCrimeRecord(crime1);
        cc.addCrimeRecord(crime2);

        //Write to database
        SQLiteDBHandler.writeCrimeRecords(cc, testCrimeDB1.getAbsolutePath());

        // Retrieve data and compare
        CrimeCollection currColl = SQLiteDBHandler.readCrimeRecords();
        assertEquals("Crime data retrieved from database is not the same what was submitted", cc.getCrimes().get(0).toString(), currColl.getCrimes().get(0).toString());
    }


    @Test
    public void testWriteandReadCrimeRecords_single() throws Exception {
        //Make CrimeRecords to add
        DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
        CrimeRecord crime1;
        // Prepare objects
        crime1 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeCollection cc = new CrimeCollection("TestCrimeDB1", 0, testCrimeDB1.getAbsolutePath());
        cc.addCrimeRecord(crime1);
        CrimeCollectionManager.setCurrWorkingCollection(cc);

        //Write to database
        SQLiteDBHandler.writeCrimeRecord(crime1);

        // Retrieve data and compare
        CrimeCollection currColl = new CrimeCollection();
        CrimeRecord crimeRecord = (SQLiteDBHandler.readCrimeRecord(crime1.getCaseID()));
        assertEquals("Crime data retrieved from database is not the same what was submitted", crime1.toString(), crimeRecord.toString());
    }


    @Test
    public void test_deleteCrimeRecords() throws Exception {
        DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
        CrimeRecord crime1;
        // Prepare objects
        crime1 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeCollection cc = new CrimeCollection("TestCrimeDB1", 0, testCrimeDB1.getAbsolutePath());
        cc.addCrimeRecord(crime1);
        CrimeCollectionManager.setCurrWorkingCollection(cc);

        //Write to database
        SQLiteDBHandler.writeCrimeRecord(crime1);

        SQLiteDBHandler.deleteCrimeRecord(crime1.getCaseID());
        CrimeRecord crimeRecord2 = (SQLiteDBHandler.readCrimeRecord(crime1.getCaseID()));
        assertNull("Crime data was not deleted from map", crimeRecord2);
    }


    @Test
    public void testEditCrimeRecord() throws Exception {

    }

    @Test
    public void testUpdateCrimeRecords() throws Exception {
        DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
        CrimeRecord crime1;
        // Prepare objects
        crime1 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeCollection cc = new CrimeCollection("TestCrimeDB1", 0, testCrimeDB1.getAbsolutePath());
        cc.addCrimeRecord(crime1);
        CrimeCollectionManager.setCurrWorkingCollection(cc);

        //Write to database
        SQLiteDBHandler.writeCrimeRecord(crime1);

        crime1.setArrest(false);

        SQLiteDBHandler.editCrimeRecord(crime1);
        CrimeRecord crimeRecord2 = (SQLiteDBHandler.readCrimeRecord(crime1.getCaseID()));
        assertFalse("Crime data was Edited", crimeRecord2.getArrest());
    }

    @Test
    public void testDeleteAll() throws Exception {
        //Make CrimeRecords to add
        DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
        DateTime date02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00 PM");
        CrimeRecord crime1;
        CrimeRecord crime2;
        // Prepare objects
        crime1 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        crime2 = new CrimeRecord("HX111112", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeCollection cc = new CrimeCollection("testDB", 2, testCrimeDB1.getAbsolutePath());

        cc.addCrimeRecord(crime1);
        cc.addCrimeRecord(crime2);
        CrimeCollectionManager.setCurrWorkingCollection(cc);

        //Write to database
        SQLiteDBHandler.writeCrimeRecords(cc, testCrimeDB1.getAbsolutePath());

        SQLiteDBHandler.deleteAllCrimeRecords();

        CrimeCollection currColl = SQLiteDBHandler.readCrimeRecords();
        assertEquals("All Crime data was not deleted from map", 0, currColl.getCrimes().size());
    }


    @Test
    public void testWrite_and_ReadCrimeCollection() throws Exception {
        CrimeCollection cc1 = new CrimeCollection("testDB2", 0, testCrimeDB2.getAbsolutePath());
        CrimeCollection cc2 = new CrimeCollection("testDB3", 1, testCrimeDB3.getAbsolutePath());
        assertEquals("Crime collection manager should be empty", 0, CrimeCollectionManager.getLength());

        SQLiteDBHandler.writeNewCrimeCollection(cc1);
        SQLiteDBHandler.writeNewCrimeCollection(cc2);

        SQLiteDBHandler.readCrimeCollections();
        assertEquals("Crime collection manager should have two crime collections", 2, CrimeCollectionManager.getLength());
    }

}
