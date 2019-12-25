/*
    crimeSpy is a FOSS crime analysis software.
    Copyright (C) 2015 SENG Team Supreme

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package seng202.crimeSpy.crimeData;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;


/**
 * Provides a 1:1 mapping of a CrimeRecord object into a structure that can be passes
 * into the DAL (data access layer) for easy writing and vice versa.
 * <p><u>Example Use:</u>
 *    OR_Map cr = new OR_Map(myCrimeRecord); <br>
 *    String sql = "INSERT INTO CRIME_RECORD (CRIME_RECORD_ID ... other column  headings"; <br>
 *    sql += "VALUES ('" <br>
 *    sql += cr.getCaseID() + "'"; <br>
 * </p>
 * <u>Current schema Maps Available</u>
 * <ul>
 *     <li>Chicago type CrimeRecord - OR_Map</li>
 * </ul>
 */
public class OR_Map {

    private String id;
    private DateTime date;
    private String arrest;
    private String domestic;
    private String iucr;
    private String fbiCD;
    private String priDesc;
    private String secDesc;
    private String block;
    private String beat;
    private String ward;
    private String xCoord;
    private String yCoord;
    private String longitude;
    private String latitude;
    private String locationStr;
    private String latLong;


    /**
     * For a <b>Chicago</b> type CrimeRecord. Object to relationObject map.<br />
     * Type conversion is performed by this class for SQLliteDB
     *
     * <p>
     * <u>Object <b>will</b> have:</u>
     * <ul>
     *     <li>ID</li>
     *     <li>Date</li>
     *     <li>Arrest</li>
     *     <li>Domestic</li>
     *     <li>Previous Crime ID</li>
     *     <li>Next Crime ID</li>
     *     <li>IUCR number</li>
     *     <li>FBI CD number</li>
     *     <li>Primary Description (type)</li>
     *     <li>Secondary Description (type)</li>
     *     <li>Block</li>
     *     <li>Beat</li>
     *     <li>Ward</li>
     *     <li>x Coordinate</li>
     *     <li>y Coordinate</li>
     *     <li>Latitude</li>
     *     <li>Longitude</li>
     *     <li>(Latitude, Longitude)</li>
     *     <li>Location Description</li>
     * </ul>
     * @param cr the crime record for which to create an OR_Map
     */
    public OR_Map(CrimeRecord cr) {
        this.id = cr.getCaseID();
        this.date = cr.getDate();
        this.iucr = cr.getCrimeType().getIucr();
        this.fbiCD = cr.getFbiCD();
        this.priDesc = cr.getCrimeType().getPrimaryDescription();
        this.secDesc = cr.getCrimeType().getSecondaryDescription();
        this.block = cr.getCrimeLocation().getBlock();
        this.beat = String.valueOf(cr.getCrimeLocation().getBeat());
        this.ward = String.valueOf(cr.getCrimeLocation().getWard());
        this.xCoord = String.valueOf(cr.getCrimeLocation().getxCoordinate());
        this.yCoord = String.valueOf(cr.getCrimeLocation().getyCoordinate());
        this.latitude = String.valueOf(cr.getCrimeLocation().getLatitude());
        this.longitude = String.valueOf(cr.getCrimeLocation().getLongitude());
        this.latLong = "(" + String.valueOf(cr.getCrimeLocation().getLatitude()) + ", " +
                String.valueOf(cr.getCrimeLocation().getLongitude()) + ")";
        this.locationStr = cr.getCrimeLocation().getLocationStr();

        if (cr.getArrest()) {
            this.arrest = "1";
        } else {
            this.arrest = "0";
        }

        if (cr.getDomestic()) {
            this.domestic = "1";
        } else {
            this.domestic = "0";
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS only are needed at this stage for this object
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Get the crimeID of this crime
     * @return the crimeID as a string
     */
    public String getId() {
        return id;
    }


    /**
     * Get the date of this crime
     * @return the date of the crime in joda DateTime
     */
    public DateTime getDate() {
        return date;
    }


    /**
     * Get arrest state of this crime
     * @return string representation of boolean value of whether an arrest has been made or not for this crime
     */
    public String getArrest() {
        return arrest;
    }


    /**
     * Get domestic state of this crime
     * @return string representation of boolean value of whether this crime was a domestic crime
     */
    public String getDomestic() {
        return domestic;
    }


    /**
     * get the IUCR code of this crime
     * @return the Illinois Uniform Crime Reporting code as a string
     */
    public String getIucr() {
        return iucr;
    }


    /**
     * Get the FBI CD number of this crime
     * @return the FBI CD number of this crime as a string
     */
    public String getFbiCD() {
        return fbiCD;
    }


    /**
     * Get the primary description of the crime.
     * @return primary description of the crime, eg: THEFT
     */
    public String getPriDesc() {
        return priDesc;
    }


    /**
     * Get the secondary (more detailed) description of this crime
     * @return secondary description of the crime
     */
    public String getSecDesc() {
        return secDesc;
    }


    /**
     * Get the block number of this crime
     * @return String representation of the block number where this crime was committed
     */
    public String getBlock() {
        return block;
    }


    /**
     * Get the beat number of this crime
     * @return String representation of the beat number where this crime was committed
     */
    public String getBeat() {
        return beat;
    }


    /**
     * Get the ward number of this crime
     * @return String representation of the ward number where this crime was committed
     */
    public String getWard() {
        return ward;
    }


    /**
     * Get the x coordinate of this crime
     * @return String representation of the x-coordinate where this crime was committed
     */
    public String getxCoord() {
        return xCoord;
    }


    /**
     *
     * @return String representation of the y-coordinate where this crime was committed
     */
    public String getyCoord() {
        return yCoord;
    }


    /**
     * Get the longitude of this crime
     * @return string representation of the longitude where this crime was committed
     */
    public String getLongitude() {
        return longitude;
    }


    /**
     * Get the latitude of this crime
     * @return string representation of the latitude where this crime was committed
     */
    public String getLatitude() {
        return latitude;
    }


    /**
     * Get the location description of this crime
     * @return string description of where the crime took place, eg: "SHOPPING MALL"
     */
    public String getLocationStr() {
        return locationStr;
    }


    /**
     * Get a lat long pair of this crime
     * @return string "latitude, longitude"
     */
    public String getLatLong() {
        return latLong;
    }


    /**
     * Creates a sqlite ready instruction to insert this crime record into
     * a pertinent crime database table
     * @return a script string, that is ready for parsing using sqlite syntax, for this crime
     */
    public String toDBCreateString() {
        String result;
        result = "'" + id + "', '" + date.toString() + "', '" + block + "', '" + locationStr +
            "', " + arrest + ", " + domestic + ", " + beat + ", " + ward + ", " + xCoord +
            ", " + yCoord + ", " + latitude + ", " + longitude + ", '" + locationStr +
            "', 0, 0, '" + iucr.replaceFirst("^0+(?!$)", "") + "', '" + fbiCD + "'"; //Remove leading zeros in IUCR code
        return result;
        /*
        FORMAT for sql:
        "INSERT INTO CRIME_RECORD " +
                "(CRIME_RECORD_ID, CRIME_RECORD_DATE, CRIME_RECORD_BLOCK, CRIME_RECORD_LOCATION_DESCRIPTION, CRIME_RECORD_ARREST, CRIME_RECORD_DOMESTIC, CRIME_RECORD_BEAT, CRIME_RECORD_WARD, CRIME_RECORD_XCOORDINATE, CRIME_RECORD_YCOORDINATE, CRIME_RECORD_LATITUDE, CRIME_RECORD_LONGITUDE, CRIME_RECORD_LOCATIONSTR, PREV_CRIME_RECORD, NEXT_CRIME_RECORD, CRIME_TYPE_ID) " +
                "VALUES ('HX321538', 'HiDate', '080XX S HALSTED ST', 'DEPARTMENT STORE', 0, 1, 621, 21, 0.0, 0.0, 0.0, 0.0, 'latlong', 0, 0, 'THEFT')";
        */
    }


    /**
     * Get a comma separated string of this crime object. This is intended for writing to csv.
     * @return a comma separated string description of this crime
     */
    public String toCSVString() {
        String csvArrest;
        String csvDomestic;

        // True false values for arrest and domestic are stored as Y N string values in csv for chicago crimes
        csvArrest = (this.arrest.equals("1") ? "Y" :"N");
        csvDomestic = (this.domestic.equals("1") ? "Y" : "N");

        String result = new String(id + "," + date.toString(DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss a"))
                + "," + block + "," + iucr + "," + priDesc + "," + secDesc + "," + locationStr + "," + csvArrest + ","
                + csvDomestic + "," + beat + "," + ward + "," + fbiCD + "," + xCoord + "," + yCoord + ","
                + latitude + "," + longitude + ", \"" + latLong + "\"\n");
        return result;
    }


    /**
     * Creates a string for updating a crime record to a SQLite database
     * @return String to update a current crime record in a SQLite database
     */
    public String toDBUpdateString() {
        String result;
        result = "CRIME_RECORD_ID = '" + id + "', CRIME_RECORD_DATE = '" + date.toString() + "', " +
                "CRIME_RECORD_BLOCK = '" + block + "', CRIME_RECORD_LOCATION_DESCRIPTION = '" + locationStr +
                "', CRIME_RECORD_ARREST = " + arrest + ", CRIME_RECORD_DOMESTIC = " + domestic +
                ", CRIME_RECORD_BEAT = " + beat + ", CRIME_RECORD_WARD = " + ward +
                ", CRIME_RECORD_XCOORDINATE = " + xCoord + ", CRIME_RECORD_YCOORDINATE = " + yCoord +
                ", CRIME_RECORD_LATITUDE = " + latitude + ", CRIME_RECORD_LONGITUDE = " + longitude +
                ", CRIME_RECORD_LOCATIONSTR = '" + locationStr + "', CRIME_TYPE_ID = '" + iucr.replaceFirst("^0+(?!$)", "") +
                "', CRIME_RECORD_FBICD = '" + fbiCD + "'"; //Remove leading zeros in IUCR code
        return result;
        /*
        SQL format:
        UPDATE CRIME_RECORD SET CRIME_RECORD_ID = 'HX321533', CRIME_RECORD_DATE = '2014-01-27T00:00:00.000+13:00',
        CRIME_RECORD_BLOCK = '10', CRIME_RECORD_LOCATION_DESCRIPTION = 'CONVENIENCE STORE',
        CRIME_RECORD_ARREST = 1, CRIME_RECORD_DOMESTIC = 0, CRIME_RECORD_BEAT = 423,
        CRIME_RECORD_WARD = 10, CRIME_RECORD_XCOORDINATE = 1197615.0, CRIME_RECORD_YCOORDINATE = 1847794.0,
        CRIME_RECORD_LATITUDE = 41.73719478002833, CRIME_RECORD_LONGITUDE = -87.55157400696488,
        CRIME_RECORD_LOCATIONSTR = 'CONVENIENCE STORE', CRIME_TYPE_ID = '560', CRIME_RECORD_FBICD = '08A'
        WHERE CRIME_RECORD_ID = 'HX321533'; */
    }
}
