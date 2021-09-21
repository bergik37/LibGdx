package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Background {
    private final int STAR_COUNT = 1000;
    private StarGame game;
    private Texture textureCosmos;
    private Texture textureStar;
    private Texture textureAsteroid;
    private Star[] stars;
    private Asteroid[] asteroids;
    public Background(StarGame game) {
        this.textureCosmos = new Texture("bg.png");
        this.textureStar = new Texture("star16.png");
        this.textureAsteroid = new Texture("asteroid.png");
        this.game = game;
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
        this.asteroids = new Asteroid[STAR_COUNT];
        for (int i = 0; i < asteroids.length; i++) {
            asteroids[i] = new Asteroid();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16,
                    stars[i].scale, stars[i].scale, 0, 0, 0, 16, 16, false, false);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16,
                        stars[i].scale * 2, stars[i].scale * 2, 0, 0, 0, 16, 16, false, false);
            }
        }
        for (int i = 0; i < 5; i++) {
            batch.draw(textureAsteroid, asteroids[i].position.x - 8, asteroids[i].position.y - 8, 80, 80, 40, 40,
                    asteroids[i].scale, asteroids[i].scale, 0, 0, 0, 160, 160, false, false);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureAsteroid, asteroids[i].position.x - 8, asteroids[i].position.y - 8, 80, 80, 40, 40,
                        asteroids[i].scale * 2, asteroids[i].scale * 2, 0, 0, 0, 40, 40, false, false);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
        for (int i = 0; i < stars.length; i++) {
            asteroids[i].update(dt);
        }
    }

    private class Star {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - game.getHero().getLastDisplacement().x * 15) * dt;
            position.y += (velocity.y - game.getHero().getLastDisplacement().y * 15) * dt;

            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }

    private class Asteroid {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;

        public Asteroid() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - game.getHero().getLastDisplacement().x * 15) * dt;
            position.y += (velocity.y - game.getHero().getLastDisplacement().y * 15) * dt;

            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }
}
