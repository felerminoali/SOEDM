/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.myabstract;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import mz.com.view.ESformController;

/**
 *
 * @author Lenovo
 */
public class CallbackCustomES implements Callback<TreeView<Object>, TreeCell<Object>> {

    private ESformController mainGUIController;


    public CallbackCustomES(ESformController mainGUIController) {
        this.mainGUIController = mainGUIController;
    }


    @Override
    public TreeCell<Object> call(TreeView<Object> param) {
        return new MyTreeCellES(mainGUIController);
    }
}
