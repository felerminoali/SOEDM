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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import mz.com.model.OptionsObject;

/**
 *
 * @author Lenovo
 */
public class ClassifierOptJ48 extends ChooserPanel implements OptionHandle {

    private String classifier = "weka.classifiers.trees.J48";
    private final String about = "Class for generating a pruned or unpruned C4.";
    private String options[];
    private MainGUIController mguic;

    private ComboBox cboUseLaplace, cboUnpruned, cboSubtreeRaising, cboBinarySplits, cboDebug, cboReducedErrorPruning, cboSaveInstanceData;

    private TextField txtSeed, txtConfidenceFactor, txtMinNumObj, txtNumFolds;

    private Button btnApply;

    public ClassifierOptJ48(MainGUIController mguic, String classifier) {
        this.mguic = mguic;
        this.classifier = classifier;
        init();
    }

    public ClassifierOptJ48(MainGUIController mguic, String classifier, String options[]) {
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
        Label binarySplits = new Label("binarySplits");
        GridPane.setConstraints(binarySplits, 0, 0);

        this.cboBinarySplits = new ComboBox<>();
        GridPane.setConstraints(cboBinarySplits, 1, 0);

        Label confidenceFactor = new Label("confidenceFactor");
        GridPane.setConstraints(confidenceFactor, 0, 1);

        this.txtConfidenceFactor = new TextField();
        GridPane.setConstraints(txtConfidenceFactor, 1, 1);

        Label debug = new Label("debug");
        GridPane.setConstraints(debug, 0, 2);

        this.cboDebug = new ComboBox<>();
        GridPane.setConstraints(cboDebug, 1, 2);

        Label minNumObj = new Label("minNumObj");
        GridPane.setConstraints(minNumObj, 0, 3);

        this.txtMinNumObj = new TextField();
        GridPane.setConstraints(txtMinNumObj, 1, 3);

        Label numFolds = new Label("numFolds");
        GridPane.setConstraints(numFolds, 0, 4);

        this.txtNumFolds = new TextField();
        GridPane.setConstraints(txtNumFolds, 1, 4);

        Label reducedErrorPruning = new Label("reducedErrorPruning");
        GridPane.setConstraints(reducedErrorPruning, 0, 5);

        this.cboReducedErrorPruning = new ComboBox<>();
        GridPane.setConstraints(cboReducedErrorPruning, 1, 5);

        Label saveInstanceData = new Label("saveInstanceData");
        GridPane.setConstraints(saveInstanceData, 0, 6);

        this.cboSaveInstanceData = new ComboBox<>();
        GridPane.setConstraints(cboSaveInstanceData, 1, 6);

        Label seed = new Label("seed");
        GridPane.setConstraints(seed, 0, 7);

        this.txtSeed = new TextField();
        GridPane.setConstraints(txtSeed, 1, 7);

        Label subtreeRaising = new Label("subtreeRaising");
        GridPane.setConstraints(subtreeRaising, 0, 8);

        this.cboSubtreeRaising = new ComboBox<>();
        GridPane.setConstraints(cboSubtreeRaising, 1, 8);

        Label unpruned = new Label("unpruned");
        GridPane.setConstraints(unpruned, 0, 9);

        this.cboUnpruned = new ComboBox<>();
        GridPane.setConstraints(cboUnpruned, 1, 9);

        Label useLaplace = new Label("useLaplace");
        GridPane.setConstraints(useLaplace, 0, 10);

        this.cboUseLaplace = new ComboBox<>();
        GridPane.setConstraints(cboUseLaplace, 1, 10);

        btnApply = new Button("Apply");
        btnApply.setOnAction(e -> {

            try {
                String opt = getOptions();
                this.mguic.txtClassifier.setText(opt);

            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(this.mguic.getMainApp().getPrimaryStage(), ex.getMessage(), "Error");
            }

        });

        GridPane.setConstraints(btnApply, 1, 11);

        grid.getChildren().addAll(
                binarySplits,
                cboBinarySplits,
                confidenceFactor,
                txtConfidenceFactor,
                debug,
                cboDebug,
                minNumObj,
                txtMinNumObj,
                numFolds,
                txtNumFolds,
                reducedErrorPruning,
                cboReducedErrorPruning,
                saveInstanceData,
                cboSaveInstanceData,
                seed,
                txtSeed,
                subtreeRaising,
                cboSubtreeRaising,
                unpruned,
                cboUnpruned,
                useLaplace,
                cboUseLaplace,
                btnApply
        );

        setOptions();

        this.getChildren().addAll(vbox, grid);

    }

    @Override
    public String getOptions() {

        String scheme = classifier;

//        scheme += " " + getComboOpts(cboUnpruned);
        scheme += " " + getComboOpts(cboUseLaplace);
        scheme += " " + getComboOpts(cboBinarySplits);
        scheme += " " + getComboOpts(cboSubtreeRaising);
        scheme += " " + getComboOpts(cboDebug);
        scheme += " " + getComboOpts(cboReducedErrorPruning);
        scheme += " " + getComboOpts(cboSaveInstanceData);
        
        if(getComboOpts(cboUnpruned).equals("-C")){
            scheme += " -C " + txtConfidenceFactor.getText();
        }else{
            scheme += " -U ";
        }
        
        scheme += " -M " + txtMinNumObj.getText();

        return scheme;
    }

    
    public String getComboOpts(ComboBox combo) {

        String opt = "";

        OptionsObject value = (OptionsObject) combo.getSelectionModel().getSelectedItem();

        if (value.toString().equals("True")) {
            opt = value.getValue();
        }

        return opt;
    }

    @Override
    public void setOptions() {

//        txtSeed, , , txtNumFolds;
        fillTextField(txtMinNumObj, "-M");
        fillTextField(txtConfidenceFactor, "-C");

        fillCombo(cboDebug, "-D", "");
        fillCombo(cboUseLaplace, "-A", "");
        fillCombo(cboUnpruned, "-U", "-C");
        fillCombo(cboSubtreeRaising, "", "-S");
        fillCombo(cboBinarySplits, "-B", "");
        fillCombo(cboReducedErrorPruning, "-R", "");
//        fillCombo(cboSaveInstanceData, "-R -N 3 -Q 1", "");
        fillCombo(cboSaveInstanceData, "-R", "");

    }

    private void fillTextField(TextField txtField, String opt) {

        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(opt)) {
                    txtField.setText(options[i + 1]);
                    break;
                }
            }

        }
    }

    private void fillCombo(ComboBox combo, String opT, String opF) {

        OptionsObject optFalse = new OptionsObject(opF);
        OptionsObject optTrue = new OptionsObject(opT);

        List<OptionsObject> cbFindList = new ArrayList<>();
        cbFindList.add(optFalse);
        cbFindList.add(optTrue);

        combo.getItems().addAll(cbFindList);

        combo.getSelectionModel().select(optFalse);

        if (options != null) {

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(opT)) {
                    combo.getSelectionModel().select(optTrue);
                    break;
                }
            }
        }

    }

}
