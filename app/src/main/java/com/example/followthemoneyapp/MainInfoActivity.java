package com.example.followthemoneyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class MainInfoActivity extends AppCompatActivity {

//    WebView videoWebView;
    VideoView mainInfoVideoView;
    TextView creditInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);

        // set video from youtube (embed)
//        videoWebView = findViewById(R.id.info_main_web_view_video);
//        String videoResource = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/caGlzR9F2zI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
//        videoWebView.loadData(videoResource, "text/html", "utf-8");
//        videoWebView.getSettings().setJavaScriptEnabled(true);
//        videoWebView.setWebChromeClient(new WebChromeClient());

        mainInfoVideoView = (VideoView) this.findViewById(R.id.main_info_video_view);
        creditInfo = (TextView) this.findViewById(R.id.info_main_video_name);

        String uri = "android.resource://" + getPackageName() + "/" + R.raw.activity_info_video;
        mainInfoVideoView.setVideoURI(Uri.parse(uri));
//        mainInfoVideoView.start();

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mainInfoVideoView);
        mainInfoVideoView.setMediaController(mediaController);
//        mainInfoVideoView.start();

        // make credit title clickable
        creditInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }
}