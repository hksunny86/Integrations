package com.inov8.jsblconsumer.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.inov8.jsblconsumer.R;

public class VideoFragment extends Fragment {

    public static VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_splash_video, container, false);
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        startVideo();
        return rootView;
    }

    private void startVideo() {
        try {
            Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video);
            videoView.setVideoURI(video);
            videoView.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener()
                    {
                        @Override
                        public void onPrepared(MediaPlayer mp)
                        {
                            mp.setVolume(0f, 0f);
                        }
                    });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    startVideo();
                }
            });
            videoView.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void stopVideo(){

        videoView.stopPlayback();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }
}
