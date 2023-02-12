package com.example.dementiamemoryapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import com.example.dementiamemoryapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

public class displayProfile extends AppCompatActivity {
    private CircleImageView displayuserimage;
    TextView nameuserr,phoneuserr,emailuserr,ageuserr,occupationuserr,addressuserr;
    DatabaseReference displaydataref;
    TextToSpeech textToSpeechdisplay;
    String name,phone,email,age,occupation,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        displayuserimage=findViewById(R.id.user_image);
        nameuserr=findViewById(R.id.nameuser);
        emailuserr=findViewById(R.id.emailuser);
        phoneuserr=findViewById(R.id.phoneuser);
        ageuserr=findViewById(R.id.ageuser);
        occupationuserr=findViewById(R.id.occupationuser);
        addressuserr=findViewById(R.id.addressuser);
        displaydataref=FirebaseDatabase.getInstance().getReference().child("Profile");

        textToSpeechdisplay=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!= TextToSpeech.ERROR)
                {
                    textToSpeechdisplay.setLanguage(Locale.UK);
                }
            }
        });


        displaydataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Picasso.get().load(snapshot.child("imageuser").getValue().toString()).into(displayuserimage);

                    name=snapshot.child("nameuser").getValue().toString();
                    phone=snapshot.child("phoneuser").getValue().toString();
                    email=snapshot.child("emailuser").getValue().toString();
                    age=snapshot.child("ageuser").getValue().toString();
                    occupation=snapshot.child("occupationuser").getValue().toString();
                    address=snapshot.child("addressuser").getValue().toString();


                    nameuserr.setText("Name : " + name);
                    phoneuserr.setText("Phone : " + phone);
                    emailuserr.setText("Email : " + email);
                    ageuserr.setText("Age : " + age);
                    occupationuserr.setText("Occupation : " + occupation);
                    addressuserr.setText("Address : "+address);

                    textToSpeechdisplay.speak("Your Good Name is "+ name +" Your Contact Number is"+ phone + " and Your Email Address is "+ email +" and You are "+age +" Year Old" + "Your Occupation is "+occupation +"Your Address is "+ address,TextToSpeech.QUEUE_FLUSH,null);



//


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        textToSpeechdisplay.speak("Your Name is "+ name + "Your Address is "+ address ,TextToSpeech.QUEUE_FLUSH,null);
////        textToSpeechdisplay.speak("Your phone number is" + phone,TextToSpeech.QUEUE_FLUSH,null);
////        textToSpeechdisplay.speak("Your Email is "+email, TextToSpeech.QUEUE_FLUSH,null);
////        textToSpeechdisplay.speak("Your Age is "+ age,TextToSpeech.QUEUE_FLUSH,null);
////        textToSpeechdisplay.speak("Your Occupation is "+ occupation,TextToSpeech.QUEUE_FLUSH,null);
////        textToSpeechdisplay.speak("Your Address is "+ address,TextToSpeech.QUEUE_FLUSH,null);
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        textToSpeechdisplay.stop();
    }
}