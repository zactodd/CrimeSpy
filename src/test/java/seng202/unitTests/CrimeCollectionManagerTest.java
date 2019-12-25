package seng202.unitTests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import seng202.crimeSpy.crimeData.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
* Unit tests for CrimeCollectionManager class
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({SQLiteDBHandler.class, CSVFileHandler.class})
public class CrimeCollectionManagerTest {

    // For legitimate crime records
    private DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
    private DateTime date02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00 PM");
    private DateTime date03 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
    private DateTime date04 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00 PM");
    // These two sets of CR's are identical
    private CrimeRecord testCR_01;
    private CrimeRecord testCR_02;
    private CrimeRecord testCR_03;
    private CrimeRecord testCR_04;
    private CrimeRecord testCR_05;
    private CrimeRecord testCR_06;
    private CrimeRecord testCR_11;
    private CrimeRecord testCR_12;
    private CrimeRecord testCR_13;
    private CrimeRecord testCR_14;
    private CrimeRecord testCR_15;
    private CrimeRecord testCR_16;

    private static CrimeCollection testCC_01;
    private static CrimeCollection testCC_02;

    // Files to read IUCR codes from
    private static File testCrimeDB_1   = new File("TestCrimeDB_1.db");
    private static File testCrimeDB_2   = new File("TestCrimeDB_2.db");

    // Mocking
    private SQLiteDBHandler sqliteDBHandler = null;

    @BeforeClass
    public static void setupDB () throws Exception{
        testCrimeDB_1.delete();
        testCrimeDB_2.delete();
        try {
            SQLiteDBHandler.createNewCrimedb(testCrimeDB_1.getAbsolutePath());
            SQLiteDBHandler.createNewCrimedb(testCrimeDB_2.getAbsolutePath());
        } catch (Exception e) {
            // Only create if it doesn't already exist
        }
    }

    @Before
    public void setUp() throws Exception {
        testCR_01 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_02 = new CrimeRecord("HX111112", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_03 = new CrimeRecord("HX111113", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_04 = new CrimeRecord("HX111114", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_05 = new CrimeRecord("HX111115", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_06 = new CrimeRecord("HX111116", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_11 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_12 = new CrimeRecord("HX111112", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_13 = new CrimeRecord("HX111113", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_14 = new CrimeRecord("HX111114", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_15 = new CrimeRecord("HX111115", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_16 = new CrimeRecord("HX111116", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCC_01 = new CrimeCollection("Test1", 0, testCrimeDB_1.getAbsolutePath()); //empty CrimeCollection
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_02 = new CrimeCollection("Test2", 1, testCrimeDB_2.getAbsolutePath());

        // Reset CrimeCollectionManager
        CrimeCollectionManager.setAllCrimeCollections(new ArrayList<CrimeCollection>());
    }

    @After
    public void tearDown() throws Exception {
        testCrimeDB_1.delete();
        testCrimeDB_2.delete();
    }



    /**
     * Add to empty set. It should add the CrimeCollection automatically.
     */
    @Test
    public void testSetCurrWorkingCollection_empty() throws Exception {
        // Set working collection
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01); //TODO: failing because SQL trying to write to it

        assertEquals("Working collection wasn't set correctly", testCC_01, CrimeCollectionManager.getCurrWorkingCollection());
    }

    /**
     * Switch between multiple sets, and make sure no CC's are duplicated
     */
    @Test
    public void testSetCurrWorkingCollection_switchMultipleTimes() throws Exception {
        CrimeCollection testCC_02 = new CrimeCollection();
        CrimeCollection testCC_03 = new CrimeCollection();

        CrimeCollectionManager.addCrimeCollection(testCC_01);
        CrimeCollectionManager.addCrimeCollection(testCC_02);
        CrimeCollectionManager.addCrimeCollection(testCC_03);

        CrimeCollectionManager.setCurrWorkingCollection(testCC_01); //TODO: Faililng because SQL trying to write to it but has no DB
        assertEquals("Working collection didn't switch correctly", testCC_01, CrimeCollectionManager.getCurrWorkingCollection());
        CrimeCollectionManager.setCurrWorkingCollection(testCC_02);
        assertEquals("Working collection didn't switch correctly", testCC_02, CrimeCollectionManager.getCurrWorkingCollection());
        CrimeCollectionManager.setCurrWorkingCollection(testCC_03);
        assertEquals("Working collection didn't switch correctly", testCC_03, CrimeCollectionManager.getCurrWorkingCollection());
    }

    @Test
    public void testAddCrimeCollection_addOneToEmpty() throws Exception {
        CrimeCollectionManager.addCrimeCollection(testCC_01);

        assertEquals("Couldn't find added CrimeCollection in the manager", testCC_01, CrimeCollectionManager.getAllCrimeCollections().get(0));
    }

    @Test
    public void testGetLength_empty() throws Exception {
        assertEquals("Empty working set wasn't of size 0", 0, CrimeCollectionManager.getLength());
    }

    @Test
    public void testGetLength_multiple() throws Exception {

        CrimeCollection testCC_02 = new CrimeCollection();
        CrimeCollection testCC_03 = new CrimeCollection();

        CrimeCollectionManager.addCrimeCollection(testCC_01);
        CrimeCollectionManager.addCrimeCollection(testCC_02);
        CrimeCollectionManager.addCrimeCollection(testCC_03);

        assertEquals("Size of working set wasn't correct", 3, CrimeCollectionManager.getLength());
    }

    /**
     * See if all collections are returned properly
     */
    @Test
    public void testGetAllCrimeCollections_nonePresent() throws Exception {
        assertEquals("Empty working set wasn't returned", new ArrayList<CrimeCollection>(0), CrimeCollectionManager.getAllCrimeCollections());
    }

    /**
     * See if all collections are returned properly
     */
    @Test
    public void testGetAllCrimeCollections_multiplePresent() throws Exception {
        CrimeCollection testCC_02 = new CrimeCollection();
        CrimeCollection testCC_03 = new CrimeCollection();

        CrimeCollectionManager.addCrimeCollection(testCC_01);
        CrimeCollectionManager.addCrimeCollection(testCC_02);
        CrimeCollectionManager.addCrimeCollection(testCC_03);

        ArrayList<CrimeCollection> result = CrimeCollectionManager.getAllCrimeCollections();

        assertEquals("1st CC wasn't correct", testCC_01, result.get(0));
        assertEquals("2nd CC wasn't correct", testCC_02, result.get(1));
        assertEquals("3rd CC wasn't correct", testCC_03, result.get(2));
    }

    /**
     * Test a valid address for a database file
     * @throws Exception
     */
    @Test
    public void testOpenUnknownCrimeDB_validAddress() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        File dbFile = new File("test/directory/crimedata.db");
        PowerMockito.when(SQLiteDBHandler.readCrimeRecords(Mockito.anyString())).thenReturn(testCC_01.getCrimes());

        // Execute
        int errCode = CrimeCollectionManager.openUnknownCrimeDB(dbFile);

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
        assertEquals("Collection wasn't added", dbFile.getAbsolutePath(), CrimeCollectionManager.getAllCrimeCollections().get(0).getDirectory());
    }

    public void testOpenKnownCrimeDB_validAddress() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        testCC_01.setDirectory("test/directory/crimedata.db");
        PowerMockito.when(SQLiteDBHandler.readCrimeRecords(Mockito.anyString())).thenReturn(testCC_01.getCrimes());

        // Execute
        int errCode = CrimeCollectionManager.openKnownCrimeDB(testCC_01);

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
        assertEquals("Collection wasn't added", testCC_01.getDirectory(), CrimeCollectionManager.getAllCrimeCollections().get(0).getDirectory());
    }

    @Test
    public void testImportNewCrimeDB_Append_validAddress() throws Exception {
        // Prepare
        PowerMockito.mockStatic(CSVFileHandler.class);
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        testCC_01.setDirectory("test/directory/crimedata.db");
        CrimeCollectionManager.addCrimeCollection(testCC_01);
        File csvFile = new File("test/directory/crimedata.csv");
        PowerMockito.when(CSVFileHandler.readCrimeRecords(Mockito.anyString())).thenReturn(testCC_01.getCrimes());

        // Execute
        int errCode = CrimeCollectionManager.importNewCrimeDB_Append(csvFile);

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
        assertEquals("Crime Collection was added instead of merged", 1, CrimeCollectionManager.getAllCrimeCollections().size());
        assertEquals("Collection wasn't added", "test/directory/crimedata.db", CrimeCollectionManager.getAllCrimeCollections().get(0).getDirectory());
    }

    @Test
    public void testImportNewCrimeDB_Replace_validAddress() throws Exception {
        // Prepare
        PowerMockito.mockStatic(CSVFileHandler.class);
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        File csvFile1 = new File("test/directory/crimedata.db");
        File csvFile2 = new File("test/directory/crimedatadb");
        PowerMockito.when(CSVFileHandler.readCrimeRecords(Mockito.anyString())).thenReturn(testCC_01.getCrimes());

        // Execute
        int errCode = CrimeCollectionManager.importNewCrimeDB_Replace(csvFile1);

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
        assertEquals("Collection wasn't added", csvFile2.getAbsolutePath(), CrimeCollectionManager.getAllCrimeCollections().get(0).getDirectory());
    }

    @Test
    public void testExport_validAddress() throws Exception {
        // Prepare
        PowerMockito.mockStatic(CSVFileHandler.class);
        File saveLocation = new File("test/directory/crimedata.db");

        // Execute
        int errCode = CrimeCollectionManager.exportCrimeCSV(saveLocation.getAbsolutePath());

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
    }

    @Test
    public void saveCrimeDB_validAddressAndNotFiltered() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        File saveLocation = new File("test/directory/crimedata.db");

        // Execute
        int errCode = CrimeCollectionManager.saveCrimeDB(false, saveLocation.getAbsolutePath());

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
    }

    /**
     * Filter
     * @throws Exception
     */
    @Test
    public void saveCrimeDB_validAddressAndFiltered() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        File saveLocation = new File("test/directory/crimedata.db");

        // Execute
        int errCode = CrimeCollectionManager.saveCrimeDB(true, saveLocation.getAbsolutePath());

        // Test
        assertEquals("Error code was not as expected", 0, errCode);
    }

    /**
     * Test for merging two empty CC's
     */
    @Test
    public void testMergeCollections_bothEmpty() {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_02 is empty
        CrimeCollection testCC_03 = new CrimeCollection();
        CrimeCollection resultCC = CrimeCollectionManager.mergeCollections(testCC_02, testCC_03);
        assertEquals("Merging of two empty CC's didn't result in an empty collection", new ArrayList<CrimeRecord>(0), testCC_02.getCrimes());
    }

    /**
     * Test with just the first one containing data and the second one is empty
     */
    @Test
    public void testMergeCollections_onlyCC2Empty() {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_01 has two records in it
        // testCC_02 is empty
        ArrayList<CrimeRecord> expectedSet = testCC_01.getCrimes();

        CrimeCollection resultCC = CrimeCollectionManager.mergeCollections(testCC_01, testCC_02);
        for (int i = 0; i < expectedSet.size(); i++) {
            assertEquals("Merging of an empty set into a non-empty one changes it, when it shouldn't", expectedSet.get(i), testCC_01.getCrimes().get(i));
        }
    }

    /**
     * Test with just the second one containing data and the second one has data
     */
    @Test
    public void testMergeCollections_onlyCC1Empty() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_01 has two records in it
        // testCC_02 is empty
        ArrayList<CrimeRecord> expectedSet = testCC_01.getCrimes();

        CrimeCollection resultCC = CrimeCollectionManager.mergeCollections(testCC_02, testCC_01);
        for (int i = 0; i < expectedSet.size(); i++) {
            assertEquals("Merging of an non-empty set into an empty one changes it incorrectly", expectedSet.get(i), testCC_02.getCrimes().get(i));
        }
    }

    /**
     * Test with two, non-empty CC's with unique data
     */
    @Test
    public void testMergeCollections_bothUniqueData() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_01 has two records in it
        testCC_02.addCrimeRecord(testCR_03);
        testCC_02.addCrimeRecord(testCR_05);

        ArrayList<CrimeRecord> expectedSet = testCC_01.getCrimes();
        expectedSet.add(testCR_03);
        expectedSet.add(testCR_05);

        CrimeCollection resultCC = CrimeCollectionManager.mergeCollections(testCC_01, testCC_02);
        for (int i = 0; i < expectedSet.size(); i++) {
            assertEquals("Merging of two, non-empty sets done incorrectly", expectedSet.get(i), testCC_01.getCrimes().get(i));
        }
    }


    /**
     * Test with two, non-empty CC's with one non-unique data
     */
    @Test
    public void testMergeCollections_oneNonUnique() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_01 has two records in it
        testCC_02.addCrimeRecord(testCR_03);
        testCC_02.addCrimeRecord(testCR_11); //testCR_11 is the same as test_CR01 in testCC_01

        ArrayList<CrimeRecord> expectedSet = testCC_01.getCrimes();
        expectedSet.add(testCR_03);

        CrimeCollection resultCC = CrimeCollectionManager.mergeCollections(testCC_01, testCC_02);
        assertNotEquals("Non unique CR didn't have char appended to it", "HX111111", testCC_01.getCrimes().get(4));
        assertEquals("Non unique CR didn't have just one char appended to it", 9, testCC_01.getCrimes().get(4).getCaseID().length());
    }

    /**
     * Test with two, non-empty CC's with all non-unique data
     */
    @Test
    public void testMergeCollections_allNonUnique() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_01 has two records in it
        testCC_01.addCrimeRecord(testCR_03);
        testCC_01.addCrimeRecord(testCR_04);
        testCC_01.addCrimeRecord(testCR_05);
        testCC_01.addCrimeRecord(testCR_06);

        testCC_02.addCrimeRecord(testCR_11);
        testCC_02.addCrimeRecord(testCR_12);
        testCC_02.addCrimeRecord(testCR_13);
        testCC_02.addCrimeRecord(testCR_14);
        testCC_02.addCrimeRecord(testCR_15);
        testCC_02.addCrimeRecord(testCR_16);

        CrimeCollection resultCC = CrimeCollectionManager.mergeCollections(testCC_01, testCC_02);

        assertEquals("Not all records were added", 12, testCC_01.getCrimes().size());

        // Test all resulting records which should have been modified
        String[] originalID = {testCR_01.getCaseID(),testCR_02.getCaseID(),testCR_03.getCaseID(),testCR_04.getCaseID(),testCR_05.getCaseID(),testCR_06.getCaseID()};
        for (int i = 6; i < 12; i++) {
            assertNotEquals("Non unique CR didn't have char appended to it", originalID[i-6], testCC_01.getCrimes().get(i));
            assertEquals("Non unique CR " + i + " didn't have just one char appended to it", 9, testCC_01.getCrimes().get(i).getCaseID().length());
        }
    }

    /**
     * Tries adding 11 records of the same unique id, which the system should handle.
     */
    @Test
    public void testMergeCollections_pushLimit() throws Exception {
        // Prepare
        PowerMockito.mockStatic(SQLiteDBHandler.class);

        // testCC_01 has two records in it
        testCC_02.addCrimeRecord(testCR_11);

        int limit = 11;
        int i = 0;
        while (i < limit) {
            testCC_01 = CrimeCollectionManager.mergeCollections(testCC_01, testCC_02);
            i++;
        }
        assertEquals("Not all records were added", limit+2, testCC_01.getCrimes().size());
    }

    @Test
    public void testDeleteFromCurrentCollection() throws Exception {

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (Iucr validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * A valid iucr code should return an empty string.
     */
    @Test
    public void test_validateIUCR_valid() {
        String errMsg = "A valid iucr code should return an empty string";
        assertEquals(errMsg, "", CrimeCollectionManager.validateIUCR("400"));
    }


    /**
     * A short iucr code should not return an empty string.
     */
    @Test
    public void test_validateIUCR_short() {
        String errMsg = "A short iucr code should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateIUCR("4").length() > 0);
    }


    /**
     * A short iucr code that contains illegal characters should
     * warn about the illegal characters first.
     * Note that the only way to test this error message is by string
     * comparison. If this test is failing then please check that the
     * error message string has not been modified in the CrimeType class.
     */
    @Test
    public void test_validateIUCR_shortAndIllegal() {
        String errMsg = "A short iucr code that also contains illegal character should warn about illegal characters first.";
        assertTrue(errMsg, CrimeCollectionManager.validateIUCR("@").length() > 0);
        assertEquals(errMsg, CrimeCollectionManager.validateIUCR("@"), "IUCR should be alphanumerical.");
    }


    /**
     * An iucr code with an illegal character but of a long
     * enough length should not an empty string.
     */
    @Test
    public void test_validateIUCR_illegalChars() {
        String errMsg = "An iucr code with an illegal character should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateIUCR("%400").length() > 0);
        assertTrue(errMsg, CrimeCollectionManager.validateIUCR("40%0").length() > 0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (ward validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A valid ward code should return an empty string.
     */
    @Test
    public void test_validateWard_valid() {
        String errMsg = "A valid ward should return an empty string";
        assertEquals(errMsg, "", CrimeCollectionManager.validateWard("400"));
    }

    /**
     * A valid ward number can only contain digits.<br>
     * CrimeCollectionManager.validateWard() should return a non empty string error message
     * if the ward parameter contains characters other than digits.
     */
    @Test
    public void test_validateWard_invalidInput() {
        String errMsg = "An invalid ward should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateWard("4a0").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateWard() should return a non empty string error message
     * if the ward parameter is too long.
     */
    @Test
    public void test_validateWard_inputTooLong() {
        String errMsg = "A ward that is too long should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateWard("42341111000").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateWard() should return an empty string
     * if the ward parameter is the maximum length.
     */
    @Test
    public void test_validateWard_inputMaxLen() {
        String errMsg = "A ward of maximum length should return an empty string";
        StringBuffer wardBuff = new StringBuffer();
        //Construct string of max length (defined in CrimeLocation class)
        for (int i = 0; i < CrimeLocation.MAX_WARD_LENGTH; i++) {
            wardBuff.append('1');
        }
        String ward = wardBuff.toString();
        assertEquals(errMsg, CrimeCollectionManager.validateWard(ward), "");
    }


    /**
     * CrimeCollectionManager.validateWard() should return an empty string
     * if the ward parameter is the minimum length.
     */
    @Test
    public void test_validateWard_inputMinLen() {
        String errMsg = "A ward of minimum length should return an empty string";
        StringBuffer wardBuff = new StringBuffer();
        //Construct string of max length (defined in CrimeLocation class)
        for (int i = 0; i < CrimeLocation.MIN_WARD_LENGTH; i++) {
            wardBuff.append('1');
        }
        String ward = wardBuff.toString();
        assertEquals(errMsg, CrimeCollectionManager.validateWard(ward), "");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (beat validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A valid beat code should return an empty string.
     */
    @Test
    public void test_validateBeat_valid() {
        String errMsg = "A valid beat should return an empty string";
        assertEquals(errMsg, "", CrimeCollectionManager.validateBeat("444"));
    }

    /**
     * A valid beat number can only contain digits.<br>
     * CrimeCollectionManager.validateBeat() should return a non empty string error message
     * if the beat parameter contains characters other than digits.
     */
    @Test
    public void test_validateBeat_invalidInput() {
        String errMsg = "An invalid beat should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateBeat("4a0").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateBeat() should return a non empty string error message
     * if the beat parameter is too long.
     */
    @Test
    public void test_validateBeat_inputTooLong() {
        String errMsg = "A beat that is too long should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateBeat("42341111000").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateBeat() should return an empty string
     * if the beat parameter is the maximum length.
     */
    @Test
    public void test_validateBeat_inputMaxLen() {
        String errMsg = "A beat of maximum length should return an empty string";
        StringBuffer beatBuff = new StringBuffer();
        //Construct string of max length (defined in CrimeLocation class)
        for (int i = 0; i < CrimeLocation.MAX_BEAT_LENGTH; i++) {
            beatBuff.append('1');
        }
        String beat = beatBuff.toString();
        assertEquals(errMsg, CrimeCollectionManager.validateBeat(beat), "");
    }


    /**
     * CrimeCollectionManager.validateBeat() should return an empty string
     * if the beat parameter is the minimum length.
     */
    @Test
    public void test_validateBeat_inputMinLen() {
        String errMsg = "A beat of minimum length should return an empty string";
        StringBuffer beatBuff = new StringBuffer();
        //Construct string of max length (defined in CrimeLocation class)
        for (int i = 0; i < CrimeLocation.MIN_BEAT_LENGTH; i++) {
            beatBuff.append('1');
        }
        String beat = beatBuff.toString();
        assertEquals(errMsg, CrimeCollectionManager.validateBeat(beat), "");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (longitude validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * CrimeCollectionManager.validateLatitude() should allow
     * the correct numerical input
     */
    @Test
    public void test_validateLatitude_inputValid() {
        String errMsg = "A valid input should be validated as correct.";
        String latitude = "66.999";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() == 0);
    }


    /**
     * CrimeCollectionManager.validateLatitude() should only allow
     * numerical input
     */
    @Test
    public void test_validateLatitude_inputLCaseLetter() {
        String errMsg = "A latitude must be numerical";
        String latitude = "11h";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() > 0);
    }


    /**
     * CrimeCollectionManager.validateLatitude() should only allow
     * numerical input
     */
    @Test
    public void test_validateLatitude_inputUCaseLetter() {
        String errMsg = "A latitude must be numerical";
        String latitude = "1Z1";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() > 0);
    }


    /**
     * CrimeCollectionManager.validateLatitude() should only allow
     * numerical input
     */
    @Test
    public void test_validateLatitude_inputOtherChar() {
        String errMsg = "A latitude must be numerical";
        String latitude = "#11";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() > 0);
    }



    /**
     * CrimeCollectionManager.validateLatitude() should only allow
     * numerical input greater than the minimum allowed latitude (-90 degrees)
     */
    @Test
    public void test_validateLatitude_minTest() {
        String errMsg = "A latitude should not be valid if it is below -90.0";
        String latitude = "-90.001";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() > 0);
    }


    /**
     * This validates the validation of test_validateLatitude_minTest
     * by checking that the same test fails if a latitude greater than
     * the minimum is checked
     */
    @Test
    public void test_validateLatitude_minTest_Test() {
        String errMsg = "A latitude should not be valid if it is below -90.0";
        String latitude = "-89.999";
        try {
            assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() > 0);
            fail();
        } catch (AssertionError e) {
            // yay...balloons
        }
    }


    /**
     * CrimeCollectionManager.validateLatitude() should allow
     * numerical input that is bang on -90 degrees (south pole)
     */
    @Test
    public void test_validateLatitude_negative90() {
        String errMsg = "A latitude should be valid if it is -90.0 exactly";
        String latitude = "-90.00000";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() == 0);
    }


    /**
     * CrimeCollectionManager.validateLatitude() should allow
     * numerical input that is bang on 90 degrees (north pole)
     */
    @Test
    public void test_validateLatitude_Positive90() {
        String errMsg = "A latitude should be valid if it is -90.0 exactly";
        String latitude = "90.00000";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() == 0);
    }


    /**
     * CrimeCollectionManager.validateLatitude() should not allow
     * numerical input that is greater than 90 degrees
     */
    @Test
    public void test_validateLatitude_MaxTest() {
        String errMsg = "A latitude should be valid if it is less than 90.0 degrees";
        String latitude = "90.00001";
        assertTrue(errMsg, CrimeCollectionManager.validateLatitude(latitude).length() > 0);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (latitude validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * CrimeCollectionManager.validateLongitude() should allow
     * the correct numerical input
     */
    @Test
    public void test_validateLongitude_inputValid() {
        String errMsg = "A valid input should be validated as correct.";
        String longitude = "126.999";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() == 0);
    }


    /**
     * CrimeCollectionManager.validateLongitude() should only allow
     * numerical input
     */
    @Test
    public void test_validateLongitude_inputLCaseLetter() {
        String errMsg = "A longitude must be numerical";
        String longitude = "1a1";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() > 0);
    }


    /**
     * CrimeCollectionManager.validateLongitude() should only allow
     * numerical input
     */
    @Test
    public void test_validateLongitude_inputUCaseLetter() {
        String errMsg = "A longitude must be numerical";
        String longitude = "11A";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() > 0);
    }


    /**
     * CrimeCollectionManager.validateLongitude() should only allow
     * numerical input
     */
    @Test
    public void test_validateLongitude_inputOtherChar() {
        String errMsg = "A longitude must be numerical";
        String longitude = "#11";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() > 0);
    }



    /**
     * CrimeCollectionManager.validateLongitude() should only allow
     * numerical input greater than the minimum allowed latitude (-90 degrees)
     */
    @Test
    public void test_validateLongitude_minTest() {
        String errMsg = "A longitude should not be valid if it is below -180.0";
        String longitude = "-180.001";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() > 0);
    }


    /**
     * This validates the validation of test_validateLongitude_minTest
     * by checking that the same test fails if a longitude greater than
     * the minimum is checked
     */
    @Test
    public void test_validateLongitude_minTest_Test() {
        String errMsg = "A latitude should be valid if it is not below -180 and is otherwise valid.";
        String longitude = "-179.999";
        try {
            assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() > 0);
            fail();
        } catch (AssertionError e) {
            // yay...cuddly puppies
        }
    }


    /**
     * CrimeCollectionManager.validateLongitude() should allow
     * numerical input that is bang on -90 degrees (south pole)
     */
    @Test
    public void test_validateLongitude_negative90() {
        String errMsg = "A longitude should be valid if it is -180.0 exactly";
        String latitude = "-180.00000";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(latitude).length() == 0);
    }


    /**
     * CrimeCollectionManager.validateLongitude() should allow
     * numerical input that is bang on 180 degrees (north pole)
     */
    @Test
    public void test_validateLatitude_Positive180() {
        String errMsg = "A longitude should be valid if it is 180.0 exactly";
        String longitude = "180.00000";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() == 0);
    }


    /**
     * CrimeCollectionManager.validateLongitude() should not allow
     * numerical input that is greater than 90 degrees
     */
    @Test
    public void test_validateLongitude_MaxTest() {
        String errMsg = "A latitude should be valid if it is less than 180.0 degrees";
        String longitude = "180.00001";
        assertTrue(errMsg, CrimeCollectionManager.validateLongitude(longitude).length() > 0);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (Hour validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A valid hour code should return an empty string.
     */
    @Test
    public void test_validateHour_valid() {
        String errMsg = "A valid Hour should return an empty string";
        assertEquals(errMsg, "", CrimeCollectionManager.validateHour("5"));
    }

    /**
     * A valid hour number can only contain digits.<br>
     * CrimeCollectionManager.validateHour() should return a non empty string error message
     * if the hour parameter contains characters other than digits.
     */
    @Test
    public void test_validateHour_invalidInput() {
        String errMsg = "An invalid hour should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateHour("4a0").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateHour() should return a non empty string error message
     * if the hour parameter is too high.
     */
    @Test
    public void test_validateHour_inputTooHigh() {
        String errMsg = "A hour that is too high should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateHour("32").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateHour() should return a non empty string error message
     * if the hour parameter is too low.
     */
    @Test
    public void test_validateHour_inputTooLow() {
        String errMsg = "A hour that is too low should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateHour("-20").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateHour() should return an empty string
     * if the hour parameter is the maximum value.
     */
    @Test
    public void test_validateHour_inputUpperLimit() {
        String errMsg = "A hour of upper limit should return an empty string";
        assertEquals(errMsg, CrimeCollectionManager.validateHour("23"), "");
    }


    /**
     * CrimeCollectionManager.validateHour() should return an empty string
     * if the hour parameter is the minimum value.
     */
    @Test
    public void test_validateHour_inputLowerLimit() {
        String errMsg = "A hour of lower limit should return an empty string";
        assertEquals(errMsg, CrimeCollectionManager.validateHour("0"), "");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION (Minute validation)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A valid minute code should return an empty string.
     */
    @Test
    public void test_validateMinute_valid() {
        String errMsg = "A valid minute should return an empty string";
        assertEquals(errMsg, "", CrimeCollectionManager.validateMinute("5"));
    }

    /**
     * A valid minute number can only contain digits.<br>
     * CrimeCollectionManager.validateMinute() should return a non empty string error message
     * if the minute parameter contains characters other than digits.
     */
    @Test
    public void test_validateMinute_invalidInput() {
        String errMsg = "An invalid minute should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateMinute("4a0").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateMinute() should return a non empty string error message
     * if the minute parameter is too high.
     */
    @Test
    public void test_validateMinute_inputTooHigh() {
        String errMsg = "A minute that is too high should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateMinute("63").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateMinute() should return a non empty string error message
     * if the minute parameter is too low.
     */
    @Test
    public void test_validateMinute_inputTooLow() {
        String errMsg = "A minute that is too low should not return an empty string";
        assertTrue(errMsg, CrimeCollectionManager.validateMinute("-20").length() > 0);
    }


    /**
     * CrimeCollectionManager.validateMinute() should return an empty string
     * if the minute parameter is the maximum value.
     */
    @Test
    public void test_validateMinute_inputUpperLimit() {
        String errMsg = "A minute of upper limit should return an empty string";
        assertEquals(errMsg, CrimeCollectionManager.validateMinute("59"), "");
    }


    /**
     * CrimeCollectionManager.validateMinute() should return an empty string
     * if the minute parameter is the minimum value.
     */
    @Test
    public void test_validateMinute_inputLowerLimit() {
        String errMsg = "A minute of lower limit should return an empty string";
        assertEquals(errMsg, CrimeCollectionManager.validateMinute("0"), "");
    }


}
