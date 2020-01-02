package crimeSpy.uiElements.Graphs;

import javafx.scene.chart.*;
import javafx.scene.text.Font;
import crimeSpy.crimeData.CrimeCollectionManager;
import crimeSpy.crimeData.CrimeRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * A Class for handling drawing a Crime Frequency Bar Chart Graph
 */
public class CrimeFrequencyGraph implements CrimeSpyCharts {

    private BarChart chart;


    /**
     * Calls update
     */
    public CrimeFrequencyGraph(){
        update();
    }


    /**
     * gets and update a chart
     * @return an updated graph
     */
    public Chart update() {
        graph();
        chart.setTitle("Frequency of Crime Types");
        chart.getXAxis().setLabel("Crime Types");
        chart.getYAxis().setLabel("Frequency");
        chart.setLegendVisible(false);
        return chart;
    }


    /**
     * transition the graph to a fonts to larger view
     */
    public void toMainStage(){
        chart.getXAxis().setTickLabelFont(new Font(12.0));
    }


    /**
     * transition the graph to a fonts to smaller view
     */
    public void toMiniStage(){
        chart.getXAxis().setTickLabelFont(new Font(4.0));

    }


    /**0
     *
     * Creates a bar graph of location vs crime frequency
     */
    private void graph(){
        String key;
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart =
                new BarChart<String,Number>(xAxis,yAxis);
        barChart.setTitle("Frequency of Crime Type");
        HashMap<String, Integer> barData = new HashMap<String, Integer>();
        Integer total = 0;
        //Ensure there is data
        if (CrimeCollectionManager.getCurrWorkingCollection() != null) {
            total = CrimeCollectionManager.getCurrWorkingCollection().getCrimes().size();
            for (CrimeRecord cR : CrimeCollectionManager.getCurrWorkingCollection().getCrimes()) {
                key = cR.getCrimeType().getPrimaryDescription();
                if (barData.containsKey(key)) {
                    barData.replace(key, barData.get(key) + 1);
                } else {
                    barData.put(key, 1);
                }
            }
        }
        XYChart.Series series = new XYChart.Series();
        for (Map.Entry<String, Integer> bar : barData.entrySet()){
            if(bar.getValue() > (total * 0.03)){
                series.getData().add(new XYChart.Data(bar.getKey(), bar.getValue()));
            }
        }
        barChart.getData().add(series);
        chart =  barChart;
    }
}
