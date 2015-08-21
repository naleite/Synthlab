package fr.istic.project.model.module.eg;

import fr.istic.project.model.module.Module;

public interface EG extends Module {

    /**
     * Returns the attack value.
     *
     * @return the attack value.
     */
    double getAttack();

    /**
     * Returns the sustain value.
     *
     * @return the sustain value.
     */
    double getSustain();

    /**
     * Returns the release value.
     *
     * @return the release value.
     */
    double getRelease();

    /**
     * Returns the decay value.
     *
     * @return the decay value.
     */
    double getDecay();

    /**
     * Sets the attack value.
     *
     * @param attack the new attack value.
     * @post getAttack() == attack
     */
    void setAttack(double attack);

    /**
     * Sets the sustain value.
     *
     * @param sustain the new sustain value.
     * @post getSustain() == sustain
     */
    void setSustain(double sustain);

    /**
     * Sets the release value.
     *
     * @param release the new release value.
     * @post getRelease() == release
     */
    void setRelease(double release);

    /**
     * Sets the decay value.
     *
     * @param decay the new decay value.
     * @post getDecay() == decay
     */
    void setDecay(double decay);
}
