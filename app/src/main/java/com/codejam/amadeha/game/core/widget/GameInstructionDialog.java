package com.codejam.amadeha.game.core.widget;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.data.GameInfo;
import com.viewpagerindicator.CirclePageIndicator;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * This file was created by Snack on 04/12/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class GameInstructionDialog {

    private final Activity context;
    private final int[][] ids;

    private GameInstructionDialog(Activity context, int[][] ids) {
        this.context = context;
        this.ids = ids;
    }

    public static GameInstructionDialog create(Activity context, int[][] ids) {
        return new GameInstructionDialog(context, ids);
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
                if(state == ViewPager.SCROLL_STATE_IDLE && pager.getCurrentItem() == ids.length - 1) {
                    pager.stopAutoScroll();
                }
            }
        });
        pager.setAdapter(new GameInstructionPager());
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

    private class GameInstructionPager extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = context.getLayoutInflater();
            View layout;
            if (position == 0) {
                layout = inflater.inflate(R.layout.template_instruction_0, container, false);
                ((TextView) layout.findViewById(R.id.description)).setText(ids[position][0]);
            } else {
                layout = inflater.inflate(R.layout.template_instruction_1, container, false);
                ((TextView) layout.findViewById(R.id.description)).setText(ids[position][0]);
                ((ImageView) layout.findViewById(R.id.image)).setImageResource(ids[position][1]);
            }
            ((ImageView) layout.findViewById(R.id.instructor)).setImageResource(GameInfo.getUser().character.img);
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
