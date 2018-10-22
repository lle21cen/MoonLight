package org.techtown.ideaconcert.SettingsDir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DummyFragment extends BaseFragment implements View.OnClickListener {

    protected static volatile int instanceCount;

    public DummyFragment() {

    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout root = new LinearLayout(getActivity());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.GREEN);

        TextView tv = new TextView(getActivity());
        tv.setText("fragment" + instanceCount);

        Button btnNew = new Button(getActivity());
        Button btnFinish = new Button(getActivity());
        btnNew.setId(0xA0A0);
        btnFinish.setId(0xA0A1);
        btnNew.setText("add fragment");
        btnFinish.setText("finish");

        btnNew.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        root.addView(tv);
        root.addView(btnNew);
        root.addView(btnFinish);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 0xa0a0:
                Log.i(getClass().getSimpleName(), "start");
                startFragment(getFragmentManager(), DummyFragment.class);
                break;
            case 0xA0A1:
                Log.i(getClass().getSimpleName(), "finish");
                finishFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        instanceCount++;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        instanceCount--;
    }
}
