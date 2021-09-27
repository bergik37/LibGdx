package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class StarShip extends GameStart {

    private Asteroid asteroid;

    private Ship ship;
    private BaseActor background;
    ArrayList<Asteroid> asteroids=new ArrayList<>();



    @Override
    public void initialize() {
        asteroids.add(new Asteroid(350,350,mainStage));
        background = new BaseActor(0, 0, mainStage);
        background.loadTexture("bg.png");
        background.setSize(800, 600);

        //asteroid=new Asteroid(350,350,mainStage);
        //asteroids.get(0);

        for (int i = 0; i < 50; i++) {
            asteroid=new Asteroid(MathUtils.random( 0, 1280) , MathUtils.random( 0, 768),mainStage);
        }

        ship = new Ship(200, 200, mainStage);

    }

    @Override
    public void update(float dt) {

    }
}
