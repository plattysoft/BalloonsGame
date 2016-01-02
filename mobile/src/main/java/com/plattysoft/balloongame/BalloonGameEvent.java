package com.plattysoft.balloongame;

import com.plattysoft.sage.sound.GameEvent;

/**
 * Created by Raul Portales on 09/04/15.
 */
public enum BalloonGameEvent implements GameEvent {
    BalloonHit,
    BalloonMissed,
    GameFinished;
}
