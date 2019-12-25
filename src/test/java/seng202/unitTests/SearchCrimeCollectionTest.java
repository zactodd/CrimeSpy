package seng202.unitTests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import seng202.crimeSpy.crimeData.CrimeCollection;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;
import seng202.crimeSpy.crimeData.CrimeRecord;
import seng202.crimeSpy.crimeData.SearchCrimeCollection;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


/**
 * Unit tests for Search class
 * <u>Notes:</u>
 * <ul>
 *
 * </ul>.
 */
public class SearchCrimeCollectionTest {

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
        testCR_01 = new CrimeRecord("HX111111", date01, false, true, "111", "5", "mall",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "HUNTING STORE");
        testCR_02 = new CrimeRecord("HX111112", date03, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                622, 22, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_03 = new CrimeRecord("HX111113", date02, false, false, "111", "5", "080XX S " +
                "HALSTED ST",
                622, 22, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "HUNTING STORE");
        testCR_04 = new CrimeRecord("HX111114", date02, false, false, "112", "6", "080XX S " +
                "HALSTED ST",
                622, 23, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "MaLl");
        testCR_05 = new CrimeRecord("HX111115", date02, false, false, "111", "5", "080XX S " +
                "HALSTED ST",
                622, 24, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "BaNk");
        testCR_06 = new CrimeRecord("HX111116", date02, false, false, "112", "6", "080XX S " +
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
     * Assert that search with an empty string result in the currentWorkingCollection
     */
    @Test
    public void searchWithEmpty() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        assertEquals(testCC_01.getCrimes(), SearchCrimeCollection.search("").getCrimes());
    }

    /**
     * Assert that searching with string find string that are equal to it
     */
    @Test
    public void searchWithString() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_02);
        assertEquals(tempArray, SearchCrimeCollection.search("DEPARTMENT STORE").getCrimes());
    }

    /**
     * Assert that searching with sub string find string that are conatin the substring
     */
    @Test
    public void searchWithSubString() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_05);
        tempArray.add(testCR_06);
        assertEquals(tempArray, SearchCrimeCollection.search("ba").getCrimes());
    }

    /**
     * Assert that searching checks all the columns in the table
     */
    @Test
    public void searchWithAllColumns() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_01);
        tempArray.add(testCR_04);
        assertEquals(tempArray, SearchCrimeCollection.search("mall").getCrimes());
    }


    /**
     * Assert that searching checks all the columns in the table
     */
    @Test
    public void searchMultiParameter() {
        CrimeCollectionManager.setCurrWorkingCollection(testCC_01);
        ArrayList<CrimeRecord> tempArray = new ArrayList<CrimeRecord>();
        tempArray.add(testCR_02);
        tempArray.add(testCR_06);
        assertEquals(tempArray, SearchCrimeCollection.search("DEPARTMENT STORE, HX111116").getCrimes());
    }
}
