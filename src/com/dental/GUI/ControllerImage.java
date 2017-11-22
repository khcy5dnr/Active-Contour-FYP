/*Copyright (c) 2017 Group 2 UNMC.
 * 
 * All rights reserved.
 * 
 * This software is made possible by OpenCV and Scene Builder.
 * 
 * */

package com.dental.GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

import com.dental.Process.LoadImage;
import com.dental.Process.PreProcessing;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.HBox;

public class ControllerImage extends Main implements Initializable {

	@FXML
	private Slider sharpSlider;
	
	@FXML
	private Slider brightSlider;
	
	@FXML
	private Button btnAutoEnhance = new Button();
		
	@FXML
	private HBox Preview = new HBox();
	
	@FXML
	private Button btnNext = new Button();
	
	@FXML
    private ToggleButton toggleManualEnhance;
	
	@FXML
	private MenuBar menuBar = new MenuBar();
	
	@FXML
	private Menu file = new Menu("File");
	
	@FXML
	private Menu help = new Menu("Help");
	
	@FXML
	private MenuItem itmOpen = new MenuItem();
	
	@FXML
	private MenuItem itmClose = new MenuItem();
	
	@FXML
	private MenuItem itmExit = new MenuItem();
	
	@FXML
	private MenuItem itmAbout = new MenuItem();
	
	@FXML
	private ImageView imageView = new ImageView();
	
	private File selectedfile;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		sharpSlider.setMin(50);
		sharpSlider.setMax(100);
		
		brightSlider.setMin(-50);
		brightSlider.setMax(50);
		
		
		// Listen for Sharpness slider value changes
		sharpSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				sharpSlider.setCache(true);
				PreProcessing.setSigmaX(newValue.intValue());
				try {
					onClickManualEnchance();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Listen for Brightness slider value changes
		brightSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,Number oldValue, Number newValue) {
				brightSlider.setCache(true);
				PreProcessing.setBeta((double)newValue.intValue());
				try {
					onClickManualEnchance();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Disable the Sharpness and Brightness slider
		sharpSlider.setDisable(true);
		brightSlider.setDisable(true);
		
		// Disable the Open menu item
		itmOpen.setDisable(true);
		
		File file = new File("resource/saved.jpg");
		FileReader fr = null;
		try	
		{
			fr = new FileReader(file);
			Image image = new Image(file.toURI().toString());
		    ImageView iv = new ImageView(image);
		    iv.setFitWidth(300);
		    iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setX(120);
		    Preview.getChildren().add(iv);
		    if(!iv.isCache())
		    {
		    	// Disable the Next button
				btnNext.setDisable(true);
				
				// Disable the Close menu item
				itmClose.setDisable(true);
				
				// Enable the Open menu item
				itmOpen.setDisable(false);
				
				// Disable the Auto Enhance button
				btnAutoEnhance.setDisable(true);
				
				// Disable the Manual Enhance toggle button
				toggleManualEnhance.setDisable(true);
		    }
		    fr.close();
		}catch(FileNotFoundException e)
		{
			// Disable the Next button
			btnNext.setDisable(true);
			
			// Disable the Close menu item
			itmClose.setDisable(true);
			
			// Enable the Open menu item
			itmOpen.setDisable(false);
			
			// Disable the Auto Enhance button
			btnAutoEnhance.setDisable(true);
			
			// Disable the Manual Enhance toggle button
			toggleManualEnhance.setDisable(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
		
	@FXML
	private void onClickOpen() throws IOException {		
		
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Choose image");
	    
	    // File format restrictions - .png, .jpg, .jpeg
	    chooser.getExtensionFilters().add(new ExtensionFilter("Image Files (*.jpg, *.jpeg)", "*.jpg", "*.jpeg") );             		
	    selectedfile = chooser.showOpenDialog(new Stage());
	    
	    if(selectedfile != null){
	    	
	    	// Taking file and storing it as image
	    	BufferedImage bimg = ImageIO.read(selectedfile);
	    
			try {
			    // retrieve image
			    BufferedImage bi = bimg;
			    
			    File outputfile = new File("resource/saved.jpg");
			    ImageIO.write(bi, "jpg", outputfile);
			    

			    File outputfile_Original = new File("resource/original.jpg");
			    ImageIO.write(bi, "jpg", outputfile_Original);
			    
				btnNext.setDisable(false);
				
				// Enable the Close menu item
				itmClose.setDisable(false);
				
				// Disable the Open menu item
				itmOpen.setDisable(true);
				
				// Enable the Auto Enhance button
				btnAutoEnhance.setDisable(false);
				
				// Enable the Manual Enhance toggle button
				toggleManualEnhance.setDisable(false);
				
			} catch (IOException e) {
				btnNext.setDisable(true);
			}
		    
		    // Getting dimensions of the image
		    int width          = bimg.getWidth();				
		    int height         = bimg.getHeight();
		    
		    
		    // Check if the image size is acceptable
		    if(width>1366&&height>768||width>1366||height>768) {
		    	
		    	// Disable the Close menu item
				itmClose.setDisable(true);
				
				// Enable the Open menu item
				itmOpen.setDisable(false);
				
				//Disable next button
				btnNext.setDisable(true);
				
				// Disable the Auto Enhance button
				btnAutoEnhance.setDisable(true);
				
				// Disable the Manual Enhance toggle button
				toggleManualEnhance.setDisable(true);
		    	
		    	// Alert box appears when if statement is true 
		    	Alert alert = new Alert(AlertType.ERROR);   	
		    	DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		    	alert.setTitle("Error");
		    	alert.setHeaderText("Upload Fail");
		    	alert.setContentText("Image to be uploaded must be less than 1.0 MB");
		    	alert.showAndWait();		    	
		    }
		    	
		    // If file is of appropriate dimensions run the following code
		   	else {
		    		
		   		// Converting buffered image into an Image so JavaFX methods work
		   		Image image = SwingFXUtils.toFXImage(bimg, null);
		   		imageView.setImage(image);
		   		
		   		// Only ImageView is resized to fit not the original "image"
		   		imageView.setFitWidth(300);
		   		
		   		// Making sure previewed image is not too big for screen
		   		imageView.setPreserveRatio(true);
		   		imageView.setSmooth(true);
		   		imageView.setCache(true);
		   		imageView.setX(120);
		   		
		   		// Prints out image inside HBox "Preview"
		   		Preview.getChildren().add(imageView);
		   } 
	    }
	    else if(selectedfile == null){
	    	//do nothing
	    }
	}
	
	@FXML
	private void onClickClose() throws IOException {
		Files.deleteIfExists(Paths.get("resource/saved.jpg"));
		imageView.setImage(null);
		Preview.getChildren().clear();
		
		// Enable the Open menu item
		itmOpen.setDisable(false);
		
		// Disable the Close menu item
		itmClose.setDisable(true);
		
		// Disable the Next button
		btnNext.setDisable(true);
		
		// Disable the Auto Enhance button
		btnAutoEnhance.setDisable(true);
		
		// Disable the Manual Enhance toggle button
		toggleManualEnhance.setDisable(true);
		toggleManualEnhance.setSelected(false);
		
		PreProcessing.setSigmaX(101);
		PreProcessing.setBeta(-50);
	}
	
	@FXML
	private void onClickAutoEnchance() throws IOException {
		new LoadImage(Paths.get("resource/saved.jpg").toFile());
		File file = new File("resource/saved.jpg");
		FileReader fr = null;
		try	
		{
			fr = new FileReader(file);
			Image image = new Image(file.toURI().toString());
		    ImageView iv = new ImageView(image);
		    iv.setFitWidth(300);
		    iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setX(120);
			Preview.getChildren().clear();
			Preview.getChildren().add(iv);
		    
		    
		    if(!iv.isCache())
		    {
				btnNext.setDisable(true);
				System.out.println("File doesn't display");
		    }
		    fr.close();
		}catch(FileNotFoundException e)
		{
			btnNext.setDisable(true);
			System.out.println("File doesn't exist");
		}
	}
	
	@FXML
	private void onClickManualEnchance() throws IOException {
		
		new LoadImage(Paths.get("resource/original.jpg").toFile());
		File file = new File("resource/saved.jpg");
		FileReader fr = null;
		try	
		{
			fr = new FileReader(file);
			Image image = new Image(file.toURI().toString());
		    ImageView iv = new ImageView(image);
		    iv.setFitWidth(300);
		    iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			iv.setX(120);
			Preview.getChildren().clear();
			Preview.getChildren().add(iv);
			
		    if(!iv.isCache())
		    {
				btnNext.setDisable(true);
				System.out.println("File doesn't display");
		    }
		    fr.close();
		}catch(FileNotFoundException e)
		{
			btnNext.setDisable(true);
			System.out.println("File doesn't exist");
		}
		
		PreProcessing.setSigmaX(101);
		PreProcessing.setBeta(-50);
	}
	
	@FXML
	private void onClickToggle() throws IOException {
		
		if(toggleManualEnhance.isSelected()){
			// Disable the Auto Enhance button
			btnAutoEnhance.setDisable(true);
			
			// Disable the Next button
			btnNext.setDisable(true);
			
			// Enable the Sharpness and Brightness slider
			sharpSlider.setDisable(false);
			brightSlider.setDisable(false);
		}
		else{
			// Enable the Auto Enhance button
			btnAutoEnhance.setDisable(false);
			
			// Enable the Next button
			btnNext.setDisable(false);
			
			// Disable the Sharpness and Brightness slider
			sharpSlider.setDisable(true);
			brightSlider.setDisable(true);
		}	    	
	}
	
	@FXML
	private void onClickNext() throws IOException {	
	
		Stage stage; 
	    Parent root;
	    stage=(Stage) btnNext.getScene().getWindow();	    
	    root = FXMLLoader.load(getClass().getResource("GUI_Result.fxml"));
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
    }


	//To display all MenuItems under File when it is clicked
	@FXML
	private void onClickFile() {
		MenuItem itmOpen = new MenuItem("Open");
		MenuItem itmClose = new MenuItem("Close");
		MenuItem itmExit = new MenuItem("Exit");
		file.getItems().addAll(itmOpen, itmClose, itmExit);
		menuBar.getMenus().addAll(file);
	}
	
	//To display MenuItems in Help option ('About' in this case)
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
	
	//When EXIT in File is clicked, the entire application is closed
	@FXML 
	private void onClickExit() throws IOException {
		Files.deleteIfExists(Paths.get("resource/saved.jpg"));
		Files.deleteIfExists(Paths.get("resource/segmented.jpg"));
		Platform.exit();
		System.exit(0);
	}
	
	public File getSelectedfile() {
		return selectedfile;
		
	}
}


