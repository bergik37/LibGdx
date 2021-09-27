package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Ship extends BaseActor{

    public Ship(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("ship.png");
        setSize(30,30);
        setAcceleration(400);
        setMaxSpeed(100);
        setDeceleration(400);
    }

    public void act(float dt)
    {
        super.act( dt );
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            accelerateAtAngle(180);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            accelerateAtAngle(0);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            accelerateAtAngle(90);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            accelerateAtAngle(270);
        applyPhysics(dt);
        setAnimationPaused( !isMoving() );
        if ( getSpeed() > 0 )
            setRotation( getMotionAngle() );
    }

}
