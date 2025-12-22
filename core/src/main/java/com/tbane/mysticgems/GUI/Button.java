package com.tbane.mysticgems.GUI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Time;
import com.tbane.mysticgems.Renderer;

public class Button extends ElementGUI {

    public Rectangle _rect;
    private final Texture _normalTexture, _hoverTexture, _pressedTexture;
    public enum states { Idle, Hover, Pressed }
    public states _state;
    float _clickTime;
    public Runnable onclick_func;

    public Button(Texture normalTexture, Texture hoverTexture, Texture pressedTexture, int x, int y, int width, int height) {
        super();
        _rect = new Rectangle(x, y, width, height);
        _normalTexture = normalTexture;
        _hoverTexture = hoverTexture;
        _pressedTexture = pressedTexture;


        _state = states.Idle;
        _clickTime = 0.0f;

        onclick_func = () -> {};
    }
    private void unclick() {
        _state = states.Idle;
    }

    private void hover() {
        _state = states.Hover;
    }

    private void click() {
        _state = states.Pressed;
        _clickTime = Time.currentTime;
    }

    @Override
    public void handleEvents() {



        if(_rect.contains(MyInput.processor.getTouchX(), MyInput.processor.getTouchY())) {
            if(MyInput.processor.isTouchDown()) {
                GUI.ElementGUI_pressed = this;
            }
		else if(MyInput.processor.isTouchUp()) {
                if (GUI.ElementGUI_pressed == this) {
                    click();
                    return;
                }
            }
        }

        if(_state!= states.Pressed) {
            if(MyInput.processor.isTouchMoved() || MyInput.processor.isTouchDown()){

                if(_rect.contains(MyInput.processor.getTouchPosition())){
                    GUI.ElementGUI_hovered = this;
                }else {

                    if(GUI.ElementGUI_hovered == this){
                        GUI.ElementGUI_hovered = null;
                    }
                }
            }
        }

    }

    @Override
    public void update() {
        if (_state == states.Pressed) {
            if ((Time.currentTime - _clickTime) > 0.05f) {
                onclick_func.run();

                if(GUI.ElementGUI_pressed == this)
                    GUI.ElementGUI_pressed = null;
                unclick();
            }
        }
        else if (GUI.ElementGUI_hovered == this) {
            hover();
        }
        else
            unclick();
    }

    @Override
    public void draw() {
        Texture tex;

        switch (_state) {
            case Hover:
                tex = _hoverTexture;
                break;
            case Pressed:
                tex = _pressedTexture;
                break;
            default:
                tex = _normalTexture;
                break;
        }

        Sprite sprite = new Sprite(tex);
        sprite.setBounds(_rect.x, _rect.y, _rect.width, _rect.height);
        sprite.draw(Renderer.batch);
    }
}
