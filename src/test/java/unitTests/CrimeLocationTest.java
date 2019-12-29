package seng202.unitTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import crimeSpy.crimeData.CrimeLocation;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * There are no uniqueness constraints on a CrimeLocation
 * There is no requirement at this stage for non empty fields.
 * Testing for getters and setters is primarily done for contract purposes.
 */
public class CrimeLocationTest {

    // A test CrimeLocation for generic use in tests
    // Will be compared with crLoc eg: [this.block == crLoc.getBlock()]
    private String block = "1111";
    private Integer beat = 11;
    private Integer ward = 11;
    private double xCoord = 11.1111;
    private double yCoord = 11.1111;
    private double latitude = 11.1111;
    private double longitude = 11.1111;
    private String locationStr = "1111";
    private CrimeLocation crLoc;

    // Acceptable difference between double values
    double delta = 0.0001;

    // For null value tests
    private CrimeLocation crLoc_withNulls;

    // Empty constuctor
    private CrimeLocation crLoc_empty;



    @Before
    public void setUp() throws Exception {
        crLoc = new CrimeLocation(block, beat, ward,xCoord, yCoord, latitude, longitude, locationStr);
        // primitive types can not be null
        crLoc_withNulls = new CrimeLocation(null, null, null, xCoord, yCoord, latitude, longitude, null);
        crLoc_empty = new CrimeLocation();

    }

    @After
    public void tearDown() throws Exception {
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Special Cases
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Tests that the constructor correctly creates the latLong array.
     */
    @Test
    public void test_Constructor_latLong() {
        double[] latLong = {latitude, longitude};
        String errMsg = "The latLong array should be a double array of size two that looks like: [latitude, longitude]";
        assertTrue(errMsg, Arrays.equals(crLoc.getLatLong(), latLong));
    }


    /**
     * Tests that updating longitude information also updates the required
     * data value in the latLong tuple. <br>
     * latLong : [latitude, longitude]
     */
    @Test
    public void test_setLatitude_alsoUpdates_latLong() {
        double newLat = 22.2222;
        crLoc.setLatitude(newLat);
        String errMsg = "setLatitude() should also update the latLong array.";
        // latitude is the 0-th element in the array
        assertEquals(errMsg, crLoc.getLatLong()[0], newLat, delta);
    }


    /**
     * Tests that updating longitude information also updates the required
     * data value in the latLong tuple. <br>
     * latLong : [latitude, longitude]
     */
    @Test
    public void test_setLongitude_alsoUpdates_latLong() {
        double newLong = 22.2222;
        crLoc.setLongitude(newLong);
        String errMsg = "setLongitude() should also update the latLong array.";
        // longitude is the 1-th element in the array
        assertEquals(errMsg, crLoc.getLatLong()[1], newLong, delta);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NULLS
    // this section also indirectly tests getters and setters for some data
    // Note the additional logic in the CrimeLocation setters to deal with nulls.
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Tests that passing in null strings to the
     * CrimeLocation constructor does not break
     */
    @Test
    public void test_constructor_NullStrings() {
        CrimeLocation uninitialised_crLoc = new CrimeLocation(null, null, null, xCoord, yCoord, latitude, longitude, null);
    }


    /**
     * Tests that passing in null block value to the CrimeLocation constructor,
     * followed by getting the block, should result in empty string returned.
     */
    @Test
    public void test_getBlock_NullStrings() {
        String errMsg = "Calling getBlock() where block was not initialised, did not return an empty string.";
        assertEquals("", "", crLoc_withNulls.getBlock());

    }


    /**
     * Tests that passing in null beat value to the CrimeLocation constructor,
     * followed by getting the beat, should result in Integer value of 0 returned.
     */
    @Test
    public void test_getBeat_NullStrings() {
        Integer expected = 0;
        String errMsg = "Calling getBeat() where beat was not initialised, did not return 0.";
        assertEquals(errMsg, expected, crLoc_withNulls.getBeat());

    }


    /**
     * Tests that passing in null ward value to the CrimeLocation constructor,
     * followed by getting the ward, should result in Integer value of 0 returned.
     */
    @Test
    public void test_getWard_NullStrings() {
        Integer expected = 0;
        String errMsg = "Calling getWard() where ward was not initialised, did not return 0.";
        assertEquals(errMsg, expected, crLoc_withNulls.getWard());

    }


    /**
     * Tests that passing in null locationString value to the CrimeLocation constructor,
     * followed by getting the locationString, should result in an empty string returned.
     */
    @Test
    public void test_getLocationStr_NullStrings() {
        String errMsg = "Calling getLocationStr() where locationString was not initialised, did not return an empty string.";
        assertEquals(errMsg, "", crLoc_withNulls.getLocationStr());

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS (of primitive data types)
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Test getxCoordinate();
     * Note for double precision value comparison it is better to assert that the
     * difference between values to be compared is minimal rather than directly compare
     * the two values. [It is noted this is now required in jUnit4
     */
    @Test
    public void test_getXCoordinate() {
        String errMsg = "The xCoordinate did not match the expected value for getxCoordinate().";
        assertEquals(errMsg, crLoc.getxCoordinate(), this.xCoord, delta);
    }


    /**
     * Test getyCoordinate();
     * Note for double precision value comparison it is better to assert that the
     * difference between values to be compared is minimal rather than directly compare
     * the two values. [It is noted this is now required in jUnit4
     */
    @Test
    public void test_getYCoordinate() {
        String errMsg = "The yCoordinate did not match the expected value for getyCoordinate().";
        assertEquals(errMsg, crLoc.getyCoordinate(), this.yCoord, delta);
    }


    /**
     * Test getLongitude();
     * Note for double precision value comparison it is better to assert that the
     * difference between values to be compared is minimal rather than directly compare
     * the two values. [It is noted this is now required in jUnit4
     */
    @Test
    public void test_getLongitude() {
        String errMsg = "The longitude did not match the expected value for getLongitude().";
        assertEquals(errMsg, crLoc.getLongitude(), this.longitude, delta);
    }


    /**
     * Test getLatitude();
     * Note for double precision value comparison it is better to assert that the
     * difference between values to be compared is minimal rather than directly compare
     * the two values. [It is noted this is now required in jUnit4
     */
    @Test
    public void test_getLatitude() {
        String errMsg = "The latitude did not match the expected value for getLatitude().";
        assertEquals(errMsg, crLoc.getLatitude(), this.latitude, delta);
    }


    /**
     * Test getLatLong();
     */
    @Test
    public void test_getLatLong() {
        String errMsg = "The latLong array did not match the expected value for getLatLong().";
        double[] expected = {this.latitude, this.longitude};
        assertTrue(errMsg, Arrays.equals(crLoc.getLatLong(), expected));
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS
    // Some setters contain additional logic
    // Again note there are currently no restriction on modification for the CrimeLocation class (set away)
    // Getters should be tested before this so that the use of the getters is acceptable in the test
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Test block setter for initialised CrimeLocation object;
     */
    @Test
    public void test_setBlock() {
        String newValue = "New Block";
        crLoc.setBlock(newValue);
        String errMsg = "The set value did not match for block information in CrimeLocation";
        assertEquals(errMsg, crLoc.getBlock(), newValue);
    }


    /**
     * Test block setter for uninitialised CrimeLocation object;
     */
    @Test
    public void test_setBlock_emptyCrimeLocation() {
        String newValue = "New Block";
        crLoc_empty.setBlock(newValue);
        String errMsg = "The set value did not match for block information in an uninitialised CrimeLocation";
        assertEquals(errMsg, crLoc_empty.getBlock(), newValue);
    }


    /**
     * Test beat setter for an initialised CrimeLocation object;
     */
    @Test
    public void test_setBeat() {
        Integer newValue = 222;
        crLoc.setBeat(newValue);
        String errMsg = "The set value did not match for block information in CrimeLocation";
        assertEquals(errMsg, crLoc.getBeat(), newValue);
    }


    /**
     * Test the beat setter for an uninitialised CrimeLocation object;
     */
    @Test
    public void test_setBeat_emptyCrimeLocation() {
        Integer newValue = 222;
        crLoc_empty.setBeat(newValue);
        String errMsg = "The set value did not match for block information in an uninitialised CrimeLocation.";
        assertEquals(errMsg, crLoc_empty.getBeat(), newValue);
    }



    /**
     * Test the ward setter for initialised CrimeLocation object;
     */
    @Test
    public void test_setWard() {
        Integer newValue = 222;
        crLoc.setWard(newValue);
        String errMsg = "The set value did not match for ward information in CrimeLocation";
        assertEquals(errMsg, crLoc.getWard(), newValue);
    }


    /**
     * Test the ward setter for an uninitialised CrimeLocation object;
     */
    @Test
    public void test_setWard_emptyCrimeLocation() {
        Integer newValue = 222;
        crLoc_empty.setWard(newValue);
        String errMsg = "The set value did not match for ward information in an uninitialised CrimeLocation object.";
        assertEquals(errMsg, crLoc_empty.getWard(), newValue);
    }


    /**
     * Test the xCoordinate setter for initialised CrimeLocation object;
     */
    @Test
    public void test_setXCoordinate() {
        double newValue = 22.2222;
        crLoc.setxCoordinate(newValue);
        String errMsg = "The set value did not match the xCoordinate information in CrimeLocation";
        assertEquals(errMsg, crLoc.getxCoordinate(), newValue, delta);
    }


    /**
     * Test the xCoordinate setter for an uninitialised CrimeLocation object;
     */
    @Test
    public void test_setXCoordinate_emptyCrimeLocation() {
        double newValue = 22.2222;
        crLoc_empty.setxCoordinate(newValue);
        String errMsg = "The set value did not match the xCoordinate information in CrimeLocation";
        assertEquals(errMsg, crLoc_empty.getxCoordinate(), newValue, delta);
    }


    /**
     * Test yCoordinate setter for initialised CrimeLocation object;
     */
    @Test
    public void test_setYCoordinate() {
        double newValue = 22.2222;
        crLoc.setyCoordinate(newValue);
        String errMsg = "The set value did not match the yCoordinate information in CrimeLocation";
        assertEquals(errMsg, crLoc.getyCoordinate(), newValue, delta);
    }


    /**
     * Test yCoordinate setter for uninitialised CrimeLocation object;
     */
    @Test
    public void test_setYCoordinate_emptyCrimeLocation() {
        double newValue = 22.2222;
        crLoc_empty.setyCoordinate(newValue);
        String errMsg = "The set value did not match the yCoordinate information inan uninitialised CrimeLocation.";
        assertEquals(errMsg, crLoc_empty.getyCoordinate(), newValue, delta);
    }


    /**
     * Test the longitude setter for initialised CrimeLocation object;
     */
    @Test
    public void test_setLongitude() {
        double newValue = 22.2222;
        crLoc.setLongitude(newValue);
        String errMsg = "The set value did not match the longitude information in CrimeLocation";
        assertEquals(errMsg, crLoc.getLongitude(), newValue, delta);
    }


    /**
     * Test the longitude setter for uninitialised CrimeLocation object;
     */
    @Test
    public void test_setLongitude_emptyCrimeLocation() {
        double newValue = 22.2222;
        crLoc_empty.setLongitude(newValue);
        String errMsg = "The set value did not match the longitude information in an uninitialised CrimeLocation.";
        assertEquals(errMsg, crLoc_empty.getLongitude(), newValue, delta);
    }


    /**
     * Test the longitude setter for initialised CrimeLocation object;
     */
    @Test
    public void test_setLatitude() {
        double newValue = 22.2222;
        crLoc.setLatitude(newValue);
        String errMsg = "The set value did not match the longitude information in CrimeLocation";
        assertEquals(errMsg, crLoc.getLatitude(), newValue, delta);
    }


    /**
     * Test the longitude setter for an uninitialised CrimeLocation object;
     */
    @Test
    public void test_setLatitude_emptyCrimeLocation() {
        double newValue = 22.2222;
        crLoc_empty.setLatitude(newValue);
        String errMsg = "The set value did not match the longitude information in an uninitialised CrimeLocation object.";
        assertEquals(errMsg, crLoc_empty.getLatitude(), newValue, delta);
    }
}