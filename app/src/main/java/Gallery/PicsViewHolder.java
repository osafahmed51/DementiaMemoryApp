package Gallery;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dementiamemoryapp.R;

public class PicsViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {
    public ImageView picLayout;
    public OnItemClickListener itemClickListener;


    public PicsViewHolder(@NonNull View itemView) {
        super(itemView);
        picLayout=itemView.findViewById(R.id.layoutPic);
    }


    @Override
    public void OnClick(View view, int position, boolean isLongClick) {
        itemClickListener.OnClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
