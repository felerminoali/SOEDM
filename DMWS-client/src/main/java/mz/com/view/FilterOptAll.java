/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Lenovo
 */
public class FilterOptAll extends ChooserPanel {

    private String filter = "weka.filters.AllFilter";
    private String about = "An instance filter that passes all instances through unmodified.";
    private MainGUIController mguic;

    public FilterOptAll() {
        init();
    }

    public FilterOptAll(MainGUIController mguic) {
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
        Label lblInfo = new Label("No Editable properties");

        this.setPadding(new Insets(20, 20, 20, 20));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(vbox, lblInfo);
    }

}
