package com.plattysoft.sage;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Raul Portales on 02/01/16.
 */
public class GameBaseFragment extends Fragment {

    public void showDialog (BaseCustomDialog newDialog) {
        showDialog(newDialog, false);
    }

    public void showDialog (BaseCustomDialog newDialog, boolean dismissOtherDialog) {
        getMainActivity().showDialog(newDialog, dismissOtherDialog);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().applyTypeface(view);
        final ViewTreeObserver obs = view.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public synchronized void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = getView().getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeGlobalOnLayoutListener(this);
                    } else {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                    }
                    onLayoutCompleted();
                }
            }
        });
    }

    public GameBaseActivity getMainActivity() {
        return (GameBaseActivity) getActivity();
    }

    protected void onLayoutCompleted() {
    }

    public boolean onBackPressed() {
        return false;
    }
}
