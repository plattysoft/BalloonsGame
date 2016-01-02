package com.plattysoft.sage;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import static android.view.View.*;

/**
 * Created by Raul Portales on 15/04/15.
 */
public abstract class BaseCustomDialog implements OnTouchListener, Animation.AnimationListener {

    private boolean mIsShowing;

    protected final GameBaseActivity mParent;
    private ViewGroup mRootLayout;
    private View mRootView;
    private boolean mIsHiding;

    public BaseCustomDialog(GameBaseActivity activity) {
        mParent = activity;
    }

    protected void onViewClicked() {
        // Ignore clicks on this view
    }

    protected void setContentView(int dialogResId) {
        ViewGroup activityRoot = (ViewGroup) mParent.findViewById(android.R.id.content);
        mRootView = LayoutInflater.from(mParent).inflate(dialogResId, activityRoot, false);
        mParent.applyTypeface(mRootView);
    }

    public void show() {
        if (mIsShowing) {
            return;
        }
        mIsShowing = true;
        mIsHiding = false;

        ViewGroup activityRoot = (ViewGroup) mParent.findViewById(android.R.id.content);
        mRootLayout = (ViewGroup) LayoutInflater.from(mParent).inflate(R.layout.my_overlay_dialog, activityRoot, false);
        activityRoot.addView(mRootLayout);
        mRootLayout.setOnTouchListener(this);
        mRootLayout.addView(mRootView);
        startShowAnimation();
    }

    private void startShowAnimation() {
//        Animation fadeIn = AnimationUtils.loadAnimation(mParent, R.animator.fade_in);
        Animation dialogIn = AnimationUtils.loadAnimation(mParent, getEnterAnimatorResId());
//        mRootLayout.startAnimation(fadeIn);
        mRootView.startAnimation(dialogIn);
    }

    protected abstract int getEnterAnimatorResId();

    protected abstract int getExitAnimatorResId();

    public void dismiss() {
        if (!mIsShowing) {
            return;
        }
        if (mIsHiding) {
            return;
        }
        mIsHiding = true;
        startHideAnimation();
    }

    protected void onDismissed() {
    }

    private void startHideAnimation() {
        Animation dialogOut = AnimationUtils.loadAnimation(mParent, getExitAnimatorResId());
//        Animation fadeOut = AnimationUtils.loadAnimation(mParent, R.animator.fade_out);
        dialogOut.setAnimationListener(this);
        mRootView.startAnimation(dialogOut);
//        mRootLayout.startAnimation(fadeOut);
    }

    private void hideViews() {
        mRootLayout.removeView(mRootView);
        ViewGroup activityRoot = (ViewGroup) mParent.findViewById(android.R.id.content);
        activityRoot.removeView(mRootLayout);
    }

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Ignoring touch events on the gray outside
        return true;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    @Override
    public void onAnimationStart(Animation paramAnimation) {
    }

    @Override
    public void onAnimationEnd(Animation paramAnimation) {
        hideViews();
        mIsShowing = false;
        onDismissed();
        mParent.setShowingDialog(false);
    }

    @Override
    public void onAnimationRepeat(Animation paramAnimation) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return false;
    }
}
