package msm.MemeSena.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import msm.MemeSena.R;
import msm.MemeSena.Model.Image;

/**
 * Created by iblinfotech on 21/12/18.
 */

public class DecorateAdapter extends RecyclerView.Adapter<DecorateAdapter.ViewHolder> {

    ArrayList<Image> mListItem = new ArrayList<>();
    Context context;
    ClickListener clickListener;


    public DecorateAdapter(Context context, ArrayList<Image> mListItem) {
        this.context = context;
        this.mListItem = mListItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_decorate,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context).load(mListItem.get(i).getDrawableId()).into(viewHolder.ivDecorate);

//        viewHolder.ivDecorate.setImageResource(mListItem.get(i));
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View v, Image integer, int position);
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivDecorate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDecorate = itemView.findViewById(R.id.ivDecorate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view,mListItem.get(getAdapterPosition()) ,getAdapterPosition());
            }
        }
    }
}
