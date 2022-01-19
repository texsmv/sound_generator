package io.github.mertguner.sound_generator.generators;

public class sinusoidalFMGenerator extends baseGenerator {
    public short getValue(double phase, double period, double secondPhase) {
        return (short) (Short.MAX_VALUE * Math.cos(phase + Math.sin(secondPhase)));
    }
}
