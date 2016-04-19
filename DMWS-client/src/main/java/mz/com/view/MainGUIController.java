/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.view;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import jaxb.connections.Connections;
import jaxb.connections.Connections.Datasource;
import jaxb.weka.associations.Associations;
import jaxb.weka.classifers.Classifers;
import jaxb.weka.cluster.Clusters;
import jaxb.weka.filters.Filters;
import mz.com.MainApp;
import mz.com.model.DataOutputHistory;
import mz.com.model.OutputHistory;
import mz.com.myabstract.CallbackCustom;
import mz.com.myabstract.CallbackHistoryOutput;
import mz.com.tasks.remote.AssociateTask;
import mz.com.tasks.remote.ClassifyTask;
import mz.com.tasks.remote.ClusterTask;
import mz.com.tasks.remote.DefaultTask;
import mz.com.tasks.remote.FilterTask;
import mz.com.tasks.remote.ManualDiscretizeTask;
import mz.com.tasks.remote.RetriverAndConvertTask;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class MainGUIController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private TreeView<String> treeCon;

    @FXML
    private ListView listClassScheme, listClustScheme, listAssocScheme, listDataCollected;

    @FXML
    public TextArea txtSummary, txtOutputClass, txtOutputCluster, txtOutputAssociator;

    @FXML
    public TextField txtCrossVal, txtPercValClass, txtPercValCluster;

    @FXML
    public TextField txtFilter, txtClassifier, txtCluster, txtAssociator;

    @FXML
    public Button btnExecute;

    @FXML
    public Button btnApply;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnClassStart, btnChooseCluster, btnClusStart, btnAssoStart;

    @FXML
    public Tab tabPrepro, tabClassify, tabCluster, tabAssociator;

    @FXML
    RadioButton radioCrossVal, radioParcValClass, radioParcValClust, radioUseTrainClass, radioUseTrainClust;

    @FXML
    private ProgressBar filterProgress, classProgress, clusProgress, assoProgress;

    @FXML
    private HBox HBxPre, HBxClass, HBxClust, HBxAss;

    @FXML
    private VBox vBoxPreResult;

    public String evalution = "";
    private String clusterEvaluation = "";

    // Reference to the main application.
    private MainApp mainApp;

//    public ListProperty<String> dataList = new SimpleListProperty<>(FXCollections.<String>observableArrayList());
    private ListProperty<OutputHistory> classifierHistorys = new SimpleListProperty<>(FXCollections.<OutputHistory>observableArrayList());
    private ListProperty<OutputHistory> clusHistorys = new SimpleListProperty<>(FXCollections.<OutputHistory>observableArrayList());
    private ListProperty<OutputHistory> assHistorys = new SimpleListProperty<>(FXCollections.<OutputHistory>observableArrayList());

    // collected data list output
    private ListProperty<DataOutputHistory> dataCollectHistory = new SimpleListProperty<>(FXCollections.<DataOutputHistory>observableArrayList());

    final ToggleGroup groupClassifierEvaluator = new ToggleGroup();
    final ToggleGroup groupClusterEvaluator = new ToggleGroup();

    Task<List<String>> tasks = new DefaultTask();

    public MainGUIController() {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bindTreeConnectionAndButtonExecute();

        bindTxtSummaryWithSaveAndButton();

        // classfier evaluation options
        txtCrossVal.textProperty().addListener(e -> {
            evalution = "";
            if (!txtCrossVal.getText().isEmpty()) {
                evalution += "cv -f " + txtCrossVal.getText() + " -S 1";
            } else {
                evalution = "";
            }
        });

        txtPercValClass.textProperty().addListener(e -> {
            evalution = "";
            if (!txtPercValClass.getText().isEmpty()) {
                evalution += "p-split -Z " + Double.parseDouble(txtPercValClass.getText()) + " -S 1";
            } else {
                evalution = "";
            }
        });

        loadDefaultValue();

        radioCrossVal.setToggleGroup(groupClassifierEvaluator);
        radioParcValClass.setToggleGroup(groupClassifierEvaluator);
        radioUseTrainClass.setToggleGroup(groupClassifierEvaluator);

        radioCrossVal.setSelected(true);

        txtCrossVal.disableProperty().bind(Bindings.not(radioCrossVal.selectedProperty()));
        txtPercValClass.disableProperty().bind(Bindings.not(radioParcValClass.selectedProperty()));

        final ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Show Propreties..");
        item1.setId("showProp");
        item1.setOnAction(this);
        MenuItem item2 = new MenuItem("Enter Configuration..");
        item2.setOnAction(this);
        contextMenu.getItems().addAll(item1, item2);

        txtFilter.setContextMenu(contextMenu);
        txtClassifier.setContextMenu(contextMenu);

        // cluster evaluation options
        txtPercValCluster.textProperty().addListener(e -> {
            clusterEvaluation = "";
            if (!txtPercValCluster.getText().isEmpty()) {
                clusterEvaluation += "p-split -Z " + Double.parseDouble(txtPercValCluster.getText()) + " -S 1";
            } else {
                clusterEvaluation = "";
            }

//            System.out.println(clusterEvaluation);
        });

        txtPercValCluster.setText("66.0");

        groupClassifierEvaluator.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button

                evalution = "";
                if (chk == radioCrossVal) {

                    if (!txtCrossVal.getText().isEmpty()) {
                        evalution += "cv -f " + txtCrossVal.getText() + " -S 1";
                    } else {
                        evalution = "";
                    }

                } else if (chk == radioParcValClass) {
                    if (!txtPercValClass.getText().isEmpty()) {
                        evalution += "p-split -Z " + Double.parseDouble(txtPercValClass.getText()) + " -S 1";
                    } else {
                        evalution = "";
                    }
                }
//                System.out.println(evalution);
            }
        });

        groupClusterEvaluator.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button

                clusterEvaluation = "";
                if (chk == radioParcValClust) {
                    if (!txtPercValCluster.getText().isEmpty()) {
                        clusterEvaluation += "p-split -Z " + Double.parseDouble(txtPercValCluster.getText()) + " -S 1";
                    } else {
                        clusterEvaluation = "";
                    }
                }

            }
        });

        radioParcValClust.setToggleGroup(groupClusterEvaluator);
        radioUseTrainClust.setToggleGroup(groupClusterEvaluator);

        radioParcValClust.setSelected(true);

        txtPercValCluster.disableProperty().bind(Bindings.not(radioParcValClust.selectedProperty()));

        txtCluster.setContextMenu(contextMenu);

        //
        txtAssociator.setContextMenu(contextMenu);

        // binding
//        dataList.addListener(new ListChangeListener<String>() {
//
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends String> c) {
//
////                System.out.println(dataList.size());
//                if (dataList.size() > 1) {
//                    btnUndo.setDisable(false);
//                }
//
//                if (dataList.size() == 1) {
//                    btnUndo.setDisable(true);
//                }
//            }
//        });

//        btnApply.disableProperty().bind(Bindings.or(txtFilter.textProperty().isEqualTo(""), dataList.emptyProperty()));
        btnApply.disableProperty().bind(Bindings.or(txtFilter.textProperty().isEmpty(), txtSummary.textProperty().isEmpty()));

//        tabClassify.disableProperty().bind(dataList.emptyProperty());
//        tabCluster.disableProperty().bind(dataList.emptyProperty());
//        tabAssociator.disableProperty().bind(dataList.emptyProperty());
        listDataCollected.setItems(dataCollectHistory);
        listClassScheme.setItems(classifierHistorys);
        listClustScheme.setItems(clusHistorys);
        listAssocScheme.setItems(assHistorys);

        listDataCollected.setOnMouseClicked(e -> {
            ObservableList<DataOutputHistory> out = listDataCollected.getSelectionModel().getSelectedItems();
            DataOutputHistory output = null;
            for (DataOutputHistory o : out) {
                output = o;
            }

            if (output != null) {
                this.txtSummary.setText(output.getOutput());
            }

        });

        listClassScheme.setOnMouseClicked(e -> {
            ObservableList<OutputHistory> out = listClassScheme.getSelectionModel().getSelectedItems();
            OutputHistory output = null;
            for (OutputHistory o : out) {
                output = o;
            }

            if (output != null) {
                this.txtOutputClass.setText(output.getOutput());
            }

        });

        listClassScheme.setCellFactory(new CallbackHistoryOutput(this));

        listClustScheme.setOnMouseClicked(e -> {
            ObservableList<OutputHistory> out = listClustScheme.getSelectionModel().getSelectedItems();
            OutputHistory output = null;
            for (OutputHistory o : out) {
                output = o;
            }

            if (output != null) {
                this.txtOutputCluster.setText(output.getOutput());
            }

        });

        listAssocScheme.setOnMouseClicked(e -> {
            ObservableList<OutputHistory> out = listAssocScheme.getSelectionModel().getSelectedItems();
            OutputHistory output = null;
            for (OutputHistory o : out) {
                output = o;
            }

            if (output != null) {
                this.txtOutputAssociator.setText(output.getOutput());
            }

        });

        btnClassStart.disableProperty().bind(txtClassifier.textProperty().isEmpty());
        btnClusStart.disableProperty().bind(txtCluster.textProperty().isEmpty());

        disableTabClosable();

    }

    private void bindTxtSummaryWithSaveAndButton() {
        // bind Save button

        txtSummary.textProperty().addListener((v, old, newValue) -> {

            String[] s = newValue.split("\\s");
            if (s[0].equals("@relation")) {
                btnSave.setDisable(Boolean.FALSE);

                tabClassify.setDisable(Boolean.FALSE);
                tabCluster.setDisable(Boolean.FALSE);
                tabAssociator.setDisable(Boolean.FALSE);

            } else {
                btnSave.setDisable(Boolean.TRUE);

                tabClassify.setDisable(Boolean.TRUE);
                tabCluster.setDisable(Boolean.TRUE);
                tabAssociator.setDisable(Boolean.TRUE);
            }

        });
    }

    private void disableTabClosable() {
        tabClassify.setClosable(false);
        tabCluster.setClosable(false);
        tabAssociator.setClosable(false);
        tabPrepro.setClosable(false);
    }

    private void loadDefaultValue() {
        txtCrossVal.setText("10");
        txtPercValClass.setText("66.0");
    }

    private void bindTreeConnectionAndButtonExecute() {
        // TODO

        BooleanProperty b = new SimpleBooleanProperty(Boolean.TRUE);
        // treeCon selection model

//        if (!treeCon.getSelectionModel().getSelectedItems().isEmpty()) {
        treeCon.getSelectionModel().selectedItemProperty().addListener((v, old, newValue) -> {

            TreeItem<String> selectedItem = (TreeItem<String>) newValue;

            try {
                if (selectedItem.isLeaf()) {
                    b.setValue(Boolean.FALSE);
                } else {
                    b.setValue(Boolean.TRUE);
                }
            } catch (Exception ex) {
                FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
            }

        });
//        }
        btnExecute.disableProperty().bind(tasks.runningProperty().or(b));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        setConnectionTree();

    }

    public MainApp getMainApp() {
        return this.mainApp;
    }

    @FXML
    public void handleBtnAssoStart() {

        try {

//            String data = this.dataList.get(dataList.size() - 1);
            // new code = no more undo 
            String data = txtSummary.getText();
            
            String associator = this.txtAssociator.getText();
            AssociateTask task;

            task = new AssociateTask(data, associator);

            // Creating a button to cancel the execution of the task
            Button btnClose = new Button("X");

            btnClose.setStyle("-fx-font: 8 arial;");
            btnClose.setMaxSize(2, 2);

            btnClose.setOnAction(e -> {
                task.cancelProgress();
            });

            HBxAss.getChildren().addAll(btnClose);

            // to disable the retriever button while is running the task
            btnAssoStart.disableProperty().bind(task.runningProperty());

            this.assoProgress.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((w, o, n) -> {
                txtOutputAssociator.clear();
                txtOutputAssociator.appendText(n + "\n");
                this.assHistorys.add(new OutputHistory(n, associator, null, ((AssociateTask) task).getTimeMillis()));
                this.listAssocScheme.getSelectionModel().selectLast();

                HBxAss.getChildren().remove(btnClose);
            });

            new Thread(task).start();

        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

    @FXML
    public void handleBtnClusStart() {

        try {

//            String data = this.dataList.get(dataList.size() - 1);
            // new code = no more undo
            String data = txtSummary.getText();
            
            String classifier = this.txtCluster.getText();
            ClusterTask task;

            if (clusterEvaluation.isEmpty()) {
                task = new ClusterTask(data, classifier);
            } else {
                task = new ClusterTask(data, classifier, clusterEvaluation);
            }

            // Creating a button to cancel the execution of the task
            Button btnClose = new Button("X");

            btnClose.setStyle("-fx-font: 8 arial;");
            btnClose.setMaxSize(2, 2);

            btnClose.setOnAction(e -> {
                task.cancelProgress();
            });

            HBxClust.getChildren().addAll(btnClose);

            // to disable the retriever button while is running the task
            btnClusStart.disableProperty().bind(task.runningProperty());

            this.clusProgress.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((w, o, n) -> {
                txtOutputCluster.clear();
                txtOutputCluster.appendText(n + "\n");
                this.clusHistorys.add(new OutputHistory(n, classifier, clusterEvaluation, ((ClusterTask) task).getTimeMillis()));
                this.listClustScheme.getSelectionModel().selectLast();

                HBxClust.getChildren().remove(btnClose);
            });

            new Thread(task).start();

        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

    @FXML
    public void handleBtnClassStart() {

        try {

//            String data = this.dataList.get(dataList.size() - 1);
            // new code = no more undo
            String data = txtSummary.getText();
            
            String classifier = this.txtClassifier.getText();
            ClassifyTask task;

            if (evalution.isEmpty()) {
                task = new ClassifyTask(data, classifier);
            } else {
                task = new ClassifyTask(data, classifier, evalution);
            }

            // Creating a button to cancel the execution of the task
            Button btnClose = new Button("X");

            btnClose.setStyle("-fx-font: 8 arial;");
            btnClose.setMaxSize(2, 2);

            btnClose.setOnAction(e -> {
                task.cancelProgress();
            });

            HBxClass.getChildren().addAll(btnClose);

            // to disable the retriever button while is running the task
            btnClassStart.disableProperty().bind(task.runningProperty());

            this.classProgress.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((w, o, n) -> {
                txtOutputClass.clear();
                txtOutputClass.appendText(n + "\n");
                
                   
                this.classifierHistorys.add(new OutputHistory(n, classifier, evalution, ((ClassifyTask) task).getTimeMillis()));
                this.listClassScheme.getSelectionModel().selectLast();

                HBxClass.getChildren().remove(btnClose);
            });

            new Thread(task).start();

        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

    @FXML
    private void handleBtnChooseFilter() {
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(jaxb.weka.filters.Filters.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Filters weka = (Filters) jaxbUnmarshaller.unmarshal(new File("src/filters.xml"));
            Filters weka = (Filters) jaxbUnmarshaller.unmarshal(new File("src/main/resources/filters.xml"));

            boolean okClicked = mainApp.showFilterChooser(weka);

            if (okClicked) {

            }
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }

    }

    @FXML
    public void handleBtnChooseAssociator() {

        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(jaxb.weka.associations.Associations.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Associations weka = (Associations) jaxbUnmarshaller.unmarshal(new File("src/associations.xml"));
            Associations weka = (Associations) jaxbUnmarshaller.unmarshal(new File("src/main/resources/associations.xml"));

            boolean okClicked = mainApp.showAssociateChooser(weka);

            if (okClicked) {

            }
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }

    }

    @FXML
    public void handleBtnChooseCluster() {
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(jaxb.weka.cluster.Clusters.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Clusters weka = (Clusters) jaxbUnmarshaller.unmarshal(new File("src/clusters.xml"));
            Clusters weka = (Clusters) jaxbUnmarshaller.unmarshal(new File("src/main/resources/clusters.xml"));

            boolean okClicked = mainApp.showClusterChooser(weka);

            if (okClicked) {

            }
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

    @FXML
    public void handleBtnChooseClassifer() {
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(jaxb.weka.classifers.Classifers.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

//            Classifers weka = (Classifers) jaxbUnmarshaller.unmarshal(new File("src/classifiers.xml"));
            Classifers weka = (Classifers) jaxbUnmarshaller.unmarshal(new File("src/main/resources/classifiers.xml"));

            boolean okClicked = mainApp.showClassifierChooser(weka);

            if (okClicked) {

            }
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }

    }

    private void setConnectionTree() {
        try {

            JAXBContext jaxbContext = JAXBContext
                    .newInstance(Connections.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

//            Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File("src/datasource.xml"));
            Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File("src/main/resources/datasource.xml"));
            setConnectionTree(datasource);

        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

    public void setConnectionTree(Connections datasource) {
        try {

            List<Datasource> ConList = datasource.getDatasource();

//            TreeItem<String> root;
            TreeItem<String> root = new TreeItem<>();

            root.setExpanded(true);

            for (Datasource dataSrc : ConList) {

                TreeItem<String> rootNode = makeBranch(dataSrc.toString(), root);
                for (String func : dataSrc.getRemote().getFunction()) {

                    // instantiate the root context menu
                    makeBranch(func, rootNode);
                }
            }

            // create tree
            treeCon.setRoot(root);

            // defines a custom tree cell factory for the tree view
            treeCon.setCellFactory(new CallbackCustom(this));

            treeCon.setShowRoot(false);
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {

        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);

        return item;
    }

    public void setTxtAssociator(String input) {
        if (!input.isEmpty()) {
            this.txtAssociator.setText(input);
        }
    }

    public void setTxtFilter(String input) {
        if (!input.isEmpty()) {
            this.txtFilter.setText(input);
        }
    }

    public void setTxtClassifier(String input) {
        if (!input.isEmpty()) {
            this.txtClassifier.setText(input);
        }
    }

    public void setTxtCluster(String input) {
        if (!input.isEmpty()) {
            this.txtCluster.setText(input);
        }
    }

    public void addTreeConnection(Datasource dataSrc) {

        TreeItem<String> root = treeCon.getRoot();

        TreeItem<String> rootNode = makeBranch(dataSrc.toString(), root);
        for (String func : dataSrc.getRemote().getFunction()) {

            // instantiate the root context menu
            makeBranch(func, rootNode);
        }

        // create tree
        treeCon.setRoot(root);

        // defines a custom tree cell factory for the tree view
        treeCon.setCellFactory(new CallbackCustom(this));

        treeCon.setShowRoot(false);
    }

    @Override
    public void handle(ActionEvent event) {

        TextField txField = new TextField();

        if (txtFilter.isFocused()) {
            txField = txtFilter;
        } else if (txtClassifier.isFocused()) {
            txField = txtClassifier;
        } else if (txtCluster.isFocused()) {
            txField = txtCluster;
        } else if (txtAssociator.isFocused()) {
            txField = txtAssociator;
        }

        if (!txField.getText().isEmpty()) {
            if (((MenuItem) event.getSource()).getId().equals("showProp")) {
                Stage window = new Stage();
                window.setTitle("Generic Object Editor");
                window.initModality(Modality.WINDOW_MODAL);
                window.initOwner(mainApp.getPrimaryStage());
                window.setResizable(false);

                //layout
                VBox layout = new ChosserPanelFactory().createPanel(this, txField);

                if (layout != null) {
                    Scene scene = new Scene(layout, 500, 500);

                    window.setScene(scene);

                    window.show();
                } else {
                    FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), "Under construction", "Error");
                }

                event.consume();
            }
        } else {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), "The field is empty", "Error");
        }

    }

    @FXML
    public void handleBtnSave() {

        try {
//            boolean okClicked = mainApp.showFileChooser(this.dataList.get(dataList.size() - 1));
            // new code = no more undo
             boolean okClicked = mainApp.showFileChooser(txtSummary.getText());

            if (okClicked) {

            }
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }

    }

    @FXML
    public void handleButtonApply() {
        try {
            String args[] = txtFilter.getText().split("\\s");

            if (args[0].equals("weka.filters.unsupervised.attribute.ManualDiscretize")) {

                if (args.length > 1) {
                    String options = "";
                    for (int i = 1; i < args.length; i++) {
                        options += args[i] + " ";
                    }

//                    String data = this.dataList.get(this.dataList.size() - 1);
                    // New code = no more undo button
                    String data = txtSummary.getText();
                    
                    String filter = options;

                    try {

                        ManualDiscretizeTask task = new ManualDiscretizeTask(data, filter);

                        // Creating a button to cancel the execution of the task
                        Button btnClose = new Button("X");

                        btnClose.setStyle("-fx-font: 8 arial;");
                        btnClose.setMaxSize(2, 2);

                        btnClose.setOnAction(e -> {
                            task.cancelProgress();
                        });

                        HBxPre.getChildren().addAll(btnClose);

                        // to disable the retriever button while is running the task
                        btnApply.disableProperty().bind(task.runningProperty());

                        this.filterProgress.progressProperty().bind(task.progressProperty());
                        task.messageProperty().addListener((w, o, n) -> {
                            txtSummary.clear();
                            txtSummary.appendText(n + "\n");
//                            this.dataList.add(n);

                            this.dataCollectHistory.add(new DataOutputHistory(n, ((ManualDiscretizeTask) task).getTimeMillis()));
                            this.listDataCollected.getSelectionModel().selectLast();

                            HBxPre.getChildren().remove(btnClose);
                        });

                        new Thread(task).start();

                    } catch (Exception ex) {
                        FXOptionPane.showConfirmDialog(this.mainApp.getPrimaryStage(), "Unsuccessful!", "Error");
                    }

                }

            } else {
//                if (!this.dataList.isEmpty()) {
                    if (!this.txtSummary.getText().isEmpty()) {
                        
//                    String data = this.dataList.get(this.dataList.size() - 1);
                    String data = this.txtSummary.getText();
                    
                    String filter = "FILTER " + txtFilter.getText();

                    try {

                        FilterTask task = new FilterTask(data, filter);

                        // Creating a button to cancel the execution of the task
                        Button btnClose = new Button("X");

                        btnClose.setStyle("-fx-font: 8 arial;");
                        btnClose.setMaxSize(2, 2);

                        btnClose.setOnAction(e -> {
                            task.cancelProgress();
                        });

                        HBxPre.getChildren().addAll(btnClose);

                        // to disable the retriever button while is running the task
                        btnApply.disableProperty().bind(task.runningProperty());

                        this.filterProgress.progressProperty().bind(task.progressProperty());
                        task.messageProperty().addListener((w, o, n) -> {
                            txtSummary.clear();
                            txtSummary.appendText(n + "\n");
//                            this.dataList.add(n);

                            this.dataCollectHistory.add(new DataOutputHistory(n, ((FilterTask) task).getTimeMillis()));
                            this.listDataCollected.getSelectionModel().selectLast();

                            HBxPre.getChildren().remove(btnClose);
                        });

                        new Thread(task).start();

                    } catch (Exception ex) {
                        FXOptionPane.showConfirmDialog(this.mainApp.getPrimaryStage(), "Unsuccessful!", "Error");
                    }

                }
            }

        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(this.mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }

    }

    @FXML
    public void handleBtnExecute() {

        try {

            String ConnName = "";

            for (TreeItem item : this.treeCon.getSelectionModel().getSelectedItems()) {
                ConnName = (String) item.getParent().getValue();
            }

            // get the connection object
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(Connections.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Connections datasource = (Connections) jaxbUnmarshaller.unmarshal(new File("src/main/resources/datasource.xml"));

            Datasource d = null;
            for (Datasource dataSrc : datasource.getDatasource()) {
                if (dataSrc.getName().equals(ConnName)) {
                    d = dataSrc;
                    break;
                }
            }

            String token = d.getToken();

            String domainName = d.getDomainName();

            String functionName = (String) d.getRemote().getFunction().get(0);

//            RetriverAndConvertTask task = new RetriverAndConvertTask(token, domainName, functionName);
            tasks = new RetriverAndConvertTask(token, domainName, functionName);

            // preventing duplicate threads
            // Creating a button to cancel the execution of the task
            Button btnClose = new Button("X");

//            btnClose.setStyle("-fx-font: 8 arial; -fx-base: #b6e7c9;");
            btnClose.setStyle("-fx-font: 8 arial;");
            btnClose.setMaxSize(2, 2);

            btnClose.setOnAction(e -> {
//                task.cancelProgress();
                ((RetriverAndConvertTask) tasks).cancelProgress();
            });

            HBxPre.getChildren().addAll(btnClose);

            // to disable the retriever button while is running the task
//            btnExecute.disableProperty().bind(task.runningProperty());
            bindTreeConnectionAndButtonExecute();

            // bind the result obtained from the retriever task
//            filterProgress.progressProperty().bind(task.progressProperty());
            filterProgress.progressProperty().bind(((RetriverAndConvertTask) tasks).progressProperty());

            ((RetriverAndConvertTask) tasks).messageProperty().addListener((w, o, n) -> {
                txtSummary.clear();
                txtSummary.appendText(n + "\n");
//                if (!n.isEmpty()) {
//                    String[] s = n.split("\\s");
//
//                    if (s[0].equals("@relation")) {
//                        this.dataList.add(n);
//                    } else {
//                        this.dataList.clear();
//                    }
//
//                }

                this.dataCollectHistory.add(new DataOutputHistory(n, ((RetriverAndConvertTask) tasks).getTimeMillis()));

                this.listDataCollected.getSelectionModel().selectLast();

                // Removing the button after finishing the process
                HBxPre.getChildren().remove(btnClose);

            });

            new Thread(((RetriverAndConvertTask) tasks)).start();

            System.out.println(">>>>>>>>>>>>>>>>.2 elapsedtime = " + ((RetriverAndConvertTask) tasks).getTimeMillis());

//            btnExecute.disableProperty().unbind();
        } catch (Exception ex) {
            FXOptionPane.showConfirmDialog(this.mainApp.getPrimaryStage(), ex.getMessage(), "Error");
        }
    }

//    @FXML
//    public void handleBtnUndo() {
//        if (dataList.size() > 1) {
//            this.dataList.remove(dataList.size() - 1);
//            this.txtSummary.setText(this.dataList.get(dataList.size() - 1));
//        }
//    }
}
