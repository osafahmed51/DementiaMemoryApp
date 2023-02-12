package com.example.dementiamemoryapp.Utility;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;

public class TTSService {

    private static final String TAG = "TTSService ";
    Context context;
    TextToSpeech textToSpeech;
    String stringToSpeak;

    public TTSService(Context context, String stringToSpeak) {
        this.context = context;
        this.stringToSpeak = stringToSpeak;
    }

    public void setTextToSpeech() {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(context, "This language is not supported", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v(TAG, "onInit succeeded");
                    speak(stringToSpeak);
                }
            } else {
                Toast.makeText(context, "Initialization failed", Toast.LENGTH_SHORT).show();
            }

        });
        ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    void speak(String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = new Bundle();
            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
            textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, bundle, null);
        } else {
            HashMap<String, String> param = new HashMap<>();
            param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
            textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, param);
        }
    }
}
