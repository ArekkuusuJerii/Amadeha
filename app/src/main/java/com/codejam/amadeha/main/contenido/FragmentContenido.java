package com.codejam.amadeha.main.contenido;

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
import com.codejam.amadeha.main.slider.SlidingImage_Adapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentContenido extends Fragment {

    private ViewPager mPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private Context context;
    private int[] myImageList = new int[]{
            R.drawable.slider1,
            R.drawable.slider5,
            R.drawable.slider4,
            R.drawable.slider3,
            R.drawable.slider2
    };

    public FragmentContenido() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,               //El sistema lo llama cuando el fragmento debe
                             Bundle savedInstanceState) {                                //diseñar su interfaz de usuario por primera vez.
        // Inflate the layout for this fragment                                          // Para diseñar una IU para tu fragmento, debes devolver
        View v = inflater.inflate(R.layout.fragment_fragment_contenido, container, false);
        context = v.getContext();

        // init
        mPager = v.findViewById(R.id.pager_contenido);
        mPager.setAdapter(new SlidingImage_Adapter(context.getApplicationContext(), myImageList));
        CirclePageIndicator indicator = v.findViewById(R.id.indicador_contenido);
        indicator.setViewPager(mPager);
        float density = getResources().getDisplayMetrics().density;
        //SetType circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = myImageList.length;
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
        Button btn_slider = v.findViewById(R.id.btn_slider);
        btn_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenido.class);
                context.startActivity(intent);
            }
        });
        return v;
    }
}
