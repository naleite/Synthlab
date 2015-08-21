package fr.istic.project.model.module.noise;

import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.view.module.Component;

public class JSynWhiteNoise extends AbstractModule implements WhiteNoise {

    private com.jsyn.unitgen.WhiteNoise whiteNoise;

    public JSynWhiteNoise() {
        whiteNoise = new com.jsyn.unitgen.WhiteNoise();
        whiteNoise.amplitude.set(MAX_VOLTAGE);
        makeCircuit();
    }

    @Override
    public String toString() {
        return Component.WHITENOISE.toString();
    }

    private void makeCircuit() {
        addEntity(whiteNoise);
        addOutput("out", whiteNoise.output);
    }
}
