package com.example.dementiamemoryapp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dementiamemoryapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import Gallery.AddDetailsOtherEvents;

public class Profile extends AppCompatActivity {
    private ImageView profileImageV;
    private TextView addImageProfile;
    private EditText nameedittxt,phoneedittext,emailedittext,addressedittext,ageedittext,occupedittext;
    private Button submitedittext;
    private DatabaseReference profileref;
    private StorageReference profileimageref;
    private Uri imageuri1;
    AlertDialog.Builder builder;
    private ProgressBar progressBarprofile;
    private String downloadurl11;
    private String namee,emailee,phonee,adresse,ageee,occupp;
    private Bitmap photo;
    Uri phototouri;
    private String checker="";
    ActivityResultLauncher<Intent> activityResultLauncher1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImageV=findViewById(R.id.profile_image);
        addImageProfile=findViewById(R.id.addimgprofile);
        nameedittxt=findViewById(R.id.nameedtext);
        phoneedittext=findViewById(R.id.phoneedtext);
        emailedittext=findViewById(R.id.emailedtext);
        addressedittext=findViewById(R.id.addressedtext);
        submitedittext=findViewById(R.id.buttonprofile);
        builder=new AlertDialog.Builder(this);
        progressBarprofile=findViewById(R.id.progressBar_cyclic111);
        ageedittext=findViewById(R.id.ageedtext);
        occupedittext=findViewById(R.id.occupationedtext);


        profileref= FirebaseDatabase.getInstance().getReference().child("Profile");
        profileimageref= FirebaseStorage.getInstance().getReference().child("Profile");



        addImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items={
                        "Camera",
                        "Gallery"

                };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                openGallery();
                                break;

                        }
                    }
                });

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Select Action");
                alert.show();
            }
        });



        submitedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 namee=nameedittxt.getText().toString();
                 emailee=emailedittext.getText().toString();
                 phonee=phoneedittext.getText().toString();
                 adresse=addressedittext.getText().toString();
                 ageee=ageedittext.getText().toString();
                 occupp=occupedittext.getText().toString();

                if(!(namee.isEmpty())  || !(emailee.isEmpty()) || !(phonee.isEmpty()) || !(adresse.isEmpty()) || !(ageee.isEmpty()) || !(occupp.isEmpty()))
                {
                    progressBarprofile.setVisibility(View.VISIBLE);
                    storedata();
                }
                else
                {
                    Toast.makeText(Profile.this, "Make sure to fill all the feilds!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        activityResultLauncher1=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle extras=result.getData().getExtras();
                Bitmap imagebitmap=(Bitmap) extras.get("data");

                WeakReference<Bitmap> result1=new WeakReference<>(Bitmap.createScaledBitmap(imagebitmap,
                        imagebitmap.getHeight(),imagebitmap.getWidth(),false).copy(
                        Bitmap.Config.RGB_565,true
                ));

                Bitmap bm=result1.get();
                imageuri1=saveimage(bm,Profile.this);
                profileImageV.setImageURI(imageuri1);
            }
        });

    }

    private Uri saveimage(Bitmap bm, Profile profile) {
        File imagesfolder=new File(profile.getCacheDir(),"images");
        Uri uriii=null;
        try {
            imagesfolder.mkdir();
            File file=new File(imagesfolder,"captured_image.jpg");
            FileOutputStream stream=new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            uriii= FileProvider.getUriForFile(profile.getApplicationContext(),"com.example.dementiamemoryapp"+".provider",file);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uriii;
    }

    private void storedata() {

       if(imageuri1!=null)
       {
           StorageReference imgpath=profileimageref.child(imageuri1.getLastPathSegment()+".jpg" );
           final UploadTask uploadTask11=imgpath.putFile(imageuri1);
           uploadTask11.addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(Profile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Task<Uri> uriTask11=uploadTask11.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                       @Override
                       public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                           if(!(task.isSuccessful()))
                           {
                               throw  task.getException();
                           }
                           progressBarprofile.setVisibility(View.INVISIBLE);
                           downloadurl11=imgpath.getDownloadUrl().toString();
                           return imgpath.getDownloadUrl();
                       }
                   }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                       @Override
                       public void onComplete(@NonNull Task<Uri> task) {
                           if(task.isSuccessful())
                           {


                               profileref.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                                      if((snapshot.exists()))
                                      {
                                          downloadurl11=task.getResult().toString();
                                          HashMap<String , Object> profilemap=new HashMap<>();
                                          profilemap.put("nameuser",namee);
                                          profilemap.put("emailuser",emailee);
                                          profilemap.put("phoneuser",phonee);
                                          profilemap.put("addressuser",adresse);
                                          profilemap.put("imageuser",downloadurl11);
                                          profilemap.put("ageuser",ageee);
                                          profilemap.put("occupationuser",occupp);

                                          profileref.updateChildren(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if(task.isSuccessful())
                                                  {
                                                      Toast.makeText(Profile.this, "User Details added successfuly", Toast.LENGTH_SHORT).show();
//                                                      progressBarprofile.setVisibility(View.GONE);
//                                                      nameedittxt.setVisibility(View.GONE);
//                                                      emailedittext.setVisibility(View.GONE);
//                                                      phoneedittext.setVisibility(View.GONE);
//                                                      addressedittext.setVisibility(View.GONE);
//                                                      ageedittext.setVisibility(View.GONE);
//                                                      occupedittext.setVisibility(View.GONE);

                                                  }
                                                  else
                                                  {
                                                      progressBarprofile.setVisibility(View.GONE);
                                                      Toast.makeText(Profile.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          });
                                      }

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });



                           }
                           else{
                               progressBarprofile.setVisibility(View.GONE);
                               Toast.makeText(Profile.this, "Path not downloaded", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           });
       }
       else
       {
           progressBarprofile.setVisibility(View.GONE);
           Toast.makeText(this, "Image is not uploaded", Toast.LENGTH_SHORT).show();
       }

    }

    private void openGallery() {
        Intent intentgallerypick=new Intent();
        intentgallerypick.setAction(Intent.ACTION_GET_CONTENT);
        intentgallerypick.setType("image/*");
        startActivityForResult(intentgallerypick,1);


    }
    private void openCamera() {
        Intent intentCamerapick = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher1.launch(intentCamerapick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageuri1=data.getData();
            profileImageV.setImageURI(imageuri1);


        }
        else if(requestCode==3 && resultCode==RESULT_OK && data!=null)
        {
            photo=(Bitmap) data.getExtras().get("data");
            imageuri1=Uri.parse(String.valueOf((photo)));
            profileImageV.setImageBitmap(photo);
        }
    }


}