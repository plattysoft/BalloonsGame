package com.plattysoft.balloongame;

import android.app.Fragment;
import android.os.Bundle;

import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;

import com.plattysoft.sage.GameBaseActivity;


public class BalloonGameActivity extends GameBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container,createMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
    }

    protected String getTypefacePath() {
        return "ttf/scribble box demo.ttf";
    }

    protected Fragment createMenuFragment() {
        return new MainMenuFragment();
    }

    public void startGame() {
        // Navigate the the game fragment, which makes the start automatically
        navigateToFragment(new GameFragment());
    }

}
