package seng202.crimeSpy.uiElements.Graphs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import seng202.crimeSpy.crimeData.CrimeCollectionManager;
import seng202.crimeSpy.crimeData.CrimeRecord;

/**
 * A Class for handling drawing a Arrest Pie Chart Graph
 */
public class ArrestGraph implements CrimeSpyCharts {

    private PieChart chart;


    /**
     * Calls update
     */
    public ArrestGraph(){
        update();
    }


    /**
     * gets and update a chart
     * @return an updated graph
     */
    public Chart update() {
        graph();
        chart.setTitle("Arrests Made");
        chart.setLegendVisible(false);

        return chart;
    }


    /**
     *transition the graph to a fonts to larger view
     */
    public void toMainStage(){

    }


    /**
     *transition the graph to a fonts to smaller view
     */
    public void toMiniStage(){

    }


    /**
     *Creates a pie graph of arrests made
     */
    private void graph(){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int noArrest = 0;
        int arrests = 0;

        // Ensure there is data
        if (CrimeCollectionManager.getCurrWorkingCollection() != null) {
            for(CrimeRecord cR : CrimeCollectionManager.getCurrWorkingCollection().getCrimes()){
                if (cR.getArrest()){
                    arrests += 1;
                }else{
                    noArrest += 1;
                }
            }
        }
        pieChartData.add(new PieChart.Data("Yes", arrests));
        pieChartData.add(new PieChart.Data("No", noArrest));
        chart = new PieChart(pieChartData);
    }
}
