package Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dementiamemoryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class OtherEvents extends AppCompatActivity {
    FloatingActionButton addDeatialsFloating_events;
    private DatabaseReference othereventsref_3;
    private RecyclerView recyclerViewfamil_3;
    TextToSpeech textToSpeechfamily_3;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_events);
        addDeatialsFloating_events=findViewById(R.id.add_fab_events);
        recyclerViewfamil_3=findViewById(R.id.rec2_otherevents);
        recyclerViewfamil_3.setLayoutManager(new GridLayoutManager(this,4));
        builder=new AlertDialog.Builder(this);


        othereventsref_3= FirebaseDatabase.getInstance().getReference().child("otherEvents");

        addDeatialsFloating_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentdetails4=new Intent(OtherEvents.this,AddDetailsOtherEvents.class);
                startActivity(intentdetails4);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<detailModel> options=new FirebaseRecyclerOptions
                .Builder<detailModel>()
                .setQuery(othereventsref_3,detailModel.class)
                .build();
        FirebaseRecyclerAdapter<detailModel, PicsViewHolder> adapter=new FirebaseRecyclerAdapter<detailModel, PicsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PicsViewHolder holder, int position, @NonNull detailModel model) {
                Picasso.get().load(model.getImage()).into(holder.picLayout);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence[] items={
                                "View Details",
                                "Delete"

                        };
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which)
                                {
                                    case 0:
                                        textToSpeechfamily_3=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int status) {
                                                if(status != TextToSpeech.ERROR)
                                                {

                                                    Bundle bundlefrag=new Bundle();
                                                    bundlefrag.putString("imagelink",model.getImage());
                                                    bundlefrag.putString("date",model.getDateTime());
                                                    Bottomsheet bottomsheet=new Bottomsheet();
                                                    bottomsheet.setArguments(bundlefrag);

                                                    bottomsheet.show(getSupportFragmentManager(),"Bottomsheet");

                                                    textToSpeechfamily_3.setLanguage(Locale.US);
                                                    String desc=model.getDescription();
                                                    Toast.makeText(OtherEvents.this, ""+desc, Toast.LENGTH_SHORT).show();
                                                    textToSpeechfamily_3.speak(desc,TextToSpeech.QUEUE_FLUSH,null);
                                                }
                                            }
                                        });
                                        break;
                                    case 1:
                                        othereventsref_3.child(model.getDateTime())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(OtherEvents.this, "Deleted Succesfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(OtherEvents.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        break;

                                }
                            }
                        });

                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Select Action");
                        alert.show();



                    };
                });
            }

            @NonNull
            @Override
            public PicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallerylayout,parent,false);
                PicsViewHolder holder=new PicsViewHolder(view);
                return holder;
            }
        };
        recyclerViewfamil_3.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        textToSpeechfamily_3.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        textToSpeechfamily_3.stop();
        Intent intentbackpress=new Intent(OtherEvents.this,SelectGalleryType.class);
        startActivity(intentbackpress);
        finish();
    }

}