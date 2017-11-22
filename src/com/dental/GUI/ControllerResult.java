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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.dental.ClusterAlgorithm.ClusteringAlgorithm;
import com.dental.Result.Result;
import com.dental.Result.Table;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerResult {
	
	@FXML
	private Button btnResult = new Button();
	
	@FXML
	private Button btnBack = new Button();
	
	@FXML
	private Button btnShowImage = new Button();
	
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
	private TextArea totalPixel = new TextArea();
	
	@FXML
	private TextArea totalArea = new TextArea();
	
	@FXML
	private LineChart<Integer, Integer> lineChart;
	
	@FXML
	private NumberAxis intensity = new NumberAxis();
	
	@FXML
	private TableView<Table> table;
	
	@FXML
	private TableColumn<Table, Integer> colPercentage;

    @FXML
    private TableColumn<Table, Integer> colPixel;
    
    @FXML
    private ComboBox<String> comboCluster;
	
	private HashMap<Integer, Integer> graph;
	
	private final IntegerProperty state = new SimpleIntegerProperty();
	
	ObservableList<String> clusteringAlgorithm = FXCollections.observableArrayList("K-Means", "Mean Shift");
	
	@FXML
	private void initialize() {
		// Set the list of clustering algorithm for Combo Box
		comboCluster.setItems(clusteringAlgorithm);
		
		// Set an integer value to the clustering algorithm
		state.bind(Bindings.when(comboCluster.valueProperty().isEqualTo("K-Means")).then(1).otherwise(2));
		
		// Disable the Show Image button
		btnShowImage.setDisable(true);
		
		// Disable the Show Result button
		btnResult.setDisable(true);
	}
	
	@FXML
	private void onClickCombo(){
		
		// Check the clustering algorithm is chosen or not
		if(comboCluster.getValue() != null){
			// Enable the Show Result button
			btnResult.setDisable(false);
		}
		comboCluster.valueProperty().addListener(new ChangeListener<String>(){
			 @SuppressWarnings("rawtypes")
			@Override 
			public void changed(ObservableValue ov, String t, String t1) {
		          if(comboCluster.valueProperty().isEqualTo("K-Means").toString() != t1){
		        	  btnShowImage.setDisable(true);
		          }
		        }  
		});
	}
	
	@FXML
	private void onClickResults() throws IOException {	
		new ClusteringAlgorithm(state.get());
		Result newResult = new Result();
		if(newResult.getPixelCalculate().isFlag_NoPink() == false){
			totalPixel.setText(Integer.toString((int)newResult.getTotalPixel()));
			totalArea.setText(String.format("%.2f", newResult.getTotalArea()));
			graph = newResult.getPixelCalculate().getHashMap_Data();
			
			// Show the result on graph
			plotGraph();
			
			// Show the result on table
			setTable();
			
			// Enable the Show Image button
			btnShowImage.setDisable(false);
		}
		else if(newResult.getPixelCalculate().isFlag_NoPink() == true){
			// Alert box appears if no pink/magenta found 
	    	Alert alert = new Alert(AlertType.INFORMATION);   
	    	DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    	alert.setTitle("Information");
	    	alert.setHeaderText("No Pink or Magenta Pixel");
	    	alert.setContentText("Image uploaded does not contain either pink or magenta pixel.\nPlease upload a different image.");
	    	alert.showAndWait();
		}
	}
		
	// Show result on graph
	@SuppressWarnings("rawtypes")
	public void plotGraph(){
		
		// Clear graph
		lineChart.getData().clear();
		
		// Set x axis properties
		intensity.setAutoRanging(false);
		intensity.setLowerBound(40);
		intensity.setUpperBound(100);
		intensity.setTickUnit(5);
		
		// Add data to line chart
		XYChart.Series<Integer, Integer> series = new XYChart.Series<Integer, Integer>();
		Set set = graph.entrySet();
	    Iterator i = set.iterator();
	    
	    while(i.hasNext()) {
	        Map.Entry me = (Map.Entry)i.next();
	        series.getData().add(new XYChart.Data<Integer, Integer>((int)me.getKey(),(int)me.getValue()));
	    }
	    
	    series.setName(comboCluster.getValue());
	    
	    // Display line chart
		lineChart.getData().add(series);
		
	}
	
	// Show the result on table
	@SuppressWarnings("rawtypes")
	private void setTable(){
		
		colPercentage.setCellValueFactory(new PropertyValueFactory<Table, Integer>("percentage"));
		colPixel.setCellValueFactory(new PropertyValueFactory<Table, Integer>("pixelCount"));
		Set set = graph.entrySet();
	    Iterator i = set.iterator();
	    ObservableList<Table> data = FXCollections.observableArrayList();
	    
	    // Add data to the table
	    while(i.hasNext()) {
	        Map.Entry me = (Map.Entry)i.next();
	        data.add(new Table((int)me.getKey(), (int)me.getValue()));
	     }
	    
	    // Display table
		table.setItems(data);
	}
	
	@FXML
	private void onClickPopup() throws IOException {	
		
	    Parent root;	    
	    root = FXMLLoader.load(getClass().getResource("GUI_ShowImage.fxml"));
	    Stage stage = new Stage(); 
	    Scene scene = new Scene(root);
	    stage.setTitle("Segmented Image");
	    stage.initStyle(StageStyle.UNDECORATED);
	    stage.setScene(scene);
	    stage.show();
	}
	
	@FXML
	private void onClickBack() throws IOException {
		
		Stage stage; 
	    Parent root;
	    stage=(Stage)btnBack.getScene().getWindow();
	    root = FXMLLoader.load(getClass().getResource("GUI_Image.fxml"));
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}
	
	@FXML
	private void onClickExit() throws IOException{
		Files.deleteIfExists(Paths.get("resource/saved.jpg"));
		Files.deleteIfExists(Paths.get("resource/segmented.jpg"));
		Platform.exit();
		System.exit(0);
	}
	
	// To display all MenuItems under File when it is clicked
	@FXML
	private void onClickFile() {
		MenuItem itmExit = new MenuItem("Exit");
		file.getItems().addAll(itmExit);
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
    			+ "Copyright (c) 2017 Group 2 UNMC.\n "
    			+ "All rights reserved.\n\n"
    			+ "This software is made possible by OpenCV and Scene Builder.\n");
    	alert.showAndWait();
	}
	
	// When Exit in File is clicked, the entire application is closed
	@FXML
	private void onClickMenuExit(){
		Platform.exit();
	}
	
}
