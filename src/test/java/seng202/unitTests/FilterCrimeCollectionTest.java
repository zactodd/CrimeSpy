package seng202.unitTests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import crimeSpy.crimeData.CrimeCollection;
import crimeSpy.crimeData.CrimeCollectionManager;
import crimeSpy.crimeData.CrimeRecord;
import crimeSpy.crimeData.FilterCrimeCollection;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for Filter class
 * <u>Notes:</u>
 * <ul>
 *
 * </ul>
 */
public class FilterCrimeCollectionTest {

    // For legitimate crime records
    private DateTime lowerBound = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("5/27/2014 7:31:00" +
            " PM");
    private DateTime midBound = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00" +
            " PM");
    private DateTime upperBound = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("10/27/2014 " +
            "7:31:00" +
            " PM");
    private DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
    private DateTime date02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("9/27/2014 7:31:00 PM");
    private DateTime date03 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("11/27/2014 7:31:00 PM");
    // These two sets of CR's are identical
    private CrimeRecord testCR_01;
    private CrimeRecord testCR_02;
    private CrimeRecord testCR_03;
    private CrimeRecord testCR_04;
    private CrimeRecord testCR_05;
    private CrimeRecord testCR_06;

    private CrimeCollection testCC_01;



    @Before
    public void setUp() throws Exception {
        testCR_01 = new CrimeRecord("HX111111", date01, false, true, "111", "5", "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "HUNTING STORE");
        testCR_02 = new CrimeRecord("HX111112", date03, true, false, "112", "7", "080XX S " +
                "HALSTED ST",
                622, 22, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_03 = new CrimeRecord("HX111113", date02, false, false, "113", "7", "080XX S " +
                "HALSTED ST",
                622, 22, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "HUNTING STORE");
        testCR_04 = new CrimeRecord("HX111114", date02, false, false, "114", "6", "080XX S " +
                "HALSTED ST",
                622, 23, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "MaLl");
        testCR_05 = new CrimeRecord("HX111115", date02, false, false, "115", "5", "080XX S " +
                "HALSTED ST",
                622, 24, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "BaNk");
        testCR_06 = new CrimeRecord("HX111116", date02, false, false, "116", "6", "080XX S " +
                "HALSTED ST",
                622, 25, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "BANK");
        testCC_01 = new CrimeCollection();
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_01.addCrimeRecord(testCR_03);
        testCC_01.addCrimeRecord(testCR_04);
        testCC_01.addCrimeRecord(testCR_05);
        testCC_01.addCrimeRecord(testCR_06);
    }


    /**
     * Assert that filtering on an empty crimeCollection result in an empty CrimeCollection
     */
    @Test
    public void filterWithNoCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(new CrimeCollection());
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "", null, null);
        assertEquals(0, CrimeCollectionManager.getCurrWorkingCollection().getCrimes().size());
    }

    /**
     * Assert that filtering on collection with no filter selected result in the same ArrayList of CrimeRecord
     */
    @Test
    public void filterWithNoFilterCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "", null, null);
        assertTrue(CrimeCollectionManager.getCurrWorkingCollection().getCrimes().equals(testCC_01.getCrimes()));
    }

    /**
     * Assert that filtering by domestic result in a collection with matching domestic boolean
     */
    @Test
    public void filterDomesticCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(true, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by arrest result in a collection with matching arrest boolean
     */
    @Test
    public void filterArrestCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, true, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_02);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering using exact location string result in collection with that location
     */
    @Test
    public void filterLocationCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "DEPARTMENT STORE", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_02);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering using substring result in collection with a location containing
     * that substring
     */
    @Test
    public void filterLocationSubCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "ba", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_05);
        tempArray.add(testCR_06);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by string is not case sensitive
     */
    @Test
    public void filterCaseSensitiveCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "mall", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_04);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by ward results in a collection containing that ward
     */
    @Test
    public void filterWardCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , 21, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by beat results in a collection containing that beat
     */
    @Test
    public void filterBeatCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", 621
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by FBICD results in a collection containing that beat
     */
    @Test
    public void filterFBICDCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "111", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by IUCR results in a collection containing that beat
     */
    @Test
    public void filterIUCRCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "5", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        tempArray.add(testCR_05);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by date using both to and from parameter result in a collection contain only record
     * that are between these parameter
     */
    @Test
    public void filterDate01CrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "", midBound, lowerBound);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by date using just one parameter result in a collection contain only record
     * that are between that parameter and its infinity
     */
    @Test
    public void filterDate02CrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, new ArrayList<String>(), "", -1
                , -1, "", "", "", "", "", null, upperBound);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_02);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by date using just one parameter result in a collection contain only record
     * that are between that parameter and its infinity
     */
    @Test
    public void filterCrimeTypeCrimeCollection() {
        testCR_01.getCrimeType().setPrimaryDescription("theft");
        testCC_01.addCrimeRecord(testCR_01);
        ArrayList<String> cT = new ArrayList<String>();
        cT.add("theft");
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(null, null, cT, "", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

    /**
     * Assert that filtering by multiple parameter result in an collection where each record meets each parameter
     */
    @Test
    public void filterMulitCrimeCollection() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        FilterCrimeCollection.filter(false, null, new ArrayList<String>(), "mall", -1
                , -1, "", "", "", "", "", null, null);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_04);
        assertEquals(tempArray, CrimeCollectionManager.getCurrWorkingCollection().getCrimes());
    }

}
