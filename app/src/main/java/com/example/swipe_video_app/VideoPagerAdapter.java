
package com.example.swipe_video_app;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoPagerAdapter
        extends RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder> {

    private final List<VideoItem> videos;
    private RecyclerView recyclerView;
    private int activePosition = 0;

    public VideoPagerAdapter(List<VideoItem> videos) {
        this.videos = videos;
    }

    @Override
    public void onAttachedToRecyclerView(
            @NonNull RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(
            @NonNull RecyclerView recyclerView) {

        pauseAll();
        this.recyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull VideoViewHolder holder, int position) {

        holder.bind(
                videos.get(position).getVideoUrl(),
                position == activePosition
        );
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        holder.release();
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void playVideo(int position) {
        if (position < 0 || position >= videos.size()) {
            return;
        }

        activePosition = position;

        if (recyclerView == null) {
            return;
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);

            RecyclerView.ViewHolder holder =
                    recyclerView.getChildViewHolder(child);

            if (holder instanceof VideoViewHolder) {
                VideoViewHolder videoHolder =
                        (VideoViewHolder) holder;

                if (videoHolder.getBindingAdapterPosition() == position) {
                    videoHolder.play();
                } else {
                    videoHolder.pause();
                }
            }
        }
    }

    public void pauseAll() {
        if (recyclerView == null) {
            return;
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);

            RecyclerView.ViewHolder holder =
                    recyclerView.getChildViewHolder(child);

            if (holder instanceof VideoViewHolder) {
                ((VideoViewHolder) holder).pause();
            }
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        private final VideoView videoView;
        private String videoUrl = "";
        private boolean prepared = false;
        private boolean shouldPlay = false;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoView);

            videoView.setOnPreparedListener(mediaPlayer -> {
                prepared = true;
                mediaPlayer.setLooping(true);

                if (shouldPlay) {
                    videoView.start();
                }
            });

            videoView.setOnErrorListener((mediaPlayer, what, extra) -> true);
        }

        void bind(String url, boolean active) {
            shouldPlay = active;

            if (url.equals(videoUrl)) {
                if (prepared && shouldPlay) {
                    videoView.start();
                }
                return;
            }

            videoUrl = url;
            prepared = false;
            videoView.stopPlayback();
            videoView.setVideoURI(Uri.parse(url));
        }

        void play() {
            shouldPlay = true;

            if (prepared) {
                videoView.start();
            }
        }

        void pause() {
            shouldPlay = false;

            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }

        void release() {
            shouldPlay = false;
            prepared = false;
            videoView.stopPlayback();
            videoUrl = "";
        }
    }
}
