package seng202.unitTests;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import crimeSpy.crimeData.CrimeRecord;
import crimeSpy.crimeData.OR_Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit Tests (jUNit4) for OR_Map test.<br>
 * <u>Note: </u><br>
 * Provides a 1:1 mapping of a CrimeRecord object into a structure that can be passes
 * into a DAL (data access layer) for easy writing and vice versa.
 */
public class OR_MapTest {

    // CrimeRecord object to test
    private final String caseID = "HX321538";
    private DateTime date = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a").parseDateTime("6/27/2014 7:31:00 PM");
    private Boolean arrest = true;
    private Boolean domestic = false;
    private String iucr = "666";
    private String fbiCD = "0";
    private String priDesc = "PRIMARY CRIME DESCRIPTION";
    private String secDesc = "SECONDARY CRIME DESCRIPTION";
    private String block = "111111 A BLOCK ADDRESS";
    private Integer beat = 111;
    private Integer ward = 22;
    private double xCoord = 123456789;
    private double yCoord = 987654321;
    private double longitude = -11.1111111111111;
    private double latitude =   22.2222222222222;
    private String location = "LOCATION STRING";
    private CrimeRecord cr;
    OR_Map map;

    /**
     * Sets up a single crimeRecord object and then
     * builds OR_Map object with it for testing purposes
     */
    @Before
    public void setUp() {
        cr = new CrimeRecord(caseID, date, arrest, domestic, iucr, fbiCD, block, beat, ward, xCoord, yCoord, latitude, longitude, location);
        map = new OR_Map(cr);
    }


    @After
    public void tearDown() throws Exception {

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILITY
    // Notes:
    //  - These will most likely require acceptance testing as is is very difficult without
    //    hardcoding expected responses to determine if the return string is correct in a given context
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Check that a standard OR_Map.toDBCreateString does not
     * contain any " characters (which would break the script!)
     */
    @Test
    public void test_toDBCreateString_quoteChar() {
        String dbStr = map.toDBCreateString();
        Integer expectedIndex = -1;
        Integer index = dbStr.indexOf("\"");
        String errMsg = "The DB string constructor returned a string with a \" character in it at index: " + index;
        assertEquals(errMsg, expectedIndex, index);
    }


    /**
     * Check that a standard OR_Map.toCSVString does not
     * contain any ' characters (which could break the script!)
     */
    @Test
    public void test_toCSVString_quoteChar() {
        String csvString = map.toCSVString();
        Integer expectedIndex = -1;
        Integer index = csvString.indexOf("'");
        String errMsg = "The CSV string constructor returned a string with a ' character in it at index: " + index;
        assertEquals(errMsg, expectedIndex, index);
    }


    /**
     * Check that a standard OR_Map.toDBUpdateString does not
     * contain any " characters (which would break the script!)
     */
    @Test
    public void test_toDBUpdateString_quoteChar() {
        String dbUpdateStr = map.toDBUpdateString();
        Integer expectedIndex = -1;
        Integer index = dbUpdateStr.indexOf("\"");
        String errMsg = "The DB string constructor returned a string with a \" character in it at index: " + index;
        assertEquals(errMsg, expectedIndex, index);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Test that the ID is retrieved correctly
     */
    @Test
    public void test_GetId() {
        String errMsg = "The caseID in the or map does not match the caseID in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCaseID(), map.getId());
    }


    /**
     * Test that the date is retrieved and stored correctly
     */
    @Test
    public void test_GetDate() {
        String errMsg = "The date in the or map does not match the date in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getDate(), map.getDate());
    }


    /**
     * Check that if the arrest state is true then the relational map has stored a "1"
     */
    @Test
    public void test_GetArrest_true() {
        String errMsg = "The arrest state in the or map does not match '1' for true.";
        cr.setArrest(true);
        map = new OR_Map(cr);
        assertEquals(errMsg, "1", map.getArrest());
    }


    /**
     * Check that if the arrest state is false then the relational map has stored a "0"
     */
    @Test
    public void test_GetArrest_false() {
        String errMsg = "The arrest state in the or map does not match '0' for false.";
        cr.setArrest(false);
        map = new OR_Map(cr);
        assertEquals(errMsg, "0", map.getArrest());
    }


    /**
     * Check that if the domestic state is true then the relational map has stored a "1"
     */
    @Test
    public void test_GetDomestic_true() {
        String errMsg = "The arrest state in the or map does not match '1' for true.";
        cr.setDomestic(true);
        map = new OR_Map(cr);
        assertEquals(errMsg, "1", map.getDomestic());
    }


    /**
     * Check that if the domestic state is false then the relational map has stored a "0"
     */
    @Test
    public void test_GetDomestic_false() {
        String errMsg = "The arrest state in the or map does not match '0' for false.";
        cr.setDomestic(false);
        map = new OR_Map(cr);
        assertEquals(errMsg, "0", map.getDomestic());
    }


    /**
     * Test that the iucr number is stored and retrieved correctly
     */
    @Test
    public void test_GetIucr() {
        String errMsg = "The iucr number in the or map does not match the iucr number in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeType().getIucr(), map.getIucr());
    }


    /**
     * Test that the FBI CD number is stored and retrieved correctly
     */
    @Test
    public void test_GetFbiCD() {
        String errMsg = "The FBI CD number in the or map does not match the FBICD in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getFbiCD(), map.getFbiCD());
    }


    /**
     * Test that the primary description is stored and retrieved correctly
     */
    @Test
    public void test_GetPriDesc() {
        String errMsg = "The primary description in the or map does not match the primary description in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeType().getPrimaryDescription(), map.getPriDesc());
    }


    /**
     * Test that the secondary description is stored and retrieved correctly
     */
    @Test
    public void test_GetSecDesc() {
        String errMsg = "The secondary description in the or map does not match the secondary description in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeType().getSecondaryDescription(), map.getSecDesc());
    }


    /**
     * Test that the block is stored and retrieved correctly
     * from and to the OR_Map
     */
    @Test
    public void test_GetBlock() {
        String errMsg = "The block information in the or map does not match the block information in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeLocation().getBlock(), map.getBlock());
    }


    /**
     * Test that the beat is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the beat is stored as an Integer
     * while in the OR_Map it is stored as a string.
     */
    @Test
    public void test_GetBeat() {
        String errMsg = "The beat information in the or map does not match the beat information in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeLocation().getBeat().toString(), map.getBeat());
    }


    /**
     * Test that the ward is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the ward is stored as an Integer
     * while in the OR_Map it is stored as a string.
     */
    @Test
    public void test_GetWard() {
        String errMsg = "The ward information in the or map does not match the ward information in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeLocation().getWard().toString(), map.getWard());
    }


    /**
     * Test that the XCoord is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the XCoord is stored as an primitive double
     * while in the OR_Map it is stored as a string - > hence conversion.
     */
    @Test
    public void test_getXCoord() {
        String errMsg = "The XCoord in the or map does not match the XCoord in the original CrimeRecord object.";
        assertEquals(errMsg, String.valueOf(cr.getCrimeLocation().getxCoordinate()), map.getxCoord());
    }


    /**
     * Test that the YCoord is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the YCoord is stored as an primitive double
     * while in the OR_Map it is stored as a string - > hence conversion.
     */
    @Test
    public void test_getYCoord() throws Exception {
        String errMsg = "The YCoord in the or map does not match the YCoord in the original CrimeRecord object.";
        assertEquals(errMsg, String.valueOf(cr.getCrimeLocation().getyCoordinate()), map.getyCoord());
    }


    /**
     * Test that the longitude is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the longitude is stored as an primitive double
     * while in the OR_Map it is stored as a string - > hence conversion.
     */
    @Test
    public void test_getLongitude() throws Exception {
        String errMsg = "The longitude in the or map does not match the longitude in the original CrimeRecord object.";
        assertEquals(errMsg, String.valueOf(cr.getCrimeLocation().getLongitude()), map.getLongitude());
    }


    /**
     * Test that the latitude is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the latitude is stored as an primitive double
     * while in the OR_Map it is stored as a string - > hence conversion.
     */
    @Test
    public void test_getLatitude() throws Exception {
        String errMsg = "The latitude in the or map does not match the latitude in the original CrimeRecord object.";
        assertEquals(errMsg, String.valueOf(cr.getCrimeLocation().getLatitude()), map.getLatitude());
    }


    /**
     * Test that the LocationStr is stored and retrieved correctly
     * from and to the OR_Map.<br>
     */
    @Test
    public void test_getLocationStr() throws Exception {
        String errMsg = "The LocationStr in the or map does not match the LocationStr in the original CrimeRecord object.";
        assertEquals(errMsg, cr.getCrimeLocation().getLocationStr(), map.getLocationStr());
    }


    /**
     * Test that the LatLong tuple is stored and retrieved correctly
     * from and to the OR_Map.<br>
     * Note that in the Crime Record object the LatLong is stored as an primitive double
     * tuple, while in the OR_Map it is stored as a string - > hence conversion.
     */
    @Test
    public void test_getLatLong() throws Exception {
       String expected =    "(" + String.valueOf(cr.getCrimeLocation().getLatitude()) + ", " +
                            String.valueOf(cr.getCrimeLocation().getLongitude()) + ")";
       String errMsg = "The LocationStr in the or map does not match the LocationStr in the original CrimeRecord object.";
       assertEquals(errMsg, expected, map.getLatLong());
    }


}