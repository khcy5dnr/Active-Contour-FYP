/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.ClusterAlgorithm;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

public class MeanShift {
	
	// Variable declarations
	private Mat dest;
	private Mat preprocessImage;
	
	// Constructor
	public MeanShift(Mat image){
		this.preprocessImage = image;
	}

	// Mean shift clustering method
	public Mat meanShift( ) {
		
		dest = new Mat(preprocessImage.rows(),preprocessImage.cols(),preprocessImage.type());
		
		// Run the mean shift algorithm on the image
		Imgproc.pyrMeanShiftFiltering(preprocessImage, dest, 10.0, 30.0,1,new TermCriteria(TermCriteria.MAX_ITER|TermCriteria.EPS, 50, 0.001));
		
		return dest;
	}
}
