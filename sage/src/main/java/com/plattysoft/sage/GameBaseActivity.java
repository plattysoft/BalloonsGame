package com.plattysoft.sage;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plattysoft.sage.sound.SoundManager;

/**
 * Created by Raul Portales on 02/01/16.
 */
public abstract class GameBaseActivity extends Activity {

    protected static final String TAG_FRAGMENT = "content";

    private Typeface mCustomTypeface;
    private BaseCustomDialog mCurrentDialog;
    private SoundManager mSoundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mCustomTypeface = Typeface.createFromAsset(getAssets(), getTypefacePath());
        mSoundManager = createSoundManager();
    }

    protected abstract SoundManager createSoundManager();

    protected String getTypefacePath() {
        return "";
    }

    public void showDialog (BaseCustomDialog newDialog, boolean dismissOtherDialog) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (dismissOtherDialog) {
                mCurrentDialog.dismiss();
            }
            else {
                return;
            }
        }
        mCurrentDialog = newDialog;
        mCurrentDialog.show();
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (mCurrentDialog.dispatchGenericMotionEvent(event)) {
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent (KeyEvent event) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (mCurrentDialog.dispatchKeyEvent(event)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            return;
        }
        final GameBaseFragment fragment = (GameBaseFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public SoundManager getSoundManager() {
        return mSoundManager;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundManager.pauseBgMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoundManager.resumeBgMusic();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    public void applyTypeface (View view) {
        if (view instanceof ViewGroup) {
            // Apply recursively to all the children
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i=0; i<viewGroup.getChildCount(); i++) {
                applyTypeface(viewGroup.getChildAt(i));
            }
        }
        else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTypeface(mCustomTypeface);
        }
    }

    protected void navigateToFragment(GameBaseFragment dst) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void navigateBack() {
        // Do a push on the navigation history
        getFragmentManager().popBackStack();
    }
}
