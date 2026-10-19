
package com.example.swipe_video_app;

public class VideoItem {

    private final String id;
    private final String title;
    private final String description;
    private final String videoUrl;

    public VideoItem(
            String id,
            String title,
            String description,
            String videoUrl) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
