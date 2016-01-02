package com.plattysoft.balloongame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.plattysoft.sage.sound.SoundManager;

/**
 * Created by Raul Portales on 03/03/15.
 */
public class MainMenuFragment extends BalloonGameBaseFragment implements View.OnClickListener, QuitDialog.QuitDialogListener {

    public static final int TITLE_ANIMATION_DURATION = 1600;

    public MainMenuFragment() {
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        return rootView;
    }

    protected int getLayoutResId() {
        return R.layout.fragment_main_menu;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.btn_sound).setOnClickListener(this);
        view.findViewById(R.id.btn_music).setOnClickListener(this);

        updateSoundAndMusicButtons();
    }

    @Override
    protected void onLayoutCompleted() {
        animateTitles();
    }

    private void animateTitles() {
        View title = getView().findViewById(R.id.main_title);
        Animation titleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.title_enter);
        title.startAnimation(titleAnimation);

    }

    private void updateSoundAndMusicButtons() {
        SoundManager soundManager = getMainActivity().getSoundManager();
        boolean music = soundManager.getMusicStatus();
        ImageView btnMusic = (ImageView) getView().findViewById(R.id.btn_music);
        if (music) {
            btnMusic.setImageResource(R.drawable.music_on_no_bg);
        }
        else {
            btnMusic.setImageResource(R.drawable.music_off_no_bg);
        }
        boolean sound = soundManager.getSoundStatus();
        ImageView btnSounds= (ImageView) getView().findViewById(R.id.btn_sound);
        if (sound) {
            btnSounds.setImageResource(R.drawable.sounds_on_no_bg);
        }
        else {
            btnSounds.setImageResource(R.drawable.sounds_off_no_bg);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start){
           getBalloonsActivity().startGame();
        }
        else if (v.getId() == R.id.btn_music) {
            SoundManager soundManager = getMainActivity().getSoundManager();
            soundManager.toggleMusicStatus();
            updateSoundAndMusicButtons();
        }
        else if (v.getId() == R.id.btn_sound) {
            SoundManager soundManager = getMainActivity().getSoundManager();
            soundManager.toggleSoundStatus();
            updateSoundAndMusicButtons();
        }
    }

    private BalloonGameActivity getBalloonsActivity() {
        return (BalloonGameActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
        boolean consumed = super.onBackPressed();
        if (!consumed){
            QuitDialog quitDialog = new QuitDialog(getMainActivity());
            quitDialog.setListener(this);
            showDialog(quitDialog);
            return true;
        }
        return consumed;
    }

    @Override
    public void exit() {
        getMainActivity().finish();
    }
}
