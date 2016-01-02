package com.plattysoft.balloongame;

import android.view.KeyEvent;
import android.view.View;

import com.plattysoft.sage.BaseCustomDialog;
import com.plattysoft.sage.GameBaseActivity;

/**
 * Created by Raul Portales on 16/04/15.
 */
public class QuitDialog extends BaseCustomDialog implements View.OnClickListener {

    private QuitDialogListener mListener;
    private int mSelectedId;

    public QuitDialog(GameBaseActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_quit);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
     }

    public void setListener(QuitDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected int getEnterAnimatorResId() {
        return R.animator.enter_from_top;
    }

    @Override
    protected int getExitAnimatorResId() {
        return R.animator.exit_trough_top;
    }

    @Override
    public void onClick(View v) {
        mSelectedId = v.getId();
        dismiss();
    }

    @Override
    protected void onDismissed() {
        if (mSelectedId == R.id.btn_exit) {
            mListener.exit();
        }
    }

    public interface QuitDialogListener {
        void exit();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_A ||
                event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (findViewById(R.id.btn_resume).isFocused() ||
                    findViewById(R.id.btn_exit).isFocused()) {
                // Return false, so a proper click is sent
                return false;
            }
            return true;
        }
        return false;
    }

}
