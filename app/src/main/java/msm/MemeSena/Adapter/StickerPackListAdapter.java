package msm.MemeSena.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import msm.MemeSena.Model.Sticker;
import msm.MemeSena.Model.StickerPack;
import msm.MemeSena.R;

public class StickerPackListAdapter extends RecyclerView.Adapter<StickerPackListItemViewHolder> implements Filterable {
    @NonNull
    private List<StickerPack> stickerPacks;
    @NonNull
    private final OnAddButtonClickedListener onAddButtonClickedListener;
    private int maxNumberOfStickersInARow;

    private List<StickerPack> stickerPacksFiltred;
    @NonNull
    private final OnContainerLayoutClickedListener OnContainerLayoutClicked;

    public StickerPackListAdapter(@NonNull List<StickerPack> stickerPacks, @NonNull OnAddButtonClickedListener onAddButtonClickedListener, @NonNull OnContainerLayoutClickedListener OnContainerLayoutClicked, Context mcontext) {
        this.stickerPacks = stickerPacks;
        this.stickerPacksFiltred = stickerPacks;
        this.onAddButtonClickedListener = onAddButtonClickedListener;
        this.OnContainerLayoutClicked = OnContainerLayoutClicked;
    }

    @NonNull
    @Override
    public StickerPackListItemViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        final Context context = viewGroup.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View stickerPackRow = layoutInflater.inflate(R.layout.sticker_packs_list_item, viewGroup, false);
        return new StickerPackListItemViewHolder(stickerPackRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final StickerPackListItemViewHolder viewHolder, final int index) {
        StickerPack pack = stickerPacksFiltred.get(index);
        final Context context = viewHolder.publisherView.getContext();
        viewHolder.publisherView.setText("by " + pack.publisher);
        //viewHolder.filesizeView.setText(Formatter.formatShortFileSize(context, pack.getTotalSize_local()));

        viewHolder.titleView.setText(pack.name);
        viewHolder.container.setOnClickListener(v -> OnContainerLayoutClicked.onContainerLayoutClicked(pack));
        viewHolder.imageRowView.removeAllViews();

        //------ set big icon
        viewHolder.bigcateicon.setImageURI(pack.getTrayImageUri());

        //-------------- get actual sticker add by user (pack.getActualStickers())
        List<Sticker> realStickersArray = pack.getActualStickers();
        int actualNumberOfStickersToShow = Math.min(maxNumberOfStickersInARow, realStickersArray.size());
        for (int i = 0; i < actualNumberOfStickersToShow; i++) {
            final SimpleDraweeView rowImage = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.sticker_pack_list_item_image, viewHolder.imageRowView, false);
            rowImage.setImageURI(realStickersArray.get(i).getUriAsUri());
            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rowImage.getLayoutParams();
            final int marginBetweenImages = (viewHolder.imageRowView.getMeasuredWidth() - maxNumberOfStickersInARow * viewHolder.imageRowView.getContext().getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size)) / (maxNumberOfStickersInARow - 1) - lp.leftMargin - lp.rightMargin;
            if (i != actualNumberOfStickersToShow - 1 && marginBetweenImages > 0) { //do not set the margin for the last image
                lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin + marginBetweenImages, lp.bottomMargin);
                rowImage.setLayoutParams(lp);
            }
            viewHolder.imageRowView.addView(rowImage);
        }

        // handle add sticker button
        //setAddButtonAppearance(viewHolder.addButton, pack);

       /* viewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataArchiver.createZipFileFromStickerPack(pack, context);
            }
        });*/
    }

    public void setStickerPackList(List<StickerPack> stickerPackList) {
        this.stickerPacks = stickerPackList;
        this.stickerPacksFiltred = stickerPackList;
    }

    private void setAddButtonAppearance(ImageView addButton, StickerPack pack) {
        if (pack.getIsWhitelisted()) {
            addButton.setImageResource(R.mipmap.sticker_3rdparty_added);
            addButton.setClickable(false);
            addButton.setOnClickListener(null);
            addButton.setBackgroundDrawable(null);
        } else {
            addButton.setImageResource(R.drawable.ic_add);
            addButton.setOnClickListener(v -> onAddButtonClickedListener.onAddButtonClicked(pack));
            TypedValue outValue = new TypedValue();
            addButton.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            addButton.setBackgroundResource(outValue.resourceId);
        }
    }

    @Override
    public int getItemCount() {
        return stickerPacksFiltred.size();
    }

    public void setMaxNumberOfStickersInARow(int maxNumberOfStickersInARow) {
        if (this.maxNumberOfStickersInARow != maxNumberOfStickersInARow) {
            this.maxNumberOfStickersInARow = maxNumberOfStickersInARow;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    stickerPacksFiltred = stickerPacks;
                } else {
                    List<StickerPack> filteredList = new ArrayList<StickerPack>();
                    for (StickerPack row : stickerPacks) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    stickerPacksFiltred = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stickerPacksFiltred;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stickerPacksFiltred = (List<StickerPack>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnContainerLayoutClickedListener {
        void onContainerLayoutClicked(StickerPack stickerPack);
    }

    public interface OnAddButtonClickedListener {
        void onAddButtonClicked(StickerPack stickerPack);
    }
}