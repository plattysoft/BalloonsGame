package com.plattysoft.balloongame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.plattysoft.sage.BodyType;
import com.plattysoft.sage.GameEngine;
import com.plattysoft.sage.GameView;
import com.plattysoft.sage.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class GameFragment extends BalloonGameBaseFragment implements InputManager.InputDeviceListener, PauseDialog.PauseDialogListener, View.OnTouchListener {

    private static final int DUMMY_OBJECT_POOL_SIZE = 10;
    private GameEngine mGameEngine;

    private List<DummyObject> mDummyObjectPool = new ArrayList<DummyObject>();

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    protected void onLayoutCompleted() {
        prepareAndStartGame();
    }

    private void prepareAndStartGame() {
        GameView gameView = (GameView) getView().findViewById(R.id.gameView);
        mGameEngine = new GameEngine(getActivity(), gameView, 4);
        mGameEngine.setSoundManager(getMainActivity().getSoundManager());
        new GameController(mGameEngine, GameFragment.this).addToGameEngine(mGameEngine, 2);
//        new FPSCounter(mGameEngine).addToGameEngine(mGameEngine, 2);
        new BackgroundImage(mGameEngine, R.drawable.background).addToGameEngine(mGameEngine, 0);
        mGameEngine.startGame();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
            inputManager.registerInputDeviceListener(GameFragment.this, null);
        }
        gameView.postInvalidate();

        for (int i=0; i<DUMMY_OBJECT_POOL_SIZE; i++) {
            mDummyObjectPool.add(new DummyObject(mGameEngine));
        }

        getView().findViewById(R.id.gameView).setOnTouchListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            InputManager inputManager = (InputManager) getActivity().getSystemService(Context.INPUT_SERVICE);
            inputManager.unregisterInputDeviceListener(this);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()){
            pauseGameAndShowPauseDialog();
            return true;
        }
        return super.onBackPressed();
    }

    private void pauseGameAndShowPauseDialog() {
        if (mGameEngine.isPaused()) {
            return;
        }
        mGameEngine.pauseGame();
        PauseDialog dialog = new PauseDialog(getMainActivity());
        dialog.setListener(this);
        showDialog(dialog);
    }

    public void resumeGame() {
        mGameEngine.resumeGame();
    }

    @Override
    public void exitGame() {
        mGameEngine.stopGame();
        getMainActivity().navigateBack();
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {

    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        if (mGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {
        // On any motion down, calculate colisions with the current point
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            if (!mDummyObjectPool.isEmpty()) {
                DummyObject dummy = mDummyObjectPool.remove(0);
                dummy.init(event);
                dummy.addToGameEngine(mGameEngine, 0);
            }
            return true;
        }
        return false;
    }

    public class DummyObject extends Sprite {

        private long mTotalMilis;

        public DummyObject(GameEngine gameEngine) {
            super(gameEngine, R.drawable.touch_visual, BodyType.Circular);
        }


        @Override
        public void startGame(GameEngine gameEngine) {

        }

        @Override
        public void onRemovedFromGameEngine() {
            super.onRemovedFromGameEngine();
            mDummyObjectPool.add(this);
        }

        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            mTotalMilis += elapsedMillis;
            if (mTotalMilis > 200) {
                gameEngine.removeGameObject(this);
            }
        }

        public void init(MotionEvent event) {
            mX = event.getX(event.getActionIndex());
            mY = event.getY(event.getActionIndex());
            mTotalMilis = 0;
        }
    }
}
