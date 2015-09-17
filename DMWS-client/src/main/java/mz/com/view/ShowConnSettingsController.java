/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mz.com.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jaxb.connections.Connections;
import jaxb.connections.Connections.Datasource;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class ShowConnSettingsController implements Initializable {

     private Stage dialogStage;
     private Datasource dataSrc;
     
     @FXML
     Label lblConName,lblUrl, lblToken; 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    
     /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
     public void setDataSource(Connections.Datasource dataSrc) {

        if (dataSrc != null) {
            this.dataSrc = dataSrc;

            lblConName.setText(this.dataSrc.getName());
            lblUrl.setText(this.dataSrc.getDomainName());
            lblToken.setText(this.dataSrc.getToken());
        }
    }
    
    
    
}
