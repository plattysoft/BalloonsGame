package com.plattysoft.balloongame;

import com.plattysoft.sage.BodyType;
import com.plattysoft.sage.GameEngine;
import com.plattysoft.sage.ScreenGameObject;
import com.plattysoft.sage.Sprite;
import com.plattysoft.sage.particles.ParticleSystem;
import com.plattysoft.sage.sound.GameEvent;

/**
 * Created by Raul Portales on 23/03/15.
 */
public class Balloon extends Sprite {

    public static final int EXPLOSION_PARTICLES = 15;
    private final GameController mController;

    private final double mSpeed;
    private final int mInitialY;
    private double mSpeedY;
    private ParticleSystem mExplisionParticleSystem;

    public Balloon(GameController gameController, GameEngine gameEngine, BalloonColor color) {
        super(gameEngine, color.getImageResId(), BodyType.Circular);
        mSpeed = 100d*mPixelFactor/1000d;
        mController = gameController;
        mExplisionParticleSystem = new ParticleSystem(gameEngine, EXPLOSION_PARTICLES, color.getParticleImageResId(), 700)
                .setSpeedRange(50, 140)
                .setFadeOut(300)
                .setInitialRotationRange(0, 360)
                .setRotationSpeedRange(-180, 180);
        mInitialY = gameEngine.mHeight;
    }

    @Override
    public void startGame(GameEngine gameEngine) {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mY += mSpeedY * elapsedMillis;
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (mY < -mHeight) {
            gameEngine.onGameEvent(GameEvent.BalloonMissed);
            removeFromGameEngine(gameEngine);
        }
    }

    @Override
    public void onRemovedFromGameEngine() {
        mController.returnToPool(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        super.onCollision(gameEngine, otherObject);
        if (otherObject instanceof GameFragment.DummyObject) {
            // Explode and remove from game
            explode(gameEngine);
            removeFromGameEngine(gameEngine);
            gameEngine.onGameEvent(GameEvent.BalloonHit);
        }
    }

    public void explode(GameEngine gameEngine) {
        mExplisionParticleSystem.oneShot(gameEngine, mX + mWidth / 2, mY + mHeight / 2, EXPLOSION_PARTICLES);
    }

    public void init(GameEngine gameEngine) {
        // They initialize in a [-30, 30] degrees angle
        mSpeedY = - mSpeed * (gameEngine.mRandom.nextInt(50) + 50) / 100f;

        // Asteroids initialize in the central 50% of the screen horizontally
        mX = gameEngine.mRandom.nextInt(gameEngine.mWidth * 6 / 8)+gameEngine.mWidth/8;
        // They initialize outside of the screen vertically
        mY = mInitialY;
        mRotation = gameEngine.mRandom.nextInt(20)-10;
    }

}
