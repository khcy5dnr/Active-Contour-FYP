/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.Result;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Result {

	private PixelCalculation pixelCalculate = null;
	private Area area = null;
	private float totalPixel = 0;
	private float totalArea = 0;	

	public Result() throws IOException {
		
		BufferedImage address;
		int width, height;
	
		address = ImageIO.read(new File("resource/clustered.jpg"));
		width = address.getWidth();
		height = address.getHeight();
	    
	    pixelCalculate = new PixelCalculation(width, height, address);
	    totalPixel = pixelCalculate.pixelCalculation();
	    
	    area = new Area(totalPixel, width*height);
	    totalArea = area.calculateTotalArea();
	    
	    ImageIO.write(address,"jpg",new File("resource/segmented.jpg"));
	}
	
	public PixelCalculation getPixelCalculate() {
		return pixelCalculate;
	}

	public float getTotalPixel() {
		return totalPixel;
	}
	public float getTotalArea() {
		return totalArea;
	}
}
