package fr.istic.project.model.module.vcoa;

import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;
import fr.istic.project.model.module.Module;

/**
 * Defines the behavior of a VCOA Module.
 * @inv getOscillatorType() != null
 * @inv the frequency == REF_FREQUENCY * Math.pow(2, getOctave() + getFineTuning() + getVFm())
 * @inv MIN_OCTAVE &lt;= getOctave() &lt;= MAX_OCTAVE
 */
public interface VCOA extends Module {

    double MIN_OCTAVE = 0;
    double MAX_OCTAVE = 8;

    public enum RefFrequencyType {
        NFO(55),
        LFO(1);

        private final double refFrequency;

        RefFrequencyType(double refFrequency) { 
            this.refFrequency = refFrequency; 
        }

        public double getValue() { 
            return  refFrequency; 
        }

        public static RefFrequencyType refFrequencyTypeByName(String name) {
            if (name == null) {
                throw new IllegalArgumentException();
            }
            for (RefFrequencyType ref : RefFrequencyType.values()) {
                if (ref.toString().equals(name)) {
                    return ref;
                }
            }
            return null;
        }
    }

    public enum OscillatorType {
        //constant
        TRIANGLE(TriangleOscillator.class),
        SQUARE(SquareOscillator.class),
        SAWTOOTH(SawtoothOscillator.class);

        private final Class<? extends UnitOscillator> klass;

        OscillatorType(Class<? extends UnitOscillator> klass) {
            this.klass = klass;
        }

        public Class<? extends UnitOscillator> getJSynClass() {
            return klass;
        }

        public static OscillatorType oscillatorTypeByName(String name) {
            if (name == null) {
                throw new IllegalArgumentException();
            }
            for (OscillatorType osc : OscillatorType.values()) {
                if (osc.toString().equals(name)) {
                    return osc;
                }
            }
            return null;
        }
    }

    //request

    /**
     * Returns the Entry value.
     *
     * @return the entry value
     */
    int getOctave();

    /**
     * Returns the fineTuning value.
     *
     * @return the fine tuning value
     */
    double getFineTuning();

    /**
     * Returns the oscillator type.
     *
     * @return the oscillator type
     */
    OscillatorType getOscillatorType();

    /**
     * Returns the reference frequency.
     *
     * @return the actual reference frequency
     */
    RefFrequencyType getRefFrequency();

    /**
     * Returns the current frequency.
     *
     * @return the actual frequency
     */
    double getCurrentFrequency();

    //command

    /**
     * Sets the octave value.
     *
     * @param octave the new value.
     * @pre MIN_OCTAVE &lt;= octave &lt;= MAX_OCTAVE
     * @post getOctave() == octave
     */
    void setOctave(int octave);

    /**
     * Sets the fineTuning value.
     *
     * @param fineTuning the new value.
     * @pre -1 &lt;= fineTuning &lt;= 1
     * @post getFineTuning() == fineTuning
     */
    void setFineTuning(double fineTuning);

    /**
     * Sets the oscillator type.
     *
     * @param o the new type.
     * @post getOscillatorType() == o
     */
    void setOscillatorType(OscillatorType o);

    /**
     * Set the reference frequency.
     *
     * @param refFrequency the new value.
     * @post getValue() == frequency
     */
    void setRefFrequency(RefFrequencyType refFrequency);
}
