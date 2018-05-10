package com.codejam.amadeha.main.videos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.main.videos.youtube.EstadisticaVideo;
import com.codejam.amadeha.main.videos.youtube.MatricesVideo;
import com.codejam.amadeha.main.videos.youtube.FuncionesVideo;
import com.codejam.amadeha.main.videos.youtube.EcuacionesVideo;
import com.codejam.amadeha.main.videos.youtube.ConjuntosVideo;

public class MenuVideos extends Activity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Button btnSkip, btnNext;
    public ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ocupa toda la pantalla... escondiento la barra de notificaciones
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu_videos);

        viewPager = findViewById(R.id.view_pager_videos);
        dotsLayout = findViewById(R.id.layoutDots_videos);
        btnSkip = findViewById(R.id.btn_skip_videos);
        btnNext = findViewById(R.id.btn_next_videos);

        layouts = new int[]{
                R.layout.menu_video_uno,
                R.layout.menu_video_dos,
                R.layout.menu_video_tres,
                R.layout.menu_video_cuatro,
                R.layout.menu_video_cinco};
        // adding bottom dots
        addBottomDots(0);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    public void conjuntos(View view) {
        Intent intent = new Intent(this, ConjuntosVideo.class);
        startActivity(intent);
    }

    public void funciones(View view) {
        Intent intent = new Intent(this, FuncionesVideo.class);
        startActivity(intent);
    }

    public void ecuaciones(View view) {
        Intent intent = new Intent(this, EcuacionesVideo.class);
        startActivity(intent);
    }

    public void matrices(View view) {
        Intent intent = new Intent(this, MatricesVideo.class);
        startActivity(intent);
    }

    public void estadistica(View view) {
        Intent intent = new Intent(this, EstadisticaVideo.class);
        startActivity(intent);
    }

    public void btnSkipClickVideos(View v) {
        finish();
    }

    public void btnNextClickVideos(View v) {
        // checking for last page
        // if last page home screen will be launched
        int current = getItem();
        if (current < layouts.length) {
            // move to next screen
            viewPager.setCurrentItem(current);
        } else {
            finish();
        }
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));
    }

    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}


