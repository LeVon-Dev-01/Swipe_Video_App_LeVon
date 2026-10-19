package com.example.swipe_video_app;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository
{

    public static List<VideoItem> getVideos()
    {
        List<VideoItem> videos = new ArrayList<>();

        videos.add(new VideoItem(
                "1",
                "Ruby Pet",
                "New foster baby",
                "https://raw.githubusercontent.com/LeVon-Dev-01/Swipe_Video_App_LeVon/master/Videos/RubyPet.mp4"
        ));

        videos.add(new VideoItem(
                "2",
                "Oso Run",
                "Run forest run",
                "https://raw.githubusercontent.com/LeVon-Dev-01/Swipe_Video_App_LeVon/master/Videos/OsoRun.mp4"
        ));

        videos.add(new VideoItem(
                "3",
                "Ringo Climb",
                "Up she goes",
                "https://raw.githubusercontent.com/LeVon-Dev-01/Swipe_Video_App_LeVon/master/Videos/RingoClimb.mp4"
        ));

        videos.add(new VideoItem(
                "4",
                "Scully Attack",
                "cat bad",
                "https://raw.githubusercontent.com/LeVon-Dev-01/Swipe_Video_App_LeVon/master/Videos/ScullyAttack.mp4"
        ));

        videos.add(new VideoItem(
                "5",
                "Scully Bag",
                "cat orange",
                "https://raw.githubusercontent.com/LeVon-Dev-01/Swipe_Video_App_LeVon/master/Videos/ScullyBag.mp4"
        ));

        return videos;
    }
}