package com.plattysoft.balloongame;

import android.graphics.Canvas;

import com.plattysoft.sage.GameEngine;
import com.plattysoft.sage.GameObject;
import com.plattysoft.sage.sound.GameEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class GameController extends GameObject {

    private static final int TIME_BETWEEN_BALLOONS = 1400;
    private static final int POOL_SIZE = 15;

    private long mCurrentMillis;
    private List<Balloon> mBalloonPool = new ArrayList<Balloon>();

    private int mBalloonsSpawned;

    public GameController(GameEngine gameEngine, GameFragment parent) {
        BalloonColor[] colors = BalloonColor.values();
        // We initialize the pool of items now
        for (int i=0; i< POOL_SIZE; i++) {
            // Add them with 4 different images
            mBalloonPool.add(new Balloon(this, gameEngine, colors[i%colors.length]));
        }
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        mCurrentMillis = 0;
        mBalloonsSpawned = 0;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mCurrentMillis += elapsedMillis;
        // TODO: make time between balloons variable
        long waveTimestamp = mBalloonsSpawned * TIME_BETWEEN_BALLOONS;
        if (mCurrentMillis > waveTimestamp) {
            // get a ballon at random from the pool
            Balloon a = mBalloonPool.remove(gameEngine.mRandom.nextInt(mBalloonPool.size()));
            a.init(gameEngine);
            a.addToGameEngine(gameEngine, mLayer);
            mBalloonsSpawned++;
            return;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Balloon balloon) {
        mBalloonPool.add(balloon);
    }
}
