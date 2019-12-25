package seng202.unitTests;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import seng202.crimeSpy.crimeData.CrimeType;
import seng202.crimeSpy.crimeData.SQLiteDBHandler;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Unit tests for CrimeType class<br>
 * <u>Notes:</u>
 * <ul>
 *     <li>The validation restriction for IUCR code are listed in the Crime type class</li>
 *     <li>If you are looking for the FBI cd it has been shifted to CrimeRecord class</li>
 *     <li>This class uses mocking in order to seperate the test from the functionality
 *     of the SQLiteDBHandler, in particular the readCrimeType() static method which
 *     returns a String array</li>
 * </ul>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SQLiteDBHandler.class)
public class CrimeTypeTest {


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETUP
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String Iucr = "111A";
    private String testPrimaryDescription = "testPrimaryDescription";
    private String testSecondaryDescription = "testSecondaryDescription";
    private CrimeType validCrType;


    /**
     * Creation of valid CrimeType object for testing purposes
     */
    @Before
    public void setUp() {
        String[] descriptionArray = {testPrimaryDescription, testSecondaryDescription};
        ArrayList<String> result = new ArrayList<>(Arrays.asList(descriptionArray));
        // Let PowerMock know to mock all static methods in SQLiteDBHandler
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        PowerMockito.when(SQLiteDBHandler.getCrimeTypeData(Mockito.anyString())).thenReturn(result);
        validCrType = new CrimeType(Iucr);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILITY FUNCTIONALITY
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Check Capitalisation for passed in IUCR code for lowercase
     * letters only
     */
    @Test
    public void test_capitaliseIucr_lowerCase() {
        CrimeType lowerCaseIucr_crimeType = new CrimeType("aa");
        lowerCaseIucr_crimeType.capitaliseIucr();
        String errMsg = "capitaliseIucr(String iucr) did not correctly capitalise the IUCR code with lowercase letters.";
        assertEquals(errMsg, "AA", lowerCaseIucr_crimeType.getIucr());
    }


    /**
     * Check Capitalisation for passed in IUCR code for mixed lowercase
     * and numerals
     */
    @Test
    public void test_capitaliseIucr_alphaNum() {
        CrimeType lowerCaseIucr_crimeType = new CrimeType("a1a1");
        lowerCaseIucr_crimeType.capitaliseIucr();
        String errMsg = "capitaliseIucr(String iucr) did not correctly capitalise the IUCR code for lowercase alphanumerical iucr.";
        assertEquals(errMsg, "A1A1", lowerCaseIucr_crimeType.getIucr());
    }


    /**
     * Check Capitalisation for passed in IUCR code with mixed capitals
     * and numerals
     */
    @Test
    public void test_capitaliseIucr_mixedCaps_alphaNum() {
        CrimeType lowerCaseIucr_crimeType = new CrimeType("b1B1");
        lowerCaseIucr_crimeType.capitaliseIucr();
        String errMsg = "capitaliseIucr(String iucr) did not correctly capitalise the IUCR code for mixed case with numerals.";
        assertEquals(errMsg, "B1B1", lowerCaseIucr_crimeType.getIucr());
    }


    /**
     * Check Capitalisation for passed in IUCR code with mixed capitals
     * and numerals
     */
    @Test
    public void test_capitaliseIucr_mixedCaps() {
        CrimeType lowerCaseIucr_crimeType = new CrimeType("bCBc");
        lowerCaseIucr_crimeType.capitaliseIucr();
        String errMsg = "capitaliseIucr(String iucr) did not correctly capitalise the IUCR code for mixed case.";
        assertEquals(errMsg, "BCBC", lowerCaseIucr_crimeType.getIucr());
    }


    /**
     * Check Capitalisation for passed in IUCR code with mixed capitals
     * and numerals
     */
    @Test
    public void test_capitaliseIucr_upperCase() {
        CrimeType lowerCaseIucr_crimeType = new CrimeType("BCBC");
        lowerCaseIucr_crimeType.capitaliseIucr();
        String errMsg = "capitaliseIucr(String iucr) did not correctly capitalise the IUCR code for upper case only.";
        assertEquals(errMsg, "BCBC", lowerCaseIucr_crimeType.getIucr());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION: object creation
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Tests for correct behaviour of IUCR code with incorrect characters
     * @throws IllegalArgumentException should be thrown for intentionally wrong IUCR number
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalFormatIucr_2() {
        String illegalIucr = "AA-A";
        CrimeType crType = new CrimeType(illegalIucr);
    }


    /**
     * Tests for correct behaviour of IUCR code with too few characters
     * @throws IllegalArgumentException should be thrown for intentionally short IUCR number
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShortIucr_1() {
        String shortIucr = "2";
        CrimeType crType = new CrimeType(shortIucr);
    }


    /**
     * Tests for correct behaviour of IUCR code with too few characters
     * @throws IllegalArgumentException should be thrown for intentionally short IUCR number
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShortIucr_2() {
        String shortIucr = "X";
        CrimeType crType = new CrimeType(shortIucr);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATION
    // Testing: setting attributes
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Attempt to set an IUCR code to an illegal value should throw an exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIucrToIllegalValue() {
        String shortIucr = "{1111";
        validCrType.setIucr(shortIucr);
    }


    /**
     * <u>Note:</u>
     * This test also tests the validity of the testSetIucrToIllegalValue1 test. <br>
     * An attempt to set an IUCR code to a legal value should NOT throw an exception
     */
    @Test
    public void testSetIucrToLegalValue() {
        String legalIucr = "1111";
        try {
            validCrType.setIucr(legalIucr);
        }
        catch(IllegalArgumentException e) {
            fail();
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS
    // These can be run to confirm the validity of using the getter for other tests as
    // a comparison
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Test the iucr getter<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_getIucr() {
        // original iucr defined at top as testIucr
        String errMsg = "getIucr method did not return expected value";
        assertEquals(errMsg, validCrType.getIucr(), Iucr);
    }


    /**
     * Test the primary description getter.<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_getPrimaryDescription() {
        // original iucr defined at top as testIucr
        String errMsg = "getPrimaryDescription method did not return expected value";
        assertEquals(errMsg, validCrType.getPrimaryDescription(), testPrimaryDescription);
    }


    /**
     * Test the secondary description getter.<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_getSecondaryDescription() {
        // original iucr defined at top as testIucr
        String errMsg = "getSecondaryDescription() method did not return expected value";
        assertEquals(errMsg, validCrType.getSecondaryDescription(), testSecondaryDescription);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Test the setter for CrimeType (iucr code).<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method.
     */
    @Test
    public void test_setCrimeType() {
        // original defined at top
        String newIucr = "222B";
        String[] resString = {"testPrimaryDesciption", "testSecondaryDesciption"};
        // Let PowerMock know to mock all static methods in SQLiteDBHandler
        ArrayList<String> result = new ArrayList<>(Arrays.asList(resString));
        // Let PowerMock know to mock all static methods in SQLiteDBHandler
        PowerMockito.mockStatic(SQLiteDBHandler.class);
        PowerMockito.when(SQLiteDBHandler.getCrimeTypeData(Mockito.anyString())).thenReturn(result);

        validCrType.setCrimeType(newIucr);
        String errMsg = "setCrimeType() method did not work as expected for " + newIucr;
        assertEquals(errMsg, validCrType.getIucr(), newIucr);
    }


    /**
     * Test the setter for primary description.<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_setPrimaryDescription() {
        // original defined at top
        String newPrimaryDescription = "This is a new primary description";
        validCrType.setPrimaryDescription(newPrimaryDescription);
        String errMsg = "setPrimaryDescription() method did not set properly";
        assertEquals(errMsg, validCrType.getPrimaryDescription(), newPrimaryDescription);
    }


    /**
     * Test the setter for the secondary description.<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_setSecondaryDescription() {
        // original defined at top
        String newSecondaryDescription = "This is a new secondary description";
        validCrType.setSecondaryDescription(newSecondaryDescription);
        String errMsg = "setSecondaryDescription() method did not work as expected: not set properly";
        assertEquals(errMsg, validCrType.getSecondaryDescription(), newSecondaryDescription);
    }



    /**
     * Test that setting the primary description does not alter the
     * iucr code or secondary description.<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_setPrimaryDescription_NoAdverseEffect() {
        String errMsg = "setPrimaryDescription() method did not work as expected: altered data it shouldn't have.";
        // Set
        String newPrimaryDescription = "This is a new secondary description";
        validCrType.setPrimaryDescription(newPrimaryDescription);
        // Check no changes
        assertEquals(errMsg, validCrType.getSecondaryDescription(), testSecondaryDescription);
        assertEquals(errMsg, validCrType.getIucr(), Iucr);
    }


    /**
     * Test that setting the primary description does not alter the
     * iucr code or secondary description.<br>
     * The database functionality is mocked for this test: specifically the
     * static readCrimeType() method during creation (see @Before)
     */
    @Test
    public void test_setSecondaryDescription_NoAdverseEffect() {
        String errMsg = "setSecondaryDescription() method did not work as expected: altered data it shouldn't have.";
        // Set
        String newSecondaryDescription = "This is a new secondary description";
        validCrType.setSecondaryDescription(newSecondaryDescription);
        // Check no changes
        assertEquals(errMsg, validCrType.getPrimaryDescription(), testPrimaryDescription);
        assertEquals(errMsg, validCrType.getIucr(), Iucr);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPRECATED
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * <u>Note:</u><br>
     * Tests for correct behaviour of IUCR code with incorrect characters
     * @deprecated The method validateIucr() is now public and should be tested directly
     */
    @Deprecated
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalFormatIucr_1() {
        String illegalIucr = "11 2A!";
        CrimeType crType = new CrimeType(illegalIucr);
    }


    /**
     * Check Capitalisation for passed in IUCR code
     * @deprecated use capitaliseIucr() - no parameter method
     */
    @Deprecated
    @Test
    public void test_capitaliseIucr_oneLetter_DEP() {

        CrimeType lowerCaseIucr_crimeType = new CrimeType("aa");
        lowerCaseIucr_crimeType.capitaliseIucr(lowerCaseIucr_crimeType.getIucr());
        String errMsg = "capitaliseIucr(String iucr) did not correctly capitalise the IUCR code.";
        assertEquals(errMsg, lowerCaseIucr_crimeType.getIucr(), "AA");
    }



}
