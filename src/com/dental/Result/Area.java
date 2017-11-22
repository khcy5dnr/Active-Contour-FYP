/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.Result;

public class Area {

	private float totalPixel = 0;
	private int size = 0;
	
	public Area(float totalPixel, int size) {
		this.totalPixel = totalPixel;
		this.size = size;
	}
	
	// Calculates the total area of pink and magenta pixels
	protected float calculateTotalArea(){
			
		float totalArea = 0;
		totalArea = (totalPixel*100/size); // Calculates the total area in percentage
		
		return totalArea;
	}
}
