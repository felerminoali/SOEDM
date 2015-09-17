/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.myabstract;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import mz.com.view.MainGUIController;

/**
 *
 * @author Lenovo
 */
public class CallbackCustom implements Callback<TreeView<String>, TreeCell<String>> {

    private MainGUIController mainGUIController;

    public CallbackCustom(MainGUIController mainGUIController) {
        this.mainGUIController = mainGUIController;
    }

    @Override
    public TreeCell<String> call(TreeView<String> arg0) {
        // custom tree cell that defines a context menu for the root tree item
        return new MyTreeCell(mainGUIController);
    }
}
