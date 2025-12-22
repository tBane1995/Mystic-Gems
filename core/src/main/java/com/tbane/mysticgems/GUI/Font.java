package com.tbane.mysticgems.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class Font {

    public static BitmapFont titleFont, buttonFont;
    public static BitmapFont descriptionFont;
    public static BitmapFont gameTopTextFont, ComboTextValue, ComboTextMiss, GameCommentFont;

    public static BitmapFont rankingTitleFont, rankingFont;
    static {

        buttonFont = generateFont("fonts/ScienceGothic.ttf", 40,1,1,1);
        titleFont = generateFont("fonts/ScienceGothic.ttf", 56,1,1,1);

        gameTopTextFont = generateFont("fonts/ScienceGothic.ttf", 48, 1,1,1);
        GameCommentFont = generateFont("fonts/ScienceGothic.ttf", 40, 1,1,1);
        ComboTextValue = generateFont("fonts/ScienceGothic.ttf", 32, 1,1,1);
        ComboTextMiss = generateFont("fonts/ScienceGothic.ttf", 32, 1,47f/255f,47f/255f);

        descriptionFont = generateFont("fonts/ScienceGothic.ttf", 32, 1,1,1);

        rankingTitleFont = generateFont("fonts/ScienceGothic.ttf", 40, 1,1,1);
        rankingFont = generateFont("fonts/ScienceGothic.ttf", 32, 1,1,1);


    }

    public static BitmapFont generateFont(String path, int size, float r, float g, float b){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        BitmapFont font = generator.generateFont(parameter);
        font.setColor(r, g, b, 1.0f);
        return font;
    }
}
