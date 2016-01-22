package com.lochbridge.peike.demo;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lochbridge.peike.demo.fragment.PlayerControlFragment;
import com.lochbridge.peike.demo.fragment.TimerFragment;
import com.lochbridge.peike.demo.fragment.player.PlayerFragment;
import com.lochbridge.peike.demo.fragment.player.SimplePlayerFragment;
import com.lochbridge.peike.demo.util.Constants;

public class PlayerActivity extends AppCompatActivity implements
        PlayerControlFragment.PlayerControlListener,
        PlayerFragment.TimerControlListener {

    private PlayerFragment playerFragment;
    private View mDecorView;
    private View controlsView;
    private TimerFragment timerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mDecorView = getWindow().getDecorView();
        controlsView = findViewById(R.id.player_overlay);
        int subId = getIntent().getIntExtra(Constants.EXTRA_SUB_ID, 0);
        String movieTitle = getIntent().getStringExtra(Constants.EXTRA_TITLE);
        int playerTheme = getIntent().getIntExtra(Constants.EXTRA_PLAYER_THEME, -1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(movieTitle);
        }

        setupSystemUiVisibilityChangeListener();

        switch (playerTheme) {
            case PlayerFragment.SIMPLE_THEME:
            default:
                playerFragment = SimplePlayerFragment.newInstance(subId);
        }

        timerFragment = new TimerFragment();
        PlayerControlFragment controlFragment = new PlayerControlFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_player, playerFragment);
        fragmentTransaction.replace(R.id.frag_timer, timerFragment);
        fragmentTransaction.replace(R.id.frag_player_control, controlFragment);
        fragmentTransaction.commit();
    }


    private void setupSystemUiVisibilityChangeListener() {
        mDecorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int flags) {
                        boolean visible = (flags & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
                        controlsView.animate()
                                .alpha(visible ? 1 : 0)
                                .translationY(visible ? 0 : controlsView.getHeight());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void nextTapped() {
        // show timer
        // Timer adjust to next srtItem.startTime
        playerFragment.nextTapped();
    }

    @Override
    public void previousTapped() {
        playerFragment.previousTapped();
    }

    @Override
    public void toggleControlVisibility() {
        boolean visible = (mDecorView.getSystemUiVisibility()
                & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        if (visible) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    @Override
    public void startTimer() {
        timerFragment.startTimer();
    }

    @Override
    public void setTimer(int millisecond) {
        timerFragment.setTimer(millisecond);
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
