package com.codejam.amadeha.game.core.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class InstructionDialog {

    private final Context context;
    private final int[] ints;

    private InstructionDialog(Context context, int... ints) {
        this.context = context;
        this.ints = ints;
    }

    public static InstructionDialog create(Context context, Object... objs) {
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
        return new InstructionDialog(context, ints);
    }

    public void show() {
        final Dialog dialog = new DialogWrapper(context, R.layout.fragment_instruction).build(1F, 1F);
        final AutoScrollViewPager pager = dialog.findViewById(R.id.view_pager);
        final CirclePageIndicator indicator = dialog.findViewById(R.id.indicator);
        final Button omit = dialog.findViewById(R.id.omit);
        pager.setAdapter(new InstructionPager());
        pager.setStopScrollWhenTouch(true);
        pager.startAutoScroll();
        pager.setInterval(10000L);
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
            LayoutInflater inflater = LayoutInflater.from(context);
            String type = context.getResources().getResourceTypeName(ints[position]);
            View layout = null;
            if (type.equals("drawable")) {
                layout = inflater.inflate(R.layout.slidingimages_layout, container, false);
                ImageView image = layout.findViewById(R.id.image);
                image.setImageResource(ints[position]);
            } else if (type.equals("layout")) {
                layout = inflater.inflate(ints[position], container, false);
            }
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
