package crimeSpy.uiElements.Graphs;

import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import crimeSpy.crimeData.CrimeCollectionManager;
import crimeSpy.crimeData.CrimeRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * A Class for handling drawing a Crime over Time line Chart Graph
 */
public class CrimeOverTimeGraph implements CrimeSpyCharts {

    private LineChart chart;


    /**
     * Calls update
     */
    public CrimeOverTimeGraph(){
        update();
    }


    /**
     * gets and update a chart
     * @return an updated chart
     */
    public Chart update() {
        graph();
        chart.setTitle("Crime Rate over Time");
        chart.setLegendVisible(false);
        chart.getXAxis().setLabel("Days in year");
        chart.getYAxis().setLabel("Frequency");
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
     *graphs crimes over time
     */
    private void graph(){
        Integer key;
        final NumberAxis yAxis = new NumberAxis();
        Integer min = 365;
        Integer max = 0;
        HashMap<Integer, Integer> lineData = new HashMap<Integer, Integer>();

        // Ensure there is data
        if (CrimeCollectionManager.getCurrWorkingCollection() != null) {
            for (CrimeRecord cR : CrimeCollectionManager.getCurrWorkingCollection().getCrimes()) {
                key = cR.getDate().getDayOfYear();
                min = key < min ? key : min;
                max = key > max ? key : max;
                if (lineData.containsKey(key)) {
                    lineData.replace(key, lineData.get(key) + 1);
                } else {
                    lineData.put(key, 1);
                }
            }
        }
        final NumberAxis xAxis = new NumberAxis(min, max, 30);
        xAxis.setLowerBound(min);
        xAxis.setTickUnit(30);
        XYChart.Series series = new XYChart.Series();
        for (Map.Entry<Integer, Integer> point : lineData.entrySet()){
            series.getData().add(new XYChart.Data(point.getKey(), point.getValue()));
        }
        final LineChart<Number,Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.getData().add(series);
        chart =  lineChart;
    }
}
