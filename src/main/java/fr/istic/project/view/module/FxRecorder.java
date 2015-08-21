package fr.istic.project.view.module;

import fr.istic.project.model.module.recorder.Recorder;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Switch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FxRecorder extends FxModule<Recorder> {

    @FXML
    private HBox switchBox;
    
    @FXML
    private Button chooseFilepath;
    
    @FXML
    private TextField filepath;

    private Switch switcher;

    public FxRecorder(Recorder model) {
        super(model);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/recorder.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        switcher = new Switch();
        switchBox.getChildren().add(switcher);
    }

    private void createController() {
        getModel().addObserver(PropertyType.RECORDING, (o, arg) -> {

            if (getModel().isRecording()) {
                switcher.switchOn();
            } else {
                switcher.switchOff();
            }

            chooseFilepath.setDisable(getModel().isRecording());
        });

        getModel().addObserver(PropertyType.FILEPATH, (o, arg) -> filepath.setText(getModel().getFilePath()));

        switcher.valueProperty().addListener(event -> {
            if (getModel().isRecording()) {
                try {
                    getModel().stopRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getModel().startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        chooseFilepath.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            // Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(getScene().getWindow());

            if (file != null) {
                try {
                    getModel().setFilePath(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
