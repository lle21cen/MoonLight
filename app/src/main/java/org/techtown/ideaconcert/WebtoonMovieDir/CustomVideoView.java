package org.techtown.ideaconcert.WebtoonMovieDir;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {

    private PlayPauseListener listener;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        this.listener = listener;
    }

    @Override
    public void pause() {
        super.pause();
        if (listener != null) {
            listener.onPlay();
        }
    }


    public static interface PlayPauseListener {
        void onPlay();

        void onPause();
    }
}
