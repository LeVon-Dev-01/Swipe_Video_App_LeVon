
package com.example.swipe_video_app;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {

    public static List<VideoItem> getVideos() {
        List<VideoItem> videos = new ArrayList<>();

        videos.add(new VideoItem(
                "1",
                "Oso Run",
                "",
                "https://github.com/LeVon-Dev-01/Swipe_Video_App_LeVon/blob/master/Videos/OsoRun.mp4"
        ));

        videos.add(new VideoItem(
                "2",
                "Ringo",
                "",
                "https://github.com/LeVon-Dev-01/Swipe_Video_App_LeVon/blob/master/Videos/RingoClimb.mp4"
        ));

        videos.add(new VideoItem(
                "3",
                "Ruby",
                "",
                "https://github.com/LeVon-Dev-01/Swipe_Video_App_LeVon/blob/master/Videos/RubyPet.mp4"
        ));


        videos.add(new VideoItem(
                "4",
                "Scully Atack",
                "",
                "https://github.com/LeVon-Dev-01/Swipe_Video_App_LeVon/blob/master/Videos/ScullyAttack.mp4"

        ));

        videos.add(new VideoItem(
                "5",
                "Scully Bag",
                "",
                "https://github.com/LeVon-Dev-01/Swipe_Video_App_LeVon/blob/master/Videos/ScullyBag.mp4"
        ));
        return videos;
    }
}
