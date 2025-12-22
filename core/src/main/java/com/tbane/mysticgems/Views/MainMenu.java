package com.tbane.mysticgems.Views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tbane.mysticgems.AssetsManager;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.Game.Game;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.SoundManager;
import com.tbane.mysticgems.GUI.Button;
import com.tbane.mysticgems.GUI.ButtonWithText;

public class MainMenu extends Layout {
    private Texture _texture;
    private ArrayList<ButtonWithText> _buttons;


    public MainMenu() {
        super();

        _texture = new Texture("libgdx.png");

        ArrayList<String> btnTexts = new ArrayList<>();

        btnTexts.add("new game");
        btnTexts.add("highscores");
        btnTexts.add("instructions");
        btnTexts.add("about");
        btnTexts.add("exit");

        int btnWdt = 512;
        int btnHgh = 96;
        int padding = 32;

        _buttons = new ArrayList<>();
        int logoSize = 320;
        int logoPadding = 32;
        int hgh = -logoSize + logoPadding + btnTexts.size()*btnHgh - (btnTexts.size()-1)*padding;
        int start_y = Renderer.VIRTUAL_HEIGHT/2 + hgh/2;

        for(int i=0;i<btnTexts.size();i+=1) {
            ButtonWithText btn = new ButtonWithText (
                btnTexts.get(i),
                AssetsManager.getTexture("tex/menuButtonNormal.png"),
                AssetsManager.getTexture("tex/menuButtonHover.png"),
                AssetsManager.getTexture("tex/menuButtonPressed.png"),
                (Renderer.VIRTUAL_WIDTH-btnWdt)/2,
                start_y - i*btnHgh - i*padding,
                btnWdt,
                btnHgh
		    );
            _buttons.add(btn);
        }

        _buttons.get(0).onclick_func = () -> {
            SoundManager.stopAll();
            MyInput.processor.reset();
            LayoutsManager.array.add(new Game());
        };

        _buttons.get(1).onclick_func = () -> {
            LayoutsManager.array.add(new Highscores());
        };

        _buttons.get(2).onclick_func = () -> {
            LayoutsManager.array.add(new Instructions());
        };

        _buttons.get(3).onclick_func = () -> {
            LayoutsManager.array.add(new About());
        };

        _buttons.get(4).onclick_func = () -> {
            Gdx.app.exit();
            System.exit(0);
        };

        SoundManager.playMenuMusic();
    }
    @Override
    public void handleEvents() {
        for(Button btn : _buttons){
            btn.handleEvents();
        }
    }

    @Override
    public void update() {

        for(Button btn : _buttons){
            btn.update();
        }
    }
    @Override
    public void draw() {

        // draw the wooden background
        Sprite background = new Sprite(AssetsManager.getTexture("tex/mainBoard.png"));
        background.setPosition(0,0 );
        background.draw(Renderer.batch);

        // draw the big Gem
        Sprite gem = new Sprite(AssetsManager.getTexture("tex/red.png"));
        gem.setSize(384,384);
        gem.setOriginCenter();
        Vector2 pos = new Vector2();
        pos.x = Renderer.VIRTUAL_WIDTH/2.0f;
        pos.y = _buttons.get(0)._rect.y + _buttons.get(0)._rect.height + 320 + 32;
        gem.setCenter(pos.x, pos.y);
        gem.draw(Renderer.batch);



        // draw "Main Menu"
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.titleFont, "Main Menu");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f;
        float y = _buttons.get(0)._rect.y + _buttons.get(0)._rect.height + 112;

        Sprite frame = new Sprite(AssetsManager.getTexture("tex/titleFrame.png"));
        frame.setOriginCenter();
        frame.setCenter(x + textWidth/2.0f, y-textHeight/2.0f);
        frame.draw(Renderer.batch);

        Font.titleFont.draw(Renderer.batch, "Main Menu", x, y);

        Sprite leftSign = new Sprite(AssetsManager.getTexture("tex/mainMenuLeftSign.png"));
        leftSign.setOriginCenter();
        leftSign.setCenter(x - 80, y-textHeight/2.0f);
        leftSign.draw(Renderer.batch);

        Sprite rightSign = new Sprite(AssetsManager.getTexture("tex/mainMenuRightSign.png"));
        rightSign.setOriginCenter();
        rightSign.setCenter(x + textWidth + 80, y-textHeight/2.0f);
        rightSign.draw(Renderer.batch);

        // draw the buttons
        for(Button btn : _buttons){
            btn.draw();
        }
    }
}
