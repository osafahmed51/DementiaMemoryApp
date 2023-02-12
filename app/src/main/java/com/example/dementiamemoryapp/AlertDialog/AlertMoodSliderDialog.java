package com.example.dementiamemoryapp.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.dementiamemoryapp.R;
import com.example.dementiamemoryapp.Utility.TTSService;
import com.google.android.material.slider.Slider;

import static android.content.Context.MODE_PRIVATE;

public class AlertMoodSliderDialog {

    Context context;
    RelativeLayout homeLay;

    ImageView ivCloseAlertDialog;
    public Button btnSubmit;
    Slider sliderTrackMood;

    AlertDialog alert;
    View alertView;
    VideoView videoView;
    String vpath;
    CardView cardViewalert;

    private SharedPreferences sharedpreferences;

    String sliderValue;
    public String check="";

    public AlertMoodSliderDialog(Context context, RelativeLayout homeLay) {
        this.context = context;
        this.homeLay = homeLay;

    }

    public void onAlertCreateView() {
//      check="checked";
        alertView = ((Activity) context).getLayoutInflater().inflate(R.layout.alert_mood, null);

        showAlertDialog();
        initUI();
        initOBJ();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (alertView.getParent() != null) {
            ((ViewGroup) alertView.getParent()).removeView(alertView);
        }
        builder.setView(alertView);
        alert = builder.show();
    }

    private void initOBJ() {
        sharedpreferences = context.getSharedPreferences("DEMENTIA", MODE_PRIVATE);
        String mood = sharedpreferences.getString("mood", "");
        if (!mood.trim().equals("")) {
            Log.d("AlertMoodSliderDialog ", mood);
            try {
                sliderTrackMood.setValue(Float.parseFloat(mood));
            } catch (Exception exception) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initUI() {

        btnSubmit = alertView.findViewById(R.id.btnSubmit);
        ivCloseAlertDialog = alertView.findViewById(R.id.ivCloseAlertDialog);

        sliderTrackMood = alertView.findViewById(R.id.sliderTrackMood);
        videoView=alertView.findViewById(R.id.videov1);
        cardViewalert=alertView.findViewById(R.id.cardvalertmood);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnSubmit();
            }
        });

        ivCloseAlertDialog.setOnClickListener(v -> alert.dismiss());
        eventOnTouchSlider();
    }

    public void onClickBtnSubmit() {

        if (!sliderValue.trim().equals("")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("mood", sliderValue);
            editor.apply();

            sharedpreferences = context.getSharedPreferences("DEMENTIA", MODE_PRIVATE);
            String mood1 = sharedpreferences.getString("mood", "");
//            if(mood1.equals("25.0"))
//            {
//
//
//            }


            speakMoodOrMoodtext();


        }
//        alert.dismiss();




    }

    private void speakMoodOrMoodtext() {
        String textToSpeech;
        if (sliderValue.equals("25.0")) {
            textToSpeech = "Your mood is happy";

            cardViewalert.setVisibility(View.GONE);

            videoView.setVisibility(View.VISIBLE);
            vpath="android.resource://"+ context.getPackageName()+"/raw/happy";
            Uri urivideo1=Uri.parse(vpath);
            videoView.setVideoURI(urivideo1);
            videoView.start();

        } else if (sliderValue.equals("50.0")) {
            textToSpeech = "Your mood is sad";
            cardViewalert.setVisibility(View.GONE);

            videoView.setVisibility(View.VISIBLE);
            vpath="android.resource://"+ context.getPackageName()+"/raw/sad";
            Uri urivideo1=Uri.parse(vpath);
            videoView.setVideoURI(urivideo1);
            videoView.start();
        } else if (sliderValue.equals("75.0")) {
            textToSpeech = "Your mood is irritated";
            cardViewalert.setVisibility(View.GONE);

            videoView.setVisibility(View.VISIBLE);
            vpath="android.resource://"+ context.getPackageName()+"/raw/angry";
            Uri urivideo1=Uri.parse(vpath);
            videoView.setVideoURI(urivideo1);
            videoView.start();

        } else if (sliderValue.equals("100.0")) {
            textToSpeech = "Your mood is sleepy";
            cardViewalert.setVisibility(View.GONE);

            videoView.setVisibility(View.VISIBLE);
            vpath="android.resource://"+ context.getPackageName()+"/raw/sleepy";
            Uri urivideo1=Uri.parse(vpath);
            videoView.setVideoURI(urivideo1);
            videoView.start();
        } else {
            textToSpeech = "";
        }
        if (!textToSpeech.trim().equals("")) {
            TTSService ttsService = new TTSService(context, textToSpeech);
            ttsService.setTextToSpeech();
        }
    }

    private void eventOnTouchSlider() {
        sliderTrackMood.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                slider.setLabelFormatter(value -> {
                    if (value == 25.0) {
                        return "Happy";
                    } else if (value == 50.0) {
                        return "Sad";
                    } else if (value == 75.0) {
                        return "Irritated";
                    } else if (value == 100.0) {
                        return "Sleepy";
                    } else {
                        return "Set mood";
                    }
                });

                sliderValue = "";
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                sliderValue = String.valueOf(slider.getValue());
            }
        });
    }

}
