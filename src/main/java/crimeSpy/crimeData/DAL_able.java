package crimeSpy.crimeData;

/**
 * DATA ACCESS LAYER Interface
 * <p>This interface is intended to be used by any persistent storage functions.</p>
 * Any interaction (reading/writing/editing) with persistent storage (DB, file, XML...)
 * <b>should</b> be done with the function naming defined within this interface. <br>
 * <u>Notes</u>
 * <ul>
 *     <li>The creation of the persistent storage location/container is left to the
 *     instantiating object (eg: FileManager will create a .csv, DBManager
 *     will create a DB).</li>
 *     <li>The conversion of the persistent storage media data to object data and
 *     vice versa is also not covered by this interface.</li>
 * </ul>
 */
public interface DAL_able {



    /**
     * Write a single block of crime record data to persistent storage.
     * This method expects a CrimeRecord object.
     * @param crime is a CrimeCollection object
     */
    void writeCrimeRecord(CrimeRecord crime);



    /**
     * Write a collection of crime data to persistent storage.
     * This method expects a CrimeCollection (collection object of CrimeRecords)
     * @param crimes is a CrimeCollection object
     */
    void writeCrimeRecords(CrimeCollection crimes);



    /**
     * Read a single crime record from persistent storage and return an
     * equivalent CrimeRecord object
     * @param crimeID A CrimeRecord ID
     * @return a CrimeRecord object
     */
    CrimeRecord readCrimeRecord(String crimeID);



    /**
     * Read a collection of crime records from persistent storage and return an
     * equivalent CrimeCollection object consisting of CrimeRecord objects
     * @return a CrimeRecord object
     */
    CrimeCollection readCrimeRecords();



    /**
     * Read a single crime record from persistent storage and return an
     * equivalent CrimeRecord object
     */
    void editCrimeRecord();



    /**
     * Read a collection of crime records from persistent storage and return an
     * equivalent CrimeCollection object consisting of CrimeRecord objects
     */
    void editCrimeRecords();


}
