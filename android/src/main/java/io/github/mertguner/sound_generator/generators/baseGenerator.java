package io.github.mertguner.sound_generator.generators;

public abstract class baseGenerator {
    // secondPhase is only used for the sinusoidal FM generator
    public abstract short getValue(double phase, double period, double secondPhase);
}
