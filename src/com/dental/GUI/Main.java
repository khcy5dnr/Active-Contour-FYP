/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.GUI;
	
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.dental.Process.PreProcessing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("GUI_Image.fxml"));			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			Image icon = new Image(getClass().getResourceAsStream("iconCamera.png"));
			primaryStage.getIcons().add(icon);
			primaryStage.setTitle("Edge Detection with Active Contour");
			primaryStage.show();
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
				@Override
				public void handle(WindowEvent t)
				{	
					try {
						Files.deleteIfExists(Paths.get("resource/saved.jpg"));
						PreProcessing.setSigmaX(101);
						PreProcessing.setBeta(-50);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Platform.exit();
					System.exit(0);
				}
				
				});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
