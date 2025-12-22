package com.tbane.mysticgems.Game;

public final class GemColor {
    public float r,g,b;

    public GemColor(){
        r = g = b = 1.0f;
    }

    public GemColor(float r, float g, float b) {

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static GemColor rgb(float h, float s, float v) {
        h = (h % 360f + 360f) % 360f;

        float c = v * s;
        float x = c * (1f - Math.abs((h / 60f) % 2f - 1f));
        float m = v - c;

        float r, g, b;
        if (h < 60) {
            r = c; g = x; b = 0;
        } else if (h < 120) {
            r = x; g = c; b = 0;
        } else if (h < 180) {
            r = 0; g = c; b = x;
        } else if (h < 240) {
            r = 0; g = x; b = c;
        } else if (h < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        return new GemColor(r + m, g + m, b + m);
    }

    public static float pulse(float time, float speed, float min, float max) {
        float s = (float)Math.sin(time * speed);
        return min + (max - min) * (s * 0.5f + 0.5f);
    }

    public static GemColor destroyAll(float time) {
        float hue = (time * 60f) % 360f;
        return rgb(hue, 1f, 1f);
    }

    public static GemColor destroyType(float time) {
        float v = pulse(time, 6f, 0.5f, 1f);
        return rgb(0f, 1f, v);
    }

    public static GemColor bonusPoints(float time) {
        float v = pulse(time, 5f, 0.7f, 1f);
        return rgb(45f, 1f, v);
    }

    public static GemColor neutral() {
        return new GemColor(47f / 255f, 47f / 255f, 47f / 255f);
    }
}
