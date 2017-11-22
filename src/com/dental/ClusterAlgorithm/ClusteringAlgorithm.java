/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.ClusterAlgorithm;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;

public class ClusteringAlgorithm {
	
	private Mat preprocessImage;
	private MeanShift meanShift;
	private KMeans kMeans;
	
	public ClusteringAlgorithm(int algorithm){
		
		String address = "resource/saved.jpg";
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		preprocessImage = Highgui.imread(address,Highgui.CV_LOAD_IMAGE_COLOR);
		
		switch(algorithm){
			case 1: 
				// Run K-Means on the image
				kMeans = new KMeans(preprocessImage);
				
				// Create the clustered image
				Highgui.imwrite("resource/clustered.jpg",kMeans.kMeans());
				break;
			case 2: 
				// Run Mean Shift on the image
				meanShift = new MeanShift(preprocessImage);
				
				// Create the clustered image
				Highgui.imwrite("resource/clustered.jpg",meanShift.meanShift());
				break;
			default:
				// Show confirmation dialog to save the image
				Alert alert = new Alert(AlertType.WARNING, "Please select the clustering algorithm!");
				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				alert.showAndWait();
				break;
		}
	}
}
