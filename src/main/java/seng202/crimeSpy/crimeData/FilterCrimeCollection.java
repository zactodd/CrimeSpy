package seng202.crimeSpy.crimeData;


import org.joda.time.DateTime;

import java.util.ArrayList;


/**
 * filters a crimeCollection based on filter crime information specified by the user and produces
 * and filterOptions (not implement yet) to produce a filtered CrimeCollection
 */
public class FilterCrimeCollection {


    /**
     * Return a filtered version of the current collection
     * @param domestic boolean description of whether there has been a domestic in this crime
     * @param arrest boolean description of whether there has been an arrest in this crime
     * @param crimeTypes ArrayList of the CrimeTypes to filter by
     * @param location Location description to filter by
     * @param beat beat location to filter by
     * @param ward ward location to filter by
     */
    public static void filter(Boolean domestic, Boolean arrest, ArrayList<String> crimeTypes,
                                  String location, int beat, int ward, String block, String primary,
                                  String secondary, String IUCR, String FBICD, DateTime to, DateTime from) {
        CrimeCollection filterCollection = new CrimeCollection();
        ArrayList<CrimeRecord> crimeRecords = CrimeCollectionManager.getFullWorkingCollection().getCrimes();
        for (CrimeRecord cR : crimeRecords) {
            if (isFilter(cR, domestic, arrest, crimeTypes, location, beat, ward, block, primary, secondary, FBICD,
                    IUCR, to, from)) {
                filterCollection.addCrimeRecord(cR);

            }
        }

        CrimeCollectionManager.setFilteredCollection(filterCollection);
    }


    /**
     * @param checkBool the Boolean to be checked
     * @param trueBool  the Boolean that may equal checkBool
     * @return true if checkBool equals trueBool or trueBool is null otherwise return bool
     */
    private static Boolean boolMatch(Boolean checkBool, Boolean trueBool) {
        return checkBool == trueBool || trueBool == null;
    }


    /**
     * @param checkInt the int to be checked
     * @param trueInt  the int that may equal checkInt
     * @return if checkInt equals trueInt or trueInt is negative otherwise return false
     */
    private static Boolean intMatch(int checkInt, int trueInt) {
        return checkInt == trueInt || trueInt < 0;
    }


    /**
     * @param checkString the string to be checked
     * @param trueStrings an ArrayList of strings than may contain checkString
     * @return true if trueStrings contains checkString or if trueString is empty otherwise return false
     */
    private static Boolean containString(String checkString, ArrayList<String> trueStrings) {
        return trueStrings.contains(checkString) || trueStrings.size() == 0;
    }


    /**
     * @param checkString the string to be checked
     * @param trueString  the string that CheckString may be equal to
     * @return true if trueString iss equal to checkString or if trueString is empty otherwise return false
     */
    private static Boolean stringMatch(String checkString, String trueString) {
        return checkString.toLowerCase().equals(trueString.toLowerCase()) || trueString.equals("");
    }


    /**
     * @param checkString the string to be checked
     * @param trueString  the string that CheckString may contain
     * @return true if checkString contains trueString or trueString is empty otherwise returns false
     */
    private static Boolean subMatch(String checkString, String trueString) {
        return checkString.toLowerCase().contains(trueString.toLowerCase()) || trueString.equals("");
    }


    /**
     * checks that a date is between two date
     *
     * @param checkDate the date to be checked
     * @param dateTo    the DateTime value that the checkDate has to be <= than
     * @param dateFrom  the DateTime value that the checkDate has to be >= than
     * @return true if checkDate is between dateTo and dateFrom otherwise return false
     */
    private static Boolean checkDate(DateTime checkDate, DateTime dateTo, DateTime dateFrom) {
        return (dateTo == null || checkDate.getMillis() <= dateTo.getMillis()) &&
                (dateFrom == null || checkDate.getMillis() >= dateFrom.getMillis());
    }


    /**
     * Determines if a CrimeRecord should be filtered or not
     * if domestic boolean is the same or null, arrest boolean is the same or null,
     * crimeTypes ArrayList contains the primary description or is empty,
     * location contains location description or is empty,
     * the beat is equal or negative, ward is equal or is negative,
     * block is equal to the block string or is empty,
     * primary is equal primary description or is empty,
     * secondary contains the secondary description or is empty,
     * IUCR match the iucr or is empty, FBICD match or is empty,
     * to DateTime is >= or null and from DateTime is <= or null
     *
     * @param cR         CrimeRecord that is being compared with
     * @param domestic   Domestic flag
     * @param arrest     Arrest flag
     * @param crimeTypes an ArrayList of accepted primary descriptions
     * @param location   accepted location description
     * @param beat       beat location number
     * @param ward       Ward location number
     * @param block      block address
     * @param primary    the primary description
     * @param secondary  the secondary description
     * @param IUCR       IUCR code
     * @param FBICD      FBICD code
     * @param to         the DateTime value that the cR has to be <= than
     * @param from       the DateTime value that the cR has to be >= than
     * @return true if cR meets all of the requirement given by the other inputs
     */
    private static Boolean isFilter(CrimeRecord cR, Boolean domestic, Boolean arrest, ArrayList<String> crimeTypes,
                                   String location, int beat, int ward, String block, String primary,
                                   String secondary, String IUCR, String FBICD, DateTime to, DateTime from) {
        return boolMatch(cR.getDomestic(), domestic) && boolMatch(cR.getArrest(), arrest) &&
                containString(cR.getCrimeType().getPrimaryDescription(), crimeTypes) && subMatch(cR
                .getCrimeLocation().getLocationStr(), location) && intMatch(cR.getCrimeLocation().getBeat(), beat) &&
                intMatch(cR.getCrimeLocation().getWard(), ward) && subMatch(cR.getCrimeLocation().getBlock(), block) &&
                stringMatch(cR.getCrimeType().getPrimaryDescription(), primary) &&
                subMatch(cR.getCrimeType().getSecondaryDescription(), secondary) &&
                stringMatch(cR.getCrimeType().getIucr(), IUCR) &&
                stringMatch(cR.getFbiCD(), FBICD) &&
                checkDate(cR.getDate(), to, from);
    }
}