package com.plattysoft.balloongame;

import android.content.Context;

import com.plattysoft.sage.sound.GameEvent;
import com.plattysoft.sage.sound.SoundManager;

import java.util.HashMap;

/**
 * Created by Raul Portales on 02/01/16.
 */
public class BalloonsSoundManager extends SoundManager {

    public BalloonsSoundManager(Context context){
        super(context);
    }

    protected void loadEventSounds(HashMap<GameEvent, Integer> mSoundsMap) {
        loadEventSound(mContext, BalloonGameEvent.BalloonHit, "balloon_pop.wav");
    }

    @Override
    protected String getMusicFileAssetPath() {
        return "sfx/Dean_Caedab_-_Everyday_Success.mp3";
    }
}
