/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.Result;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PixelCalculation {
	
	private int width;
	private int height;
	private int totalPixels = 0;
	private float HSV[] = new float[3];
	private BufferedImage address;
	private ArrayList<Integer> brightness;
	private HashMap<Integer, Integer> graph = new HashMap<Integer, Integer>();
	private int brightnessCount = 0;
	private boolean flag_NoPink = false;

	// Constructor
	public PixelCalculation(int width, int height, BufferedImage address) {
		this.width = width;
		this.height = height;
		this.address = address;
		brightness = new ArrayList<Integer>();
	}

	// Calculates the total of pink and magenta pixel
	protected int pixelCalculation() {
	    
	    for(int Y=0; Y<height;Y++)
	    {
	            for(int X=0;X<width;X++)
	            {
	                 RGBtoHSV(X,Y);
	                 changePixels(X,Y);
	            }
	    }
	    Collections.sort(brightness);
	    brightnessCount();	
	    brightness.clear();
	    return totalPixels;
	}
	
	// Changes the RGB value to the HSV value
	private void RGBtoHSV(int x, int y){
		
		int RGB = address.getRGB(x, y);
        int R = (RGB >> 16) & 0xff;
        int G = (RGB >> 8) & 0xff;
        int B = (RGB) & 0xff;
        Color.RGBtoHSB(R,G,B,HSV);
	}
	
	// Segments the pink and magenta pixel 
	private void changePixels(int x, int y){
		
		boolean flag = false;
		float a = 100*HSV[0];
        float b = 100*HSV[1];
        float c = 100*HSV[2];
		Color white = Color.white;
		
		// Calculates the total pink and magenta pixels
		if(( a >= 75 && a < 97) && ( b >= 40 && b < 101  ) && (c >= 40 && c < 101)) {
        	flag = true;
        	totalPixels++;
        	brightness.add((int) c);
        	
        }
		
		// Converts the non-pink and non-magenta pixel to white pixel
		if(flag == false){
			address.setRGB(x, y, white.getRGB());
        }
		
		
	}
	
	private void brightnessCount(){
		if(!brightness.isEmpty()){
			int pivot = brightness.get(0);
			graph.put(pivot,0);
			
			for(int i=0; i<totalPixels; i++){
				
				if(pivot == brightness.get(i)){
					brightnessCount++;
				}
				else{
					graph.put(pivot,brightnessCount);
					pivot = brightness.get(i);
					brightnessCount = 1;
				}
				graph.put(pivot,brightnessCount);
			}
		}
		else{
			flag_NoPink = true;
		}
	}
	
	public boolean isFlag_NoPink() {
		return flag_NoPink;
	}

	public HashMap<Integer,Integer> getHashMap_Data(){
		return graph;
	}
}
