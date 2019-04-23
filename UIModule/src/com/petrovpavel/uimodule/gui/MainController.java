package com.petrovpavel.uimodule.gui;

import com.petrovpavel.uimodule.BrokerStarter;
import com.petrovpavel.uimodule.TaskResultWaiter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.petrovpavel.datamodule.core.DataManager;
import com.petrovpavel.datamodule.entity.Route;
import com.petrovpavel.datamodule.entity.RouteData;
import com.petrovpavel.distributionmodule.core.RouteBuilder;
import com.petrovpavel.distributionmodule.core.RouteBuilderBase;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.petrovpavel.datamodule.core.DataManager.readDataFile;
import static com.petrovpavel.datamodule.core.DataManager.writeRouteFile;
import static com.petrovpavel.datamodule.utils.MathUtils.genAdjMatrix;

public class MainController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private AnchorPane adjMatrixPane;

    @FXML
    private Button saveMatrixBtn;

    @FXML
    private Button loadMatrixBtn;

    @FXML
    private Button genMatrixBtn;

    @FXML
    private TextField randOriginField;

    @FXML
    private TextField randBoundField;

    @FXML
    private ToggleButton genMatrixRandToggle;

    @FXML
    private TextField genMatrixSizeField;

    @FXML
    private TextField peerDescPathField;

    @FXML
    private TextField brokerPathField;

    @FXML
    private Button peerDescPathBtn;

    @FXML
    private Button brokerPathBtn;

    @FXML
    private TextField jarFilePathField;

    @FXML
    private TextField remoteCommandField;

    @FXML
    private TextField jobNameField;

    @FXML
    private Button jarFilePathBtn;

    @FXML
    private TextField taskSizeField;

    @FXML
    private TextField genDataFileToField;

    @FXML
    private Button genDataFilePathBtn;

    @FXML
    private TextField genTasksFilesToField;

    @FXML
    private Button genTasksFilesPathBtn;

    @FXML
    private TextField genJobFileToField;

    @FXML
    private Button genJobFileToBtn;

    @FXML
    private TextField dataFileNameField;

    @FXML
    private TextField taskFileNameField;

    @FXML
    private TextField taskResFileNameField;

    @FXML
    private TextField filesExtensionField;

    @FXML
    private TextField checkResultTimeoutField;

    @FXML
    private TextField routeLengthField;

    @FXML
    private Button startButton;

    @FXML
    private ProgressBar infinityProgressBar;

    @FXML
    private Label resultLabel;


    private RouteBuilder routeBuilder;

    private MatrixView matrixView;

    private TaskResultWaiter taskResultWaiter;

    private BigInteger tasksCount;

    private BigInteger taskSize;

    private boolean startToggle = false;

    private static String minRouteResultString = "null";





    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initInputFields();

        taskSizeField.textProperty().addListener(getFieldChecker(taskSizeField));
        checkResultTimeoutField.textProperty().addListener(getFieldChecker(checkResultTimeoutField));
        routeLengthField.textProperty().addListener(getFieldChecker(routeLengthField));
        randOriginField.textProperty().addListener(getFieldChecker(randOriginField));
        randBoundField.textProperty().addListener(getFieldChecker(randBoundField));
        genMatrixSizeField.textProperty().addListener(getFieldChecker(genMatrixSizeField));


        brokerPathBtn.setOnAction(event -> {
            File broker = selectFile("Select broker!");
            if (broker != null) {
                brokerPathField.setText(broker.getPath());
            }
        });

        peerDescPathBtn.setOnAction(event -> {
            File peerDesc = selectFile(
                    "Select peer description file!",
                    "Peer description file (*.gdf)",
                    "*.gdf"
            );
            if (peerDesc != null) {
                peerDescPathField.setText(peerDesc.getPath());
            }
        });

        loadMatrixBtn.setOnAction(event -> {
            File data = selectFile(
                    "Select adjacency matrix!",
                    "Data files (*.pp)",
                    "*.pp"
            );
            if (data != null) {
                RouteData routeData = null;
                try {
                    routeData = readDataFile(Paths.get(data.getPath()));
                } catch (IOException e) {}
                this.routeLengthField.setText(String.valueOf(routeData.getRouteLength()));
                initMatrixView(routeData.getAdjMatrix());
            }
        });

        saveMatrixBtn.setOnAction(event -> {
            if (matrixView != null) {
                File folder = selectFolder("Select a folder to save the matrix!");
                if (folder != null) {
                    try {
                        writeDataFile(new RouteData(
                                matrixView.getAdjMatrix(),
                                Integer.parseInt(routeLengthField.getText())
                        ), Paths.get(folder.getPath()));
                    } catch (IOException e) {}
                }
            }
        });

        genMatrixRandToggle.setOnAction(event -> {
            if (genMatrixRandToggle.isSelected()) {
                randOriginField.setDisable(false);
                randBoundField.setDisable(false);
            } else {
                randOriginField.setDisable(true);
                randBoundField.setDisable(true);
            }
        });

        genMatrixBtn.setOnAction(event -> {
            int[][] adjMatrix;

            if (genMatrixRandToggle.isSelected()) {
                adjMatrix = genAdjMatrix(
                        Integer.parseInt(genMatrixSizeField.getText()),
                        Integer.parseInt(randOriginField.getText()),
                        Integer.parseInt(randBoundField.getText())
                );
            } else {
                adjMatrix = genAdjMatrix(
                        Integer.parseInt(genMatrixSizeField.getText())
                );
            }
            initMatrixView(adjMatrix);
        });

        genDataFilePathBtn.setOnAction(event -> {
            File file = selectFolder("Choose a place to save the data");
            if (file != null) {
                genDataFileToField.setText(file.getPath());
            }
        });

        genTasksFilesPathBtn.setOnAction(event -> {
            File file = selectFolder("Choose a place to save tasks");
            if (file != null) {
                genTasksFilesToField.setText(file.getPath());
            }
        });

        genJobFileToBtn.setOnAction(event -> {
            File file = selectFolder("Choose a place to save the job");
            if (file != null) {
                genJobFileToField.setText(file.getPath());
            }
        });

        jarFilePathBtn.setOnAction(event -> {
            File file = selectFile(
                    "Select jar file!",
                    "Jar file (*.jar)",
                    "*.jar");
            if (file != null) {
                jarFilePathField.setText(file.getPath());
            }
        });


        startButton.setOnAction(event -> {
            try {
                if (matrixView != null) {
                    startToggle();
                    initInputValues();
                    Path dataPath = writeDataFile(
                            routeBuilder.getRouteData(),
                            Paths.get(genDataFileToField.getText())
                    );

                    Path jobPath = writeJobAndTaskFiles(dataPath);

                    startBroker(jobPath);

                    setupResultWaiter();
                }
            }
            catch (Exception e) {}
        });
    }


    private File selectFile(String title, String extDesc, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                extDesc,
                extensions
        );
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(null);
    }

    private File selectFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(null);
    }

    private File selectFolder(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(null);
    }


    private void initRouteBuilder() {
        routeBuilder = new RouteBuilderBase(
                matrixView.getAdjMatrix(),
                Integer.parseInt(routeLengthField.getText())
        );
        tasksCount = routeBuilder.getTaskCount(taskSize);
    }

    private void initMatrixView(int[][] adjMatrix) {
        this.matrixView = new MatrixView(adjMatrix);
        AnchorPane.setLeftAnchor(matrixView, 0d);
        this.matrixView.setPrefHeight(adjMatrixPane.getHeight());
        this.matrixView.setPrefWidth(adjMatrixPane.getWidth());
        AnchorPane.setTopAnchor(matrixView, 0d);
        this.adjMatrixPane.getChildren().clear();
        this.adjMatrixPane.getChildren().add(matrixView);
        initMatrixViewResize();
    }

    private void initMatrixViewResize() {
        initTableResize(adjMatrixPane, matrixView);
    }




    private void initTableResize(AnchorPane pane, TableView tableView) {
        Platform.runLater(() -> {

            pane.widthProperty().addListener((observable, oldValue, newValue) -> {
                double newV = (double) newValue;
                tableView.setPrefWidth(newV);
                tableView.setMinWidth(newV);
                tableView.setMinWidth(newV);
            });
            pane.heightProperty().addListener((observable, oldValue, newValue) -> {
                double newV = (double) newValue;
                tableView.setPrefHeight(newV);
                tableView.setMinHeight(newV);
                tableView.setMaxHeight(newV);
            });
        });
    }

    private void initInputFields() {
        dataFileNameField.setText(DataManager.DATA_FILE_NAME);
        taskFileNameField.setText(DataManager.TASK_FILE_NAME);
        taskResFileNameField.setText(DataManager.TASK_RESULT_NAME);
        filesExtensionField.setText(DataManager.FILE_EXTENSION);
        remoteCommandField.setTooltip(new Tooltip("Command example:\n" +
                "java -jar $JAR $DATA $TASK > $RESULT\n" +
                "$JAR - jar file\n" +
                "$DATA - data file\n" +
                "$TASK - task file\n" +
                "$RESULT – result file\n")
        );
        if (System.getProperty("os.name").contains("Windows")) {
            genDataFileToField.setPromptText("C:\\Data\\");
            genTasksFilesToField.setPromptText("C:\\Tasks\\");
            genJobFileToField.setPromptText("C:\\Jobs\\");
            jarFilePathField.setPromptText("C:\\Programs\\program.jar");
            brokerPathField.setPromptText("C:\\mygrid");
            peerDescPathField.setPromptText("C:\\setPeer.gdf");
        } else {
            genDataFileToField.setPromptText("/home/user/data/");
            genTasksFilesToField.setPromptText("/home/user/tasks/");
            genJobFileToField.setPromptText("/home/user/jobs/");
            jarFilePathField.setPromptText("/home/user/programs/program.jar");
            brokerPathField.setPromptText("/home/user/mygrid");
            peerDescPathField.setPromptText("/home/user/setPeer.gdf");
        }
    }

    private void initInputValues() {
        DataManager.DATA_FILE_NAME = dataFileNameField.getText();
        DataManager.TASK_FILE_NAME = taskFileNameField.getText();
        this.taskSize = new BigInteger(taskSizeField.getText());
        initRouteBuilder();
    }




    private Path writeDataFile(RouteData routeData, Path folderPath) throws IOException {
        Path dataPath = DataManager.writeDataFile(routeData, folderPath);
        return dataPath;
    }

    private Path writeJobAndTaskFiles(Path dataFilePath) throws IOException {
        Path jobPath = routeBuilder.writeJobAndTaskFiles(
                taskSize,
                jobNameField.getText(),
                Paths.get(jarFilePathField.getText()),
                dataFilePath,
                Paths.get(genTasksFilesToField.getText()),
                Paths.get(genJobFileToField.getText()),
                remoteCommandField.getText()
        );
        return jobPath;
    }



    private void startBroker(Path jobPath) throws Exception {

        BrokerStarter brokerStarter = new BrokerStarter();

        brokerStarter.setCommandOutputListener(new BrokerStarter.Callback() {
            @Override
            public void onOutputLineRead(String outputLine) {}

            @Override
            public void onJobDone() {
                try {
                    Route minRoute = routeBuilder.readTaskResultFiles(
                            tasksCount, Paths.get(genJobFileToField.getText()),
                            taskResult -> {});
                    minRouteResultString = minRoute.toString();
                    updaterouteLabel();
                    startToggle();
                } catch (IOException e) {resultLabel.setText(e.getMessage());}
            }

            @Override
            public void onException(Exception e) {}
        });

        brokerStarter.startAndRunJob(
                Paths.get(brokerPathField.getText()),
                Paths.get(peerDescPathField.getText()),
                jobPath
        );

    }

    private void updaterouteLabel() {
        resultLabel.setText("Min route -> " + minRouteResultString);
    }

    private void setupResultWaiter() throws IOException {

        taskResultWaiter = new TaskResultWaiter(
                genJobFileToField.getText(),
                tasksCount
        );

        taskResultWaiter.start(new TaskResultWaiter.Callback() {
            @Override
            public void onTimeoutTick(BigInteger resultsCount) {}

            @Override
            public void onAllResultsExist() {}

            @Override
            public void onException(Exception e) {}

        }, Integer.parseInt(checkResultTimeoutField.getText()));
    }


    private void startToggle() {
        Platform.runLater(() -> {
            if (startToggle) {
                startToggle = false;
                startButton.setDisable(false);
                startButton.setText("Start");
                startButton.setStyle("-fx-background-color: F39C63");
                infinityProgressBar.setVisible(false);
                if (taskResultWaiter != null) {
                    taskResultWaiter.stop();
                }
            } else {
                startToggle = true;
                startButton.setDisable(true);
                startButton.setText("Wait...");
                startButton.setStyle("-fx-background-color: red");
                infinityProgressBar.setVisible(true);
            }
        });
    }

    private ChangeListener<String> getFieldChecker(TextField textField) {
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        };
    }

}
