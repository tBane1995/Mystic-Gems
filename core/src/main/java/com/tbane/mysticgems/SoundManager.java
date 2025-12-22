package com.tbane.mysticgems;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
public class SoundManager {
    public static Music menuMusic;
    public static Music gameMusic;

    public static Sound hit;
    public static Sound miss;

    static {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("msc/Kesha - your love is my drug  8 bit  (Slowed + bass boosted).ogg"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(1.0f);

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("msc/xDeviruchi - Title Theme.ogg"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(1.0f);

        hit = Gdx.audio.newSound(Gdx.files.internal("msc/56_Attack_03.ogg"));
        miss = Gdx.audio.newSound(Gdx.files.internal("msc/033_Denied_03.ogg"));
    }


    public static void stopAll() {
        menuMusic.stop();
        gameMusic.stop();

    }

    public static void playMenuMusic(){
        menuMusic.play();
    }

    public static void playGameMusic(){
        gameMusic.play();
    }

    public static void playHit(){
        hit.play();
    }

    public static void playMiss(){
        miss.play();
    }
}
