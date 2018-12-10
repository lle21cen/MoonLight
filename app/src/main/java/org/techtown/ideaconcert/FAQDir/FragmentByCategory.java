package org.techtown.ideaconcert.FAQDir;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class FragmentByCategory extends Fragment {
    private final String GetBoardURL = ActivityCodes.DATABASE_IP + "/platform/GetBoard";
    View view;
    ExpandableListView faqExListView;
    private int category;

    @SuppressLint("ValidFragment")
    public FragmentByCategory(int category) {
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_fragment, container, false);
        faqExListView = view.findViewById(R.id.faq_expandable_list_view);

        DataListener listener = new DataListener(getActivity(), faqExListView);
        FAQDataRequest request = new FAQDataRequest(GetBoardURL, listener, category);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return view;
    }
}