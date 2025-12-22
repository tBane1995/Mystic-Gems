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

        // MENU
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        buttonFont = generator.generateFont(parameter);
        generator.dispose();

        FreeTypeFontGenerator generator4 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter4 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter4.size = 56;
        parameter4.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        titleFont = generator4.generateFont(parameter4);
        generator4.dispose();

        /// /////////////////////////////////
        // GAME
        FreeTypeFontGenerator generator3 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 48;
        parameter3.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        gameTopTextFont = generator3.generateFont(parameter3);
        generator3.dispose();

        FreeTypeFontGenerator generator5 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter5 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter5.size = 40;
        parameter5.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        GameCommentFont = generator5.generateFont(parameter5);
        generator5.dispose();

        FreeTypeFontGenerator generator6 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter6 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter6.size = 32;
        parameter6.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        ComboTextValue = generator6.generateFont(parameter6);
        ComboTextValue.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        generator6.dispose();

        FreeTypeFontGenerator generator7= new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter7 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter7.size = 32;
        parameter7.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        ComboTextMiss = generator7.generateFont(parameter5);
        float c = 47.0f/255.0f;
        ComboTextMiss.setColor(1.0f, c, c, 1.0f);
        generator7.dispose();

        /// ////////////////////////////////
        // INSTRUCTIONS & ABOUT

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 32;
        parameter2.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        descriptionFont = generator2.generateFont(parameter2);
        generator2.dispose();

        /// ////////////////////////////////
        // RANKING
        FreeTypeFontGenerator generator9 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter9 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter9.size = 40;
        parameter9.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        rankingTitleFont = generator9.generateFont(parameter9);
        generator9.dispose();

        FreeTypeFontGenerator generator8 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ScienceGothic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter8 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter8.size = 32;
        parameter8.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśżźĄĆĘŁŃÓŚŻŹ";
        rankingFont = generator8.generateFont(parameter8);
        generator8.dispose();

    }
}
