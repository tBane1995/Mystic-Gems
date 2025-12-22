package com.tbane.mysticgems;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.MyInput.MyInputProcessor;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Views.LayoutsManager;
import com.tbane.mysticgems.GUI.GUI;
import com.tbane.mysticgems.Views.MainMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class main extends ApplicationAdapter {

    private float FPStimer;
    private float FPScount;
    private String FPStext;

    @Override
    public void create() {

        // create camera - when delete this line then touch not work
        Renderer.camera = new OrthographicCamera();

        // send the events to MyInput
        Gdx.input.setCatchKey(Input.Keys.BACK, true);


        // set the input processor
        MyInput.processor = new MyInputProcessor(Renderer.camera);
        Gdx.input.setInputProcessor(MyInput.processor);

        // create main Layout - MainMenu
        LayoutsManager.array.add(new MainMenu());

        FPStimer = Time.currentTime;
        FPScount = 0;
        FPStext = "0";
    }

    @Override
    public void render() {

        Time.update();

        if(Time.currentTime - FPStimer > 1){
            FPStimer = Time.currentTime;
            FPStext = Float.toString(FPScount);
            FPScount = 0;
        }

        FPScount += 1;

        if(MyInput.processor.isTouchDown() || MyInput.processor.isTouchUp() || MyInput.processor.isTouchMoved() || MyInput.processor.isBackPressed() || MyInput.processor.isKeyTyped() ){
            GUI.ElementGUI_hovered = null;
            LayoutsManager.back().handleEvents();
            // reset the Events
            MyInput.processor.reset();
        }



        LayoutsManager.back().update();

        ScreenUtils.clear(0, 0, 0, 0);
        Renderer.begin();
        LayoutsManager.back().draw();
        Font.descriptionFont.draw(Renderer.batch, FPStext, 16, 16+Font.descriptionFont.getCapHeight());
        Renderer.end();
    }

    @Override
    public void dispose() {

    }
}
