package com.codejam.amadeha.game.core.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.codejam.amadeha.R;
import com.viewpagerindicator.CirclePageIndicator;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * This file was created by Snack on 04/12/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class AutoScrollDialog {

    private final Activity context;
    private final int[] ints;

    private AutoScrollDialog(Activity context, int... ints) {
        this.context = context;
        this.ints = ints;
    }

    public static AutoScrollDialog create(Activity context, Object... objs) {
        int[] ints = new int[objs.length];
        for (int i = 0; i < objs.length; i++) {
            Object obj = objs[i];
            if (obj instanceof Integer) {
                ints[i] = (Integer) obj;
            } else if (obj instanceof CharSequence) {
                Resources resources = context.getResources();
                ints[i] = resources.getIdentifier(String.valueOf(obj), "drawable", context.getPackageName());
            }
        }
        return new AutoScrollDialog(context, ints);
    }

    public void show() {
        final Dialog dialog = new DialogWrapper(context, R.layout.fragment_instruction)
                .setFeature(Window.FEATURE_NO_TITLE)
                .setCancelable(true)
                .setMinimizable(true)
                .build(1F, 1F);
        final AutoScrollViewPager pager = dialog.findViewById(R.id.view_pager);
        final CirclePageIndicator indicator = dialog.findViewById(R.id.indicator);
        final Button omit = dialog.findViewById(R.id.omit);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE && pager.getCurrentItem() == ints.length - 1) {
                    pager.stopAutoScroll();
                }
            }
        });
        pager.setAdapter(new InstructionPager());
        pager.setStopScrollWhenTouch(true);
        pager.setInterval(9000L);
        pager.startAutoScroll();
        ((View) omit).setOnTouchListener(new SimpleTouchListener() {

            @Override
            public void touchPush(View v) {
                dialog.cancel();
            }
        });
        indicator.setViewPager(pager);
        dialog.show();
    }

    private class InstructionPager extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = context.getLayoutInflater();
            String type = context.getResources().getResourceTypeName(ints[position]);
            View layout = null;
            if (type.equals("drawable")) {
                layout = inflater.inflate(R.layout.images_layout, container, false);
                ImageView image = layout.findViewById(R.id.image);
                image.setImageResource(ints[position]);
            } else if (type.equals("layout")) {
                layout = inflater.inflate(ints[position], container, false);
            }
            assert layout != null;
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return ints.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
