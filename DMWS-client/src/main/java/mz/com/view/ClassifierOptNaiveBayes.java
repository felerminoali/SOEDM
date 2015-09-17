/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import mz.com.model.OptionsObject;

/**
 *
 * @author Lenovo
 */
public class ClassifierOptNaiveBayes extends ChooserPanel implements OptionHandle {

    private String classifier = "weka.classifiers.bayes.NaiveBayes";
    private final String about = "Class for a Naive Bayes classifier using estimator classes.";
    private String options[];
    private MainGUIController mguic;

    private ComboBox<Object> cboDisplayModelInOldFormat, cboUseKernelEstimator;
    private Button btnApply;

    public ClassifierOptNaiveBayes(MainGUIController mguic, String classifier) {
        this.mguic = mguic;
        this.classifier = classifier;
        init();
    }

    public ClassifierOptNaiveBayes(MainGUIController mguic, String classifier, String options[]) {
        this.options = options;
        this.classifier = classifier;
        this.mguic = mguic;
        init();
    }

    @Override
    public void init() {

        // show classifier
        Label lblFilter = new Label(this.classifier);
        Label lbAbout = new Label(this.about);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(lblFilter, lbAbout);

        // classifier config
        GridPane grid = new GridPane();

        GridPane.setMargin(grid, new Insets(0, 10, 10, 10));
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Name lablel
        Label lblDisplayModelInOldFormat = new Label("displayModelInOldFormat");
        GridPane.setConstraints(lblDisplayModelInOldFormat, 0, 0);

        this.cboDisplayModelInOldFormat = new ComboBox<>();
        GridPane.setConstraints(cboDisplayModelInOldFormat, 1, 0);

        Label lblUseKernelEstimator = new Label("useKernelEstimator");
        GridPane.setConstraints(lblUseKernelEstimator, 0, 1);

        this.cboUseKernelEstimator = new ComboBox<>();
        GridPane.setConstraints(cboUseKernelEstimator, 1, 1);

        btnApply = new Button("Apply");
        btnApply.setOnAction(e -> {

            try {
                String opt = getOptions();
                this.mguic.txtClassifier.setText(opt);

            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(this.mguic.getMainApp().getPrimaryStage(), ex.getMessage(), "Error");
            }

        });

        GridPane.setConstraints(btnApply, 1, 2);

        grid.getChildren().addAll(
                lblDisplayModelInOldFormat,
                cboDisplayModelInOldFormat,
                lblUseKernelEstimator,
                cboUseKernelEstimator,
                btnApply
        );

        setOptions();

        this.getChildren().addAll(vbox, grid);
    }

    @Override
    public String getOptions() {

        String scheme = classifier;

        OptionsObject displayModelInOldFormat = (OptionsObject) cboDisplayModelInOldFormat.getSelectionModel().getSelectedItem();

        if (displayModelInOldFormat.toString().equals("True")) {
            scheme += " " + displayModelInOldFormat.getValue();
        }

        OptionsObject useKernelEstimator = (OptionsObject) cboUseKernelEstimator.getSelectionModel().getSelectedItem();

        if (useKernelEstimator.toString().equals("True")) {
            scheme += " " + useKernelEstimator.getValue();
        }

        return scheme;
    }

    @Override
    public void setOptions() {

        OptionsObject optFalse = new OptionsObject();
        OptionsObject optTrue = new OptionsObject("-O");

        List<OptionsObject> cbFindList = new ArrayList<>();
        cbFindList.add(optFalse);
        cbFindList.add(optTrue);

        cboDisplayModelInOldFormat.getItems().addAll(cbFindList);

        cboDisplayModelInOldFormat.getSelectionModel().select(optFalse);

        // cboUseKernelEstimator 
        OptionsObject optIgFalse = new OptionsObject();
        OptionsObject optIgTrue = new OptionsObject("-K");

        List<OptionsObject> cbIgnList = new ArrayList<>();
        cbIgnList.add(optIgFalse);
        cbIgnList.add(optIgTrue);

        cboUseKernelEstimator.getItems().addAll(cbIgnList);

        cboUseKernelEstimator.getSelectionModel().select(optIgFalse);

        if (options != null) {
            // cboDisplayModelInOldFormat

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-O")) {
                    cboDisplayModelInOldFormat.getSelectionModel().select(optTrue);
                    break;
                }
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-K")) {
                    cboUseKernelEstimator.getSelectionModel().select(optIgTrue);
                    break;
                }
            }
        }
    }
}
