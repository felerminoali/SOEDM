/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.myabstract;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mz.com.MainApp;
import mz.com.model.OutputHistory;
import mz.com.tasks.remote.VisualizeTreeTask;
import mz.com.view.ESformController;
import mz.com.view.FXOptionPane;
import mz.com.view.MainGUIController;
import mz.com.view.VisualizeTreeController;

/**
 *
 * @author Lenovo
 */
public class MyHistoryOutputListCell extends ListCell<OutputHistory> {

    private ContextMenu ctextMenuSimpleClass, ctextMenuTreeClass;
    private MainGUIController mainGUIController;

    public MyHistoryOutputListCell(MainGUIController mainGUIController) {
        this.mainGUIController = mainGUIController;

        ctextMenuSimpleClass
                = ContextMenuBuilder.create()
                .items(
                        MenuItemBuilder.create()
                        .text("Delete result buffer")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                        } catch (Exception e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }
                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Save result buffer")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                        } catch (Exception e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }
                                    }
                                }
                        )
                        .build()
                )
                .build();

        ctextMenuTreeClass
                = ContextMenuBuilder.create()
                .items(
                        MenuItemBuilder.create()
                        .text("Delete result buffer")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                        } catch (Exception e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }
                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Save result buffer")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                        } catch (Exception e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }
                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Visualise tree")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/VisualizeTree.fxml"));
                                            AnchorPane page = (AnchorPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Visualize Tree");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);

                                            Scene scene = new Scene(page);
                                            dialogStage.setScene(scene);

                                            // Set the person into the controller.
                                            VisualizeTreeController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);

                                            try {
                                              
//                                                String data = mainGUIController.dataList.get(mainGUIController.dataList.size() - 1);
                                                // new code = no more undo
                                                String data = mainGUIController.txtSummary.getText();
                                                OutputHistory out = getItem();
                                                String classifier = out.getTask();
                                                String evaluation = out.getEvaluation();

                                                controller.setClassifier(classifier);
                                                controller.setEvaluation(evaluation);
                                                controller.setData(data);

                                                controller.visualize();                                               

                                            } catch (Exception ex) {
                                                FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), ex.getMessage(), "Error");
                                            }

                                            dialogStage.showAndWait();
                                        } catch (Exception e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        }
                                    }
                                }
                        )
                        .build(),
                        MenuItemBuilder.create()
                        .text("Generate Recommendation")
                        .onAction(
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        try {

                                            // Load the fxml file and create a new stage for the popup dialog.
                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setLocation(MainApp.class.getResource("view/ESform.fxml"));
                                            BorderPane page = (BorderPane) loader.load();

                                            // Create the dialog Stage.
                                            Stage dialogStage = new Stage();
                                            dialogStage.setTitle("Expert System Generator");
                                            dialogStage.initModality(Modality.WINDOW_MODAL);
                                            dialogStage.initOwner(getScene().getWindow());
                                            dialogStage.setResizable(false);

                                            Scene scene = new Scene(page);
                                            dialogStage.setScene(scene);

                                            // Set the person into the controller.
                                            ESformController controller = loader.getController();
                                            controller.setDialogStage(dialogStage);

                                            try {

                                               
//                                                String data = mainGUIController.dataList.get(mainGUIController.dataList.size() - 1);
                                                 // new code = no more undo
                                                String data = mainGUIController.txtSummary.getText();
                                                
                                                OutputHistory out = getItem();
                                                String classifier = out.getTask();
                                                String evaluation = out.getEvaluation();
                                                
                                                controller.setClassifier(classifier);
                                                controller.setEvaluation(evaluation);
                                                controller.setData(data);

                                                controller.visualize();

                                            } catch (Exception ex) {
                                                FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), ex.getMessage(), "Error");
                                            }

                                            dialogStage.showAndWait();

                                        } catch (IOException e) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), e.getMessage(), "Error");

                                        } catch (Exception exc) {
                                            FXOptionPane.showConfirmDialog((Stage) getScene().getWindow(), exc.getMessage(), "Error");
                                        }
                                    }
                                }
                        )
                        .build()
                )
                .build();

    }

    @Override
    protected void updateItem(OutputHistory t, boolean isblank) {
        super.updateItem(t, isblank);

        if (!isblank) {
            if (t != null) {
                setText(t.titleProperty().get());

                if (t.isDrawable()) {
                    setContextMenu(ctextMenuTreeClass);
                } else {
                    setContextMenu(ctextMenuSimpleClass);
                }

            } else {
                setText("");
            }

        }

    }
}
