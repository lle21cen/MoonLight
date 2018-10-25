package org.techtown.ideaconcert.MyPageDir;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.ideaconcert.R;
public class Fragment1RecentViewWorks extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page1_recent_view_works, container, false);
    }
}
