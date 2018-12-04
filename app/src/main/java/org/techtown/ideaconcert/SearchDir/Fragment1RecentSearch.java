package org.techtown.ideaconcert.SearchDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

public class Fragment1RecentSearch extends Fragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment1_recent, container, false);
        TextView contentsNameView = view.findViewById(R.id.search_item_contents_name);
        TextView dateView = view.findViewById(R.id.search_date);
        ImageView cancelView = view.findViewById(R.id.search_cancel_image);


        return view;
    }
}
