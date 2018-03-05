/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControllerPopup implements Initializable {

	@FXML
	private HBox Preview = new HBox();
	
	@FXML
	private Menu help = new Menu("Help");
	
	@FXML
	private Menu file = new Menu("File");
	
	@FXML
	private MenuBar menuBar = new MenuBar();
	
	@FXML
	private Button btnBack = new Button();
	
	@FXML
	private ImageView iv = new ImageView();
		
	private int saved = 0;
	private Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		File file = new File("resource/clustered.jpg");
		FileReader fr = null;
		try	
		{
			fr = new FileReader(file);
			Image image = new Image(file.toURI().toString());
		    iv = new ImageView(image);
		    iv.setFitWidth(300);
		    iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setX(120);
		    Preview.getChildren().add(iv);
		    fr.close();
		}catch(FileNotFoundException e)
		{
			System.out.println("File doesn't exist");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// To display all MenuItems under File when it is clicked
	@FXML
	private void onClickFile() {
		MenuItem itmSave = new MenuItem("Save");
		file.getItems().addAll(itmSave);
		menuBar.getMenus().addAll(file);
	}
	
	// To display MenuItems in Help option ('About' in this case)
	@FXML 
	private void onClickHelp() {
		MenuItem itmAbout = new MenuItem("About");
		help.getItems().addAll(itmAbout);
		menuBar.getMenus().addAll(help);
		
		// Display copyright information when 'Help' is clicked
		Alert alert = new Alert(AlertType.INFORMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    	alert.setTitle("Help");
    	alert.setHeaderText("Automated Image Dental Analysis");
    	alert.setContentText("Version 1.0 - Last Updated March 2017\n\n"
    			+ "Copyright 2017 Group 2 UNMC.\n "
    			+ "All rights reserved.\n\n"
    			+ "This software is made possible by OpenCV and Scene Builder.\n");
    	alert.showAndWait();
	}
	
	@FXML
	private void onClickBack() throws IOException {		
		if(saved == 0){
			
			// Show confirmation dialog to save the image
			Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to save the image?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				onClickSave();
			}else if(alert.getResult() == ButtonType.NO){
				stage=(Stage)btnBack.getScene().getWindow();
			    stage.close();
			}
		}else{			
			// Close the application
			stage=(Stage)btnBack.getScene().getWindow();
			stage.close();
		}
	}
	
	@FXML
	private void onClickExit() throws IOException{
		
		
	}
	
	// Save the segmented file
	@FXML
	private void onClickSave() {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        
        // Set the type of saved file
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Show the Save Image file chooser
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(iv.getImage(),
                    null), "jpg", file);
                saved = 1;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
	}

}
