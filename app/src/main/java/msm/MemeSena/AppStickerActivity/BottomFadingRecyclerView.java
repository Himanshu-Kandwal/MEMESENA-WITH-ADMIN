package msm.MemeSena.AppStickerActivity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class BottomFadingRecyclerView extends RecyclerView {
    public BottomFadingRecyclerView(Context context) {
        super(context);
    }

    public BottomFadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomFadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return 0.0f;
    }
}
