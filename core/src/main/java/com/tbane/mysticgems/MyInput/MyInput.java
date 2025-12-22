package com.tbane.mysticgems.MyInput;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class MyInput {
    public static MyInputProcessor processor;

    public MyInput(OrthographicCamera camera){
        processor = new MyInputProcessor(camera);
    }
}
