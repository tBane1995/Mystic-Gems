package com.tbane.mysticgems;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetsManager {
    public static AssetManager manager;

    static {

        manager = new AssetManager();

        loadTexture("tex/topBoard.png");
        loadTexture("tex/bottomBoard.png");
        loadTexture("tex/mainBoard.png");
        loadTexture("tex/panel.png");
        loadTexture("tex/smallPanel.png");

        loadTexture("tex/titleFrame.png");

        loadTexture("tex/mainMenuLeftSign.png");
        loadTexture("tex/mainMenuRightSign.png");

        loadTexture("tex/menuButtonNormal.png");
        loadTexture("tex/menuButtonHover.png");
        loadTexture("tex/menuButtonPressed.png");

        loadTexture("tex/panelButtonNormal.png");
        loadTexture("tex/panelButtonHover.png");
        loadTexture("tex/panelButtonPressed.png");

        loadTexture("tex/backButtonNormal.png");
        loadTexture("tex/backButtonHover.png");
        loadTexture("tex/backButtonPressed.png");

        loadTexture("tex/awardCup.png");

        loadTexture("tex/underwater.png");

        loadTexture("tex/bubble.png");
        loadTexture("tex/red.png");
        loadTexture("tex/green.png");
        loadTexture("tex/blue.png");
        loadTexture("tex/cyan.png");
        loadTexture("tex/magenta.png");
        loadTexture("tex/yellow.png");

        manager.finishLoading();
    }

    public static void loadTexture(String path) {
        if (!manager.isLoaded(path, Texture.class)) {
            manager.load(path, Texture.class);
        }
    }

    public static Texture getTexture(String path) {
        if (manager.isLoaded(path, Texture.class)) {
            return manager.get(path, Texture.class);
        }
        return null;
    }

    public static Texture getTexture(int colorIndex) {
        Texture texture;

        switch (colorIndex) {
            case 0: texture = getTexture("tex/red.png"); break;
            case 1: texture = getTexture("tex/green.png"); break;
            case 2: texture = getTexture("tex/blue.png"); break;
            case 3: texture = getTexture("tex/yellow.png"); break;
            case 4: texture = getTexture("tex/magenta.png"); break;
            case 5: texture = getTexture("tex/cyan.png"); break;
            default: texture = null; break;
        }

        return texture;
    }

}
