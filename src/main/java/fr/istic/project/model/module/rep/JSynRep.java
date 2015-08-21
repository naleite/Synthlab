package fr.istic.project.model.module.rep;

import com.jsyn.unitgen.PassThrough;

import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.view.module.Component;

public class JSynRep extends AbstractModule implements Rep {

    PassThrough passThrough;

    public JSynRep() {
        makeCircuit();
    }

    private void makeCircuit() {
        passThrough = new PassThrough();

        addInput("in", passThrough.input);
        addOutput("out1", passThrough.output);
        addOutput("out2",passThrough.output);
        addOutput("out3",passThrough.output);
        addEntity(passThrough);
    }

    @Override
    public String toString() {
        return Component.REP.toString();
    }
}
