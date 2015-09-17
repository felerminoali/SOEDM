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
import javafx.scene.control.Button;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import mz.com.expert.AttributeType;

/**
 *
 * @author Lenovo
 */
public class AddRecomendationController implements Initializable {

//    private AttributeType conclusion;
    private SimpleObjectProperty<AttributeType> conclusion = new SimpleObjectProperty<>();

    @FXML
    private Button btnSave;

    @FXML
    private HTMLEditor hEditr;

    private Stage dialogStage;
    private boolean okClicked = false;
    private ESformController mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleBtnSave() {

        String file = "src/expert.xml";
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext
                    .newInstance(AttributeType.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AttributeType root = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

            editNode(root, conclusion.get());

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File XMLfile = new File(file);
            jaxbMarshaller.marshal(root, XMLfile);
            jaxbMarshaller.marshal(root, System.out);

            this.mainApp.setTree();

            okClicked = true;

            dialogStage.close();

        } catch (JAXBException e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(ESformController mainApp) {
        this.mainApp = mainApp;
    }

    public void setConclusion(AttributeType conclusion) {
        this.conclusion.set(conclusion);

        if (conclusion.getRecommendation() != null) {
            if (!conclusion.getRecommendation().isEmpty()) {
                hEditr.setHtmlText(conclusion.getRecommendation());
            }
        }

    }

    private void editNode(AttributeType root, AttributeType conclusion) {

        for (AttributeType child : root.getAttribute()) {

            if (child.getId().equals(conclusion.getId())) {
                child.setRecommendation(hEditr.getHtmlText());
                break;
            }
            editNode(child, conclusion);
        }
    }

}
