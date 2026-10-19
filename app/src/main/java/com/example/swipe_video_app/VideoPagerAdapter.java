
package com.example.swipe_video_app;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoPagerAdapter
        extends RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>
{

    private final List<VideoItem> videos;
    private RecyclerView recyclerView;
    private int activePosition = 0;

    public VideoPagerAdapter(List<VideoItem> videos)
    {
        this.videos = videos;
    }

    @Override
    public void onAttachedToRecyclerView(
            @NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(
            @NonNull RecyclerView recyclerView)
    {
        pauseAll();
        this.recyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull VideoViewHolder holder, int position)
    {
        VideoItem video = videos.get(position);

        holder.videoIdView.setText("ID: " + video.getId());
        holder.videoTitleView.setText(video.getTitle());
        holder.videoDescriptionView.setText(video.getDescription());

        holder.bind(video.getVideoUrl(), position == activePosition);
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder)
    {
        holder.release();
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount()
    {
        return videos.size();
    }

    public void playVideo(int position) {
        if (position < 0 || position >= videos.size())
        {
            return;
        }

        activePosition = position;

        if (recyclerView == null)
        {
            return;
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++)
        {
            View child = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder holder =
                    recyclerView.getChildViewHolder(child);

            if (holder instanceof VideoViewHolder)
            {
                VideoViewHolder videoHolder = (VideoViewHolder) holder;

                if (videoHolder.getBindingAdapterPosition() == position)
                {
                    videoHolder.play();
                } else {
                    videoHolder.pause();
                }
            }
        }
    }

    public void pauseAll()
    {
        if (recyclerView == null)
        {
            return;
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++)
        {
            View child = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder holder =
                    recyclerView.getChildViewHolder(child);

            if (holder instanceof VideoViewHolder)
            {
                ((VideoViewHolder) holder).pause();
            }
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder
    {

        private final TextureView textureView;
        private final TextView videoIdView;
        private final TextView videoTitleView;
        private final TextView videoDescriptionView;

        private MediaPlayer mediaPlayer;
        private Surface videoSurface;
        private String videoUrl = "";

        private boolean prepared = false;
        private boolean shouldPlay = false;

        private int videoWidth;
        private int videoHeight;

        VideoViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textureView = itemView.findViewById(R.id.videoView);
            videoIdView = itemView.findViewById(R.id.videoId);
            videoTitleView = itemView.findViewById(R.id.videoTitle);
            videoDescriptionView =
                    itemView.findViewById(R.id.videoDescription);

            textureView.setSurfaceTextureListener(
                    new TextureView.SurfaceTextureListener()
                    {
                        @Override
                        public void onSurfaceTextureAvailable(
                                SurfaceTexture surface,
                                int width, int height)
                        {
                            videoSurface = new Surface(surface);
                            updateVideoTransform();
                            initializePlayer();
                        }

                        @Override
                        public void onSurfaceTextureSizeChanged(
                                SurfaceTexture surface,
                                int width, int height)
                        {
                            updateVideoTransform();
                        }

                        @Override
                        public boolean onSurfaceTextureDestroyed(
                                SurfaceTexture surface)
                        {
                            releasePlayer();

                            if (videoSurface != null)
                            {
                                videoSurface.release();
                                videoSurface = null;
                            }

                            return true;
                        }

                        @Override
                        public void onSurfaceTextureUpdated(
                                SurfaceTexture surface)
                        {
                        }
                    });

            if (textureView.isAvailable())
            {
                videoSurface = new Surface(
                        textureView.getSurfaceTexture());
            }
        }

        void bind(String url, boolean active)
        {
            shouldPlay = active;

            if (url.equals(videoUrl))
            {
                if (prepared && shouldPlay && mediaPlayer != null
                        && !mediaPlayer.isPlaying())
                {
                    mediaPlayer.start();
                }
                return;
            }

            videoUrl = url;
            releasePlayer();

            if (textureView.isAvailable())
            {
                initializePlayer();
            }
        }

        private void initializePlayer()
        {
            if (!textureView.isAvailable()
                    || videoSurface == null
                    || videoUrl.isEmpty()
                    || mediaPlayer != null)
            {
                return;
            }

            try {
                mediaPlayer = new MediaPlayer();

                mediaPlayer.setOnPreparedListener(player ->
                {
                    prepared = true;
                    videoWidth = player.getVideoWidth();
                    videoHeight = player.getVideoHeight();

                    updateVideoTransform();

                    if (shouldPlay)
                    {
                        player.start();
                    }
                });

                mediaPlayer.setOnVideoSizeChangedListener(
                        (player, width, height) ->
                        {
                            videoWidth = width;
                            videoHeight = height;
                            updateVideoTransform();
                        });

                mediaPlayer.setOnCompletionListener(
                        MediaPlayer::start);

                mediaPlayer.setOnErrorListener((player, what, extra) ->
                {
                    android.util.Log.e(
                            "VideoPagerAdapter",
                            "Playback error: " + what + ", " + extra);
                    releasePlayer();
                    return true;
                });

                mediaPlayer.setDataSource(
                        itemView.getContext(),
                        Uri.parse(videoUrl));

                mediaPlayer.setSurface(videoSurface);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();

            } catch (Exception e)
            {
                android.util.Log.e(
                        "VideoPagerAdapter",
                        "Unable to load video: " + videoUrl, e);
                releasePlayer();
            }
        }

        private void updateVideoTransform()
        {
            if (videoWidth <= 0 || videoHeight <= 0
                    || textureView.getWidth() == 0
                    || textureView.getHeight() == 0)
            {
                return;
            }

            float viewWidth = textureView.getWidth();
            float viewHeight = textureView.getHeight();

            float scale = Math.max(
                    viewWidth / videoWidth,
                    viewHeight / videoHeight);

            float scaledWidth = videoWidth * scale;
            float scaledHeight = videoHeight * scale;

            Matrix matrix = new Matrix();
            matrix.setScale(
                    scaledWidth / viewWidth,
                    scaledHeight / viewHeight,
                    viewWidth / 2f,
                    viewHeight / 2f);

            textureView.setTransform(matrix);
            textureView.invalidate();
        }

        void play()
        {
            shouldPlay = true;

            if (prepared && mediaPlayer != null)
            {
                if (!mediaPlayer.isPlaying())
                {
                    mediaPlayer.start();
                }
            } else {
                initializePlayer();
            }
        }

        void pause()
        {
            shouldPlay = false;

            if (mediaPlayer != null && mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
            }
        }

        void release()
        {
            shouldPlay = false;
            releasePlayer();
            videoUrl = "";
        }

        private void releasePlayer()
        {
            prepared = false;

            if (mediaPlayer != null)
            {
                try {
                    mediaPlayer.setOnPreparedListener(null);
                    mediaPlayer.setOnVideoSizeChangedListener(null);
                    mediaPlayer.setOnCompletionListener(null);
                    mediaPlayer.setOnErrorListener(null);
                    mediaPlayer.setSurface(null);
                    mediaPlayer.reset();
                    mediaPlayer.release();
                } catch (Exception ignored) {
                }

                mediaPlayer = null;
            }
        }
    }
}
