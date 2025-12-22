package com.tbane.mysticgems.Views;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tbane.mysticgems.AssetsManager;
import com.tbane.mysticgems.GUI.Button;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Renderer;

public class Instructions extends Layout {

    private Button _backBtn;
    private int panelWdt, panelHgh;
    private int padding;
    private int innerWdt, innerHgh;
    private String _text;


    public Instructions() {
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

        _text = "Mystic Gems\n"
            + "\n"
            + "Your goal is to stop the gems\nfrom reaching the bottom\nof the screen.\n"
            + "\n"
            + "- Gems keep falling from the top.\n"
            + "- Click on the gems to destroy\nthem and gain points.\n"
            + "- If any gem reaches the bottom\nof the screen, you lose.\n"
            + "- Some gems are special and\ngive bonuses, for example:\n"
            + "  * Destroying all visible gems.\n"
            + "  * Destroying all visible gems\nof a specific type.\n"
            + "  * Additional score points.\n"
            + "\n"
            + "Combos:\n"
            + "- Destroy gems,\none after another, to build\na combo.\n"
            + "- Higher combo chains grant\nextra bonus points.\n"
            + "\n"
            + "React quickly, aim carefully,\nand try to get as many\npoints as possible!";
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
        layout.setText(Font.titleFont, "Instructions");

        float textWidth = layout.width;
        float textHeight = Font.titleFont.getCapHeight();

        float x = Renderer.VIRTUAL_WIDTH/2.0f - textWidth / 2f + 32;
        float y = _backBtn._rect.y + _backBtn._rect.height/2.0f;

        Sprite frame = new Sprite(AssetsManager.getTexture("tex/titleFrame.png"));
        frame.setOriginCenter();
        frame.setCenter(x + textWidth/2.0f, y);
        frame.draw(Renderer.batch);

        Font.titleFont.draw(Renderer.batch, "Instructions", x, y+textHeight/2.0f);


        // draw a panel
        Sprite panel = new Sprite(AssetsManager.getTexture("tex/panel.png"));
        float panelX = padding;
        float panelY = Renderer.VIRTUAL_HEIGHT - panel.getTexture().getHeight() - (96 + 2 * padding);
        panel.setPosition(panelX, panelY);
        panel.draw(Renderer.batch);

        Font.descriptionFont.draw(Renderer.batch, _text, panelX + padding, Renderer.VIRTUAL_HEIGHT - (96 + 2 * padding) - padding);
    }
}
