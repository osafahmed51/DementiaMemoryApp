package Gallery;

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
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
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

public class AddDetails extends AppCompatActivity {
    private ImageView imagevdetails;
    private TextInputEditText descDetails;
    private Button submitBtn;
    private FloatingActionButton speech_to_text;
    private ProgressBar progressBar1;
    private DatabaseReference adddetailsRef;
    private StorageReference addpicRef;
    private Uri imageuri;
    private static final int REQUEST_CODE_SPEECH_INPUT=2;
    TextToSpeech textToSpeech;
    private String desc1;
    private String savecurrentdate,savecurrenttime,randomkey;
    private String downloadimageuri;
    private String medintenttt,evetnsintt;
    AlertDialog.Builder builder;
    ActivityResultLauncher<Intent> activityResultLauncher2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        imagevdetails=findViewById(R.id.camera_addprod);
        descDetails=findViewById(R.id.desc_text1);
        submitBtn=findViewById(R.id.submit_btn);
        progressBar1=findViewById(R.id.progressBar_cyclic);
        speech_to_text=findViewById(R.id.voiceNote);
        builder = new AlertDialog.Builder(this);


        adddetailsRef=FirebaseDatabase.getInstance().getReference();
        addpicRef= FirebaseStorage.getInstance().getReference().child("imageEvents");


        Intent intentb=getIntent();
        medintenttt=intentb.getStringExtra("medicine");




        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar1.setVisibility(View.VISIBLE);
                desc1=descDetails.getText().toString();
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
                savecurrentdate=currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
                savecurrenttime=currentTime.format(calendar.getTime());
                randomkey=savecurrentdate+ "  " + savecurrenttime ;

                if(imageuri==null)
                {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(AddDetails.this, "Image is Mandatory, Please add it.", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak("Image is Mandatory, Please add it.",TextToSpeech.QUEUE_FLUSH,null);
                }
                else if(desc1.isEmpty())
                {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(AddDetails.this, "Please add description also for your future reminder" , Toast.LENGTH_SHORT).show();
                    textToSpeech.speak("Please add description also for your future reminder",TextToSpeech.QUEUE_FLUSH,null);
                }
                else
                {
                    storeInfo();
                }
            }
        });

        speech_to_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentspeech_txt=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentspeech_txt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intentspeech_txt.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intentspeech_txt.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
                try {
                    startActivityForResult(intentspeech_txt,REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e)
                {
                    Toast.makeText(AddDetails.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imagevdetails.setOnClickListener(new View.OnClickListener() {
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

        activityResultLauncher2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
           Bundle extras=result.getData().getExtras();
           Bitmap imagebitmap=(Bitmap) extras.get("data");
                WeakReference<Bitmap> result2=new WeakReference<>(Bitmap.createScaledBitmap(
                        imagebitmap,imagebitmap.getHeight(),imagebitmap.getWidth(),false
                ).copy(Bitmap.Config.RGB_565,true));

                Bitmap bm=result2.get();
                imageuri=saveImage(bm,AddDetails.this);
                imagevdetails.setImageURI(imageuri);
            }
        });


    }

    private Uri saveImage(Bitmap bm, AddDetails addDetails) {
        File imagefolder=new File(addDetails.getCacheDir(),"images");
        Uri urii=null;
        try {
            imagefolder.mkdir();
            File file=new File(imagefolder,"captures_image.jpg");
            FileOutputStream stream=new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            urii= FileProvider.getUriForFile(AddDetails.this,"com.example.dementiamemoryapp"+".provider",file);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urii;
    }

    private void openGallery() {
        Intent intentgallerypick=new Intent();
        intentgallerypick.setAction(Intent.ACTION_GET_CONTENT);
        intentgallerypick.setType("image/*");
        startActivityForResult(intentgallerypick,1);
    }

    private void openCamera() {
        Intent intentCamerapick = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamerapick, 3);
    }

    private void storeInfo() {


        StorageReference file_path=addpicRef.child(imageuri.getLastPathSegment()+".jpg");
        final UploadTask uploadTask=file_path.putFile(imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddDetails.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        downloadimageuri=file_path.getDownloadUrl().toString();
                        return file_path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadimageuri=task.getResult().toString();
                            HashMap<String ,Object> newsmap=new HashMap<>();
                            newsmap.put("description",desc1);
                            newsmap.put("image",downloadimageuri);
                            newsmap.put("dateTime",randomkey);
                            if("medicine".equals(medintenttt)){
                                adddetailsRef.child("Medicine").child(randomkey).updateChildren(newsmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressBar1.setVisibility(View.GONE);
                                            Toast.makeText(AddDetails.this, "Added Successfuly.", Toast.LENGTH_SHORT).show();
                                            Intent intentradnomkey=new Intent(AddDetails.this, Medicine
                                                    .class);
                                            startActivity(intentradnomkey);

                                        }
                                        else
                                        {
                                            progressBar1.setVisibility(View.GONE);
                                            Toast.makeText(AddDetails.this, "Error2"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
//
                            else{
                                adddetailsRef.child("Family").child(randomkey).updateChildren(newsmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressBar1.setVisibility(View.GONE);
                                            Toast.makeText(AddDetails.this, "Added Successfuly.", Toast.LENGTH_SHORT).show();
                                            Intent intentradnomkey=new Intent(AddDetails.this, Family
                                                    .class);
//                                        intentradnomkey.putExtra("keyrandom",randomkey);
                                            startActivity(intentradnomkey);

                                        }
                                        else
                                        {
                                            progressBar1.setVisibility(View.GONE);
                                            Toast.makeText(AddDetails.this, "Error2"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        }
                        else
                        {
                            progressBar1.setVisibility(View.GONE);
                            Toast.makeText(AddDetails.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
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
            imageuri=data.getData();
            imagevdetails.setImageURI(imageuri);


        }
        else if(requestCode==3 && resultCode==RESULT_OK && data!=null)
        {
            Bitmap photo=(Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,90,bytes);

//            byte bb[]=new toByteArray();

            imagevdetails.setImageBitmap(photo);
//            imageuri=data.getData();
            imageuri=Uri.parse(String.valueOf(photo));

//            imagevdetails.setImageURI(imageuri);
        }
        else if(requestCode==REQUEST_CODE_SPEECH_INPUT && resultCode==RESULT_OK && data!=null)
        {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            descDetails.setText(
                    Objects.requireNonNull(result).get(0));
        }
    }

}