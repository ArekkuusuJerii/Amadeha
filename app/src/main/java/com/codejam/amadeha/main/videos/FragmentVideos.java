package com.codejam.amadeha.main.videos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentVideos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentVideos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVideos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private Button btn_slider;
    private OnFragmentInteractionListener mListener;
    private View v;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageListVideos = new int[]{
            R.drawable.slider1, R.drawable.slider5
            , R.drawable.slider4, R.drawable.slider3, R.drawable.slider2};

    public FragmentVideos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVideos newInstance(String param1, String param2) {
        FragmentVideos fragment = new FragmentVideos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_videos, container, false);

        btn_slider = (Button) v.findViewById(R.id.btn_slider_videos);
        btn_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                Intent intent = new Intent(context.getApplicationContext(), MenuVideos.class);
                context.startActivity(intent);
            }
        });

        context = v.getContext();

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        // init
        mPager = (ViewPager) v.findViewById(R.id.pager_videos);
        mPager.setAdapter(new SlidingImage_Adapter(context.getApplicationContext(), imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                v.findViewById(R.id.indicador_videos);

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

    // Fin AutoImageSlider

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
