package crimeSpy.crimeData;

import javafx.beans.property.*;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * Define the crime record object. (this currently describes a crime incident
 * as recorded for general public consumption by CLEAR<br>
 * <u>Dependant on:</u>
 * <ul>
 *     <li><b>CrimeLocation</b> - object describes the location of the crime with various properties</li>
 *     <li><b>CrimeType</b> - object describes the type of the crime with various properties</li>
 * </ul>
 * <br>
 * <u>Updates:</u>
 * <ul>
 *     <li><b>fbiCD</b> - This is now a property of this object where it
 *     previously belonged to CrimeType</li>
 * </ul>
 */
public class CrimeRecord {


    /**
     * Describes the minimum FBI CD number length.
     */
    public final static int MIN_FBICD_LENGTH = 1;

    private final static String LEGAL_FBICD_PATTERN = "^[a-zA-Z0-9]*$";

    // Note Attributes can be observed at http://learn.canterbury.ac.nz/mod/resource/view.php?id=328504
    private String caseID;
    private DateTime date;
    private Boolean arrest;
    private Boolean domestic;
    public CrimeType crimeType;
    public CrimeLocation crimeLocation;
    private String prevCrimeID;
    private String nextCrimeID;
    private String fbiCD;


    /**
     * Constructor for the copy of a crime Record
     */
    public CrimeRecord(CrimeRecord cr) {
        this(cr.getCaseID(), cr.getDate(), cr.getArrest(), cr.getDomestic(),
                cr.getCrimeType().getIucr(), cr.getFbiCD(),
                cr.getCrimeLocation().getBlock(), cr.getCrimeLocation().getBeat(), cr.getCrimeLocation().getWard(),
                cr.getCrimeLocation().getxCoordinate(), cr.getCrimeLocation().getyCoordinate(),
                cr.getCrimeLocation().getLatitude(), cr.getCrimeLocation().getLongitude(),
                cr.getCrimeLocation().getLocationStr());
    }


    /**
     * Constructor for full CrimeRecord
     * <p>
     * <u>Notes</u>
     * <ul>
     *     <li><b>location</b> the location string description is separate and distinct to the
     *     location tuple consisting of {latitude, longitude}. It is instead purely a textual
     *     description of the location of the crime. <b>eg:</b> "DEPARTMENT STORE"</li>
     *     <li>Location data is stored in a CrimeLocation object and
     *     includes (block, beat, ward, x/y co-ord, lat, long, locStr)</li>
     *     <li>Crime type data is stored in a CrimeType object and includes
     *     (iucr, fbiCD, pri/sec descrip.)</li>
     * </ul>
     * </p>
     * @param caseID the unique caseID for the crime. (Alphanumeric)
     * @param date The date the crime was committed. (This is currently stored as JodaTime)
     * @param arrest True/False - was there an arrest?
     * @param domestic True/False - was this a domestic crime
     * @param iucr Illinois Uniform Crime Reporting code
     *             (see also <a href="https://www.isp.state.il.us/docs/6-260.pdf">here</a>)
     * @param fbiCD FBI crime code. (Numerical/Alphanumerical)
     * @param block Address of crime at city block level (zip code and street; last two digits of zip code are anonymised)
     * @param beat Police district (geographic area of the city broken down for patrol and stats reasons)
     * @param ward Election precincts
     *             (see also <a href="http://www.cityofchicago.org/city/en/about/wards.html">here</a>)
     * @param xCoord map coordinate
     * @param yCoord map coordinate
     * @param latitude Latitude of location of crime
     * @param longitude Longitude of location of crime
     * @param location string location description
     */
    public CrimeRecord(String caseID, DateTime date, Boolean arrest, Boolean domestic,
                       String iucr, String fbiCD,
                       String block, Integer beat, Integer ward,
                       double xCoord, double yCoord, double latitude, double longitude,  String location) {

            this.caseID = caseID;
            this.date = date;
            this.arrest = arrest;
            this.domestic = domestic;
            this.crimeType = new CrimeType(iucr);
            this.crimeLocation = new CrimeLocation(block, beat, ward, xCoord, yCoord, latitude, longitude, location);
            this.prevCrimeID = null;
            this.nextCrimeID = null;
            this.fbiCD = fbiCD;
        }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILITY
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Public access to the obfuscate method<br>
     * Calls obfuscateRecord()
     */
    public void obfuscate() {
        this.obfuscateRecord();
    }


    /**
     * Obfuscate the pertinent details of a new crime (or loaded crime that needs it).
     * This is intended to prevent identifying or personal information being persistently
     * stored by the system.<br>
     * Currently this method will take the <b>BLOCK</b> information and split off the
     * first space separated number (if it exists) in the string.<br>
     * it will convert this number DDD..DD -> DDDD..XX where D is any digit.<br>
     * In the event that the last, second to last or both last digits are already
     * abfuscated the function will only convert one or none of the last two digits to X.<br>
     * The function does not return a value, rather modifies the <b>BLOCK</b> information in place.<br>
     * <u>Example:</u><br>
     * "00023 LIVELY LANE" --> "000XX LIVELY LANE"<br>
     * "000X3 LIVELY LANE" --> "000XX LIVELY LANE"<br>
     * "000XX LIVELY LANE" --> "000XX LIVELY LANE"<br>
     * "LIVELY LANE"       --> "LIVELY LANE"<br>
     */
    private void obfuscateRecord() {
        boolean isChanged = false;
        String specificBlock = this.getCrimeLocation().getBlock();
        String obfuscatedBlock = "";
        if (specificBlock != null) {

            // get the first word which should be the address information to obfuscate
            int index = specificBlock.indexOf(' ');
            String addressStart;
            String addressEnd;

            // Index of returns -1 if the character was not found. Assign appropriately
            if (index > 0) {
                addressStart = specificBlock.substring(0, index);
                addressEnd = specificBlock.substring(index);
            } else {
                addressStart = specificBlock;
                addressEnd = "";
            }

            // Check the last two characters of address to see if they need obfuscation
            int lastIndex = addressStart.length() - 1;
            int secondLastIndex = lastIndex - 1;
            obfuscatedBlock = addressStart;

            // ...Last character - only modify if it is a digit otherwise assume incorrect format and ignore
            if ( lastIndex >= 0 && Character.isDigit(addressStart.charAt(lastIndex)) ) {
                isChanged = true;
                obfuscatedBlock = obfuscatedBlock.substring(0, lastIndex) + "X";
            }

            // ...Second to last character - only modify if it is a digit otherwise assume incorrect format and ignore
            if ( secondLastIndex >= 0 && Character.isDigit(addressStart.charAt(secondLastIndex)) ) {
                isChanged = true;
                obfuscatedBlock = obfuscatedBlock.substring(0, secondLastIndex) + "X";
                obfuscatedBlock += obfuscatedBlock.substring(secondLastIndex);
            }

            // Update IFF changes have been made
            if (isChanged) {
                obfuscatedBlock += addressEnd;
                this.getCrimeLocation().setBlock(obfuscatedBlock);
            }
        }
    }


    /**
     * Get a string representation of the crime - intended for general debug not user interface.
     * @return string representation of a CrimeRecord object: "CrimeRecord(.....)" where ellipsis indicates attributes
     */
    @Override
    public String toString() {
        String cR = "";
        try {
            cR += "CrimeRecord [ID: " + this.caseID + "]\n";
            cR += "~~~~~~~~~~~~~~~~~~~~~\n";
            cR += "Date:\t"     + date     + "\n";
            cR += "Arrest:\t"   + arrest   + "\n";
            cR += "Domestic:\t" + domestic + "\n";
            cR += "CrimeType:\n";
            cR += "\tIUCR:\t"   + crimeType.getIucr() + "\n";
            cR += "\tFBI CD:\t" + getFbiCD() + "\n";
            cR += "\tPri:\t"    + crimeType.getPrimaryDescription() + "\n";
            cR += "\tSec:\t"    + crimeType.getSecondaryDescription() + "\n";
            cR += "Location:\n";
            cR += "\tBlock:\t"  + crimeLocation.getBlock() + "\n";
            cR += "\tBeat:\t"   + crimeLocation.getBeat() + "\n";
            cR += "\tWard:\t"   + crimeLocation.getWard() + "\n";
            cR += "\txCoord:\t" + crimeLocation.getxCoordinate() + "\n";
            cR += "\tyCoord:\t" + crimeLocation.getyCoordinate() + "\n";
            cR += "\tLat:\t"    + crimeLocation.getLatitude() + "\n";
            cR += "\tLong:\t"   + crimeLocation.getLongitude() + "\n";
            cR += "\tLoc:\t"   + crimeLocation.getLocationStr() + "\n";
        } catch (NullPointerException e) {
            // one or more of the properties of this CrimeType object have not been initialised:
            cR = "Problem converting this CrimeType object to a string.\n";
            cR += "NullPointerFailure: one or more of the properties of this CrimeType object have not been initialised.\n";
            cR += "Did you use the empty constructor?";
        }
        return cR;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS / SETTERS
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets case ID.
     * @return A String of the case identifier (should be alphanum only)
     */
    public String getCaseID() { return this.caseID; }


    /**
     * Sets case ID.
     * @param caseID String representation of the case identifier (should be alphanum only)
     */
    public void setCaseID(String caseID) { this.caseID = caseID; }


    /**
     * Gets the date of the crime.
     * @return returns a Date object (using Joda as of 12 Aug)
     */
    public DateTime getDate() { return this.date; }


    /**
     * Set the date of the crime.
     * @param date date to set crime to (Joda as of 12 Aug)
     */
    public void setDate(DateTime date) { this.date = date; }


    /**
     * Gets the hour of the day of the crime
     * @return hour as an integer between 0 and 23
     */
    public Integer getHour() { return this.date.getHourOfDay(); }


    /**
     * Sets the hour value of the time of the crime
     * @param hour An integer between 0 and 23 for the crime time hour to be set to
     */
    public void setHour(Integer hour) { this.date = this.date.withHourOfDay(hour); }


    /**
     * Gets the minute of the hour of the crime
     * @return minute as an integer between 0 and 59
     */
    public Integer getMinute() { return this.date.getMinuteOfHour(); }


    /**
     * Sets the minute value of the hour of the crime
     * @param minute An integer between 0 and 59 for the crime time minute to be set to
     */
    public void setMinute(Integer minute) { this.date = this.date.withMinuteOfHour(minute); }


    /**
     * Note that .print() has documented behaviour that a parsed null will return the
     * Current time:<br>
     * <a href='http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormatter.html#print%28org.joda.time.ReadableInstant%29'>api</a><br>
     * This is not useful so a check has been added to return a string warning message.
     * @return String representation of DateTime for nice user readability
     */
    public String getDateTimeStr() {
        DateTime date = this.getDate();
        String dateStr;
        if (date != null) {
            org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern(("dd/MM/yyyy HH:mmaa")).withLocale(Locale.US);
            dateStr = fmt.print(date);
        } else {
            dateStr = "No date information for this crime";
        }
        return dateStr;
    }


    /**
     * Gets True/False if there was an arrest.
     * @return returns a bool.
     */
    public Boolean getArrest() {
        return this.arrest;
    }


    /**
     * Set the arrest to True or False.
     * @param arrest True/False value if there was/was not an arrest
     */
    public void setArrest(Boolean arrest) {
        this.arrest = arrest;
    }


    /**
     * Gets True/False if there was an domestic.
     * @return returns a bool.
     */
    public Boolean getDomestic() {
        return this.domestic;
    }


    /**
     * Set the domestic value to True or False.
     * @param domestic True/False value if there was/was not an arrest
     */
    public void setDomestic(Boolean domestic) {
        this.domestic = domestic;
    }


    /**
     * Gets FBI crime code
     * @return A String of the case identifier. Alphanumeric.
     */
    public String getFbiCD() {
        return fbiCD;
    }


    /**
     * Set FBI crime code
     * Validation checks are performed (length and format)
     * @param fbiCD typically (though not always) 3 chars or 2 digits. Alphanumeric.
     */
    public void setFbiCD(String fbiCD) throws IllegalArgumentException {
        validateFbiCd(fbiCD);
        this.fbiCD = fbiCD;
    }


    /**
     * Gets the crime type of the crime.
     * @return returns a CrimeType object.
     */
    public CrimeType getCrimeType() {
        return this.crimeType;
    }


    /**
     * Gets the crime type of the crime.
     * @return returns a CrimeType object.
     */
    public String getCrimeTypeID() {
        return this.crimeType.getIucr();
    }


    /**
     * Sets the crime type.
     * @param iucr Illinois Uniform Crime Reporting code
     * @param primaryDescription textual description of crime
     * @param secondaryDescription textual description of crime
     */
    public void setCrimeType(String iucr, String primaryDescription, String secondaryDescription) {
        this.crimeType.setCrimeType(iucr);
    }


    /**
     * @return the crimeLocation <i>object</i> that helps describe this CrimeRecord
     */
    public CrimeLocation getCrimeLocation() {
        return this.crimeLocation;
    }


    /**
     * Check the validity of the FBI CD code for the crime record.
     * <p>An FBI CD code should be alphanumeric, not null and not empty.
     * Additionally this code assumes that the code should be at least 3 characters in length.</p>
     * @param fbicdStr intended iucr code
     * @throws IllegalArgumentException If the FBI CD number is not valid
     * @return true iff the string matches the pattern of a valid iucr code. false otherwise.
     */
    public boolean validateFbiCd (String fbicdStr) throws IllegalArgumentException {
        //assume false
        boolean isValid = false;
        if (fbicdStr == null) {
            throw new NullPointerException("The FBI CD number is not initialised");
        }
        if (fbicdStr.length() < MIN_FBICD_LENGTH) {
            String e = "The FBI CD number code " + fbicdStr + " is too short (min " + MIN_FBICD_LENGTH + ")";
            throw new IllegalArgumentException(e);
        }
        if (!fbicdStr.matches(LEGAL_FBICD_PATTERN)) {
            String e = "The FBI CD number code " + fbicdStr + " is not valid (pattern: " + LEGAL_FBICD_PATTERN + ")";
            throw new IllegalArgumentException(e);
        }
        else {
            isValid = true;
        }
        return isValid;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PROPERTY GETTERS FOR JAVAFX
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Property getter for Case ID
     * @return SimpleStringProperty of the Case ID
     */
    public StringProperty caseIDProperty() {return new SimpleStringProperty(this.getCaseID());}


    /**
     * Property getter for the date. Returns a string representation of the Joda DateTime.
     * @return A SimpleStringProperty of the DateTime
     */
    public StringProperty dateProperty() {
        if (this.getDate() != null) {
            return new SimpleStringProperty(this.getDateTimeStr());
        } else {
            return new SimpleStringProperty(null);
        }
    }


    /**
     * Property getter for arrest. If the value is not initialised - false is default
     * @return SimpleBooleanProperty of this record's arrest property
     */
    public BooleanProperty arrestProperty() {
        if (this.getArrest() != null) {
            return new SimpleBooleanProperty(this.getArrest());
        } else {
            return new SimpleBooleanProperty(false);
        }
    }


    /**
     * Property getter for domestic. If the value is not initialised - false is default
     * @return SimpleBooleanProperty of this record's domestic property
     */
    public BooleanProperty domesticProperty() {
        if (this.getDomestic() != null) {
            return new SimpleBooleanProperty(this.getDomestic());
        } else {
            return new SimpleBooleanProperty(false);
        }
    }


    /**
     * Property getter for iucr
     * @return SimpleStringProperty of this record's iucr property
     */
    public StringProperty iucrProperty(){return new SimpleStringProperty(this.getCrimeType().getIucr());}


    /**
     * Property getter for FBI CD
     * @return SimpleStringProperty of this record's FBI CD property
     */
    public StringProperty fbiCDProperty() {return  new SimpleStringProperty(this.getFbiCD());}


    /**
     * Property getter for Primary Description
     * @return SimpleStringProperty of this record's Primary Description property
     */
    public StringProperty primaryDescriptionProperty() {
        return new SimpleStringProperty(this.getCrimeType().getPrimaryDescription());
    }


    /**
     * Property getter for Secondary Description
     * @return SimpleStringProperty of this record's Secondary Description property
     */
    public StringProperty secondaryDescriptionProperty() {
        return new SimpleStringProperty(this.getCrimeType().getSecondaryDescription());
    }


    /**
     * Property getter for block
     * @return SimpleStringProperty of this record's block property
     */
    public StringProperty blockProperty() {return new SimpleStringProperty(this.getCrimeLocation().getBlock());}


    /**
     * Property getter for beat
     * @return SimpleIntegerProperty of this record's beat property
     */
    public IntegerProperty beatProperty() {return new SimpleIntegerProperty(this.getCrimeLocation().getBeat());}


    /**
     * Property getter for ward
     * @return SimpleIntegerProperty of this record's ward property
     */
    public IntegerProperty wardProperty() {return new SimpleIntegerProperty(this.getCrimeLocation().getWard());}


    /**
     * Property getter for x coordinate
     * @return SimpleDoubleProperty of this record's x coordinate property
     */
    public DoubleProperty xCoordProperty() {return new SimpleDoubleProperty(this.getCrimeLocation().getxCoordinate());}


    /**
     * Property getter for y coordinate
     * @return SimpleDoubleProperty of this record's y coordinate property
     */
    public DoubleProperty yCoordProperty() {return new SimpleDoubleProperty(this.getCrimeLocation().getyCoordinate());}


    /**
     * Property getter for longitude
     * @return SimpleDoubleProperty of this record's longitude property
     */
    public DoubleProperty longitudeProperty() {return new SimpleDoubleProperty(this.getCrimeLocation().getLongitude());}


    /**
     * Property getter for latitude
     * @return SimpleDoubleProperty of this record's latitude property
     */
    public DoubleProperty latitudeProperty() {return new SimpleDoubleProperty(this.getCrimeLocation().getLatitude());}


    /**
     * Property getter for location
     * @return SimpleStringProperty of this record's location property
     */
    public StringProperty locStrProperty() {return new SimpleStringProperty(this.getCrimeLocation().getLocationStr());}


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Time and distance calculations
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets a time period between two CrimeRecords and returns it as a period of years, months, weeks, days, hours,
     * minutes, seconds and millis.
     * @param other the CrimeRecord being compared to the CrimeRecord the method is being called on
     * @return period of years, months, weeks, days, hours, minutes, seconds and millis representing the time between
     * the CrimeRecords being compared
     */
    public Period millisBetween(CrimeRecord other) {
        long thisMilli = this.getDate().getMillis();
        long otherMilli = other.getDate().getMillis();
        return new Period(Math.abs(thisMilli - otherMilli));
    }


    /**
     * Uses the <a href=https://en.wikipedia.org/wiki/Haversine_formula>haversine formula</a> to calculate the distance
     * between two CrimeRecords, taking into account the curvature of the earth (as sphere rather than an oblate
     * spheroid, because we're filthy math casuals.
     * @param other The CrimeRecord that the distance is measured to.
     * @return The distance in kilometers
     */
    public double distanceBetween(CrimeRecord other) {
        int earthRad = 6371;
        double long1 = this.getCrimeLocation().getLongitude();
        double long2 = other.getCrimeLocation().getLongitude();
        double lat1 = this.getCrimeLocation().getLatitude();
        double lat2 = other.getCrimeLocation().getLatitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRad * c;
    }


    /**
     * Get the previous caseId number from a given CrimeRecord in a Collection of a particular sorting
     * @return the Identification number (caseID) of the previous crime after current in a particular sorting
     */
    public String getPrevCrimeID() {
        return this.prevCrimeID;
    }


    /**
     * Set the previous caseId number from a given CrimeRecord in a Collection of a particular sorting
     * @param prevCrimeID Identification number (caseID) of the previous crime after current in a particular sorting [String]
     */
    public void setPrevCrimeID(String prevCrimeID) { this.prevCrimeID = prevCrimeID; }


    /**
     * Get the next caseId number from a given CrimeRecord in a Collection of a particular sorting
     * @return The identification number (caseID) of the next crime after current in a particular sorting [String]
     */
    public String getNextCrimeID() {
        return this.nextCrimeID;
    }


    /**
     * Set the next caseId number from a given CrimeRecord in a Collection of a particular sorting
     * @param nextCrimeID The identification number (caseID) of the next crime after current [String]
     */
    public void setNextCrimeID(String nextCrimeID) {
        this.nextCrimeID = nextCrimeID;
    }


    /**
     * Get the distance to the next CrimeRecord in a Collection of a particular sorting
     * @return The distance to the next crime after current [Double]
     */
    public Double getNextCrimeDistance() {
        return this.distanceBetween(CrimeCollectionManager.getFullWorkingCollection().getCrimeRecord(this.nextCrimeID));
    }


    /**
     * Get the time to the next CrimeRecord in a Collection of a particular sorting
     * @return The time to the next crime after current [String]
     */
    public Period getNextCrimeTimeSince() {
        return this.millisBetween(CrimeCollectionManager.getFullWorkingCollection().getCrimeRecord(this.nextCrimeID));
    }

    /**
     * Checks to see if a crime record object has the same caseID as this CrimeRecord object.
     * @param cr the crime record object to compare this crime record to
     * @return true iff tje cr CrimeRecord object has the same caseID as this CrimeRecord object
     */
    public boolean myEquals(Object cr) {
        return this.getCaseID().equals( ((CrimeRecord)cr).getCaseID());
    }
}