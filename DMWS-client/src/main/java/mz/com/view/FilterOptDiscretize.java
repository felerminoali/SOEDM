/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class FilterOptDiscretize extends ChooserPanel implements OptionHandle {

    private String filter = "weka.filters.unsupervised.attribute.Discretize";
    private final String about = "An instance filter that discretizes a range of numeric attributes in the dataset into \nnominal attributes.";
    private String options[];
    private MainGUIController mguic;

    // fields
    TextField txtAttributesIndices, txtBins, txtDised;

    ComboBox<Object> cboFindNumBins, cbIgnoreClass, cbMakeBinary, cbEqualFreq;
    Button btnApply;

    public FilterOptDiscretize(MainGUIController mguic, String filter) {
        this.filter = filter;
        init();
    }

    public FilterOptDiscretize(MainGUIController mguic, String filter, String options[]) {
        this.options = options;
        this.filter = filter;
        this.mguic = mguic;
        init();
    }

    @Override
    public void init() {

        // show filter
        Label lblFilter = new Label(this.filter);
        Label lbAbout = new Label(this.about);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(lblFilter, lbAbout);

        // filter config
        GridPane grid = new GridPane();

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(8);

        // Name lablel
        Label attributesIndices = new Label("attributesIndices");
        GridPane.setConstraints(attributesIndices, 0, 0);

        //
        txtAttributesIndices = new TextField();
//        txtAttributesIndices.
        GridPane.setConstraints(txtAttributesIndices, 1, 0);

        // Name lablel
        Label bins = new Label("bins");
        GridPane.setConstraints(bins, 0, 1);

        txtBins = new TextField();
        GridPane.setConstraints(txtBins, 1, 1);

        // Name lablel
        Label diserdWeightOfInstancesPerInterval = new Label("diserdWeightOfInstancesPerInterval");
        GridPane.setConstraints(diserdWeightOfInstancesPerInterval, 0, 2);

        txtDised = new TextField();
        GridPane.setConstraints(txtDised, 1, 2);

        // Name lablel
        Label findNumbBins = new Label("findNumbBins");
        GridPane.setConstraints(findNumbBins, 0, 3);

        cboFindNumBins = new ComboBox();
        GridPane.setConstraints(cboFindNumBins, 1, 3);

        // Name lablel
        Label ignoreClass = new Label("ignireClass");
        GridPane.setConstraints(ignoreClass, 0, 4);

        cbIgnoreClass = new ComboBox();
        GridPane.setConstraints(cbIgnoreClass, 1, 4);

        // Name lablel
        Label makeBinary = new Label("makeBinary");
        GridPane.setConstraints(makeBinary, 0, 5);

        cbMakeBinary = new ComboBox();
        GridPane.setConstraints(cbMakeBinary, 1, 5);

        // Name lablel
        Label useEqualFrequency = new Label("useEqualFrequency");
        GridPane.setConstraints(useEqualFrequency, 0, 6);

        cbEqualFreq = new ComboBox();
        GridPane.setConstraints(cbEqualFreq, 1, 6);

        btnApply = new Button("Apply");
        btnApply.setOnAction(e -> {

            try {
                String opt = getOptions();
                this.mguic.txtFilter.setText(opt);

            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(this.mguic.getMainApp().getPrimaryStage(), ex.getMessage(), "Error");
            }

        });

        GridPane.setConstraints(btnApply, 1, 7);
//        grid.getChildren().addAll(nameLabel, nameTxtField, passLabel, passTxtField, btn);
        grid.getChildren().addAll(
                attributesIndices,
                txtAttributesIndices,
                bins,
                txtBins,
                diserdWeightOfInstancesPerInterval,
                txtDised,
                findNumbBins,
                cboFindNumBins,
                ignoreClass,
                cbIgnoreClass,
                makeBinary,
                cbMakeBinary,
                useEqualFrequency,
                cbEqualFreq,
                btnApply
        );

        setOptions();

        this.getChildren().addAll(vbox, grid);
    }

    @Override
    public String getOptions() {

        String scheme = filter;

        OptionsObject findNumB = (OptionsObject) cboFindNumBins.getSelectionModel().getSelectedItem();

        if (findNumB.toString().equals("True")) {
            scheme += " " + findNumB.getValue();
        }

        OptionsObject ignoreClass = (OptionsObject) cbIgnoreClass.getSelectionModel().getSelectedItem();

        if (ignoreClass.toString().equals("True")) {
            scheme += " " + ignoreClass.getValue();
        }

        OptionsObject makeBinary = (OptionsObject) cbMakeBinary.getSelectionModel().getSelectedItem();

        if (makeBinary.toString().equals("True")) {
            scheme += " " + makeBinary.getValue();
        }

        OptionsObject equalFreq = (OptionsObject) cbEqualFreq.getSelectionModel().getSelectedItem();

        if (equalFreq.toString().equals("True")) {
            scheme += " " + equalFreq.getValue();
        }

        scheme += " -B " + txtBins.getText();
        scheme += " -M " + txtDised.getText();
        scheme += " -R " + txtAttributesIndices.getText();

        return scheme;
//        OptionsObject ignoreClass = (OptionsObject) ((AbstractObjectsList) cbIgnoreClass.getModel()).getElementAt(cbIgnoreClass.getSelectedIndex());
    }

    @Override
    public void setOptions() {

        OptionsObject optFalse = new OptionsObject();
        OptionsObject optTrue = new OptionsObject("-O");

        List<OptionsObject> cbFindList = new ArrayList<>();
        cbFindList.add(optFalse);
        cbFindList.add(optTrue);

        cboFindNumBins.getItems().addAll(cbFindList);

        OptionsObject optIgFalse = new OptionsObject();
        OptionsObject optIgTrue = new OptionsObject("-unset-class-temporarily");

        List<OptionsObject> cbIgnList = new ArrayList<>();
        cbIgnList.add(optIgFalse);
        cbIgnList.add(optIgTrue);

        cbIgnoreClass.getItems().addAll(cbIgnList);

        cbIgnoreClass.getSelectionModel().select(optIgFalse);

        OptionsObject optMbFalse = new OptionsObject();
        OptionsObject optMbTrue = new OptionsObject("-D");

        List<OptionsObject> cbMbList = new ArrayList<>();
        cbMbList.add(optMbFalse);
        cbMbList.add(optMbTrue);

        cbMakeBinary.getItems().addAll(cbMbList);

        cbMakeBinary.getSelectionModel().select(optMbFalse);

        OptionsObject optEfFalse = new OptionsObject();
        OptionsObject optEfTrue = new OptionsObject("-F");

        List<OptionsObject> cbEfList = new ArrayList<>();
        cbEfList.add(optEfFalse);
        cbEfList.add(optEfTrue);

        cbEqualFreq.getItems().addAll(cbEfList);

        cbEqualFreq.getSelectionModel().select(optEfFalse);
        if (options != null) {

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-R")) {
                    txtAttributesIndices.setText(options[i + 1]);
                    break;
                }
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-B")) {
                    txtBins.setText(options[i + 1]);
                    break;
                }
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-M")) {
                    txtDised.setText(options[i + 1]);
                    break;
                }
            }

            cboFindNumBins.getSelectionModel().select(optFalse);
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-O")) {
                    cboFindNumBins.getSelectionModel().select(optTrue);
                    break;
                }
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-unset-class-temporarily")) {
                    cbIgnoreClass.getSelectionModel().select(optIgTrue);
                    break;
                }
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-D")) {
                    cbMakeBinary.getSelectionModel().select(optMbTrue);
                    break;
                }
            }

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-F")) {
                    cbEqualFreq.getSelectionModel().select(optEfTrue);
                    break;
                }
            }

        }
    }
}
