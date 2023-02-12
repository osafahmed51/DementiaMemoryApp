package Gallery;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dementiamemoryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;


public class Bottomsheet extends BottomSheetDialogFragment {
    private ImageView bottomsheetimgv;
    private TextView datebottomsheet;
    String btmsheetInt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String imageuriiii=getArguments().getString("imagelink");
        String datetimeee=getArguments().getString("date");

        View view= inflater.inflate(R.layout.bottomsheet, container, false);
        bottomsheetimgv=view.findViewById(R.id.imagebottomsheet);
        datebottomsheet=view.findViewById(R.id.dateLayoutbottoms);

//        Intent intentttt=getActivity().getIntent();
//        String imageuriiii=intentttt.getStringExtra("imagelink");
//        String datetimeee=intentttt.getStringExtra("date");

        Picasso.get().load(imageuriiii).into(bottomsheetimgv);
        datebottomsheet.setText(datetimeee);



        return view;
    }
}