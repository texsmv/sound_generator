import 'dart:async';

import 'package:flutter/services.dart';
import 'package:sound_generator/waveTypes.dart';

class SoundGenerator {
  static const MethodChannel _channel = const MethodChannel('sound_generator');
  static const EventChannel _onChangeIsPlaying = const EventChannel(
      'io.github.mertguner.sound_generator/onChangeIsPlaying');
  static const EventChannel _onOneCycleDataHandler = const EventChannel(
      'io.github.mertguner.sound_generator/onOneCycleDataHandler');

  /// is Playing data changed event
  static bool _onIsPlayingChangedInitialized = false;
  static late Stream<bool> _onIsPlayingChanged;
  static Stream<bool> get onIsPlayingChanged {
    if (!_onIsPlayingChangedInitialized) {
      _onIsPlayingChanged = _onChangeIsPlaying
          .receiveBroadcastStream()
          .map<bool>((value) => value);

      _onIsPlayingChangedInitialized = true;
    }

    return _onIsPlayingChanged;
  }

  /// One cycle data changed event
  static bool _onGetOneCycleDataHandlerInitialized = false;
  static late Stream<List<int>> _onGetOneCycleDataHandler;
  static Stream<List<int>> get onOneCycleDataHandler {
    if (!_onGetOneCycleDataHandlerInitialized) {
      _onGetOneCycleDataHandler = _onOneCycleDataHandler
          .receiveBroadcastStream()
          .map<List<int>>((value) => new List<int>.from(value));
      _onGetOneCycleDataHandlerInitialized = true;
    }

    return _onGetOneCycleDataHandler;
  }

  /// init function
  static Future<bool> init(int sampleRate) async {
    final bool init = await _channel
        .invokeMethod("init", <String, dynamic>{"sampleRate": sampleRate});
    return init;
  }

  /// Play sound
  static void play() async {
    await _channel.invokeMethod('play');
  }

  /// Stop playing sound
  static void stop() async {
    await _channel.invokeMethod('stop');
  }

  /// Release all data
  static void release() async {
    await _channel.invokeMethod('release');
  }

  /// Refresh One Cycle Data
  static void refreshOneCycleData() async {
    await _channel.invokeMethod('refreshOneCycleData');
  }

  /// Get is Playing data
  static Future<bool> get isPlaying async {
    final bool playing = await _channel.invokeMethod('isPlaying');
    return playing;
  }

  /// Get SampleRate
  static Future<int> get getSampleRate async {
    final int sampleRate = await _channel.invokeMethod('getSampleRate');
    return sampleRate;
  }

  /// Set AutoUpdateOneCycleSample
  static void setAutoUpdateOneCycleSample(bool autoUpdateOneCycleSample) async {
    await _channel.invokeMethod(
        "setAutoUpdateOneCycleSample", <String, dynamic>{
      "autoUpdateOneCycleSample": autoUpdateOneCycleSample
    });
  }

  /// Set Frequency
  static void setFrequency(double frequency) async {
    await _channel.invokeMethod(
        "setFrequency", <String, dynamic>{"frequency": frequency});
  }

  /// Set second Frequency
  static void setModularFrequency(double frequency) async {
    await _channel.invokeMethod("setModularFrequency",
        <String, dynamic>{"modularFrequency": frequency});
  }

  /// Set Balance Range from -1 to 1
  static void setBalance(double balance) async {
    await _channel
        .invokeMethod("setBalance", <String, dynamic>{"balance": balance});
  }

  /// Set WaveType
  static void setWaveType(waveTypes waveType) async {
    await _channel.invokeMethod("setWaveform", <String, dynamic>{
      "waveType": waveType.toString().replaceAll("waveTypes.", "")
    });
  }

  /// Set Volume Range from 0 to 1
  static void setVolume(double volume) async {
    await _channel
        .invokeMethod("setVolume", <String, dynamic>{"volume": volume});
  }
}
