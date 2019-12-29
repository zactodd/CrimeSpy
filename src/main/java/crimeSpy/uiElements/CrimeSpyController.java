package crimeSpy.uiElements;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import crimeSpy.crimeData.CrimeCollectionManager;
import crimeSpy.crimeData.CrimeRecord;
import crimeSpy.crimeData.FilterCrimeCollection;
import crimeSpy.crimeData.SearchCrimeCollection;
import crimeSpy.uiElements.Graphs.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.*;


/**
 * The most important class of all GUI elements in crimeSpy
 * Controls all view tabs and most navigation functionality
 */
public class CrimeSpyController {

    /**
     * LOGGER is the class wide instance of java.util.logging
     */
    private static final Logger LOGGER = Logger.getLogger(CrimeSpyController.class.getName());
    private static final String LOG_FILE_STORE_LOC = "./LOG_CrimeSpyController";

    // initialise the logger for fileloc
    private static Handler fileHandler;
    private static SimpleFormatter simpleFormatter;

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

    @FXML
    private Tab gridTab;

    @FXML
    private Tab graphTab;

    @FXML
    private Tab mapTab;

    @FXML
    private AnchorPane crimeDataInfoPane;

    @FXML
    private ComboBox<String> crimeCollectionSelector = new ComboBox<String>(CrimeCollectionManager.observableList);

    @FXML
    private DatePicker filterDateFrom;

    @FXML
    private DatePicker filterDateTo;

    @FXML
    private TextField filterLocation;

    @FXML
    private VBox filterCrimeType;

    @FXML
    private TextField filterWard;

    @FXML
    private TextField filterBeat;

    @FXML
    private CheckBox filterDomestic;

    @FXML
    private CheckBox filterArrest;

    @FXML
    private TextField searchItem;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView gridTable;

    @FXML
    private TableColumn caseIDCol;

    @FXML
    private TableColumn dateCol;

    @FXML
    private TableColumn arrestCol;

    @FXML
    private TableColumn domesticCol;

    @FXML
    private TableColumn iucrCol;

    @FXML
    private TableColumn fbiCDCol;

    @FXML
    private TableColumn priDescripCol;

    @FXML
    private TableColumn secDescripCol;

    @FXML
    private TableColumn blockCol;

    @FXML
    private TableColumn beatCol;

    @FXML
    private TableColumn wardCol;

    @FXML
    private TableColumn latitudeCol;

    @FXML
    private TableColumn longitudeCol;

    @FXML
    private TableColumn locStrCol;

    @FXML
    private CheckBox filterIsDomestic;

    @FXML
    private CheckBox filterIsArrest;

    @FXML
    private TextField filterPrimary;

    @FXML
    private TextField filterSecondary;

    @FXML
    private TextField filterBlock;

    @FXML
    private TextField filterFBICD;

    @FXML
    private TextField filterIUCR;

    private ArrayList<CheckBox> crimeTypeCheckBoxes = new ArrayList<CheckBox>();

    @FXML
    private TextField infoLocation;

    @FXML
    private TextField infoLat;

    @FXML
    private TextField infoLong;

    @FXML
    private TextField infoXCoor;

    @FXML
    private TextField infoYCoor;

    @FXML
    private TextField infoPrimary;

    @FXML
    private TextField infoSecondary;

    @FXML
    private CheckBox infoArrest;

    @FXML
    private CheckBox infoDomestic;

    @FXML
    private TextField infoIUCR;

    @FXML
    private TextField infoFBI;

    @FXML
    private DatePicker infoDate;

    @FXML
    private TextField infoBlock;

    @FXML
    private TextField infoWard;

    @FXML
    private TextField infoBeat;

    @FXML
    private TextField infoCrimeID;

    @FXML
    private TextField infoTimeNext;

    @FXML
    private TextField infoDistNext;

    @FXML
    private TextField infoTimePrev;

    @FXML
    private TextField infoDistPrev;

    @FXML
    private TextField infoTimeHour;

    @FXML
    private TextField infoTimeMinute;

    @FXML
    private AnchorPane filterPane;

    @FXML
    private BorderPane controllerPane;

    @FXML
    private Button editButton;

    @FXML
    private Label infoHeader;

    @FXML
    private HBox infoSaveBar;

    @FXML
    private  AnchorPane miniGraph1;

    @FXML
    private  AnchorPane miniGraph2;

    @FXML
    private  AnchorPane miniGraph3;

    @FXML
    private  AnchorPane miniGraph4;

    @FXML
    private  AnchorPane mainGraph;

    @FXML
    private HashMap<AnchorPane, CrimeSpyCharts> graphs = new HashMap<AnchorPane, CrimeSpyCharts>();

    @FXML
    private Tab currentTab;

    @FXML
    private HBox prevNextBar;

    @FXML
    private WebView miniMap;

    @FXML
    private WebView mapView;


    /**
     * A method to initialize various items in the UI, called first before anything
     * Includes LOGGER initialisation:
     * <p>logging for this class is stored in the file specified by the static variable at the top of the class code.
     * Current level of data stored is <b>INFO</b>.
     * Typically fine and lower are used for script calls, info is for function or method start and end.</p>
     * <p>The console is used (with a level of <b>SEVERE</b> in the event that the FileHandler can not be created)</p>
     */
    public void initializeController() {

        // Init everthing!!
        initGraph();
        initializeTableView();
        initFilter();
        MapController.initializeMap(mapView);
        MapController.plotPoints();
        initInfo();
        currentTab = tabPane.getSelectionModel().getSelectedItem();

        // Initalise the crime collection selector drop down
        crimeCollectionSelector.getItems().clear();
        crimeCollectionSelector.setItems(CrimeCollectionManager.observableList);
        if (CrimeCollectionManager.getAllCrimeCollections().size() != 0) {
            LOGGER.info("Found at least one crime collection in collection manager");
            LOGGER.info("Setting the working collection to the first collection in AllCrimeCollections.");
            crimeCollectionSelector.setValue(CrimeCollectionManager.getAllCrimeCollections().get(0).getName());

        } else {
            // Opens a nice welcome menu for the uninitiated
            goWelcomeMenu();
            LOGGER.info("Failed to find any crime collections in the collection manager.");
        }


        crimeCollectionSelector.valueProperty().addListener(new ChangeListener<String>() {
            // The listener for a change in the selected value of the CrimeCollectionSelector combo box
            public void changed(ObservableValue ov, String oldValue, String newValue) {
                //Known bugs here, see documentation in final report
                CrimeCollectionManager.setCurrWorkingCollection(newValue);
                updateTableView();
                updateGraph();
                MapController.initializeMap(mapView);
                initFilter();
            }
        });

        //Allows 'hotkeys' in the grid table - at the moment just delete
        gridTable.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.DELETE && !editMode) {
                    try {
                        goToDeleteMenu();
                    } catch (IOException e) {
                        LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
                    }
                }
            }
        });
    }


    /**
     * For data initializing when needed for gridTable view etc.
     */
    private void initializeTableView() {
        LOGGER.info("Beginning Table View Init from initializeTableView()");
                caseIDCol = new TableColumn<CrimeRecord, String>("Case ID");
                caseIDCol.setCellValueFactory(new PropertyValueFactory("caseID"));
                dateCol = new TableColumn<CrimeRecord, String>("Time/Date");
                dateCol.setCellValueFactory(new PropertyValueFactory("date"));
                arrestCol = new TableColumn<CrimeRecord, Boolean>("Arrest made");
                arrestCol.setCellValueFactory(new PropertyValueFactory("arrest"));
                domesticCol = new TableColumn<CrimeRecord, Boolean>("Domestic");
                domesticCol.setCellValueFactory(new PropertyValueFactory("domestic"));
                iucrCol = new TableColumn<CrimeRecord, String>("IUCR");
                iucrCol.setCellValueFactory(new PropertyValueFactory("iucr"));
                fbiCDCol = new TableColumn<CrimeRecord, String>("FBI Crime Descriptor");
                fbiCDCol.setCellValueFactory(new PropertyValueFactory("fbiCD"));
                priDescripCol = new TableColumn<CrimeRecord, String>("Primary Description");
                priDescripCol.setCellValueFactory(new PropertyValueFactory("primaryDescription"));
                secDescripCol = new TableColumn<CrimeRecord, String>("Secondary Description");
                secDescripCol.setCellValueFactory(new PropertyValueFactory("secondaryDescription"));
                blockCol = new TableColumn<CrimeRecord, String>("Block");
                blockCol.setCellValueFactory(new PropertyValueFactory("block"));
                beatCol = new TableColumn<CrimeRecord, Integer>("Beat");
                beatCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, Integer>("beat"));
                wardCol = new TableColumn<CrimeRecord, Integer>("Ward");
                wardCol.setCellValueFactory(new PropertyValueFactory("ward"));
                latitudeCol = new TableColumn<CrimeRecord, Double>("Latitude");
                latitudeCol.setCellValueFactory(new PropertyValueFactory("latitude"));
                longitudeCol = new TableColumn<CrimeRecord, Double>("Longitude");
                longitudeCol.setCellValueFactory(new PropertyValueFactory("longitude"));
                locStrCol = new TableColumn<CrimeRecord, String>("Location");
                locStrCol.setCellValueFactory(new PropertyValueFactory("locStr"));
        gridTable.getColumns().setAll(caseIDCol, dateCol, arrestCol, domesticCol, iucrCol, fbiCDCol, priDescripCol,
                secDescripCol, blockCol, beatCol, wardCol, latitudeCol, longitudeCol, locStrCol);
    }

    /**
     * Updates the table view with the current working collection
     */
    private void updateTableView() {
        gridTable.getColumns().setAll(caseIDCol, dateCol, arrestCol, domesticCol, iucrCol, fbiCDCol, priDescripCol,
                secDescripCol, blockCol, beatCol, wardCol, latitudeCol, longitudeCol, locStrCol);
        ObservableList<CrimeRecord> crimes = CrimeCollectionManager.getCurrWorkingCollection().getObservableCrList();
        gridTable.setItems(crimes);
    }


//===================================================graph section=====================================================
//=====================================================================================================================
//=====================================================================================================================


    /**
     * initise the graph selector combo box
     */
    @FXML
    private void initGraph(){
        setChartInPane(new CrimeOverTimeGraph(), mainGraph);
        setChartInPane(new CrimeFrequencyGraph(), miniGraph1);
        setChartInPane(new CrimeLocationGraph(), miniGraph2);
        setChartInPane(new ArrestGraph(), miniGraph3);
        setChartInPane(new DomesticGraph(), miniGraph4);
    }


    /**
     * selectes what is being graphed
     */
    @FXML
    private void updateGraph(){
        ArrayList<AnchorPane> anchors = new ArrayList<AnchorPane>(graphs.keySet());
        for(AnchorPane aP : anchors){
            setChartInPane(graphs.get(aP), aP);
        }
    }


    /**
     * swap miniGraphs with mainGraph on mouseClick
     */
    @FXML
    private void swapWithMain(MouseEvent mouseEven){
        ArrayList<AnchorPane> anchors = new ArrayList<AnchorPane>(graphs.keySet());
        for(final AnchorPane aP : anchors) {
            if(! aP.equals(mainGraph)) {
                aP.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            CrimeSpyCharts tempChart = graphs.get(mainGraph);
                            setChartInPane(graphs.get(aP), mainGraph);
                            graphs.get(aP).toMainStage();
                            setChartInPane(tempChart, aP);
                            tempChart.toMiniStage();
                        }
                    }
                });
            }
        }
    }


    /**
     * Set a chart in anchor pane in its desired format
     * @param chart the chart to set in the anchorPane
     * @param aP the anchorPane
     */
    @FXML
    private void setChartInPane(CrimeSpyCharts chart, AnchorPane aP){
        if(aP.getChildren().size() != 0){
            aP.getChildren().remove(0);
        }
        Node graph = chart.update();
        aP.getChildren().add(graph);
        graphs.replace(aP, chart);
        graphs.put(aP, chart);
        AnchorPane.setLeftAnchor(graph, 0.0);
        AnchorPane.setBottomAnchor(graph, 0.0);
        AnchorPane.setRightAnchor(graph , 0.0);
        AnchorPane.setTopAnchor(graph , 0.0);
        if(aP.equals(mainGraph)){
            chart.toMainStage();
        }else {
            chart.toMiniStage();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  TAB SELECTORS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * For the view drop-down, to go to the Map view (tab 0)
     * @param event the tab has been selected
     */
    @FXML
    private void goToMapView(ActionEvent event) {
        tabPane.getSelectionModel().select(0);
    }


    /**
     * For the view drop-down, to go to the Grid view (tab 1)
     * @param event tab is selected
     */
    @FXML
    private void goToGridView(ActionEvent event) {
        tabPane.getSelectionModel().select(1);
    }


    /**
     * For the view drop-down, to go to the Graph view (tab 2)
     * @param event tab is selected
     */
    @FXML
    private void goToGraphView(ActionEvent event) { tabPane.getSelectionModel().select(2);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  FORM VALIDATION
    // If you want to make changes please read and learn how it works: don't just hack something together.
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private Label iucrValidationFeedbackLabel;

    @FXML
    private Label beatValidationFeedbackLabel;

    @FXML
    private Label wardValidationFeedbackLabel;

    @FXML
    private Label latitudeValidationFeedbackLabel;

    @FXML
    private Label longitudeValidationFeedbackLabel;

    @FXML
    private Label timeHourValidationFeedbackLabel;

    @FXML
    private Label timeMinuteValidationFeedbackLabel;


    /**
     * Clear all the validation labels of messages.<br>
     * This is preferable than another controller attempting to remember how many
     * and what are the validation labels to clear.<br>
     * Example use would be to clear the messages if the cotext is suddenly
     * changed or the current record being viewed is switched to another.<br>
     * <u>Includes: </u>
     * <ul>
     *     <li><b>iucrValidationFeedbackLabel</b> - for the IUCR code validation</li>
     *     <li><b>beatValidationFeedbackLabel</b> - for beat number validation</li>
     *     <li><b>wardValidationFeedbackLabel</b> - for ward number validation</li>
     *     <li><b>latitudeValidationFeedbackLabel</b> - for latitude validation</li>
     *     <li><b>longitudeValidationFeedbackLabel</b> - for latitude validation</li>
     *     <li><b>timeHourValidationFeedbackLabel</b> - for Hour validation</li>
     *     <li><b>timeMinuteValidationFeedbackLabel</b> - for minute validation</li>
     * </ul>
     */
    private void clearAllValidationMessages() {
        // If you add labels to be cleared please also update the javadoc above
        iucrValidationFeedbackLabel.setText("");
        beatValidationFeedbackLabel.setText("");
        wardValidationFeedbackLabel.setText("");
        latitudeValidationFeedbackLabel.setText("");
        longitudeValidationFeedbackLabel.setText("");
        timeHourValidationFeedbackLabel.setText("");
        timeMinuteValidationFeedbackLabel.setText("");
    }


    /**
     * Check all existing validation fields to see if they are empty
     * @return true iff all validation textfields are empty
     */
    private boolean validationFieldsOk() {
        boolean isOK = true;
        if (    (iucrValidationFeedbackLabel.getText().length() != 0) ||
                (beatValidationFeedbackLabel.getText().length() != 0) ||
                (wardValidationFeedbackLabel.getText().length() != 0) ||
                (latitudeValidationFeedbackLabel.getText().length() != 0) ||
                (longitudeValidationFeedbackLabel.getText().length() != 0) ||
                (timeHourValidationFeedbackLabel.getText().length() != 0) ||
                (timeMinuteValidationFeedbackLabel.getText().length() != 0)) {
            isOK = false;
        }
        return isOK;
    }


    /**
     * Check if the current input in the iucr entry textfield is valid
     * and display a helpful message if it is not.
     */
    public void validateIUCR() {
        String errMsg = CrimeCollectionManager.validateIUCR(infoIUCR.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            iucrValidationFeedbackLabel.setText(errMsg);
        } else {
            iucrValidationFeedbackLabel.setText("");
        }
    }


    /**
     * Check if the current input in the beat entry textfield is valid
     * and display a helpful message if it is not.
     */
    public void validateBeat() {
        String errMsg = CrimeCollectionManager.validateBeat(infoBeat.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            beatValidationFeedbackLabel.setText(errMsg);
        } else {
            beatValidationFeedbackLabel.setText("");
        }
    }


    /**
     * Check if the current input in the ward entry textfield is valid
     * and display a helpful message if it is not.
     */
    public void validateWard() {
        String errMsg = CrimeCollectionManager.validateWard(infoWard.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            wardValidationFeedbackLabel.setText(errMsg);
        } else {
            wardValidationFeedbackLabel.setText("");
        }
    }


    /**
     * Check if the current input in the latitude entry textfield is valid
     * and display a helpful message if it is not. Note that latitude
     * should be between -90 and 90 degrees inclusive as defined in the
     * CrimeLocation class
     */
    public void validateLatitude() {
        String errMsg = CrimeCollectionManager.validateLatitude(infoLat.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            latitudeValidationFeedbackLabel.setText(errMsg);
        } else {
            latitudeValidationFeedbackLabel.setText("");
        }
    }


    /**
     * Check if the current input in the longitude entry textfield is valid
     * and display a helpful message if it is not. Note that longitude
     * should be between -180 and 180 degrees inclusive as defined in the
     * CrimeLocation class
     */
    public void validateLongitude() {
        String errMsg = CrimeCollectionManager.validateLongitude(infoLong.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            longitudeValidationFeedbackLabel.setText(errMsg);
        } else {
            longitudeValidationFeedbackLabel.setText("");
        }
    }


    /**
     * Check if the current input in the Hour entry textfield is valid
     * and display a helpful message if it is not.
     */
    public void validateTimeHour() {
        String errMsg = CrimeCollectionManager.validateHour(infoTimeHour.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            timeHourValidationFeedbackLabel.setText(errMsg);
        } else {
            timeHourValidationFeedbackLabel.setText("");
        }
    }


    /**
     * Check if the current input in the Minute entry textfield is valid
     * and display a helpful message if it is not.
     */
    public void validateTimeMinute() {
        String errMsg = CrimeCollectionManager.validateMinute(infoTimeMinute.getText());
        // Display the error message in the appropriate place
        if (errMsg.length() != 0) {
            timeMinuteValidationFeedbackLabel.setText(errMsg);
        } else {
            timeMinuteValidationFeedbackLabel.setText("");
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INFO SECTION
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * This array enables the temporary storage of crime data as it is being edited or created.
     * This is intended to enable the user to revert back to the previous data view.<br>
     * data should be stored in this array in the following order:
     * <ol>
     *     <li> - CrimeID</li>
     *     <li> - Date</li>
     *     <li> - Arrest</li>
     *     <li> - Domestic</li>
     *     <li> - Beat</li>
     *     <li> - Ward</li>
     *     <li> - Block</li>
     *     <li> - FBI CD</li>
     *     <li> - IUCR number</li>
     *     <li> - Primary Description</li>
     *     <li> - Secondary Description</li>
     *     <li> - Longitude</li>
     *     <li> - Latitude</li>
     *     <li> - xCoordinate</li>
     *     <li> - yCoordinate</li>
     *     <li> - Location Description</li>
     * </ol>
     */
    private ArrayList<String> previousData = new ArrayList<String>(Arrays.asList("", "", "", "",
                            "", "", "", "", "", "", "", "", "", "", "", ""));
    private boolean editMode = false;


    /**
     * This function enables the temporary storage of crime data as it is being edited or created.
     * This is intended to enable the user to revert back to the previous data view.<br>
     */
    private void storeCurrentData() {
        previousData = new ArrayList<String>();
        previousData.add(infoCrimeID.getText());
        previousData.add("12/12/12");//infoDate.getAccessibleText()); //<----fix
        previousData.add(infoArrest.isSelected() ? "Y" : "N");
        previousData.add(infoDomestic.isSelected() ? "Y" : "N");
        previousData.add(infoBeat.getText());
        previousData.add(infoWard.getText());
        previousData.add(infoBlock.getText());
        previousData.add(infoFBI.getText());
        previousData.add(infoIUCR.getText());
        previousData.add(infoPrimary.getText());
        previousData.add(infoSecondary.getText());
        previousData.add(infoLong.getText());
        previousData.add(infoLat.getText());
        previousData.add(infoXCoor.getText());
        previousData.add(infoYCoor.getText());
        previousData.add(infoLocation.getText());
        previousData.add(infoTimeHour.getText());
        previousData.add(infoTimeMinute.getText());
    }


    /**
     * selects next item in the gridTable
     */
    @FXML
    private void nextTableItem(){
        int next = gridTable.getSelectionModel().getSelectedIndex() + 1;
        gridTable.getSelectionModel().select(next  >= gridTable.getItems().size() ? 0 : next);
        setCrimeInformation();
    }


    /**
     * selects previousitem in the gridTable
     */
    @FXML
    private void prevTableItem(){
        int prev = gridTable.getSelectionModel().getSelectedIndex() - 1;
        gridTable.getSelectionModel().select(prev  < 0 ? gridTable.getItems().size() - 1 : prev);
        setCrimeInformation();
    }


    /**
     * This function enables the restoring of crime data if it is no longer being edited or created.
     * This is intended to enable the user to revert back to the previous data view.<br>
     */
    private void restoreCurrentData() {
        infoCrimeID.setText(previousData.get(0));
        //Date javaUtilDate = previousData.get(1);
        //infoDate.setValue(javaUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        infoArrest.setSelected(previousData.get(2).equals("Y") ? true : false);
        infoDomestic.setSelected(previousData.get(3).equals("Y") ? true : false);
        infoBeat.setText(previousData.get(4));
        infoWard.setText(previousData.get(5));
        infoBlock.setText(previousData.get(6));
        infoFBI.setText(previousData.get(7));
        infoIUCR.setText(previousData.get(8));
        infoPrimary.setText(previousData.get(9));
        infoSecondary.setText(previousData.get(10));
        infoLong.setText(previousData.get(11));
        infoLat.setText(previousData.get(12));
        infoXCoor.setText(previousData.get(13));
        infoYCoor.setText(previousData.get(14));
        infoLocation.setText(previousData.get(15));
        if (previousData.size() > 16) {
            infoTimeHour.setText(previousData.get(16));
            infoTimeMinute.setText(previousData.get(17));
        }
    }


    /**
     *start close information panel to being with
     */
    private void initInfo(){
        controllerPane.setRight(new AnchorPane());
        infoHeader.setVisible(true);
        infoHeader.setText("Information");
        MapController.initMiniMap(miniMap, infoLat, infoLong);
    }


    /**
     * open the information on double click of the table
     */
    @FXML
    private void quickOpenInfoPane(MouseEvent mouseEven){
        gridTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cancelEdit();
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 1 && editButton.isVisible()) {
                        setCrimeInformation();
                    } else if (mouseEvent.getClickCount() == 2) {
                        setCrimeInformation();
                        openInfoPane();
                        // changeInfoItemVisibility(false);
                    }
                }
            }
        });
    }


    /**
     * Quick class that resizes everything else when the info pane is clicked on.
     * Allows a better UI design
     * Toggle class, so will work both ways
     */
    @FXML
    void toggleInfoPane() {
        crimeDataInfoPane.setVisible(!crimeDataInfoPane.isVisible());
        if(crimeDataInfoPane.isVisible()) {
            controllerPane.setRight(crimeDataInfoPane);
            changeInfoItemVisibility(! editButton.isVisible());
        } else {
            controllerPane.setRight(new AnchorPane());
        }
    }


    /**
     * opens the information pane
     */
    @FXML
    private void openInfoPane(){
        controllerPane.setRight(crimeDataInfoPane);
        crimeDataInfoPane.setVisible(true);
        storeCurrentData();
    }


    /**
     * Populates the crimeRecord Info side pane with the currently selected crime record
     */
    @FXML
    void setCrimeInformation(){
        PeriodFormatter periodFormat = new PeriodFormatterBuilder()
                .appendHours()
                .appendSuffix(" hour", " hours")
                .appendSeparator(" and ")
                .appendMinutes()
                .appendSuffix(" minute", " minutes")
                .toFormatter();

        CrimeRecord cR = (CrimeRecord) gridTable.getSelectionModel().getSelectedItem();
        // Only if we have loaded data otherwise we could have some null pointer exceptions thrown
        if (cR != null) {
            infoCrimeID.setText(cR.getCaseID());
            infoArrest.setSelected(cR.getArrest());
            infoDomestic.setSelected(cR.getDomestic());
            infoBeat.setText(cR.getCrimeLocation().getBeat().toString());
            infoWard.setText(cR.getCrimeLocation().getWard().toString());
            infoBlock.setText(cR.getCrimeLocation().getBlock());
            infoFBI.setText(cR.getFbiCD());
            infoIUCR.setText(cR.getCrimeType().getIucr());
            Date javaUtilDate = cR.getDate().toDate();
            infoDate.setValue(javaUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            infoTimeHour.setText(cR.getHour().toString());
            infoTimeMinute.setText(cR.getMinute().toString());
            infoPrimary.setText(cR.getCrimeType().getPrimaryDescription());
            infoSecondary.setText(cR.getCrimeType().getSecondaryDescription());
            infoLong.setText(Double.toString(cR.getCrimeLocation().getLongitude()));
            infoLat.setText(Double.toString(cR.getCrimeLocation().getLatitude()));
            infoXCoor.setText(Double.toString(cR.getCrimeLocation().getxCoordinate()));
            infoYCoor.setText(Double.toString(cR.getCrimeLocation().getyCoordinate()));
            infoLocation.setText(cR.getCrimeLocation().getLocationStr());
            infoDistNext.setText(cR.getNextCrimeDistance().toString());
            infoTimeNext.setText(cR.getNextCrimeTimeSince().toString(periodFormat));
            infoDistPrev.setText(CrimeCollectionManager.getFullWorkingCollection().getCrimeRecord(cR.getPrevCrimeID()).getNextCrimeDistance().toString());
            infoTimePrev.setText(CrimeCollectionManager.getFullWorkingCollection().getCrimeRecord(cR.getPrevCrimeID()).getNextCrimeTimeSince().toString(periodFormat));

            MapController.initMiniMap(miniMap, infoLat, infoLong);
            // Keep up to date with what we want to be able to retrieve
            storeCurrentData();
            // With each new record clear any validation messages that have been left behind
            clearAllValidationMessages();
        }
    }


    /**
     * Toggles the edibility/visibility of all the crimeRecord info inputs
     * For editing and creating crime records
     * TODO Crime Type fields auto update depending on typed IUCR
     */
    private void changeInfoItemVisibility(boolean newBool) {
        editMode = newBool;
        boolean disableNew = !newBool; //Inverse of newBool
        infoCrimeID.setEditable(false);
        infoArrest.setDisable(disableNew);
        infoDomestic.setDisable(disableNew);
        infoBeat.setEditable(newBool);
        infoWard.setEditable(newBool);
        infoBlock.setEditable(newBool);
        infoFBI.setEditable(newBool);
        infoIUCR.setEditable(newBool);
        infoDate.setEditable(newBool);
        //infoPrimary.setEditable(true); Change the IUCR, not this
        //infoSecondary.setEditable(true); Change the IUCR, not this
        infoLong.setEditable(newBool);
        infoLat.setEditable(newBool);
        infoXCoor.setEditable(newBool);
        infoYCoor.setEditable(newBool);
        infoLocation.setEditable(newBool);
        infoTimeHour.setEditable(newBool);
        infoTimeMinute.setEditable(newBool);
    }


    /**
     * cancel the editing of crime record.
     */
    @FXML
    private void cancelEdit(){
        editMode = false;
        infoHeader.setText("Information");
        editButton.setVisible(true);
        editButton.setDisable(false);
        infoSaveBar.setVisible(false);
        prevNextBar.setVisible(true);

        // Stop the ability to edit the data
        changeInfoItemVisibility(false);

        // If we cancel the validation messages will no longer be applicable so clear them
        clearAllValidationMessages();


        // Data values should be reset to their original form.
        // for now we reload them though it may be safer to store them in a second memory space
        restoreCurrentData();
    }


    /**
     * Toggles the edit menu
     */
    @FXML
    void goToEditMenu() {
        // We only want to do this if there is crime data to actually edit
        if (CrimeCollectionManager.getCurrWorkingCollection() != null) {
            openInfoPane();
            changeInfoItemVisibility(true);
            infoSaveBar.setVisible(true);
            editButton.setVisible(false);
            prevNextBar.setVisible(false);
            infoHeader.setText("Edit");
        }
    }


    /**
     * Allows a crime record to be created,
     * Shows the create menu
     */
    @FXML
    void goToCreateMenu(ActionEvent event) {
        openInfoPane();
        changeInfoItemVisibility(true);
        infoHeader.setText("Create");
        gridTable.getSelectionModel().clearSelection();
        //change visibility or edibility
        infoSaveBar.setVisible(true);
        editButton.setVisible(false);
        infoCrimeID.setEditable(true);
        prevNextBar.setVisible(false);

        //clear or init all inputs
        infoArrest.setSelected(false);
        infoDomestic.setSelected(false);
        infoCrimeID.clear();
        infoBeat.clear();
        infoWard.clear();
        infoBlock.clear();
        infoFBI.clear();
        infoIUCR.clear();
        infoDate.setValue(LocalDate.now());
        infoPrimary.clear();
        infoSecondary.clear();
        infoLong.clear();
        infoLat.clear();
        infoXCoor.clear();
        infoYCoor.clear();
        infoLocation.clear();
        infoTimeHour.clear();
        infoTimeMinute.clear();
        infoTimeNext.clear();
        infoTimePrev.clear();
        infoDistNext.clear();
        infoDistPrev.clear();

        storeCurrentData();
    }


    /**
     * Attempts to save a new crime record or an edited crime record
     */
    @FXML
    void goSave(ActionEvent event) {
        // First we check all validation fields
        if (validationFieldsOk()) {
            if (gridTable.getSelectionModel().getSelectedItem() != null) {
                //We are editing rather than creating
                CrimeRecord cR = (CrimeRecord) gridTable.getSelectionModel().getSelectedItem();
                if (infoBlock.getText().equals("") || infoIUCR.getText().equals("")) {
                    //check block or Iucr are not Null
                    displayWarning("Warning", "Block or IUCR have not been set");
                } else {
                    cR.setArrest(infoArrest.isSelected());
                    cR.setDomestic(infoDomestic.isSelected());
                    cR.crimeLocation.setBeat(Integer.parseInt(infoBeat.getText()));
                    cR.crimeLocation.setWard(Integer.parseInt(infoWard.getText()));
                    cR.crimeLocation.setBlock(infoBlock.getText());
                    cR.crimeLocation.setLatitude(Double.parseDouble(infoLat.getText()));
                    cR.crimeLocation.setLongitude(Double.parseDouble(infoLong.getText()));
                    cR.crimeLocation.setxCoordinate(Double.parseDouble(infoXCoor.getText()));
                    cR.crimeLocation.setyCoordinate(Double.parseDouble(infoYCoor.getText()));
                    cR.crimeLocation.setLocationStr(infoLocation.getText());
                    cR.setFbiCD(infoFBI.getText());
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-DD");
                    cR.setDate(DateTime.parse(infoDate.getValue().toString(), fmt));
                    cR.setHour(Integer.parseInt(infoTimeHour.getText()));
                    cR.setMinute(Integer.parseInt(infoTimeMinute.getText()));
                    cR.crimeType.setCrimeType(infoIUCR.getText());
                    // Get rid of personal address info
                    cR.obfuscate();

                    Integer resCode = CrimeCollectionManager.getFullWorkingCollection().updateCrimeRecord(cR);

                    if (resCode == 0) {
                        // Update successful
                        displayWarning("Message", "The crime record was updated correctly");
                        CrimeCollectionManager.getFullWorkingCollection().sortCrimeRecordsByTime();
                        //saveCurrentButton.setVisible(false);
                        changeInfoItemVisibility(false);
                        infoCrimeID.setEditable(false);
                        infoSaveBar.setVisible(false);
                        updateGraph();
                        updateTableView();
                        editButton.setVisible(true);
                        editButton.setDisable(false);
                        infoHeader.setText("Information");
                    } else if (resCode == 1) {
                        displayWarning("Error", "Problem During Edit");
                        //TODO Reset the crime Record to original
                    }
                }
            } else {
                // We are creating!
                if (infoCrimeID.getText().equals("") || infoBlock.getText().equals("") || infoIUCR.getText().equals("")) {
                    //check Case ID, block or Iucr are not Null
                    displayWarning("Warning", "CaseID, Block or IUCR have not been set");
                } else {
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-DD");
                    CrimeRecord newcr = new CrimeRecord(
                            infoCrimeID.getText(),
                            DateTime.parse(infoDate.getValue().toString(), fmt),
                            infoArrest.isSelected(),
                            infoDomestic.isSelected(),
                            infoIUCR.getText(),
                            infoFBI.getText(),
                            infoBlock.getText(),
                            Integer.parseInt(infoBeat.getText()),
                            Integer.parseInt(infoWard.getText()),
                            Double.parseDouble(infoXCoor.getText()),
                            Double.parseDouble(infoYCoor.getText()),
                            Double.parseDouble(infoLat.getText()),
                            Double.parseDouble(infoLong.getText()),
                            infoLocation.getText());

                    //Add specific hours and minutes from textboxes
                    newcr.setHour(Integer.parseInt(infoTimeHour.getText()));
                    newcr.setMinute(Integer.parseInt(infoTimeMinute.getText()));
                    // add in obfuscation here
                    newcr.obfuscate();
                    Integer resCode = CrimeCollectionManager.getFullWorkingCollection().addCrimeRecordtoDB(newcr);
                    if (resCode == 0) {
                        // Write successful
                        displayWarning("Message", "Record was successfully added");
                        CrimeCollectionManager.getFullWorkingCollection().sortCrimeRecordsByTime();
                        //saveCurrentButton.setVisible(false);
                        changeInfoItemVisibility(false);
                        infoCrimeID.setEditable(false);
                        updateGraph();
                        updateTableView();
                        editButton.setVisible(true);
                        editButton.setDisable(false);
                        infoHeader.setText("Information");
                    } else if (resCode == 1) {
                        displayWarning("Error", "Duplicate Case ID");
                    } else if (resCode == 2) {
                        displayWarning("Error", "SQL Problem During Write");
                    } else if (resCode == 3) {
                        displayWarning("Error", "Other Problem During Write");
                    }
                }
            }
        }
    }


//===================================Open, save, import and export=====================================================
//=====================================================================================================================
//=====================================================================================================================


    /**
     * Launches the Open File Menu.
     * Sets the title of the window, modality that it must be responded to, not resizeable.
     * Calls method to initialise internal data of this window.
     * @throws IOException
     */
    @FXML
    void goToOpenFileMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("openUI.fxml"));
            Parent root = fxmlLoader.load();
            OpenController openController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Open File");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            openController.initData();
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the Import File Menu.
     * Sets the title of the window, modality that it must be responded to, not resizeable.
     * Calls method to initialise internal data of this window.
     * @throws IOException
     */
    @FXML
    void goToImportFileMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("importUI.fxml"));
            Parent root = fxmlLoader.load();
            ImportController importController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Import File");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            importController.initData();
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the Save File Menu.
     * Sets the title of the window, modality that it must be responded to, not resizeable.
     * Calls method to initialise internal data of this window.
     * @throws IOException
     */
    @FXML
    void goToSaveFileMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("saveUI.fxml"));
            Parent root = fxmlLoader.load();
            SaveController saveController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Save File");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            saveController.initData();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the Export File Menu.
     * Sets the title of the window, modality that it must be responded to, not resizeable.
     * Calls method to initialise internal data of this window.
     * @throws IOException
     */
    @FXML
    void goToExportFileMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("exportUI.fxml"));
            Parent root = fxmlLoader.load();
            ExportController ExportController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Export File");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            ExportController.initData();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the About File Menu.
     * Sets the title of the window, modality that it must be responded to, not resizeable.
     * Calls method to initialise internal data of this window.
     * @throws IOException
     */
    @FXML
    void goToAboutMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("aboutUI.fxml"));
            Parent root = fxmlLoader.load();
            AboutController aboutController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("About");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            aboutController.initData();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the Help File Menu.
     * Sets the title of the window, modality that it must be responded to, not resizeable.
     * Calls method to initialise internal data of this window.
     * @throws IOException
     */
    @FXML
    void goToHelpMenu() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("helpUI.fxml"));
            Parent root = fxmlLoader.load();
            HelpController helpController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Help Menu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            helpController.initData();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the Delete Dialog menu, which ensures the user confirms he wants to delete
     */
    @FXML
    void goToDeleteMenu() throws IOException {
        try {
            CrimeRecord cR = (CrimeRecord) gridTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("confirmDeletionBox.fxml"));
            Parent root = fxmlLoader.load();
            ConfirmDeletionBoxController confirmDeletionBoxController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            confirmDeletionBoxController.initData(cR);
            stage.showAndWait();
            updateTableView();
            updateGraph();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Launches the Welcome menu at first system startup
     */
    private void goWelcomeMenu(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcomeUI.fxml"));
            Parent root = fxmlLoader.load();
            WelcomeController welcomeController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            stage.toFront();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * Allows a Alertbox to be shown for any message
     * @param header  The header of the alert box
     * @param message THe message shown to the user.
     */
    private void displayWarning(String header, String message) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alertBox.fxml"));
            Parent root = fxmlLoader.load();
            AlertBoxController alertBoxController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            alertBoxController.initData(header, message);
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    //================================================search section=======================================================
    //=====================================================================================================================
    //=====================================================================================================================


    /**
     * enables search over crime data
     */
    @FXML
    void goSearchButton(){
        try {

            gridTable.setItems(SearchCrimeCollection.search(searchItem.getText()).getObservableCrList());
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * clear text field if the word search on click
     *
     */
    @FXML
    private void clearSearch(){
        if (searchItem.getText().equals("Search...")) {
            searchItem.setText("");
        }
    }


    /**
     * adds the word search to text field if text is empty
     */
    @FXML

    private void addsSearch(){
        if(searchItem.getText().equals("")){
            searchItem.setText("Search...");
        }
    }


    //==============================================filter section==========================================================
    //======================================================================================================================
    //======================================================================================================================


    /**
     * toggle the filter pane visibility
     */
    @FXML
    private void toggleFilter(){
        filterPane.setVisible(! filterPane.isVisible());
        if(filterPane.isVisible()) {
            controllerPane.setLeft(filterPane);
        } else {
            controllerPane.setLeft(new AnchorPane());
        }
    }


    /**
     * filters crimeData in table view
     */
    @FXML
    private void goFilter(){
        try {
            FilterCrimeCollection.filter(getDomesticFromGUI(), getArrestFromGUI(), getCrimeTypesFromGUI(),
                    getLocationFromGUI(), getBeatFromGUI(), getWardFromGUI(), getBlockFromGUI(), getPrimaryFromGUI(),
                    getSecondaryFromGUI(), getIUCRFromGUI(), getFBICDFromGUI(), getDateToFromGUI(), getDateFromFromGUI());
            filterTab();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * determine which is filtered and filters it
     */
    @FXML
    private void filterTab(){
        if(tabPane.getSelectionModel().getSelectedItem().equals(gridTab)) {
            // If there is no crime data loaded then this would be silly to set right?
            if (CrimeCollectionManager.getCurrWorkingCollection() != null) {
                gridTable.setItems(CrimeCollectionManager.getCurrWorkingCollection().getObservableCrList());
            }
        } else if (tabPane.getSelectionModel().getSelectedItem().equals(mapTab)){
            MapController.plotPoints();
        } else if(tabPane.getSelectionModel().getSelectedItem().equals(graphTab)){
            updateGraph();
        }
    }


    /**
     * set the selected item in the filter to there start state
     */
    @FXML
    private void clearFilter() {
        for (CheckBox cB : crimeTypeCheckBoxes) {
            cB.setSelected(false);
        }
        filterDateTo.setValue(null);
        filterSecondary.setText("Secondary Description...");
        filterPrimary.setText("Primary Description...");
        filterBeat.setText("Beat...");
        filterArrest.setSelected(false);
        filterIsArrest.setSelected(false);
        filterDateFrom.setValue(null);
        filterDomestic.setSelected(false);
        filterIsDomestic.setSelected(false);
        filterBlock.setText("Block...");
        filterLocation.setText("Location...");
        filterWard.setText("Ward...");
        filterIUCR.setText("IUCR...");
        filterFBICD.setText("FBICD...");
    }


    /**
     * init filter with crimeType primary descriptions
     */
    private void initFilter() {
        try {
            ArrayList<CrimeRecord> currentRecords = CrimeCollectionManager.getCurrWorkingCollection().getCrimes();
            HashSet<String> crimeTypes = new HashSet();
            filterCrimeType.getChildren().clear();
            String crimeType;
            CheckBox tempCheckBox;
            for(CrimeRecord cR : currentRecords) {
                crimeType = cR.getCrimeType().getPrimaryDescription(); //.toLowerCase();//makes the first letter captize
                //crimeType = crimeType.substring(0,1).toUpperCase() + crimeType.substring(1);
                if(crimeTypes.add(cR.getCrimeType().getPrimaryDescription())){
                    tempCheckBox = new CheckBox();
                    tempCheckBox.setText(crimeType);
                    tempCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        public void changed(ObservableValue<? extends Boolean> ov,
                                            Boolean old_val, Boolean new_val) {
                            goFilter();
                        }
                    });
                    crimeTypeCheckBoxes.add(tempCheckBox);
                    filterCrimeType.getChildren().add(tempCheckBox);
                }
            }
            getCrimeTypesFromGUI();
        } catch (Exception e) {
            LOGGER.warning("Error in " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * checks if a tab has been changed
     */
    @FXML
    private void tabChange(){
        if(! currentTab.equals(tabPane.getSelectionModel().getSelectedItem())){
            currentTab = tabPane.getSelectionModel().getSelectedItem();
            filterTab();

        }
    }


    //=============================================gets data form text field================================================
    //======================================================================================================================
    //======================================================================================================================


    /**
     * clear text field if the word location on click
     */
    @FXML
    private void clearLocation(){
        if (filterLocation.getText().equals("Location...")) {
            filterLocation.setText("");
        }
    }


    /**
     * @return the beat if the field has been set otherwise returns - 1
     */
    private int getBeatFromGUI(){
        int beat = -1;
        if(! filterBeat.getText().equals("Beat...") && ! filterBeat.getText().equals("")) {
            beat = Integer.parseInt(filterBeat.getText());
        }
        return beat;
    }


    /**
     * @return the ward if the text field has been set otherwise returns - 1
     */
    private int getWardFromGUI(){
        int ward = -1;
        if(! filterWard.getText().equals("Ward...") && ! filterWard.getText().equals("")) {
            ward = Integer.parseInt(filterWard.getText());
        }
        return ward;
    }


    /**
     * @return the FBICD if the text field has been set otherwise returns - 1
     */
    private String getFBICDFromGUI(){
        String FBICD = "";
        if(! filterFBICD.getText().equals("FBICD...") && ! filterFBICD.getText().equals("")) {
            FBICD = filterFBICD.getText();
        }
        return FBICD;
    }


    /**
     * @return the IUCR if the text field has been set otherwise returns - 1
     */
    private String getIUCRFromGUI(){
        String IUCR = "";
        if(! filterIUCR.getText().equals("IUCR...") && ! filterIUCR.getText().equals("")) {
            IUCR = filterIUCR.getText();
        }
        return IUCR;
    }


    /**
     * @return the location if the text field has been set otherwise returns an empty string
     */
    private String getLocationFromGUI(){
        String location = "";
        if(! filterLocation.getText().equals("Location...") && ! filterLocation.getText().equals("")) {
            location = filterLocation.getText();
        }
        return location;
    }


    /**
     * @return the location if the text field has been set otherwise returns an empty string
     */
    private String getBlockFromGUI(){
        String location = "";
        if(! filterBlock.getText().equals("Block...") && ! filterBlock.getText().equals("")) {
            location = filterBlock.getText();
        }
        return location;
    }


    /**
     * @return the primary description if the text field has been set otherwise returns an empty string
     */
    private String getPrimaryFromGUI(){
        String location = "";
        if(! filterPrimary.getText().equals("Primary Description...") && ! filterPrimary.getText().equals("")) {
            location = filterPrimary.getText();
        }
        return location;
    }


    /**
     * @return the primary description if the text field has been set otherwise returns an empty string
     */
    private String getSecondaryFromGUI(){
        String location = "";
        if(! filterSecondary.getText().equals("Secondary Description...") && ! filterSecondary.getText().equals("")) {
            location = filterSecondary.getText();
        }
        return location;
    }


    /**
     * @return all the selected crimeTypes in an arrayList
     */
    private ArrayList<String> getCrimeTypesFromGUI(){
        ArrayList<String> crimesSelected = new ArrayList<String>();
        for(CheckBox cB : crimeTypeCheckBoxes){
            if(cB.isSelected()){
                crimesSelected.add(cB.getText());
            }
        }
        return crimesSelected;
    }


    /**
     * @return the value of domestic the if has been chosen to be filter otherwise return null
     */
    private Boolean getDomesticFromGUI(){
        Boolean domestic = null;
        if(filterIsDomestic.isSelected()){
            domestic = filterDomestic.isSelected();
        }
        return domestic;
    }


    /**
     * @return the value of domestic the if has been chosen to be filter otherwise return null
     */
    private Boolean getArrestFromGUI(){
        Boolean arrest = null;
        if(filterIsArrest.isSelected()){
            arrest = filterArrest.isSelected();
        }
        return arrest;
    }


    /**
     * @return the value of date to if has been chosen to be filter otherwise return null
     */
    private DateTime getDateToFromGUI(){
        LocalDate locTo = filterDateTo.getValue();
        if(locTo == null){
            return null;
        }
        return new DateTime(locTo.getYear(), locTo.getMonthValue(), locTo.getDayOfMonth(), 0, 0);
    }


    /**
     * @return the value of domestic from if has been chosen to be filter otherwise return null
     */
    private DateTime getDateFromFromGUI(){
        LocalDate locFrom = filterDateFrom.getValue();
        if(locFrom == null){
            return null;
        }
        return new DateTime(locFrom.getYear(), locFrom.getMonthValue(), locFrom.getDayOfMonth(), 0, 0);
    }


    /**
     * adds the word location to text field if text is emtpy
     */
    @FXML
    private void addsLocation(){
        if(filterLocation.getText().equals("")){
            filterLocation.setText("Location...");
        }
    }


    /**
     * clear text field if the word beat
     **/
    @FXML
    private void clearBeat(){
        if (filterBeat.getText().equals("Beat...")) {
            filterBeat.setText("");
        }
    }


    /**
     * adds the word beat to text field if text is emtpy
     *
     */
    @FXML
    private void addsBeat(){
        if(filterBeat.getText().equals("")){
            filterBeat.setText("Beat...");
        }
    }


    /**
     * clear text field if the word ward
     */
    @FXML
    private void clearWard(){
        if (filterWard.getText().equals("Ward...")) {
            filterWard.setText("");
        }
    }



    /**
     * adds the word ward to text field if text is emtpy
     *
     */
    @FXML
    private void addsWard(){
        if(filterWard.getText().equals("")){
            filterWard.setText("Ward...");
        }
    }


    /**
     * adds the word Longitude to text field if text is emtpy
     *
     */
    @FXML
    private void addsIUCR(){
        if(filterIUCR.getText().equals("")){
            filterIUCR.setText("IUCR...");
        }
    }


    /**
     * clear text field if the word Longitude
     *
     */
    @FXML
    private void clearIUCR() {
        if (filterIUCR.getText().equals("IUCR...")) {
            filterIUCR.setText("");
        }
    }


    /**
     * adds the word Longitude to text field if text is emtpy
     */
    @FXML
    private void addsPrimary(){
        if(filterPrimary.getText().equals("")){
            filterPrimary.setText("Primary Description...");
        }
    }


    /**
     * clear text field if the word Longitude
     */
    @FXML
    private void clearPrimary() {
        if (filterPrimary.getText().equals("Primary Description...")) {
            filterPrimary.setText("");
        }
    }


    /**
     * adds the word Longitude to text field if text is emtpy
     *
     */
    @FXML
    private void addsSecondary(){
        if(filterSecondary.getText().equals("")){
            filterSecondary.setText("Secondary Description...");
        }
    }


    /**
     * clear text field if the word Longitude
     */
    @FXML
    private void clearSecondary() {
        if (filterSecondary.getText().equals("Secondary Description...")) {
            filterSecondary.setText("");
        }
    }


    /**
     * adds the word Longitude to text field if text is emtpy
     */
    @FXML
    private void addsBlock(){
        if(filterBlock.getText().equals("")){
            filterBlock.setText("Block...");
        }
    }


    /**
     * clear text field if the word Longitude
     */
    @FXML
    private void clearBlock() {
        if (filterBlock.getText().equals("Block...")) {
            filterBlock.setText("");
        }
    }


    /**
     * adds the word Longitude to text field if text is emtpy
     */
    @FXML
    private void addsFBICD(){
        if(filterFBICD.getText().equals("")){
            filterFBICD.setText("FBICD...");
        }
    }


    /**
     * clear text field if the word Longitude
     */
    @FXML
    private void clearFBICD() {
        if (filterFBICD.getText().equals("FBICD...")) {
            filterFBICD.setText("");
        }
    }
}
