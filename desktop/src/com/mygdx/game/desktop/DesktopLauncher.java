package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.StarShip;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Game myGame = new StarShip();
        LwjglApplication launcher =
                new LwjglApplication( myGame, "Star ship", 800, 600 );
    }
}
