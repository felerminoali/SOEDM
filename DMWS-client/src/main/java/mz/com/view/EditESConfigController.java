/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import mz.com.expert.AttributeType;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class EditESConfigController implements Initializable {

    private SimpleObjectProperty<AttributeType> root = new SimpleObjectProperty<>();

    @FXML
    private TextField txtName, txtCreator, txtConfIndex, txtConclusion;

    @FXML
    private TextArea txtStartText, txtDesc;

    private Stage dialogStage;
    private boolean okClicked = false;
    private ESformController mainApp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(ESformController mainApp) {
        this.mainApp = mainApp;
    }

    public void setRoot(AttributeType root) {
        this.root.set(root);

        txtName.setText((root.getName() != null) ? root.getName() : "");
        txtCreator.setText((root.getCreator() != null) ? root.getCreator() : "");
        txtConfIndex.setText((root.getConfidence() != null) ? root.getConfidence() : "");
        txtConclusion.setText((root.getConclusion() != null) ? root.getConclusion() : "");
        txtStartText.setText((root.getStartText() != null) ? root.getStartText() : "");
        txtDesc.setText((root.getDescription() != null) ? root.getDescription() : "");
    }
    
    
    @FXML
    public void handleBtnSave(){
    
     String file = "src/expert.xml";
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext
                    .newInstance(AttributeType.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AttributeType rootFile = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

           
            rootFile.setName(txtName.getText());
            rootFile.setCreator(txtCreator.getText());
            rootFile.setStartText(txtStartText.getText());
            rootFile.setConclusion(txtConclusion.getText());
            rootFile.setConfidence(txtConfIndex.getText());
            rootFile.setDescription(txtDesc.getText());
            
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File XMLfile = new File(file);
            jaxbMarshaller.marshal(rootFile, XMLfile);
            jaxbMarshaller.marshal(rootFile, System.out);

            this.mainApp.setTree();

            okClicked = true;

            dialogStage.close();

        } catch (JAXBException e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }
    
    }

}
