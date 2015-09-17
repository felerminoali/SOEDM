/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import mz.com.tasks.remote.VisualizeTreeTask;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class VisualizeTreeController implements Initializable {

    @FXML
    TextArea txtTree;

    @FXML
    private ProgressBar graphProgress;

    private String data, classifier, evaluation;

    Stage dialogStage;

  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public void setTxtTree(String txt) {
        txtTree.setText(txt);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public void visualize() {

        VisualizeTreeTask task;

        if (evaluation.isEmpty()) {
            task = new VisualizeTreeTask(data, classifier);
        } else {
            task = new VisualizeTreeTask(data, classifier, evaluation);
        }

        this.graphProgress.progressProperty().bind(task.progressProperty());

        task.messageProperty().addListener((w, o, n) -> {
            txtTree.clear();
            txtTree.appendText(n + "\n");
        });

        new Thread(task).start();
    }

    
      
}
