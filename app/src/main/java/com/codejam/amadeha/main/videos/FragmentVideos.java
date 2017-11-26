package com.codejam.amadeha.main.videos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codejam.amadeha.R;
import com.codejam.amadeha.main.slider.ImageModel;
import com.codejam.amadeha.main.slider.SlidingImage_Adapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentVideos extends Fragment {
    private ViewPager mPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private Context context;
    private int[] myImageListVideos = new int[]{
            R.drawable.slider1,
            R.drawable.slider5,
            R.drawable.slider4,
            R.drawable.slider3,
            R.drawable.slider2
    };

    public FragmentVideos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_videos, container, false);

        Button btn_slider = v.findViewById(R.id.btn_slider_videos);
        btn_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                Intent intent = new Intent(context.getApplicationContext(), MenuVideos.class);
                context.startActivity(intent);
            }
        });
        context = v.getContext();
        ArrayList<ImageModel> imageModelArrayList = populateList();
        // init
        mPager = v.findViewById(R.id.pager_videos);
        mPager.setAdapter(new SlidingImage_Adapter(context.getApplicationContext(), imageModelArrayList));

        CirclePageIndicator indicator = v.findViewById(R.id.indicador_videos);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //SetType circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = imageModelArrayList.size();

        // Auto start of viewpager
        // See following code which is responsible for auto sliding of image
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }
        });
        return v;
    }

    // Inicio AutoImageSlider
    private ArrayList<ImageModel> populateList() {
        ArrayList<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageListVideos[i]);
            list.add(imageModel);
        }
        return list;
    }
}
