package com.codejam.amadeha.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.widget.AutoScrollDialog;
import com.codejam.amadeha.main.contenido.FragmentContenido;
import com.codejam.amadeha.main.contenido.FragmentCreditos;
import com.codejam.amadeha.main.contenido.FragmentLeaderboard;
import com.codejam.amadeha.main.creditos.BlankFragment;
import com.codejam.amadeha.main.inicio.FragmentInicio;
import com.codejam.amadeha.main.juegos.FragmentJuegos;
import com.codejam.amadeha.main.videos.FragmentVideos;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amadehaa_activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 3000);
    }

    private void init() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("AMADEHA");

        FragmentInicio fragment = new FragmentInicio();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Vista, fragment, "Inicio");
        fragmentTransaction.commit();
        //Show instructions
        if(isFirstSession()) {
            AutoScrollDialog.create(this,
                    R.layout.activity_bienvenida,
                    R.layout.activity_instrucciones1,
                    R.layout.activity_instrucciones2,
                    R.layout.activity_instrucciones3
            ).show();
        }
    }

    private boolean isFirstSession() {
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        if(preferences.getBoolean("first", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first", false);
            editor.apply();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_inicio: {

                setTitle("AMADEHA");
                FragmentInicio fragment = new FragmentInicio();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, "Inicio");
                fragmentTransaction.commit();

                break;
            }
            case R.id.nav_contenido: {

                setTitle("Teoría");
                FragmentContenido fragment = new FragmentContenido();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, "Teoría");
                fragmentTransaction.commit();

                break;
            }
            case R.id.nav_videos: {

                setTitle("Vídeos tutoriales");
                FragmentVideos fragment = new FragmentVideos();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, "Vídeos");
                fragmentTransaction.commit();

                break;
            }
            case R.id.nav_jugando: {

                setTitle("Juegos");
                FragmentJuegos fragment = new FragmentJuegos();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, "Juegos");
                fragmentTransaction.commit();

                break;
            }
            case R.id.nav_puntaje: {

                setTitle("Créditos");
                BlankFragment fragment = new BlankFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, "Inicio");
                fragmentTransaction.commit();

                break;
            }
            case R.id.nav_c: {

                setTitle("Referencias");
                FragmentCreditos fragment = new FragmentCreditos();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, "Referencias");
                fragmentTransaction.commit();

                break;
            }
            default: {

                setTitle(getText(R.string.leaderboard));
                FragmentLeaderboard fragment = new FragmentLeaderboard();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Vista, fragment, getText(R.string.leaderboard).toString());
                fragmentTransaction.commit();

                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
