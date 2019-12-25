package seng202.unitTests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import seng202.crimeSpy.crimeData.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CSVFileHandlerTest {

    private File testFile1 = new File("TestFile.csv");
    private static File testCrimeDB_gen   = new File("TestCrimeDB_gen.db");

    @BeforeClass
    public static void setupDB () throws Exception{
        try {
            SQLiteDBHandler.createNewCrimedb(testCrimeDB_gen.getAbsolutePath());

        } catch (Exception e) {
            // Only create if it doesn't already exist
        }
    }

    @After
    public void tearDown() throws Exception {
        testFile1.delete();
        testCrimeDB_gen.delete();
    }

    @Test
    public void testWriteCrimeRecords_ReadCrimeRecords() throws Exception {
        CrimeCollection cc = new CrimeCollection("test", 0, testCrimeDB_gen.getAbsolutePath());
        CrimeCollectionManager.setCurrWorkingCollection(cc);
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

        ArrayList<CrimeRecord> expectedResults = new ArrayList<>();

        cc.addCrimeRecord(crime1);
        cc.addCrimeRecord(crime2);
        expectedResults.add(crime1);
        expectedResults.add(crime2);

        CSVFileHandler.writeCrimeRecords(cc, testFile1.getAbsolutePath());

        ArrayList<CrimeRecord> results = CSVFileHandler.readCrimeRecords(testFile1.getAbsolutePath());
        assertEquals("The resulting arraylist of crime records was not as expected", expectedResults.get(0).toString(), results.get(0).toString());
        assertEquals("The resulting arraylist of crime records was not as expected", expectedResults.get(1).toString(), results.get(1).toString());
    }


    @Test
    public void testReadIUCRcodes() throws Exception {
        ArrayList<String[]> result = CSVFileHandler.readIUCRcodes();

        //First Element
        String[] expected1 = {"110","HOMICIDE","FIRST DEGREE MURDER","I"};
        assertArrayEquals("The first element is incorrect", expected1, result.get(0));

        //Random element
        String[] expected2 = {"453","BATTERY","AGGRAVATED PO: OTHER DANG WEAP","I"};
        assertArrayEquals("The first element is incorrect", expected2, result.get(39));

        //Difficult value with quotation marks in it
        String[] expected3 = {"917","MOTOR VEHICLE THEFT","\"CYCLE, SCOOTER, BIKE W-VIN\"","I"};
        assertArrayEquals("The Difficult value with quotation marks is incorrect", expected3, result.get(101));

        //Last value
        String[] expected4 = {"5132","OTHER OFFENSE","VIOLENT OFFENDER: FAIL TO REGISTER NEW ADDRESS","N"};
        assertArrayEquals("The last element is incorrect", expected4, result.get(400));
    }
}