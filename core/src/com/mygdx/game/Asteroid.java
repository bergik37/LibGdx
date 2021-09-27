package com.mygdx.game;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Asteroid extends BaseActor implements Cloneable {
    public Asteroid(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("asteroid.png");
        setSize(30,30);
        float random = MathUtils.random(30);
        addAction( Actions.forever( Actions.rotateBy(30 + random, 1) ) );
        setSpeed(50 + random);
        setMaxSpeed(50 + random);
        setDeceleration(0);
        setMotionAngle( MathUtils.random(360) );
    }
    public void act(float dt)
    {
        super.act(dt);
        applyPhysics(dt);
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
