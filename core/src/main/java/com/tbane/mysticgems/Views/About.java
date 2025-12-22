package com.tbane.mysticgems.Views;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tbane.mysticgems.GUI.Button;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.Textures.TexturesManager;

public class About extends Layout {

    private Button _backBtn;
    private int panelWdt, panelHgh;
    private int padding;
    private int innerWdt, innerHgh;
    private String _text;

    public About() {
        super();

        _backBtn = new Button(
            TexturesManager.getTexture("tex/backButtonNormal.png"),
            TexturesManager.getTexture("tex/backButtonHover.png"),
            TexturesManager.getTexture("tex/backButtonPressed.png"),
            32, Renderer.VIRTUAL_HEIGHT - 96 - 32, 96, 96
        );

        _backBtn.onclick_func = () -> { LayoutsManager.pop_back(); };

        panelWdt = 720;
        panelHgh = 1356;
        padding = 32;
        innerWdt = panelWdt - 2 * padding;
        innerHgh = panelHgh - 2 * padding;

        _text = "Mystic Gems v1.0\n"
            + "\n"
            + "Game programmed by tBane\n"
            + "\n"
            + "Graphics:\n"
            + "Gems - Indigo Tiger\n"
            + "Bubbles - Moonflower Carnivore\n"
            + "Underwater - freedesignfile\n"
            + "Panels - Sebastian Jaksan\n"
            + "Award Cup - Ninoshik\n"
            + "\n"
            + "Music & Sound Effects:\n"
            + "Game - xDeviruchi\n"
            + "Menu - trapskull3103\n"
            + "Miss&Hit - leohpaz \n";
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
        Sprite background = new Sprite(TexturesManager.getTexture("tex/mainBoard.png").texture);
        background.setPosition(0,0 );
        background.draw(Renderer.batch);

        _backBtn.draw();

        // draw text "About"

        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.titleFont, "About");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f + 32;
        float y = _backBtn._rect.y + _backBtn._rect.height/2.0f;

        Sprite frame = new Sprite(TexturesManager.getTexture("tex/titleFrame.png").texture);
        frame.setOriginCenter();
        frame.setCenter(x + textWidth/2.0f, y);
        frame.draw(Renderer.batch);

        Font.titleFont.draw(Renderer.batch, "About", x, y+textHeight/2.0f);


        // draw a panel
        Sprite panel = new Sprite(TexturesManager.getTexture("tex/panel.png").texture);
        float panelX = padding;
        float panelY = Renderer.VIRTUAL_HEIGHT - panel.getTexture().getHeight() - (96 + 2 * padding);
        panel.setPosition(panelX, panelY);
        panel.draw(Renderer.batch);

        Font.descriptionFont.draw(Renderer.batch, _text, panelX + padding, Renderer.VIRTUAL_HEIGHT - (96 + 2 * padding) - padding);

    }
}
