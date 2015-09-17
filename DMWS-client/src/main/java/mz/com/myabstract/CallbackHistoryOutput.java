/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mz.com.myabstract;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import mz.com.model.OutputHistory;
import mz.com.view.MainGUIController;

/**
 *
 * @author Lenovo
 */
public class CallbackHistoryOutput implements Callback<ListView<OutputHistory>, ListCell<OutputHistory>>{

     private MainGUIController mainGUIController;

    public CallbackHistoryOutput(MainGUIController mainGUIController) {
        this.mainGUIController = mainGUIController;
    }
    
    @Override
    public ListCell<OutputHistory> call(ListView<OutputHistory> param) {
        return new MyHistoryOutputListCell(mainGUIController);
    }
    
}
