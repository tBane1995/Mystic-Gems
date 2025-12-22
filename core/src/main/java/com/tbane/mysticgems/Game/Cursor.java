package com.tbane.mysticgems.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.Time;

public class Cursor {

    public static final float minRadius = 32;
    public static final float maxRadius = 80;
    public static final float grownLimitSeconds = 0.3f;

    public static Vector2 position;
    public static float startTouchTime;


    static {
        position = new Vector2(0,0);
        startTouchTime = 0;
    }

    public static void setOn(Vector2 pos) {
        position.set(pos.x,pos.y);
        startTouchTime = Time.currentTime;
    }

    public static void draw() {
        if((Time.currentTime-startTouchTime) > grownLimitSeconds){
            return;
        }

        float t = (Time.currentTime - startTouchTime)/grownLimitSeconds;
        float radius = Time.easeOut(t)*(maxRadius-minRadius) + minRadius;

        //draw colored shape of bubble
        Renderer.batch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Renderer.shapeRenderer.setColor(255.0f/255.0f,47.0f/255.0f, 47.0f/255.0f, 0.5f );
        Renderer.shapeRenderer.circle(position.x, position.y, radius);
        Renderer.shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.batch.begin();
    }

}
