package com.plattysoft.balloongame;

import com.plattysoft.balloongame.R;

/**
 * Created by Raul Portales on 01/01/16.
 */
public enum BalloonColor {
    Red,
    Yellow,
    Green,
    Orange,
    Blue;

    public int getImageResId() {
        switch (this) {
            case Blue:
                return R.drawable.balloon_blue;
            case Green:
                return R.drawable.balloon_green;
            case Orange:
                return R.drawable.balloon_orange;
            case Red:
                return R.drawable.balloon_red;
            case Yellow:
                return R.drawable.balloon_yellow;
        }
        return 0;
    }

    public int getParticleImageResId() {
        switch (this) {
            case Blue:
                return R.drawable.particle_blue;
            case Green:
                return R.drawable.particle_green;
            case Orange:
                return R.drawable.particle_orange;
            case Red:
                return R.drawable.particle_red;
            case Yellow:
                return R.drawable.particle_yellow;
        }
        return 0;
    }
}
