package com.tbane.mysticgems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer {

    public static int VIRTUAL_WIDTH = 720;
    public static int VIRTUAL_HEIGHT = 1612;
    public static OrthographicCamera camera;
    public static SpriteBatch batch = new SpriteBatch();    // for draw all stuff
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();    // for draw only the colored shapes (dark shape of bubbles)

    public static Viewport viewport;

    static {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        camera.position.set( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);

        //camera.update();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public static void begin() {
        batch.begin();
    }

    public static void end() {
        batch.end();
    }

}
