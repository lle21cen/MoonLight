package org.techtown.ideaconcert;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BannerFragment extends Fragment {

    private int pageNumber;
    static int totalPageNum;

    public BannerFragment() {}

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_banner, container, false);
        TextView indicator = layout.findViewById(R.id.fragment_indicator);
        ImageView banner_img = layout.findViewById(R.id.banner_img);
        if (pageNumber == 0) {
            banner_img.setImageResource(R.drawable.banner_sample_img1);
            indicator.setText("01");
        }
        else if (pageNumber == 1) {
            banner_img.setImageResource(R.drawable.banner_sample_img2);
            indicator.setText("02");
        }
        else if (pageNumber == 2) {
            banner_img.setImageResource(R.drawable.banner_sample_img3);
            indicator.setText("03");
        }
        return layout;
    }
}
