package fr.istic.project.view.module;

import fr.istic.project.model.module.noise.WhiteNoise;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FxWhiteNoise extends FxModule<WhiteNoise> {

    public FxWhiteNoise(WhiteNoise model) {
        super(model);
        createView();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/whiteNoise.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
