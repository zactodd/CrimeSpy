package crimeSpy.crimeData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * A collection of CrimeRecords. The class can store Crime records.
 * It is intended that this class be used to display to store a set
 * or a filtered/searched set of CrimeRecord objects. Each crime collection is
 * intended to be managed by the CrimeCollectionManager.
 */
public class CrimeCollection {


    private ArrayList<CrimeRecord> crimeList = new ArrayList<CrimeRecord>();
    private HashSet<String> allCaseIDs = new HashSet<String>();
    private String name = "";
    private Integer id = null;
    private String directory = "";


    /**
     * Constructor creates an <i>empty</i> CrimeCollection object
     */
    public CrimeCollection() {
        this.crimeList = new ArrayList<CrimeRecord>();
        this.allCaseIDs = new HashSet();
    }

    /**
     * Constructor creates an <i>populated</i> CrimeCollection object with the name, id and directory
     */
    public CrimeCollection(String name, Integer id, String directory) {
        this.name = name;
        this.id = id;
        this.directory = directory;
        this.crimeList = new ArrayList<CrimeRecord>();
        this.allCaseIDs = new HashSet();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void setId(Integer id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDirectory(String directory) { this.directory = directory; }

    public String getName() { return name; }

    public Integer getId() { return id; }

    public String getDirectory() { return directory; }


    /**
     * Finds a crime record object given a crime id
     * @param CrimeID the Unique ID of the crime record
     * @return the crime record object if found - else null
     */
    public CrimeRecord getCrimeRecord(String CrimeID) {
        for (CrimeRecord cr : crimeList) {
            if (cr.getCaseID().equals(CrimeID)) {
                return cr;
            }
        }
        return null;
    }


    /**
     * Return an ArrayList of all CrimeRecords in the collection
     * @return Arraylist of all crimes in the collection
     */
    public ArrayList<CrimeRecord> getCrimes() { return crimeList; }


    /**
     * Returns the CrimeCollection's observableList for the TableView
     * @return An ObservableArrayList
     */
    public ObservableList getObservableCrList() { return FXCollections.observableList(this.crimeList); }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Utility Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * This method adds a crime record to the CrimeCollection object iff
     * the CrimeRecord is not already in the collection.
     * Then saves that crime record to the database
     * @param cr a CrimeRecord to add to the CrimeCollection
     * @return 0 if success, 1 if fail unique case ID, 2 if sql exception on writing database,
     * 3 if other exception on writing database
     */
    public int addCrimeRecordtoDB(CrimeRecord cr) {
        // verify that the caseId of the crime we are trying to add to the collection is unique
        String caseId = cr.getCaseID();
        if (!uniqueCaseIdCheck(caseId)) {
            return 1;
        }
        // Crime is not in the collection so add and update record
        try {
            SQLiteDBHandler.writeCrimeRecord(cr);
        } catch (SQLException e) {
            return 2;
        } catch (Exception e) {
            return 3;
        }
        crimeList.add(cr);
        allCaseIDs.add(caseId);
        return 0;
    }


    /**
     * This method adds a crime record to the CrimeCollection object iff
     * the CrimeRecord is not already in the collection.
     * @param cr a CrimeRecord to add to the CrimeCollection
     * @return 0 if success, 1 if fail unique case ID
     */
    public int addCrimeRecord(CrimeRecord cr) {
        // verify that the caseId of the crime we are trying to add to the collection is unique
        String caseId = cr.getCaseID();
        if (!uniqueCaseIdCheck(caseId)) {
            return 1;
        }
        // Crime is not in the collection so add and update record
        crimeList.add(cr);
        allCaseIDs.add(caseId);
        return 0;
    }


    /**
     * This method updates a current crime record in the database
     * @param cr a CrimeRecord to update in the CrimeCollection
     * @return 0 if success, 1 if sql exception on writing database, 3 if other exception on writing to database
     */
    public int updateCrimeRecord(CrimeRecord cr) {
        // verify that the caseId of the crime we are trying to add to the collection is unique
        try {
            SQLiteDBHandler.editCrimeRecord(cr);
        } catch (SQLException e) {
            return 1;
        } catch (Exception e) {
            return 2;
        }
        return 0;

    }


    /**
     * This method removes a crime record from the CrimeCollection object  and database
     * @param cR The caseID number of the crime which to remove from the CrimeCollection [String]
     * @return 0 if success, 1 if sql exception on deleting from database, 3 if other exception on deleting from database
     */
    public int removeCrimeRecord(CrimeRecord cR) {
        try {
            SQLiteDBHandler.deleteCrimeRecord(cR.getCaseID());
        } catch (SQLException e) {
            return 1;
        } catch (Exception e) {
            return 2;
        }
        crimeList.remove(cR);
        allCaseIDs.remove(cR.getCaseID());
        return 0;
    }


    /**
     * Checks if the caseId is already in the collection.
     * @param caseId the case identification number of a CrimeRecord
     * @return true if unique
     */
    private boolean uniqueCaseIdCheck(String caseId) {
        return !allCaseIDs.contains(caseId);
    }


    /**
     * Given an arraylist of CrimeRecords, this function adds them all to the Crime Collection if they have unique ID
     * Then sorts them by time
     * @param newCrimes An arraylist of crimerecords to add to the crime collection
     */
    public void populateCrimeRecords(ArrayList<CrimeRecord> newCrimes) {
        for (CrimeRecord crime : newCrimes) {
            if (uniqueCaseIdCheck(crime.getCaseID())) {
                this.crimeList.add(crime);
                this.allCaseIDs.add(crime.getCaseID());
            }
        }
        sortCrimeRecordsByTime();
    }


    /**
     * Custom comparator to compare the CrimeRecord objects sent to it by sortCrimeRecordsByTime()
     * Compares the getDate methods
     */
    private class TimeComparator implements Comparator<CrimeRecord> {
        @Override
        public int compare(CrimeRecord o1, CrimeRecord o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }


    /**
     * Sorts crimeList by time using the custom comapator above
     * Then sets the nextCrimeID and prevCrimeID of each crime record
     * @return  0 if everything is all good, 1 if the crimelist has a zero size.
     */
    public int sortCrimeRecordsByTime(){
        if(crimeList.size() == 0) {
            return 1;
        }
        Collections.sort(crimeList, new TimeComparator());
        int numCrimes = this.getCrimes().size();
        CrimeRecord prevCrime = crimeList.get(0);
        for (int i = 1; i < numCrimes; i++) {
            prevCrime.setNextCrimeID(crimeList.get(i).getCaseID());
            crimeList.get(i).setPrevCrimeID(prevCrime.getCaseID());
            prevCrime = crimeList.get(i);
        }
        return 0;
    }


}
