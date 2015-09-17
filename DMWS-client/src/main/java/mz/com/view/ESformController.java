/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import mz.com.expert.AttributeType;
import mz.com.model.Topic;
import mz.com.model.Value;
import mz.com.myabstract.CallbackCustomES;
import mz.com.tasks.local.PreviewTask;
import mz.com.tasks.local.PublishTask;
import mz.com.tasks.remote.VisualizeTreeTask;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class ESformController implements Initializable {

    @FXML
    TextArea txtTree;

    @FXML
    private TreeView<Object> tree;

    @FXML
    Button btnPublish, btnPreview;

    @FXML
    private ProgressBar graphProgress;

    @FXML
    private Label lblProgress;

    private String data, classifier, evaluation;

    Stage dialogStage;

    public final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("images/R.png")));
    public final Node attributeIcon = new ImageView(new Image(getClass().getResourceAsStream("images/A.png")));
    public final Node valueIcon = new ImageView(new Image(getClass().getResourceAsStream("images/V.png")));
    public final Node conclusionIcon = new ImageView(new Image(getClass().getResourceAsStream("images/C.png")));

    private int index = 1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        txtTree.textProperty().addListener(e -> {
            System.out.println(txtTree.getText());
        });

        setTree();

        btnPublish.setOnAction(event -> {

            try {

//                String file = "src/expert.xml";
                String file = "src/main/resources/expert.xml";

                JAXBContext jaxbContext = JAXBContext
                        .newInstance(AttributeType.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                AttributeType root = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

                publish(root);

            } catch (JAXBException ex) {
                FXOptionPane.showConfirmDialog(dialogStage, ex.getMessage(), "Error");
            }

        });

        btnPreview.setOnAction(event -> {

            try {
                PreviewTask task = new PreviewTask();

                graphProgress.progressProperty().bind(task.progressProperty());
                task.messageProperty().addListener((w, o, n) -> {
                    lblProgress.setText(n + "\n");
                });

                new Thread(task).start();
            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(dialogStage, ex.getMessage(), "Error");
            }

        });

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

    private void graphMLToXML() {

    }

    public void setTree() {

        try {

//            String file = "src/expert.xml";
             String file = "src/main/resources/expert.xml";

            JAXBContext jaxbContext = JAXBContext
                    .newInstance(AttributeType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AttributeType rootA = (AttributeType) jaxbUnmarshaller.unmarshal(new File(file));

            TreeItem<Object> root = new TreeItem<>(rootA, rootIcon);

            System.out.println(rootA.getDescription());
            root.setExpanded(true);

            createNodes(rootA, root);

            tree.setRoot(root);

            // defines a custom tree cell factory for the tree view
            tree.setEditable(true);
            tree.setCellFactory(new CallbackCustomES(this));

            tree.setShowRoot(true);

        } catch (JAXBException ex) {
            FXOptionPane.showConfirmDialog(dialogStage, ex.getMessage(), "Error");
        }

    }

    private TreeItem<Object> makeBranch(Object node, TreeItem<Object> parent) {

        Node icon;
        if (((AttributeType) node).getType().equals("V")) {
            icon = valueIcon;
        } else if (((AttributeType) node).getType().equals("C")) {
            icon = conclusionIcon;
        } else {
            icon = attributeIcon;
        }
        TreeItem<Object> item = new TreeItem<>(node, icon);

        item.setExpanded(false);
        parent.getChildren().add(item);

        return item;
    }

    private void createNodes(AttributeType nodeChild, TreeItem<Object> rootParent) {

        for (AttributeType newAttr : nodeChild.getAttribute()) {
            TreeItem<Object> nodeChildChild = makeBranch(newAttr, rootParent);
            createNodes(newAttr, nodeChildChild);
        }
    }

    public void publish(AttributeType rootObj) {

        try {
            PublishTask task = new PublishTask(rootObj);

            graphProgress.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((w, o, n) -> {
                lblProgress.setText(n + "\n");
            });

            new Thread(task).start();
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(dialogStage, ex.getMessage(), "Error");
        }
    }

}
