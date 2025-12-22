package com.tbane.mysticgems.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.Time;

import java.awt.image.Kernel;

public class TextInput {

    String _text;            // is only text
    String _defaultText;    // if _text is empty then return _defaultText
    String _messageText;     // if text is empty then show this message
    Rectangle _rect;
    int _limitCharacters;
    enum TextInputState { Idle, TextEntered }
    TextInputState _state;
    int _cursorPosition;
    public TextInput(String messageText, String text, String defaultText, Rectangle rect, int limitCharacters){
        _messageText = messageText;
        _text = text;
        _defaultText = defaultText;
        _rect = new Rectangle(rect);
        _limitCharacters = limitCharacters;
        _state = TextInputState.Idle;
        _cursorPosition = text.length();
    }

    public String getText() {
        if(!_text.isEmpty())
            return _text;
        else
            return _defaultText;
    }

    public void handleEvents() {
        if(MyInput.processor.isTouchDown()){
            if(_rect.contains(MyInput.processor.getTouchPosition())){
                _state = TextInputState.TextEntered;
                Gdx.input.setOnscreenKeyboardVisible(true);
            }else{
                _state = TextInputState.Idle;
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        }

        if(_state == TextInputState.TextEntered){
            while(MyInput.processor.isKeyTyped()){

                char key = MyInput.processor.getLastChar();

                if(key == '\r' || key == '\n') {
                    // enter
                    if(!_text.isEmpty() && !_text.trim().isEmpty()) {
                        System.out.println("Enter pressed: " + _text);
                        Gdx.input.setOnscreenKeyboardVisible(false);
                        _state = TextInputState.Idle;
                    }
                }
                else if(key == '\b' && !_text.isEmpty() && _cursorPosition > 0){
                    // backspace
                    _text = _text.substring(0, _cursorPosition - 1) + _text.substring(_cursorPosition);
                    _cursorPosition--;
                }else {

                    if(key == ' ') {
                        if(_cursorPosition != 0 && _text.charAt(_cursorPosition - 1) != ' ') {
                            _text = _text.substring(0, _cursorPosition) + key + _text.substring(_cursorPosition);
                            _cursorPosition++;
                        }
                    } else {
                        _text = _text.substring(0, _cursorPosition) + key + _text.substring(_cursorPosition);
                        _cursorPosition++;
                    }

                }

            }
        }




    }

    public void update(){

    }

    public void draw() {

        // draw rect
        Renderer.batch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float c = 47.0f/255.0f;
        Renderer.shapeRenderer.setColor(c, c, c, 1.0f);
        Renderer.shapeRenderer.rect(_rect.x, _rect.y, _rect.width, _rect.height);
        Renderer.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.batch.begin();

        // draw text
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.descriptionFont, _text);

        float textWidth = layout.width;
        float textHeight = Font.descriptionFont.getCapHeight();

        float x = _rect.x + (_rect.height - textHeight)/2;
        float y = _rect.y + (_rect.height + textHeight)/2;
        if(_text != "")
            Font.descriptionFont.draw(Renderer.batch, _text, x, y);
        else if(_state == TextInputState.Idle)
            Font.descriptionFont.draw(Renderer.batch, _messageText, x, y);

        // draw cursor
        if(_state == TextInputState.TextEntered){
            if((int)(Time.currentTime*3) % 2 == 0 ) {
                Renderer.batch.end();
                Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                Renderer.shapeRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
                int cursorHgh = (int)_rect.height * 3 / 4;
                Renderer.shapeRenderer.rect(x+textWidth, _rect.y+(_rect.height-cursorHgh)/2, 6, cursorHgh);
                Renderer.shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                Renderer.batch.begin();
            }
        }
    }
}
