package org.techtown.ideaconcert.MainActivityDir;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CircleAnimIndicator extends LinearLayout {

    private Context context;
    // 원 사이의 간격
    private int itemMargin = 5;

    // 애니메이션 시간
    private int animDuration = 250;

    private int defaultCircle;
    private int selectCircle;

    private ImageView[] imageDot;

    public CircleAnimIndicator(Context context) {
        super(context);
        this.context = context;
    }

    public CircleAnimIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
    }

    /**
     * 기본 점 생성
     *
     * @param count         점의 갯수
     * @param defaultCircle 점의 이미지
     */
    public void createDotPanel(int count, int defaultCircle, int selectCircle) {
        this.defaultCircle = defaultCircle;
        this.selectCircle = selectCircle;

        imageDot = new ImageView[count];

        for (int i = 0; i < count; i++) {
            imageDot[i] = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(itemMargin, itemMargin, itemMargin, itemMargin);
            params.gravity = Gravity.CENTER;

            imageDot[i].setLayoutParams(params);
            imageDot[i].setImageResource(defaultCircle);
            imageDot[i].setTag(imageDot[i].getId(), false);
            this.addView(imageDot[i]);
        }
        selectDot(0);
    }

    /**
     * 선택된 점 표시
     *
     * @param position
     */
    public void selectDot(int position) {
        for (int i = 0; i < imageDot.length; i++) {
            if (i == position) {
                imageDot[i].setImageResource(selectCircle);
                selectScaleAnim(imageDot[i], 1f, 1.5f);
            } else {
                if ((boolean) imageDot[i].getTag(imageDot[i].getId())) {
                    imageDot[i].setImageResource(defaultCircle);
                    selectScaleAnim(imageDot[i], 1.5f, 1f);
                }
            }
        }
    }

    /**
     * 선택된 점의 애니메이션
     *
     * @param view
     * @param startScale
     * @param endScale
     */
    public void selectScaleAnim(View view, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(startScale, endScale, startScale, endScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(animDuration);
        view.startAnimation(anim);
        view.setTag(view.getId(), true);
    }
}