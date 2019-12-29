package crimeSpy.crimeData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class to handle searching of the crime collection
 */
public class SearchCrimeCollection {


    /**
     * LOGGER is the class wide instance of java.util.logging for CrimeCollectionManager
     * Logging to root directory: ./LOG_SearchCrimeCollection.xml
     */
    private static final Logger LOGGER = Logger.getLogger(SearchCrimeCollection.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_SearchCrimeCollection.xml";

    // initialise the logger for fileloc
    private static Handler fileHandler;
    static {
        try {
            fileHandler  = new FileHandler(LOG_FILE_STORE_LOC);
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.INFO);
            LOGGER.setUseParentHandlers(false);
            LOGGER.config("Configuration done.");
        } catch (IOException e) {
            LOGGER.warning("Failed to initiate file handler");
            LOGGER.setLevel(Level.SEVERE); // for case of console only
        }
    }


    /**
     * Search a CrimeCollection and return a new CrimeCollection that contains all matches of
     * the search criteria
     * @param text is the string input in the search box
     * @return a CrimeCollection of CrimeRecords that match the search criteria
     */
    public static CrimeCollection search(String text) {
        if (text.equals("Search...") || text.equals("")) {
            return CrimeCollectionManager.getCurrWorkingCollection();
        }

        CrimeCollection searchedCollection = new CrimeCollection();
        ArrayList<String> stringArray = separateString(text);

        for (String searchElement : stringArray) {
            searchUsingPhrase(searchElement, searchedCollection);
        }
        return searchedCollection;
    }


    /**
     * Separates string by commas
     * @param commasString comma separated string
     * @return stringArray a string array of all words in commasString split by comma
     */
    private static ArrayList<String> separateString(String commasString){
        ArrayList<String> stringArray = new ArrayList<String>();
        int index = 0;
        int lastComma = -1;
        int stringLength = commasString.length();
        while(index < stringLength){
            if (commasString.substring(index, index + 1).equals(",")){
                stringArray.add(commasString.substring(lastComma + 1, index).trim());
                lastComma = index;
            }
            index++;
        }
        stringArray.add(commasString.substring(lastComma + 1, index).trim());
        return stringArray;
    }


    /**
     * adds crimeRecords to the searchedCollection if they meet the requirements
     * @param phrase the string which to search by
     * @param searchCollection the collection to search
     */
    private static void searchUsingPhrase(String phrase, CrimeCollection searchCollection){

        ArrayList<CrimeRecord> crimeRecords = CrimeCollectionManager.getCurrWorkingCollection().getCrimes();


        for(CrimeRecord cR : crimeRecords){
            if(isSearch(cR, phrase)){
                searchCollection.addCrimeRecord(cR);

            }
        }
    }


    /**
     * @param cR the crime record
     * @param phrase the phrase to search by
     * @return True if phrase is equal to or is a substring the types
     */
    private static Boolean isSearch(CrimeRecord cR, String phrase){
        Boolean isFound = false;
        String[] searchItems = {cR.getCrimeType().getPrimaryDescription(),
                cR.getCrimeType().getSecondaryDescription(), cR.getCaseID(),
                cR.getCrimeLocation().getLocationStr(), cR.getCrimeLocation().getWard().toString(),
                cR.getCrimeLocation().getBeat().toString(), cR.getCrimeLocation().getBlock(),
                cR.getCrimeType().getIucr(), cR.getFbiCD()};
        for(String checkString : searchItems){
            if (checkString.toLowerCase().contains(phrase.toLowerCase())){
                isFound = true;
                break;
            }
        }
        return isFound;
    }
}
