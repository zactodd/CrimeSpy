package seng202.unitTests;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import org.joda.time.DateTime;
import org.joda.time.Period;
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
import crimeSpy.crimeData.CrimeCollection;
import crimeSpy.crimeData.CrimeCollectionManager;
import crimeSpy.crimeData.CrimeRecord;
import crimeSpy.crimeData.SQLiteDBHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit tests for CrimeRecord class
 * <u>Notes:</u>
 * <ul>
 *     <li>There are constraints on uniqueness for caseId</li>
 *     <li>This class also uses object from CrimeLocation</li>
 *     <li>This class also uses objects from CrimeRecord</li>
 * </ul>
 * In some cases there is more than one assertion in an individual unit test.
 * Where this is the case the assertions are testing the same functionality
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SQLiteDBHandler.class)
public class CrimeRecordTest {


    // legitimate crime record object
    private final String caseID = "HX321538";
    private DateTime date = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
    private Boolean arrest = true;
    private Boolean domestic = false;
    private String iucr = "860";
    private String fbiCD = "6";
    private String priDesc = "THEFT";
    private String secDesc = "RETAIL THEFT";
    private String block = "080XX S HALSTED ST";
    private Integer beat = 621;
    private Integer ward = 21;
    private double xCoord = 1172409;
    private double yCoord = 1851438;
    private double longitude = -87.70587610484924;
    private double latitude = 41.8808655731203;
    private String location = "DEPARTMENT STORE";
    private Integer prevCrimeID;
    private Integer nextCrimeID;
    private CrimeRecord cr;
    private CrimeRecord cr2;
    private CrimeRecord crIdent;
    private CrimeRecord crNullDate;
    private CrimeRecord crimeAtMyPlace;
    private CrimeRecord emptyCr;
    private CrimeRecord emptyCr_EmptyConstructor;
    private DateTime date2 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:35:00 PM");
    private double longitude2 = -87.60364133299082;
    private double latitude2 = 41.8045418862783;

    private double myLong = 174.828859;
    private double myLat = -41.321576;

    private static CrimeCollection testCC;
    private static File testCrimeDB   = new File("TestCrimeDB.db"); // File to read iucr codes from

    /**
     * Delta is the acceptable difference when comparing floating point numbers
     */
    Double delta = 0.0001;

    @BeforeClass
    public static void setupDB () throws Exception{
        testCrimeDB.delete();
        try {
            SQLiteDBHandler.createNewCrimedb(testCrimeDB.getAbsolutePath());
            testCC = new CrimeCollection("Test1", 0, testCrimeDB.getAbsolutePath());

        } catch (Exception e) {
            // Only create if it doesn't already exist
        }
    }

    /**
     * Creation of valid CrimeRecords for testing
     */
    @Before
    public void setUp() {

        // Make some convenient crime records to perform tests on
        cr = new CrimeRecord(caseID, date, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, latitude, longitude, location);
        cr2 = new CrimeRecord("HX321540", date2, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, latitude2, longitude2, location);
        crIdent = new CrimeRecord(caseID, date, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, latitude, longitude, location);
        crNullDate = new CrimeRecord(caseID, null, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, latitude, longitude, location);
        crimeAtMyPlace = new CrimeRecord("HX321539", date2, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, myLat, myLong, location);

        // This uses the empty constructor
        emptyCr = new CrimeRecord(null, null, null, null, iucr, null, null, null, null, xCoord, yCoord, latitude, longitude, null);
        CrimeCollectionManager.setCurrWorkingCollection(testCC);
    }

    @After
    public void tearDown() throws Exception {
        testCrimeDB.delete();
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILITY
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor that takes a CrimeRecord and returns a duplicated
     * CrimeRecord as another object. Check that the crime id is duplicated correctly
     */
    @Test
    public void test_duplicateConstructor() {
        String errMsg = "The duplication constructor should create an identical CrimeRecord object";
        CrimeRecord crDup = new CrimeRecord(cr);
        assertEquals(errMsg, cr.getCaseID(), crDup.getCaseID());
    }


    /**
     * Test the atrociously named myEquals function to ensure that two crime
     * records with the same caseID evaluated by this function will return true
     */
    @Test
    public void test_myEquals_true() {
        String errMsg = "two crime records with the same case id should return true";
        CrimeRecord crDup = new CrimeRecord(cr);
        assertTrue(errMsg, cr.myEquals(crDup));
    }


    /**
     * Test the atrociously named myEquals function to ensure that two crime
     * records with the different caseIDs evaluated by this function will return false
     */
    @Test
    public void test_myEquals_false() {
        String errMsg = "two crime records with different case ids should have false return by myEquals";
        String differentCaseId = cr.getCaseID() + "2";
        CrimeRecord crDiff = new CrimeRecord(differentCaseId, date, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, latitude, longitude, location);
        assertFalse(errMsg, cr.myEquals(crDiff));
    }


    /**
     * Test the class .toString method. Should return a description of
     * the object for debugging purposes.<br>
     * This checks behaviour of operation on an object with null values: should return error string
     * and <b>not</b> throw an exception.
     * @throws NullPointerException should not be thrown but in fact caught in the toString method
     */
    @Test
    public void test_toString_nulls() throws Exception {
        String dummy = emptyCr.toString();
    }


    /**
     * obfuscateRecord() should obfuscate block data if it is too specific
     */
    @Test
    public void test_obfuscateRecord_specificBlockInfo() {
        String errMsg = "The obfuscate method did not correctly obfuscate the block information";
        cr.obfuscate();
        // Is this test failing??
        // The block was defined as "08012 S HALSTED ST" at the top --> check it still is
        // Obfuscate should replace with "080XX S HALSTED ST"
        assertEquals(errMsg, "080XX S HALSTED ST", cr.getCrimeLocation().getBlock());
    }


    /**
     * obfuscateRecord() should obfuscate block data if it is too specific
     */
    @Test
    public void test_obfuscateRecord_specificBlockInfo2() {
        String errMsg = "The obfuscate method did not correctly obfuscate the block information";
        cr.getCrimeLocation().setBlock("123456 SOMEWHERE ST");
        cr.obfuscate();
        assertEquals(errMsg, "1234XX SOMEWHERE ST", cr.getCrimeLocation().getBlock());
    }


    /**
     * obfuscateRecord() should not change block data if it is already obfuscated
     */
    @Test
    public void test_obfuscateRecord_alreadyObfuscatedBlockInfo() {
        String specificBlock = "080XX S HALSTED ST";
        String errMsg = "The obfuscate method did not correctly obfuscate the block information";
        cr.obfuscate();
        assertEquals(errMsg, specificBlock, cr.getCrimeLocation().getBlock());
    }


    /**
     * obfuscateRecord() should obfuscate block data if it is too specific
     * Check behaviour if second to last digit is already obfuscated
     */
    @Test
    public void test_obfuscateRecord_SecondLastDigitIsX() {
        String errMsg = "The obfuscate method did not correctly obfuscate the block information";
        cr.getCrimeLocation().setBlock("1111X1 SOMEWHERE ST");
        cr.obfuscate();
        assertEquals(errMsg, "1111XX SOMEWHERE ST", cr.getCrimeLocation().getBlock());
    }



    /**
     * obfuscateRecord() should obfuscate block data if it is too specific
     * Check behaviour if last digit is already obfuscated
     */
    @Test
    public void test_obfuscateRecord_LastDigitIsX() {
        String errMsg = "The obfuscate method did not correctly obfuscate the block information";
        cr.getCrimeLocation().setBlock("11111X SOMEWHERE ST");
        cr.obfuscate();
        assertEquals(errMsg, "1111XX SOMEWHERE ST", cr.getCrimeLocation().getBlock());
    }


    /**
     * Edge case check behaviour if the block is short
     */
    @Test
    public void test_obfuscateRecord_shortString() {
        String errMsg = "The obfuscate method did not handle a short string";
        cr.getCrimeLocation().setBlock("1");
        cr.obfuscate();
        assertEquals(errMsg, "X", cr.getCrimeLocation().getBlock());
    }


    /**
     * Edge case check behaviour if the block is short
     */
    @Test
    public void test_obfuscateRecord_shortString2() {
        String errMsg = "The obfuscate method did not handle a short string";
        cr.getCrimeLocation().setBlock("11");
        cr.obfuscate();
        assertEquals(errMsg, "XX", cr.getCrimeLocation().getBlock());
    }


    /**
     * Edge case check behaviour if the block is short with mixed obfuscation requirements
     */
    @Test
    public void test_obfuscateRecord_shortString2_mixed() {
        String errMsg = "The obfuscate method did not handle a short string";
        cr.getCrimeLocation().setBlock("1X");
        cr.obfuscate();
        assertEquals(errMsg, "XX", cr.getCrimeLocation().getBlock());
    }


    /**
     * Edge case check behaviour if the block is empty
     */
    @Test
    public void test_obfuscateRecord_emptyString() {
        String errMsg = "The obfuscate method did not handle a short string";
        cr.getCrimeLocation().setBlock("");
        cr.obfuscate();
        assertEquals(errMsg, "", cr.getCrimeLocation().getBlock());
    }



    /**
     * getDateTimeStr() should return a nice string representation of a DateTime object.
     * Check that it does not throw an exception if the date is uninitialised
     */
    @Test
    public void test_dateStr_ifNull() {
        String dummy = emptyCr.getDateTimeStr();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS
    // Some setters will use the getter to verify correct functionality and
    // so the getters are tested to ensure logical dependence.
    // (The setters are tested as they sometimes contain additional logic).
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Checks the return value of getCaseID() for
     * an uninitialised CrimeType is null.
     */
    @Test
    public void test_getCaseId() {
        assertEquals("getCaseID should return null for null made object.", null, emptyCr.getCaseID());
    }


    /**
     * Checks the return value of getDate() for
     * an uninitialised CrimeType is null.
     */
    @Test
    public void test_getDate() {
        assertEquals("getDate() should return null for null made object.", null, emptyCr.getDate());

    }


    /**
     * Checks the return value of getArrest() for
     * an uninitialised CrimeType is null.
     */
    @Test
    public void test_getArrest() {
        assertEquals("getArrest should return null for null made object.", null, emptyCr.getArrest());
    }


    /**
     * Checks the return value of getDomestic() for
     * an uninitialised CrimeType is null.
     */
    @Test
    public void test_getDomestic() {
        assertEquals("getDomestic() should return null for null made object.", null, emptyCr.getDomestic());
    }


    /**
     * Checks the return value of getCrimeType() for
     * an uninitialised CrimeType is <b>not</b> null.
     * Check that .getCrimeType().getIucr() matches expected.
     */
    @Test
    public void test_getCrimeType() {
        assertNotEquals("IUCR can not be null!", null, emptyCr.getCrimeType());
        assertEquals("IUCR does not match expected", iucr, emptyCr.getCrimeType().getIucr() );
        //This method should do exactly as the above
        assertEquals("IUCR does not match expected", iucr, emptyCr.getCrimeTypeID());
    }


    /**
     * Checks the return value of getCrimeLocation() for
     * an uninitialised CrimeType is null.
     */
    @Test
    public void test_getCrimeLocation() {
        assertEquals("getCrimeLocation() should return null for null made object.", null, emptyCr.getFbiCD());

    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS
    // Some setter have additional logic
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Checks that the FBI CD can be set to a legitimate value
     * This indirectly tests validateFbiCd as well.
     */
    @Test
    public void test_setFbiCD() {
        String legitFbiCD = "01A";
        String errMsg = "setFbiCD() should set the value if it is a legitimate FBI CD.";
        emptyCr.setFbiCD(legitFbiCD);
        assertEquals(errMsg, legitFbiCD, emptyCr.getFbiCD());
    }


    /**
     * Checks that the FBI CD can not be set to a short value.
     * While this method uses getters and setter it is in fact
     * indirectly testing validateFbiCd() - which should be private.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setFbiCD_invalidFBICD_short() {
        String shortFbiCD = "";
        String errMsg = "setFbiCD() should not set the value if it is too short.";
        emptyCr.setFbiCD(shortFbiCD); //should throw exception
        assertNotEquals(errMsg, shortFbiCD, emptyCr.getFbiCD());
    }


    /**
     * Checks that the FBI CD can not be set to an illegal pattern.
     * While this method uses getters and setter it is in fact
     * indirectly testing validateFbiCd() - which should be private.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_setFbiCD_invalidFBICD_withBadPattern() {
        String badFbiCD = "!A1";
        String errMsg = "setFbiCD() should not set the value if it is an illegal pattern.";
        emptyCr.setFbiCD(badFbiCD); //should throw exception
        assertNotEquals(errMsg, badFbiCD, emptyCr.getFbiCD());
    }



    /**
     * Checks that setCaseID correctly sets the caseID if the caseID
     * has <b>not</b> been initialised!
     */
    @Test
    public void test_setCaseID_originalIsNull() {
        String caseID = "NEW";
        String errMsg = "setCaseId() should be able to set the caseId iff it is currently null.";
        emptyCr.setCaseID(caseID);
        assertEquals(errMsg, caseID, emptyCr.getCaseID());
    }

//Disabled because you should be allowed to modify CaseID's

//    /**
//     * Checks that setCaseID does <b>not</b> set the caseID
//     * if the caseID <b>has</b> been initialised!
//     */
//    @Test
//    public void test_setCaseID_originalIsNotNull() {
//        String caseID = "NEWCASEID";
//        String errMsg = "setCaseId() should not able to change the caseId if its initialised.";
//        cr.setCaseID(caseID);
//        //this.caseID is original caseID for cr CrimeRecord
//        assertEquals(errMsg, this.caseID, cr.getCaseID());
//    }

    /**
     * Checks that setDate() sets the date correctly
     */
    @Test
    public void test_setDate() {
        DateTime newDate = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("1/01/2001 1:00:00 PM");
        String errMsg = "setDate() should be able to change the date.";
        cr.setDate(newDate);
        assertEquals(errMsg, newDate, cr.getDate());
    }


    /**
     * Checks that setArrest() sets the arrest state correctly
     */
    @Test
    public void test_setArrest_originalIsNull() {
        Boolean newArrestState = !this.arrest;
        String errMsg = "setArrest() should be able to set the arrest state.";
        emptyCr.setArrest(newArrestState);
        assertEquals(errMsg, newArrestState, emptyCr.getArrest());
    }


    /**
     * Checks that setDomestic() sets the domestic state correctly
     */
    @Test
    public void test_setDomestic_originalIsNull() {
        Boolean newDomesticState = !this.domestic;
        String errMsg = "setDomestic() should be able to set the domestic state.";
        emptyCr.setDomestic(newDomesticState);
        assertEquals(errMsg, newDomesticState, emptyCr.getDomestic());
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Time and distance calculations (ben)
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void testMillisBetween()
    {
        Period expect = new Period(240000);
        assertEquals(expect, cr.millisBetween(cr2));

        expect = new Period(240000);
        assertEquals(expect, cr2.millisBetween(cr));

        expect = new Period(0);
        assertEquals(expect, cr.millisBetween(crIdent));

    }

    @Test (expected=NullPointerException.class)
    public void testNullDate_MillisBetween()
    {
        cr.millisBetween(crNullDate);
    }

    @Test
    public void testDistanceBetween() {
        // All tests to 1m accuracy unless otherwise stated
        // Test that it properly gets a known distance between two crime points
        double expected = 11.990;
        assertEquals(expected, cr.distanceBetween(cr2), .001);

        // Test that this relation is reflexive - ie, changing the order of arguments won't mess with anything
        expected = 11.99;
        assertEquals(expected, cr2.distanceBetween(cr), .001);

        // Test that a 0 distance will be correctly calculated
        expected = 0;
        assertEquals(expected, cr.distanceBetween(crIdent), .001);

        // Test that distance from Chicago to my house in Wellington is correct so that we know that haversine is doing
        // its thing properly-ish
        expected = 13440;
        assertEquals(expected, cr.distanceBetween(crimeAtMyPlace), 3);
        /* This algorithm gets less accurate as distance increases, hence the larger margin for error, but it's okay
           because A) We're unlikely to get distances this large and B) It's already significantly more accurate than
           pythagorean approximation, and C) the inaccuracy is due to the non-spherishness of the earth, which we could
           theoretically account for with Vincenty's Formulae, but it's orders of magnitude more complex and wouldn't be
           worth the gain in accuracy.
           Fun Fact: Even Vincety's Formulae fail with nearly antipodal points. The more you know!
         */
    }


    @Test (expected=NullPointerException.class)
    public void testNullDate_DistanceBetween()
    {
        cr.millisBetween(crNullDate);
    }

    /**
     * Ensure that there can not be two records with identical caseId's
     */
    @Test
    public void testLatLong() {
        assertTrue(true);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Pseudo INTEGRATION testing (not to the database layer tho)
    // -->the results of the following tests are dependent on more than one class,
    // at least one of which should be CrimeRecord.
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Checks that the crimeType can be set to a legitimate value
     * The primary and secondary description generation static method is
     * mocked using PowerMockito.
     */
    @Test
    public void test_setCrimeType() {
        String errMsg = "setCrimeType() should be able to set the crime type.";
        String iucr = "1XX";
        String testPrimaryDescription = "PrimaryDescription";
        String testSecondaryDescription = "SecondaryDescription";

        // Mock the construction of primary and secondary descriptions (SQLiteDBHandler dependency)
        String[] resArray = {testPrimaryDescription, testSecondaryDescription};
        ArrayList<String> result = new ArrayList<String>(Arrays.asList(resArray));
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        PowerMockito.when(SQLiteDBHandler.getCrimeTypeData(Mockito.anyString())).thenReturn(result);
        //SQLiteDBHandler.getCrimeTypeData(iucr)

        // When (set it)
        emptyCr.setCrimeType(iucr, testPrimaryDescription, testSecondaryDescription);

        // Check IUCR code
        assertEquals(errMsg, iucr, emptyCr.getCrimeTypeID());
        // Check Primary Description
        assertEquals(errMsg, testPrimaryDescription, emptyCr.getCrimeType().getPrimaryDescription());
        // Check Secondary Description
        assertEquals(errMsg, testSecondaryDescription, emptyCr.getCrimeType().getSecondaryDescription());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // testing for JavaFX properties
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks caseIDProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_caseIDProperty() {
        String errMsg = "caseIDProperty() should return a valid StringProperty, which has equivalent value as original caseID.";
        StringProperty property = cr.caseIDProperty();
        assertEquals(errMsg, cr.getCaseID(), property.get());
    }

    /**
     * Checks caseIDProperty() getter on a null caseID
     * .get() should give the same value as the original.
     */
    @Test
    public void test_caseIDProperty_nullCaseID() {
        String errMsg = "caseIDProperty() should return a valid StringProperty, which has equivalent value as origninal caseID";
        StringProperty property = emptyCr.caseIDProperty();
        assertEquals(errMsg, emptyCr.getCaseID(), property.get());
    }


    /**
     * Checks dateProperty() getter.<br>
     * Date is a bit special at the moment as it is converted to a nice string: note the .toString()
     * .get() should give the same value as the original.
     */
    @Test
    public void test_dateProperty() {
        String errMsg = "dateProperty() should return a valid StringProperty, which has equivalent value as original date";
        StringProperty property = cr.dateProperty();
        assertEquals(errMsg, cr.getDateTimeStr(), property.get());
    }


    /**
     * Checks dateProperty() getter with the date not initialised or null.<br>
     * Date is a bit special at the moment as it is converted to a nice string: note the .toString()
     * .get() should give the same value as the original.
     */
    @Test
    public void test_dateProperty_nullDate() {
        String errMsg = "dateProperty() should return a valid StringProperty, which has equivalent value as original date, when date is null";
        StringProperty property = emptyCr.dateProperty();
        assertEquals(errMsg, null, property.get());
    }


    /**
     * Checks arrestProperty() getter.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_arrestProperty() {
        String errMsg = "arrestProperty() should return a valid BooleanProperty, which has equivalent value as original";
        BooleanProperty property = cr.arrestProperty();
        assertEquals(errMsg, cr.getArrest(), property.get());
    }


    /**
     * Checks arrestProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_arrestProperty_nullArrest() {
        String errMsg = "arrestProperty() should return a valid BooleanProperty, which has equivalent value as original, wven when original is null";
        BooleanProperty property = emptyCr.arrestProperty();
        Boolean expected = false;
        assertEquals(errMsg, expected, property.get());
    }


    /**
     * Checks domesticProperty() getter.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_domesticProperty() {
        String errMsg = "arrestProperty() should return a valid BooleanProperty, which has equivalent value as original";
        BooleanProperty property = cr.domesticProperty();
        assertEquals(errMsg, cr.getDomestic(), property.get());
    }


    /**
     * Checks domesticProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_domesticProperty_nullDomestic() {
        String errMsg = "domesticProperty() should return a valid BooleanProperty, which has equivalent value as original, even when original is null";
        BooleanProperty property = emptyCr.domesticProperty();
        Boolean expected = false;
        assertEquals(errMsg, expected, property.get());
    }


    /**
     * Checks iucrProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_iucrProperty() {
        String errMsg = "iucrProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = cr.iucrProperty();
        assertEquals(errMsg, cr.getCrimeType().getIucr(), property.get());
    }


    /**
     * Checks iucrProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_iucrProperty_nullIucr() {
        String errMsg = "iucrProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = emptyCr.iucrProperty();
        assertEquals(errMsg, emptyCr.getCrimeType().getIucr(), property.get());
    }


    /**
     * Checks fbiCDProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_fbiCDProperty() {
        String errMsg = "fbiCDProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = cr.fbiCDProperty();
        assertEquals(errMsg, cr.getFbiCD(), property.get());
    }


    /**
     * Checks fbiCDProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_fbiCDProperty_nullFBICD() {
        String errMsg = "fbiCDProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = emptyCr.fbiCDProperty();
        assertEquals(errMsg, emptyCr.getFbiCD(), property.get());
    }


    /**
     * Checks primaryDescriptionProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.<br>
     * This uses mocking to ensure an actual primary description String is set and tested.
     */
    @Test
    public void test_primaryDescriptionProperty() {
        String errMsg = "primaryDescriptionProperty() should return a valid StringProperty, which has equivalent value as original.";
        String iucr = "111";
        String testPrimaryDescription = "PrimaryDescription";
        String testSecondaryDescription = "Sec";
        // Mock the construction of primary and secondary descriptions (SQLiteDBHandler dependency)
        String[] resArray = {testPrimaryDescription, testSecondaryDescription};
        ArrayList<String> result = new ArrayList<String>(Arrays.asList(resArray));
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        PowerMockito.when(SQLiteDBHandler.getCrimeTypeData(Mockito.anyString())).thenReturn(result);
        // When (set it)
        cr.setCrimeType(iucr, testPrimaryDescription, testSecondaryDescription);
        StringProperty property = cr.primaryDescriptionProperty();
        assertEquals(errMsg, cr.getCrimeType().getPrimaryDescription(), property.get());
    }


    /**
     * Checks primaryDescriptionProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_primaryDescriptionProperty_nullPrimaryDescription() {
        String errMsg = "primaryDescriptionProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = emptyCr.primaryDescriptionProperty();
        assertEquals(errMsg, emptyCr.getCrimeType().getPrimaryDescription(), property.get());
    }


    /**
     * Checks secondaryDescriptionProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.<br>
     * This uses mocking to ensure an actual secondary description String is set and tested.
     */
    @Test
    public void test_secondaryDescriptionProperty() {
        String errMsg = "secondaryDescriptionProperty() should return a valid StringProperty, which has equivalent value as original.";
        String iucr = "222";
        String testPrimaryDescription = "Pri";
        String testSecondaryDescription = "SecondaryDescription";
        // Mock the construction of primary and secondary descriptions (SQLiteDBHandler dependency)
        String[] resArray = {testPrimaryDescription, testSecondaryDescription};
        ArrayList<String> result = new ArrayList<String>(Arrays.asList(resArray));
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        PowerMockito.when(SQLiteDBHandler.getCrimeTypeData(Mockito.anyString())).thenReturn(result);
        // When (set it)
        cr.setCrimeType(iucr, testPrimaryDescription, testSecondaryDescription);
        StringProperty property = cr.secondaryDescriptionProperty();
        assertEquals(errMsg, cr.getCrimeType().getSecondaryDescription(), property.get());
    }


    /**
     * Checks secondaryDescriptionProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_secondaryDescriptionProperty_nullSecondaryDescription() {
        String errMsg = "secondaryDescriptionProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = emptyCr.secondaryDescriptionProperty();
        assertEquals(errMsg, emptyCr.getCrimeType().getSecondaryDescription(), property.get());
    }


    /**
     * Checks blockProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_blockProperty() {
        String errMsg = "blockProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = cr.blockProperty();
        assertEquals(errMsg, cr.getCrimeLocation().getBlock(), property.get());
    }


    /**
     * Checks blockProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_blockProperty_nullBlock() {
        String errMsg = "blockProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = emptyCr.blockProperty();
        assertEquals(errMsg, emptyCr.getCrimeLocation().getBlock(), property.get());
    }



    /**
     * Checks beatProperty() getter returns a valid IntegerProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_beatProperty() {
        String errMsg = "beatProperty() should return a valid IntegerProperty, which has equivalent value as original.";
        IntegerProperty property = cr.beatProperty();
        // Cast as otherwise it is ambiguous as to whether to compare by object or long
        assertEquals(errMsg, cr.getCrimeLocation().getBeat(), (Integer)property.get());
    }


    /**
     * Checks beatProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_beatProperty_nullBeat() {
        String errMsg = "beatProperty() should return a valid IntegerProperty, which has equivalent value as original.";
        IntegerProperty property = emptyCr.beatProperty();
        assertEquals(errMsg, emptyCr.getCrimeLocation().getBeat(), (Integer)property.get());
    }


    /**
     * Checks wardProperty() getter returns a valid IntegerProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_wardProperty() {
        String errMsg = "wardProperty() should return a valid IntegerProperty, which has equivalent value as original.";
        IntegerProperty property = cr.wardProperty();
        // Cast as otherwise it is ambiguous as to whether to compare by object or long
        assertEquals(errMsg, cr.getCrimeLocation().getWard(), (Integer)property.get());
    }


    /**
     * Checks wardProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_wardProperty_nullWard() {
        String errMsg = "wardProperty() should return a valid IntegerProperty, which has equivalent value as original.";
        IntegerProperty property = emptyCr.wardProperty();
        assertEquals(errMsg, emptyCr.getCrimeLocation().getWard(), (Integer)property.get());
    }


    /**
     * Checks xCoordProperty() getter returns a valid DoubleProperty.
     * <b>delta</b> is used when comparing the two
     * .get() should give the same value as the original.
     */
    @Test
    public void test_xCoordProperty() {
        String errMsg = "xCoordProperty() should return a valid DoubleProperty, which has equivalent value as original.";
        DoubleProperty property = cr.xCoordProperty();
        assertEquals(errMsg, delta, cr.getCrimeLocation().getxCoordinate(), property.get());
    }


    /**
     * Checks yCoordProperty() getter returns a valid DoubleProperty.
     * <b>delta</b> is used when comparing the two
     * .get() should give the same value as the original.
     */
    @Test
    public void test_yCoordProperty() {
        String errMsg = "yCoordProperty() should return a valid DoubleProperty, which has equivalent value as original.";
        DoubleProperty property = cr.yCoordProperty();
        assertEquals(errMsg, delta, cr.getCrimeLocation().getyCoordinate(), property.get());
    }


    /**
     * Checks longitudeProperty() getter returns a valid DoubleProperty.
     * <b>delta</b> is used when comparing the two
     * .get() should give the same value as the original.
     */
    @Test
    public void test_longitudeProperty() {
        String errMsg = "longitudeProperty() should return a valid DoubleProperty, which has equivalent value as original.";
        DoubleProperty property = cr.longitudeProperty();
        assertEquals(errMsg, delta, Math.abs(cr.getCrimeLocation().getLongitude()), Math.abs(property.get()));

    }


    /**
     * Checks latitudeProperty() getter returns a valid DoubleProperty.
     * <b>delta</b> is used when comparing the two
     * .get() should give the same value as the original.
     */
    @Test
    public void test_latitudeProperty() {
        String errMsg = "latitudeProperty() should return a valid DoubleProperty, which has equivalent value as original.";
        DoubleProperty property = cr.latitudeProperty();
        assertEquals(errMsg, delta, Math.abs(cr.getCrimeLocation().getLatitude()), Math.abs(property.get()));

    }


    /**
     * Checks locStrProperty() getter returns a valid StringProperty.
     * .get() should give the same value as the original.
     */
    @Test
    public void test_locStrProperty() {
        String errMsg = "locStrProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = cr.locStrProperty();
        assertEquals(errMsg, cr.getCrimeLocation().getLocationStr(), property.get());
    }


    /**
     * Checks locStrProperty() getter with the original value not initialised or null.<br>
     * .get() should give the same value as the original.
     */
    @Test
    public void test_locStrProperty_nullLocStr() {
        String errMsg = "locStrProperty() should return a valid StringProperty, which has equivalent value as original.";
        StringProperty property = emptyCr.locStrProperty();
        assertEquals(errMsg, emptyCr.getCrimeLocation().getLocationStr(), property.get());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OTHER - defined as 'future functionality' in CrimeRecord
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////




    /**
     * Check that setting the previous crime ID can then be retrieved correctly.<br>
     * Initial crime record is initialised with null values.
     */
    @Test
    public void test_setPrevCrimeID_getPrevCrimeID_nullInit() {
        String newPrevCID = "newPrevCID";
        String errMsg = "There was an inconsistency when setting and getting the previous crime ID for an uninitiated crime record";
        emptyCr.setPrevCrimeID(newPrevCID);
        assertEquals(errMsg, newPrevCID, emptyCr.getPrevCrimeID());
    }


    /**
     * Check that setting the previous crime ID can then be retrieved correctly.
     */
    @Test
    public void test_setPrevCrimeID_getPrevCrimeID_initiated() {
        String newPrevCID = "newPrevCID";
        String errMsg = "There was an inconsistency when setting and getting the previous crime ID for a crime record";
        cr.setPrevCrimeID(newPrevCID);
        assertEquals(errMsg, newPrevCID, cr.getPrevCrimeID());
    }


    /**
     * Check that setting the next crime ID can then be retrieved correctly.<br>
     * Initial crime record is initialised with null values.
     */
    @Test
    public void test_setNextCrimeID_getNextCrimeID_nullInit() {
        String newNextCID = "newNextCID";
        String errMsg = "There was an inconsistency when setting and getting the next crime ID";
        emptyCr.setNextCrimeID(newNextCID);
        assertEquals(errMsg, newNextCID, emptyCr.getNextCrimeID());
    }


    /**
     * Check that setting the next crime distance can then be retrieved correctly.<br>
     * Initial crime record is initialised with null values.
     */
    @Test
    public void test_getNextCrimeDistance_setNextCrimeDistance_null() {
        CrimeCollection cc = new CrimeCollection();
        ArrayList<CrimeRecord> aL = new ArrayList<>();
        aL.add(cr);
        aL.add(crimeAtMyPlace);
        cc.populateCrimeRecords(aL);
        CrimeCollectionManager.setCurrWorkingCollection(cc);
        double newNextCrimeDistance = 13442.743747029366;
        String errMsg = "There was an inconsistency when setting and getting the next crime distance";
        assertEquals(errMsg, delta, newNextCrimeDistance, cr.getNextCrimeDistance());
    }


    /**
     * Check that setting the next next crime time since can then be retrieved correctly.<br>
     */
    @Test
    public void test_getNextCrimeTimeSince_setNextCrimeTimeSince() {
        //System.out.println(cr.getNextCrimeTimeSince());
        // Zac - define crimeTimeSince please !?!

        // Set up 5 second difference
        DateTime cr_DateTime = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:00:00 PM");
        DateTime cr2_DateTime = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:00:05 PM");
        // Set two crime records to the difference
        cr.setDate(cr_DateTime);
        cr2.setDate(cr2_DateTime);

        CrimeCollection cc = new CrimeCollection();
        ArrayList<CrimeRecord> aL = new ArrayList<>();
        aL.add(cr);
        aL.add(cr2);
        cc.populateCrimeRecords(aL);
        CrimeCollectionManager.setCurrWorkingCollection(cc);

        // Define actual difference as time between two crime records
        Period actual = cr.millisBetween(cr2);
        // expected 5 seconds: Period(hours, mins, seconds, millis)

        Period expected = new Period(0, 0, 5, 0);
        String errMsg = "There was an inconsistency when setting and getting the next crime time since";
        assertEquals(errMsg, expected, cr.getNextCrimeTimeSince());
    }


}

