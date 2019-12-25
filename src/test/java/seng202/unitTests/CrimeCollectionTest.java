package seng202.unitTests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import seng202.crimeSpy.crimeData.CrimeCollection;
import seng202.crimeSpy.crimeData.CrimeRecord;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for CrimeCollection class
 * <u>Notes:</u>
 * <ul>
 *     <li>There are constraints on uniqueness for caseId in the collection</li>
 *     <li>This class also uses object from CrimeRecord</li>
 * </ul>
 */
public class CrimeCollectionTest {

    // For legitimate crime records
    private DateTime date01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
    private DateTime date02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("7/27/2014 7:31:00 PM");
    private CrimeRecord testCR_01;
    private CrimeRecord testCR_02;
    private CrimeCollection testCC_01;

    private ArrayList<CrimeRecord> crimeArrayList = new ArrayList<>();


    /**
     * Set up two unique CrimeRecords and an empty CrimeCollection
     * @throws Exception General exception
     */
    @Before
    public void setUp() throws Exception {
        testCR_01 = new CrimeRecord("HX111111", date01, true, true, "111", "5", "080XX S " +
                "HALSTED ST",
                    621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCR_02 = new CrimeRecord("HX111112", date02, true, false, "112", "6", "080XX S " +
                "HALSTED ST",
                    621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCC_01 = new CrimeCollection(); //empty CrimeCollection

        crimeArrayList.add(testCR_01);
        crimeArrayList.add(testCR_02);
    }


    /**
     * Assert that it is possible to add a CrimeRecord to a CrimeCollection
     * @throws Exception General exception
     */
    @Test
    public void testAddCrimeRecord_1() throws Exception {
        testCC_01.addCrimeRecord(testCR_01);
    }

    /**
     * Tests the constructor that accepts name, id and directory.
     */
    @Test
    public void testPopulatedConstructor() {
        String name = "Ben is the greatest";
        Integer id = 8008135;
        String directory = "I wish I could be more like Ben";
        // These don't actually have to be valid names - it's just for testing

        CrimeCollection testCC_03 = new CrimeCollection(name, id, directory);
        assertEquals(name, testCC_03.getName());
        assertEquals(id, testCC_03.getId());
        assertEquals(directory, testCC_03.getDirectory());
    }

    /**
     * Tests the name setter
     */
    @Test
    public void testSetName() {
        String name = "I am legitimately blown away by how attractive Ben is. It's simply mindblowing";
        testCC_01.setName(name);
        assertEquals(name, testCC_01.getName());
    }

    /**
     * Tests the ID setter
     */
    @Test
    public void testSetID() {
        Integer ID = 80085;
        testCC_01.setId(ID);
        assertEquals(ID, testCC_01.getId());
    }

    /**
     * Tests the directory setter
     */
    @Test
    public void testSetDirectory() {
        String directory = "Every time I see Ben, it adds just that little extra bit of joy into my life.";
        testCC_01.setDirectory(directory);
        assertEquals(directory, testCC_01.getDirectory());
    }

    /**
     * Assert that it is possible to add two unique crimes CrimeRecord to a CrimeCollection
     * @throws Exception General exception
     */
    @Test
    public void testAddCrimeRecord_2() throws Exception {
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
    }


    /**
     * Assert that it is <b>NOT</b> possible to add more than one of the same CrimeRecord to a CrimeCollection. <br />
     * Functional Requirement <b>CR02</b>
     * @throws Exception General exception
     */
    @Test
    public void testAddSameCrimeRecord() throws Exception {
        assertTrue(testCC_01.addCrimeRecord(testCR_01) == 0);
        assertTrue(testCC_01.addCrimeRecord(testCR_01) == 1); // TestCC_01 has returned the error code
    }

    /**
     *Assert that sorting by time doesn't irreparably fuck up everything (aka throw an exception).
     * Apparently we can write tests for this! If only I'd known this earlier - Ben
     * @throws Exception General exception
     */
    @Test
    public void sortingTest() throws Exception {
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_01.sortCrimeRecordsByTime();
    }

    @Test
    public void sortingTestEmptyList() {
        assertTrue(testCC_01.sortCrimeRecordsByTime() == 1);
    }

    /**
     * Assert that sorting by when there is a crime value with a null pointer that sort still works
     * @throws Exception General exception
     */
    @Test
    public void sortingTestNullPointer() throws Exception {
        CrimeRecord testCR_temp = new CrimeRecord("HX111112", null, true, false, "112", "6",
                "080XX S " +
                "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCC_01.addCrimeRecord(testCR_temp);
        testCC_01.sortCrimeRecordsByTime();
    }

    /**
     *Assert that sorting by time sort from smallest to largest
     * @throws Exception General exception
     */
    @Test
    public void sortingTestSmallLarge() throws Exception {
        DateTime date_temp01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("8/27/2014 7:31:00" +
                " PM");
        DateTime date_temp02 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("9/27/2014 7:31:00" +
                " PM");
        DateTime date_temp03 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("10/27/2014 7:31:00" +
                " PM");
        CrimeRecord testCR_temp01 = new CrimeRecord("HX111113", date_temp01, true, false, "112", "6",
                "080XX S " +
                        "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeRecord testCR_temp02 = new CrimeRecord("HX111114", date_temp02, true, false, "112", "6",
                "080XX S " +
                        "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        CrimeRecord testCR_temp03 = new CrimeRecord("HX111115", date_temp03, true, false, "112", "6",
                "080XX S " +
                        "HALSTED ST",
                621, 21, 1172409 ,1851438, 41.8808655731203,  -87.7058761048492, "DEPARTMENT STORE");
        testCC_01.addCrimeRecord(testCR_temp01);
        testCC_01.addCrimeRecord(testCR_temp02);
        testCC_01.addCrimeRecord(testCR_temp03);
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_01.sortCrimeRecordsByTime();
        assertEquals(true, testCC_01.getCrimes().get(0).getDate().getMillis() - testCC_01.getCrimes().get(1).getDate
                ().getMillis() < 0 &&
                testCC_01.getCrimes().get(1).getDate().getMillis() - testCC_01.getCrimes().get(2).getDate().getMillis() < 0 &&
                testCC_01.getCrimes().get(1).getDate().getMillis() - testCC_01.getCrimes().get(2).getDate().getMillis() < 0 &&
                testCC_01.getCrimes().get(2).getDate().getMillis() - testCC_01.getCrimes().get(3).getDate().getMillis() < 0 &&
                testCC_01.getCrimes().get(3).getDate().getMillis() - testCC_01.getCrimes().get(4).getDate().getMillis() < 0);
    }


        /* WE NO LONGER SET THE NEXT CRIME TIME OR DIST HERE, TAKES TOO LONG
    RETRIEVE IT AT RUNTIME
    @Test
    /**
     *Assert that sorting by time sort populates nextCrimeTimeSince except for the last crimeRecord
     * @throws Exception General exception
     **
    public void sortingTest04() throws Exception {
        DateTime date_temp01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("8/27/2014 7:31:00" +
                " PM");
        CrimeRecord testCR_temp01 = new CrimeRecord("HX111113", date_temp01, true, false, "112", "6",
                "080XX S " +
                        "HALSTED ST",
                621, 21, 1172409, 1851438, 41.8808655731203, -87.7058761048492, "DEPARTMENT STORE");
        testCC_01.addCrimeRecord(testCR_temp01);
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_01.sortCrimeRecordsByTime();
        assertEquals(true, testCR_01.getNextCrimeTimeSince() != null && testCR_02.getNextCrimeTimeSince() != null &&
                testCR_temp01.getNextCrimeTimeSince() == null);
    }

    /**
     *Assert that sorting by time sort populates nextCrimeDistance except for the last crimeRecord
     * @throws Exception General exception
     **/
        /* WE NO LONGER SET THE NEXT CRIME TIME OR DIST HERE, TAKES TOO LONG
    RETRIEVE IT AT RUNTIME
    @Test
    public void sortingTest05() throws Exception {
        DateTime date_temp01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("8/27/2014 7:31:00" +
                " PM");
        CrimeRecord testCR_temp01 = new CrimeRecord("HX111113", date_temp01, true, false, "112", "6",
                "080XX S " +
                        "HALSTED ST",
                621, 21, 1172409, 1851438, 41.8808655731203, -87.7058761048492, "DEPARTMENT STORE");
        testCC_01.addCrimeRecord(testCR_temp01);
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_01.sortCrimeRecordsByTime();
        assertEquals(true, testCR_01.getNextCrimeDistance() != null && testCR_02.getNextCrimeDistance() != null &&
                testCR_temp01.getNextCrimeDistance() == null);
    }

    /**
     *Assert that sorting by time links in next and prev crimeRecord id
     * @throws Exception General exception
     */
        /* WE NO LONGER SET THE NEXT CRIME TIME OR DIST HERE, TAKES TOO LONG
    RETRIEVE IT AT RUNTIME
    @Test
    public void sortingTest06() throws Exception {
        DateTime date_temp01 = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("8/27/2014 7:31:00" +
                " PM");
        CrimeRecord testCR_temp01 = new CrimeRecord("HX111113", date_temp01, true, false, "112", "6",
                "080XX S " +
                        "HALSTED ST",
                621, 21, 1172409, 1851438, 41.8808655731203, -87.7058761048492, "DEPARTMENT STORE");
        testCC_01.addCrimeRecord(testCR_temp01);
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        testCC_01.sortCrimeRecordsByTime();
        assertEquals(true, testCR_01.getNextCrimeID().equals(testCR_02.getCaseID()) &&
                testCR_02.getPrevCrimeID().equals(testCR_01.getCaseID()) &&
                testCR_02.getNextCrimeID().equals(testCR_temp01.getCaseID()) &&
                testCR_temp01.getPrevCrimeID().equals(testCR_02.getCaseID()));
    }
    */

    /**
     * Checks that the ObservableList returned by getObservableList() contains all of the objects
     * contained in the ArrayList behind CrimeCollection.
     * @throws Exception
     */
    @Test
    public void getObservableCrListTest() throws Exception {
        testCC_01.addCrimeRecord(testCR_01);
        testCC_01.addCrimeRecord(testCR_02);
        ObservableList<CrimeRecord> expected = FXCollections.observableArrayList(crimeArrayList);
        assertEquals(expected, testCC_01.getObservableCrList());
        }

    }


