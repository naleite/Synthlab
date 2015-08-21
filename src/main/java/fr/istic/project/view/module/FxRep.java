package fr.istic.project.view.module;

import fr.istic.project.model.module.rep.Rep;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FxRep extends FxModule<Rep> {

    public FxRep(Rep model) {
        super(model);
        createView();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/rep.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
