package Gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.CalendarContract;
import android.view.View;

import com.example.dementiamemoryapp.Activities.HomeActivity;
import com.example.dementiamemoryapp.R;
import com.google.android.gms.common.api.Api;

public class SelectGalleryType extends AppCompatActivity {
    CardView cardFam,cardMed,cardEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gallery_type);
        cardFam=findViewById(R.id.cardFamily);
        cardMed=findViewById(R.id.cardMedicine);
        cardEvents=findViewById(R.id.cardOtherEvents);

        cardFam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfamily=new Intent(SelectGalleryType.this,Family.class);
                startActivity(intentfamily);
            }
        });
        cardMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMedicine=new Intent(SelectGalleryType.this,Medicine.class);
                startActivity(intentMedicine);
            }
        });
        cardEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEvents=new Intent(SelectGalleryType.this, OtherEvents.class);
                startActivity(intentEvents);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentbackpress1=new Intent(SelectGalleryType.this, HomeActivity.class);
        startActivity(intentbackpress1);
        finish();
    }
}