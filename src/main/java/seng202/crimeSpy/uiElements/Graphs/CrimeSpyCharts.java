package seng202.crimeSpy.uiElements.Graphs;

import javafx.scene.chart.Chart;

/**
 * A Class for handling all graphs
 */
public interface CrimeSpyCharts {

    /**
     * gets and update a chart
     * @return the updated chart
     */
    Chart update();


    /**
     * transition the graph to a fonts to larger view
     */
    void toMainStage();

    /**
     * transition the graph to a fonts to smaller view
     */
    void toMiniStage();

}
