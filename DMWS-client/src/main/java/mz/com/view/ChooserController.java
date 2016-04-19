/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import jaxb.weka.associations.Associations;
import jaxb.weka.classifers.Classifers;
import jaxb.weka.classifers.Classifers.Method;
import jaxb.weka.cluster.Clusters;
import jaxb.weka.filters.Filters;
import jaxb.weka.filters.Filters.Strategy;
import jaxb.weka.filters.Filters.Strategy.InputType;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class ChooserController implements Initializable {

    @FXML
    private TreeView<Object> treeChooser;

    private MainGUIController mainApp;
    private boolean okClicked = false;
    private Stage dialogStage;
//    private Weka weka;
    private Object weka;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setWekaFilter(Filters weka) {
        this.weka = weka;
        setFilterTree();
    }

    public void setWekaClassifier(Classifers weka) {

        this.weka = weka;
        setClassifierTree();

    }

    public void setWekaCluster(Clusters weka) {

        this.weka = weka;
        setClusterTree();

    }

    public void setWekaAssociator(Associations weka) {

        this.weka = weka;
        setAssociateTree();

    }

    private void setFilterTree() {

        try {
            //create the child nodes

            TreeItem<Object> root = new TreeItem<>();
            root.setExpanded(false);

             // super node - tem ser aultomatizado
            TreeItem<Object> root1 = makeBranch("Weka", root);
            root1.setExpanded(true);
            
            Filters filters = (Filters) weka;
            for (jaxb.weka.filters.Filters.Config f : filters.getConfig()) {
                // antes e depois
//                makeBranch(f, root);
                makeBranch(f, root1);
            }

            
            for(Strategy strategy: filters.getStrategy()){
            
//                     TreeItem<Object> strategyType = makeBranch(strategy, root);
                      TreeItem<Object> strategyType = makeBranch(strategy, root1);
                      strategyType.setExpanded(true);
                     
                     for(InputType inputType : strategy.getInputType()){
                         TreeItem<Object> inputTypeName = makeBranch(inputType, strategyType);
                         
                         for(jaxb.weka.filters.Filters.Config c : inputType.getConfig()){
                             makeBranch(c, inputTypeName);
                         }
                     }
            
            }
            // create tree
//            treeChooser.setRoot(root);
             treeChooser.setRoot(root);
            treeChooser.setShowRoot(false);

        } catch (Exception e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }
    }

    private void setClassifierTree() {

        try {
            //create the child nodes

            TreeItem<Object> root = new TreeItem<>();
            root.setExpanded(false);
             // super node - tem ser aultomatizado
            TreeItem<Object> root1 = makeBranch("Weka", root);

            Classifers classfiers = (Classifers) weka;

            for (Method method : classfiers.getMethod()) {
//                TreeItem<Object> methodName = makeBranch(method, root);
                TreeItem<Object> methodName = makeBranch(method, root1);
                for (jaxb.weka.classifers.Classifers.Method.Config c : method.getConfig()) {
                    makeBranch(c, methodName);
                }
            }

            // create tree
            treeChooser.setRoot(root);
            treeChooser.setShowRoot(false);

        } catch (Exception e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }
    }

    private void setClusterTree() {

        try {
            //create the child nodes

            TreeItem<Object> root = new TreeItem<>();
            root.setExpanded(false);

             // super node - tem ser aultomatizado
            TreeItem<Object> root1 = makeBranch("Weka", root);
            
            Clusters clusters = (Clusters) weka;

            for (jaxb.weka.cluster.Clusters.Config c : clusters.getConfig()) {
//                makeBranch(c, root);
                makeBranch(c, root1);
            }

            // create tree
            treeChooser.setRoot(root);
            treeChooser.setShowRoot(false);

        } catch (Exception e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }

    }

    private void setAssociateTree() {

        try {
            //create the child nodes

            TreeItem<Object> root = new TreeItem<>();
            root.setExpanded(false);

              // super node - tem ser aultomatizado
            TreeItem<Object> root1 = makeBranch("Weka", root);
            
            Associations associations = (Associations) weka;

            for (jaxb.weka.associations.Associations.Config c : associations.getConfig()) {
//                makeBranch(c, root);
                 makeBranch(c, root1);
            }

            // create tree
            treeChooser.setRoot(root);
            treeChooser.setShowRoot(false);

        } catch (Exception e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }

    }

//treeChooser
    @FXML
    public void handleBtnSelect() {

        try {

            if (!treeChooser.getSelectionModel().getSelectedItems().isEmpty()) {

                ObservableList<TreeItem<Object>> selectedFilter = treeChooser.getSelectionModel().getSelectedItems();

                String choosed = "";
                String defaults = "";
                boolean isFilter = false;
                boolean isClassifier = false;
                boolean isCluster = false;
                boolean isAssociator = false;

                for (TreeItem<Object> treeItem : selectedFilter) {

                    if (treeItem.getValue() instanceof jaxb.weka.filters.Filters.Config) {
                        isFilter = true;
                        jaxb.weka.filters.Filters.Config selectedObject = (jaxb.weka.filters.Filters.Config) treeItem.getValue();
                        choosed = selectedObject.getFilter();
                        defaults = selectedObject.getDefaults();
                    }

                    if (treeItem.getValue() instanceof jaxb.weka.classifers.Classifers.Method.Config) {
                        isClassifier = true;
                        jaxb.weka.classifers.Classifers.Method.Config selectedObject = (jaxb.weka.classifers.Classifers.Method.Config) treeItem.getValue();
                        choosed = selectedObject.getClassifer();
                        defaults = selectedObject.getDefaults();

                    }

                    if (treeItem.getValue() instanceof jaxb.weka.cluster.Clusters.Config) {
                        isCluster = true;
                        jaxb.weka.cluster.Clusters.Config selectedObject = (jaxb.weka.cluster.Clusters.Config) treeItem.getValue();
                        choosed = selectedObject.getCluster();
                        defaults = selectedObject.getDefaults();

                    }
                    if (treeItem.getValue() instanceof jaxb.weka.associations.Associations.Config) {
                        isAssociator = true;
                        jaxb.weka.associations.Associations.Config selectedObject = (jaxb.weka.associations.Associations.Config) treeItem.getValue();
                        choosed = selectedObject.getAssociatior();
                        defaults = selectedObject.getDefaults();

                    }

                }

                if (isFilter) {
                    if (defaults.isEmpty()) {
                        this.mainApp.setTxtFilter(choosed);
                    } else {
                        this.mainApp.setTxtFilter(choosed + " " + defaults);
                    }
                }

                if (isClassifier) {
                    if (defaults.isEmpty()) {
                        this.mainApp.setTxtClassifier(choosed);
                    } else {
                        this.mainApp.setTxtClassifier(choosed + " " + defaults);
                    }

                }

                if (isCluster) {
                    if (defaults.isEmpty()) {
                        this.mainApp.setTxtCluster(choosed);
                    } else {
                        this.mainApp.setTxtCluster(choosed + " " + defaults);
                    }

                }

                if (isAssociator) {
                    if (defaults.isEmpty()) {
                        this.mainApp.setTxtAssociator(choosed);
                    } else {
                        this.mainApp.setTxtAssociator(choosed + " " + defaults);
                    }

                }

                okClicked = true;
                this.dialogStage.close();
            }

        } catch (Exception e) {
            FXOptionPane.showConfirmDialog(dialogStage, e.getMessage(), "Error");
        }

    }

    private TreeItem<Object> makeBranch(Object title, TreeItem<Object> parent) {

        TreeItem<Object> item = new TreeItem<>(title);

        item.setExpanded(false);
        parent.getChildren().add(item);

        return item;
    }

    public void setMainController(MainGUIController mainGUIController) {
        this.mainApp = mainGUIController;
    }

}
