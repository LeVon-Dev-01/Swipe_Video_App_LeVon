
package com.example.swipe_video_app;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {

    public static List<VideoItem> getVideos() {
        List<VideoItem> videos = new ArrayList<>();

        videos.add(new VideoItem(
                "1",
                "Big Buck Bunny",
                "",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ));

        videos.add(new VideoItem(
                "2",
                "Elephants Dream",
                "",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        ));

        videos.add(new VideoItem(
                "3",
                "For Bigger Escapes",
                "",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        ));

        return videos;
    }
}
