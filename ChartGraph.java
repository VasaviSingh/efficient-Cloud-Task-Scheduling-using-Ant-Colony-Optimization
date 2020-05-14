package org.cloudbus.cloudsim.examples;

import org.jfree.chart.ChartPanel;    //ChartPanel class from the org.jfree.chart package is used as a swing GUI component for displaying JfreeChart object.
import org.jfree.chart.ChartFactory;    //ChartFactory is an abstract class under the org.jfree.chart package. It provides a collection of utility methods for generating standard charts.
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;   //A base class for creating the main frame for simple applications. The frame listens for window closing events, and responds by shutting down the JVM. This is OK for small demo applications...for more serious applications, you'll want to use something more robust
import org.jfree.chart.plot.PlotOrientation;  //his is a serialized class available in org.jfree.chart.plot package and it is used to show the orientation of a 2D plot. The orientation can either be vertical or horizontal. It sets the orientation of Y-axis. A conventional plot has a vertical Y- axis.
import org.jfree.data.category.DefaultCategoryDataset;


//JfreeChart is an open source library developed in Java. It can be used within Java based applications to create a wide range of charts.

public class ChartGraph extends ApplicationFrame {

	public ChartGraph( String applicationTitle , String chartTitle,String a,String b,DefaultCategoryDataset dataset ) {
	      super(applicationTitle);
	      JFreeChart lineChart = ChartFactory.createLineChart(    //createLineChart(java.lang.String title, java.lang.String categoryAxisLabel, java.lang.String valueAxisLabel, 
                                                                    CategoryDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls)
	         chartTitle,a,b,
	         dataset,
	         PlotOrientation.VERTICAL,
	         true,true,false);
	         
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
	      setContentPane( chartPanel );
	   }  
}