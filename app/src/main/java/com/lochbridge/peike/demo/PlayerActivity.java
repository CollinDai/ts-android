package com.lochbridge.peike.demo;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lochbridge.peike.demo.fragment.PlayerControlFragment;
import com.lochbridge.peike.demo.fragment.TimerFragment;
import com.lochbridge.peike.demo.fragment.player.PlayerFragment;
import com.lochbridge.peike.demo.fragment.player.SimplePlayerFragment;
import com.lochbridge.peike.demo.util.Constants;

public class PlayerActivity extends AppCompatActivity implements PlayerControlFragment.PlayerControlListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        int subId = getIntent().getIntExtra(Constants.EXTRA_SUB_ID, 0);
        int playerTheme = getIntent().getIntExtra(Constants.EXTRA_PLAYER_THEME, -1);
        PlayerFragment playerFragment = null;
        switch (playerTheme) {
            case PlayerFragment.SIMPLE_THEME:
            default:
                playerFragment = SimplePlayerFragment.newInstance(subId);
        }

        TimerFragment timerFragment = new TimerFragment();
        PlayerControlFragment controlFragment = new PlayerControlFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_player, playerFragment);
        fragmentTransaction.replace(R.id.frag_timer, timerFragment);
        fragmentTransaction.replace(R.id.frag_player_control, controlFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void nextTapped() {
        // show timer
        // Timer adjust to next srtItem.startTime
        //
    }

    @Override
    public void previousTapped() {
    }
}
