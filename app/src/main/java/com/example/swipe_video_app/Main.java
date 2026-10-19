
package com.example.swipe_video_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class Main extends AppCompatActivity {

    private ViewPager2 videoPager;
    private VideoPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        videoPager = findViewById(R.id.videoPager);

        List<VideoItem> videos = VideoRepository.getVideos();
        adapter = new VideoPagerAdapter(videos);

        videoPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        videoPager.setOffscreenPageLimit(1);
        videoPager.setAdapter(adapter);

        videoPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        adapter.playVideo(position);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (videoPager != null && adapter != null) {
            adapter.playVideo(videoPager.getCurrentItem());
        }
    }

    @Override
    protected void onPause() {
        if (adapter != null) {
            adapter.pauseAll();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (videoPager != null) {
            videoPager.setAdapter(null);
        }

        super.onDestroy();
    }
}
