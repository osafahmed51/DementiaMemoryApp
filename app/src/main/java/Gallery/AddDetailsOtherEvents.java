package Gallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dementiamemoryapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AddDetailsOtherEvents extends AppCompatActivity {

    private ImageView imagevdetails_otherevent2;
    private TextInputEditText descDetails_otherevent2;
    private Button submitBtn_otherevent2;
    private FloatingActionButton speech_to_text_otherevent2;
    private ProgressBar progressBar1_otherevent2;
    private DatabaseReference adddetailsRef_otherevent2;
    private StorageReference addpicRef_otherevent2;
    private Uri imageuri_otherevent2;
    private static final int REQUEST_CODE_SPEECH_INPUT_otherevent2=2;
    TextToSpeech textToSpeech_otherevent2;
    private String desc1_otherevent2;
    private String savecurrentdate_otherevent2,savecurrenttime_otherevent2,randomkey_otherevent2;
    private String downloadimageuri_otherevent2;
    AlertDialog.Builder builder;
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details_other_events);
        imagevdetails_otherevent2=findViewById(R.id.camera_addprod_2);
        descDetails_otherevent2=findViewById(R.id.desc_text1_2);
        submitBtn_otherevent2=findViewById(R.id.submit_btn_2);
        progressBar1_otherevent2=findViewById(R.id.progressBar_cyclic_2);
        speech_to_text_otherevent2=findViewById(R.id.voiceNote_2);
        builder = new AlertDialog.Builder(this);


        adddetailsRef_otherevent2= FirebaseDatabase.getInstance().getReference();
        addpicRef_otherevent2= FirebaseStorage.getInstance().getReference().child("imageEvents");




        textToSpeech_otherevent2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech_otherevent2.setLanguage(Locale.UK);
                }
            }
        });



        submitBtn_otherevent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar1_otherevent2.setVisibility(View.VISIBLE);
                desc1_otherevent2=descDetails_otherevent2.getText().toString();
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
                savecurrentdate_otherevent2=currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
                savecurrenttime_otherevent2=currentTime.format(calendar.getTime());
                randomkey_otherevent2=savecurrentdate_otherevent2+"  " + savecurrenttime_otherevent2 ;

                if(imageuri_otherevent2==null)
                {
                    progressBar1_otherevent2.setVisibility(View.GONE);
                    Toast.makeText(AddDetailsOtherEvents.this, "Image is Mandatory, Please add it.", Toast.LENGTH_SHORT).show();
                    textToSpeech_otherevent2.speak("Image is Mandatory, Please add it.",TextToSpeech.QUEUE_FLUSH,null);
                }
                else if(desc1_otherevent2.isEmpty())
                {
                    progressBar1_otherevent2.setVisibility(View.GONE);
                    Toast.makeText(AddDetailsOtherEvents.this, "Please add description also for your future reminder" , Toast.LENGTH_SHORT).show();
                    textToSpeech_otherevent2.speak("Please add description also for your future reminder",TextToSpeech.QUEUE_FLUSH,null);
                }
                else
                {
                    storeInfo();
                }
            }
        });

        speech_to_text_otherevent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentspeech_txt=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentspeech_txt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intentspeech_txt.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intentspeech_txt.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
                try {
                    startActivityForResult(intentspeech_txt,REQUEST_CODE_SPEECH_INPUT_otherevent2);
                }
                catch (Exception e)
                {
                    Toast.makeText(AddDetailsOtherEvents.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imagevdetails_otherevent2.setOnClickListener(new View.OnClickListener() {
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
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
           Bundle extras=result.getData().getExtras();
           Bitmap imagebitmap=(Bitmap) extras.get("data");

                WeakReference<Bitmap> result1=new WeakReference<>(Bitmap.createScaledBitmap(imagebitmap,
                        imagebitmap.getHeight(),imagebitmap.getWidth(),false).copy(
                                Bitmap.Config.RGB_565,true
                ));

                Bitmap bm=result1.get();
                imageuri_otherevent2=saveimage(bm,AddDetailsOtherEvents.this);
                imagevdetails_otherevent2.setImageURI(imageuri_otherevent2);

            }
        });

    }

    private Uri saveimage(Bitmap bm, AddDetailsOtherEvents addDetailsOtherEvents) {
        File imagesfolder=new File(addDetailsOtherEvents.getCacheDir(),"images");
        Uri uriii=null;
        try {
            imagesfolder.mkdir();
            File file=new File(imagesfolder,"captured_image.jpg");
            FileOutputStream stream=new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            uriii= FileProvider.getUriForFile(addDetailsOtherEvents.getApplicationContext(),"com.example.dementiamemoryapp"+".provider",file);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uriii;
    }

    private void openGallery() {
        Intent intentgallerypick=new Intent();
        intentgallerypick.setAction(Intent.ACTION_GET_CONTENT);
        intentgallerypick.setType("image/*");
        startActivityForResult(intentgallerypick,1);
    }

    private void openCamera() {
        Intent intentCamerapick = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intentCamerapick, 3);
        activityResultLauncher.launch(intentCamerapick);
    }



    private void storeInfo() {


        StorageReference file_path=addpicRef_otherevent2.child(imageuri_otherevent2.getLastPathSegment()+".jpg");
        final UploadTask uploadTask=file_path.putFile(imageuri_otherevent2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddDetailsOtherEvents.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadimageuri_otherevent2=file_path.getDownloadUrl().toString();
                        return file_path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadimageuri_otherevent2=task.getResult().toString();
                            HashMap<String ,Object> newsmap=new HashMap<>();
                            newsmap.put("description",desc1_otherevent2);
                            newsmap.put("image",downloadimageuri_otherevent2);
                            newsmap.put("dateTime",randomkey_otherevent2);
                                adddetailsRef_otherevent2.child("otherEvents").child(randomkey_otherevent2).updateChildren(newsmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressBar1_otherevent2.setVisibility(View.GONE);
                                            Toast.makeText(AddDetailsOtherEvents.this, "Added Successfuly.", Toast.LENGTH_SHORT).show();
                                            Intent intentradnomkey=new Intent(AddDetailsOtherEvents.this, OtherEvents
                                                    .class);
                                            startActivity(intentradnomkey);

                                        }
                                        else
                                        {
                                            progressBar1_otherevent2.setVisibility(View.GONE);
                                            Toast.makeText(AddDetailsOtherEvents.this, "Error2"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        }
                        else
                        {
                            progressBar1_otherevent2.setVisibility(View.GONE);
                            Toast.makeText(AddDetailsOtherEvents.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageuri_otherevent2=data.getData();
            imagevdetails_otherevent2.setImageURI(imageuri_otherevent2);


        }
//        else if(requestCode==3 && resultCode==RESULT_OK && data!=null)
//        {
//            Bundle extras=getdata
//            Bitmap photo=(Bitmap) data.getExtras().get("data");
//            imageuri_otherevent2=Uri.parse(String.valueOf(photo));
////            imageuri=data.getData();
//            imagevdetails_otherevent2.setImageBitmap(photo);
//        }
        else if(requestCode==REQUEST_CODE_SPEECH_INPUT_otherevent2 && resultCode==RESULT_OK && data!=null)
        {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            descDetails_otherevent2.setText(
                    Objects.requireNonNull(result).get(0));
        }
    }
}
