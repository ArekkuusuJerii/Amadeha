package com.codejam.amadeha.main.contenido;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codejam.amadeha.main.slider.ImageModel;
import com.codejam.amadeha.main.slider.SlidingImage_Adapter;
import com.codejam.amadeha.R;


import android.os.Handler;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentContenido.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentContenido#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContenido extends Fragment {

    private Context context;
    private Button btn_slider;
    private View v;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{
            R.drawable.slider1,R.drawable.slider5
            ,R.drawable.slider4,R.drawable.slider3,R.drawable.slider2};


    private OnFragmentInteractionListener mListener;

    public FragmentContenido(){

    }


    public static FragmentContenido newInstance(String param1, String param2) {
        FragmentContenido fragment = new FragmentContenido();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {                                   // Método
        super.onCreate(savedInstanceState);                                             //El sistema lo llama cuando crea el fragmento
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,               //El sistema lo llama cuando el fragmento debe
                             Bundle savedInstanceState) {                                //diseñar su interfaz de usuario por primera vez.
        // Inflate the layout for this fragment                                          // Para diseñar una IU para tu fragmento, debes devolver

        v = inflater.inflate(R.layout.fragment_fragment_contenido, container, false); //una View desde este método que será la raíz del diseño de  tu fragmento.
        context = v.getContext();

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        // init

        mPager = (ViewPager) v.findViewById(R.id.pager_contenido);
        mPager.setAdapter(new SlidingImage_Adapter(context.getApplicationContext(),imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                v.findViewById(R.id.indicador_contenido);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

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
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        //

        btn_slider = (Button) v.findViewById(R.id.btn_slider);
        btn_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenido.class);
                context.startActivity(intent);
            }
        });
        return v;
    }


    // Inicio AutoImageSlider

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
