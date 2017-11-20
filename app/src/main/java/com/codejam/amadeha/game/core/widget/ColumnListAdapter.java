package com.codejam.amadeha.game.core.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.internal.deps.guava.base.Predicate;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codejam.amadeha.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by Snack on 07/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class ColumnListAdapter extends BaseAdapter {

    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1F);

    private final Activity activity;
    private final List<List<View>> list;

    private ColumnListAdapter(Activity activity, List<List<View>> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public boolean isEnabled(int position) {
        return position != 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        List<View> list = this.list.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.multi_view_list, null);
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.multi_view_root);
            for (View view : list) {
                if (view.getParent() != null)
                    ((ViewGroup) view.getParent()).removeView(view);
                layout.addView(view, params);
            }
        }

        return convertView;
    }

    public static class Builder {

        protected final List<List<View>> list = new ArrayList<>();
        final Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Column addRow() {
            return new Column(this);
        }

        public ColumnListAdapter build() {
            return new ColumnListAdapter(activity, list);
        }

        @SuppressWarnings({"WeakerAccess", "unchecked"})
        public static class Column {

            private final List<View> list = new ArrayList<>();
            private final Builder build;

            Column(Builder build) {
                this.build = build;
            }

            public Column addColumn(View view) {
                return addColumn(view, null);
            }

            public Column addColumn(View view, @Nullable Predicate<View> args) {
                if (args != null) {
                    args.apply(view);
                }
                list.add(view);
                return this;
            }

            public Builder close() {
                build.list.add(list);
                return build;
            }
        }
    }
}
