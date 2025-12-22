package com.tbane.mysticgems.Game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.Time;
import com.badlogic.gdx.math.Rectangle;

public class Fragment {

    public int color;
    public Rectangle rect;
    public Rectangle textureRect;
    public Vector2 velocity;
    public float life;
    public float createTime;

    public Fragment(int color, Rectangle rect, Rectangle textureRect, Vector2 velocity, float life) {

        this.color = color;
        this.rect = rect;
        this.textureRect = textureRect;
        this.velocity = velocity;
        this.life = life;

        createTime = Time.currentTime;
    }

    public void draw() {

        float r,g,b;
        switch(color){
            case 0:
                r = 255.0f/255.0f;
                g = 47.0f/255.0f;
                b = 47.0f/255.0f;
                break;
            case 1:
                r = 47/255.0f;
                g = 255.0f/255.0f;
                b = 47.0f/255.0f;
                break;
            case 2:
                r = 47/255.0f;
                g = 47.0f/255.0f;
                b = 255.0f/255.0f;
                break;
            case 3:
                r = 255.0f/255.0f;
                g = 255.0f/255.0f;
                b = 47.0f/255.0f;
                break;
            case 4:
                r = 255.0f/255.0f;
                g = 47.0f/255.0f;
                b = 255.0f/255.0f;
                break;
            case 5:
                r = 47.0f/255.0f;
                g = 255.0f/255.0f;
                b = 255.0f/255.0f;
                break;
            default:
                r = 127.0f/255.0f;
                g = 127.0f/255.0f;
                b = 127.0f/255.0f;
                break;
        };

        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Renderer.shapeRenderer.setColor(r, g, b, 1.0f);
        Renderer.shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        Renderer.shapeRenderer.end();
    }
}
