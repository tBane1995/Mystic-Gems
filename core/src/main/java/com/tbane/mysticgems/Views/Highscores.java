package com.tbane.mysticgems.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tbane.mysticgems.AssetsManager;
import com.tbane.mysticgems.GUI.Button;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Renderer;


import java.util.ArrayList;

public class Highscores extends Layout {

    private Button _backBtn;
    private int panelWdt, panelHgh;
    private int padding;
    private int innerWdt, innerHgh;
    private ArrayList<String> _names;
    private ArrayList<Integer> _points;
    private ArrayList<Integer> _combos;

    public Highscores() {
        super();

        _backBtn = new Button(
            AssetsManager.getTexture("tex/backButtonNormal.png"),
            AssetsManager.getTexture("tex/backButtonHover.png"),
            AssetsManager.getTexture("tex/backButtonPressed.png"),
            32, Renderer.VIRTUAL_HEIGHT - 96 - 32, 96, 96
        );

        _backBtn.onclick_func = () -> { LayoutsManager.pop_back(); };

        panelWdt = 720;
        panelHgh = 1356;
        padding = 32;
        innerWdt = panelWdt - 2 * padding;
        innerHgh = panelHgh - 2 * padding;

        Preferences prefs = Gdx.app.getPreferences("highscores");

        _names = new ArrayList<String>();
        _points = new ArrayList<Integer>();
        _combos = new ArrayList<Integer>();

        for(int i=0;i<15;i+=1){
            _names.add(prefs.getString("name" + i, "-"));
            _points.add(prefs.getInteger("point" + i, -1));
            _combos.add(prefs.getInteger("combo" + i, -1));
        }
    }

    @Override
    public void handleEvents() {
        _backBtn.handleEvents();

        if(MyInput.processor.isBackPressed()){
            LayoutsManager.pop_back();
            MyInput.processor.reset();
        }


    }

    @Override
    public void update() {
        _backBtn.update();
    }

    @Override
    public void draw() {
        Sprite background = new Sprite(AssetsManager.getTexture("tex/mainBoard.png"));
        background.setPosition(0,0 );
        background.draw(Renderer.batch);

        _backBtn.draw();

        // draw text "Instructions"
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.titleFont, "Highscores");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f + 32;
        float y = _backBtn._rect.y + _backBtn._rect.height/2.0f;

        Sprite frame = new Sprite(AssetsManager.getTexture("tex/titleFrame.png"));
        frame.setOriginCenter();
        frame.setCenter(x + textWidth/2.0f, y);
        frame.draw(Renderer.batch);

        Font.titleFont.draw(Renderer.batch, "Highscores", x, y+textHeight/2.0f);

        // draw a panel
        Sprite panel = new Sprite(AssetsManager.getTexture("tex/panel.png"));
        float panelX = padding;
        float panelY = Renderer.VIRTUAL_HEIGHT - panel.getTexture().getHeight() - (96 + 2 * padding);
        panel.setPosition(panelX, panelY);
        panel.draw(Renderer.batch);

        // draw a award cup
        Sprite awardCup = new Sprite(AssetsManager.getTexture("tex/awardCup.png"));
        awardCup.setOriginCenter();
        awardCup.setScale(0.25f);
        awardCup.setCenter(Renderer.VIRTUAL_WIDTH/2.0f, Renderer.VIRTUAL_HEIGHT-panelY - 288);
        awardCup.draw(Renderer.batch);



        // parameters for draw a list
        float start_y1 = panelY + panel.getHeight() - 288 - 128 + 20;
        float start_y2 = panelY + panel.getHeight() - 288 - 128 - 32 - Font.rankingTitleFont.getCapHeight();
        float dy = 24;
        float line_height = 48;
        float x1 = 64;
        float x2 = 256+16;
        float x3 = 480;


        // draw a shadow rectangles
        Renderer.batch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float c = 47.0f/255.0f;
        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Renderer.shapeRenderer.setColor(c, c, c, 0.9f);
        float wdt = panelWdt - 2 * x1 + 2 * 16;
        float hgh = Font.rankingTitleFont.getLineHeight() + dy + 15 * line_height + 78;
        Renderer.shapeRenderer.rect(x1 - 16, start_y1 -Font.rankingTitleFont.getLineHeight() , wdt, Font.rankingTitleFont.getLineHeight());
        Renderer.shapeRenderer.rect(x1 - 16, start_y2 - hgh + 16, wdt, hgh);
        Renderer.shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.batch.begin();

        // draw a highscore list
        float wdt1, wdt2, wdt3;
        layout = new GlyphLayout();
        layout.setText(Font.rankingTitleFont, "HIGHSCORES");
        wdt = layout.width;
        layout.setText(Font.rankingTitleFont, "NAME");
        wdt1 = layout.width;
        layout.setText(Font.rankingTitleFont, "POINTS");
        wdt2 = layout.width;
        layout.setText(Font.rankingTitleFont, "COMBO");
        wdt3 = layout.width;

        Font.rankingTitleFont.draw(Renderer.batch, "HIGHSCORES", (Renderer.VIRTUAL_WIDTH-wdt)/2.0f, start_y1 - Font.rankingTitleFont.getLineHeight()/4);
        Font.rankingTitleFont.draw(Renderer.batch, "NAME", x1, start_y2);
        Font.rankingTitleFont.draw(Renderer.batch, "POINTS", x2, start_y2);
        Font.rankingTitleFont.draw(Renderer.batch, "COMBO", x3, start_y2);

        for(int i=0;i<15;i+=1){

            layout = new GlyphLayout();
            textHeight = Font.rankingFont.getCapHeight();

            layout.setText(Font.rankingFont, _names.get(i));
            textWidth = layout.width;
            Font.rankingFont.draw(Renderer.batch, _names.get(i), x1+(wdt1-textWidth)/2, start_y2 - dy - line_height*(i+1));

            String value;
            if(_points.get(i) == -1)
                value = "-";
            else
                value = Integer.toString(_points.get(i));

            layout.setText(Font.rankingFont, value);
            textWidth = layout.width;
            Font.rankingFont.draw(Renderer.batch, value, x2+(wdt2-textWidth)/2, start_y2 - dy - line_height*(i+1));

            if(_points.get(i) == -1)
                value = "-";
            else
                value = Integer.toString(_combos.get(i));

            layout.setText(Font.rankingFont, value);
            textWidth = layout.width;
            Font.rankingFont.draw(Renderer.batch, value, x3+(wdt3-textWidth)/2, start_y2 - dy  - line_height*(i+1));
        }



    }
}
