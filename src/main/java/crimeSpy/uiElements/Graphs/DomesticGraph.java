package crimeSpy.uiElements.Graphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import crimeSpy.crimeData.CrimeCollectionManager;
import crimeSpy.crimeData.CrimeRecord;

/**
 * A Class for handling drawing a Domestic Pie Chart Graph
 */
public class DomesticGraph implements CrimeSpyCharts {


    private PieChart domesticChart;


    /**
     * Performs basic view update/initialisation and returns the
     * Graph object<br>
     * @return graph object for domestic state comparison in current crime collection.
     */
    public PieChart update() {
        graph();
        domesticChart.setTitle("Crimes that involved domestic incident");
        domesticChart.setLegendVisible(false);
        return domesticChart;
    }


    /**
     * Construct data for graph object based on the
     * <i>CrimeCollectionManager.currentWorkingCollection</i>
     */
    private void graph() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Integer domestic = 0;
        Integer noDomestic = 0;

        // Ensure there is data to chart!
        if (CrimeCollectionManager.getCurrWorkingCollection() != null) {
            for(CrimeRecord cR : CrimeCollectionManager.getCurrWorkingCollection().getCrimes()){
                // aggregate data for the red/black pie chart.
                if (cR.getDomestic()) {
                    domestic += 1;
                } else {
                    noDomestic += 1;
                }
            }
        }
        pieChartData.add(new PieChart.Data("Yes", domestic));
        pieChartData.add(new PieChart.Data("No", noDomestic));
        domesticChart = new PieChart(pieChartData);
    }


    // Other interface methods not used.


    /**
     *transition the graph to a fonts to larger view
     */
    @Override
    public void toMainStage() {

    }


    /**
     *transition the graph to a fonts to smaller view
     */
    @Override
    public void toMiniStage() {

    }

}
