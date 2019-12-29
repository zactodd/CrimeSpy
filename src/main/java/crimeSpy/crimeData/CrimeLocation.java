package crimeSpy.crimeData;

/**
 * The CrimeLocation class is intended to be used for location data storage and
 * methods for the CrimeRecord.
 * <u>Notes:</u>
 * <ul>
 *     <li>Primitive doubles are used for coordinates</li>
 * </ul>
 */
public class CrimeLocation {



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Looking for convertLatitudeToXCoordinate?
    // conversion between coordinate systems is a very complex topic and is unfortunately outside th
    // scope of this project.
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * The minimum number of digits that a ward can be
     */
    public static final Integer MIN_WARD_LENGTH = 1;


    /**
     * The maximum number of digits that a ward can be
     */
    public static final Integer MAX_WARD_LENGTH = 3;


    /**
     * The minimum number of digits that a beat can be
     */
    public static final Integer MIN_BEAT_LENGTH = 3;


    /**
     * The maximum number of digits that a beat can be
     */
    public static final Integer MAX_BEAT_LENGTH = 5;


    /**
     * The minimum latitude. Latitude measures how far north or south of the equator a place is located.
     * The equator is situated at 0 degrees, the north pole is situated at 90 degrees which is our maximum
     * allowable latitude.
     */
    public static final Double MAX_LATITUDE = 90.0;


    /**
     * The minimum latitude. Latitude measures how far north or south of the equator a place is located.
     * The equator is situated at 0 degrees, the south pole is situated at -90 degrees
     * which is our minimum allowable latitude
     */
    public static final Double MIN_LATITUDE = -90.0;


    /**
     * The minimum longitude. Longitude measures how far east or west of the prime meridian a
     * location is. The minimum allowable longitude is -180 degrees

     */
    public static final Double MIN_LONGITUDE = -180.0;


    /**
     * The maximum longitude. Longitude measures how far east or west of the prime meridian a
     * location is. The maximum allowable longitude is 180 degrees
     */
    public static final Double MAX_LONGITUDE = 180.0;


    //Attributes of CrimeLocation abstracted from CrimeRecord
    private String block;
    private Integer beat;
    private Integer ward;
    private double xCoordinate;
    private double yCoordinate;
    private double longitude;
    private double latitude;
    private String locationStr;
    private double[] latLong;


    /**
     * Constructor: Creates a blank location object
     */
    public CrimeLocation(){
    }


    /**
     * Create a CrimeLocation object for a CrimeRecord.
     * @param block Address of crime at city block level (zip code and street; last two digits of zip code are anonymised)
     * @param beat Police district (geographic area of the city broken down for patrol and stats reasons)
     * @param ward Election precincts
     *             (see also <a href="http://www.cityofchicago.org/city/en/about/wards.html">here</a>)
     * @param xCoordinate Map coordinate as given by Chicago Police Department's CLEAR system
     * @param yCoordinate Map coordinate as given by Chicago Police Department's CLEAR system
     * @param latitude Latitude of location of crime
     * @param longitude longitude of location of crime
     * @param location a textual description of the location of the crime
     */
    public CrimeLocation(String block, Integer beat, Integer ward,
                         double xCoordinate, double yCoordinate,
                         double latitude, double longitude, String location) {
        // Now setters in the case of non primitive data types: for additional null checks
        this.setBlock(block);
        this.setBeat(beat);
        this.setWard(ward);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.setLocationStr(location);
        this.setLatLong(latitude, longitude);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS / SETTERS
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Get the address of crime at city block level (zip code and street; last two digits of zip code are anonymised)
     * @return block of crime location [String]
     */
    public String getBlock() {
        return block;
    }


    /**
     * Set the address of crime at city block level (zip code and street; last two digits of zip code are anonymised)
     * Sets to empty string on null
     * @param block of crime location [String]
     */
    public void setBlock(String block) {
        if (block ==  null) {
            block = "";
        }
        this.block = block;
    }


    /**
     * Get the police district of crime location (geographic area of the city broken down for patrol and stats reasons)
     * @return beat of crime location [String]
     */
    public Integer getBeat() {
        return beat;
    }


    /**
     * Set the police district of crime location (geographic area of the city broken down for patrol and stats reasons)
     * Sets to 0 on null.
     * @param beat of crime location [String]
     */
    public void setBeat(Integer beat) {
        if (beat ==  null) {
            beat = 0;
        }
        this.beat = beat;
    }


    /**
     * Get the election precincts location of crime
     * @return ward of crime location [String]
     */
    public Integer getWard() {
        return ward;
    }


    /**
     * Set the election precincts location of crime for a CrimeRecord
     * Sets to 0 on null.
     * @param ward of crime location [String]
     */
    public void setWard(Integer ward) {
        if (ward ==  null) {
            ward = 0;
        }
        this.ward = ward;
    }


    /**
     * Get the Map X coordinate as given by Chicago Police Department's CLEAR system
     * @return the xCoordinate of crime location [double]
     */
    public double getxCoordinate() {
        return xCoordinate;
    }


    /**
     * Set the Map X coordinate as given by Chicago Police Department's CLEAR system
     * @param xCoordinate of crime location [double]
     */
    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }


    /**
     * Get the Map Y coordinate as given by Chicago Police Department's CLEAR system
     * @return the yCoordinate of crime location [double]
     */
    public double getyCoordinate() {
        return yCoordinate;
    }


    /**
     * Set the Map Y coordinate as given by Chicago Police Department's CLEAR system
     * @param yCoordinate of crime location [double]
     */
    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }


    /**
     * Get the longitude of location of crime
     * @return the longitude of location of crime [double]
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * Set the longitude of location of crime
     * @param longitude of location of crime [double]
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.setLatLong(this.getLatitude(), longitude);
    }


    /**
     * Get the latitude of location of crime
     * @return latitude of location of crime [double]
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Set the latitude of location of crime and updates the latitude component of
     * the double tuple latLong[]
     * @param latitude of location of crime [double]
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.setLatLong(latitude, this.getLongitude());
    }


    /**
     * Get description of crime location. eg: "MALL"
     * @return location description of crime [String]
     */
    public String getLocationStr() {
        return locationStr;
    }


    /**
     * Set description of crime location. eg: "MALL"
     * If locationStr is null then set it to an empty string.
     * @param locationStr description of crime [String]
     */
    public void setLocationStr(String locationStr) {
        if (locationStr ==  null) {
            locationStr = "";
        }
        this.locationStr = locationStr;
    }


    /**
     * Set latLong: a tuple that describes the {latitude, longitude} of the crime<br>
     * This method should be called by the constructor only. All adjustments to
     * latitude or longitude individually will also update this array.
     * @param latitude latitude of location of crime [double]
     * @param longitude longitude of location of crime [double]
     */
    public void setLatLong(double latitude, double longitude) {
        this.latLong = new double[] {latitude, longitude};
    }


    /**
     * Get latLong: a tuple that describes the {latitude, longitude} of the crime
     * @return {latitude, longitude} of location of crime {[double], [double]}
     */
    public double[] getLatLong() {
        return this.latLong;
    }
}
