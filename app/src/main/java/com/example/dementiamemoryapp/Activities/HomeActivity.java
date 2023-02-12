package com.example.dementiamemoryapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dementiamemoryapp.AlertDialog.AlertMoodSliderDialog;
import com.example.dementiamemoryapp.R;
import com.example.dementiamemoryapp.Utility.TTSService;

import Gallery.SelectGalleryType;

public class HomeActivity extends AppCompatActivity {


    RelativeLayout homeLay;
    SharedPreferences sharedpreferences;
    String userId;
    ImageView imgRefresh, imgPopupMenu;
    LinearLayout llMoodAboutMeContainer;
    CardView cardMood, cardLocation,cardGalleryy,cardAboutme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        initUI();
    }

    private void initUI() {
        sharedpreferences = getSharedPreferences("DEMENTIA", MODE_PRIVATE);
        userId = sharedpreferences.getString("Uid", "");

        homeLay = findViewById(R.id.homeLay);
//        imgRefresh = findViewById(R.id.imgRefresh);
        imgPopupMenu = findViewById(R.id.imgPopupMenu);


        llMoodAboutMeContainer = findViewById(R.id.llMoodAboutMeContainer);
        cardMood = findViewById(R.id.cardMood);
        cardLocation = findViewById(R.id.cardlocation);
        cardGalleryy=findViewById(R.id.cardGalleryy);
        cardAboutme=findViewById(R.id.aboutme);



        cardMood.setOnClickListener(v -> onClickCardMood());
        cardLocation.setOnClickListener(v -> onClickCardLocation());
        cardAboutme.setOnClickListener(v -> onClickCardAbout());
        cardGalleryy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCardGallery();
            }
        });

        speakPreviousMoodOrMoodText();

        imgPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImgPopupMenu();
            }
        });
    }



    private void speakPreviousMoodOrMoodText() {
        String textToSpeech;
      String value= sharedpreferences.getString("mood", "");
        if (value.equals("25.0")) {
            textToSpeech = "Your mood is happy";
//            Intent intenthome=new Intent(HomeActivity.this,check.class);
//            startActivity(intenthome);

        } else if (value.equals("50.0")) {
            textToSpeech = "Your mood is sad";
        } else if (value.equals("75.0")) {
            textToSpeech = "Your mood is irritated";
        } else if (value.equals("100.0")) {
            textToSpeech = "Your mood is sleepy";
        } else {
            textToSpeech = "";
        }
        if (!textToSpeech.trim().equals("")) {
            TTSService ttsService = new TTSService(this, textToSpeech);
            ttsService.setTextToSpeech();
        }
    }

    private void onClickImgPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, imgPopupMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
          case R.id.menuAboutMe:
                    Intent aboutmeintent=new Intent(HomeActivity.this,Profile.class);
                    startActivity(aboutmeintent);
                    break;
            }
            return true;
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupMenu.show();
            }
        },100);
    }




    private void onClickCardAbout() {
        Intent intentaboutme=new Intent(HomeActivity.this,displayProfile.class);
        startActivity(intentaboutme);
    }
    private void onClickCardLocation() {

        Intent intentloc=new Intent(HomeActivity.this,location.class);
        startActivity(intentloc);
        finish();

    }

    private void onClickCardMood() {
        AlertMoodSliderDialog alertMoodSliderDialog = new AlertMoodSliderDialog(this, homeLay);
        alertMoodSliderDialog.onAlertCreateView();



    }
    private void onClickCardGallery() {
        Intent intentgallery=new Intent(HomeActivity.this, SelectGalleryType.class);
        startActivity(intentgallery);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentbackpress2=new Intent(Intent.ACTION_MAIN);
        intentbackpress2.addCategory(Intent.CATEGORY_HOME);
        startActivity(intentbackpress2);
    }
}
