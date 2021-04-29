
package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.PowerOutagesModel;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;

public class FXMLController 
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Nerc> NercComboBox;

    @FXML
    private TextField maxYearsTextField;

    @FXML
    private TextField maxHoursTextField;

    @FXML
    private Button worstCaseAnalysisButton;

    @FXML
    private TextArea resultTextArea;
    
	private PowerOutagesModel model;
	private final String NO_RESULT_FOUND_ERROR = "There are no power outages matching this criteria!";

    @FXML
    void doWorstCaseAnalysis(ActionEvent event) 
    {    	
    	Nerc selectedNerc = this.NercComboBox.getValue();
    	String maxHoursInput = this.maxHoursTextField.getText();
    	String maxYearsInput = this.maxYearsTextField.getText();
    	
    	if(selectedNerc == null || maxHoursInput == null || maxYearsInput == null)
    		throw new RuntimeException("Error in doWorstCaseAnalysis()");
    	
    	int maxHours, maxYears;
    	try
		{
			maxYears = Integer.parseInt(maxYearsInput);
			maxHours = Integer.parseInt(maxHoursInput);
		}
		catch(NumberFormatException nfe)
		{
			this.resultTextArea.setText("Error: wrong input format exception");
			return;
		}
    	
    	long init = System.currentTimeMillis();
    	List<PowerOutage> worstCasePowerOutages = this.model.computeWorstCase(selectedNerc, maxYears, maxHours);
    	long fin = System.currentTimeMillis();

    	if(worstCasePowerOutages.isEmpty())
    	{
    		this.resultTextArea.setText(NO_RESULT_FOUND_ERROR);
    		return;
    	}
    	
    	int totPeopleAffected = this.model.totalCustomersAffected(worstCasePowerOutages);
    	long totalHoursOfOutage = this.model.totHoursAffected(worstCasePowerOutages);
    	
    	StringBuilder output = new StringBuilder();
    	output.append("time: ").append(fin-init).append("ms\n");
    	output.append("Total people affected: ").append(totPeopleAffected).append("\n");
    	output.append("Total hours of outage: ").append(totalHoursOfOutage).append("\n\n");
    	output.append(String.format("   %-5s %-5s %-20s %-20s %-4s %-4s %-8s\n",
    						"id", "year", "event begin", "event finished", "hs", "min", "people"));
    	output.append(this.printPowerOutages(worstCasePowerOutages));
    	
    	this.resultTextArea.setText(output.toString());
    }
    
    private String printPowerOutages(Collection<PowerOutage> powerOutages)
    {
    	StringBuilder sb = new StringBuilder();
    	int count = 0;
    	
    	for(PowerOutage po : powerOutages)
    	{
    		count++;
    		
    		if(sb.length() > 0)
    			sb.append("\n");
    		
    		sb.append(count).append(") ").append(po.toFormattedString());
    	}
    	
    	return sb.toString();
    }

    @FXML
    void handleMaxHoursTyped(KeyEvent event) 
    {
    	this.checkEmptyFields();
    }

    @FXML
    void handleMaxYearsTyped(KeyEvent event) 
    {
    	this.checkEmptyFields();
    }

    @FXML
    void handleNercSelected(ActionEvent event) 
    {
    	Nerc selectedNerc = this.NercComboBox.getValue();
    	if(selectedNerc != null)
    	{
    		this.maxYearsTextField.setDisable(false);
    		this.maxHoursTextField.setDisable(false);
    	}
    	else
    	{
    		this.maxYearsTextField.setDisable(true);
    		this.maxHoursTextField.setDisable(true);
    	}
    	
    	this.checkEmptyFields();
    }
    
    private void checkEmptyFields()
    {
    	Nerc selectedNerc = this.NercComboBox.getValue();
    	String maxYearsInput = this.maxYearsTextField.getText();
    	String maxHoursInput = this.maxHoursTextField.getText();
    	
    	if(selectedNerc != null && !maxYearsInput.isBlank() && !maxHoursInput.isBlank())
    		this.worstCaseAnalysisButton.setDisable(false);
    	else
    		this.worstCaseAnalysisButton.setDisable(true);
    }

    @FXML
    void initialize() 
    {
        assert NercComboBox != null : "fx:id=\"NercComboBox\" was not injected: check your FXML file 'Scene_lab07.fxml'.";
        assert maxYearsTextField != null : "fx:id=\"maxYearsTextField\" was not injected: check your FXML file 'Scene_lab07.fxml'.";
        assert maxHoursTextField != null : "fx:id=\"maxHoursTextField\" was not injected: check your FXML file 'Scene_lab07.fxml'.";
        assert worstCaseAnalysisButton != null : "fx:id=\"worstCaseAnalysisButton\" was not injected: check your FXML file 'Scene_lab07.fxml'.";
        assert resultTextArea != null : "fx:id=\"resultTextArea\" was not injected: check your FXML file 'Scene_lab07.fxml'.";

        // Utilizzare questo font per incolonnare correttamente i dati;
        this.resultTextArea.setStyle("-fx-font-family: monospace");
        
        this.maxYearsTextField.setTextFormatter(new TextFormatter<>(change -> 
        {
        	if(this.maxYearsTextField.getText().length() >= 3)
        		change.setText("");
        	else
        	{
	        	String text = change.getText();
	        	
	        	if(text.length() > 3)
	        		text = text.substring(0,3);
	        	
	        	if(text.matches("(.)*\\D(.)*"))
	        		text = text.replaceAll("\\D", "");
	        	
	        	change.setText(text);
        	}
        	return change;
        }));
        
        this.maxHoursTextField.setTextFormatter(new TextFormatter<Double>(change -> 
        {
        	if(this.maxHoursTextField.getText().length() >= 5)
        		change.setText("");
        	else
        	{        		
	        	String text = change.getText();
	        	
	        	if(text.length() > 5)
	        		text = text.substring(0,5);
	        	
	        	if(text.matches("(.)*\\D(.)*"))
	        		text = text.replaceAll("\\D", "");
	        	
	        	change.setText(text);
        	}
        	return change;
        }));
        
    }
    
    public void setModel(PowerOutagesModel newModel) 
    {
    	this.model = newModel;
    	this.NercComboBox.getItems().addAll(this.model.getAllNercs());
    }
}

