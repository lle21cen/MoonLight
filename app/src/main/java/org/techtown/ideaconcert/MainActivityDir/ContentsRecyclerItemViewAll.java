package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import org.techtown.ideaconcert.R;

public class ContentsRecyclerItemViewAll extends RelativeLayout {

    public ContentsRecyclerItemViewAll(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.recycler_item_view_all, this, true);
    }
}
