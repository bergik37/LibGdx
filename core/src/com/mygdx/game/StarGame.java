package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StarGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Background background;
    private Hero hero;


    public Hero getHero() {
        return hero;
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Background(this);
        hero = new Hero();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        //ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        background.render(batch);
        hero.render(batch);
        batch.end();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
