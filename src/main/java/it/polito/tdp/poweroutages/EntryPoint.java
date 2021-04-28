package it.polito.tdp.poweroutages;

import javafx.application.Application;

import it.polito.tdp.poweroutages.model.PowerOutagesModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class EntryPoint extends Application 
{
    @Override
    public void start(Stage stage) throws Exception 
    {	    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene_lab07.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        FXMLController controller = loader.getController();
        // TODO impostare il model nel controller   
        PowerOutagesModel model = new PowerOutagesModel();
        controller.setModel(model);
                
        stage.setTitle("Lab07 - Power outages");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }

}
