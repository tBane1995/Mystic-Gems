package com.tbane.mysticgems.Game;

import java.util.concurrent.atomic.AtomicReference;

public class GemColor {
    public float r, g, b;
    public GemColor(float r, float g, float b) {

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static GemColor rgb(float h, float s, float v) {
        h = (h % 360.0f + 360.0f) % 360f;

        float c = v * s;
        float x = c * (1f - Math.abs((h / 60.0f) % 2.0f - 1.0f));
        float m = v - c;

        float r, g, b;
        if (h < 60.0f) {
            r = c;
            g = x;
            b = 0;
        } else if (h < 120.0f) {
            r = x;
            g = c;
            b = 0;
        } else if (h < 180.0f) {
            r = 0;
            g = c;
            b = x;
        } else if (h < 240.0f) {
            r = 0;
            g = x;
            b = c;
        } else if (h < 300.0f) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }
        GemColor color = new GemColor(r + m, g + m, b + m);
        return color;
    }
}
