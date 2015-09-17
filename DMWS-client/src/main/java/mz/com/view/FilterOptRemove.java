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
public class FilterOptRemove extends ChooserPanel implements OptionHandle {

    private String filter = "weka.filters.unsupervised.attribute.Discretize";
    private String about = "An instance filter that discretizes a range of numeric attributes in the dataset into \nnominal attributes.";
    private String options[];
    private MainGUIController mguic;

    // fields
    TextField txtAttrIndeces;
    ComboBox<Object> cbInvertSel;
    Button btnApply;

    public FilterOptRemove(MainGUIController mguic, String filter) {
        this.mguic = mguic;
        this.filter = filter;
        init();
    }

    public FilterOptRemove(MainGUIController mguic, String filter, String options[]) {
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
        Label attributesIndeces = new Label("attributesIndeces");
        GridPane.setConstraints(attributesIndeces, 0, 0);

        //
        txtAttrIndeces = new TextField();
        GridPane.setConstraints(txtAttrIndeces, 1, 0);

        // Name lablel
        Label invertSelection = new Label("invertSelection");
        GridPane.setConstraints(invertSelection, 0, 1);

        //
        cbInvertSel = new ComboBox<>();
//        txtAttributesIndices.
        GridPane.setConstraints(cbInvertSel, 1, 1);

        btnApply = new Button("Apply");
        btnApply.setOnAction(e -> {

            try {
                String opt = getOptions();
                this.mguic.txtFilter.setText(opt);

            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(this.mguic.getMainApp().getPrimaryStage(), ex.getMessage(), "Error");
            }

        });

        GridPane.setConstraints(btnApply, 1, 2);
//        grid.getChildren().addAll(nameLabel, nameTxtField, passLabel, passTxtField, btn);
        grid.getChildren().addAll(
                attributesIndeces,
                txtAttrIndeces,
                invertSelection,
                cbInvertSel,
                btnApply
        );

        setOptions();

        this.getChildren().addAll(vbox, grid);
    }

    @Override
    public String getOptions() {
        String scheme = filter;

        OptionsObject invertSel = (OptionsObject) cbInvertSel.getSelectionModel().getSelectedItem();

        if (invertSel.toString().equals("True")) {
            scheme += " " + invertSel.getValue();
        }

        if (!txtAttrIndeces.getText().isEmpty()) {
            scheme += " -R " + txtAttrIndeces.getText();
        }

        return scheme;
    }

    @Override
    public void setOptions() {

        OptionsObject optFalse = new OptionsObject();
        OptionsObject optTrue = new OptionsObject("-V");

        List<OptionsObject> cbFindList = new ArrayList<>();
        cbFindList.add(optFalse);
        cbFindList.add(optTrue);

        cbInvertSel.getItems().addAll(cbFindList);

        cbInvertSel.getSelectionModel().select(optFalse);
        if (options != null) {

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-V")) {
                    cbInvertSel.getSelectionModel().select(optTrue);
                    break;
                }
            }
        }
    }

}
