package com.codejam.amadeha.main.contenido;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.widget.ColumnListAdapter;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.score.Score;
import com.codejam.amadeha.game.data.score.ScoreHandler;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

public class FragmentLeaderboard extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_leaderboard, container, false);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager pager = view.findViewById(R.id.container);
        pager.setAdapter(adapter);
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        return view;
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {}

        public static PlaceholderFragment newInstance(int position) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
            View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
            int frag = getArguments().getInt("position");
            Game game = Game.values()[frag];
            ((TextView) view.findViewById(R.id.title)).setText(game.name);
            ColumnListAdapter.Builder adapter = new ColumnListAdapter.Builder(getActivity())
                    .addRow()
                    .addColumn(inflateScoreRow(
                            getText(R.string.player).toString(),
                            getText(R.string.score).toString(),
                            getText(R.string.date).toString())
                    ).close();

            List<Score> get = ScoreHandler.getScores(getContext(), game);
            for (int i = 0; i < get.size() && i < 3; i++) {
                Score score = get.get(i);
                View row = inflateScoreRow(score.user, String.valueOf(score.score), score.date.toString());
                adapter.addRow().addColumn(row).close();
            }
            if(!get.isEmpty()) {
                Score score = get.get(0);

                ((TextView) view.findViewById(R.id.user)).setText(score.user);
                ((TextView) view.findViewById(R.id.score)).setText(String.valueOf(score.score));
                ((TextView) view.findViewById(R.id.date)).setText(score.date.toString());
            }

            ((ListView) view.findViewById(R.id.list)).setAdapter(adapter.build());
            return view;
        }

        private View inflateScoreRow(String user, String score, String date) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams")
            View row = inflater.inflate(R.layout.score_row_fragment, null);
            ((TextView) row.findViewById(R.id.user)).setText(user);
            ((TextView) row.findViewById(R.id.score)).setText(score);
            ((TextView) row.findViewById(R.id.date)).setText(date);
            return row;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return Game.values().length;
        }
    }
}
