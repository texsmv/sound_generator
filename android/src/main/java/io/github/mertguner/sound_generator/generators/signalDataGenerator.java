package io.github.mertguner.sound_generator.generators;

import java.util.ArrayList;
import java.util.List;

import io.github.mertguner.sound_generator.handlers.getOneCycleDataHandler;

public class signalDataGenerator {


    private final float _2Pi = 2.0f * (float) Math.PI;

    private int sampleRate = 48000;
    private float phCoefficient = _2Pi / (float) sampleRate;
    private float smoothStep = 1f / (float) sampleRate * 20f;

    private float frequency = 50;
    private float modularFrequency = 7.83f;
    private baseGenerator generator = new sinusoidalGenerator();

    private short[] backgroundBuffer;
    private short[] buffer;
    private List<Integer> oneCycleBuffer = new ArrayList<>();
    private int bufferSamplesSize;
    private float ph = 0;
    private float modularPh = 0;
    private float oldFrequency = 50;
    private float oldModularFrequency = 7.83f;
    private boolean creatingNewData = false;
    private boolean autoUpdateOneCycleSample = false;
    private boolean isModular = false;

    public boolean isAutoUpdateOneCycleSample() { return autoUpdateOneCycleSample; }
    public void setAutoUpdateOneCycleSample(boolean autoUpdateOneCycleSample) { this.autoUpdateOneCycleSample = autoUpdateOneCycleSample; }

    public int getSampleRate() { return sampleRate; }
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        phCoefficient = _2Pi / (float) sampleRate;
        smoothStep = 1f / (float) sampleRate * 20f;
    }

    public baseGenerator getGenerator() {
        return generator;
    }
    public void setGenerator(baseGenerator generator, boolean isModular) {
        this.generator = generator;
        this.isModular = isModular;
        createOneCycleData();
    }

    public float getFrequency() {
        return frequency;
    }
    public void setFrequency(float frequency) {
        this.frequency = frequency;
        createOneCycleData();
    }
    public void setModularFrequency(float frequency) {
        this.modularFrequency = frequency;
        createOneCycleData();
    }

    public signalDataGenerator(int bufferSamplesSize, int sampleRate) {
        this.bufferSamplesSize = bufferSamplesSize;
        backgroundBuffer = new short[bufferSamplesSize];
        buffer = new short[bufferSamplesSize];
        setSampleRate(sampleRate);
        updateData();
        createOneCycleData();
    }


    // Generates new data base on the selected generator
    // 
    // It generates the wave values on demand storing them in the 
    // backgroundBuffer. The variables ph and modularPh are basically 
    // the phases of the waves. They are increased until the reach pi
    // and then they are reseted.
    private void updateData() {
        creatingNewData = true;
        for (int i = 0; i < bufferSamplesSize; i++) {
            // This step only smooths the transition after
            // changing the frequencies.
            oldFrequency += ((frequency - oldFrequency) * smoothStep);
            oldModularFrequency += ((modularFrequency - oldModularFrequency) * smoothStep);

            backgroundBuffer[i] = generator.getValue(ph, _2Pi, modularPh);
            ph += (oldFrequency * phCoefficient);
            modularPh += (oldModularFrequency * phCoefficient);

            
            if (ph > _2Pi) {
                ph -= _2Pi;
            }

            if (modularPh > _2Pi) {
                modularPh -= _2Pi;
            }
        }
        creatingNewData = false;
    }

    public short[] getData() {
        if (!creatingNewData) {
            System.arraycopy(backgroundBuffer, 0, buffer, 0, bufferSamplesSize);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateData();
                }
            }).start();
        }
        return this.buffer;
    }

    public void createOneCycleData() {
        createOneCycleData(false);
    }
    

    // Generates data for one cycle base on the frequency
    // 
    // For the case of sinusoidal FM, it checks witch frequency generates
    // a bigger size and uses that size.
    // * This is only used to plot the wave
    public void createOneCycleData(boolean force) {
        if (generator == null || (!autoUpdateOneCycleSample && !force))
            return;

        int size;

        if(isModular){
            int firstSize = Math.round(_2Pi / (frequency * phCoefficient));
            int secondSize = Math.round(_2Pi / (modularFrequency * phCoefficient));
            size = Math.max(firstSize, secondSize);
        }else{
            size = Math.round(_2Pi / (frequency * phCoefficient));
        }

        oneCycleBuffer.clear();
        for (int i = 0; i <= size; i++) {
            oneCycleBuffer.add((int)generator.getValue((frequency * phCoefficient) * (float) i, _2Pi, (modularFrequency * phCoefficient) * (float) i));
        }
        getOneCycleDataHandler.setData(oneCycleBuffer);
    }
}
