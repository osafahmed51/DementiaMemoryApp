package Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class Medicine extends AppCompatActivity {
    FloatingActionButton addDeatialsFloating_med;
    private DatabaseReference medicineref;
    private RecyclerView recyclerViewmed;
    private TextToSpeech textToSpeechmed;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        addDeatialsFloating_med=findViewById(R.id.add_fab_medicine);
        recyclerViewmed=findViewById(R.id.rec2medicine);
        recyclerViewmed.setLayoutManager(new GridLayoutManager(this,4));
        builder=new AlertDialog.Builder(this);

        medicineref= FirebaseDatabase.getInstance().getReference().child("Medicine");

        addDeatialsFloating_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentdetails2=new Intent(Medicine.this,AddDetails.class);
                intentdetails2.putExtra("medicine","medicine");
                startActivity(intentdetails2);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<detailModel> options=new FirebaseRecyclerOptions
                .Builder<detailModel>()
                .setQuery(medicineref,detailModel.class)
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

                                        textToSpeechmed=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int status) {

                                                Bundle bundlefrag=new Bundle();
                                                bundlefrag.putString("imagelink",model.getImage());
                                                bundlefrag.putString("date",model.getDateTime());
                                                Bottomsheet bottomsheet=new Bottomsheet();
                                                bottomsheet.setArguments(bundlefrag);

                                                bottomsheet.show(getSupportFragmentManager(),"Bottomsheet");


                                                if(status != TextToSpeech.ERROR)
                                                {
                                                    textToSpeechmed.setLanguage(Locale.US);
                                                    String desc=model.getDescription();
                                                    Toast.makeText(Medicine.this, ""+desc, Toast.LENGTH_SHORT).show();
                                                    textToSpeechmed.speak(desc,TextToSpeech.QUEUE_FLUSH,null);
                                                }
                                            }
                                        });
                                        break;
                                    case 1:
                                        medicineref.child(model.getDateTime())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(Medicine.this, "Deleted Succesfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(Medicine.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
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


                    }
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
        recyclerViewmed.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        textToSpeechmed.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        textToSpeechmed.stop();
        Intent intentbackpress=new Intent(Medicine.this,SelectGalleryType.class);
        startActivity(intentbackpress);
        finish();
    }
}