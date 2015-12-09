package com.lochbridge.peike.demo;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lochbridge.peike.demo.fragment.player.PlayerFragment;
import com.lochbridge.peike.demo.fragment.player.SimplePlayerFragment;
import com.lochbridge.peike.demo.util.Constants;

public class PlayerActivity extends AppCompatActivity {

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

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_player, playerFragment);
        fragmentTransaction.commit();
    }
}
