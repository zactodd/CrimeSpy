package crimeSpy.crimeData;

/**
 * CrimeType object abstracts information about a crime (it's type)
 * <p>Validation is performed at the getter setter level. All constructors should implement
 * a getter or setter to ensure consistent validation. (<b>don't</b> use this.value = value;)</p>
 */
public class CrimeType {


    /**
     * Describes the minimum acceptable IUCR code length
     */
    public final static Integer MIN_IUCR_LENGTH = 2;

    /**
     * Describes the acceptable IUCR code format
     */
    private final static String LEGAL_IUCR_PATTERN = "^[a-zA-Z0-9]*$";

    /**
    * Attributes of the CrimeType abstracted from CrimeRecord
    */
    private String iucr;
    private String primaryDescription;
    private String secondaryDescription;


    /**
     * Constructor for CrimeType object takes the code descriptions of a crime
     * CrimeType crimeTYP = new CrimeType(460);<br />
     * CrimeType crimeTYP = new CrimeType(430);</p>
     *
     * @param iucr Illinois Uniform Crime Reporting code (Alphanumeric and min of 2 chars)
     */
    public CrimeType(String iucr) throws IllegalArgumentException {
        setIucr(iucr);
        setPrimaryDescription(SQLiteDBHandler.getCrimeTypeData(iucr).get(0));
        setSecondaryDescription(SQLiteDBHandler.getCrimeTypeData(iucr).get(1));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Validation
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Check the validity of the iucr code for the crime type. This is for initialisation purposes.
     * <p>The IUCR code is used in database storage as a primary key. This field can therefore not be null and
     * in fact must be unique</p>
     * <p>An iucr code should be alphanumeric, not null and not empty.
     * Additionally this code assumes that the code should be at least 3 characters in length.</p>
     * <p>See also <a href="https://data.cityofchicago.org/Public-Safety/Chicago-Police-Department-Illinois-Uniform-Crime-R/c7ck-438e">
     * here</a></p>
     *
     * @param iucrStr intended iucr code
     * @return true iff the string matches the pattern of a valid iucr code. false otherwise.
     * @throws IllegalArgumentException
     */
    public Boolean validateIucr(String iucrStr) throws IllegalArgumentException {
        //assume false
        Boolean isValid = false;
        if (iucrStr == null) {
            throw new NullPointerException("The IUCR is not initialised");
        }
        if (iucrStr.length() < MIN_IUCR_LENGTH) {
            String e = "The IUCR code " + iucrStr + " is too short (min " + MIN_IUCR_LENGTH + ")";
            throw new IllegalArgumentException(e);
        }
        if (!iucrStr.matches(LEGAL_IUCR_PATTERN)) {
            String e = "The IUCR code " + iucrStr + " is not valid (pattern: " + LEGAL_IUCR_PATTERN + ")";
            throw new IllegalArgumentException(e);
        } else {
            isValid = true;
        }
        return isValid;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILITY
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * <p>Change any occurrences of lower case letters to upper case for the IUCR code</p>
     * <p>See also <a href="https://data.cityofchicago.org/Public-Safety/Chicago-Police-Department-Illinois-Uniform-Crime-R/c7ck-438e">
     * here</a></p>
     */
    public void capitaliseIucr() {
        String originalIucr = this.getIucr();
        StringBuffer capitalisedIucr = new StringBuffer();
        Character c;
        for (Integer i = 0; i < originalIucr.length(); i++) {
            c = originalIucr.charAt(i);
            if (Character.isLowerCase(c)) {
                capitalisedIucr.append(Character.toUpperCase(c));
            } else {
                capitalisedIucr.append(c);
            }
        }
        this.setIucr(capitalisedIucr.toString());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * This function can be used instead of the individual setters.
     * @param iucr Illinois Uniform Crime Reporting code
     *             (see also <a href="https://www.isp.state.il.us/docs/6-260.pdf">here</a>)
     */
    public void setCrimeType(String iucr) throws IllegalArgumentException {
        setIucr(iucr);
        setPrimaryDescription(SQLiteDBHandler.getCrimeTypeData(iucr).get(0));
        setSecondaryDescription(SQLiteDBHandler.getCrimeTypeData(iucr).get(1));
    }


    /**
     * Gets case iucr: Illinois Uniform Crime Reporting code
     * @return A String of the case identifier (should be alphanumeric only)
     */
    public String getIucr() {
        return iucr;
    }


    /**
     * Set case iucr: Illinois Uniform Crime Reporting code
     * Validation checks are performed (length and format)
     * @param iucr typically (though not always) 4 chars and is alphanumeric
     */
    public void setIucr(String iucr) throws IllegalArgumentException {
        validateIucr(iucr);
        iucr = iucr.replaceFirst("^0+(?!$)", "");
        this.iucr = iucr;
    }


    /**
     * Gets textual description of crime (crime category based on the IUCR)
     * @return A String that describes the crime. eg: "THEFT"
     */
    public String getPrimaryDescription() {
        return primaryDescription;
    }


    /**
     * Sets textual description of crime (crime category based on the IUCR)
     * @param primaryDescription A String that describes the crime. eg: "THEFT"
     */
    public void setPrimaryDescription(String primaryDescription) {
        this.primaryDescription = primaryDescription;
    }


    /**
     * Gets textual description of crime (crime subcategory based on the IUCR)
     * @return A String that describes the crime. eg: "PETTY THEFT"
     */
    public String getSecondaryDescription() {
        return secondaryDescription;
    }


    /**
     * Sets textual description of crime (crime subcategory based on the IUCR)
     * @param secondaryDescription A String that describes the crime. eg: "PETTY THEFT"
     */
    public void setSecondaryDescription(String secondaryDescription) {
        this.secondaryDescription = secondaryDescription;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Deprecated
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * <p>Change any occurrences of lower case letters to upper case for the IUCR code</p>
     * <p>See also <a href="https://data.cityofchicago.org/Public-Safety/Chicago-Police-Department-Illinois-Uniform-Crime-R/c7ck-438e">
     * here</a></p>
     * @param iucrStr iucr code
     * @deprecated There should be no need to pass in the IUCR code as it can be
     * retrieved based on the CrimeType.iucr -->Use capitaliseIucr() instead.
     */
    @Deprecated
    public void capitaliseIucr(String iucrStr) {
        StringBuffer capitalisedIucr = new StringBuffer();
        Character c;
        for (Integer i = 0; i < iucrStr.length(); i++) {
            c = iucrStr.charAt(i);
            if (Character.isLowerCase(c)) {
                capitalisedIucr.append(Character.toUpperCase(c));
            } else {
                capitalisedIucr.append(c);
            }
        }
        this.setIucr(capitalisedIucr.toString());
    }
}

