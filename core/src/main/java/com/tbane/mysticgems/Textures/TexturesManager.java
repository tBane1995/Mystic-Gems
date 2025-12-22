package com.tbane.mysticgems.Textures;

import java.util.ArrayList;
import java.util.List;

public class TexturesManager {
    private static List<Tex> array;

    static {
        array = new ArrayList<>();

        loadTexture("tex/topBoard.png");
        loadTexture("tex/bottomBoard.png");
        loadTexture("tex/mainBoard.png");
        loadTexture("tex/panel.png");
        loadTexture("tex/smallPanel.png");

        loadTexture("tex/titleFrame.png");

        loadTexture("tex/mainMenuLeftSign.png");
        loadTexture("tex/mainMenuRightSign.png");

        loadTexture("tex/menuButtonNormal.png");
        loadTexture("tex/menuButtonHover.png");
        loadTexture("tex/menuButtonPressed.png");

        loadTexture("tex/panelButtonNormal.png");
        loadTexture("tex/panelButtonHover.png");
        loadTexture("tex/panelButtonPressed.png");

        loadTexture("tex/backButtonNormal.png");
        loadTexture("tex/backButtonHover.png");
        loadTexture("tex/backButtonPressed.png");

        loadTexture("tex/awardCup.png");

        loadTexture("tex/underwater.png");

        loadTexture("tex/bubble.png");
        loadTexture("tex/red.png");
        loadTexture("tex/green.png");
        loadTexture("tex/blue.png");
        loadTexture("tex/cyan.png");
        loadTexture("tex/magenta.png");
        loadTexture("tex/yellow.png");

    }

    private static void loadTexture(String pathfile){
        Tex tex = new Tex(pathfile);
        array.add(tex);
    }

    public static Tex getTexture(String pathfile){
        for(Tex tex : array){
            if(tex.path.equals(pathfile))
                return tex;
        }

        return null;
    }

    public static Tex getTexture(int colorIndex){
        Tex texture;

        switch(colorIndex){
            case 0:
                texture = getTexture("tex/red.png");
                break;
            case 1:
                texture = getTexture("tex/green.png");
                break;
            case 2:
                texture = getTexture("tex/blue.png");
                break;
            case 3:
                texture = getTexture("tex/yellow.png");;
                break;
            case 4:
                texture = getTexture("tex/magenta.png");
                break;
            case 5:
                texture = getTexture("tex/cyan.png");
                break;
            default:
                texture = null;
                break;
        };

        return texture;
    }

}
