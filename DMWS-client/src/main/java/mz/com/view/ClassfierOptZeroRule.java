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
public class ClassfierOptZeroRule extends ChooserPanel implements OptionHandle {

    private String classifier = "weka.classifiers.rules.ZeroR";
    private final String about = "Class for building and using a 0-R classifier.";
    private String options[];
    private MainGUIController mguic;

    private ComboBox<Object> cboDebug;
    private Button btnApply;

    public ClassfierOptZeroRule(MainGUIController mguic, String classifier) {
        this.mguic = mguic;
        this.classifier = classifier;
        init();
    }

    public ClassfierOptZeroRule(MainGUIController mguic, String classifier, String options[]) {
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
        Label lblDebug = new Label("debug");
        GridPane.setConstraints(lblDebug, 0, 0);

        this.cboDebug = new ComboBox<>();
        GridPane.setConstraints(cboDebug, 1, 0);

         btnApply = new Button("Apply");
        btnApply.setOnAction(e -> {

            try {
                String opt = getOptions();
                this.mguic.txtClassifier.setText(opt);

            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(this.mguic.getMainApp().getPrimaryStage(), ex.getMessage(), "Error");
            }

        });

        GridPane.setConstraints(btnApply, 1, 1);

        grid.getChildren().addAll(
                lblDebug,
                cboDebug,
                btnApply
        );

        setOptions();

        this.getChildren().addAll(vbox, grid);
    }

    @Override
    public String getOptions() {
        
        
        String scheme = classifier;

        OptionsObject debug = (OptionsObject) cboDebug.getSelectionModel().getSelectedItem();

        if (debug.toString().equals("True")) {
            scheme += " " + debug.getValue();
        }
        
          return scheme;
    }

    @Override
    public void setOptions() {
        
         OptionsObject optFalse = new OptionsObject();
        OptionsObject optTrue = new OptionsObject("-D");

        List<OptionsObject> cbFindList = new ArrayList<>();
        cbFindList.add(optFalse);
        cbFindList.add(optTrue);

        cboDebug.getItems().addAll(cbFindList);

        cboDebug.getSelectionModel().select(optFalse);
        
         if (options != null) {
             
             for (int i = 0; i < options.length; i++) {
                if (options[i].equals("-D")) {
                    cboDebug.getSelectionModel().select(optTrue);
                    break;
                }
            }
         
         
         }
        
    }

}
