package com.tbane.mysticgems.Game;

import com.badlogic.gdx.math.Vector2;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.Time;

public class ComboText {

    public float createTime;
    public int value;
    public Vector2 position;

    public ComboText(int value, Vector2 position){
        this.value = value;
        this.position = position;
        this.createTime = Time.currentTime;
    }

    public ComboText(int value, float x, float y){
        this.value = value;
        this.position = new Vector2(x,y);
        this.createTime = Time.currentTime;
    }

    public void draw() {
        if(value == -1){
            Font.ComboTextMiss.draw(Renderer.batch, "miss", position.x, position.y);
        }
        else{
            Font.ComboTextValue.draw(Renderer.batch, "x"+Integer.toString(value), position.x, position.y);
        }


    }
}
