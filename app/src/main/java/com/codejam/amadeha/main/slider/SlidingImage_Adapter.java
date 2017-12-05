package com.codejam.amadeha.main.slider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codejam.amadeha.R;

/**
 * This file was created by Magdalena on 21/07/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class SlidingImage_Adapter extends PagerAdapter {

    private int[] images;
    private LayoutInflater inflater;

    public SlidingImage_Adapter(Context context, int... images) {
        this.inflater = LayoutInflater.from(context);
        this.images = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View layout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert layout != null;
        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(images[position]);
        view.addView(layout, 0);
        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}