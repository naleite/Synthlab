package fr.istic.project.view.module;

import fr.istic.project.model.Board;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.module.eg.JSynEG;
import fr.istic.project.model.module.keyboard.JSynKeyboard;
import fr.istic.project.model.module.mix.JSynMix;
import fr.istic.project.model.module.noise.JSynWhiteNoise;
import fr.istic.project.model.module.osc.JSynOscilloscope;
import fr.istic.project.model.module.out.JSynOut;
import fr.istic.project.model.module.player.JSynPlayer;
import fr.istic.project.model.module.recorder.JSynRecorder;
import fr.istic.project.model.module.rep.JSynRep;
import fr.istic.project.model.module.seq.JSynSeq;
import fr.istic.project.model.module.vca.JSynVCA;
import fr.istic.project.model.module.vcfa.JSynVCFA;
import fr.istic.project.model.module.vcfb.JSynVCFB;
import fr.istic.project.model.module.vcoa.JSynVCOA;

public enum Component {
    EG("EG", JSynEG.class, (board, id) -> new FxEG(board.getModule(JSynEG.class, id))),
    KEYBOARD("Keyboard", JSynKeyboard.class, (board, id) -> new FxKeyboard(board.getModule(JSynKeyboard.class, id))),
    MIX("Mix", JSynMix.class, (board, id) -> new FxMix(board.getModule(JSynMix.class, id))),
    OSC("Oscilloscope", JSynOscilloscope.class, (board, id) -> new FxOscilloscope(board.getModule(JSynOscilloscope.class, id))),
    OUT("Out", JSynOut.class, (board, id) -> new FxOut(board.getModule(JSynOut.class, id))),
    PLAYER("Player", JSynPlayer.class, (board, id) -> new FxPlayer(board.getModule(JSynPlayer.class, id))),
    RECORDER("Recorder", JSynRecorder.class, (board, id) -> new FxRecorder(board.getModule(JSynRecorder.class, id))),
    REP("Replicator", JSynRep.class, (board, id) -> new FxRep(board.getModule(JSynRep.class, id))),
    SEQ("Sequencer", JSynSeq.class, (board, id) -> new FxSequencer(board.getModule(JSynSeq.class, id))),
    VCA("VCA", JSynVCA.class, (board, id) -> new FxVCA(board.getModule(JSynVCA.class, id))),
    VCFA("VCFA", JSynVCFA.class, (board, id) -> new FxVCFA(board.getModule(JSynVCFA.class, id))),
    VCFB("VCFB", JSynVCFB.class, (board, id) -> new FxVCFB(board.getModule(JSynVCFB.class, id))),
    VCOA("VCOA", JSynVCOA.class, (board, id) -> new FxVCOA(board.getModule(JSynVCOA.class, id))),
    WHITENOISE("WhiteNoise", JSynWhiteNoise.class, (board, id) -> new FxWhiteNoise(board.getModule(JSynWhiteNoise.class, id)));

    /**
     * Builder interface
     */
    interface ComponentBuilder {
        FxModule build(Board board, long id);
    }

    /**
     * The component identifier.
     */
    private final String id;

    /**
     * The component builder.
     */
    private final ComponentBuilder builder;

    /**
     * The component class representation.
     */
    private final Class<? extends Module> klass;

    /**
     * Constructor
     * @param id the component identifier
     * @param klass the component class representation
     * @param builder the component builder
     */
    Component(String id, Class<? extends Module> klass, ComponentBuilder builder) {
        this.id = id;
        this.builder = builder;
        this.klass = klass;
    }

    @Override
    public String toString() {
        return id;
    }

    public Class<? extends Module> getKlass() {
        return klass;
    }

    public static FxModule build( Class<? extends Module> klass, Board board, long id) {
        if (board == null || klass == null) {
            throw new IllegalArgumentException();
        }
        for (Component c : Component.values()) {
            if (c.getKlass().equals(klass)) {
                return c.build(board, id);
            }
        }
        return null;
    }

    public FxModule build(Board board, long id) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        return builder.build(board, id);
    }

    public static Component getBySimpleName(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }
        for (Component mt : Component.values()) {
            if (mt.getKlass().getSimpleName().equals(str)) {
                return mt;
            }
        }
        return null;
    }
}
