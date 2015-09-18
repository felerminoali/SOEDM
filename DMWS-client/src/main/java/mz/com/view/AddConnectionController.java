/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import jaxb.connections.Connections;
import mz.com.tasks.remote.TestConnectionTask;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class AddConnectionController implements Initializable {

    private Connections.Datasource dataSrc;
    private Stage dialogStage;
    private boolean okClicked = false;
    private MainGUIController mainApp;

    @FXML
    private TextField txtConName;

    @FXML
    private TextField txtUrl;

    @FXML
    private TextField txtToken;

    @FXML
    private TextArea txtConStatus;

    @FXML
    private ProgressBar progStatus;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//        progStatus.progressProperty().bind(control.progressStatusValue);
//        System.out.println(ProgressBar.INDETERMINATE_PROGRESS);

    }

    @FXML
    public void handleBtnTestConnection() {

        String token = this.txtToken.getText();
        String domainName = this.txtUrl.getText();

        try {
            TestConnectionTask task = new TestConnectionTask(token, domainName);
           
            progStatus.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((w, o, n) -> {
                txtConStatus.clear();
                txtConStatus.appendText(n + "\n");
            });

//             Platform.runLater(task);
            new Thread(task).start();
            
        } catch (Exception e) {
            txtConStatus.setText("Unsuccess!");
        }

    }

    @FXML
    private void handleBtnSave() {
        if (isInputValid()) {

//            String file = "src/datasource.xml";
            String file = "src/main/resources/datasource.xml";
            JAXBContext jaxbContext;
            try {
                jaxbContext = JAXBContext
                        .newInstance(Connections.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File(file));

                dataSrc = new Connections.Datasource();

                dataSrc.setName(txtConName.getText());
                dataSrc.setDomainName(txtUrl.getText());
                dataSrc.setToken(txtToken.getText());

                Connections.Datasource.Remote remote = new Connections.Datasource.Remote();

                remote.getFunction().add("local_miningdata_get_students_data");
                dataSrc.setRemote(remote);

                datasource.getDatasource().add(dataSrc);

                // create JAXB context and initializing Marshaller
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                // for getting nice formatted output
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                //specify the location and name of xml file to be created
                File XMLfile = new File(file);

                // Writing to XML file
                jaxbMarshaller.marshal(datasource, XMLfile);

//                System.out.println(dataSrc.getRemote().getFunction());
                this.mainApp.addTreeConnection(dataSrc);
                okClicked = true;

                dialogStage.close();
            } catch (JAXBException ex) {
//                Logger.getLogger(AddConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                FXOptionPane.showConfirmDialog(dialogStage, ex.getMessage(), "Input Error");
            }
        }
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isSaveClicked() {
        return okClicked;
    }

    public void setDataSource(Connections.Datasource dataSrc) {

        if (dataSrc != null) {
            this.dataSrc = dataSrc;

            txtConName.setText(this.dataSrc.getName());
            txtUrl.setText(this.dataSrc.getDomainName());
            txtToken.setText(this.dataSrc.getToken());
        }
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (txtConName.getText() == null || txtConName.getText().length() == 0) {
            errorMessage += "No valid Connection Name !\n";
        }
        if (txtUrl.getText() == null || txtUrl.getText().length() == 0) {
            errorMessage += "No valid URL!\n";
        }
        if (txtToken.getText() == null || txtToken.getText().length() == 0) {
            errorMessage += "No valid Token!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            FXOptionPane.showConfirmDialog(dialogStage, errorMessage, "Input Error");
            return false;
        }

    }

    public void setMainApp(MainGUIController mainApp) {
        this.mainApp = mainApp;
    }

}
