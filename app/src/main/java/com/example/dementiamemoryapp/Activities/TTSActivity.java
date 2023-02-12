package com.example.dementiamemoryapp.Activities;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dementiamemoryapp.R;

import java.util.HashMap;
import java.util.Locale;

public class TTSActivity extends AppCompatActivity {

    TextView txtHeading, txtDescription;
    ImageView img;
    TextToSpeech textToSpeech;
    final String TAG = "RESPONSE:-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_t_s);
        getSupportActionBar().hide();
        initUI();
    }

    private void initUI() {

        txtHeading = findViewById(R.id.txtHeading);
        txtDescription = findViewById(R.id.txtDescription);
        img = findViewById(R.id.img);

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "This language is not supported", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v(TAG, "onInit succeeded");

                }
            } else {
                Toast.makeText(getApplicationContext(), "Initialization failed", Toast.LENGTH_SHORT).show();
            }

        });
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    void speak(String s){
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