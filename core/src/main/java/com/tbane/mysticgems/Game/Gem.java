package com.tbane.mysticgems.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tbane.mysticgems.AssetsManager;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.Time;


public class Gem {

    public static Vector2 size = new Vector2(96, 96);
    public int color;
    public float dt;

    public Vector2 position;
    public Vector2 realPosition;
    public float rotation;
    public enum GemEffect { None, DestroyType, DestroyAll, BonusPoints }
    public GemEffect effect;

    public Gem(Vector2 position, int color){
        this.position = position;
        this.realPosition = position;
        this.color = color;

        dt = (float)(Math.random() * 360.0f) * (float)Math.PI / 180f;


        int e = (int)(Math.random()*200.0f);
        if(e > 190) effect = GemEffect.DestroyAll;
        else if(e > 170) effect = GemEffect.DestroyType;
        else if(e > 145) effect = GemEffect.BonusPoints;
        else effect = GemEffect.None;


    }

    public void drawColor() {

        // calc the values of color of shape of bubble
        float r,g,b;
        float t = Time.currentTime;
        switch (effect) {

            case DestroyAll: {
                GemColor c = GemColor.rgb((t * 60f) % 360f, 1f, 1f);
                r = c.r;
                g = c.g;
                b = c.b;
                break;
            }

            case DestroyType: {
                float pulse = 0.7f + 0.3f * (float)Math.sin(t * 6f); // 0.4–1.0
                GemColor c = GemColor.rgb(0f, 1f, pulse); // hue 0 = red
                r = c.r;
                g = c.g;
                b = c.b;
                break;
            }

            case BonusPoints: {
                float pulse = 0.8f + 0.2f * (float)Math.sin(t * 5f);
                GemColor c = GemColor.rgb(45f, 1f, pulse); // 45° = gold
                r = c.r;
                g = c.g;
                b = c.b;
                break;
            }

            default:
                r = g = b = 47f / 255f;
                break;
        }

        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Renderer.shapeRenderer.setColor(r, g, b, 0.5f);
        Renderer.shapeRenderer.circle(position.x, position.y, Math.max(size.x/2, size.y/2));
        Renderer.shapeRenderer.end();
    }

    public void drawBubble() {

        Texture bubbleTex = AssetsManager.getTexture("tex/bubble.png");
        if(bubbleTex != null){
            // draw bubble sprite
            Sprite sprite = new Sprite(bubbleTex);
            sprite.setSize(size.x, size.y);
            sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
            sprite.setCenter(position.x, position.y);   // positioning with centering
            sprite.setRotation(rotation);
            sprite.draw(Renderer.batch);
        }
    }

    public void drawGem() {

        // draw the gem
        Texture gemTex = AssetsManager.getTexture(color);
        if(gemTex != null){
            Sprite sprite = new Sprite(gemTex);
            sprite.setSize(size.x, size.y);
            sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
            sprite.setCenter(position.x, position.y);   // positioning with centering
            sprite.setRotation(rotation);
            sprite.draw(Renderer.batch);
        }
    }
}
