package com.example.videofi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


public class FirstFragment extends Fragment {
    private ImageView imageView0;
    private ImageView imageView1;
    private ImageView profilePicture0;
    private ImageView profilePicture1;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Inflate the layout for this fragment
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);
        imageView0 = rootView.findViewById(R.id.thumbnail);
        imageView1 = rootView.findViewById(R.id.thumbnail1);

        profilePicture0 = rootView.findViewById(R.id.user_picture);
        profilePicture1 = rootView.findViewById(R.id.user_picture1);

        imageView0.setImageDrawable(getImageResource("thumb1"));
        imageView1.setImageDrawable(getImageResource("thumb2"));

        profilePicture0.setImageDrawable(getImageResource("avatar1"));
        profilePicture1.setImageDrawable(getImageResource("avatar2"));
        //Generate thumbnail from video.
//        try {
//            String filePath = "android.resource://" + "com.example.videofi" + "/raw/" + "video3";
//            bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
//            if (bitmap != null) {
//                //bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
//                imageView.setImageBitmap(bitmap);
//            }
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.play_button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        view.findViewById(R.id.play_button_main1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
    public String getPackageName(){
        return "com.example.videofi";
    }
    private Drawable getImageResource(String name){
        String uri = "@drawable/"+name;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        return  res;
    }

}