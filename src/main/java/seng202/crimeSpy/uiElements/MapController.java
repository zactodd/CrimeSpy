package seng202.crimeSpy.uiElements;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import seng202.crimeSpy.crimeData.CrimeCollection;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;
import seng202.crimeSpy.crimeData.CrimeRecord;
import seng202.crimeSpy.exceptionHandling.ResourceNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.logging.*;

/**
 * The Controller for the map class.<br>
 * Singleton pattern (yuk)! This is a quick fix for the ballooning CrimeSpyController class.
 * fx:include has been tried but is prone to errors and with the time remaining before the final deliverable
 * this is the most feasible solution.
 */
public class MapController {


    /**
     * LOGGER is the class wide instance of java.util.logging
     */
    private static final Logger LOGGER = Logger.getLogger(CrimeSpyController.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_MapController";

    // initialise the logger for fileloc
    private static Handler fileHandler;
    private static Formatter simpleFormatter;

    static {
        try {
            simpleFormatter = new SimpleFormatter();
            fileHandler  = new FileHandler(LOG_FILE_STORE_LOC);
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.INFO);
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.INFO);
            LOGGER.config("Configuration done.");
        } catch (IOException e) {
            LOGGER.warning("Failed to initiate file handler");
            LOGGER.setLevel(Level.SEVERE); // for case of console only
        }
    }


    private static boolean isGoogleMapInitiated = false;
    private static boolean isMiniMapInitiated = false;
    private static final String GOOGLE_MAP_HTML_REF = "GoogleMap.html";
    private static final String MINI_MAP_HTML_REF = "MiniMap.html";
    private static final String CRIME_REPORTING_SCRIPT = "onclick=\"reportTip()\"";
    private static final String MAP_ICON_URL = "crimeMapIcon_generic.png";
    private static boolean WEB_DOCUMENT_READY = false;
    private static final Integer MAX_NUM_POINTS = 400;

    private static WebView mapView;


    /**
     * Initialises the map view. Loads GoogleMap.html into a BorderPane (center)
     * GoogleMap.html has div tag 'map' that content (map) and scripts
     * are injected into.
     */
    public static void initializeMap(WebView theMapView) {
        mapView = theMapView;
        final WebEngine engine = mapView.getEngine();
        URL url;
        try {
            //throws ResourceNotFoundException
            url = checkResource(GOOGLE_MAP_HTML_REF);

            String urlString = url.toExternalForm();
            if (!isGoogleMapInitiated) {
                LOGGER.info("Trying to load GoogleMap.html at: " + urlString);
                engine.load(urlString);
                // To prevent attempt to access the script function before the document has loaded
                engine.documentProperty().addListener(new ChangeListener<Document>() {
                    //@Override
                    public void changed(ObservableValue<? extends Document> observableValue, Document document, Document newDoc) {
                        if (newDoc != null) {
                            engine.documentProperty().removeListener(this);
                            try {
                                engine.executeScript("initMap();");
                                isGoogleMapInitiated = true;
                                plotPoints();
                            } catch (Exception e) {
                                LOGGER.warning("Failed to execute javascript: initMap(); - at " + e.getClass().getName() + ": " + e.getMessage());
                            }
                        }
                    }
                });
            } else {
                plotPoints();
            }

        } catch (ResourceNotFoundException e) {
            LOGGER.severe("Resource not found!\n" + e.getClass().getName() + ": " + e.getMessage() + "!!!!!");
        } catch (Exception e) {
            LOGGER.warning(e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Robust check for file url: check the resource location to ensure that it is valid.
     * @param resourceLoc String url of resource
     * @return URL object of legitimate resource url
     * @throws ResourceNotFoundException if the resource is not found
     */
    private static URL checkResource(String resourceLoc) throws ResourceNotFoundException {
        LOGGER.info("Checking for url resource " + resourceLoc);
        URL url = MapController.class.getResource(resourceLoc);
        if (url == null) {
            String cName = MapController.class.getName();
            int i = cName.lastIndexOf(".");
            if (i > -1) {
                cName = cName.substring(i + 1);
            }
            cName = cName + ".class";
            // Object testPath = this.getClass().getResource(cName); // <-- can add in path but gets kinda clunky for output
            String err = "Could not find resource: " + resourceLoc;
            err += ", from class: " + cName;
            LOGGER.warning("url resource location failed: \n" + err);
            throw new ResourceNotFoundException(err);
        } else {
            return url;
        }
    }


    /**
     * This is intended to solve the problem of attempting to execute a script
     * that has not yet been loaded by listening to see if the document has loaded. <br>
     * @param scriptName The name of the script that is intended to be run (eg: "initMap();")
     * @param engine the WebEngine object on which to apply the observer
     */
    private static void delayRunScriptTillReady (final WebEngine engine, final String scriptName) {
        LOGGER.fine("Invoking delay run script on " + scriptName);
        if (WEB_DOCUMENT_READY) {
            try {
                LOGGER.finer("executeScript(" + scriptName + ");");
                engine.executeScript(scriptName);
            } catch (Exception e) {
                LOGGER.warning("failed to executeScript(\" + scriptName + \"); - " + e.getClass().getName() + ": " + e.getMessage());
            }
        }
        else {
            engine.documentProperty().addListener(new ChangeListener<Document>() {
                //@Override
                public void changed(ObservableValue<? extends Document> observableValue, Document document, Document newDoc) {
                    if (newDoc != null) {
                        engine.documentProperty().removeListener(this);
                        WEB_DOCUMENT_READY = true;
                        try {
                            LOGGER.finer("executeScript(" + scriptName + ");");
                            engine.executeScript(scriptName);
                        } catch (Exception e) {
                            LOGGER.warning("failed to executeScript(" + scriptName + "); - " + e.getClass().getName() + ": " + e.getMessage());
                        }
                    }
                }
            });
        }
    }


    /**
     * <p>Return a string description of the crime record c for plotting on the map
     * Some balance between information and space is needed. ie: too much information may
     * overcrowd the map, not enough information may be frustrating to users</p>
     * <p><b>Note:</b> the string description will be passed to javascript injection and so
     * should avoid the use of line breaks and other control characters.</p>
     * @param c the CrimeRecord to retrieve a description
     * @return string description of the crime
     */
    private static String getMapPointDescription(CrimeRecord c) {
        String s;
        // Note that style is not defined here...the css for this
        // will be in the googleMap.html file (in the head).
        // This obviously saves beaucoup space.
        s = "<div class=\"crime-info\">";
        s += "<span class=\"crime-info-heading\">Crime Incident Description</span>";
        s += "<div class=\"crime-info-data\">";
        s += "Case identification: <b>" + c.getCaseID() + "</b><br>";
        s += "Date: " + c.getDateTimeStr() + "<br>";
        s += "<hr>";
        s += "Location: " + c.getCrimeLocation().getLocationStr() + "<br>";
        s += "Beat: " + c.getCrimeLocation().getBeat() + "<br>";
        s += "<hr>";
        s += "IUCR: " + c.getCrimeType().getIucr() + "<br>";
        s += "Type: " + c.getCrimeType().getPrimaryDescription() + "<br>";
        // Reporting link in here for citizens who may have information about this crime :D
        s += "<hr>";
        s += "<a " + CRIME_REPORTING_SCRIPT + ">Submit information about this crime</a>";
        s += "</div><!-- close crime data -->";
        s += "</div><!-- close crime info -->";
        return s;
    }


    /**
     * Plot all points in the current selected collection on the map.
     */
    public static void plotPoints() {

        // Possible subject of contention is whether to store the points persistently in
        // a javascript array at the document level. This adds management functionality
        // that increases speed (eg adding/removing single points) but increase space :(

        final WebEngine engine = mapView.getEngine();
        deleteMarkers();
        CrimeCollection crimes = CrimeCollectionManager.getCurrWorkingCollection();
        if (crimes != null) {
            double latitude;
            double longitude;
            String description;
            String crimeID;
            String iconURL;
            // Add first 400 point data to the javascript array
            for (int i=0; (i<MAX_NUM_POINTS && i<crimes.getCrimes().size()); i++) {
                CrimeRecord crime = crimes.getCrimes().get(i);
                latitude = crime.getCrimeLocation().getLatitude();
                longitude = crime.getCrimeLocation().getLongitude();
                description = getMapPointDescription(crime);
                crimeID = crime.getCaseID();
                // todo: have the icon specific to the crime type. eg money for THEFT, a fist for ASSAULT etc.
                iconURL = MAP_ICON_URL;
                addPoint(latitude, longitude, description, crimeID, iconURL);
            }
            try {
                LOGGER.info("Attempting to plot points");
                delayRunScriptTillReady(engine, "setMapOnAll(map);");
            } catch (Exception e) {
                LOGGER.warning("Failed to plot points - at" + e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }


    /**
     * Clear the displayed markers on the currently loaded Google map.
     * <p>This does <b>not</b> clear the map array,
     * which remains persistent in the javascript <b>markers[]</b> array.</p>
     * In order to clear the markers completely use deleteMarkers()
     */
    private static void clearMarkers() {
        WebEngine engine = mapView.getEngine();
        delayRunScriptTillReady(engine, "clearMarkers();");
    }


    /**
     * Clear and delete the displayed markers on the currently loaded Google map.
     * <p>This will zero the javascript <b>markers[]</b> array.</p>
     * In order to only clear the markers from displaying use clearMarkers()
     */
    private static void deleteMarkers() {
        LOGGER.info("Attempting to delete all points currently in js array");
        clearMarkers();
        WebEngine engine = mapView.getEngine();
        delayRunScriptTillReady(engine, "deleteMarkers();");
    }


    /**
     * <p>
     * Add a single point to the array of points to be displayed on the map.
     * Does not otherwise alter state of map
     * ie: all previous points plotted should be persistent until removed
     * <b>javascript</b> is generated to plot the point and run with webEngine
     * </p>
     * <br>
     * This makes use of the javascript function addMarker(lat, lng, desc, title, icon) which takes
     *     <ul>
     *         <li><b>x</b> the latitude of the point to plot</li>
     *         <li><b>y</b> the longitude of the point to plot</li>
     *         <li><b>popupMsg</b> a string (textual) description of the point (optional)</li>
     *         <li><b>crimeTitle</b> A string (textual) description to display when hovering over the point</li>
     *         <li><b>crimeIcon</b> A string that describes the url of of the icon to display as a marker</li>
     *     </ul>
     * Example of Java call to WebEngine .executeScript() for javascript addMarker() function:<br>
     * <blockquote>engine.executeScript("<b>addMarker(latitude, longitude, 'THEFT', 'H11234', 'c:/pointer.png');</b>");</blockquote>
     * Note the use of quotes in the addMarker() function(') so as not to break the outer quotes(")
     * @param lat latitude of plot point
     * @param lng longitude of plot point
     * @param desc textual (String) description of crime. (default is empty)
     * @param cID the crimeID (caseID) to display when hovering over a marker
     * @param iconURL the url of the icon if the Google default is not to be used
     */
    private static void addPoint(double lat, double lng, String desc, String cID, String iconURL) {

        LOGGER.fine("Attempting to add points to javascript array");
        WebEngine engine = mapView.getEngine();
        // Generate script to plot a point
        String  s;
        s = "addMarker(" + lat + ", " + lng + ", '" + desc + "', '" + cID + "', '" + iconURL + "');";
        // Stuff the addPoint script into webEngine
        try {
            delayRunScriptTillReady(engine, s);
        } catch (Exception e) {
            LOGGER.warning("Failed to add points - at" + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * This function checks various conditions of a location (latitude
     * or longitude). Will return 0 if the parameter is invalid or empty.
     * @param loc the latitude or longitude to check
     * @return string representation of a location point (latitude or longitude)
     */
    private static String valPlotLoc(String loc) {
        if (loc.length() <= 0) {
            loc = "0";
        } else if (loc.isEmpty()) {
            loc = "0";
        }
        return loc;
    }


    /**
     * Sets up the mini map display for the information
     * pane. If this is already set up this function will
     * just run the script to plot a point on the map.
     */
    public static void initMiniMap(WebView miniMap, TextField infoLat, TextField infoLong) {
        final WebEngine engine = miniMap.getEngine();
        URL url;
        String latitude = valPlotLoc(infoLat.getText());
        String longitude = valPlotLoc(infoLong.getText());


        final String script = "initMiniMap(" + latitude +  "," + longitude + ");";

        if (isMiniMapInitiated) {
            delayRunScriptTillReady(engine, script);
        } else {
            try {
                //throws ResourceNotFoundException
                url = checkResource(MINI_MAP_HTML_REF);

                String urlString = url.toExternalForm();

                if (!isMiniMapInitiated) {
                    LOGGER.info("Trying to load GoogleMap.html at: " + urlString);
                    engine.load(urlString);
                    // To prevent attempt to access the script function before the document has loaded
                    engine.documentProperty().addListener(new ChangeListener<Document>() {
                        //@Override
                        public void changed(ObservableValue<? extends Document> observableValue, Document document, Document newDoc) {
                            if (newDoc != null) {
                                engine.documentProperty().removeListener(this);
                                try {
                                    engine.executeScript(script);
                                    isMiniMapInitiated = true;
                                } catch (Exception e) {
                                    LOGGER.warning("Failed to execute javascript: " + script + " - at " + e.getClass().getName() + ": " + e.getMessage());
                                }
                            }
                        }
                    });
                } else {
                    delayRunScriptTillReady(engine, script);
                }
            } catch (ResourceNotFoundException e) {
                LOGGER.severe("Resource not found!\n" + e.getClass().getName() + ": " + e.getMessage() + "!!!!!");
            } catch (Exception e) {
                LOGGER.warning(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }
}
