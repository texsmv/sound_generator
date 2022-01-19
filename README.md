# Sound Generator

This is a fork from the flutter plugin sound_generator

A wavetype was added to the plugin, named Sinusoidal Frecuency Modulation (FM). More info in the following [link](https://ccrma.stanford.edu/~jos/st/Sinusoidal_Frequency_Modulation_FM.html)

The Sinusoidal FM follows the formula:

$x(t) = A_c cos[w_c t + \phi_c + A_m sin(w_m t + \phi_m)]$

For the sake of simplicity (due to the structure of the original repository), the amplitude will be equal to 1 and the initial phases to 0. Leading to the formula:

$x(t) = cos[w_c t + sin(w_m t)]$

where:

$w_c = f_c 2 \pi$

$w_m = f_m 2 \pi$

If you want to undertand how the code works, you may want to start with the (sinusoidal wave)[https://ccrma.stanford.edu/~jos/mdft/Sinusoids.html]:

A simple example in java for this formula can be found in: https://riptutorial.com/android/example/28432/generate-tone-of-a-specific-frequency , in this example note[0] is the frequency. You can also check https://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android

Although these examples may be useful to understand how to generate a tone with a duration in seconds, this repository generates continually the tones.
Check the comentaries I'm leaving in the file android/src/main/java/io/github/mertguner/sound_generator/generators/signalDataGenerator.java , mainly in the methods updateData and createOneCycleData
