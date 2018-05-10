package com.codejam.amadeha.game.levels.function;

import android.os.Handler;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.core.widget.GameInstructionDialog;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.codejam.amadeha.game.data.settings.MusicHelper;
import com.codejam.amadeha.game.data.settings.Sound;
import com.codejam.amadeha.game.levels.LevelBase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codejam.amadeha.game.levels.function.Function.Card;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class FunctionScreen extends LevelBase implements ITickable {

    private static final int[][] instructions = {
            {R.string.function_description},
            {R.string.function_0, R.drawable.function_0},
            {R.string.function_1, R.drawable.function_1},
            {R.string.function_2, R.drawable.function_2},
            {R.string.function_3, R.drawable.function_3}
    };
    private ImmutableMap<ImageView, Function.Card> cards;
    private Sound[] pop = new Sound[2];
    private Pair<ImageView, Card> cardOne;
    private Pair<ImageView, Card> cardTwo;
    private boolean canSelectCard = true;
    private int score;

    @Override
    public void init() {
        pop[0] = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.card1);
        pop[1] = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.card2);
        ImmutableMap.Builder<ImageView, Function.Card> builder = new ImmutableMap.Builder<>();
        List<Function> functions = LevelRegistry.getShuffledRegistry(getGame());
        List<Function.Card> cards = Lists.newArrayList();
        for (Function function : functions) {
            if(cards.size() >= 12) break;
            Collections.addAll(cards, function.getCards(getBaseContext()));
        }
        Collections.shuffle(cards);

        for (int i = 0; i < 12; i++) {
            int id = getResources().getIdentifier("q_" + i, "id", getPackageName());
            ImageView image = findViewById(id);
            builder.put(image, cards.get(i));
        }

        this.cards = builder.build();
        startCountdown(120000L);
    }

    @Override
    public void showInstructions() {
        GameInstructionDialog.create(this, instructions).show();
    }

    @Override
    public void play() {
        if (cardOne != null && cardTwo != null) {
            Card one = cardOne.second;
            Card two = cardTwo.second;
            canSelectCard = false;

            if (one.mirror == two && two.mirror == one) {
                startCountdown(120000L);
                correct.play();
                level++;
                score += 10;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeMatchingCards();
                    }
                }, 1500);
            } else {
                score -= score > 0 ? 5 : 1;
                incorrect.play();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideCurrentCards();
                    }
                }, 1500);
            }
        }
        if (level == cards.size() / 2) {
            canSelectCard = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(getScore() <= 0) {
                        lose.play();
                    } else {
                        win.play();
                    }
                    gameover();
                }
            }, 2000);
        }
    }

    public void showCard(View view) {
        if(canSelectCard && view instanceof ImageView && (cardOne == null || cards.get(view) != cardOne.second)) {
            ImageView image = (ImageView) view;
            Card card = cards.get(image);

            pop[rand.nextInt(1)].play();
            image.setImageResource(card.image);
            if (cardOne != null) {
                cardTwo = new Pair<>(image, card);
                iterate();
            } else {
                cardOne = new Pair<>(image, card);
            }
        }
    }

    private void removeMatchingCards() {
        canSelectCard = true;
        cardOne.first.setClickable(false);
        cardTwo.first.setClickable(false);
        cardOne = null;
        cardTwo = null;
    }

    private void hideCurrentCards() {
        canSelectCard = true;
        cardOne.first.setImageResource(R.drawable.tj_back);
        cardOne = null;
        cardTwo.first.setImageResource(R.drawable.tj_back);
        cardTwo = null;
    }

    private void showAllCards() {
        for (Map.Entry<ImageView, Card> entry : cards.entrySet()) {
            entry.getKey().setImageResource(entry.getValue().image);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iterate();
            }
        }, 5000);
    }

    @Override
    public boolean canResume() {
        return true;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getView() {
        return R.layout.activity_function;
    }

    @Override
    public Game getGame() {
        return Game.FUNCTION;
    }

    @Override
    public void onCountdownTick(long remaining) {}

    @Override
    public void onCountdownFinish() {
        canSelectCard = false;
        lose.play();
        showAllCards();
        if(!isGameOver()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(getScore() <= 0) {
                        lose.play();
                    }
                    gameover();
                }
            }, 5000);
        }
    }
}
