package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewDecoration  extends RecyclerView.ItemDecoration {
    private final int divRight;

    public RecyclerViewDecoration(int divRight)
    {
        this.divRight = divRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = divRight;
    }
}
