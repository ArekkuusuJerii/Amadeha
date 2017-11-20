package com.codejam.amadeha.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.widget.DialogWrapper;
import com.codejam.amadeha.game.data.GameInfo;
import com.codejam.amadeha.game.data.profile.User;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.codejam.amadeha.game.data.settings.MusicHelper;

/**
 * This file was created by Snack on 06/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class LoadingScreen extends Activity {

    private Dialog wrapper;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_loading);
        LevelRegistry.init(getResources());
        GameInfo.init(this);
        MusicHelper.init(this);
        showProfiles();
        playMusic();
    }

    private void playMusic() {
        MusicHelper.playBackground(getBaseContext(), R.raw.ambient_loop);
    }

    private void showProfiles() {
        wrapper = new DialogWrapper(this, R.layout.activity_profiles)
                .setFeature(Window.FEATURE_NO_TITLE)
                .setCancelable(false)
                .build(0.85F, 0.85F);

        wrapper.findViewById(R.id.profile_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputText = ((EditText) wrapper.findViewById(R.id.profile_text));
                String name = inputText.getText().toString();
                if (!name.isEmpty()) {
                    GameInfo.addUser(getBaseContext(), name);
                    inputText.setText("");
                    listUsers();
                }
            }
        });
        ((ListView) wrapper.findViewById(R.id.profile_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUser(GameInfo.getUsers().get(position));
            }
        });
        ((ListView) wrapper.findViewById(R.id.profile_list)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeUser(GameInfo.getUsers().get(position));
                return true;
            }
        });
        wrapper.findViewById(R.id.profile_guest_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUser(null);
            }
        });
        wrapper.show();
        listUsers();
    }

    private void setUser(User user) {
        GameInfo.setUser(user);
        start();
    }

    private void removeUser(final User user) {
        new AlertDialog.Builder(LoadingScreen.this)
                .setTitle(getText(R.string.delete_user))
                .setMessage(getText(R.string.delete_user_question))
                .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameInfo.removeUser(LoadingScreen.this, user);
                        listUsers();
                    }
                })
                .setNegativeButton(getText(R.string.no), null)
                .show();
    }

    private void listUsers() {
        String[] names = new String[GameInfo.getUsers().size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = GameInfo.getUsers().get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.view_list,
                R.id._list_text, names);
        ((ListView) wrapper.findViewById(R.id.profile_list)).setAdapter(adapter);
    }

    private void start() {
        Intent intent = new Intent(getBaseContext(), LevelsScreen.class);
        startActivity(intent);
        wrapper.cancel();
        finish();
    }

    @Override
    protected void onPause() {
        MusicHelper.stopBackground(getBaseContext());
        super.onPause();
    }

    @Override
    protected void onResume() {
        MusicHelper.playBackground(getBaseContext(), R.raw.ambient_loop);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MusicHelper.stopBackground(getBaseContext());
        finish();
    }
}
