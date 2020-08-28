package com.example.videofi;

import android.media.MediaController2;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecondFragment extends Fragment {
    private VideoView videoView;
    //private MediaController controller;
    private Button nextButton;
    private Button previousButton;
    private SeekBar seekBar;
    private TextView timer;
    private String[] randomArray = {"video","video1","video2","video3","video4","video5","video6"};
    int videoCount = 5;// how many videos to show on story. It is static for now.
    int stopPosition;
    private String[] videoArray = new String[videoCount];
    private CountDownTimer videoTimer=null;

    int count=0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        //Finding IDs
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_second, container, false);
        timer = rootView.findViewById(R.id.timer);
        seekBar = rootView.findViewById(R.id.seekBar);
        videoView = rootView.findViewById(R.id.videoView);
        previousButton = rootView.findViewById(R.id.previous_button);
        nextButton = rootView.findViewById(R.id.next_button);
        videoArray = makeVideoArray(randomArray);

//        controller = new MediaController(this.getContext());
//        controller.setEnabled(true);
//        controller.setAnchorView(videoView);
//        videoView.setMediaController(controller);
        previousButton.setEnabled(false);
        previousButton.setVisibility(View.GONE);
        seekBar.setMax(videoArray.length);
        seekBar.setProgress(count+1);
        if(videoArray.length<=1){
            seekBar.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }
        startVideo(videoArray[count]);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying() && videoView.canPause()){
                    videoView.pause();
                    stopPosition = videoView.getCurrentPosition();
                    //controller.show(5000);
                    //controller.findFocus();
                    rootView.setKeepScreenOn(false);
                    if(videoTimer!=null){
                        videoTimer.cancel();
                        videoTimer=null;
                    }
//                    if(!(previousButton.isShown() || nextButton.isShown())){
//                        previousButton.setVisibility(View.VISIBLE);
//                        nextButton.setVisibility(View.VISIBLE);
//                    }
                }
                else if(!videoView.isPlaying()){
                    rootView.setKeepScreenOn(true);
                    videoView.requestFocus();
                    videoView.seekTo(stopPosition);
                    videoView.start();
                    setTimer(getVideoDuration(videoArray[count])-stopPosition);
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                count=keepSafe(count,videoArray.length,true);
                startVideo(videoArray[count]);
                seekBar.setProgress(count+1);
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=keepSafe(count,videoArray.length,false);
                startVideo(videoArray[count]);
                seekBar.setProgress(count+1);
            }

        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=keepSafe(count,videoArray.length,true);
                startVideo(videoArray[count]);
                seekBar.setProgress(count+1);
            }

        });
        return  rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }

        });

    }
    private void startVideo(String video){
//        previousButton.setVisibility(View.GONE);
//        nextButton.setVisibility(View.GONE);
        videoView.setVideoURI(getMedia(video));
        videoView.start();
        videoView.requestFocus();
        long startingTime=getVideoDuration(video);
        setTimer(startingTime);
    }

    public String getPackageName(){
        return "com.example.videofi";
    }

    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }

    public int keepSafe(int count, int length, boolean next){
        if(next)++count;
        else --count;

        if(count==length && next){
            count=0;
            //to go back to home. Comment below 2 lines to run video in loop.
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);

        }

        if(count==0){
            previousButton.setEnabled(false);
            previousButton.setVisibility(View.GONE);
        }
        else{
            previousButton.setEnabled(true);
            previousButton.setVisibility(View.VISIBLE);
        }

        if(count==videoArray.length-1){
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.GONE);
        }
        else {
            nextButton.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
        }
        return count;
    }
    private String[] makeVideoArray(String[] array){
        String[] result= new String[videoCount];
        int count = 0;
        List<String> list = Arrays.asList(array);
        // Shuffling list elements
        Collections.shuffle(list);
        for (String str : list) {
            result[count]=str;
            count++;
            if(count==videoCount)break;
        }
        return result;
    }
    //duration to appropriate format (00:00:00)
    private static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format("%02d:%02d" , minute, second);
        }

    }

    //get Duration of video.
    private long getVideoDuration(String videoName){
        MediaPlayer mp = new MediaPlayer();
        Uri videoUri = getMedia(videoName);
        try{
            mp.setDataSource(this.getContext(),videoUri);
            mp.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long timeInMillisec = mp.getDuration();
        return timeInMillisec;
    }
    private void setTimer(long startingTime){

        if(videoTimer==null){
            videoTimer = new CountDownTimer(startingTime , 1000) {

                public void onTick(long millisUntilFinished) {
                    String durationLeft = convertMillieToHMmSs(millisUntilFinished);
                    timer.setText(durationLeft);
                }

                public void onFinish() {
                    timer.setText("00:00");
                }
            }.start();
        }
        else{
            videoTimer.cancel();
            videoTimer=null;
            setTimer(startingTime);
        }

    }

}