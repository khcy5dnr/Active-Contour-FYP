package com.dental.ClusterAlgorithm;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class EdgeDetection {
	
	private Mat src;
	private Mat srcColour;
	private Mat dst;
	private Mat dstColour;
	private Mat dstX;
	private Mat dstY;
	private Mat absX;
	private Mat absY;
	private Mat dstFinal;
	
	private int kernelSize = 3;
	
	public EdgeDetection(String algorithmName) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.src = Highgui.imread("resource/saved.jpg",Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		this.srcColour = Highgui.imread("resource/saved.jpg", Highgui.CV_LOAD_IMAGE_COLOR);
		dstColour = new Mat(srcColour.rows(),srcColour.cols(),srcColour.type());
		dst = new Mat(src.rows(),src.cols(), src.type());
		dstX = new Mat(src.rows(),src.cols(), src.type());
		dstY = new Mat(src.rows(),src.cols(), src.type());		
		absX = new Mat();
		absY = new Mat();
		dstFinal = new Mat();
		
		switch(algorithmName){
		case "Sobel":						
			
			Imgproc.Sobel(src, dstX, -1, 1, 0, 3, 1, 0);
			Imgproc.Sobel(src, dstY, -1, 0, 1, 3, 1, 0);			
			
			Core.convertScaleAbs(dstX, absX);
			Core.convertScaleAbs(dstY, absY);
			
			Core.addWeighted(absX, 0.5, absY, 0.5, 0, dstFinal);
			
			Highgui.imwrite("resource/clustered.jpg",dstFinal);
			
			break;
			
		case "Kirsch":
			System.out.println("Kirsch called");
			//Kirsch Operator
			try {
		         
		         Mat kernelX = new Mat(kernelSize,kernelSize, CvType.CV_32F){
		            {
		               put(0,0,3);
		               put(0,1,3);
		               put(0,2,3);
		      
		               put(1,0,3);
		               put(1,1,0);
		               put(1,2,3);

		               put(2,0,-5);
		               put(2,1,-5);
		               put(2,2,-5);
		            }
		         };
		         
		         Mat kernelY = new Mat(kernelSize,kernelSize, CvType.CV_32F){
			            {
			               put(0,0,-5);
			               put(0,1,3);
			               put(0,2,3);
			      
			               put(1,0,-5);
			               put(1,1,0);
			               put(1,2,3);

			               put(2,0,-5);
			               put(2,1,3);
			               put(2,2,3);
			            }
			         };
			     for(int i = 0; i < 10; i++){
			    	 Imgproc.filter2D(src, dstX, -1, kernelX);
			    	 Imgproc.filter2D(src, dstY, -1, kernelY);
			     }
		         		         
		         
		         Core.convertScaleAbs(dstX, absX);
		         Core.convertScaleAbs(dstY, absY);
		         
		         //Highgui.imwrite("resource/kirschX.jpg",absX);
		         //Highgui.imwrite("resource/kirschY.jpg",absY);
		         
		         Core.addWeighted(absX, 0.5, absY, 0.5, 0, dstFinal);
				
		         Highgui.imwrite("resource/clusteredKirsch.jpg",dstFinal);
		         
		         
		      } catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		      }		
			
			break;
			
		case "Prewitt":
			System.out.println("Prewitt called");
			try {
		         
		         Mat kernelX = new Mat(kernelSize,kernelSize, CvType.CV_32F){
		            {
		               put(0,0,1);
		               put(0,1,1);
		               put(0,2,1);
		      
		               put(1,0,0);
		               put(1,1,0);
		               put(1,2,0);

		               put(2,0,-1);
		               put(2,1,-1);
		               put(2,2,-1);
		            }
		         };
		         
		         Mat kernelY = new Mat(kernelSize,kernelSize, CvType.CV_32F){
			            {
			               put(0,0,-1);
			               put(0,1,0);
			               put(0,2,1);
			      
			               put(1,0,-1);
			               put(1,1,0);
			               put(1,2,1);

			               put(2,0,-1);
			               put(2,1,0);
			               put(2,2,1);
			            }
			         };
		         
		         Imgproc.filter2D(src, dstX, -1, kernelX);
		         Imgproc.filter2D(src, dstY, -1, kernelY);
		         
		         Core.convertScaleAbs(dstX, absX);
		         Core.convertScaleAbs(dstY, absY);
				
		         Core.addWeighted(absX, 0.5, absY, 0.5, 0, dstFinal);
				
		         Highgui.imwrite("resource/clusteredPrewitt.jpg",dstFinal);
		         //Highgui.imwrite("resource/prewittX.jpg",absX);
		         //Highgui.imwrite("resource/prewittY.jpg",absY);
		         
		      } catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		      }
			
			break;
		case "Laplacian":
			System.out.println("Laplacian called");
			try {
		         
		         Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F){
		            {
		               put(0,0,0);
		               put(0,1,1);
		               put(0,2,0);
		      
		               put(1,0,1);
		               put(1,1,-4);
		               put(1,2,1);

		               put(2,0,0);
		               put(2,1,1);
		               put(2,2,0);
		            }
		         };
		         
		         Imgproc.Laplacian(src, dstX, -1);
		         Core.convertScaleAbs(dstX, absX);
		         Highgui.imwrite("resource/Laplacian.jpg",absX);
		         
			} catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		    }
			break;
		case "Laplacian of Gaussian":
			System.out.println("LOG called");
			
			Imgproc.GaussianBlur(srcColour, dstColour, new Size(0,0), 101);
	        Imgproc.cvtColor(dstColour, dst, Imgproc.COLOR_RGB2GRAY);
			
			try {		         
		         
		         Mat kernel = new Mat(kernelSize,kernelSize, CvType.CV_32F){
		            {
		               put(0,0,0);
		               put(0,1,1);
		               put(0,2,0);
		      
		               put(1,0,1);
		               put(1,1,-4);
		               put(1,2,1);

		               put(2,0,0);
		               put(2,1,1);
		               put(2,2,0);
		            }
		         };
		         
		         Imgproc.Laplacian(dst, dstX, -1);
		         Core.convertScaleAbs(dstX, absX);
		         Highgui.imwrite("resource/LOG.jpg",absX);
		         
			} catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		    }
			
			break;
		case "Robinson":
			System.out.println("Robinson called");
			try {
		         int kernelSize = 3;
		         
		         Mat kernelX = new Mat(kernelSize,kernelSize, CvType.CV_32F){
		            {
		               put(0,0,1);
		               put(0,1,1);
		               put(0,2,1);
		      
		               put(1,0,1);
		               put(1,1,-2);
		               put(1,2,1);

		               put(2,0,-1);
		               put(2,1,-1);
		               put(2,2,-1);
		            }
		         };
		         
		         Mat kernelY = new Mat(kernelSize,kernelSize, CvType.CV_32F){
			            {
			               put(0,0,-1);
			               put(0,1,1);
			               put(0,2,1);
			      
			               put(1,0,-1);
			               put(1,1,-2);
			               put(1,2,1);

			               put(2,0,-1);
			               put(2,1,1);
			               put(2,2,1);
			            }
			         };
		         
		         Imgproc.filter2D(src, dstX, -1, kernelX);
		         Imgproc.filter2D(src, dstY, -1, kernelY);
		         
		         Core.convertScaleAbs(dstX, absX);
		         Core.convertScaleAbs(dstY, absY);
				
		         Core.addWeighted(absX, 0.5, absY, 0.5, 0, dstFinal);
				
		         Highgui.imwrite("resource/clusteredRobinson.jpg",dstFinal);
		         //Highgui.imwrite("resource/RobinsonX.jpg",absX);
		         //Highgui.imwrite("resource/RobinsonY.jpg",absY);
		         
		      } catch (Exception e) {
		         System.out.println("Error: " + e.getMessage());
		      }
			
			break;
		case "Canny":
			System.out.println("Canny called");
			int threshold = 40;
			Imgproc.Canny(src, dstX, threshold, threshold*3);
			
	        Highgui.imwrite("resource/Canny.jpg",dstX);
	         
			break;
		default:
			System.out.println("Error");
		}
		
	}
}
