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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Lenovo
 */
public class FilterOptManualDiscretize extends ChooserPanel implements OptionHandle {

    private String filter = "weka.filters.unsupervised.attribute.Discretize";
    private String about = "An instance filter that discretizes a range of numeric attributes in the dataset into \nnominal attributes.";
    private String options[];
    private MainGUIController mguic;

    private ListView<Object> listCatClass;
    private ListView<Object> listAtrrC;
    private Button btnApply;

    public FilterOptManualDiscretize(MainGUIController mguic, String filter) {
        this.filter = filter;
        init();
    }

    public FilterOptManualDiscretize(MainGUIController mguic, String filter, String options[]) {
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

        GridPane.setMargin(grid, new Insets(0, 10, 10, 10));
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Name lablel
        Label lblAcat = new Label("Attribute categories");
        GridPane.setConstraints(lblAcat, 0, 0);

        //
        listAtrrC = new ListView<>();
        GridPane.setConstraints(listAtrrC, 1, 0);

        // Name lablel
        Label lblAclass = new Label("Class categories");
        GridPane.setConstraints(lblAclass, 0, 1);

        //
        listCatClass = new ListView<>();
        GridPane.setConstraints(listCatClass, 1, 1);

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

        grid.getChildren().addAll(
                lblAcat,
                listAtrrC,
                lblAclass,
                listCatClass,
                btnApply
        );

        setOptions();

        this.getChildren().addAll(vbox, grid);
    }

    @Override
    public String getOptions() {
        return "";
    }

    @Override
    public void setOptions() {
        if (options != null) {
            List<String> attrOpt = new ArrayList<>();
            int i = 0;

            while (i < options.length) {
                if (options[i].equals("-A")) {
                    break;
                }
                i++;
            }

            int j = (i + 1);
            while (true) {
                if (options[j].equals("-C") || j == options.length) {
                    break;
                }
                attrOpt.add(options[j]);
                j++;
            }

            attrOpt.add("Add Category");

            listAtrrC.getItems().addAll(attrOpt);

            List<String> attrOptClass = new ArrayList<>();
            int k = 0;
            while (k < options.length) {
                if (options[k].equals("-C")) {
                    break;
                }
                k++;
            }

            int z = (k + 1);
            while (true) {

                if (z == options.length) {
                    break;
                }
                attrOptClass.add(options[z]);

                z++;
            }
            attrOptClass.add("Add Category");
            listCatClass.getItems().addAll(attrOptClass);
        }
    }
}
