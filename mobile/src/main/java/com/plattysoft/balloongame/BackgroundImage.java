package com.plattysoft.balloongame;

import android.graphics.Canvas;

import com.plattysoft.sage.BodyType;
import com.plattysoft.sage.GameEngine;
import com.plattysoft.sage.Sprite;

/**
 * Created by Raul Portales on 28/12/15.
 */
public class BackgroundImage extends Sprite {

    private boolean mFirstRun;

    protected BackgroundImage(GameEngine gameEngine, int drawableRes) {
        super(gameEngine, drawableRes, BodyType.None);
    }

    @Override
    public void startGame(GameEngine gameEngine) {
        mFirstRun = true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mFirstRun) {
            double margin = (mWidth - canvas.getWidth()) / 2d;
            mX = -margin;
            mFirstRun = false;
        }
        super.onDraw(canvas);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    }
}
